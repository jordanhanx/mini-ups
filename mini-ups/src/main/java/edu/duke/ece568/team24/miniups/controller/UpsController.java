package edu.duke.ece568.team24.miniups.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.duke.ece568.team24.miniups.model.OrderEntity;
import edu.duke.ece568.team24.miniups.model.PackageEntity;

@Controller
public class UpsController {

    private static final Logger logger = LoggerFactory.getLogger(UpsController.class);

    @GetMapping("/")
    public String getIndex(Model model) {
        return "index";
    }

    @GetMapping("/package/detail")
    public String getDetail(@RequestParam("trackingNumber") String trackNum, Model model) {
        logger.debug("\nTrackingNumber = " + Long.parseLong(trackNum));
        OrderEntity odr = new OrderEntity();
        odr.setDestinationX(200);
        odr.setDestinationY(200);
        PackageEntity pack = new PackageEntity();
        pack.setTrackingNumber(Long.parseLong(trackNum));
        pack.setDescription("Small Box");
        pack.setStatus("DELIVERING");
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
