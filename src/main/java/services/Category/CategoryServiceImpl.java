package services.Category;

import entities.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CategoryServiceImpl implements CategoryService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;

    public CategoryServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Category> findAllCategory() {
        try {
            String query = "select * from category";
            return jdbcTemplate.query(query, new RowMapper<Category>() {
                public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Category(
                            rs.getInt("category_id"),
                            rs.getString("name")
                    );
                }
            });
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public void createCategory(String name) {
        try {
            String query = "insert into category (name) values (?)";
            jdbcTemplate.update(query, name);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }
}
