package services.guider;

import entities.Activity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import services.GeneralServiceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class ActivityServiceImpl implements  ActivityService {

    private Logger logger;
    private JdbcTemplate jdbcTemplate;
    private GeneralServiceImpl generalService;

    public ActivityServiceImpl(JdbcTemplate jdbcTemplate, GeneralServiceImpl generalService) {
        this.jdbcTemplate = jdbcTemplate;
        this.generalService = generalService;
    }

    @Override
    public List<Activity> findActivityOfAPost(long post_id) {
        try{
            return jdbcTemplate.query("select * from  activity where post_id = ? ", new RowMapper<Activity>() {
                @Override
                public Activity mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new Activity(
                            resultSet.getString("brief"),
                            resultSet.getString("detail")
                    );
                }
            }, post_id);
        }catch (Exception e){

        }

        return new ArrayList<>();
    }
}
