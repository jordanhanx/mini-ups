package edu.duke.ece568.team24.miniups.service;

import edu.duke.ece568.team24.miniups.dto.TruckDto;
import edu.duke.ece568.team24.miniups.dto.WarehouseDto;
import edu.duke.ece568.team24.miniups.model.TruckEntity;
import edu.duke.ece568.team24.miniups.model.WarehouseEntity;
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
        return TruckDto.mapper(truckRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found Truck with ID:" + id)));
    }

    public TruckDto updateTruckStatus(int id, int x, int y, String status) {
        TruckEntity truck = truckRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found Truck: " + id));
        truck.setRealX(x);
        truck.setRealY(y);
        truck.setStatus(status.toLowerCase());
        return TruckDto.mapper(truckRepository.save(truck));
    }

    private static double calculateDistance(int x1, int y1, int x2, int y2) {
        int deltaX = x1 - x2;
        int deltaY = y1 - y2;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public TruckDto assignATruckToWarehouse(WarehouseDto targetWareHouse) {
        WarehouseEntity warehouseEntity = warehouseRepository.findById(targetWareHouse.getId())
                .orElseThrow(() -> new EntityNotFoundException("Not found Warehouse: " + targetWareHouse.getId()));
        List<TruckEntity> trucks = truckRepository.findByStatusIn(List.of("idle", "delivering"));
        TruckEntity nearestTruck = trucks.stream().min((t1, t2) -> {
            double distance1 = calculateDistance(t1.getRealX(), t1.getRealY(), warehouseEntity.getX(),
                    warehouseEntity.getY());
            double distance2 = calculateDistance(t2.getRealX(), t2.getRealY(), warehouseEntity.getX(),
                    warehouseEntity.getY());
            return Double.compare(distance1, distance2);
        }).orElse(null);
        if (nearestTruck != null) {
            nearestTruck.setTargetWareHouse(warehouseEntity);
            truckRepository.save(nearestTruck);
        }
        return TruckDto.mapper(nearestTruck);
    }

    public List<TruckDto> dispatchAllReadyTrucks() {
        List<TruckEntity> allTrucks = truckRepository.findAll();
        return allTrucks.stream()
                .filter(t -> t.getStatus().equalsIgnoreCase("arrive warehouse"))
                .filter(t -> t.getPackages().size() > 0)
                .peek(t -> t.getPackages().stream().forEach(p -> p.setStatus("out for delivery")))
                .map(TruckDto::mapper)
                .toList();
    }
}
