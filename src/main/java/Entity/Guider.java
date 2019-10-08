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
public class Guider {
    private long id;
    private String first;
    private String about;
    private String city;

    public Guider(long id, String first, String about, String city) {
        this.id = id;
        this.first = first;
        this.about = about;
        this.city = city;
    }
    
}
