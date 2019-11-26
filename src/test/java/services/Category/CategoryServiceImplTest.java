package services.Category;

import entities.Category;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import winter.findGuider.TestDataSourceConfig;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceImplTest {

    @InjectMocks
    CategoryServiceImpl categoryService;

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Before
    public void init() {
        TestDataSourceConfig config = new TestDataSourceConfig();
        jdbcTemplate.setDataSource(config.setupDatasource());
        categoryService = new CategoryServiceImpl(jdbcTemplate);
        jdbcTemplate.update("delete from category");
        jdbcTemplate.update("insert into category (name) values ('Food')");
        jdbcTemplate.update("insert into category (name) values ('History')");
        jdbcTemplate.update("insert into category (name) values ('Culture')");
        jdbcTemplate.update("insert into category (name) values ('Night Tour')");
        jdbcTemplate.update("insert into category (name) values ('Art')");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllCategory() {
        Assert.assertEquals(5, categoryService.findAllCategory().size());
    }

    @Test(expected = Exception.class)
    public void testFindAllCategory2() {
        when(jdbcTemplate.query("select * from category", new RowMapper<Category>() {
            public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Category(
                        rs.getInt("category_id"),
                        rs.getString("name")
                );
            }
        })).thenThrow(Exception.class);
        Assert.assertEquals(5, categoryService.findAllCategory().size());
    }
}
