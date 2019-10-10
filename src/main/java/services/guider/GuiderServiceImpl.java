package services.guider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import entities.Guider;
import services.GeneralServiceImpl;

@Repository
public class GuiderServiceImpl implements GuiderService {
    private Logger logger;
    private JdbcTemplate jdbcTemplate;
    private GeneralServiceImpl generalService;

    public GuiderServiceImpl(JdbcTemplate jdbcTemplate, GeneralServiceImpl generalService) {
        this.jdbcTemplate = jdbcTemplate;
        this.generalService = generalService;
    }

    @Override
    public Guider findGuiderWithID(long id) {
        try {
            String query = "select * from guider where guider_id = " + id;
            System.out.println(query);
            return jdbcTemplate.queryForObject("select * from guider where guider_id = ?", new RowMapper<Guider>() {
                public Guider mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Guider(rs.getLong("guider_id"), rs.getString("first_name"), rs.getString("last_name"),
                            rs.getInt("age"), rs.getString("about_me"), rs.getLong("contribution"),
                            rs.getString("city"), rs.getBoolean("active"));
                }

                ;
            }, id);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return null;
    }

    @Override
    public long updateGuiderWithId(Guider guiderNeedUpdate) {
        try {
            String query = "update guider set first_name = ?, last_name = ?, age = ?, about_me = ?, city = ? where guider_id = ?";
            jdbcTemplate.update(query, guiderNeedUpdate.getFirst_name(), guiderNeedUpdate.getLast_name(),
                    guiderNeedUpdate.getAge(), guiderNeedUpdate.getAbout_me(), guiderNeedUpdate.getCity(),
                    guiderNeedUpdate.getGuider_id());
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return guiderNeedUpdate.getGuider_id();
    }
}
