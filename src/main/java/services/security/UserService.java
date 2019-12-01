/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.security;

import entities.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import services.account.AccountRepository;

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
        repo.addAccount(acc);
        // the rest of the registration operation
        return acc;
    }

    private boolean nameExisted(String name) throws Exception {
        try {
            Account user = repo.findAccountByName(name);
            if (user != null) {
                return true;
            }
        } catch (EmptyResultDataAccessException empty) {
            return false;
        }
        return false;
    }
}
