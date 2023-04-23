package edu.duke.ece568.sp.miniups.service;

import edu.duke.ece568.sp.miniups.model.*;

import edu.duke.ece568.sp.miniups.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static edu.duke.ece568.sp.miniups.model.myenum.TruckStatus.IDLE;

@Service
@Transactional
public class MyOrderService {

    @Autowired
    private final MyOrderRepository myorderRepository;
    // 删
    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final TruckRepository truckRepository;
    @Autowired
    private final MyPackageRepository mypackageRepository;
    @Autowired
    private final WarehouseRepository warehouseRepository;

//    MyOrderService(MyOrderRepository myorderRepository) {
//        this.myorderRepository = myorderRepository;
//    }

    // 删
    MyOrderService(MyOrderRepository myorderRepository, AccountRepository accountRepository, TruckRepository truckRepository, MyPackageRepository mypackageRepository, WarehouseRepository warehouseRepository) {
        this.myorderRepository = myorderRepository;
        this.accountRepository = accountRepository;
        this.truckRepository = truckRepository;
        this.mypackageRepository = mypackageRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public void createOrder() {
        Account account = new Account("david","123456");
        accountRepository.save(account);
        MyOrder myorder = new MyOrder(11,22,account);
        myorderRepository.save(myorder);
        Truck truck = new Truck(0,1, IDLE);
        truckRepository.save(truck);
        MyPackage mypackage = new MyPackage("shampoo",myorder,truck);
        mypackageRepository.save(mypackage);
        Warehouse warehouse = new Warehouse(5,7);
        warehouseRepository.save(warehouse);

    }

//    public List<Order> getOrders(){
//        return List.of(new Order("David"));
//    }
}
