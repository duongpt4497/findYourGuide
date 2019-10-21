/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entity.Account;
import entity.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import repository.UserRepo;

/**
 *
 * @author dgdbp
 */
@Component
public class PrincipalService implements UserDetailsService {

    @Autowired
    private UserRepo repo;
    
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = repo.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new Principal(user);
    }


}
