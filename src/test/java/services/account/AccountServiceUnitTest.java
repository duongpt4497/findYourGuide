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
import winter.findGuider.TestDataSourceConfig;

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
    public void testFindByName() throws Exception {
        Account acc = accountService.findAccountByName("Jacky");
        Assert.assertEquals("$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK", acc.getPassword());
        Assert.assertEquals("Jacky@gmail.com", acc.getEmail());
        Assert.assertEquals("GUIDER", acc.getRole());
    }

    @Test
    public void testAddAccount() throws Exception {
        Account account = new Account("test", "123", "test@test.com", "GUIDER");
        jdbcTemplate.update("delete from account where user_name = 'test'");
        int id = accountService.addAccount(account);
        Assert.assertEquals(accountService.findAccountByName("test").getId(), id);
    }

    @Test
    public void testGetEmail() throws Exception {
        jdbcTemplate.update("delete from account");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','TRAVELER')");
        Assert.assertEquals("Jacky@gmail.com", accountService.getEmail(1));
    }

    @Test
    public void findAllAccount() throws Exception {
        jdbcTemplate.update("delete from account");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','TRAVELER')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (2,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','TRAVELER')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (3,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','TRAVELER')");
        Assert.assertEquals(3, accountService.findAllAccount().size());
>>>>>>> a16cb8c8c4e03dc53cf8386b7f7be9b886e984f4
    }
}
