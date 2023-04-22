package edu.duke.ece568.sp.miniups.db;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "myorder")
public class MyOrderController {

    private final MyOrderService orderService;

    public MyOrderController(MyOrderService rhsorderService){
        this.orderService = rhsorderService;
    }
    @GetMapping
//    public String getStudents(){
//        return "hello";
//    }
    public void getOrders(){

        orderService.createOrder("david");

    }
}
