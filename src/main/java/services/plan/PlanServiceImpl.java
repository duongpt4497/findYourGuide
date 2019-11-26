package services.plan;

import entities.Plan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Repository
public class PlanServiceImpl implements PlanService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PlanServiceImpl(JdbcTemplate jt) {
        this.jdbcTemplate = jt;
    }

    @Override
    public int createPlan(Plan newPlan) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            String insertQuery = "insert into plan (meeting_point, detail, post_id, trip_id) values (?,?,?,?)";

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(insertQuery, new String[]{"plan_id"});
                ps.setString(1, newPlan.getMeeting_point());
                ps.setString(2, newPlan.getDetail());
                ps.setInt(3, newPlan.getPost_id());
                ps.setInt(4, newPlan.gettrip_id());
                return ps;
            }, keyHolder);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return (int) keyHolder.getKey();
    }

    @Override
    public Plan searchPlanByPlanId(int plan_id) {
        try {
            String query = "select * from plan where plan_id = ?";
            return jdbcTemplate.queryForObject(query, new RowMapper<Plan>() {
                public Plan mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Plan(
                            rs.getInt("plan_id"),
                            rs.getString("meeting_point"),
                            rs.getString("detail"),
                            rs.getInt("post_id"),
                            rs.getInt("trip_id")
                    );
                }
            }, plan_id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @Override
    public Plan searchPlanByPostId(int post_id) {
        try {
            String query = "select * from plan where post_id = ?";
            return jdbcTemplate.queryForObject(query, new RowMapper<Plan>() {
                public Plan mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Plan(
                            rs.getInt("plan_id"),
                            rs.getString("meeting_point"),
                            rs.getString("detail"),
                            rs.getInt("post_id"),
                            rs.getInt("trip_id")
                    );
                }
            }, post_id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @Override
    public void updatePlan(int post_id, String meeting_point, String detail) {
        try {
            String query = "update plan set meeting_point = ?, detail = ? where post_id = ?";
            jdbcTemplate.update(query, meeting_point, detail, post_id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }
}
