package services.account;

import entities.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceUnitTest {

    @InjectMocks
    AccountRepository accountServiceActual;

    @Mock
    JdbcTemplate jdbcTemplate;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByName() throws Exception {
        Account test = new Account("test", "123", "GUIDER");
        int id =1;
        String name = "test";
        /*when(jdbcTemplate.queryForObject("select * from account where user_name=?",
                this::mapRow, name)).thenReturn(test);
*/
        Account account = accountServiceActual.findByName("test");
        Assert.assertEquals(null,account);
    }

    public Account mapRow(ResultSet rs, int rowNum)
            throws SQLException {
        return new Account(
                rs.getLong("account_id"),
                rs.getString("user_name"),
                rs.getString("password"),
                rs.getString("role"));
    }
}
