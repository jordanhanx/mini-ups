package edu.duke.ece568.team24.miniups.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import edu.duke.ece568.team24.miniups.protobuf.amazonups.AUCommands;
import edu.duke.ece568.team24.miniups.protobuf.worldups.UResponses;

@Service
public class ParseProtoService {

    private static final Logger logger = LoggerFactory.getLogger(ParseProtoService.class);

    private SendProtoService sendProtoService;

    public ParseProtoService(SendProtoService service) {
        this.sendProtoService = service;
    }

    public void parseProtoFromWorld(UResponses responses) {
        responses.getAcksList().stream().peek(ack -> {
            sendProtoService.removeMsgByACK(ack);
            logger.info(ack.toString());
        });
        responses.getCompletionsList().stream().peek(c -> {
            sendProtoService.postAckToWorld(c.getSeqnum());
            logger.info(c.toString());
        }).filter(c -> c.getStatus().contains("arrive warehouse")).peek(c -> {
            sendProtoService.postUATruckArrived(c.getTruckid(), 1);
        });
        responses.getDeliveredList().stream().peek(d -> {
            sendProtoService.postAckToWorld(d.getSeqnum());
            logger.info(d.toString());
            sendProtoService.postUAOrderDelivered(d.getPackageid(), 0, 0);
        });
        responses.getTruckstatusList().stream().peek(t -> {
            sendProtoService.postAckToWorld(t.getSeqnum());
            logger.info(t.toString());
        });
        responses.getErrorList().stream().peek(err -> {
            sendProtoService.postAckToWorld(err.getSeqnum());
            logger.error(err.toString());
        });
    }

    public void parseProtoFromAmazon(AUCommands cmds) {
        cmds.getAcksList().stream().peek(ack -> {
            sendProtoService.removeMsgByACK(ack);
            logger.info(ack.toString());
        });
        cmds.getOrdercreatedList().stream().peek(o -> {
            sendProtoService.postAckToAmazon(o.getSeqnum());
            logger.info(o.toString());
        });
        cmds.getRequesttruckList().stream().peek(r -> {
            sendProtoService.postAckToAmazon(r.getSeqnum());
            logger.info(r.toString());
            sendProtoService.postUGoPickup(1, r.getWhnum());
        });
        cmds.getOrderloadedList().stream().peek(ld -> {
            sendProtoService.postAckToAmazon(ld.getSeqnum());
            logger.info(ld.toString());
            sendProtoService.postUGoUGoDeliver(ld.getTruckid(), ld.getPackageid(), 1, 1);
        });
        cmds.getErrorList().stream().peek(err -> {
            sendProtoService.postAckToAmazon(err.getSeqnum());
            logger.info(err.toString());
        });
    }
}
