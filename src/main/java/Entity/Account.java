/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.Date;
import lombok.Data;
import lombok.RequiredArgsConstructor;
/**
 *
 * @author dgdbp
 */
@Data
public class Account {
    private long id;
    private String first;
    private String last;
    private Date now;

    public Account(long id, String first, String last, Date now) {
        this.id = id;
        this.first = first;
        this.last = last;
        this.now = now;
    }
    
    
}
