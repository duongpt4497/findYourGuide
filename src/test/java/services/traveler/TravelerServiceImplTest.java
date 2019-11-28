package services.traveler;

import entities.Traveler;
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

import java.time.LocalDateTime;

@RunWith(MockitoJUnitRunner.class)
public class TravelerServiceImplTest {

    @InjectMocks
    private TravelerServiceImpl travelerService;

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Mock
    private GeneralService generalService;

    @Before
    public void init() {
        TestDataSourceConfig config = new TestDataSourceConfig();
        jdbcTemplate.setDataSource(config.setupDatasource());
        travelerService = new TravelerServiceImpl(jdbcTemplate, generalService);
        config.cleanTestDb(jdbcTemplate);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createTraveler() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','TRAVELER')");
        Traveler traveler = new Traveler(1, "John", "Doe", "123", 1,
                LocalDateTime.parse("1993-06-05T00:00"), "a", "12", "12", "a",
                "a", new String[]{"vi"}, "vietnam", "hanoi", "a");
        Assert.assertEquals(true, travelerService.createTraveler(traveler));
    }

    @Test
    public void createTraveler2() {
        Traveler traveler = new Traveler(1, "John", "Doe", "123", 1,
                LocalDateTime.parse("1993-06-05T00:00"), "a", "12", "12", "a",
                "a", new String[]{"vi"}, "vietnam", "hanoi", "a");
        Assert.assertEquals(false, travelerService.createTraveler(traveler));
    }

    @Test
    public void findTravelerWithId() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','TRAVELER')");
        jdbcTemplate.update("insert into traveler (traveler_id, first_name, last_name, phone, gender, date_of_birth, street, house_number, postal_code, slogan, about_me, language, country, city, avatar_link)" +
                "values (1,'Megan','Deo','123',2,'1996-02-13','a','12','12','a','a','{en,vi}','vietnam','hanoi','a')");
        Assert.assertEquals("Megan", travelerService.findTravelerWithId(1).getFirst_name());
    }

    @Test
    public void findTravelerWithId2() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','TRAVELER')");
        jdbcTemplate.update("insert into traveler (traveler_id, first_name, last_name, phone, gender, date_of_birth, street, house_number, postal_code, slogan, about_me, language, country, city, avatar_link)" +
                "values (1,'Megan','Deo','123',2,'1996-02-13','a','12','12','a','a','{en,vi}','vietnam','hanoi','a')");
        Assert.assertEquals(null, travelerService.findTravelerWithId(2));
    }

    @Test
    public void updateTraveler() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','TRAVELER')");
        jdbcTemplate.update("insert into traveler (traveler_id, first_name, last_name, phone, gender, date_of_birth, street, house_number, postal_code, slogan, about_me, language, country, city, avatar_link)" +
                "values (1,'Megan','Deo','123',2,'1996-02-13','a','12','12','a','a','{en,vi}','vietnam','hanoi','a')");
        Traveler traveler = new Traveler(1, "John", "Doe", "123", 1,
                LocalDateTime.parse("1993-06-05T00:00"), "a", "12", "12", "a",
                "a", new String[]{"vi"}, "vietnam", "hanoi", "a");
        travelerService.updateTraveler(traveler);
        Assert.assertEquals("John", travelerService.findTravelerWithId(1).getFirst_name());
    }

    @Test
    public void favoritePost() {
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
        travelerService.favoritePost(2, 1);
        int result = jdbcTemplate.queryForObject("select count (post_id) from favoritepost where traveler_id = ?", new Object[]{2}, int.class);
        Assert.assertEquals(1, result);
    }

    @Test(expected = Exception.class)
    public void favoritePost2() {
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
        travelerService.favoritePost(2, 2);
        int result = jdbcTemplate.queryForObject("select count (post_id) from favoritepost where traveler_id = ?", new Object[]{2}, int.class);
        Assert.assertEquals(1, result);
    }
}