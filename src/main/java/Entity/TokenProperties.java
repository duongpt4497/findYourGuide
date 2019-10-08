/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import lombok.Data;

/**
 *
 * @author dgdbp
 */
@Data

public class TokenProperties {
    private long maxAge;
    private String secret;

    public TokenProperties(long maxAge, String secret) {
        this.maxAge = maxAge;
        this.secret = secret;
    }

    public TokenProperties() {
    }
    
    
    
}
