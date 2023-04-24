package edu.duke.ece568.sp.miniups.service;

import edu.duke.ece568.sp.miniups.model.Account;
import edu.duke.ece568.sp.miniups.model.MyOrder;
import edu.duke.ece568.sp.miniups.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public Account updateAccount(Long id, Account rhsaccount){
        return accountRepository.findById(id).map(
                Account -> {
//                    Account.setAccountID(rhsaccount.getAccountID());
                    Account.setPassword(rhsaccount.getPassword());
                    Account.setAccountName(rhsaccount.getAccountName());
                    return accountRepository.save(Account);
                }
        ).orElseThrow(() -> new NoSuchElementException("Cannot find this account"));
    }

    public List<Account> getAllMyAccount(){
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id){
        return accountRepository.findById(id);
    }

    public void deleteAllMyAccount(){
        accountRepository.deleteAll();
    }

    public void deleteAccountById(Long id){
        accountRepository.deleteById(id);
    }

}
