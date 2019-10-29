package services.guider;

import entities.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import services.GeneralServiceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CategoryServiceImpl implements CategoryService {
    private JdbcTemplate jdbcTemplate;
    private GeneralServiceImpl generalService;

    public CategoryServiceImpl(JdbcTemplate jdbcTemplate, GeneralServiceImpl generalService) {
        this.jdbcTemplate = jdbcTemplate;
        this.generalService = generalService;
    }

    @Override
    public List<Category> findAllCategory() {
        try {
            String query = "select * from category";
            System.out.println(query);
            return jdbcTemplate.query("select * from category", new RowMapper<Category>() {
                public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Category(
                            rs.getInt("category_id"),
                            rs.getString("name")
                    );
                }

                ;
            });
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getCause() + e.getStackTrace());

        }

        return null;
    }
}
