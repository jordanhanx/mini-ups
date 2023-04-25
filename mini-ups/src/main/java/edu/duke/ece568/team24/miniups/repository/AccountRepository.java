package edu.duke.ece568.team24.miniups.repository;

import edu.duke.ece568.team24.miniups.model.Account;
import edu.duke.ece568.team24.miniups.model.MyOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {

    // @Query("SELECT o FROM MyOrder WHERE o.accountID = :accountID;")
    // List<MyOrder> findOrdersByAccount(@Param("accountID") Long accountID);
}
