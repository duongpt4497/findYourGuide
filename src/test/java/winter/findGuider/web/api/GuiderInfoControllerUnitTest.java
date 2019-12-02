package winter.findGuider.web.api;

import entities.Guider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.guider.GuiderService;

public class GuiderInfoControllerUnitTest {
    @InjectMocks
    GuiderInfoController guiderInfoController;

    @Mock
    GuiderService guiderService;

    @Rule
    public ExpectedException thrown= ExpectedException.none();
    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindGuider(){

        Guider guider = guiderInfoController.findGuider(1);
        //Assert.assertEquals(1,1);
    }

    @Test
    public void testFindGuiderByPostById(){
        Guider guider = guiderInfoController.findGuiderByPostId(1);
    }
}
