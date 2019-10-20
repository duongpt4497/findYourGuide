/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Account;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import entity.Guider;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author dgdbp
 */
@Repository
public class GuiderRepo {
    private JdbcTemplate jdbc;

    @Autowired
    public GuiderRepo(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    
    public List<Guider> getAllGuiders() {
        return jdbc.query("select guider_id,first_name, about_me,city from guider ", this::mapRow);
    }
        public Guider mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Guider(
                rs.getLong("guider_id"),
                rs.getString("first_name"),
                rs.getString("about_me"),
                rs.getString("city"));
    }
}
