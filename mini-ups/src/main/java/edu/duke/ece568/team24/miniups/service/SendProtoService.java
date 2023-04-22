package edu.duke.ece568.team24.miniups.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import edu.duke.ece568.team24.miniups.protobuf.amazonups.Err;
import edu.duke.ece568.team24.miniups.protobuf.amazonups.UACommands;
import edu.duke.ece568.team24.miniups.protobuf.amazonups.UAConnectedToWorld;
import edu.duke.ece568.team24.miniups.protobuf.amazonups.UADestinationUpdated;
import edu.duke.ece568.team24.miniups.protobuf.amazonups.UAOrderDelivered;
import edu.duke.ece568.team24.miniups.protobuf.amazonups.UAOrderDeparture;
import edu.duke.ece568.team24.miniups.protobuf.amazonups.UATruckArrived;
import edu.duke.ece568.team24.miniups.protobuf.worldups.UCommands;
import edu.duke.ece568.team24.miniups.protobuf.worldups.UDeliveryLocation;
import edu.duke.ece568.team24.miniups.protobuf.worldups.UGoDeliver;
import edu.duke.ece568.team24.miniups.protobuf.worldups.UGoPickup;
import edu.duke.ece568.team24.miniups.protobuf.worldups.UQuery;

@Service
public class SendProtoService {

    private static final Logger logger = LoggerFactory.getLogger(SendProtoService.class);

    private AtomicLong seqNumCounter = new AtomicLong(0);

    private Map<Long, UGoPickup> goPickupMap = new ConcurrentHashMap<>();
    private Map<Long, UGoDeliver> goDeliverMap = new ConcurrentHashMap<>();
    private Map<Long, UQuery> queryMap = new ConcurrentHashMap<>();
    private List<Long> ackToWorld = new CopyOnWriteArrayList<>();

    private Map<Long, UAConnectedToWorld> connectedToWorldMap = new ConcurrentHashMap<>();
    private Map<Long, UADestinationUpdated> destinationUpdatedMap = new ConcurrentHashMap<>();
    private Map<Long, UATruckArrived> truckArrivedMap = new ConcurrentHashMap<>();
    private Map<Long, UAOrderDeparture> orderDepartureMap = new ConcurrentHashMap<>();
    private Map<Long, UAOrderDelivered> orderDeliveredMap = new ConcurrentHashMap<>();
    private Map<Long, Err> errMap = new ConcurrentHashMap<>();
    private List<Long> ackToAmazon = new CopyOnWriteArrayList<>();

    public void removeMsgByACK(Long ack) {
        if (goPickupMap.containsKey(ack)) {
            goPickupMap.remove(ack);
        } else if (goDeliverMap.containsKey(ack)) {
            goDeliverMap.remove(ack);
        } else if (queryMap.containsKey(ack)) {
            queryMap.remove(ack);
        } else if (connectedToWorldMap.containsKey(ack)) {
            connectedToWorldMap.remove(ack);
        } else if (destinationUpdatedMap.containsKey(ack)) {
            destinationUpdatedMap.remove(ack);
        } else if (truckArrivedMap.containsKey(ack)) {
            truckArrivedMap.remove(ack);
        } else if (orderDepartureMap.containsKey(ack)) {
            orderDepartureMap.remove(ack);
        } else if (orderDeliveredMap.containsKey(ack)) {
            orderDeliveredMap.remove(ack);
        } else if (errMap.containsKey(ack)) {
            errMap.containsKey(ack);
        } else {
            logger.error("invalid ACK =" + ack);
        }
    }

    public void postUGoPickup(int truckid, int whid) {
        Long seqNum = seqNumCounter.incrementAndGet();
        UGoPickup goPickup = UGoPickup.newBuilder().setSeqnum(seqNum).setTruckid(truckid).setWhid(whid).build();
        goPickupMap.put(seqNum, goPickup);
    }

    public void postUGoUGoDeliver(int truckid, int whid, int packageid, int x, int y) {
        Long seqNum = seqNumCounter.incrementAndGet();
        UDeliveryLocation deliveryLocation = UDeliveryLocation.newBuilder().setPackageid(packageid).setX(x).setY(y)
                .build();
        UGoDeliver goDeliver = UGoDeliver.newBuilder().setSeqnum(seqNum).setTruckid(truckid)
                .addAllPackages(List.of(deliveryLocation))
                .build();
        goDeliverMap.put(seqNum, goDeliver);
    }

