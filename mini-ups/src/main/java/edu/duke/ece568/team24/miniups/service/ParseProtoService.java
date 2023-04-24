package edu.duke.ece568.team24.miniups.service;

import java.util.concurrent.ConcurrentSkipListSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.duke.ece568.team24.miniups.protobuf.amazonups.AUCommands;
import edu.duke.ece568.team24.miniups.protobuf.worldups.UResponses;

@Service
public class ParseProtoService {

    private static final Logger logger = LoggerFactory.getLogger(ParseProtoService.class);

    private SendProtoService sendProtoService;

    private ConcurrentSkipListSet<Long> worldSeqNumCacheSet = new ConcurrentSkipListSet<>();
    private ConcurrentSkipListSet<Long> amazonSeqNumCacheSet = new ConcurrentSkipListSet<>();
    private final int worldSeqNumCacheSize;
    private final int amazonSeqNumCacheSize;

    public ParseProtoService(SendProtoService service,
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
                .peek(ack -> {
                    sendProtoService.removeMsgByACK(ack);
                    logger.info(ack.toString());
                });
        responses.getCompletionsList().stream()
                .peek(c -> {
                    sendProtoService.postAckToWorld(c.getSeqnum());
                    logger.info(c.toString());
                })
                .filter(c -> !worldSeqNumCacheSet.contains(c.getSeqnum()) && c.getStatus().contains("arrive warehouse"))
                .peek(c -> {
                    sendProtoService.postUATruckArrived(c.getTruckid(), 1);
                    addToWorldSeqNumCacheSet(c.getSeqnum());
                });
        responses.getDeliveredList().stream()
                .peek(d -> {
                    sendProtoService.postAckToWorld(d.getSeqnum());
                    logger.info(d.toString());
                })
                .filter(d -> !worldSeqNumCacheSet.contains(d.getSeqnum()))
                .peek(d -> {
                    sendProtoService.postUAOrderDelivered(d.getPackageid(), 0, 0);
                    addToWorldSeqNumCacheSet(d.getSeqnum());
                });
        responses.getTruckstatusList().stream()
                .peek(t -> {
                    sendProtoService.postAckToWorld(t.getSeqnum());
                    logger.info(t.toString());
                })
                .filter(d -> !worldSeqNumCacheSet.contains(d.getSeqnum()))
                .peek(t -> {
                    addToWorldSeqNumCacheSet(t.getSeqnum());
                });
        responses.getErrorList().stream()
                .peek(err -> {
                    sendProtoService.postAckToWorld(err.getSeqnum());
                    logger.error(err.toString());
                })
                .filter(d -> !worldSeqNumCacheSet.contains(d.getSeqnum()))
                .peek(err -> {
                    addToWorldSeqNumCacheSet(err.getSeqnum());
                });
    }

    public void parseProtoFromAmazon(AUCommands cmds) {
        cmds.getAcksList().stream()
                .peek(ack -> {
                    sendProtoService.removeMsgByACK(ack);
                    logger.info(ack.toString());
                });
        cmds.getConnectedtoworldList().stream()
                .peek(c -> {
                    sendProtoService.postAckToAmazon(c.getSeqnum());
                    logger.error(c.toString());
                });
        cmds.getOrdercreatedList().stream()
                .peek(o -> {
                    sendProtoService.postAckToAmazon(o.getSeqnum());
                    logger.info(o.toString());
                })
                .filter(o -> !amazonSeqNumCacheSet.contains(o.getSeqnum()))
                .peek(o -> {
                    addToAmazonSeqNumCacheSet(o.getSeqnum());
                });
        cmds.getRequesttruckList().stream()
                .peek(r -> {
                    sendProtoService.postAckToAmazon(r.getSeqnum());
                    logger.info(r.toString());
                })
                .filter(r -> !amazonSeqNumCacheSet.contains(r.getSeqnum()))
                .peek(r -> {
                    sendProtoService.postUGoPickup(1, r.getWhnum());
                    addToAmazonSeqNumCacheSet(r.getSeqnum());
                });
        cmds.getOrderloadedList().stream()
                .peek(ld -> {
                    sendProtoService.postAckToAmazon(ld.getSeqnum());
                    logger.info(ld.toString());
                })
                .filter(ld -> !amazonSeqNumCacheSet.contains(ld.getSeqnum()))
                .peek(ld -> {
                    sendProtoService.postUGoUGoDeliver(ld.getTruckid(), ld.getPackageid(), 1, 1);
                    addToAmazonSeqNumCacheSet(ld.getSeqnum());
                });
        cmds.getErrorList().stream()
                .peek(err -> {
                    sendProtoService.postAckToAmazon(err.getSeqnum());
                    logger.info(err.toString());
                })
                .filter(err -> !amazonSeqNumCacheSet.contains(err.getSeqnum()))
                .peek(err -> {
                    addToAmazonSeqNumCacheSet(err.getSeqnum());
                });
    }
}
