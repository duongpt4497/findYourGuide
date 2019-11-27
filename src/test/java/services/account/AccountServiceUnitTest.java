package services.account;

import entities.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import winter.findGuider.TestDataSourceConfig;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceUnitTest {
    @InjectMocks
    AccountRepository accountService;

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Before
    public void init() {
        TestDataSourceConfig config = new TestDataSourceConfig();
        jdbcTemplate.setDataSource(config.setupDatasource());
        accountService = new AccountRepository(jdbcTemplate);
        config.cleanTestDb(jdbcTemplate);
        jdbcTemplate.update("insert into account (user_name, password, email ,role) " +
                "values ('Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByName() {
        Assert.assertEquals("$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK", accountService.findByName("Jacky").getPassword());
        Assert.assertEquals("Jacky@gmail.com", accountService.findByName("Jacky").getEmail());
        Assert.assertEquals("GUIDER", accountService.findByName("Jacky").getRole());
    }

    @Test(expected = Exception.class)
    public void testFindByName2() {
        when(jdbcTemplate.queryForObject("select * from account where user_name=?", new RowMapper<Account>() {
            @Override
            public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Account(
                        rs.getLong("account_id"),
                        rs.getString("user_name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("role"));
            }
        }, "Jacky")).thenThrow(Exception.class);
        Assert.assertEquals("$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK", accountService.findByName("Jacky").getPassword());
        Assert.assertEquals("Jacky@gmail.com", accountService.findByName("Jacky").getEmail());
        Assert.assertEquals("GUIDER", accountService.findByName("Jacky").getRole());
    }

    @Test
    public void testAddAccount() {
        Account account = new Account("test", "123", "test@test.com", "GUIDER");
        jdbcTemplate.update("delete from account where user_name = 'test'");
        int id = accountService.addAccount(account);
        Assert.assertEquals(accountService.findByName("test").getId(), id);
    }

    @Test(expected = Exception.class)
    public void testAddAccount2() {
        Account account = new Account();
        jdbcTemplate.update("delete from account where user_name = 'test'");
        int id = accountService.addAccount(account);
        Assert.assertEquals(accountService.findByName("test").getId(), id);
    }
}
