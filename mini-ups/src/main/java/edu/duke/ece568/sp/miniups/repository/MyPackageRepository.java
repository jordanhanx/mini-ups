package edu.duke.ece568.sp.miniups.repository;

import edu.duke.ece568.sp.miniups.model.MyPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface MyPackageRepository extends JpaRepository<MyPackage, Long> {
}
