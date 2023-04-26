package edu.duke.ece568.team24.miniups.controller;

import edu.duke.ece568.team24.miniups.model.MyPackage;
import edu.duke.ece568.team24.miniups.model.Truck;
import edu.duke.ece568.team24.miniups.service.MyPackageService;
import edu.duke.ece568.team24.miniups.service.TruckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static edu.duke.ece568.team24.miniups.model.myenum.TruckStatus.ARRIVE_WAREHOUSE;
import static edu.duke.ece568.team24.miniups.model.myenum.TruckStatus.IDLE;

// @RestController
// @RequestMapping(path = "truck")
// public class TruckController {

// private final TruckService truckService;
// private final MyPackageService myPackageService;

// public TruckController(TruckService truckService, MyPackageService
// myPackageService) {
// this.truckService = truckService;
// this.myPackageService = myPackageService;
// }

// @GetMapping(path = "create")
// public void createTruck() {

// Truck truck = new Truck(0, 1, IDLE);
// truckService.createTruck(truck);

// }

// @GetMapping(path = "update")
// public void updateTruck() {

// Truck truck = new Truck(1L, 0, 1, ARRIVE_WAREHOUSE);
// truckService.updateTruck(1L, truck);

// }

// @GetMapping(path = "query")
// public Optional<Truck> queryTruckByPackageID() {

// Optional<MyPackage> myPackage = myPackageService.getMyPackageById(1L);
// return truckService.getTruckById(myPackage.get().getTruck().getTruckID());

// }

// }