package services.contributionPoint;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import winter.findGuider.TestDataSourceConfig;

@RunWith(MockitoJUnitRunner.class)
public class ContributionPointServiceImplTest {

    @InjectMocks
    ContributionPointServiceImpl contributionPointService;

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Before
    public void init() {
        TestDataSourceConfig config = new TestDataSourceConfig();
        jdbcTemplate.setDataSource(config.setupDatasource());
        contributionPointService = new ContributionPointServiceImpl(jdbcTemplate);
        config.cleanTestDb(jdbcTemplate);
        ReflectionTestUtils.setField(contributionPointService, "corMoney", "10");
        ReflectionTestUtils.setField(contributionPointService, "corRated", "100");
        ReflectionTestUtils.setField(contributionPointService, "corTurn", "200");
        ReflectionTestUtils.setField(contributionPointService, "minus", "5000");

        jdbcTemplate.update("insert into account(account_id,user_name,password,email,role) values (1,'John','123','John@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into account(account_id,user_name,password,email,role) values (2,'Jack','123','Jack@gmail.com','TRAVELER')");
        jdbcTemplate.update("insert into guider(guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (1,'John','Doe',21,'12345678','abc',1000,'Hanoi','{vi,en}',true,5,'a','a')");
        jdbcTemplate.update("insert into traveler(traveler_id, first_name, last_name, phone, gender, date_of_birth, street, house_number, postal_code, slogan, about_me, language, country, city, avatar_link)" +
                "values (2,'Jack','Smith','123456',1,'1993-06-02','a','12','12','a','a','{vi}','vietnam','hanoi','a')");
        jdbcTemplate.update("insert into locations (location_id,country,city,place) values (1,'Vietnam','Hanoi','Hoan Kiem')");
        jdbcTemplate.update("insert into category (category_id,name) values (1,'history')");
        jdbcTemplate.update("INSERT INTO post(post_id,guider_id, location_id,category_id, title, video_link, picture_link, total_hour, description, including_service, active,price,rated,reasons) " +
                "VALUES (1,1,1,1,'test post','a','{a}',2,'a','{a,b}',true,10,5,'abc')");
        jdbcTemplate.update("insert into transaction (transaction_id,payment_id,payer_id,description,date_of_transaction,success) values ('abc','abc','abc','abc','2019-11-22T03:00',true)");
        jdbcTemplate.update("insert into trip (trip_id,traveler_id,post_id,begin_date,finish_date,adult_quantity,children_quantity,fee_paid,transaction_id,status)" +
                "values (1,2,1,'2019-11-22T03:30','2019-11-23T10:00',1,1,150,'abc','FINISHED')");
        jdbcTemplate.update("insert into review (trip_id,rated,post_date,review,visible) values (1,5,'2019-11-25','abc',true)");
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

    @Test
    public void penaltyGuiderForCancel() throws Exception {
        contributionPointService.penaltyGuiderForCancel(1);
        int result = jdbcTemplate.queryForObject("select contribution from guider where guider_id = ?", new Object[]{1}, int.class);
        Assert.assertEquals(500, result);
    }

    @Test
    public void penaltyGuiderForCancel2() throws Exception {
        jdbcTemplate.update("insert into account(account_id,user_name,password,email,role) values (3,'Jack','123','Jack@gmail.com','TRAVELER')");
        jdbcTemplate.update("insert into guider(guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (3,'John','Doe',21,'12345678','abc',10,'Hanoi','{vi,en}',true,5,'a','a')");
        contributionPointService.penaltyGuiderForCancel(3);
        int result = jdbcTemplate.queryForObject("select contribution from guider where guider_id = ?", new Object[]{3}, int.class);
        Assert.assertEquals(0, result);
    }

    @Test
    public void penaltyGuiderForCancel3() throws Exception {
        jdbcTemplate.update("insert into account(account_id,user_name,password,email,role) values (3,'Jack','123','Jack@gmail.com','TRAVELER')");
        jdbcTemplate.update("insert into guider(guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (3,'John','Doe',21,'12345678','abc',500,'Hanoi','{vi,en}',true,5,'a','a')");
        contributionPointService.penaltyGuiderForCancel(3);
        int result = jdbcTemplate.queryForObject("select contribution from guider where guider_id = ?", new Object[]{3}, int.class);
        Assert.assertEquals(0, result);
    }
}