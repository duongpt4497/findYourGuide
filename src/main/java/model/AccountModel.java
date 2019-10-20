/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Account;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/*
 * @author dgdbp
 */
@Repository
public class AccountModel {

    //@Autowired
    private JdbcTemplate jdbc;

    @Autowired
    public AccountModel(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Account findActorById(Long id) {
        return jdbc.queryForObject("select * from actor where actor_id = ?",
                new RowMapper<Account>() {
            public Account mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return new Account(
                        rs.getLong("actor_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("last_update"));
            }
         ;
            }, id);
    }

    public List<Account> findAllActors() {
        return jdbc.query("select * from actor ", this::mapRow);
    }

    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Account(
                rs.getLong("actor_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getDate("last_update"));
    }
}
