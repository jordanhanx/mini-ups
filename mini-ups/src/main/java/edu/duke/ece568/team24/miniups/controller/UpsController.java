package edu.duke.ece568.team24.miniups.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UpsController {

    private static final Logger logger = LoggerFactory.getLogger(UpsController.class);

    @GetMapping("/")
    public String getIndex(Model model) {
        return "index";
    }

    @GetMapping("/signin")
    public String getSignin(Model model) {
        return "signin";
    }

    @GetMapping("/signup")
    public String getSignup(Model model) {
        return "signup";
    }

    @GetMapping("/account/{accountID}/profile")
    public String getProfile(@PathVariable("accountID") long accountID, Model model) {
        logger.debug("\nccountID = " + accountID);
        return "profile";
    }

    @GetMapping("/package/{trackNum}/detail")
    public String getDetail(@PathVariable("trackNum") long trackNum, Model model) {
        logger.debug("\nTrackingNumber = " + trackNum);
        return "detail";
    }

    @GetMapping("/account/{accountID}/orders")
    public String getOrders(Model model) {
        return "orders";
    }
}
