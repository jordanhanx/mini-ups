package edu.duke.ece568.team24.miniups.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import edu.duke.ece568.team24.miniups.dto.TruckDto;
import edu.duke.ece568.team24.miniups.protobuf.amazonups.*;
import edu.duke.ece568.team24.miniups.protobuf.worldups.*;
import edu.duke.ece568.team24.miniups.service.TruckService;

@Component
public class ProtoServiceRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ProtoServiceRunner.class);

    private final ThreadPoolTaskExecutor executor;
    private final ThreadPoolTaskScheduler scheduler;
    private final ProtoMsgParser protoMsgParser;
    private final ProtoMsgSender protoMsgSender;

    private final TruckService truckService;

    private final String amazonHost;
    private final int amazonPort;
    private final String worldHost;
    private final int worldPort;

    private final int initTruckNum;

    private Socket amazonSocket;
    private Socket worldSocket;
    private OutputStream toAmazon;
    private InputStream fromAmazon;
    private OutputStream toWorld;
    private InputStream fromWorld;
    private long worldid;

    public ProtoServiceRunner(ThreadPoolTaskExecutor executor, ThreadPoolTaskScheduler scheduler,
            ProtoMsgParser protoMsgParser, ProtoMsgSender protoMsgSender, TruckService truckService,
            @Value("${miniamazon.host}") String amazonHost, @Value("${miniamazon.port}") String amazonPort,
            @Value("${simworld.host}") String worldHost, @Value("${simworld.port}") String worldPort,
            @Value("${ups.init.truck.num}") String initTruckNum) {

        this.executor = executor;
        this.scheduler = scheduler;
        this.protoMsgParser = protoMsgParser;
        this.protoMsgSender = protoMsgSender;
        this.truckService = truckService;

        this.amazonHost = amazonHost;
        this.amazonPort = Integer.parseInt(amazonPort);
        this.worldHost = worldHost;
        this.worldPort = Integer.parseInt(worldPort);
        this.initTruckNum = Integer.parseInt(initTruckNum);
    }

    @Override
    public void run(String... args) throws Exception {
        connectToAmazon(amazonHost, amazonPort);
        connectToWorld(worldHost, worldPort);
        replyToAmazon();

        logger.info("[UPS] listen from [AMAZON]");
        executor.execute(() -> {
            while (true) {
                try {
                    AUCommands cmds = AUCommands.parseDelimitedFrom(fromAmazon);
                    logger.info("[From AMAZON]\n" + cmds.toString());
                    protoMsgParser.parseProtoFromAmazon(cmds);
                } catch (Exception e) {
                    logger.error("[From AMAZON]" + ProtoMsgParser.getCausedError(e));
                }
            }
        });

        logger.info("[UPS] listen from [WORLD]");
        executor.execute(() -> {
            while (true) {
                try {
                    UResponses responses = UResponses.parseDelimitedFrom(fromWorld);
                    logger.debug("[From WORLD]\n" + responses.toString());
                    protoMsgParser.parseProtoFromWorld(responses);
                } catch (Exception e) {
                    logger.error("[From WORLD]" + ProtoMsgParser.getCausedError(e));
                    reConnectWorld(worldHost, worldPort);
                }
            }
        });

        logger.info("[UPS] send periadically to [AMAZON]");
        scheduler.scheduleAtFixedRate(() -> {
            try {
                protoMsgSender.sendProtoToAmazon(toAmazon);
            } catch (Exception e) {
                logger.error("[To AMAZON]" + ProtoMsgParser.getCausedError(e));
            }
        }, Duration.ofSeconds(5));

        logger.info("[UPS] send periadically to [WORLD]");
        scheduler.scheduleAtFixedRate(() -> {
            try {
                truckService.findAll().stream().forEach(t -> protoMsgSender.postUQuery(t.getId()));
                protoMsgSender.sendProtoToWorld(toWorld);
            } catch (Exception e) {
                logger.error("[To WORLD]" + ProtoMsgParser.getCausedError(e));
            }
        }, Duration.ofSeconds(5));
    }

    public List<UInitTruck> initTrucks(int num) {
        List<TruckDto> truckDtos = truckService.createTrucks(num);
        return truckDtos.stream().map(t -> {
            return UInitTruck.newBuilder().setId(t.getId()).setX(t.getRealX()).setY(t.getRealY()).build();
        }).toList();
    }

    public void connectToAmazon(String amazonHost, int amazonPort)
            throws IOException {
        amazonSocket = new Socket(amazonHost, amazonPort);
        logger.info("[Connected AMAZON] at " + amazonSocket.toString());
        toAmazon = amazonSocket.getOutputStream();
        fromAmazon = amazonSocket.getInputStream();
        AUCommands cmds = AUCommands.parseDelimitedFrom(fromAmazon);
        logger.info("[From AMAZON]\n" + cmds.toString());
        AUConnectedToWorld conn = cmds.getConnectedtoworld(0);
        worldid = conn.getWorldid();
        UACommands responseToAmazon = UACommands.newBuilder()
                .addAcks(conn.getSeqnum())
                .build();
        responseToAmazon.writeDelimitedTo(toAmazon);
        logger.info("[To AMAZON]\n" + responseToAmazon.toString());
    }

    public void connectToWorld(String worldHost, int worldPort)
            throws IOException {
        worldSocket = new Socket(worldHost, worldPort);
        logger.info("[Connected WORLD] at " + worldSocket.toString());
        toWorld = worldSocket.getOutputStream();
        fromWorld = worldSocket.getInputStream();
        UConnect.newBuilder()
                .setWorldid(worldid)
                .setIsAmazon(false)
                .addAllTrucks(initTrucks(initTruckNum))
                .build()
                .writeDelimitedTo(toWorld);
        UConnected uConnected = UConnected.parseDelimitedFrom(fromWorld);
        logger.info("[From WORLD]\n" + uConnected.toString());
        if (!uConnected.getResult().contains("connected")) {
            logger.error("\n" + uConnected.toString());
            throw new IOException(uConnected.getResult());
        }
    }

    public void replyToAmazon() throws IOException {
        UACommands responseToAmazon = UACommands.newBuilder()
                .addConnectedtoworld(UAConnectedToWorld.newBuilder().setSeqnum(0).setWorldid(worldid))
                .build();
        responseToAmazon.writeDelimitedTo(toAmazon);
        logger.info("[To Amazon]\n" + responseToAmazon.toString());
    }

    public void reConnectWorld(String worldHost, int worldPort) {
        try {
            Thread.sleep(5000);
            worldSocket = new Socket(worldHost, worldPort);
            toWorld = worldSocket.getOutputStream();
            fromWorld = worldSocket.getInputStream();
            UConnect.newBuilder().setWorldid(worldid).setIsAmazon(false).build().writeDelimitedTo(toWorld);
            UConnected uConnected = UConnected.parseDelimitedFrom(fromWorld);
            if (!uConnected.getResult().contains("connected")) {
                throw new IOException(uConnected.getResult());
            }
            logger.warn("[Reconnected World] at " + worldSocket.toString());
        } catch (Exception e) {
            logger.error("[Failed to reconnect World]\n" + ProtoMsgParser.getCausedError(e));
        }
    }
}
