package services.Location;

import entities.Location;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import services.GeneralService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LocationServiceImpl implements LocationService {

    private JdbcTemplate jdbcTemplate;
    private GeneralService generalService;

    public LocationServiceImpl(JdbcTemplate jdbcTemplate, GeneralService generalService) {
        this.jdbcTemplate = jdbcTemplate;
        this.generalService = generalService;
    }

    @Override
    public List<Location> showAllLocation() {
        return jdbcTemplate.query("select * from locations", new RowMapper<Location>() {
            @Override
            public Location mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Location(
                        resultSet.getInt("location_id"),
                        resultSet.getString("place")
                );
            }
        });

    }
}
