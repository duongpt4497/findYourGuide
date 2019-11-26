package services.contributionPoint;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class ContributionPointServiceImplTest {

    @InjectMocks
    ContributionPointServiceImpl contributionPointService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Before
    public void init() {
        ReflectionTestUtils.setField(contributionPointService, "corMoney", "10");
        ReflectionTestUtils.setField(contributionPointService, "corRated", "100");
        ReflectionTestUtils.setField(contributionPointService, "corTurn", "200");
        ReflectionTestUtils.setField(contributionPointService, "minus", "5000");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void calculateContributionPerTour() {
        Assert.assertEquals(2100, contributionPointService.calculateContributionPerTour(10, 5, 10));
    }

    @Test
    public void updateContributionByDay() {
        contributionPointService.updateContributionbyDay();
    }

    @Test
    public void updateContributionByMonth() {
        contributionPointService.updateContributionbyMonth();
    }
}