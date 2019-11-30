package services.traveler;

import entities.Traveler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import services.GeneralService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class TravelerServiceImpl implements TravelerService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;
    private GeneralService generalService;

    public TravelerServiceImpl(JdbcTemplate jdbcTemplate, GeneralService gs) {
        this.jdbcTemplate = jdbcTemplate;
        this.generalService = gs;
    }

    @Override
    public boolean createTraveler(Traveler newTraveler) {
        try {
            String insertQuery = "insert into traveler (traveler_id, first_name, last_name, phone, gender, date_of_birth, street, house_number, postal_code, slogan, about_me, language, country, city, avatar_link) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(insertQuery, newTraveler.getTraveler_id(), newTraveler.getFirst_name(), newTraveler.getLast_name(),
                    newTraveler.getPhone(), newTraveler.getGender(), Timestamp.valueOf(newTraveler.getDate_of_birth()),
                    newTraveler.getStreet(), newTraveler.getHouse_number(), newTraveler.getSlogan(),
                    newTraveler.getPostal_code(), newTraveler.getAbout_me(),
                    generalService.createSqlArray(Arrays.asList(newTraveler.getLanguage())),
                    newTraveler.getCountry(), newTraveler.getCity(), newTraveler.getAvatar_link());
            return true;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return false;
        }
    }

    @Override
    public Traveler findTravelerWithId(long id) {
        List<Traveler> searchTraveler = new ArrayList<>();
        try {
            String query = "select * from traveler where traveler_id = ?";
            searchTraveler = jdbcTemplate.query(query, new RowMapper<Traveler>() {
                @Override
                public Traveler mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Traveler(rs.getLong("traveler_id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getString("phone"),
                            rs.getInt("gender"), rs.getTimestamp("date_of_birth").toLocalDateTime(),
                            rs.getString("street"), rs.getString("house_number"),
                            rs.getString("postal_code"),
                            rs.getString("slogan"), rs.getString("about_me"),
                            generalService.checkForNull(rs.getArray("language")), rs.getString("country"),
                            rs.getString("city"), rs.getString("avatar_link"));
                }
            }, id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return null;
        }
        if (searchTraveler.isEmpty()) {
            return null;
        }
        return searchTraveler.get(0);
    }

    @Override
    public void updateTraveler(Traveler travelerNeedUpdate) {
        try {
            String query = "update traveler set first_name = ?, last_name = ?, phone = ?, gender = ?," +
                    "date_of_birth = ?, street = ?, house_number = ?, postal_code = ?, slogan = ?, about_me = ?," +
                    "language = ?, country = ?, city = ?, avatar_link = ? where traveler_id = ?";
            jdbcTemplate.update(query, travelerNeedUpdate.getFirst_name(), travelerNeedUpdate.getLast_name(),
                    travelerNeedUpdate.getPhone(), travelerNeedUpdate.getGender(),
                    Timestamp.valueOf(travelerNeedUpdate.getDate_of_birth()), travelerNeedUpdate.getStreet(),
                    travelerNeedUpdate.getHouse_number(), travelerNeedUpdate.getPostal_code(), travelerNeedUpdate.getSlogan(),
                    travelerNeedUpdate.getAbout_me(), generalService.createSqlArray(Arrays.asList(travelerNeedUpdate.getLanguage())),
                    travelerNeedUpdate.getCountry(), travelerNeedUpdate.getCity(), travelerNeedUpdate.getAvatar_link(),
                    travelerNeedUpdate.getTraveler_id());
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public void favoritePost(int traveler_id, int post_id) {
        try {
            String query = "insert into favoritepost (traveler_id, post_id) values (?,?)";
            jdbcTemplate.update(query, traveler_id, post_id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }
}
