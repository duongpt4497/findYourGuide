/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.account;

import entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author dgdbp
 */
@Repository
public class AccountRepository {

    private JdbcTemplate jdbc;

    @Autowired
    public AccountRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Account findByName(String name) {
        try {
            return jdbc.queryForObject("select * from account where user_name=?", new RowMapper<Account>() {
                @Override
                public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Account(
                            rs.getLong("account_id"),
                            rs.getString("user_name"),
                            rs.getString("password"),
                            rs.getString("role"));
                }
            }, name);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public int addAccount(Account acc) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            String query = " INSERT INTO public.account( "
                    + " user_name, password ,role ) "
                    + " VALUES ( ?, ?, ? ) ";
            jdbc.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(query, new String[]{"account_id"});
                ps.setString(1, acc.getUserName());
                ps.setString(2, acc.getPassword());
                ps.setString(3, acc.getRole());
                return ps;
            }, keyHolder);
        } catch (Exception e) {
            System.out.println(e);
        }
        return (int) keyHolder.getKey();
    }

}
