/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import entities.Account;
import services.account.AccountRepository;
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
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
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
        repo.addAccount(acc);
        acc.setToken(TokenHelper.createToken(acc.getUserName()));
        System.out.println("token:  "+acc.getToken());
        acc.setPassword(passwordEncoder.encode(acc.getPassword()));
        // the rest of the registration operation
        return acc;
    }

    private boolean nameExisted(String name) {
        try {
            Account user = repo.findAccountByName(name);
            System.out.println(name);
            if (user != null) {
                return true;
            }
        } catch (EmptyResultDataAccessException empty){
            return false;
        } catch (Exception ex) {
            log.error(ex.toString());
        }

        return false;
    }

//    @Transactional
//    public String saveUser(String username, String password, String email) {
//
//    }
}
