package edu.duke.ece568.team24.miniups.controller;

import edu.duke.ece568.team24.miniups.model.Account;
import edu.duke.ece568.team24.miniups.model.MyOrder;
import edu.duke.ece568.team24.miniups.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(path = "create")
    public void createMyAccount() {

        Account account = new Account("david", "123456", "david123@gmail.com", "ADMIN");
        accountService.createAccount(account);

        Account account3 = new Account("mary", "123456", "mary234@gmail.com", "USER");
        accountService.createAccount(account3);

    }

    @GetMapping(path = "update")
    public void updateMyAccount() {

        Account account2 = new Account(1L, "david green", "123456", "david123@gmail.com", "ADMIN");
        accountService.updateAccount(1L, account2);

    }

}