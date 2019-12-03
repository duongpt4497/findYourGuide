/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winter.findGuider.web.api;

import entities.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.account.AccountRepository;
import security.AuthenProvider;
import security.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

/**
 * @author dgdbp
 */
@RestController
@RequestMapping(path = "/account")
//@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private UserService userService;
    private AccountRepository repo;
    private AuthenProvider auth;

    @Value("${order.client.root.url}")
    private String URL_ROOT_CLIENT;

    @Value("${order.client.root.url.domain}")
    private String URL_ROOT_CLIENT_DOMAIN;

    @Autowired
    public AccountController(AccountRepository repo, UserService userService, AuthenProvider auth) {
        this.userService = userService;
        this.repo = repo;
        this.auth = auth;
    }

    @PostMapping(path = "registrator", consumes = "application/json")
    public ResponseEntity registerUserAccount(@RequestBody Account acc, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://" + URL_ROOT_CLIENT);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        Account registered = null;
        acc.setToken("");
        acc.setExpired(new Date());
        try {
            System.out.println(acc.getPassword() + "/" + acc.getUserName() + "/" + acc.getRole());
            registered = userService.registerNewUserAccount(acc);
            Cookie sidCookie = new Cookie("token", registered.getToken());
            sidCookie.setPath("/");
            sidCookie.setHttpOnly(true);
            sidCookie.setDomain(URL_ROOT_CLIENT_DOMAIN);
            response.addCookie(sidCookie);
            registered.setPassword("");
            registered.setToken("");
        } catch (Exception e) {
            logger.error(e.toString());
            return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // rest of the implementation
        //let log in after registration
        return new ResponseEntity<>(registered, HttpStatus.OK);

    }

    @PostMapping("/logout")
    public void logout() {
    }

    @RequestMapping("/findAll")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Account>> findAllCategory() {
        try {
            return new ResponseEntity(repo.findAllAccount(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
    }
}
