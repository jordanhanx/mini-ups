package edu.duke.ece568.team24.miniups.repository;

import edu.duke.ece568.team24.miniups.model.MyOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.duke.ece568.team24.miniups.model.MyOrder;

@Repository
@Transactional
public interface MyOrderRepository extends JpaRepository<MyOrder, Long> {

}
