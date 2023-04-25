package edu.duke.ece568.team24.miniups.service;

import edu.duke.ece568.team24.miniups.model.Truck;
import edu.duke.ece568.team24.miniups.model.Warehouse;
import edu.duke.ece568.team24.miniups.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class WarehouseService {

    @Autowired
    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public void createWarehouse(Warehouse warehouse) {
        // Warehouse warehouse = new Warehouse(5,7);
        warehouseRepository.save(warehouse);

    }

    public Warehouse updateWarehouse(Long id, Warehouse rhswarehouse) {
        return warehouseRepository.findById(id).map(
                Warehouse -> {
                    // Warehouse.setWarehouseID(rhswarehouse.getWarehouseID());
                    Warehouse.setX(rhswarehouse.getX());
                    Warehouse.setY(rhswarehouse.getY());
                    return warehouseRepository.save(Warehouse);
                }).orElseThrow(() -> new NoSuchElementException("Cannot find this warehouse"));
    }

    public List<Warehouse> getAllWarehouse() {
        return warehouseRepository.findAll();
    }

    public Optional<Warehouse> getWarehouseById(Long id) {
        return warehouseRepository.findById(id);
    }

    public void deleteAllWarehouse() {
        warehouseRepository.deleteAll();
    }

    public void deleteWarehouseById(Long id) {
        warehouseRepository.deleteById(id);
    }
}
