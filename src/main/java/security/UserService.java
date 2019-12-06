/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import entities.Account;
import entities.Guider;
import entities.Traveler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import services.account.AccountRepository;
import services.guider.GuiderService;
import services.traveler.TravelerService;

/**
 * @author dgdbp
 */
@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private AccountRepository repo;
    private PasswordEncoder passwordEncoder;
    private TokenHelper TokenHelper;
    @Autowired
    private GuiderService gs;
    @Autowired
    private TravelerService ts;

    @Autowired
    public UserService(AccountRepository repo, PasswordEncoder passwordEncoder, TokenHelper tokenService) {
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
        acc.setPassword(passwordEncoder.encode(acc.getPassword()));
        long id = repo.addAccount(acc);
        if (acc.getRole().equalsIgnoreCase("GUIDER")) {
            gs.createGuider(new Guider(id, "", "", 0, "", "", 0, "", new String[0], false, 0, "account.jpg", ""));
        } else if (acc.getRole().equalsIgnoreCase("TRAVELER")) {
            ts.createTraveler(new Traveler(id, "", "", "", 0, new java.sql.Timestamp(
                    new Date().getTime()).toLocalDateTime(), "", "", "", "", "", new String[0], "", "", "account.jpg"));
        }
        // the rest of the registration operation
        return acc;
    }

    private boolean nameExisted(String name) throws Exception {
        Account user = null;
        try {
            
                user = repo.findAccountByName(name);


            if (user != null) {
                return true;
            }
        } catch (EmptyResultDataAccessException empty) {
            return false;
        }
        return false;
    }
}
