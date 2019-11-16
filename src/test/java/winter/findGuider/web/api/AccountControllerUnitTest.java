package winter.findGuider.web.api;

import entities.Activity;

import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import services.Activity.ActivityServiceImpl;
import services.GeneralServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AccountControllerUnitTest {
    @InjectMocks
    ActivityApi activityApi;

    @Mock
    ActivityServiceImpl activityService;

    @Mock
    GeneralServiceImpl generalService;

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testFindActivity() throws Exception {

        Activity activity = new Activity(1, "this is a brief", "this is a detail");
         when(activityService.findActivityOfAPost(1)).thenReturn(Arrays.asList(activity));

        List<Activity> activities = activityApi.findActivity(1);
        assertEquals(1, activities.size());

    }
}
