package services.account;

import entities.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceUnitTest {

    @InjectMocks
    AccountRepository accountServiceActual;

    @Mock
    AccountRepository activityService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByName() throws Exception {
        Account test = new Account("test", "123", "GUIDER");
        when(activityService.findByName("test")).thenReturn(test);

        Account account = accountServiceActual.findByName("test");
        assertEquals(test, account);
    }
}
