package services.Post;

import entities.Post;
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

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceImplTest {

    @InjectMocks
    PostServiceImpl postService;

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Mock
    private GeneralService generalService;

    @Before
    public void init() {
        TestDataSourceConfig config = new TestDataSourceConfig();
        jdbcTemplate.setDataSource(config.setupDatasource());
        postService = new PostServiceImpl(jdbcTemplate, generalService);
        config.cleanTestDb(jdbcTemplate);
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
        jdbcTemplate.update("INSERT INTO post(post_id,guider_id, location_id,category_id, title, video_link, picture_link, total_hour, description, including_service, active,price,rated,reasons) " +
                "VALUES (2,1,1,1,'test post2','a','{a}',2,'a','{a,b}',true,10,7,'abc')");
        jdbcTemplate.update("INSERT INTO post(post_id,guider_id, location_id,category_id, title, video_link, picture_link, total_hour, description, including_service, active,price,rated,reasons) " +
                "VALUES (3,1,1,1,'test post3','a','{a}',2,'a','{a,b}',true,10,16,'abc')");
        jdbcTemplate.update("INSERT INTO post(post_id,guider_id, location_id,category_id, title, video_link, picture_link, total_hour, description, including_service, active,price,rated,reasons) " +
                "VALUES (4,1,1,1,'test post4','a','{a}',2,'a','{a,b}',true,10,11,'abc')");
        jdbcTemplate.update("INSERT INTO post(post_id,guider_id, location_id,category_id, title, video_link, picture_link, total_hour, description, including_service, active,price,rated,reasons) " +
                "VALUES (5,1,1,1,'test post5','a','{a}',2,'a','{a,b}',true,10,19,'abc')");
        jdbcTemplate.update("INSERT INTO post(post_id,guider_id, location_id,category_id, title, video_link, picture_link, total_hour, description, including_service, active,price,rated,reasons) " +
                "VALUES (6,1,1,1,'test post6','a','{a}',2,'a','{a,b}',true,10,1,'abc')");
        jdbcTemplate.update("INSERT INTO post(post_id,guider_id, location_id,category_id, title, video_link, picture_link, total_hour, description, including_service, active,price,rated,reasons) " +
                "VALUES (7,1,1,1,'test post7','a','{a}',2,'a','{a,b}',true,10,3,'abc')");
        jdbcTemplate.update("INSERT INTO post(post_id,guider_id, location_id,category_id, title, video_link, picture_link, total_hour, description, including_service, active,price,rated,reasons) " +
                "VALUES (8,1,1,1,'test post8','a','{a}',2,'a','{a,b}',true,10,20,'abc')");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllPost() {
        Assert.assertEquals(8, postService.findAllPost(1).size());
    }

    @Test
    public void findAllPostByCategoryId() {
        Assert.assertEquals(8, postService.findAllPostByCategoryId(1).size());
    }

    @Test
    public void findSpecificPost() {
        Assert.assertEquals("test post", postService.findSpecificPost(1).getTitle());
    }

    @Test
    public void updatePost() {
        Post post = new Post(1, 1, "test", "a", new String[]{"a"}, 2, "a", new String[]{"a", "b"}, true, 10, 5, "a");
        postService.updatePost(1, post);
        Assert.assertEquals("test", postService.findSpecificPost(1).getTitle());
    }

    @Test
    public void insertNewPost() {
        jdbcTemplate.update("delete from post");
        Post post = new Post(1, 1, "test new", "a", new String[]{"a"}, 2, "a", new String[]{"a", "b"}, true, 10, 5, "a");
        int id = postService.insertNewPost(1, post);
        Assert.assertEquals("test new", postService.findSpecificPost(id).getTitle());
    }

    @Test
    public void getTopTour() {
        List<Post> result = postService.getTopTour();
        Assert.assertEquals(6, result.size());
        Assert.assertEquals(8, result.get(0).getPost_id());
        Assert.assertEquals(5, result.get(1).getPost_id());
        Assert.assertEquals(3, result.get(2).getPost_id());
        Assert.assertEquals(4, result.get(3).getPost_id());
        Assert.assertEquals(2, result.get(4).getPost_id());
        Assert.assertEquals(1, result.get(5).getPost_id());
    }
}