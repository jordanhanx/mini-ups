package edu.duke.ece568.team24.miniups.repository;

import edu.duke.ece568.team24.miniups.model.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, Long> {

    Optional<PackageEntity> findByTrackingNumber(Long trackNum);

    List<PackageEntity> findByOrderEntityId(Integer orderId);

    List<PackageEntity> findByTruckEntityId(Integer truckId);
}
