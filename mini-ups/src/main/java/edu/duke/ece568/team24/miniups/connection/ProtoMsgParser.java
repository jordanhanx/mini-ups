package edu.duke.ece568.team24.miniups.connection;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.duke.ece568.team24.miniups.dto.*;
import edu.duke.ece568.team24.miniups.protobuf.amazonups.*;
import edu.duke.ece568.team24.miniups.protobuf.worldups.*;
import edu.duke.ece568.team24.miniups.service.*;

@Service
public class ProtoMsgParser {

    private static final Logger logger = LoggerFactory.getLogger(ProtoMsgParser.class);

    public static String getCausedError(Exception e) {
        Throwable throwable = e;
        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        return throwable.getMessage();
    }

    private final ProtoMsgSender protoMsgSender;

    private final OrderService orderService;
    private final PackageService packageService;
    private final TruckService truckService;

    private final int worldSeqNumCacheSize;
    private final int amazonSeqNumCacheSize;

    private ConcurrentSkipListSet<Long> worldSeqNumCacheSet = new ConcurrentSkipListSet<>();
    private ConcurrentSkipListSet<Long> amazonSeqNumCacheSet = new ConcurrentSkipListSet<>();

    private final int requestTruckHistoryCacheSize;
    private ConcurrentSkipListMap<Long, AURequestTruck> requestTrucksHistoryMap = new ConcurrentSkipListMap<>();
    private List<AURequestTruck> requestTrucksRedoList = new CopyOnWriteArrayList<>();

    public ProtoMsgParser(ProtoMsgSender protoMsgSender, OrderService orderService,
            PackageService packageService, TruckService truckService,
            @Value("${simworld.seqnum.cachesize}") String worldSize,
            @Value("${miniamazon.seqnum.cachesize}") String amazonSize,
            @Value("${requesttruck.history.cachesize}") String requestTruckHistoryCacheSize) {
        this.protoMsgSender = protoMsgSender;
        this.orderService = orderService;
        this.packageService = packageService;
        this.truckService = truckService;
        this.worldSeqNumCacheSize = Integer.parseInt(worldSize);
        this.amazonSeqNumCacheSize = Integer.parseInt(amazonSize);
        this.requestTruckHistoryCacheSize = Integer.parseInt(requestTruckHistoryCacheSize);
    }

    public void addToWorldSeqNumCacheSet(long seqNum) {
        if (worldSeqNumCacheSet.size() > worldSeqNumCacheSize) {
            worldSeqNumCacheSet.pollFirst();
        }
        worldSeqNumCacheSet.add(seqNum);
    }

    public void addToAmazonSeqNumCacheSet(long seqNum) {
        if (amazonSeqNumCacheSet.size() > amazonSeqNumCacheSize) {
            amazonSeqNumCacheSet.pollFirst();
        }
        amazonSeqNumCacheSet.add(seqNum);
    }

    public void addToRequestTrucksHistory(long upsSeqNum, AURequestTruck req) {
        if (requestTrucksHistoryMap.size() > requestTruckHistoryCacheSize) {
            requestTrucksHistoryMap.pollFirstEntry();
        }
        requestTrucksHistoryMap.put(upsSeqNum, req);
    }

    public void parseProtoFromWorld(UResponses worldResponses) {
        parseACKs(worldResponses.getAcksList());
        parseTruckstatuses(worldResponses.getTruckstatusList()); // update truck status with priority
        redoRequestTrucksInRedoList();
        parseCompletions(worldResponses.getCompletionsList());
        parseDelivereds(worldResponses.getDeliveredList());
        parseWorldErrors(worldResponses.getErrorList());
        orderService.updateAllOrdersStatus();
    }

    public void parseProtoFromAmazon(AUCommands amazonCmds) {
        parseACKs(amazonCmds.getAcksList());
        parseConnectedToWorlds(amazonCmds.getConnectedtoworldList());
        parseOrderCreateds(amazonCmds.getOrdercreatedList());
        parseRequestTrucks(amazonCmds.getRequesttruckList());
        parseOrderLoadeds(amazonCmds.getOrderloadedList());
        parseAmazonErrors(amazonCmds.getErrorList());
        orderService.updateAllOrdersStatus();
    }

    public void parseACKs(List<Long> acks) {
        acks.stream()
                .forEach(ack -> {
                    protoMsgSender.removeMsgByACK(ack);
                });
        // logger.debug("[UPS]confirm ACKs:" + acks.toString());
    }

