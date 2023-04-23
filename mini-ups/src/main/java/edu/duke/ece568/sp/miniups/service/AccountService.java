package edu.duke.ece568.sp.miniups.service;

import edu.duke.ece568.sp.miniups.model.Account;
import edu.duke.ece568.sp.miniups.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static edu.duke.ece568.sp.miniups.model.myenum.TruckStatus.IDLE;

@Service
@Transactional
public class AccountService {

    @Autowired
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void createAccount(Account account) {
//        Account account = new Account("david","123456");
        accountRepository.save(account);

    }

}
