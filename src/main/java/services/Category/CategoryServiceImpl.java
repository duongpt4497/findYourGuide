package services.Category;

import entities.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Repository
public class CategoryServiceImpl implements CategoryService {
    private Logger logger;
    private JdbcTemplate jdbcTemplate;

    public CategoryServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createCategory(Category newCategory) {
        try {
            String insertQuery = "insert into category (name) values (?)";
            jdbcTemplate.update(insertQuery, newCategory.getName());
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    @Override
    public List<Category> findCategory() {
        String query = "select * from category";
        List<Map<String, Object>> sqlResult = jdbcTemplate.queryForList(query);
        List<Category> categoryList = new ArrayList<>();
        for (Map<String, Object> row : sqlResult) {
            Category cat = new Category((long) row.get("category_id"), (String) row.get("name"));
            categoryList.add(cat);
        }
        return categoryList;
    }

    @Override
    public void editCategory(Category categoryNeedUpdate) {
        try {
            String query = "update category set name = ? where category_id = ?";
            jdbcTemplate.update(query, categoryNeedUpdate.getName(), categoryNeedUpdate.getCategory_id());
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
}
