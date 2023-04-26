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
import edu.duke.ece568.team24.miniups.model.myenum.MyPackageStatus;

@Controller
public class UpsController {

    private static final Logger logger = LoggerFactory.getLogger(UpsController.class);

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
        MyOrder odr = new MyOrder();
        odr.setDestinationX(200);
        odr.setDestinationY(200);
        MyPackage pack = new MyPackage();
        pack.setPackageID(Long.parseLong(trackNum));
        pack.setDescription("Small Box");
        pack.setStatus(MyPackageStatus.DELIVERING);
        pack.setOriginX(10);
        pack.setOriginY(10);
        model.addAttribute("order", odr);
        model.addAttribute("package", pack);
        return "package-detail";
    }

    @GetMapping("/account/order")
    public String getOrders(Model model) {

        return "order-list";
    }
}
