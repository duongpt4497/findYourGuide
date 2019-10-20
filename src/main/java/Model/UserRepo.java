/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import entity.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author dgdbp
 */
@Repository
public class UserRepo {
    private JdbcTemplate jdbc;

    @Autowired
    public UserRepo(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    
    public User findByEmail(String email) {
        return jdbc.queryForObject("select staff_id, username, password, active, store_id from actor where staff where email=?",
                new RowMapper<User>() {
            public User mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return new User(
                        rs.getLong("staff_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("active"),
                        email,
                        rs.getString("store_id")); 
            }
         ;
            }, email);
    }
    
    public User saveAndFlush() {
        return null;
    }
}
