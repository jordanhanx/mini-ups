package edu.duke.ece568.team24.miniups.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.duke.ece568.team24.miniups.service.*;
import edu.duke.ece568.team24.miniups.dto.*;

@Controller
public class UpsController {

    private static final Logger logger = LoggerFactory.getLogger(UpsController.class);

    private final AccountService accountService;
    private final OrderService orderService;
    private final PackageService packageService;
    private final TruckService truckService;

    public UpsController(AccountService accountService, OrderService orderService, PackageService packageService,
            TruckService truckService) {
        this.accountService = accountService;
        this.orderService = orderService;
        this.packageService = packageService;
        this.truckService = truckService;
    }

    @GetMapping("/")
    public String getIndex(Model model) {
        return "index";
    }

    @GetMapping("/services")
    public String getServices(Model model) {
        return "services";
    }

    @GetMapping("/support")
    public String getSupport(Model model) {
        return "support";
    }

    @GetMapping("/package/detail")
    public String getDetail(@RequestParam("trackingNumber") String trackNum, Model model) {
        logger.debug("\nTrackingNumber = " + Long.parseLong(trackNum));
        PackageDto pack = packageService.findByTrackingNumber(Long.parseLong(trackNum));
        if (pack == null) {
            return "redirect:/?error=Package not found";
        }
        model.addAttribute("package", pack);
        return "package-detail";
    }

}
