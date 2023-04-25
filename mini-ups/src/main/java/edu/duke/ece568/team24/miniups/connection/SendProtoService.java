package edu.duke.ece568.team24.miniups.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import edu.duke.ece568.team24.miniups.protobuf.amazonups.*;
import edu.duke.ece568.team24.miniups.protobuf.worldups.*;

@Service
public class SendProtoService {

    private static final Logger logger = LoggerFactory.getLogger(SendProtoService.class);

    private AtomicLong seqNumCounter = new AtomicLong(0);

    private Map<Long, UGoPickup> goPickupMap = new ConcurrentHashMap<>();
    private Map<Long, UGoDeliver> goDeliverMap = new ConcurrentHashMap<>();
    private Map<Long, UQuery> queryMap = new ConcurrentHashMap<>();
    private Set<Long> ackToWorld = new CopyOnWriteArraySet<>();

    private Map<Long, UAConnectedToWorld> connectedToWorldMap = new ConcurrentHashMap<>();
    private Map<Long, UADestinationUpdated> destinationUpdatedMap = new ConcurrentHashMap<>();
    private Map<Long, UATruckArrived> truckArrivedMap = new ConcurrentHashMap<>();
    private Map<Long, UAOrderDeparture> orderDepartureMap = new ConcurrentHashMap<>();
    private Map<Long, UAOrderDelivered> orderDeliveredMap = new ConcurrentHashMap<>();
    private Map<Long, Err> errMap = new ConcurrentHashMap<>();
    private Set<Long> ackToAmazon = new CopyOnWriteArraySet<>();

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
            logger.warn(Thread.currentThread().getName() + "\n Not found Msg by ACK = " + ack);
        }
    }

    public void postUGoPickup(int truckid, int whid) {
        Long seqNum = seqNumCounter.incrementAndGet();
        UGoPickup goPickup = UGoPickup.newBuilder().setSeqnum(seqNum).setTruckid(truckid).setWhid(whid).build();
        goPickupMap.put(seqNum, goPickup);
    }

    public void postUGoUGoDeliver(int truckid, long packageid, int x, int y) {
        long seqNum = seqNumCounter.incrementAndGet();
        UDeliveryLocation deliveryLocation = UDeliveryLocation.newBuilder().setPackageid(packageid).setX(x).setY(y)
                .build();
        UGoDeliver goDeliver = UGoDeliver.newBuilder().setSeqnum(seqNum).setTruckid(truckid)
                .addAllPackages(List.of(deliveryLocation))
                .build();
        goDeliverMap.put(seqNum, goDeliver);
    }

    public void postUQuery(int truckid) {
        long seqNum = seqNumCounter.incrementAndGet();
        UQuery query = UQuery.newBuilder().setSeqnum(seqNum).setTruckid(truckid).build();
        queryMap.put(seqNum, query);
    }

    public void postAckToWorld(long ack) {
        ackToWorld.add(ack);
    }

    public void postUAConnectedToWorld(int worldid) {
        long seqNum = seqNumCounter.incrementAndGet();
        UAConnectedToWorld connectedToWorld = UAConnectedToWorld.newBuilder().setSeqnum(seqNum).setWorldid(worldid)
                .build();
        connectedToWorldMap.put(seqNum, connectedToWorld);
    }

    public void postUADestinationUpdated(int orderid, int x, int y) {
        long seqNum = seqNumCounter.incrementAndGet();
        UADestinationUpdated destinationUpdated = UADestinationUpdated.newBuilder().setSeqnum(seqNum)
                .setOrderid(orderid).setDestinationx(x).setDestinationy(y).build();
        destinationUpdatedMap.put(seqNum, destinationUpdated);
    }

    public void postUATruckArrived(int truckid, int whid) {
        long seqNum = seqNumCounter.incrementAndGet();
        UATruckArrived truckArrived = UATruckArrived.newBuilder().setSeqnum(seqNum).setTruckid(truckid).setWhnum(whid)
                .build();
        truckArrivedMap.put(seqNum, truckArrived);
    }

    public void postUAOrderDeparture(int orderid, long packageid, int trackingnum) {
        long seqNum = seqNumCounter.incrementAndGet();
        UAOrderDeparture orderDeparture = UAOrderDeparture.newBuilder().setSeqnum(seqNum).setOrderid(orderid)
                .setPackageid(packageid).setTrackingnum(trackingnum).build();
        orderDepartureMap.put(seqNum, orderDeparture);
    }

    public void postUAOrderDelivered(long packageid, int x, int y) {
        long seqNum = seqNumCounter.incrementAndGet();
        UAOrderDelivered orderDelivered = UAOrderDelivered.newBuilder().setSeqnum(seqNum).setPackageid(packageid)
                .setDestinationx(x).setDestinationy(y).build();
        orderDeliveredMap.put(seqNum, orderDelivered);
    }

    public void postErr(String msg, long originseqnum) {
        long seqNum = seqNumCounter.incrementAndGet();
        Err err = Err.newBuilder().setSeqnum(seqNum).setErr(msg).setOriginseqnum(originseqnum).build();
        errMap.put(seqNum, err);
    }

    public void postAckToAmazon(long ack) {
        ackToAmazon.add(ack);
    }

    public void sendProtoToWorld(OutputStream toWorld) throws IOException {
        boolean isEmpty = true;
        UCommands.Builder cmdsBldr = UCommands.newBuilder();
        if (goPickupMap.size() > 0) {
            cmdsBldr.addAllPickups(goPickupMap.values());
            isEmpty = false;
        }
        if (goDeliverMap.size() > 0) {
            cmdsBldr.addAllDeliveries(goDeliverMap.values());
            isEmpty = false;
        }
        if (queryMap.size() > 0) {
            cmdsBldr.addAllQueries(queryMap.values());
            isEmpty = false;
        }
        if (ackToWorld.size() > 0) {
            cmdsBldr.addAllAcks(ackToWorld);
            isEmpty = false;
        }
        if (!isEmpty) {
            UCommands msgToWorld = cmdsBldr.build();
            msgToWorld.writeDelimitedTo(toWorld);
            logger.debug(
                    Thread.currentThread().getName() + "\nTo World:\n" + msgToWorld.toString());
            ackToWorld.clear();
        }
    }

    public void sendProtoToAmazon(OutputStream toAmazon) throws IOException {
        boolean isEmpty = true;
        UACommands.Builder cmdsBldr = UACommands.newBuilder();
        if (connectedToWorldMap.size() > 0) {
            cmdsBldr.addAllConnectedtoworld(connectedToWorldMap.values());
            isEmpty = false;
        }
        if (destinationUpdatedMap.size() > 0) {
            cmdsBldr.addAllDestinationupdated(destinationUpdatedMap.values());
            isEmpty = false;
        }
        if (truckArrivedMap.size() > 0) {
            cmdsBldr.addAllTruckarrived(truckArrivedMap.values());
            isEmpty = false;
        }
        if (orderDepartureMap.size() > 0) {
            cmdsBldr.addAllOrderdeparture(orderDepartureMap.values());
            isEmpty = false;
        }
        if (orderDeliveredMap.size() > 0) {
            cmdsBldr.addAllOrderdelivered(orderDeliveredMap.values());
            isEmpty = false;
        }
        if (errMap.size() > 0) {
            cmdsBldr.addAllError(errMap.values());
            isEmpty = false;
        }
        if (ackToAmazon.size() > 0) {
            cmdsBldr.addAllAcks(ackToAmazon);
            isEmpty = false;
        }
        if (!isEmpty) {
            UACommands msgToAmazon = cmdsBldr.build();
            msgToAmazon.writeDelimitedTo(toAmazon);
            logger.debug(
                    Thread.currentThread().getName() + "\nTo Amazon:\n" + msgToAmazon.toString());
            ackToAmazon.clear();
        }
    }

}
