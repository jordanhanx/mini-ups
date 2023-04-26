package edu.duke.ece568.team24.miniups.service;

import edu.duke.ece568.team24.miniups.model.AccountEntity;
import edu.duke.ece568.team24.miniups.repository.AccountRepository;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AccountEntity createAccount(String username, String password, String role) {
        return accountRepository
                .saveAndFlush(new AccountEntity(username, passwordEncoder.encode(password), null, role));
    }

    public AccountEntity createAccount(String username, String password, String role, String email) {
        return accountRepository
                .saveAndFlush(new AccountEntity(username, passwordEncoder.encode(password), email, role));
    }

    public AccountEntity updatePassword(String username, String newPassword) {
        AccountEntity account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new EntityNotFoundException("Not found user: " + username);
        }
        account.setPassword(passwordEncoder.encode(newPassword));
        return accountRepository.saveAndFlush(account);
    }

    public AccountEntity updateEmail(String username, String newEmail) {
        AccountEntity account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new EntityNotFoundException("Not found user: " + username);
        }
        account.setEmail(newEmail);
        return accountRepository.saveAndFlush(account);
    }

    public AccountEntity findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public AccountEntity findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }
}
