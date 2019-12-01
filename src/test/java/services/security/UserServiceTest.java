package services.security;

import entities.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import services.account.AccountRepository;
import winter.findGuider.TestDataSourceConfig;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    AccountRepository repo;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    TokenHelper tokenHelper;

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Before
    public void init() {
        TestDataSourceConfig config = new TestDataSourceConfig();
        jdbcTemplate.setDataSource(config.setupDatasource());
        repo = new AccountRepository(jdbcTemplate);
        userService = new UserService(repo, passwordEncoder, tokenHelper);
        config.cleanTestDb(jdbcTemplate);
        jdbcTemplate.update("insert into account (user_name, password, email ,role) " +
                "values ('Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void registerNewUserAccount() throws Exception {
        Account acc = new Account("Tom", "123", "Tom@gmail.com", "GUIDER");
        when(passwordEncoder.encode(acc.getPassword())).thenReturn("123");
        userService.registerNewUserAccount(acc);
        Assert.assertEquals("Tom", repo.findAccountByName("Tom").getUserName());
    }

    @Test(expected = Exception.class)
    public void registerNewUserAccount2() throws Exception {
        Account acc = new Account("Jacky", "123", "Tom@gmail.com", "GUIDER");
        userService.registerNewUserAccount(acc);
        Assert.assertEquals("Tom", repo.findAccountByName("Tom").getUserName());
    }
}