package edu.duke.ece568.team24.miniups.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.duke.ece568.team24.miniups.model.AccountEntity;
import edu.duke.ece568.team24.miniups.repository.AccountRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private AccountRepository accountRepository;

    public CustomUserDetailService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountEntity account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        } else {
            return new User(
                    account.getUsername(),
                    account.getPassword(),
                    List.of(new SimpleGrantedAuthority(account.getRole())));
        }
    }
}
