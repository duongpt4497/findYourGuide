package services.guider;

import entities.Contract;
import entities.Guider;
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
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GuiderServiceImplTest {

    @InjectMocks
    GuiderServiceImpl guiderService;

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Mock
    private GeneralService generalService;

    @Before
    public void init() {
        TestDataSourceConfig config = new TestDataSourceConfig();
        jdbcTemplate.setDataSource(config.setupDatasource());
        guiderService = new GuiderServiceImpl(jdbcTemplate, generalService);
        config.cleanTestDb(jdbcTemplate);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findGuiderWithID() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (1,'John','Doe',21,'123456','abc',150,'hanoi','{en,vi}',true,5,'a','a')");
        Assert.assertEquals("John", guiderService.findGuiderWithID(1).getFirst_name());
    }

    @Test
    public void findGuiderWithPostId() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into locations (location_id,country,city,place) values (1,'Vietnam','Hanoi','Hoan Kiem')");
        jdbcTemplate.update("insert into category (category_id,name) values (1,'history')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (1,'John','Doe',21,'123456','abc',150,'hanoi','{en,vi}',true,5,'a','a')");
        jdbcTemplate.update("insert into contract_detail (contract_id,name,nationality,date_of_birth,gender,hometown,address,identity_card_number,card_issued_date,card_issued_province,account_active_date)" +
                "values (1,'John Doe','Vietnamese','1993-06-05',1,'Hanoi','a','123456','2000-04-05','Hanoi','2016-10-15')");
        jdbcTemplate.update("insert into contract (guider_id,contract_id) values (1,1)");
        jdbcTemplate.update("INSERT INTO post(post_id,guider_id, location_id,category_id, title, video_link, picture_link, total_hour, description, including_service, active,price,rated,reasons) " +
                "VALUES (1,1,1,1,'test post','a','{a}',2,'a','{a,b}',true,10,5,'abc')");
        Assert.assertEquals("John", guiderService.findGuiderWithPostId(1).getFirst_name());
    }

    @Test
    public void findGuiderContract() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (1,'John','Doe',21,'123456','abc',150,'hanoi','{en,vi}',true,5,'a','a')");
        jdbcTemplate.update("insert into contract_detail (contract_id,name,nationality,date_of_birth,gender,hometown,address,identity_card_number,card_issued_date,card_issued_province,account_active_date)" +
                "values (1,'John Doe','Vietnamese','1993-06-05',1,'Hanoi','a','123456','2000-04-05','Hanoi','2016-10-15')");
        jdbcTemplate.update("insert into contract (guider_id,contract_id) values (1,1)");
        Assert.assertEquals("John Doe", guiderService.findGuiderContract(1).getName());
    }

    @Test
    public void createGuider() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        Guider guider = new Guider(1, "test", "test", 21, "123456", "a", 123, "hanoi", new String[]{"en", "vi"}, true, 5, "a", "a");
        Assert.assertEquals(1, guiderService.createGuider(guider));
    }

    @Test(expected = NullPointerException.class)
    public void createGuider2() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        Guider guider = new Guider();
        Assert.assertEquals(1, guiderService.createGuider(guider));
    }

    @Test
    public void createGuiderContract() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (1,'John','Doe',21,'123456','abc',150,'hanoi','{en,vi}',true,5,'a','a')");
        Contract contract = new Contract("test", "vietnamese", LocalDateTime.parse("1993-06-05T00:00"), 1,
                "Hanoi", "a", "12345", LocalDateTime.parse("2001-05-07T00:00"),
                "Hanoi", null, null);
        long id = guiderService.createGuiderContract(contract);
        jdbcTemplate.update("insert into contract (guider_id,contract_id) values (1," + id + ")");
        Assert.assertEquals("test", guiderService.findGuiderContract(1).getName());
    }

    @Test(expected = NullPointerException.class)
    public void createGuiderContract2() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (1,'John','Doe',21,'123456','abc',150,'hanoi','{en,vi}',true,5,'a','a')");
        Contract contract = new Contract();
        long id = guiderService.createGuiderContract(contract);
        jdbcTemplate.update("insert into contract (guider_id,contract_id) values (1," + id + ")");
        Assert.assertEquals("test", guiderService.findGuiderContract(1).getName());
    }

    @Test
    public void updateGuiderWithId() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (1,'John','Doe',21,'123456','abc',150,'hanoi','{en,vi}',true,5,'a','a')");
        Guider guider = new Guider(1, "test", "test", 21, "123456", "a", 123, "hanoi", new String[]{"en", "vi"}, true, 5, "a", "a");
        long id = guiderService.updateGuiderWithId(guider);
        Assert.assertEquals("test", guiderService.findGuiderWithID(id).getFirst_name());
    }

    @Test(expected = NullPointerException.class)
    public void updateGuiderWithId2() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (1,'John','Doe',21,'123456','abc',150,'hanoi','{en,vi}',true,5,'a','a')");
        Guider guider = new Guider();
        long id = guiderService.updateGuiderWithId(guider);
        Assert.assertEquals("test", guiderService.findGuiderWithID(id).getFirst_name());
    }

    @Test
    public void activateGuider() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (1,'John','Doe',21,'123456','abc',150,'hanoi','{en,vi}',false,5,'a','a')");
        long id = guiderService.activateGuider(1);
        Assert.assertEquals(true, guiderService.findGuiderWithID(id).isActive());
    }

    @Test
    public void deactivateGuider() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (1,'John','Doe',21,'123456','abc',150,'hanoi','{en,vi}',true,5,'a','a')");
        long id = guiderService.deactivateGuider(1);
        Assert.assertEquals(false, guiderService.findGuiderWithID(id).isActive());
    }

    @Test
    public void searchGuider() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (1,'John','Doe',21,'123456','abc',150,'hanoi','{en,vi}',true,5,'a','a')");
        Assert.assertEquals(1, guiderService.searchGuider("John").size());
    }

    @Test
    public void getTopGuiderByRate() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (1,'John','Doe',21,'123456','abc',150,'hanoi','{en,vi}',true,5,'a','a')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (2,'B','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (2,'Smith','Doe',21,'123456','abc',200,'hanoi','{en,vi}',true,9,'a','a')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (3,'C','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (3,'Larry','Doe',21,'123456','abc',10,'hanoi','{en,vi}',true,4,'a','a')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (4,'D','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (4,'Pop','Doe',21,'123456','abc',450,'hanoi','{en,vi}',true,10,'a','a')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (5,'E','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (5,'Minh','Doe',21,'123456','abc',320,'hanoi','{en,vi}',true,15,'a','a')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (6,'F','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (6,'Emma','Doe',21,'123456','abc',215,'hanoi','{en,vi}',true,25,'a','a')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (7,'G','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (7,'Uny','Doe',21,'123456','abc',150,'hanoi','{en,vi}',true,1,'a','a')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (8,'H','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (8,'Peter','Doe',21,'123456','abc',150,'hanoi','{en,vi}',true,13,'a','a')");
        List<Guider> result = guiderService.getTopGuiderByRate();
        Assert.assertEquals(6, result.size());
        Assert.assertEquals(6, result.get(0).getGuider_id());
        Assert.assertEquals(5, result.get(1).getGuider_id());
        Assert.assertEquals(8, result.get(2).getGuider_id());
        Assert.assertEquals(4, result.get(3).getGuider_id());
        Assert.assertEquals(2, result.get(4).getGuider_id());
        Assert.assertEquals(1, result.get(5).getGuider_id());
    }

    @Test
    public void getTopGuiderByContribute() {
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (1,'Jacky','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (1,'John','Doe',21,'123456','abc',150,'hanoi','{en,vi}',true,5,'a','a')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (2,'B','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (2,'Smith','Doe',21,'123456','abc',200,'hanoi','{en,vi}',true,9,'a','a')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (3,'C','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (3,'Larry','Doe',21,'123456','abc',10,'hanoi','{en,vi}',true,4,'a','a')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (4,'D','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (4,'Pop','Doe',21,'123456','abc',450,'hanoi','{en,vi}',true,10,'a','a')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (5,'E','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (5,'Minh','Doe',21,'123456','abc',320,'hanoi','{en,vi}',true,15,'a','a')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (6,'F','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (6,'Emma','Doe',21,'123456','abc',215,'hanoi','{en,vi}',true,25,'a','a')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (7,'G','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (7,'Uny','Doe',21,'123456','abc',170,'hanoi','{en,vi}',true,1,'a','a')");
        jdbcTemplate.update("insert into account (account_id,user_name, password, email ,role) " +
                "values (8,'H','$2a$10$Tb3mK1p2pCuPvDJUgSOJr.Rupo9isjom9vmmzAppMjtvWfLn/vQcK','Jacky@gmail.com','GUIDER')");
        jdbcTemplate.update("insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (8,'Peter','Doe',21,'123456','abc',15,'hanoi','{en,vi}',true,13,'a','a')");
        List<Guider> result = guiderService.getTopGuiderByContribute();
        Assert.assertEquals(6, result.size());
        Assert.assertEquals(4, result.get(0).getGuider_id());
        Assert.assertEquals(5, result.get(1).getGuider_id());
        Assert.assertEquals(6, result.get(2).getGuider_id());
        Assert.assertEquals(2, result.get(3).getGuider_id());
        Assert.assertEquals(7, result.get(4).getGuider_id());
        Assert.assertEquals(1, result.get(5).getGuider_id());
    }
}