    public void parseTruckstatuses(List<UTruck> truckstatuses) {
        truckstatuses.stream()
                .peek(t -> {
                    protoMsgSender.postAckToWorld(t.getSeqnum());
                })
                .filter(t -> !worldSeqNumCacheSet.contains(t.getSeqnum()))
                .forEach(t -> {
                    try {
                        truckService.updateTruckStatus(t.getTruckid(), t.getX(), t.getY(), t.getStatus());
                        addToWorldSeqNumCacheSet(t.getSeqnum());
                    } catch (Exception e) {
                        logger.error(getCausedError(e));
                    }
                });
        logger.debug("[UPS]parseTruckstatuses(), update [" + truckstatuses.size() + "] trucks");
    }

    public void parseCompletions(List<UFinished> completions) {
        completions.stream()
                .peek(c -> {
                    protoMsgSender.postAckToWorld(c.getSeqnum());
                })
                .filter(c -> !worldSeqNumCacheSet.contains(c.getSeqnum()))
                .peek(c -> {
                    try {
                        truckService.updateTruckStatus(c.getTruckid(), c.getX(), c.getY(), c.getStatus());
                        addToWorldSeqNumCacheSet(c.getSeqnum());
                        logger.debug("[UPS]truckService.updateTruckStatus()");
                    } catch (Exception e) {
                        logger.error(getCausedError(e));
                    }
                })
                .filter(c -> c.getStatus().equalsIgnoreCase("ARRIVE WAREHOUSE"))
                .forEach(c -> {
                    try {
                        TruckDto truck = truckService.findById(c.getTruckid());
                        long upsSeqNum = protoMsgSender.postUATruckArrived(c.getTruckid(),
                                truck.getTargetWarehouseId());
                        logger.debug("[UPS]postUATruckArrived()[AMAZON], seqNum=" + upsSeqNum);
                    } catch (Exception e) {
                        logger.error(getCausedError(e));
                    }
                });
    }

    public void parseDelivereds(List<UDeliveryMade> delivereds) {
        delivereds.stream()
                .peek(d -> {
                    protoMsgSender.postAckToWorld(d.getSeqnum());
                })
                .filter(d -> !worldSeqNumCacheSet.contains(d.getSeqnum()))
                .forEach(d -> {
                    try {
                        packageService.updateStatus(d.getPackageid(), "delivered");
                        logger.debug("[UPS]packageService.updateStatus()");
                        PackageDto pack = packageService.findById(d.getPackageid());
                        long upsSeqNum = protoMsgSender.postUAOrderDelivered(pack.getId(), pack.getCurrX(),
                                pack.getCurrY());
                        logger.debug("[UPS]postUAOrderDelivered()[AMAZON], seqNum=" + upsSeqNum);
                        addToWorldSeqNumCacheSet(d.getSeqnum());
                    } catch (Exception e) {
                        logger.error(getCausedError(e));
                    }
                });
    }

    public void parseWorldErrors(List<UErr> worldErrors) {
        worldErrors.stream()
                .peek(err -> {
                    protoMsgSender.postAckToWorld(err.getSeqnum());
                })
                .filter(err -> !worldSeqNumCacheSet.contains(err.getSeqnum()))
                .peek(err -> {
                    logger.warn("[UPS]recvWorldErr\n" + err.toString());
                    addToWorldSeqNumCacheSet(err.getSeqnum());
                })
                .filter(err -> requestTrucksHistoryMap.containsKey(err.getOriginseqnum()))
                .forEach(err -> {
                    AURequestTruck req = requestTrucksHistoryMap.remove(err.getOriginseqnum());
                    requestTrucksRedoList.add(req);
                    logger.warn("[UPS]add AURequestTruck into redo list\n" + req.toString());
                });
    }

    public void parseConnectedToWorlds(List<AUConnectedToWorld> connectedToWorlds) {
        connectedToWorlds.stream()
                .forEach(c -> {
                    try {
                        protoMsgSender.postAckToAmazon(c.getSeqnum());
                        logger.debug("[UPS]ignore redundant AUConnectedToWorlds");
                    } catch (Exception e) {
                        String causedMsg = getCausedError(e);
                        logger.error(causedMsg);
                        protoMsgSender.postErr(causedMsg, c.getSeqnum());
                    }
                });
    }

    public void parseOrderCreateds(List<AUOrderCreated> orderCreateds) {
        orderCreateds.stream()
                .peek(o -> {
                    protoMsgSender.postAckToAmazon(o.getSeqnum());
                })
                .filter(o -> !amazonSeqNumCacheSet.contains(o.getSeqnum()))
                .forEach(o -> {
                    try {
                        String username = o.hasUpsaccount() ? o.getUpsaccount() : null;
                        orderService.createOrder(o.getOrderid(), o.getDestinationx(), o.getDestinationy(), username);
                        logger.debug("[UPS]orderService.createOrder()");
                        addToAmazonSeqNumCacheSet(o.getSeqnum());
                    } catch (Exception e) {
                        String causedMsg = getCausedError(e);
                        logger.error(causedMsg);
                        protoMsgSender.postErr(causedMsg, o.getSeqnum());
                    }
                });
    }

