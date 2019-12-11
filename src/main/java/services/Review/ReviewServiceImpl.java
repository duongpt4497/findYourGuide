package services.Review;

import entities.Review;
import entities.TravelerReview;
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
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Review> findReviewByOrderId(long trip_id) throws Exception {
        String query = "select * from review where trip_id = ?";
        return jdbcTemplate.query(query, new RowMapper<Review>() {
            public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Review(rs.getLong("trip_id"), rs.getLong("rated"),
                        rs.getDate("post_date"), rs.getString("review"),
                        rs.getBoolean("visible"));
            }
        }, trip_id);
    }

    @Override
    public List<Review> findReviewsByGuiderId(long guider_id) throws Exception {
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
    }

    @Override
    public List<Review> findReviewsByPostId(long post_id, long page) throws Exception {
        String query = "select review.*, avatar_link from review " +
                "inner join trip on trip.trip_id = review.trip_id " +
                "inner join traveler on trip.traveler_id = traveler.traveler_id " +
                "where trip.post_id = ? and visible = true " +
                "limit 5 offset ?";
        return jdbcTemplate.query(query, new RowMapper<Review>() {
            public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Review(rs.getLong("trip_id"), rs.getLong("rated"),
                        rs.getDate("post_date"), rs.getString("review"),
                        rs.getBoolean("visible"), rs.getString("avatar_link"));
            }
        }, post_id, page * 5);
    }

    @Override
    public List<Review> findReviewsByPostIdAdmin(long post_id) throws Exception {
        String query = "select review.* from review inner join trip on trip.trip_id = review.trip_id " +
                "where trip.post_id = ?";
        return jdbcTemplate.query(query, new RowMapper<Review>() {
            public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Review(rs.getLong("trip_id"), rs.getLong("rated"),
                        rs.getDate("post_date"), rs.getString("review"),
                        rs.getBoolean("visible"));
            }
        }, post_id);
    }

    @Override
    public boolean createReview(Review newReview) throws Exception {
        String query = "insert into review (trip_id, rated, post_date, review, visible)" +
                "values (?,?,?,?,?)";
        jdbcTemplate.update(query, newReview.getTrip_id(), newReview.getRated(),
                Timestamp.valueOf(LocalDateTime.now()), newReview.getReview(), true);
        return true;
    }

    @Override
    public boolean checkReviewExist(long trip_id) throws Exception {
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
    }

    @Override
    public void showHideReview(long trip_id) throws Exception {
        String check = "select visible from review where trip_id = ?";
        boolean isVisible = jdbcTemplate.queryForObject(check, new Object[]{trip_id}, boolean.class);
        String query;
        if (isVisible) {
            query = "update review set visible = false where trip_id = ?";
        } else {
            query = "update review set visible = true where trip_id = ?";
        }
        jdbcTemplate.update(query, trip_id);
    }

    @Override
    public void createTravelerReview(TravelerReview review) throws Exception {
        String query = "insert into travelerreviews (traveler_id,guider_id,review) values (?,?,?)";
        jdbcTemplate.update(query, review.getTraveler_id(), review.getGuider_id(), review.getReview());
    }

    @Override
    public List<TravelerReview> findReviewOfATraveler(long traveler_id, long page) throws Exception {
        String query = "select review_id, " +
                "concat (gu.first_name, ' ', gu.last_name) as gu_name, " +
                "post_date, review, gu.avatar from travelerreviews as tra_re " +
                "inner join traveler as tra on tra_re.traveler_id = tra.traveler_id " +
                "inner join guider as gu on tra_re.guider_id = gu.guider_id " +
                "where tra_re.traveler_id = ? and visible = true " +
                "limit 5 offset ?";
        return jdbcTemplate.query(query, new RowMapper<TravelerReview>() {
            @Override
            public TravelerReview mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new TravelerReview(rs.getLong("review_id"),
                        rs.getTimestamp("post_date").toLocalDateTime(),
                        rs.getString("review"),
                        rs.getString("gu_name"),
                        rs.getString("avatar"));
            }
        }, traveler_id, page * 5);
    }
}
