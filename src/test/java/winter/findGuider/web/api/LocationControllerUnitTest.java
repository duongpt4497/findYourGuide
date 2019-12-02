package winter.findGuider.web.api;

import entities.Location;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import services.Location.LocationServiceImpl;
import services.guider.GuiderService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;

public class LocationControllerUnitTest {
    @InjectMocks
    LocationController locationController;

    @Mock
    LocationServiceImpl locationService;

    @Rule
    public ExpectedException thrown= ExpectedException.none();
    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllLocation(){
        List<Location> listLocation = new ArrayList<>();
        //when(locationService.showAllLocation()).then()

        ResponseEntity<Location> result =locationController.findAllLocation();
        Assert.assertEquals(200, result.getStatusCodeValue());
        //Assert.assertEquals(true, result.equals());
    }

    @Test(expected = AssertionError.class)
    public void testLocationWithException() throws Exception{
        thrown.expect(AssertionError.class);
        when(locationService.showAllLocation()).thenThrow(Exception.class);
        ResponseEntity<Location> result =locationController.findAllLocation();


        Assert.assertEquals(null, result);
    }


}
