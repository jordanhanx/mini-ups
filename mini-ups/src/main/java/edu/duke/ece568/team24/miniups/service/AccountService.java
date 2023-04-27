package edu.duke.ece568.team24.miniups.service;

import edu.duke.ece568.team24.miniups.model.AccountEntity;
import edu.duke.ece568.team24.miniups.repository.AccountRepository;
import edu.duke.ece568.team24.miniups.dto.AccountDto;

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

    public AccountDto createAccount(String username, String password, String role) {
        return AccountDto.mapper(
                accountRepository
                        .save(new AccountEntity(username, passwordEncoder.encode(password), null, role)));
    }

    public AccountDto createAccount(String username, String password, String role, String email) {
        return AccountDto.mapper(
                accountRepository
                        .save(new AccountEntity(username, passwordEncoder.encode(password), email, role)));
    }

    public AccountDto findByUsername(String username) {
        return AccountDto.mapper(accountRepository.findByUsername(username).orElse(null));
    }

    public AccountDto findByEmail(String email) {
        return AccountDto.mapper(accountRepository.findByEmail(email).orElse(null));
    }

    public boolean matchPassword(String username, String oldPassword) {
        AccountEntity account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Not found Account: " + username));
        return passwordEncoder.matches(oldPassword, account.getPassword());
    }

    public AccountDto updatePassword(String username, String newPassword) {
        AccountEntity account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Not found Account: " + username));
        account.setPassword(passwordEncoder.encode(newPassword));
        return AccountDto.mapper(accountRepository.save(account));
    }

    public AccountDto updateEmail(String username, String newEmail) {
        AccountEntity account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Not found Account: " + username));
        account.setEmail(newEmail);
        return AccountDto.mapper(accountRepository.save(account));
    }

}
