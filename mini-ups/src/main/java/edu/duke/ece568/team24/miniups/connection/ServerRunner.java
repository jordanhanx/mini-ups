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

import edu.duke.ece568.team24.miniups.protobuf.amazonups.*;
import edu.duke.ece568.team24.miniups.protobuf.worldups.*;
import edu.duke.ece568.team24.miniups.service.ParseProtoService;
import edu.duke.ece568.team24.miniups.service.SendProtoService;

@Component
public class ServerRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ServerRunner.class);

    private final ThreadPoolTaskExecutor executor;
    private final ThreadPoolTaskScheduler scheduler;
    private final ParseProtoService parseProtoService;
    private final SendProtoService sendProtoService;

    private final String amazonHost;
    private final int amazonPort;
    private final String worldHost;
    private final int worldPort;

    private Socket amazonSocket;
    private Socket worldSocket;
    private OutputStream toAmazon;
    private InputStream fromAmazon;
    private OutputStream toWorld;
    private InputStream fromWorld;
    private long worldid;

    public ServerRunner(ThreadPoolTaskExecutor executor, ThreadPoolTaskScheduler scheduler,
            ParseProtoService parseProtoService, SendProtoService sendProtoService,
            @Value("${miniamazon.host}") String amazonHost, @Value("${miniamazon.port}") String amazonPort,
            @Value("${simworld.host}") String worldHost, @Value("${simworld.port}") String worldPort) {

        this.executor = executor;
        this.scheduler = scheduler;
        this.parseProtoService = parseProtoService;
        this.sendProtoService = sendProtoService;

        this.amazonHost = amazonHost;
        this.amazonPort = Integer.parseInt(amazonPort);
        this.worldHost = worldHost;
        this.worldPort = Integer.parseInt(worldPort);
    }

    @Override
    public void run(String... args) throws Exception {
        connectToAmazon(amazonHost, amazonPort);
        connectToWorld(worldHost, worldPort);

        executor.execute(() -> {
            while (true) {
                try {
                    AUCommands cmds = AUCommands.parseDelimitedFrom(fromAmazon);
                    parseProtoService.parseProtoFromAmazon(cmds);
                } catch (Exception e) {
                    logger.error(Thread.currentThread().getName() + ": " + e.getMessage(), e);
                    reConnectAmazon(amazonHost, amazonPort);
                }
            }
        });

        executor.execute(() -> {
            while (true) {
                try {
                    UResponses responses = UResponses.parseDelimitedFrom(fromWorld);
                    parseProtoService.parseProtoFromWorld(responses);
                } catch (Exception e) {
                    logger.error(Thread.currentThread().getName() + ": " + e.getMessage(), e);
                    reConnectWorld(worldHost, worldPort);
                }
            }
        });

        scheduler.scheduleAtFixedRate(() -> {
            try {
                sendProtoService.sendProtoToAmazon(toAmazon);
            } catch (Exception e) {
                logger.error(Thread.currentThread().getName() + ": " + e.getMessage(), e);
                reConnectAmazon(amazonHost, amazonPort);
            }
        }, Duration.ofSeconds(1));

        scheduler.scheduleAtFixedRate(() -> {
            try {
                sendProtoService.sendProtoToWorld(toWorld);
            } catch (Exception e) {
                logger.error(Thread.currentThread().getName() + ": " + e.getMessage(), e);
                reConnectWorld(worldHost, worldPort);
            }
        }, Duration.ofSeconds(1));
    }

    public void connectToAmazon(String amazonHost, int amazonPort)
            throws IOException {
        amazonSocket = new Socket(amazonHost, amazonPort);
        toAmazon = amazonSocket.getOutputStream();
        fromAmazon = amazonSocket.getInputStream();
        AUCommands cmds = AUCommands.parseDelimitedFrom(fromAmazon);
        AUConnectedToWorld conn = cmds.getConnectedtoworld(0);
        worldid = conn.getWorldid();
    }

    public void connectToWorld(String worldHost, int worldPort)
            throws IOException {
        worldSocket = new Socket(worldHost, worldPort);
        toWorld = worldSocket.getOutputStream();
        fromWorld = worldSocket.getInputStream();
        UInitTruck truck1 = UInitTruck.newBuilder().setId(1).setX(0).setY(0).build();
        UInitTruck truck2 = UInitTruck.newBuilder().setId(2).setX(0).setY(0).build();
        UConnect.newBuilder()
                .setWorldid(worldid)
                .setIsAmazon(false)
                .addAllTrucks(List.of(truck1, truck2))
                .build()
                .writeDelimitedTo(toWorld);
        UConnected uConnected = UConnected.parseDelimitedFrom(fromWorld);
        if (uConnected.getResult().contains("connected")) {
            UACommands.newBuilder().setConnectedtoworld(0,
                    UAConnectedToWorld.newBuilder().setSeqnum(0).setWorldid(uConnected.getWorldid()))
                    .build().writeDelimitedTo(toAmazon);
        } else {
            logger.error(uConnected.toString());
            throw new IllegalStateException(uConnected.getResult());
        }
    }

    public void reConnectAmazon(String amazonHost, int amazonPort) {
        try {
            amazonSocket = new Socket(amazonHost, amazonPort);
            toAmazon = amazonSocket.getOutputStream();
            fromAmazon = amazonSocket.getInputStream();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void reConnectWorld(String worldHost, int worldPort) {
        try {
            worldSocket = new Socket(worldHost, worldPort);
            toWorld = worldSocket.getOutputStream();
            fromWorld = worldSocket.getInputStream();
            UConnect.newBuilder().setWorldid(worldid).setIsAmazon(false).build().writeDelimitedTo(toWorld);
            UConnected uConnected = UConnected.parseDelimitedFrom(fromWorld);
            if (!uConnected.getResult().contains("connected")) {
                logger.error(uConnected.toString());
                throw new IllegalStateException(uConnected.getResult());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
