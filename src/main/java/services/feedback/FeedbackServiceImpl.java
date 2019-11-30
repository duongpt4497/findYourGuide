package services.feedback;

import entities.Feedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FeedbackServiceImpl implements FeedbackService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;

    public FeedbackServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Feedback> findAllFeedback() {
        List<Feedback> result = new ArrayList<>();
        try {
            String query = "select feedback_id, user_name, time_sent, message from feedback " +
                    "inner join account on feedback.account_id = account.account_id";
            result = jdbcTemplate.query(query, new RowMapper<Feedback>() {
                @Override
                public Feedback mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Feedback(rs.getInt("feedback_id"), rs.getString("message"),
                            rs.getTimestamp("time_sent").toLocalDateTime(), rs.getString("user_name"));
                }
            });
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
        return result;
    }

    @Override
    public void createFeedback(int account_id, String message) {
        try {
            String query = "insert into feedback (account_id,time_sent,message) values (?,?,?)";
            jdbcTemplate.update(query, account_id, Timestamp.valueOf(LocalDateTime.now()), message);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }
}
