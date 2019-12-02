package winter.findGuider.web.api;

import entities.Plan;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import services.plan.PlanService;

import static org.mockito.Mockito.when;

public class PlanControllerUnitTest {
    @InjectMocks
    PlanController planController;

    @Mock
    PlanService planService;

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Before
    public void init() {
         MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreatePlan(){
        Plan plan = new Plan();
        when(planService.createPlan(plan)).thenReturn(1);
        ResponseEntity<Integer> result = planController.createPlan(plan);
    }
    @Test(expected = AssertionError.class)
    public void testCreatePlanWithException() throws Exception{
        thrown.expect(AssertionError.class);
        Plan plan = new Plan();
        when(planService.createPlan(plan)).thenThrow(Exception.class);
        ResponseEntity<Integer> result = planController.createPlan(plan);
    }
    @Test
    public void testUpdatePlan(){
        Plan plan = Mockito.mock(Plan.class);
        //when(planService.createPlan(plan)).thenReturn(1);
        ResponseEntity<Plan> result = planController.updatePlan(plan);
    }
    @Test(expected = AssertionError.class)
    public void testUpdatePlanWithException() throws Exception{
        thrown.expect(AssertionError.class);
        Plan plan = Mockito.mock(Plan.class);
        //when(planService.updatePlan(plan.getPost_id(), plan.getMeeting_point(), plan.getDetail())).thenThrow(Exception.class);
        ResponseEntity<Plan> result = planController.updatePlan(plan);
    }

    @Test
    public void testFindPlan(){
        Plan plan = Mockito.mock(Plan.class);
        //when(planService.createPlan(plan)).thenReturn(1);
        ResponseEntity<Plan> result = planController.findPlan(1);
    }
    @Test(expected = AssertionError.class)
    public void testFindPlanWithException() throws Exception{
        thrown.expect(AssertionError.class);
        Plan plan = Mockito.mock(Plan.class);
        when(planService.searchPlanByPostId(1)).thenThrow(Exception.class);
        ResponseEntity<Plan> result = planController.updatePlan(plan);
    }
}
