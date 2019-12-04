package winter.findGuider.web.api;



import entities.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;

import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import services.account.AccountRepository;
import security.AuthenProvider;
import security.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AccountControllerUnitTest {
    @InjectMocks
    AccountController accountController;

    @Mock
    AccountRepository accountRepository;

    @Mock
    UserService userService;

    @Mock
    AuthenProvider authenProvider;

    @Rule
    public ExpectedException thrown= ExpectedException.none();
    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testRegisterUserAccount() throws Exception{
        Account account = Mockito.mock(Account.class);
        Account acc = new Account();
        acc.setToken("123");
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        ReflectionTestUtils.setField(accountController, "URL_ROOT_CLIENT_DOMAIN", "localhost");
        when(userService.registerNewUserAccount(account)).thenReturn(acc);
        ResponseEntity result = accountController.registerUserAccount(account,httpServletResponse);
        Assert.assertEquals(200,result.getStatusCodeValue());
    }


    @Test
    public void testRegisterUserAccountWithException(){
        Account account = Mockito.mock(Account.class);
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        ResponseEntity result = accountController.registerUserAccount(account,httpServletResponse);
        Assert.assertEquals(500,result.getStatusCodeValue());
    }

    @Test
    public void testLogout(){
         accountController.logout();
    }

    @Test
    public void testFindAllCategory() throws Exception{
        when(accountRepository.findAllAccount()).thenThrow(Exception.class);
        ResponseEntity<List<Account>> result = accountController.findAll();
    }
}
