package edu.duke.ece568.team24.miniups.service;

import edu.duke.ece568.team24.miniups.dto.TruckDto;
import edu.duke.ece568.team24.miniups.model.TruckEntity;
import edu.duke.ece568.team24.miniups.repository.TruckRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class TruckService {

    private final TruckRepository truckRepository;

    public TruckService(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
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

    public List<TruckDto> findAll() {
        return truckRepository.findAll().stream().map(TruckDto::mapper).toList();
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

    public TruckDto assignATruckToWarehouse(int targetWarehouseId, int whX, int whY) {
        List<TruckEntity> trucks = truckRepository.findByStatusIn(List.of("idle", "delivering"));
        TruckEntity nearestTruck = trucks.stream().min((t1, t2) -> {
            double distance1 = calculateDistance(t1.getRealX(), t1.getRealY(), whX, whY);
            double distance2 = calculateDistance(t2.getRealX(), t2.getRealY(), whX, whY);
            return Double.compare(distance1, distance2);
        }).orElse(null);
        if (nearestTruck != null) {
            nearestTruck.setTargetWarehouseId(targetWarehouseId);
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
