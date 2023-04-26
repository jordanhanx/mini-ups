package edu.duke.ece568.team24.miniups.security;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.duke.ece568.team24.miniups.model.Account;
import edu.duke.ece568.team24.miniups.repository.AccountRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private AccountRepository accountRepository;

    public CustomUserDetailService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByAccountName(username);
        if (account.isPresent()) {
            User user = new User(
                    account.get().getAccountName(),
                    account.get().getPassword(),
                    List.of(new SimpleGrantedAuthority(account.get().getRole())));
            return user;
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
}