    public void redoRequestTrucksInRedoList() {
        List<AURequestTruck> redoSuccessList = requestTrucksRedoList
                .stream()
                .filter(r -> {
                    try {
                        TruckDto truck = truckService.assignATruckToWarehouse(r.getWhnum(), r.getX(), r.getY());
                        if (truck != null) {
                            long upsSeqNum = protoMsgSender.postUGoPickup(truck.getId(), r.getWhnum());
                            logger.debug("[UPS][Redo]postUGoPickup()[WORLD], seqNum=" + upsSeqNum);
                            addToRequestTrucksHistory(upsSeqNum, r);
                            logger.debug("[UPS][Redo]addToRequestTrucksHistory()");
                            return true;
                        } else {
                            logger.warn("[UPS][Redo]No available trucks now, redo next time");
                        }
                    } catch (Exception e) {
                        String causedMsg = getCausedError(e);
                        logger.error(causedMsg);
                        protoMsgSender.postErr(causedMsg, r.getSeqnum());
                    }
                    return false;
                }).toList();
        requestTrucksRedoList.removeAll(redoSuccessList);
    }

    public void parseRequestTrucks(List<AURequestTruck> requestTrucks) {
        requestTrucks.stream()
                .peek(r -> {
                    protoMsgSender.postAckToAmazon(r.getSeqnum());
                })
                .filter(r -> !amazonSeqNumCacheSet.contains(r.getSeqnum()))
                .forEach(r -> {
                    try {
                        TruckDto truck = truckService.assignATruckToWarehouse(r.getWhnum(), r.getX(), r.getY());
                        if (truck != null) {
                            long upsSeqNum = protoMsgSender.postUGoPickup(truck.getId(), r.getWhnum());
                            logger.debug("[UPS]postUGoPickup()[WORLD], seqNum=" + upsSeqNum);
                            addToRequestTrucksHistory(upsSeqNum, r);
                            logger.debug("[UPS]addToRequestTrucksHistory()");
                        } else {
                            requestTrucksRedoList.add(r);
                            logger.warn("[UPS]No available trucks now, redo next time");
                            logger.warn("[UPS]add AURequestTruck into redo list\n" + r.toString());
                        }
                        addToAmazonSeqNumCacheSet(r.getSeqnum());
                    } catch (Exception e) {
                        String causedMsg = getCausedError(e);
                        logger.error(causedMsg);
                        protoMsgSender.postErr(causedMsg, r.getSeqnum());
                    }
                });
    }

    public void parseOrderLoadeds(List<AUOrderLoaded> orderLoadeds) {
        orderLoadeds.stream()
                .peek(ld -> {
                    protoMsgSender.postAckToAmazon(ld.getSeqnum());
                })
                .filter(ld -> !amazonSeqNumCacheSet.contains(ld.getSeqnum()))
                .forEach(ld -> {
                    try {
                        OrderDto order = orderService.findById(ld.getOrderid());
                        TruckDto truck = truckService.findById(ld.getTruckid());
                        PackageDto newPack = packageService.createPackage(ld.getPackageid(), ld.getDescription(), order,
                                truck);
                        logger.debug("[UPS]packageService.createPackage()");
                        long upsSeqNumToWorld = protoMsgSender.postUGoDeliver(newPack.getTruckId(), newPack.getId(),
                                newPack.getDestinationX(), newPack.getDestinationY());
                        logger.debug("[UPS]postUGoUGoDeliver()[WORLD], seqNum=" + upsSeqNumToWorld);
                        long upsSeqNumToAmazon = protoMsgSender.postUAOrderDeparture(newPack.getOrderId(),
                                newPack.getId(), newPack.getTrackingNumber());
                        logger.debug("[UPS]postUAOrderDeparture()[AMAZON], seqNum=" + upsSeqNumToAmazon);
                        addToAmazonSeqNumCacheSet(ld.getSeqnum());
                    } catch (Exception e) {
                        String causedMsg = getCausedError(e);
                        logger.error(causedMsg);
                        protoMsgSender.postErr(causedMsg, ld.getSeqnum());
                    }
                });

    }

    public void parseAmazonErrors(List<Err> amazonErrors) {
        amazonErrors.stream()
                .peek(err -> {
                    protoMsgSender.postAckToAmazon(err.getSeqnum());
                })
                .filter(err -> !amazonSeqNumCacheSet.contains(err.getSeqnum()))
                .forEach(err -> {
                    logger.warn("[UPS]recvAmazonErrors\n" + err.toString());
                    addToAmazonSeqNumCacheSet(err.getSeqnum());
                });
    }
}
