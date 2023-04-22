package edu.duke.ece568.team24.miniups.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import edu.duke.ece568.team24.miniups.protobuf.worldups.UConnect;
import edu.duke.ece568.team24.miniups.protobuf.worldups.UConnected;
import edu.duke.ece568.team24.miniups.protobuf.worldups.UInitTruck;

@Component
public class ServerRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ServerRunner.class);

    private final ThreadPoolTaskExecutor executor;
    private final ThreadPoolTaskScheduler scheduler;
    private int worldid;

    public ServerRunner(ThreadPoolTaskExecutor executor, ThreadPoolTaskScheduler scheduler) {
        this.executor = executor;
        this.scheduler = scheduler;
    }

    @Override
    public void run(String... args) throws Exception {
        try (Socket socket = new Socket("localhost", 12345)) {
            UInitTruck truck1 = UInitTruck.newBuilder().setId(1).setX(0).setY(0).build();
            UInitTruck truck2 = UInitTruck.newBuilder().setId(2).setX(0).setY(0).build();
            UConnect uConnect = UConnect.newBuilder()
                    .setIsAmazon(false)
                    .addAllTrucks(List.of(truck1, truck2))
                    .build();
            OutputStream outputStream = socket.getOutputStream();
            uConnect.writeDelimitedTo(outputStream);
            InputStream inputStream = socket.getInputStream();
            UConnected uConnected = UConnected.parseDelimitedFrom(inputStream);
            System.out.println(uConnected.toString());
        }
    }

    public UConnect buildUConnect() {
        UInitTruck truck1 = UInitTruck.newBuilder().setId(1).setX(0).setY(0).build();
        UInitTruck truck2 = UInitTruck.newBuilder().setId(2).setX(0).setY(0).build();
        return UConnect.newBuilder()
                .setIsAmazon(false)
                .addAllTrucks(List.of(truck1, truck2))
                .build();
    }
}
