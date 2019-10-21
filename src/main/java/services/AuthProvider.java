/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entity.Account;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import repository.UserRepo;

/**
 *
 * @author dgdbp
 */
@Component
public class AuthProvider implements AuthenticationProvider {
    private PasswordEncoder encoder;
    private UserRepo userService;

    @Autowired
    public AuthProvider(UserRepo userService, PasswordEncoder encoder) {
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
        Account acc = userService.findByName(username);
        grantedAuths.add(new SimpleGrantedAuthority(acc.getRole()));
        System.out.println("db name:  " + acc.getUserName());
        System.out.println("db pass:  " + acc.getPassword());
        if (acc != null && acc.getUserName().equals(username) && encoder.matches(password, acc.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password,grantedAuths);
        } else {
            throw new BadCredentialsException("Authentication failed");
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(UsernamePasswordAuthenticationToken.class);
    }

}
