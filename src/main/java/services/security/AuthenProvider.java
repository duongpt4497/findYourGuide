/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.security;

import entities.Account;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.*;
import java.util.Optional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import services.account.AccountRepository;

/**
 *
 * @author dgdbp
 */
@Component
public class AuthenProvider implements AuthenticationProvider {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private PasswordEncoder encoder;
    private AccountRepository userService;

    @Autowired
    public AuthenProvider(AccountRepository userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("to AuthProvider");
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        System.out.println("auth name:  " + username);
        System.out.println("auth pass:  " + password);
        Account acc = null;
        try {
            acc = userService.findAccountByName(username);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BadCredentialsException("Authentication failed");
        }
        grantedAuths.add(new SimpleGrantedAuthority(acc.getRole()));
        System.out.println("db name:  " + acc.getUserName());
        System.out.println("db pass:  " + acc.getPassword());
        if (acc != null && acc.getUserName().equals(username) && encoder.matches(password, acc.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, acc.getId(),grantedAuths);
        } else {
            throw new BadCredentialsException("Authentication failed");
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(UsernamePasswordAuthenticationToken.class);
    }

}
