package edu.duke.ece568.team24.miniups.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.duke.ece568.team24.miniups.model.*;
import edu.duke.ece568.team24.miniups.repository.*;

import static edu.duke.ece568.team24.miniups.model.myenum.TruckStatus.IDLE;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    // MyOrderService(MyOrderRepository myorderRepository) {
    // this.myorderRepository = myorderRepository;
    // }

    // 删
    MyOrderService(MyOrderRepository myorderRepository, AccountRepository accountRepository,
            TruckRepository truckRepository, MyPackageRepository mypackageRepository,
            WarehouseRepository warehouseRepository) {
        this.myorderRepository = myorderRepository;
        this.accountRepository = accountRepository;
        this.truckRepository = truckRepository;
        this.mypackageRepository = mypackageRepository;
        this.warehouseRepository = warehouseRepository;
    }

    // 删
    public void createOrder() {
        Account account = new Account("david", "123456", "david123@gmail.com", "ADMIN");
        accountRepository.save(account);
        MyOrder myorder = new MyOrder(233L, 11, 22, account);
        myorderRepository.save(myorder);
        // Truck truck = new Truck(0,1, IDLE);
        // truckRepository.save(truck);
        // MyPackage mypackage = new MyPackage("shampoo",myorder,truck);
        // mypackageRepository.save(mypackage);
        // Warehouse warehouse = new Warehouse(5,7);
        // warehouseRepository.save(warehouse);
        //
        MyOrder myorder2 = new MyOrder(233L, 111, 222, account);
        updateOrder(myorder.getOrderID(), myorder2);
        // deleteOrderById(1L);

    }

    public void createOrder(MyOrder myorder) {
        myorderRepository.save(myorder);
    }

    public MyOrder updateOrder(Long id, MyOrder rhsmyorder) {
        return myorderRepository.findById(id).map(
                myOrder -> {
                    myOrder.setOrderID(rhsmyorder.getOrderID());
                    myOrder.setDestinationX(rhsmyorder.getDestinationX());
                    myOrder.setDestinationY(rhsmyorder.getDestinationY());
                    // myOrder.setAccount(rhsmyorder.getAccount());
                    return myorderRepository.save(myOrder);
                }).orElseThrow(() -> new NoSuchElementException("Cannot find this order"));
    }

    public List<MyOrder> getAllMyOrder() {
        return myorderRepository.findAll();
    }

    public Optional<MyOrder> getOrderById(Long id) {
        return myorderRepository.findById(id);
    }

    public void deleteAllMyOrder() {
        myorderRepository.deleteAll();
    }

    public void deleteOrderById(Long id) {
        // myorderRepository.findById(id).orElseThrow(() -> new
        // NoSuchElementException("Cannot find this order"));
        myorderRepository.deleteById(id);

    }
}
