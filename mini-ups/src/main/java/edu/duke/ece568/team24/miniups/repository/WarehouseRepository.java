package edu.duke.ece568.team24.miniups.repository;

import edu.duke.ece568.team24.miniups.model.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<WarehouseEntity, Integer> {
}
