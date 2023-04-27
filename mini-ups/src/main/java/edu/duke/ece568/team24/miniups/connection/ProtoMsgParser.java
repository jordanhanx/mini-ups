package edu.duke.ece568.team24.miniups.connection;

import java.util.concurrent.ConcurrentSkipListSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.duke.ece568.team24.miniups.protobuf.amazonups.AUCommands;
import edu.duke.ece568.team24.miniups.protobuf.worldups.UResponses;

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

    public void parseProtoFromWorld(UResponses responses) {
        responses.getAcksList().stream()
                .forEach(ack -> {
                    sendProtoService.removeMsgByACK(ack);
                    logger.debug(Thread.currentThread().getName() + "\nParsed:" + ack.toString());
                });
        responses.getCompletionsList().stream()
                .peek(c -> {
                    sendProtoService.postAckToWorld(c.getSeqnum());
                    logger.debug(Thread.currentThread().getName() + "\nParsed:" + c.toString());
                })
                .filter(c -> !worldSeqNumCacheSet.contains(c.getSeqnum())
                        && c.getStatus().equalsIgnoreCase("ARRIVE WAREHOUSE"))
                .forEach(c -> {
                    sendProtoService.postUATruckArrived(c.getTruckid(), 0);
                    logger.debug(Thread.currentThread().getName() + "\nPostUATruckArrived:");
                    addToWorldSeqNumCacheSet(c.getSeqnum());
                });
        responses.getDeliveredList().stream()
                .peek(d -> {
                    sendProtoService.postAckToWorld(d.getSeqnum());
                    logger.debug(Thread.currentThread().getName() + "\nParsed:" + d.toString());
                })
                .filter(d -> !worldSeqNumCacheSet.contains(d.getSeqnum()))
                .forEach(d -> {
                    sendProtoService.postUAOrderDelivered(d.getPackageid(), 0, 0);
                    addToWorldSeqNumCacheSet(d.getSeqnum());
                });
        responses.getTruckstatusList().stream()
                .peek(t -> {
                    sendProtoService.postAckToWorld(t.getSeqnum());
                    logger.debug(Thread.currentThread().getName() + "\nParsed:" + t.toString());
                })
                .filter(d -> !worldSeqNumCacheSet.contains(d.getSeqnum()))
                .forEach(t -> {
                    addToWorldSeqNumCacheSet(t.getSeqnum());
                });
        responses.getErrorList().stream()
                .peek(err -> {
                    sendProtoService.postAckToWorld(err.getSeqnum());
                    logger.debug(Thread.currentThread().getName() + "\nParsed:" + err.toString());
                })
                .filter(d -> !worldSeqNumCacheSet.contains(d.getSeqnum()))
                .forEach(err -> {
                    addToWorldSeqNumCacheSet(err.getSeqnum());
                });
    }

    public void parseProtoFromAmazon(AUCommands cmds) {
        cmds.getAcksList().stream()
                .forEach(ack -> {
                    sendProtoService.removeMsgByACK(ack);
                    logger.debug(Thread.currentThread().getName() + "\nParsed:" + ack.toString());
                });
        cmds.getConnectedtoworldList().stream()
                .forEach(c -> {
                    sendProtoService.postAckToAmazon(c.getSeqnum());
                    logger.debug(Thread.currentThread().getName() + "\nParsed:" + c.toString());
                });
        cmds.getOrdercreatedList().stream()
                .peek(o -> {
                    sendProtoService.postAckToAmazon(o.getSeqnum());
                    logger.debug(Thread.currentThread().getName() + "\nParsed:" + o.toString());
                })
                .filter(o -> !amazonSeqNumCacheSet.contains(o.getSeqnum()))
                .forEach(o -> {
                    addToAmazonSeqNumCacheSet(o.getSeqnum());
                });
        cmds.getRequesttruckList().stream()
                .peek(r -> {
                    sendProtoService.postAckToAmazon(r.getSeqnum());
                    logger.debug(Thread.currentThread().getName() + "\nParsed:" + r.toString());
                })
                .filter(r -> !amazonSeqNumCacheSet.contains(r.getSeqnum()))
                .forEach(r -> {
                    sendProtoService.postUGoPickup(1, r.getWhnum());
                    addToAmazonSeqNumCacheSet(r.getSeqnum());
                });
        cmds.getOrderloadedList().stream()
                .peek(ld -> {
                    sendProtoService.postAckToAmazon(ld.getSeqnum());
                    logger.debug(Thread.currentThread().getName() + "\nParsed:" + ld.toString());
                })
                .filter(ld -> !amazonSeqNumCacheSet.contains(ld.getSeqnum()))
                .forEach(ld -> {
                    sendProtoService.postUGoUGoDeliver(ld.getTruckid(), ld.getPackageid(), 1, 1);
                    addToAmazonSeqNumCacheSet(ld.getSeqnum());
                });
        cmds.getErrorList().stream()
                .peek(err -> {
                    sendProtoService.postAckToAmazon(err.getSeqnum());
                    logger.debug(Thread.currentThread().getName() + "\nParsed:" + err.toString());
                })
                .filter(err -> !amazonSeqNumCacheSet.contains(err.getSeqnum()))
                .forEach(err -> {
                    addToAmazonSeqNumCacheSet(err.getSeqnum());
                });
    }
}
