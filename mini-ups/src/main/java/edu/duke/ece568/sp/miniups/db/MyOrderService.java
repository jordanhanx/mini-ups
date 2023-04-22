package edu.duke.ece568.sp.miniups.db;

import org.springframework.stereotype.Service;

@Service
class MyOrderService {

    private final MyOrderRepository myorderRepository;

    MyOrderService(MyOrderRepository myorderRepository) {
        this.myorderRepository = myorderRepository;
    }

    public void createOrder(String name) {
        MyOrder myorder = new MyOrder(name);
//        Order.setName(name);
        myorderRepository.save(myorder);
    }

//    public List<Order> getOrders(){
//        return List.of(new Order("David"));
//    }
}
