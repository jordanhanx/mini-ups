package edu.duke.ece568.team24.miniups.service;

import edu.duke.ece568.team24.miniups.dto.WarehouseDto;
import edu.duke.ece568.team24.miniups.model.WarehouseEntity;
import edu.duke.ece568.team24.miniups.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public WarehouseDto createWarehouse(int id, int x, int y) {
        return WarehouseDto.mapper(warehouseRepository.save(new WarehouseEntity(id, x, y)));
    }

    public WarehouseDto findById(Integer id) {
        return WarehouseDto.mapper(warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found Warehouse: " + id)));
    }
}
