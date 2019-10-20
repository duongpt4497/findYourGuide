/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author dgdbp
 */
@Data
public class User {
    private long id;
    private String username;
    private String password;
    private boolean isActive;
    private String email;
    private String role;
    private String token;
    private Date expiresAt;

    public User(long id, String username, String password, boolean isActive, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.email = email;
        this.role = role;
        token = null;
        expiresAt = null;
    }

    public User() {
    }
    
    
   
}
