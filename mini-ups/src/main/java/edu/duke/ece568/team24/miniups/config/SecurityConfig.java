package edu.duke.ece568.team24.miniups.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import edu.duke.ece568.team24.miniups.security.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private CustomUserDetailService customUserDetailService;

    public SecurityConfig(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/", "/package/detail", "/account/signup", "/account/login")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/account/login")
                .defaultSuccessUrl("/?loginsuccess=true")
                .failureUrl("/account/login?loginfail=true")
                .and()
                .logout()
                .logoutUrl("/account/logout")
                .logoutSuccessUrl("/account/login?logoutsuccess=true");

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // If @EnableWebSecurity, Spring will find the method called "configure" and
    // invoke it.
    public void configure(AuthenticationManagerBuilder authBldr) throws Exception {
        authBldr.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
    }

}
