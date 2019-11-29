package services.account;

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

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceUnitTest {

    @InjectMocks
    AccountRepository accountService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    private Account account;

    @Before
    public void init() {
        account = new Account("test", "123", "GUIDER");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByName() {
        Assert.assertEquals(null, accountService.findByName("Jacky"));
    }
}
