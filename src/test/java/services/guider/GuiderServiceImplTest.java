package services.guider;

import entities.Guider;
import entities.Guider_Contract;
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
public class GuiderServiceImplTest {

    @InjectMocks
    GuiderServiceImpl guiderService;
    Guider guider;
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Before
    public void init() {
        guider = new Guider();
        guider.setGuider_id(1);
        guider.setFirst_name("test");
        guider.setLast_name("test");
        guider.setAge(21);
        guider.setAbout_me("test");
        guider.setCity("hanoi");
        guider.setLanguages(new String[]{"en", "vi"});
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findGuiderWithID() {
        Assert.assertEquals(null, guiderService.findGuiderWithID(1));
    }

    @Test
    public void findGuiderWithPostId() {
        Assert.assertEquals(null, guiderService.findGuiderWithPostId(1));
    }

    @Test
    public void findGuiderContract() {
        Assert.assertEquals(null, guiderService.findGuiderContract(1));
    }

    @Test(expected = NullPointerException.class)
    public void createGuider() {
        Assert.assertEquals(null, guiderService.createGuider(guider));
    }

    @Test
    public void createGuiderContract() {
        Guider_Contract contract = new Guider_Contract();
        guiderService.createGuiderContract(1, contract);
    }

    @Test
    public void updateGuiderWithId() {
        Assert.assertEquals(1, guiderService.updateGuiderWithId(guider));
    }

    @Test
    public void activateGuider() {
        Assert.assertEquals(1, guiderService.activateGuider(1));
    }

    @Test
    public void deactivateGuider() {
        Assert.assertEquals(1, guiderService.deactivateGuider(1));
    }

    @Test
    public void searchGuider() {
        Assert.assertTrue(guiderService.searchGuider("abc").isEmpty());
    }

    @Test
    public void getTopGuiderByRate() {
        Assert.assertTrue(guiderService.getTopGuiderByRate().isEmpty());
    }

    @Test
    public void getTopGuiderByContribute() {
        Assert.assertTrue(guiderService.getTopGuiderByContribute().isEmpty());
    }
}