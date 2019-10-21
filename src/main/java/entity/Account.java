/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 *
 * @author dgdbp
 */
@Data
@Component
public class Account {

    private long id;
//    @NotNull
//    @NotEmpty
    private String userName;
//    @NotNull
//    @NotEmpty
    private String password;
//    @NotNull
//    @NotEmpty
    private String role;
    private String token;
    private Date expired;

    public Account(long id, String userName, String password, String role, String token, Date expired) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.token = token;
        this.expired = expired;
    }
    public Account(long id, String userName, String password, String role) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
     
    }

    public Account() {
    }

    public Account(String userName, String password, String role) {

        this.userName = userName;
        this.password = password;
        this.role = role;
    }   
    
    

}
