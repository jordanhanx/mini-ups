package edu.duke.ece568.team24.miniups.service;

import edu.duke.ece568.team24.miniups.dto.TruckDto;
import edu.duke.ece568.team24.miniups.dto.WarehouseDto;
import edu.duke.ece568.team24.miniups.model.TruckEntity;
import edu.duke.ece568.team24.miniups.repository.TruckRepository;
import edu.duke.ece568.team24.miniups.repository.WarehouseRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class TruckService {

    private final TruckRepository truckRepository;

    private final WarehouseRepository warehouseRepository;

    public TruckService(TruckRepository truckRepository, WarehouseRepository warehouseRepository) {
        this.truckRepository = truckRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public List<TruckDto> createTrucks(int num) {
        List<TruckDto> trucks = new ArrayList<>();
        for (int i = 0; i < num; ++i) {
            trucks.add(TruckDto.mapper(truckRepository.save(new TruckEntity())));
        }
        return trucks;
    }

    public TruckDto findById(int id) {
        return TruckDto.mapper(truckRepository.findById(id).orElse(null));
    }

    public TruckDto updateTruck(int id, int x, int y, String status) {
        TruckEntity truck = truckRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found Truck: " + id));
        truck.setRealX(x);
        truck.setRealY(y);
        truck.setStatus(status);
        return TruckDto.mapper(truckRepository.save(truck));
    }

    public TruckDto assignTargetWarehouse(int id, WarehouseDto targetWareHouse) {
        TruckEntity truck = truckRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found Truck: " + id));
        truck.setTargetWareHouse(warehouseRepository.findById(targetWareHouse.getId())
                .orElseThrow(() -> new EntityNotFoundException("Not found Warehouse: " + targetWareHouse.getId())));
        return TruckDto.mapper(truckRepository.save(truck));
    }
}
