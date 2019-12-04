package winter.findGuider.web.api;

import entities.Contract;
import entities.Guider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import services.Mail.MailService;
import services.account.AccountRepository;
import services.guider.GuiderService;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;

public class GuiderControllerUnitTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @InjectMocks
    GuiderController guiderController;
    @Mock
    GuiderService guiderService;

    @Mock
    AccountRepository accountRepository;

    @Mock
    MailService mailService;

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateGuider() {
        Contract Contract = Mockito.mock(Contract.class);
        guiderController.createGuider(Contract);
    }

    @Test(expected = AssertionError.class)
    public void testCreateGuiderWithException() throws Exception {
        try {
            thrown.expect(AssertionError.class);
            Contract contract = Mockito.mock(Contract.class);
            when(guiderService.createGuiderContract(contract)).thenThrow(Exception.class);
            ResponseEntity<Boolean> responseEntity = guiderController.createGuider(contract);
        } catch (Exception e) {
            Assert.assertThat(e.getMessage(), containsString("whatever"));
        }

    }

    @Test
    public void testEditGuider() {
        Guider guider = Mockito.mock(Guider.class);
        Contract Contract = Mockito.mock(Contract.class);
        guiderController.editGuider(guider);
    }

    @Test(expected = AssertionError.class)
    public void testEditGuiderWithException() throws Exception {
        try {
            thrown.expect(AssertionError.class);
            Guider guider = Mockito.mock(Guider.class);
            Contract Contract = Mockito.mock(Contract.class);
            when(guiderService.updateGuiderWithId(guider)).thenThrow(Exception.class);
            ResponseEntity<Guider> responseEntity = guiderController.editGuider(guider);
        } catch (Exception e) {
            Assert.assertThat(e.getMessage(), containsString("whatever"));
        }

    }

    @Test
    public void testActivateGuider() {
        Guider guider = Mockito.mock(Guider.class);
        Contract Contract = Mockito.mock(Contract.class);
        guiderController.activateGuider(1);
    }

    @Test(expected = AssertionError.class)
    public void testActivateGuiderWithException() throws Exception {
        try {
            thrown.expect(AssertionError.class);
            when(guiderService.activateGuider(1)).thenThrow(Exception.class);
            ResponseEntity<Guider> responseEntity = guiderController.activateGuider(1);
        } catch (Exception e) {
            Assert.assertThat(e.getMessage(), containsString("whatever"));
        }

    }

    @Test
    public void testDeactivateGuider() {
        Guider guider = Mockito.mock(Guider.class);
        Contract Contract = Mockito.mock(Contract.class);
        guiderController.deactivateGuider(1);
    }

    @Test(expected = AssertionError.class)
    public void testDeactivateGuiderWithException() throws Exception {
        try {
            thrown.expect(AssertionError.class);
            when(guiderService.deactivateGuider(1)).thenThrow(Exception.class);
            ResponseEntity<Guider> responseEntity = guiderController.deactivateGuider(1);
        } catch (Exception e) {
            Assert.assertThat(e.getMessage(), containsString("whatever"));
        }

    }

    @Test
    public void testSearchGuider() {
        ResponseEntity<List<Guider>> result = guiderController.searchGuider("ha");
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testSearchGuiderWithException() throws Exception {
        when(guiderService.searchGuiderByName("ha")).thenThrow(Exception.class);
        ResponseEntity<List<Guider>> result = guiderController.searchGuider("ha");
        Assert.assertEquals(204, result.getStatusCodeValue());
    }

    @Test
    public void testFindGuider() {
        ResponseEntity<Guider> result = guiderController.findGuider(1);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testFindGuiderWithException() throws Exception {
        when(guiderService.findGuiderWithID(1)).thenThrow(Exception.class);
        ResponseEntity<Guider> result = guiderController.findGuider(1);
        Assert.assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void testFindGuiderByPostId() {
        ResponseEntity<Guider> result = guiderController.findGuiderByPostId(1);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testFindGuiderByPostIdWithException() throws Exception {
        when(guiderService.findGuiderWithPostId(1)).thenThrow(Exception.class);
        ResponseEntity<Guider> result = guiderController.findGuiderByPostId(1);
        Assert.assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void testGetTop5GuiderByContribute() throws Exception {
        when(guiderService.getTopGuiderByContribute()).thenThrow(Exception.class);
        ResponseEntity<List<Guider>> result = guiderController.getTop5guiderByContribute();
        Assert.assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void testGetTop5GuiderByRate() throws Exception {
        when(guiderService.getTopGuiderByRate()).thenThrow(Exception.class);
        ResponseEntity<List<Guider>> result = guiderController.getTop5guiderByRate();
        Assert.assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void testAcceptContract() {
        ResponseEntity<Boolean> result = guiderController.acceptContract(1, 1);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testAcceptContractWithException() throws Exception {
        //when(guiderService.linkGuiderWithContract(1,1)).thenThrow(Exception.class);
        ReflectionTestUtils.setField(guiderController, "guiderService", null);

        ResponseEntity<Boolean> result = guiderController.acceptContract(1, 1);
        Assert.assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void testGetAllContract(){
        ResponseEntity<List<Contract>> result = guiderController.getAllContract();
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testGetAllContractWithException() throws Exception{
        when(guiderService.getAllContract()).thenThrow(Exception.class);
        ResponseEntity<List<Contract>> result = guiderController.getAllContract();
        Assert.assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void testRejectContract() {
        ResponseEntity<Boolean> result = guiderController.rejectContract( 1);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void testRejectContractWithException() throws Exception {
        //when(guiderService.linkGuiderWithContract(1,1)).thenThrow(Exception.class);
        ReflectionTestUtils.setField(guiderController, "guiderService", null);

        ResponseEntity<Boolean> result = guiderController.rejectContract( 1);
        Assert.assertEquals(404, result.getStatusCodeValue());
    }
}
