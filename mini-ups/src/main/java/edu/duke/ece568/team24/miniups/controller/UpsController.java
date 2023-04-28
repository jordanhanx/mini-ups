package edu.duke.ece568.team24.miniups.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

<<<<<<< HEAD
import edu.duke.ece568.team24.miniups.model.Account;
import edu.duke.ece568.team24.miniups.model.MyOrder;
import edu.duke.ece568.team24.miniups.model.MyPackage;
import edu.duke.ece568.team24.miniups.model.Truck;
import edu.duke.ece568.team24.miniups.model.CombineMyOrder;
import edu.duke.ece568.team24.miniups.model.myenum.MyPackageStatus;
=======
import edu.duke.ece568.team24.miniups.model.OrderEntity;
import edu.duke.ece568.team24.miniups.model.PackageEntity;
>>>>>>> origin/main

import edu.duke.ece568.team24.miniups.service.MyOrderService;
import edu.duke.ece568.team24.miniups.service.MyPackageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import static edu.duke.ece568.team24.miniups.model.myenum.MyPackageStatus.DELIVERING;
import static edu.duke.ece568.team24.miniups.model.myenum.TruckStatus.IDLE;

@Controller
public class UpsController {

    private static final Logger logger = LoggerFactory.getLogger(UpsController.class);

    private final MyOrderService myorderService;
    private final MyPackageService myPackageService;

    public UpsController(MyOrderService myorderService, MyPackageService myPackageService) {
        this.myorderService = myorderService;
        this.myPackageService = myPackageService;
    }

    @GetMapping("/")
    public String getIndex(Model model) {
        return "index";
    }

    @GetMapping("/package/detail")
    public String getDetail(@RequestParam("trackingNumber") String trackNum, Model model) {
        logger.debug("\nTrackingNumber = " + Long.parseLong(trackNum));

        Optional<MyPackage> checkpack = myPackageService.getMyPackageById(Long.parseLong(trackNum));
        Optional<MyOrder> checkodr;
        MyPackage pack;
        MyOrder odr;
        if(checkpack.isPresent()) {
            checkodr = myorderService.getOrderById(checkpack.get().getMyorder().getOrderID());
            if(checkodr.isPresent()){
                pack = checkpack.get();
                odr = checkodr.get();
            }else{
                return "index";
            }
        }else{
            return "index";
        }
//=======
//        OrderEntity odr = new OrderEntity();
//        odr.setDestinationX(200);
//        odr.setDestinationY(200);
//        PackageEntity pack = new PackageEntity();
//        pack.setTrackingNumber(Long.parseLong(trackNum));
//        pack.setDescription("Small Box");
//        pack.setStatus("DELIVERING");
//        pack.setOriginX(10);
//        pack.setOriginY(10);
//>>>>>>> origin/main
        model.addAttribute("order", odr);
        model.addAttribute("package", pack);
        return "package-detail";
    }

//    @GetMapping("/account/order")
//    public String getOrders(Model model) {
//        return "order-list";
//    }

}
