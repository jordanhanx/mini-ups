package edu.duke.ece568.team24.miniups.controller;

import edu.duke.ece568.team24.miniups.model.Account;
import edu.duke.ece568.team24.miniups.model.MyOrder;
import edu.duke.ece568.team24.miniups.service.MyOrderService;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "order")
public class MyOrderController {

    private final MyOrderService myorderService;

    public MyOrderController(MyOrderService myorderService) {
        this.myorderService = myorderService;
    }

    @GetMapping(path = "create")
    public void createOrders() {

        Account account = new Account(1L, "david green", "123456", "david123@gmail.com", "ADMIN");
        MyOrder myorder = new MyOrder(7L, 12, 65, account);
        myorderService.createOrder(myorder);
        MyOrder myorder0 = new MyOrder(10L, 100, 88, account);
        myorderService.createOrder(myorder0);

    }

    @GetMapping(path = "query")
    public List<MyOrder> queryallOrdersByAccount() {

        return myorderService.findOrdersByAccount(1L);

    }
}
