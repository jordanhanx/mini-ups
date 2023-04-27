package edu.duke.ece568.team24.miniups.repository;

import edu.duke.ece568.team24.miniups.model.TruckEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TruckRepository extends JpaRepository<TruckEntity, Integer> {

    List<TruckEntity> findByStatusIn(List<String> statuses);
}
