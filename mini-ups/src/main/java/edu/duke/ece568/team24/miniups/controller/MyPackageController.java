package edu.duke.ece568.team24.miniups.controller;

import edu.duke.ece568.team24.miniups.model.Account;
import edu.duke.ece568.team24.miniups.model.MyOrder;
import edu.duke.ece568.team24.miniups.model.MyPackage;
import edu.duke.ece568.team24.miniups.model.Truck;
import edu.duke.ece568.team24.miniups.service.MyPackageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static edu.duke.ece568.team24.miniups.model.myenum.MyPackageStatus.DELIVERING;
import static edu.duke.ece568.team24.miniups.model.myenum.TruckStatus.IDLE;

@RestController
@RequestMapping(path = "package")
public class MyPackageController {

    private final MyPackageService mypackageService;

    public MyPackageController(MyPackageService mypackageService) {
        this.mypackageService = mypackageService;
    }

    @GetMapping(path = "create")
    public void createMyPackage() {

        Account account = new Account(1L, "david green", "123456", "david123@gmail.com", "ADMIN");
        MyOrder myorder = new MyOrder(7L, 12, 65, account);
        MyOrder myorder2 = new MyOrder(10L,100,88,account);
        Truck truck = new Truck(1L, 0, 1, IDLE);

        MyPackage mypackage = new MyPackage(630, "shampoo", DELIVERING, 5, 7, myorder, truck);
        mypackageService.createMyPackage(mypackage);
        MyPackage mypackage2 = new MyPackage(631, "soap", DELIVERING, 5, 7, myorder, truck);
        mypackageService.createMyPackage(mypackage2);
        MyPackage mypackage3 = new MyPackage(632, "gloves", DELIVERING, 5, 7, myorder, truck);
        mypackageService.createMyPackage(mypackage3);

        MyPackage mypackage4 = new MyPackage(634,"dog food",DELIVERING,5,7,myorder2,truck);
        mypackageService.createMyPackage(mypackage4);

    }

    @GetMapping(path = "update")
    public void updateMyPackage() {

        Account account = new Account(1L, "david green", "123456", "david123@gmail.com", "ADMIN");
        MyOrder myorder = new MyOrder(7L, 12, 65, account);
        Truck truck = new Truck(1L, 0, 1, IDLE);
        MyPackage mypackage = new MyPackage(1L, 630, "cat food", DELIVERING, 5, 7, myorder, truck);
        mypackageService.updateMyPackage(1L, mypackage);

    }

    @GetMapping(path = "query")
    public List<MyPackage> queryallPackagesByOrder() {

        return mypackageService.findPackagesByOrderID(7L);

    }

}
