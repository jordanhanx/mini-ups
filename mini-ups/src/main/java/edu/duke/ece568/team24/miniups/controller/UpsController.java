package edu.duke.ece568.team24.miniups.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.duke.ece568.team24.miniups.model.Account;
import edu.duke.ece568.team24.miniups.model.MyOrder;
import edu.duke.ece568.team24.miniups.model.MyPackage;
import edu.duke.ece568.team24.miniups.model.Truck;
import edu.duke.ece568.team24.miniups.model.CombineMyOrder;
import edu.duke.ece568.team24.miniups.model.myenum.MyPackageStatus;

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

    // @GetMapping("/account/profile")
    // public String getProfile(Model model) {
    // Account acct = new Account(456L, "username", "password", "email@email.com",
    // "USER");
    // model.addAttribute("user", acct);
    // return "account-profile";
    // }

    @GetMapping("/package/detail")
    public String getDetail(@RequestParam("trackingNumber") String trackNum, Model model) {
        logger.debug("\nTrackingNumber = " + Long.parseLong(trackNum));
//        MyOrder odr = new MyOrder();
//        odr.setDestinationX(200);
//        odr.setDestinationY(200);
//        MyPackage pack = new MyPackage();
//        pack.setPackageID(Long.parseLong(trackNum));
//        pack.setDescription("Small Box");
//        pack.setStatus(MyPackageStatus.DELIVERING);
//        pack.setOriginX(10);
//        pack.setOriginY(10);
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
        model.addAttribute("order", odr);
        model.addAttribute("package", pack);
        return "package-detail";
    }

    @GetMapping("/account/order")
    public String getOrders(Model model) {
//        List<CombineMyOrder> orders = new ArrayList<>();
//        CombineMyOrder order = new CombineMyOrder();
//        order.setOrderID(188L);
//        order.setDestinationX(8);
//        order.setDestinationY(9);
//        Truck truck = new Truck(1L,0,1, IDLE);
//
//        List<MyPackage> packages = new ArrayList<>();
//        MyPackage mypackage = new MyPackage();
//        mypackage.setPackageID(388L);
//        mypackage.setStatus(DELIVERING);
//        mypackage.setOriginX(38);
//        mypackage.setOriginY(39);
//        packages.add(mypackage);
//
//        order.setPackages(packages);
//        orders.add(order);
//
//        model.addAttribute("orders",orders);

        // 输入accountid即可查询
        List<MyOrder> realorders = myorderService.findOrdersByAccount(1L);
        List<CombineMyOrder> orders = new ArrayList<>();
        for(int i = 0;i<realorders.size();i++){
            CombineMyOrder order = new CombineMyOrder();
            order.setOrderID(realorders.get(i).getOrderID());
            order.setDestinationX(realorders.get(i).getDestinationX());
            order.setDestinationY(realorders.get(i).getDestinationY());

            List<MyPackage> realpackages = myPackageService.findPackagesByOrderID(realorders.get(i).getOrderID());
            order.setPackages(realpackages);
            orders.add(order);
        }

        model.addAttribute("orders",orders);

        return "order-list";
    }
}
