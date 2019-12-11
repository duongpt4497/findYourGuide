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
import services.Mail.MailService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author dgdbp
 */
@Repository
public class AccountRepository {
    private JdbcTemplate jdbc;
    private MailService mailService;

    @Autowired
    public AccountRepository(JdbcTemplate jdbc, MailService ms) {
        this.jdbc = jdbc;
        this.mailService = ms;
    }

    public Account findAccountByName(String name) throws Exception {
        return jdbc.queryForObject("select * from account where user_name=?", new RowMapper<Account>() {
            @Override
            public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Account(
                        rs.getLong("account_id"),
                        rs.getString("user_name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("role"));
            }
        }, name);
    }

    public String findAccountNameByAccountId(int account_id) throws Exception {
        Account account = jdbc.queryForObject("select * from account where account_id=?", new RowMapper<Account>() {
            @Override
            public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Account(
                        rs.getLong("account_id"),
                        rs.getString("user_name"),
                        rs.getString("email"),
                        rs.getString("role"));
            }
        }, account_id);
        return account.getUserName();
    }

    public int addAccount(Account acc) throws Exception {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = " INSERT INTO public.account( "
                + " user_name, password, email ,role ) "
                + " VALUES ( ?, ?, ?, ? ) ";
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, new String[]{"account_id"});
            ps.setString(1, acc.getUserName());
            ps.setString(2, acc.getPassword());
            ps.setString(3, acc.getEmail());
            ps.setString(4, acc.getRole());
            return ps;
        }, keyHolder);
        return (int) keyHolder.getKey();
    }

    public int changePassword(String name, String pass) throws Exception {
        String query = "update account set password = ? where user_name = ? ; ";
        return jdbc.update(query, pass, name);
    }

    public String getEmail(int account_id) throws Exception {
        String query = "select email from account where account_id = ?";
        return jdbc.queryForObject(query, new Object[]{account_id}, String.class);
    }

    public List<Account> findAllAccount() throws Exception {
        List<Account> result = new ArrayList<>();
        String query = "SELECT account_id, user_name, email, role, active FROM account left join guider on account_id = guider_id";
        result = jdbc.query(query, new RowMapper<Account>() {
            @Override
            public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Account(rs.getInt("account_id"), rs.getString("user_name"),
                        rs.getString("email"), rs.getString("role"), rs.getBoolean("active"));
            }
        });
        return result;
    }

    public String insertEmailConfirmToken(long account_id) throws Exception {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = (int) (Math.random() * ((50 - 20) + 1)) + 20;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String token = buffer.toString();
        jdbc.update("update account set email_token = ? where account_id = ?", token, account_id);
        return account_id + "e" + token;
    }

    public void confirmEmail(String token) throws Exception {
        String[] data = token.split("e", 2);
        int account_id = Integer.parseInt(data[0]);
        String checkToken = data[1];
        String query = "select email_token from account where account_id = ?";
        String email_token = jdbc.queryForObject(query, new Object[]{account_id}, String.class);
        String content = "";
        if (email_token.equals(checkToken)) {
            // Change verified status
            jdbc.update("update account set email_verified = true where account_id = ?", account_id);
            // Mail content
            content = "Your email has been verified !\n" +
                    "Have a nice day\n\n";
            content = content.concat("Sincerely,\nTravelWLocal");
        } else {
            content = "Your email has not been verified !\n" + "Please try again later !" +
                    "Have a nice day\n\n";
            content = content.concat("Sincerely,\nTravelWLocal");
        }
        // Send result email
        String email = this.getEmail(account_id);
        mailService.sendMail(email, "TravelWLocal Email Confirmation", content);
    }
}
