package edu.duke.ece568.team24.miniups.connection;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.duke.ece568.team24.miniups.protobuf.amazonups.*;
import edu.duke.ece568.team24.miniups.protobuf.worldups.*;

@Service
public class ProtoMsgParser {

    private static final Logger logger = LoggerFactory.getLogger(ProtoMsgParser.class);

    private ProtoMsgSender sendProtoService;

    private ConcurrentSkipListSet<Long> worldSeqNumCacheSet = new ConcurrentSkipListSet<>();
    private ConcurrentSkipListSet<Long> amazonSeqNumCacheSet = new ConcurrentSkipListSet<>();
    private final int worldSeqNumCacheSize;
    private final int amazonSeqNumCacheSize;

    public ProtoMsgParser(ProtoMsgSender service,
            @Value("${simworld.seqnum.cachesize}") String worldSize,
            @Value("${miniamazon.seqnum.cachesize}") String amazonSize) {
        this.sendProtoService = service;
        this.worldSeqNumCacheSize = Integer.parseInt(worldSize);
        this.amazonSeqNumCacheSize = Integer.parseInt(amazonSize);
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

    public void parseProtoFromWorld(UResponses worldResponses) {
        parseACKs(worldResponses.getAcksList());
        parseCompletions(worldResponses.getCompletionsList());
        parseDelivereds(worldResponses.getDeliveredList());
        parseTruckstatuses(worldResponses.getTruckstatusList());
        parseWorldErrors(worldResponses.getErrorList());
    }

    public void parseProtoFromAmazon(AUCommands amazonCmds) {
        parseACKs(amazonCmds.getAcksList());
        parseConnectedtoworlds(amazonCmds.getConnectedtoworldList());
        parseOrderCreateds(amazonCmds.getOrdercreatedList());
        parseRequestTrucks(amazonCmds.getRequesttruckList());
        parseOrderLoadeds(amazonCmds.getOrderloadedList());
        parseAmazonErrors(amazonCmds.getErrorList());
    }

    public void parseACKs(List<Long> acks) {
        acks.stream()
                .forEach(ack -> {
                    sendProtoService.removeMsgByACK(ack);
                    logger.debug("\nparseACKs(): " + ack.toString());
                });
    }

    public void parseCompletions(List<UFinished> completions) {
        completions.stream()
                .peek(c -> {
                    sendProtoService.postAckToWorld(c.getSeqnum());
                    logger.debug("\nparseCompletions(): " + c.toString());
                })
                .filter(c -> !worldSeqNumCacheSet.contains(c.getSeqnum())
                        && c.getStatus().equalsIgnoreCase("ARRIVE WAREHOUSE"))
                .forEach(c -> {
                    sendProtoService.postUATruckArrived(c.getTruckid(), 0);
                    addToWorldSeqNumCacheSet(c.getSeqnum());
                    logger.debug("\npostUATruckArrived");
                });
    }

    public void parseDelivereds(List<UDeliveryMade> delivereds) {
        delivereds.stream()
                .peek(d -> {
                    sendProtoService.postAckToWorld(d.getSeqnum());
                    logger.debug("\nparseDelivereds(): " + d.toString());
                })
                .filter(d -> !worldSeqNumCacheSet.contains(d.getSeqnum()))
                .forEach(d -> {
                    sendProtoService.postUAOrderDelivered(d.getPackageid(), 0, 0);
                    addToWorldSeqNumCacheSet(d.getSeqnum());
                    logger.debug("\npostUAOrderDelivered");
                });
    }

    public void parseTruckstatuses(List<UTruck> truckstatuses) {
        truckstatuses.stream()
                .peek(t -> {
                    sendProtoService.postAckToWorld(t.getSeqnum());
                    logger.debug("\nparseTruckstatuses(): " + t.toString());
                })
                .filter(t -> !worldSeqNumCacheSet.contains(t.getSeqnum()))
                .forEach(t -> {
                    addToWorldSeqNumCacheSet(t.getSeqnum());
                });
    }

    public void parseWorldErrors(List<UErr> worldErrors) {
        worldErrors.stream()
                .peek(err -> {
                    sendProtoService.postAckToWorld(err.getSeqnum());
                    logger.debug("\nparseWorldErrors(): " + err.toString());
                })
                .filter(err -> !worldSeqNumCacheSet.contains(err.getSeqnum()))
                .forEach(err -> {
                    addToWorldSeqNumCacheSet(err.getSeqnum());
                });
    }

    public void parseConnectedtoworlds(List<AUConnectedToWorld> connectedtoworlds) {
        connectedtoworlds.stream()
                .forEach(c -> {
                    sendProtoService.postAckToAmazon(c.getSeqnum());
                    logger.debug("\nparseConnectedtoworlds(): " + c.toString());
                });
    }

    public void parseOrderCreateds(List<AUOrderCreated> orderCreateds) {
        orderCreateds.stream()
                .peek(o -> {
                    sendProtoService.postAckToAmazon(o.getSeqnum());
                    logger.debug("\nparseOrderCreateds(): " + o.toString());
                })
                .filter(o -> !amazonSeqNumCacheSet.contains(o.getSeqnum()))
                .forEach(o -> {
                    addToAmazonSeqNumCacheSet(o.getSeqnum());
                });
    }

    public void parseRequestTrucks(List<AURequestTruck> requestTrucks) {
        requestTrucks.stream()
                .peek(r -> {
                    sendProtoService.postAckToAmazon(r.getSeqnum());
                    logger.debug("\nparseRequestTrucks(): " + r.toString());
                })
                .filter(r -> !amazonSeqNumCacheSet.contains(r.getSeqnum()))
                .forEach(r -> {
                    sendProtoService.postUGoPickup(1, r.getWhnum());
                    logger.debug("\npostUGoPickup()");
                    addToAmazonSeqNumCacheSet(r.getSeqnum());
                });
    }

    public void parseOrderLoadeds(List<AUOrderLoaded> orderLoadeds) {
        orderLoadeds.stream()
                .peek(ld -> {
                    sendProtoService.postAckToAmazon(ld.getSeqnum());
                    logger.debug("\nparseOrderLoadeds(): " + ld.toString());
                })
                .filter(ld -> !amazonSeqNumCacheSet.contains(ld.getSeqnum()))
                .forEach(ld -> {
                    sendProtoService.postUGoUGoDeliver(ld.getTruckid(), ld.getPackageid(), 1, 1);
                    logger.debug("\npostUGoUGoDeliver()");
                    addToAmazonSeqNumCacheSet(ld.getSeqnum());
                });
    }

    public void parseAmazonErrors(List<Err> amazonErrors) {
        amazonErrors.stream()
                .peek(err -> {
                    sendProtoService.postAckToAmazon(err.getSeqnum());
                    logger.debug("\nparseAmazonErrors(): " + err.toString());
                })
                .filter(err -> !amazonSeqNumCacheSet.contains(err.getSeqnum()))
                .forEach(err -> {
                    addToAmazonSeqNumCacheSet(err.getSeqnum());
                });
    }
}
