package edu.duke.ece568.team24.miniups.repository;

import edu.duke.ece568.team24.miniups.model.MyOrder;
import edu.duke.ece568.team24.miniups.model.MyPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface MyPackageRepository extends JpaRepository<MyPackage, Long> {

    List<MyPackage> findByMyorderOrderID(Long orderID);
}
