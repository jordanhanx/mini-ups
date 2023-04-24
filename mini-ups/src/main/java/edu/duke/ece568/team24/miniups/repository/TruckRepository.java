package edu.duke.ece568.team24.miniups.repository;

import edu.duke.ece568.team24.miniups.model.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TruckRepository extends JpaRepository<Truck, Long> {
}
