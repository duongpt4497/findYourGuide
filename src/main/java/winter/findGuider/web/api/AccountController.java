/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winter.findGuider.web.api;

import entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.account.AccountRepository;
import services.security.AuthProvider;
import services.security.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author dgdbp
 */
@RestController
@RequestMapping(path = "/account")
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {

    private UserService userService;
    private AccountRepository repo;
    private AuthProvider auth;

    @Autowired
    public AccountController(AccountRepository repo, UserService userService, AuthProvider auth) {
        this.userService = userService;
        this.repo = repo;
        this.auth = auth;
    }

    @PostMapping(path = "registrator", consumes = "application/json")
    public ResponseEntity registerUserAccount(@RequestBody Account acc, HttpServletResponse response) {

        Account registered = null;
        acc.setToken("");
        acc.setExpired(new Date());
        try {
            System.out.println(acc.getPassword() + "/" + acc.getUserName() + "/" + acc.getRole());
            registered = userService.registerNewUserAccount(acc);
            Cookie sidCookie = new Cookie("token", registered.getToken());
            sidCookie.setPath("/");
//            sidCookie.setSecure(true);
            sidCookie.setHttpOnly(true);
            sidCookie.setDomain("localhost");
            response.addCookie(sidCookie);
            repo.addAccount(registered);
            registered.setPassword("");
            registered.setToken("");
            System.out.println(acc.getExpired());
        } catch (Exception e) {
            return new ResponseEntity<>("regist fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // rest of the implementation
        return new ResponseEntity<>(registered.getToken(), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public void logout() {
    }


    @GetMapping()
    public ResponseEntity blahblah() {

        return new ResponseEntity<>("leuleu!!", HttpStatus.OK);
    }

    @RequestMapping("/findAll")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Account>> findAllCategory() {
        try {
            return new ResponseEntity(repo.findAllAccount(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
    }
}
