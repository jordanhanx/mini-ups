package edu.duke.ece568.team24.miniups.connection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import edu.duke.ece568.team24.miniups.protobuf.worldups.UConnect;
import edu.duke.ece568.team24.miniups.protobuf.worldups.UConnected;
import edu.duke.ece568.team24.miniups.protobuf.worldups.UInitTruck;

@Component
public class ProtoSender implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // try (Socket socket = new Socket("localhost", 23456)) {
        UInitTruck truck1 = UInitTruck.newBuilder().setId(1).setX(0).setY(0).build();
        UInitTruck truck2 = UInitTruck.newBuilder().setId(2).setX(0).setY(0).build();
        UConnect uconnect = UConnect.newBuilder()
                .setIsAmazon(false)
                .addAllTrucks(List.of(truck1, truck2))
                .build();
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        uconnect.writeDelimitedTo(outBytes);
        ByteArrayInputStream inBytes = new ByteArrayInputStream(outBytes.toByteArray());
        UConnect uconnect2 = UConnect.parseDelimitedFrom(inBytes);
        System.out.println(uconnect2.toString());
        // }
    }
}
