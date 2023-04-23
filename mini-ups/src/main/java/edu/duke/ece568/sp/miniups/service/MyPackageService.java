package edu.duke.ece568.sp.miniups.service;

import edu.duke.ece568.sp.miniups.model.MyPackage;
import edu.duke.ece568.sp.miniups.repository.MyPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MyPackageService {


    @Autowired
    private final MyPackageRepository mypackageRepository;

    public MyPackageService(MyPackageRepository mypackageRepository) {
        this.mypackageRepository = mypackageRepository;
    }


    public void createMyPackage(MyPackage mypackage) {
//        Account account = new Account("david","123456");
//        accountRepository.save(account);
//        MyOrder myorder = new MyOrder(11,22,account);
//        myorderRepository.save(myorder);
//        Truck truck = new Truck(0,1, IDLE);
//        truckRepository.save(truck);
//        truckRepository.save(truck);
//        MyPackage mypackage = new MyPackage("shampoo",myorder,truck);
        mypackageRepository.save(mypackage);

    }
}
