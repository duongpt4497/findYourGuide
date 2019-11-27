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
}