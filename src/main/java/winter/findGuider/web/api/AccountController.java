/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winter.findGuider.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import services.security.UserService;
import entities.Account;
import java.util.Date;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import services.security.AuthenProvider;
import services.account.AccountRepository;

/**
 *
 * @author dgdbp
 */
@RestController
@RequestMapping(path = "/account")
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {

    private UserService userService;
    private AccountRepository repo;
    private AuthenProvider auth;
    @Autowired
    public AccountController(AccountRepository repo,UserService userService, AuthenProvider auth){
        this.userService = userService;
        this.repo = repo;
        this.auth = auth;
    }

    @PostMapping(path="registrator", consumes="application/json")
    public ResponseEntity registerUserAccount( @RequestBody Account acc, HttpServletResponse response) {
        
        Account registered = null;
        acc.setToken("");
        acc.setExpired(new Date());
        try {
            System.out.println(acc.getPassword()+"/"+acc.getUserName()+"/"+acc.getRole());
            registered = userService.registerNewUserAccount(acc);
             Cookie sidCookie = new Cookie("token",registered.getToken());
            sidCookie.setPath("/");
//            sidCookie.setSecure(true);
            sidCookie.setHttpOnly(true);
            sidCookie.setDomain("localhost");
            response.addCookie(sidCookie);
//            repo.addAccount(registered);
            registered.setPassword("");
            registered.setToken("");
            System.out.println(acc.getExpired());
        } catch (Exception e) {
            return new ResponseEntity<>( "regist fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // rest of the implementation
        return new ResponseEntity<>( registered.getToken(), HttpStatus.OK);
    }
    
    @PostMapping("/logout")
    public void logout() {
    }
  
    
    @GetMapping()
    public ResponseEntity blahblah( ) {
      
        return new ResponseEntity<>( "leuleu!!", HttpStatus.OK);
    }
 

}
