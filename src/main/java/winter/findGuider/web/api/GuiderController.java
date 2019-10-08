/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winter.findGuider.web.api;

import entity.Account;
import java.util.List;
import model.AccountModel;
import model.GuiderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(path="/guider", produces="application/json")
@CrossOrigin(origins="*")
public class GuiderController {
    private GuiderRepo accRepo;
    
    @Autowired
    public GuiderController(GuiderRepo am) {
        accRepo = am;
    }
    @GetMapping("/all")
    public ResponseEntity<List<Account>> accByAll() {
     
        return new ResponseEntity(accRepo.getAllGuiders(), HttpStatus.OK);
    }
}
