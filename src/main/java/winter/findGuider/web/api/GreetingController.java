/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winter.findGuider.web.api;

import entity.Account;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dgdbp
 */
@RestController
@RequestMapping(path="/greeting")
public class GreetingController {
    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody Account acc) {
        return null;
    }
}
