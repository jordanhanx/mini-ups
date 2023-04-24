package edu.duke.ece568.team24.miniups.service;

import edu.duke.ece568.team24.miniups.model.MyPackage;
import edu.duke.ece568.team24.miniups.model.Truck;
import edu.duke.ece568.team24.miniups.repository.TruckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static edu.duke.ece568.team24.miniups.model.myenum.TruckStatus.IDLE;

@Service
@Transactional
public class TruckService {

    @Autowired
    private final TruckRepository truckRepository;

    public TruckService(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    public void createTruck(Truck truck) {
        // Account account = new Account("david","123456");
        // accountRepository.save(account);
        // MyOrder myorder = new MyOrder(11,22,account);
        // myorderRepository.save(myorder);
        // Truck truck = new Truck(0,1, IDLE);
        // truckRepository.save(truck);
        truckRepository.save(truck);

    }

    public Truck updateTruck(Long id, Truck rhstruck) {
        return truckRepository.findById(id).map(
                Truck -> {
                    // Truck.setTruckID(rhstruck.getTruckID());
                    Truck.setRealX(rhstruck.getRealX());
                    Truck.setRealY(rhstruck.getRealY());
                    Truck.setStatus(rhstruck.getStatus());
                    return truckRepository.save(Truck);
                }).orElseThrow(() -> new NoSuchElementException("Cannot find this truck"));
    }

    public List<Truck> getAllMyTruck() {
        return truckRepository.findAll();
    }

    public Optional<Truck> getTruckById(Long id) {
        return truckRepository.findById(id);
    }

    public void deleteAllTruck() {
        truckRepository.deleteAll();
    }

    public void deleteTruckById(Long id) {
        truckRepository.deleteById(id);
    }

}
