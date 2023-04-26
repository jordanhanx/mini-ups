package edu.duke.ece568.team24.miniups.repository;

import edu.duke.ece568.team24.miniups.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    AccountEntity findByUsername(String username);

    AccountEntity findByEmail(String email);

}
