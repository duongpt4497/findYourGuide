/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.security;

import entities.Account;
import entities.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import services.account.AccountRepository;

/**
 *
 * @author dgdbp
 */
@Component
public class PrincipalService implements UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AccountRepository repo;
    
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = null;
        try {
            user = repo.findAccountByName(username);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new UsernameNotFoundException(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new Principal(user);
    }


}
