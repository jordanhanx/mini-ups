package edu.duke.ece568.team24.miniups.service;

import edu.duke.ece568.team24.miniups.dto.OrderDto;
import edu.duke.ece568.team24.miniups.dto.PackageDto;
import edu.duke.ece568.team24.miniups.dto.TruckDto;
import edu.duke.ece568.team24.miniups.model.PackageEntity;
import edu.duke.ece568.team24.miniups.repository.OrderRepository;
import edu.duke.ece568.team24.miniups.repository.PackageRepository;
import edu.duke.ece568.team24.miniups.repository.TruckRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class PackageService {

    private final PackageRepository packageRepository;

    private final OrderRepository orderRepository;

    private final TruckRepository truckRepository;

    public PackageService(PackageRepository packageRepository, OrderRepository orderRepository,
            TruckRepository truckRepository) {
        this.packageRepository = packageRepository;
        this.orderRepository = orderRepository;
        this.truckRepository = truckRepository;
    }

    public PackageDto createPackage(long id, String description, int originX, int originY,
            OrderDto orderDto, TruckDto truckDto) {
        return PackageDto.mapper(
                packageRepository.save(
                        new PackageEntity(id, description, originX, originY,
                                orderRepository.findById(orderDto.getId())
                                        .orElseThrow(() -> new EntityNotFoundException(
                                                "Not found Order: " + orderDto.getId())),
                                truckRepository.findById(truckDto.getId()).orElseThrow(
                                        () -> new EntityNotFoundException("Not found Truck: " + truckDto.getId())))));
    }

    public PackageDto findById(long id) {
        return PackageDto.mapper(packageRepository.findByTrackingNumber(id).orElse(null));
    }

    public PackageDto findByTrackingNumber(long trackNum) {
        return PackageDto.mapper(packageRepository.findByTrackingNumber(trackNum).orElse(null));
    }

    public PackageDto updateStatus(long id, String status) {
        PackageEntity p = packageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found Package: " + id));
        p.setStatus(status);
        return PackageDto.mapper(packageRepository.save(p));
    }

    public PackageDto updateTruckEntity(long id, TruckDto truckDto) {
        PackageEntity p = packageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found Package: " + id));
        p.setTruckEntity(truckRepository.findById(truckDto.getId()).orElseThrow(
                () -> new EntityNotFoundException("Not found Truck: " + truckDto.getId())));
        return PackageDto.mapper(packageRepository.save(p));
    }

    public double getRemainDistance(long id) {
        PackageEntity p = packageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found Package: " + id));
        int currX = p.getTruckEntity().getRealX();
        int currY = p.getTruckEntity().getRealY();
        int destX = p.getOrderEntity().getDestinationX();
        int destY = p.getOrderEntity().getDestinationY();
        return Math.sqrt(Math.pow(currX - destX, 2) + Math.pow(currY - destY, 2));
    }

}
