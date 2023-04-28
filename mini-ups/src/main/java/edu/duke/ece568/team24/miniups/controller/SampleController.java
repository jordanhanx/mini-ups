package edu.duke.ece568.team24.miniups.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.duke.ece568.team24.miniups.service.*;
import edu.duke.ece568.team24.miniups.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "sample")
public class SampleController {

    private final AccountService accountService;
    private final OrderService orderService;
    private final PackageService packageService;
    private final TruckService truckService;

    public SampleController(AccountService accountService, OrderService orderService, PackageService packageService, TruckService truckService) {
        this.accountService = accountService;
        this.orderService = orderService;
        this.packageService = packageService;
        this.truckService = truckService;
    }

    @GetMapping(path = "create")
    public void createSampleExample() {

//        AccountDto accountDto = accountService.createAccount("david", "123456", "ADMIN", "david123@gmail.com");
//        AccountDto accountDto2 = accountService.createAccount("mary", "123456", "USER", "mary234@gmail.com");

        OrderDto orderDto1 = orderService.createOrder(7, 12, 65, "david");
        OrderDto orderDto2 = orderService.createOrder(10, 100, 88, "david");

        List<TruckDto> truckDtoList = truckService.createTrucks(1);

        PackageDto packageDto1 = packageService.createPackage(630, "shampoo", orderDto1, truckDtoList.get(0));
        PackageDto packageDto2 = packageService.createPackage(631, "soap", orderDto1, truckDtoList.get(0));
        PackageDto packageDto3 = packageService.createPackage(632, "gloves", orderDto1, truckDtoList.get(0));

        PackageDto packageDto4 = packageService.createPackage(634, "dog food", orderDto2, truckDtoList.get(0));

//        packageDto1.setStatus("DELIVERED");
//        packageDto1.setOriginX(5);
//        packageDto1.setOriginY(7);
//        packageDto2.setStatus("DELIVERED");
//        packageDto2.setOriginX(5);
//        packageDto2.setOriginY(7);
//        packageDto3.setStatus("DELIVERED");
//        packageDto3.setOriginX(5);
//        packageDto3.setOriginY(7);
//        packageDto4.setStatus("DELIVERING");
//        packageDto4.setOriginX(5);
//        packageDto4.setOriginY(7);

    }
}
