package services.Mail;

import entities.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import services.Post.PostService;
import services.guider.GuiderService;
import services.traveler.TravelerService;
import services.trip.TripService;
import winter.findGuider.TestDataSourceConfig;

import java.time.LocalDateTime;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceImplTest {

    @InjectMocks
    MailServiceImpl mailService;

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private TravelerService travelerService;

    @Mock
    private GuiderService guiderService;

    @Mock
    private PostService postService;

    @Mock
    private TripService tripService;

    @Before
    public void init() {
        TestDataSourceConfig config = new TestDataSourceConfig();
        jdbcTemplate.setDataSource(config.setupDatasource());
        mailService = new MailServiceImpl(emailSender, travelerService, guiderService, postService, tripService);
        config.cleanTestDb(jdbcTemplate);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sendMail() {
        Assert.assertEquals(true, mailService.sendMail("travelwithlocalsysadm@gmail.com", "test method", "test"));
    }

    @Test
    public void getMailContent() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (2,'Megan','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','TRAVELER')");
        jdbcTemplate.update("insert into locations (location_id,country,city,place) values (1,'Vietnam','Hanoi','Hoan Kiem')");
        jdbcTemplate.update("insert into category (category_id,name) values (1,'history')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (1,'John','Doe',21,'123456','abc',150,'hanoi','{en,vi}',true,5,'a','a')");
        jdbcTemplate.update("insert into contract_detail (contract_id,name,nationality,date_of_birth,gender,hometown,address,identity_card_number,card_issued_date,card_issued_province,account_active_date)" +
                "values (1,'John Doe','Vietnamese','1993-06-05',1,'Hanoi','a','123456','2000-04-05','Hanoi','2016-10-15')");
        jdbcTemplate.update("insert into contract (guider_id,contract_id) values (1,1)");
        jdbcTemplate.update("insert into traveler (traveler_id, first_name, last_name, phone, gender, date_of_birth, street, house_number, postal_code, slogan, about_me, language, country, city, avatar_link)" +
                "values (2,'Megan','Deo','123',2,'1996-02-13','a','12','12','a','a','{en,vi}','vietnam','hanoi','a')");
        jdbcTemplate.update("INSERT INTO post(post_id,guider_id, location_id,category_id, title, video_link, picture_link, total_hour, description, including_service, active,price,rated,reasons) " +
                "VALUES (1,1,1,1,'test post','a','{a}',2,'a','{a,b}',true,10,5,'abc')");
        Order order = new Order(1, 2, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 2, 1, 150, "ABCD", null);
        Assert.assertEquals(null, mailService.getMailContent(order, "UNCONFIRMED"));
    }

    @Test
    public void getMailContentError() {
        Order order = new Order(1, 1, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 2, 1, 150, "ABCD", null);
        Assert.assertEquals(null, mailService.getMailContent(order, "UNCONFIRMED"));
    }
}