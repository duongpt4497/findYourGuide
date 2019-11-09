package services.guider;


import ch.qos.logback.classic.Logger;
import entities.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class ReviewServiceImpl implements ReviewService {
    private Logger logger;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Review> findReviewsByGuiderId(long guider_id) {
        try {
            String query = "select * from review where guider_id = " + guider_id;
            System.out.println(query);
            return jdbcTemplate.query("select * from review where guider_id = ?", new RowMapper<Review>() {
                public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Review(
                            rs.getLong("order_id"),
                            rs.getLong("traveler_id"),
                            rs.getLong("guider_id"),
                            rs.getLong("post_id"),
                            rs.getFloat("rated"),
                            new Date(rs.getDate("post_date").getTime()),
                            rs.getString("review")
                    );
                }

                ;
            }, guider_id);
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getCause() + e.getStackTrace());
            logger.info(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Review> findReviewsByPostId(long post_id) {
        try {
            String query = "select * from review where guider_id = " + post_id;
            System.out.println(query);
            return jdbcTemplate.query("select * from review where post_id = ?", new RowMapper<Review>() {
                public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Review(
                            rs.getLong("order_id"),
                            rs.getLong("traveler_id"),
                            rs.getLong("guider_id"),
                            rs.getLong("post_id"),
                            rs.getFloat("rated"),
                            new Date(rs.getDate("post_date").getTime()),
                            rs.getString("review")
                    );
                }

                ;
            }, post_id);
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getCause());
            logger.info(e.getMessage());
        }
        return null;
    }
}
