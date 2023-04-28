package edu.duke.ece568.team24.miniups.service;

import edu.duke.ece568.team24.miniups.dto.OrderDto;
import edu.duke.ece568.team24.miniups.dto.PackageDto;
import edu.duke.ece568.team24.miniups.dto.TruckDto;
import edu.duke.ece568.team24.miniups.model.OrderEntity;
import edu.duke.ece568.team24.miniups.model.PackageEntity;
import edu.duke.ece568.team24.miniups.model.TruckEntity;
import edu.duke.ece568.team24.miniups.repository.OrderRepository;
import edu.duke.ece568.team24.miniups.repository.PackageRepository;
import edu.duke.ece568.team24.miniups.repository.TruckRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
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

        public PackageDto createPackage(long id, String description, OrderDto orderDto, TruckDto truckDto) {
                if (packageRepository.existsById(id)) {
                        throw new EntityExistsException("Package with ID:" + id + " already exists");
                }
                OrderEntity orderEntity = orderRepository.findById(orderDto.getId())
                                .orElseThrow(() -> new EntityNotFoundException("Not found Order: " + orderDto.getId()));
                TruckEntity truckEntity = truckRepository.findById(truckDto.getId())
                                .orElseThrow(() -> new EntityNotFoundException("Not found Truck: " + truckDto.getId()));
                return PackageDto.mapper(
                                packageRepository.save(new PackageEntity(id, description, orderEntity, truckEntity)));
        }

        public PackageDto findById(long id) {
                return PackageDto.mapper(packageRepository.findByTrackingNumber(id)
                                .orElseThrow(() -> new EntityNotFoundException("Not found Package with ID:" + id)));
        }

        public PackageDto findByTrackingNumber(long trackNum) {
                return PackageDto.mapper(packageRepository.findByTrackingNumber(trackNum).orElse(null));
        }

        public PackageDto updateStatus(long id, String status) {
                PackageEntity p = packageRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Not found Package with ID:" + id));
                p.setStatus(status);
                if (status.equalsIgnoreCase("delivered")) {
                        p.setTruckEntity(null);
                }
                return PackageDto.mapper(packageRepository.save(p));
        }

}
