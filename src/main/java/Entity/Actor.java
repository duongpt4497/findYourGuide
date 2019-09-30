/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.sql.Date;

/**
 *
 * @author dgdbp
 */
public class Actor {
    private int id;
    private String first;
    private String last;
    private Date now;

    public Actor() {
    }

    public Actor(int id, String first, String last, Date now) {
        this.id = id;
        this.first = first;
        this.last = last;
        this.now = now;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    @Override
    public String toString() {
        return String.format("%s %s at %s", first, last, now.toString());
    }
    
    
}
