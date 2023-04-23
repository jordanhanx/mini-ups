package edu.duke.ece568.sp.miniups.repository;

import edu.duke.ece568.sp.miniups.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PackageRepository extends JpaRepository<Package, Long> {
}
