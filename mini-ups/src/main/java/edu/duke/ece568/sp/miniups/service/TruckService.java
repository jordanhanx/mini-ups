package edu.duke.ece568.sp.miniups.service;

import edu.duke.ece568.sp.miniups.model.Truck;
import edu.duke.ece568.sp.miniups.repository.TruckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static edu.duke.ece568.sp.miniups.model.myenum.TruckStatus.IDLE;

@Service
@Transactional
public class TruckService {

    @Autowired
    private final TruckRepository truckRepository;

    public TruckService(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }


    public void createTruck(Truck truck) {
//        Account account = new Account("david","123456");
//        accountRepository.save(account);
//        MyOrder myorder = new MyOrder(11,22,account);
//        myorderRepository.save(myorder);
//        Truck truck = new Truck(0,1, IDLE);
//        truckRepository.save(truck);
        truckRepository.save(truck);

    }

}

