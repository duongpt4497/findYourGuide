package services.traveler;

import entities.Traveler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import services.GeneralService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

@Repository
public class TravelerServiceImpl implements TravelerService {
    private Logger logger;
    private JdbcTemplate jdbcTemplate;
    private GeneralService generalService;

    public TravelerServiceImpl(JdbcTemplate jdbcTemplate, GeneralService gs) {
        this.jdbcTemplate = jdbcTemplate;
        this.generalService = gs;
    }

    @Override
    public long createTraveler(Traveler newTraveler) {
        Traveler insertedTraveler = new Traveler();
        try {
            String insertQuery = "insert into traveler (first_name, last_name, gender, date_of_birth, phone, email, street, " +
                    "house_number, slogan, postal_code, about_me, language, country, city) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(insertQuery, newTraveler.getFirst_name(), newTraveler.getLast_name(), newTraveler.getGender(),
                    newTraveler.getDate_of_birth(), newTraveler.getPhone(), newTraveler.getEmail(), newTraveler.getStreet(),
                    newTraveler.getHouse_number(), newTraveler.getSlogan(), newTraveler.getPostal_code(),
                    newTraveler.getAbout_me(), newTraveler.getLanguage(), newTraveler.getCountry(), newTraveler.getCity());

            String insertResultQuery = "select * from inserted";
            insertedTraveler = jdbcTemplate.queryForObject(insertResultQuery, new RowMapper<Traveler>() {
                @Override
                public Traveler mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Traveler(rs.getLong("traveler_id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getString("phone"), rs.getString("email"),
                            rs.getInt("gender"), rs.getDate("date_of_birth"), rs.getString("street"),
                            rs.getString("house_number"), rs.getString("postal_code"),
                            rs.getString("slogan"), rs.getString("about_me"),
                            generalService.checkForNull(rs.getArray("language")), rs.getString("country"), rs.getString("city"));
                }
            });
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return insertedTraveler.getTraveler_id();
    }

    @Override
    public Traveler findTravelerWithId(long id) {
        Traveler searchTraveler = new Traveler();
        try {
            String query = "select * from traveler where id = ?";
            searchTraveler = jdbcTemplate.queryForObject(query, new RowMapper<Traveler>() {
                @Override
                public Traveler mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Traveler(rs.getLong("traveler_id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getString("phone"), rs.getString("email"),
                            rs.getInt("gender"), rs.getDate("date_of_birth"), rs.getString("street"),
                            rs.getString("house_number"), rs.getString("postal_code"),
                            rs.getString("slogan"), rs.getString("about_me"),
                            generalService.checkForNull(rs.getArray("language")), rs.getString("country"), rs.getString("city"));
                }
            }, id);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return searchTraveler;
    }

    @Override
    public long updateTraveler(Traveler travelerNeedUpdate) {
        return 0;
    }
}
