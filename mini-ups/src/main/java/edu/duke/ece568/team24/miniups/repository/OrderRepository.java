package edu.duke.ece568.team24.miniups.repository;

import edu.duke.ece568.team24.miniups.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    List<OrderEntity> findByOwnerUsername(String username);

}
