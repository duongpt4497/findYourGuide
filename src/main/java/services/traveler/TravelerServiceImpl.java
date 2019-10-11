package services.traveler;

import entities.Traveler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import services.GeneralService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
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
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            String insertQuery = "insert into traveler (first_name, last_name, gender, date_of_birth, phone, email, street, " +
                    "house_number, slogan, postal_code, about_me, language, country, city, avatar_link) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(insertQuery, new String[]{"traveler_id"});
                ps.setString(1, newTraveler.getFirst_name());
                ps.setString(2, newTraveler.getLast_name());
                ps.setInt(3, newTraveler.getGender());
                ps.setDate(4, new java.sql.Date(newTraveler.getDate_of_birth().getTime()));
                ps.setString(5, newTraveler.getPhone());
                ps.setString(6, newTraveler.getEmail());
                ps.setString(7, newTraveler.getStreet());
                ps.setString(8, newTraveler.getHouse_number());
                ps.setString(9, newTraveler.getSlogan());
                ps.setString(10, newTraveler.getPostal_code());
                ps.setString(11, newTraveler.getAbout_me());
                ps.setArray(12, generalService.createSqlArray(Arrays.asList(newTraveler.getLanguage())));
                ps.setString(13, newTraveler.getCountry());
                ps.setString(14, newTraveler.getCity());
                ps.setString(15, newTraveler.getAvatar_link());
                return ps;
            }, keyHolder);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return (long) keyHolder.getKey();
    }

    @Override
    public Traveler findTravelerWithId(long id) {
        Traveler searchTraveler = new Traveler();
        try {
            String query = "select * from traveler where traveler_id = ?";
            searchTraveler = jdbcTemplate.queryForObject(query, new RowMapper<Traveler>() {
                @Override
                public Traveler mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Traveler(rs.getLong("traveler_id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getString("phone"), rs.getString("email"),
                            rs.getInt("gender"), rs.getDate("date_of_birth"), rs.getString("street"),
                            rs.getString("house_number"), rs.getString("postal_code"),
                            rs.getString("slogan"), rs.getString("about_me"),
                            generalService.checkForNull(rs.getArray("language")), rs.getString("country"),
                            rs.getString("city"), rs.getString("avatar_link"));
                }
            }, id);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return searchTraveler;
    }

    @Override
    public void updateTraveler(Traveler travelerNeedUpdate) {
        try {
            String query = "update traveler set first_name = ?, last_name = ?, phone = ?, email = ?, gender = ?," +
                    "date_of_birth = ?, street = ?, house_number = ?, postal_code = ?, slogan = ?, about_me = ?," +
                    "language = ?, country = ?, city = ?, avatar_link = ? where traveler_id = ?";
            jdbcTemplate.update(query, travelerNeedUpdate.getFirst_name(), travelerNeedUpdate.getLast_name(),
                    travelerNeedUpdate.getPhone(), travelerNeedUpdate.getEmail(), travelerNeedUpdate.getGender(),
                    new java.sql.Date(travelerNeedUpdate.getDate_of_birth().getTime()), travelerNeedUpdate.getStreet(),
                    travelerNeedUpdate.getHouse_number(), travelerNeedUpdate.getPostal_code(), travelerNeedUpdate.getSlogan(),
                    travelerNeedUpdate.getAbout_me(), generalService.createSqlArray(Arrays.asList(travelerNeedUpdate.getLanguage())),
                    travelerNeedUpdate.getCountry(), travelerNeedUpdate.getCity(), travelerNeedUpdate.getAvatar_link(),
                    travelerNeedUpdate.getTraveler_id());
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
}
