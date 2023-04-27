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

@Component
public class ProtoServiceRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ProtoServiceRunner.class);

    private final ThreadPoolTaskExecutor executor;
    private final ThreadPoolTaskScheduler scheduler;
    private final ProtoMsgParser parseProtoService;
    private final ProtoMsgSender sendProtoService;

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

    public ProtoServiceRunner(ThreadPoolTaskExecutor executor, ThreadPoolTaskScheduler scheduler,
            ProtoMsgParser parseProtoService, ProtoMsgSender sendProtoService,
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
        // connectToAmazon(amazonHost, amazonPort);
        // connectToWorld(worldHost, worldPort);
        // replyToAmazon();

        // executor.execute(() -> {
        // while (true) {
        // try {
        // AUCommands cmds = AUCommands.parseDelimitedFrom(fromAmazon);
        // logger.debug(Thread.currentThread().getName() + "\nFrom Amazon:\n" +
        // cmds.toString());
        // parseProtoService.parseProtoFromAmazon(cmds);
        // } catch (Exception e) {
        // Throwable throwable = e;
        // while (throwable.getCause() != null) {
        // throwable = throwable.getCause();
        // }
        // logger.error(Thread.currentThread().getName() + "\nAmazon: " +
        // throwable.getMessage());
        // reConnectAmazon(amazonHost, amazonPort);
        // }
        // }
        // });

        // executor.execute(() -> {
        // while (true) {
        // try {
        // UResponses responses = UResponses.parseDelimitedFrom(fromWorld);
        // logger.debug(Thread.currentThread().getName() + "\nFrom World:\n" +
        // responses.toString());
        // parseProtoService.parseProtoFromWorld(responses);
        // } catch (Exception e) {
        // Throwable throwable = e;
        // while (throwable.getCause() != null) {
        // throwable = throwable.getCause();
        // }
        // logger.error(Thread.currentThread().getName() + "\nWorld: " +
        // throwable.getMessage());
        // reConnectWorld(worldHost, worldPort);
        // }
        // }
        // });

        // scheduler.scheduleAtFixedRate(() -> {
        // try {
        // sendProtoService.sendProtoToAmazon(toAmazon);
        // } catch (Exception e) {
        // Throwable throwable = e;
        // while (throwable.getCause() != null) {
        // throwable = throwable.getCause();
        // }
        // logger.error(Thread.currentThread().getName() + "\nTo Amazon: " +
        // throwable.getMessage());
        // }
        // }, Duration.ofSeconds(5));

        // scheduler.scheduleAtFixedRate(() -> {
        // try {
        // sendProtoService.sendProtoToWorld(toWorld);
        // } catch (Exception e) {
        // Throwable throwable = e;
        // while (throwable.getCause() != null) {
        // throwable = throwable.getCause();
        // }
        // logger.error(Thread.currentThread().getName() + "\nTo World" +
        // throwable.getMessage());
        // }
        // }, Duration.ofSeconds(5));
    }

    public void connectToAmazon(String amazonHost, int amazonPort)
            throws IOException {
        amazonSocket = new Socket(amazonHost, amazonPort);
        logger.debug("\nConnected to Amazon at " + amazonSocket.toString());
        toAmazon = amazonSocket.getOutputStream();
        fromAmazon = amazonSocket.getInputStream();
        AUCommands cmds = AUCommands.parseDelimitedFrom(fromAmazon);
        logger.debug("\nFrom Amazon:\n" + cmds.toString());
        AUConnectedToWorld conn = cmds.getConnectedtoworld(0);
        worldid = conn.getWorldid();
        UACommands responseToAmazon = UACommands.newBuilder()
                .addAcks(conn.getSeqnum())
                .build();
        responseToAmazon.writeDelimitedTo(toAmazon);
        logger.debug("\nTo Amazon:\n" + responseToAmazon.toString());
    }

    public void connectToWorld(String worldHost, int worldPort)
            throws IOException {
        worldSocket = new Socket(worldHost, worldPort);
        logger.debug("\nConnected to World at " + worldSocket.toString());
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
        logger.debug("\nFrom World:\n" + uConnected.toString());
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
        logger.debug("\nTo Amazon:\n"
                + responseToAmazon.toString());
    }

    public void reConnectAmazon(String amazonHost, int amazonPort) {
        try {
            amazonSocket = new Socket(amazonHost, amazonPort);
            toAmazon = amazonSocket.getOutputStream();
            fromAmazon = amazonSocket.getInputStream();
            logger.warn(Thread.currentThread().getName() + "\nReconnected to Amazon at " + amazonSocket.toString());
        } catch (IOException e) {
            Throwable throwable = e;
            while (throwable.getCause() != null) {
                throwable = throwable.getCause();
            }
            logger.error(Thread.currentThread().getName() + "\nFailed to reconnect Amazon: " + throwable.getMessage());
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
                throw new IOException(uConnected.getResult());
            }
            logger.warn(Thread.currentThread().getName() + "\nReconnected to World at " + worldSocket.toString());
        } catch (IOException e) {
            Throwable throwable = e;
            while (throwable.getCause() != null) {
                throwable = throwable.getCause();
            }
            logger.error(Thread.currentThread().getName() + "\nFailed to reconnect World: " + throwable.getMessage());
        }
    }
}
