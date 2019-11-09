package services.Review;


import entities.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ReviewServiceImpl implements ReviewService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Review> findReviewsByGuiderId(long guider_id) {
        try {
            String query = "select * from review where guider_id = " + guider_id;
            return jdbcTemplate.query("select * from review where guider_id = ?", new RowMapper<Review>() {
                public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Review(rs.getLong("order_id"), rs.getLong("traveler_id"),
                            rs.getLong("guider_id"), rs.getLong("post_id"), rs.getLong("rated"),
                            rs.getDate("post_date"), rs.getString("review"));
                }
            }, guider_id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Review> findReviewsByPostId(long post_id) {
        try {
            String query = "select * from review where guider_id = " + post_id;
            return jdbcTemplate.query("select * from review where post_id = ?", new RowMapper<Review>() {
                public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Review(rs.getLong("order_id"), rs.getLong("traveler_id"),
                            rs.getLong("guider_id"), rs.getLong("post_id"), rs.getLong("rated"),
                            rs.getDate("post_date"), rs.getString("review"));
                }
            }, post_id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }
}
