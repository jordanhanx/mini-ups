package edu.duke.ece568.team24.miniups.service;

import edu.duke.ece568.team24.miniups.model.*;

import edu.duke.ece568.team24.miniups.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static edu.duke.ece568.team24.miniups.model.myenum.TruckStatus.IDLE;

// @Service
// @Transactional
// public class MyOrderService {

// @Autowired
// private final MyOrderRepository myorderRepository;

// MyOrderService(MyOrderRepository myorderRepository) {
// this.myorderRepository = myorderRepository;
// }

// public void createOrder(MyOrder myorder) {
// myorderRepository.save(myorder);
// }

// public MyOrder updateOrder(Long id, MyOrder rhsmyorder) {
// return myorderRepository.findById(id).map(
// myOrder -> {
// myOrder.setOrderID(rhsmyorder.getOrderID());
// myOrder.setDestinationX(rhsmyorder.getDestinationX());
// myOrder.setDestinationY(rhsmyorder.getDestinationY());
// // myOrder.setAccount(rhsmyorder.getAccount());
// return myorderRepository.save(myOrder);
// }).orElseThrow(() -> new NoSuchElementException("Cannot find this order"));
// }

// public List<MyOrder> getAllMyOrder() {
// return myorderRepository.findAll();
// }

// public Optional<MyOrder> getOrderById(Long id) {
// return myorderRepository.findById(id);
// }

// public void deleteAllMyOrder() {
// myorderRepository.deleteAll();
// }

// public void deleteOrderById(Long id) {
// // myorderRepository.findById(id).orElseThrow(() -> new
// // NoSuchElementException("Cannot find this order"));
// myorderRepository.deleteById(id);

// }

// public List<MyOrder> findOrdersByAccount(Long accountID) {
// return myorderRepository.findByAccountAccountID(accountID);
// }
// }
