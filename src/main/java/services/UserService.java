/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import repository.UserRepo;
import entity.Account;
import repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dgdbp
 */
@Service
public class UserService {

    private UserRepo repo;
    private PasswordEncoder passwordEncoder;
    private TokenHelper TokenHelper;

    @Autowired
    public UserService(UserRepo repo, PasswordEncoder passwordEncoder, TokenHelper tokenService) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.TokenHelper = tokenService;
    }

    //add register user here
    @Transactional
    public Account registerNewUserAccount(Account acc)
            throws Exception {

        if (nameExisted(acc.getUserName())) {
            throw new Exception(
                    "There is an account with that user name: "
                    + acc.getUserName());
        }

        acc.setToken(TokenHelper.createToken(acc.getUserName()));
        System.out.println("token:  "+acc.getToken());
        acc.setPassword(passwordEncoder.encode(acc.getPassword()));
        // the rest of the registration operation
        return acc;
    }

    private boolean nameExisted(String name) {
        try {
            Account user = repo.findByName(name);
            System.out.println(name);
            if (user != null) {
                return true;
            }
        } catch (EmptyResultDataAccessException empty){
            return false;
        }

        return false;
    }

//    @Transactional
//    public String saveUser(String username, String password, String email) {
//
//    }
}
