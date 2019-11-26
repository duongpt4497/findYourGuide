package services.plan;

import entities.Plan;
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
public class PlanServiceImplTest {

    @InjectMocks
    PlanServiceImpl planService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NullPointerException.class)
    public void createPlan() {
        Assert.assertEquals(null, planService.createPlan(new Plan()));
    }

    @Test
    public void searchPlanByPlanId() {
        Assert.assertEquals(null, planService.searchPlanByPlanId(1));
    }

    @Test
    public void searchPlanByPostId() {
        Assert.assertEquals(null, planService.searchPlanByPostId(1));
    }

    @Test
    public void updatePlan() {
        planService.updatePlan(1, "a", "a");
    }
}