package services.Location;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import services.GeneralService;
import winter.findGuider.TestDataSourceConfig;

@RunWith(MockitoJUnitRunner.class)
public class LocationServiceImplTest {

    @InjectMocks
    LocationServiceImpl locationService;

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Mock
    private GeneralService generalService;

    @Before
    public void init() {
        TestDataSourceConfig config = new TestDataSourceConfig();
        jdbcTemplate.setDataSource(config.setupDatasource());
        locationService = new LocationServiceImpl(jdbcTemplate, generalService);
        config.cleanTestDb(jdbcTemplate);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void showAllLocation() {
        jdbcTemplate.update("insert into locations (location_id,country,city,place) values (1,'Vietnam','Hanoi','Hoan Kiem')");
        jdbcTemplate.update("insert into locations (location_id,country,city,place) values (2,'Vietnam','Hanoi','Ho Tay')");
        jdbcTemplate.update("insert into locations (location_id,country,city,place) values (3,'Vietnam','Hanoi','Pho Co')");
        jdbcTemplate.update("insert into locations (location_id,country,city,place) values (4,'Vietnam','Hanoi','Trang Tien')");
        Assert.assertEquals(4, locationService.showAllLocation().size());
    }
}