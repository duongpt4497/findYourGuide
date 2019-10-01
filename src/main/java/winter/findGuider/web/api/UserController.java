/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winter.findGuider.web.api;

import entity.Account;
import java.util.List;
import model.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author dgdbp
 */
@RestController
@RequestMapping(path="/find", produces="application/json")
@CrossOrigin(origins="*")
public class UserController {
    //@Autowired
    private AccountModel accRepo;
    
    @Autowired
    public UserController(AccountModel am) {
        accRepo = am;
    }
    
    
    @GetMapping("/{id}")
    public Account accById(@PathVariable("id") Long id) {
        //return id;
        return accRepo.findActorById(id);
    }
    @GetMapping("/all")
    public List<Account> accByAll() {
        //return id;
        return accRepo.findAllActors();
    }
}