    public void postUQuery(int truckid) {
        Long seqNum = seqNumCounter.incrementAndGet();
        UQuery query = UQuery.newBuilder().setSeqnum(seqNum).setTruckid(truckid).build();
        queryMap.put(seqNum, query);
    }

    public void postAckToWorld(Long ack) {
        ackToWorld.add(ack);
    }

    public void postUAConnectedToWorld(int worldid) {
        Long seqNum = seqNumCounter.incrementAndGet();
        UAConnectedToWorld connectedToWorld = UAConnectedToWorld.newBuilder().setSeqnum(seqNum).setWorldid(worldid)
                .build();
        connectedToWorldMap.put(seqNum, connectedToWorld);
    }

    public void postUADestinationUpdated(int orderid, int x, int y) {
        Long seqNum = seqNumCounter.incrementAndGet();
        UADestinationUpdated destinationUpdated = UADestinationUpdated.newBuilder().setSeqnum(seqNum)
                .setOrderid(orderid).setDestinationx(x).setDestinationy(y).build();
        destinationUpdatedMap.put(seqNum, destinationUpdated);
    }

    public void postUATruckArrived(int truckid, int whid) {
        Long seqNum = seqNumCounter.incrementAndGet();
        UATruckArrived truckArrived = UATruckArrived.newBuilder().setSeqnum(seqNum).setTruckid(truckid).setWhnum(whid)
                .build();
        truckArrivedMap.put(seqNum, truckArrived);
    }

    public void postUAOrderDeparture(int orderid, int packageid, int trackingnum) {
        Long seqNum = seqNumCounter.incrementAndGet();
        UAOrderDeparture orderDeparture = UAOrderDeparture.newBuilder().setSeqnum(seqNum).setOrderid(orderid)
                .setPackageid(packageid).setTrackingnum(trackingnum).build();
        orderDepartureMap.put(seqNum, orderDeparture);
    }

    public void postUAOrderDelivered(int packageid, int x, int y) {
        Long seqNum = seqNumCounter.incrementAndGet();
        UAOrderDelivered orderDelivered = UAOrderDelivered.newBuilder().setSeqnum(seqNum).setPackageid(packageid)
                .setDestinationx(x).setDestinationy(y).build();
        orderDeliveredMap.put(seqNum, orderDelivered);
    }

    public void postErr(String msg, int originseqnum) {
        Long seqNum = seqNumCounter.incrementAndGet();
        Err err = Err.newBuilder().setSeqnum(seqNum).setErr(msg).setOriginseqnum(originseqnum).build();
        errMap.put(seqNum, err);
    }

    public void postAckToAmazon(Long ack) {
        ackToAmazon.add(ack);
    }

    public void sendProtoToWorld(OutputStream toWorld) {
        UCommands.Builder cmdsBldr = UCommands.newBuilder();
        if (goPickupMap.size() > 0) {
            cmdsBldr.addAllPickups(goPickupMap.values());
        }
        if (goDeliverMap.size() > 0) {
            cmdsBldr.addAllDeliveries(goDeliverMap.values());
        }
        if (queryMap.size() > 0) {
            cmdsBldr.addAllQueries(queryMap.values());
        }
        if (ackToWorld.size() > 0) {
            cmdsBldr.addAllAcks(ackToWorld);
        }
        try {
            cmdsBldr.build().writeDelimitedTo(toWorld);
            ackToWorld.clear();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void sendProtoToAmazon(OutputStream toAmazon) {
        UACommands.Builder cmdsBldr = UACommands.newBuilder();
        if (connectedToWorldMap.size() > 0) {
            cmdsBldr.addAllConnectedtoworld(connectedToWorldMap.values());
        }
        if (destinationUpdatedMap.size() > 0) {
            cmdsBldr.addAllDestinationupdated(destinationUpdatedMap.values());
        }
        if (truckArrivedMap.size() > 0) {
            cmdsBldr.addAllTruckarrived(truckArrivedMap.values());
        }
        if (orderDepartureMap.size() > 0) {
            cmdsBldr.addAllOrderdeparture(orderDepartureMap.values());
        }
        if (orderDeliveredMap.size() > 0) {
            cmdsBldr.addAllOrderdelivered(orderDeliveredMap.values());
        }
        if (errMap.size() > 0) {
            cmdsBldr.addAllError(errMap.values());
        }
        if (ackToAmazon.size() > 0) {
            cmdsBldr.addAllAcks(ackToAmazon);
        }
        try {
            cmdsBldr.build().writeDelimitedTo(toAmazon);
            ackToAmazon.clear();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
