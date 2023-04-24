package edu.duke.ece568.team24.miniups.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.duke.ece568.team24.miniups.model.Truck;

@Repository
@Transactional
public interface TruckRepository extends JpaRepository<Truck, Long> {
}
