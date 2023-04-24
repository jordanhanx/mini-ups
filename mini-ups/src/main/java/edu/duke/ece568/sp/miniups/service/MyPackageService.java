package edu.duke.ece568.sp.miniups.service;

import edu.duke.ece568.sp.miniups.model.Account;
import edu.duke.ece568.sp.miniups.model.MyPackage;
import edu.duke.ece568.sp.miniups.repository.MyPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public MyPackage updateMyPackage(Long id, MyPackage rhsmypackage){
        return mypackageRepository.findById(id).map(
                MyPackage -> {
//                    MyPackage.setPackageID(rhsmypackage.getPackageID());
//                    MyPackage.setPackagefromAmazonID(rhsmypackage.getPackagefromAmazonID());
                    MyPackage.setDescription(rhsmypackage.getDescription());
//                    MyPackage.setMyorder(rhsmypackage.getMyorder());
//                    MyPackage.setTruck(rhsmypackage.getTruck());
                    MyPackage.setStatus(rhsmypackage.getStatus());
                    MyPackage.setOriginX(rhsmypackage.getOriginX());
                    MyPackage.setOriginY(rhsmypackage.getOriginY());
                    return mypackageRepository.save(MyPackage);
                }
        ).orElseThrow(() -> new NoSuchElementException("Cannot find this package"));
    }

    public List<MyPackage> getAllMyPackage(){
        return mypackageRepository.findAll();
    }

    public Optional<MyPackage> getMyPackageById(Long id){
        return mypackageRepository.findById(id);
    }

    public void deleteAllMyPackage(){
        mypackageRepository.deleteAll();
    }

    public void deleteMyPackageById(Long id){
        mypackageRepository.deleteById(id);
    }

    public List<MyPackage> findPackagesByOrderID(Long id) { return mypackageRepository.findByMyorderOrderID(id); }
}
