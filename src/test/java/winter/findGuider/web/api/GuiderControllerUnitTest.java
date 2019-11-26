package winter.findGuider.web.api;

import entities.Guider;
import entities.Guider_Contract;
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
import services.Feedback.FeedbackService;
import services.guider.GuiderService;

import javax.xml.ws.Response;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;

public class GuiderControllerUnitTest {
    @InjectMocks
    GuiderController guiderController;

    @Mock
    GuiderService guiderService;

    @Rule
    public ExpectedException thrown= ExpectedException.none();
    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateGuider(){
        Guider guider = Mockito.mock(Guider.class);
        Guider_Contract guider_contract = Mockito.mock(Guider_Contract.class);
        guiderController.createGuider(guider,guider_contract);
    }

    @Test(expected = AssertionError.class)
    public void testCreateGuiderWithException() throws Exception{
        try{
            thrown.expect(AssertionError.class);
            Guider guider = Mockito.mock(Guider.class);
            Guider_Contract guider_contract = Mockito.mock(Guider_Contract.class);
            when(guiderService.createGuider(guider)).thenThrow(Exception.class);
            ResponseEntity<Guider> responseEntity = guiderController.createGuider(guider,guider_contract);
        }catch(Exception e ){
            Assert.assertThat(e.getMessage(), containsString("whatever"));
        }

    }

    @Test
    public void testEditGuider(){
        Guider guider = Mockito.mock(Guider.class);
        Guider_Contract guider_contract = Mockito.mock(Guider_Contract.class);
        guiderController.editGuider(guider);
    }

    @Test(expected = AssertionError.class)
    public void testEditGuiderWithException() throws Exception{
        try{
            thrown.expect(AssertionError.class);
            Guider guider = Mockito.mock(Guider.class);
            Guider_Contract guider_contract = Mockito.mock(Guider_Contract.class);
            when(guiderService.updateGuiderWithId(guider)).thenThrow(Exception.class);
            ResponseEntity<Guider> responseEntity = guiderController.editGuider(guider);
        }catch(Exception e ){
            Assert.assertThat(e.getMessage(), containsString("whatever"));
        }

    }

    @Test
    public void testActivateGuider(){
        Guider guider = Mockito.mock(Guider.class);
        Guider_Contract guider_contract = Mockito.mock(Guider_Contract.class);
        guiderController.activateGuider(1);
    }

    @Test(expected = AssertionError.class)
    public void testActivateGuiderWithException() throws Exception{
        try{
            thrown.expect(AssertionError.class);
            when(guiderService.activateGuider(1)).thenThrow(Exception.class);
            ResponseEntity<Guider> responseEntity = guiderController.activateGuider(1);
        }catch(Exception e ){
            Assert.assertThat(e.getMessage(), containsString("whatever"));
        }

    }

    @Test
    public void testDeactivateGuider(){
        Guider guider = Mockito.mock(Guider.class);
        Guider_Contract guider_contract = Mockito.mock(Guider_Contract.class);
        guiderController.deactivateGuider(1);
    }

    @Test(expected = AssertionError.class)
    public void testDeactivateGuiderWithException() throws Exception{
        try{
            thrown.expect(AssertionError.class);
            when(guiderService.deactivateGuider(1)).thenThrow(Exception.class);
            ResponseEntity<Guider> responseEntity = guiderController.deactivateGuider(1);
        }catch(Exception e ){
            Assert.assertThat(e.getMessage(), containsString("whatever"));
        }

    }

}
