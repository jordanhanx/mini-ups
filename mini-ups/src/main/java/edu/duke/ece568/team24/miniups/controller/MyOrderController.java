package edu.duke.ece568.team24.miniups.controller;

import edu.duke.ece568.team24.miniups.service.MyOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "myorder")
public class MyOrderController {

    private final MyOrderService orderService;

    public MyOrderController(MyOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    // public String getStudents(){
    // return "hello";
    // }
    public void getOrders() {

        orderService.createOrder();

    }
}
