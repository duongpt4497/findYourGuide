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

@RunWith(MockitoJUnitRunner.class)
public class PostServiceImplTest {

    @InjectMocks
    PostServiceImpl postService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllPost() {
        Assert.assertTrue(postService.findAllPost(1).isEmpty());
    }

    @Test
    public void findAllPostByCategoryId() {
        Assert.assertTrue(postService.findAllPostByCategoryId(1).isEmpty());
    }

    @Test
    public void findSpecificPost() {

        Assert.assertEquals(null, postService.findSpecificPost(1));
    }

    @Test
    public void updatePost() {
        postService.updatePost(1, new Post());
    }

    @Test
    public void insertNewPost() {
    }

    @Test
    public void getTopTour() {
    }
}