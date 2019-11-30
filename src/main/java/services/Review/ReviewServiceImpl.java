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
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    public List<Review> findReviewByOrderId(long trip_id) {
        try {
            String query = "select * from review where trip_id = ?";
            return jdbcTemplate.query(query, new RowMapper<Review>() {
                public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Review(rs.getLong("trip_id"), rs.getLong("rated"),
                            rs.getDate("post_date"), rs.getString("review"),
                            rs.getBoolean("visible"));
                }
            }, trip_id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Review> findReviewsByGuiderId(long guider_id) {
        try {
            String query = "select review.* from review " +
                    "inner join trip on review.trip_id = trip.trip_id " +
                    "inner join post on post.guider_id = ?";
            return jdbcTemplate.query(query, new RowMapper<Review>() {
                public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Review(rs.getLong("trip_id"), rs.getLong("rated"),
                            rs.getDate("post_date"), rs.getString("review"),
                            rs.getBoolean("visible"));
                }
            }, guider_id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Review> findReviewsByPostId(long post_id) {
        try {
            String query = "select review.* from review " +
                    "inner join trip on trip.post_id = ?";
            return jdbcTemplate.query(query, new RowMapper<Review>() {
                public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Review(rs.getLong("trip_id"), rs.getLong("rated"),
                            rs.getDate("post_date"), rs.getString("review"),
                            rs.getBoolean("visible"));
                }
            }, post_id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean createReview(Review newReview) {
        try {
            String query = "insert into review (trip_id, rated, post_date, review, visible)" +
                    "values (?,?,?,?,?)";
            jdbcTemplate.update(query, newReview.getTrip_id(), newReview.getRated(),
                    Timestamp.valueOf(LocalDateTime.now()), newReview.getReview(), true);
            return true;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean checkReviewExist(long trip_id) {
        try {
            String query = "select count(trip_id) from review where trip_id = ?";
            List<Integer> checklist = jdbcTemplate.query(query, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getInt("count");
                }
            }, trip_id);
            if (checklist.get(0) == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return false;
        }
    }

    @Override
    public void showHideReview(long trip_id) {
        try {
            String check = "select visible from review where trip_id = ?";
            boolean isVisible = jdbcTemplate.queryForObject(check, new Object[]{trip_id}, boolean.class);
            String query;
            if (isVisible) {
                query = "update review set visible = false where trip_id = ?";
            } else {
                query = "update review set visible = true where trip_id = ?";
            }
            jdbcTemplate.update(query, trip_id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }
}
