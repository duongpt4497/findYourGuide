package services.guider;

import entities.Guider;
import entities.Guider_Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import services.GeneralServiceImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class GuiderServiceImpl implements GuiderService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;
    private GeneralServiceImpl generalService;

    public GuiderServiceImpl(JdbcTemplate jdbcTemplate, GeneralServiceImpl generalService) {
        this.jdbcTemplate = jdbcTemplate;
        this.generalService = generalService;
    }

    @Override
    public Guider findGuiderWithID(long id) {
        Guider result = new Guider();
        try {
            String query = "select * from guider where guider_id = ?";
            result = jdbcTemplate.queryForObject(query, new RowMapper<Guider>() {
                public Guider mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Guider(rs.getLong("guider_id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getInt("age"), rs.getString("about_me"),
                            rs.getLong("contribution"), rs.getString("city"),
                            generalService.checkForNull(rs.getArray("languages")),
                            rs.getBoolean("active"), rs.getLong("rated"), rs.getString("avatar"),
                            rs.getString("passion"));
                }
            }, id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return result;
    }

    @Override
    public Guider findGuiderWithPostId(long id) {
        try {
            String query = "select g.* from guider as g, post as p where g.guider_id = p.guider_id and p.post_id = ?";
            return jdbcTemplate.queryForObject(query, new RowMapper<Guider>() {
                public Guider mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Guider(rs.getLong("guider_id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getInt("age"), rs.getString("about_me"),
                            rs.getLong("contribution"), rs.getString("city"),
                            generalService.checkForNull(rs.getArray("languages")),
                            rs.getBoolean("active"), rs.getLong("rated"), rs.getString("avartar"),
                            rs.getString("passion"));
                }
            }, id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @Override
    public Guider_Contract findGuiderContract(long id) {
        Guider_Contract result = new Guider_Contract();
        try {
            String query = "select * from Guider_Contract where guider_id = ?";
            result = jdbcTemplate.queryForObject(query, new RowMapper<Guider_Contract>() {
                @Override
                public Guider_Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Guider_Contract(rs.getLong("guider_id"), rs.getString("name"),
                            rs.getString("nationality"),
                            rs.getDate("date_of_birth"), rs.getInt("gender"), rs.getString("hometown"),
                            rs.getString("address"), rs.getString("identity_card_number"),
                            rs.getDate("card_issued_date"), rs.getString("card_issued_province"),
                            rs.getDate("account_active_date"));
                }
            }, id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return result;
    }

    @Override
    public long createGuider(Guider newGuider) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            String query = "insert into guider (first_name,last_name,age,about_me,contribution,city,active,language)" +
                    "values (?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(query, new String[]{"guider_id"});
                ps.setString(1, newGuider.getFirst_name());
                ps.setString(2, newGuider.getLast_name());
                ps.setInt(3, newGuider.getAge());
                ps.setString(4, newGuider.getAbout_me());
                ps.setLong(5, 0);
                ps.setString(6, newGuider.getCity());
                ps.setBoolean(7, true);
                ps.setArray(8, generalService.createSqlArray(Arrays.asList(newGuider.getLanguages())));
                return ps;
            }, keyHolder);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return (long) keyHolder.getKey();
    }

    @Override
    public void createGuiderContract(long guider_id, Guider_Contract newGuiderContract) {
        try {
            String query = "insert into guider_contract (guider_id,name,nationality,date_of_birth,gender,hometown,address,identity_card_number,card_issued_date,card_issued_province,account_active_date)" +
                    "values (?,?,?,?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(query, guider_id, newGuiderContract.getName(), newGuiderContract.getNationality(),
                    new java.sql.Date(newGuiderContract.getDate_of_birth().getTime()), newGuiderContract.getGender(),
                    newGuiderContract.getHometown(), newGuiderContract.getAddress(), newGuiderContract.getIdentity_card_number(),
                    new java.sql.Date(newGuiderContract.getCard_issued_date().getTime()), newGuiderContract.getCard_issued_province(),
                    java.sql.Date.valueOf(LocalDate.now()));
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    @Override
    public long updateGuiderWithId(Guider guiderNeedUpdate) {
        try {
            String query = "update guider set first_name = ?, last_name = ?, age = ?, about_me = ?, city = ?, language = ? where guider_id = ?";
            jdbcTemplate.update(query, guiderNeedUpdate.getFirst_name(), guiderNeedUpdate.getLast_name(),
                    guiderNeedUpdate.getAge(), guiderNeedUpdate.getAbout_me(), guiderNeedUpdate.getCity(),
                    guiderNeedUpdate.getGuider_id(), generalService.createSqlArray(Arrays.asList(guiderNeedUpdate.getLanguages())));
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return guiderNeedUpdate.getGuider_id();
    }

    @Override
    public long activateGuider(long id) {
        try {
            String query = "update guider set active = true where guider_id = ? and active = false";
            jdbcTemplate.update(query, id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return id;
    }

    @Override
    public long deactivateGuider(long id) {
        try {
            String query = "update guider set active = false where guider_id = ? and active = true";
            jdbcTemplate.update(query, id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return id;
    }

    @Override
    public List<Guider> searchGuider(String key) {
        List<Guider> result = new ArrayList<>();
        try {
            String query = "select g.* from guider as g "
                    + " inner join account as a on g.guider_id = a.account_id "
                    + " where g.first_name like '%" + key + "%' "
                    + " or g.last_name like '%" + key + "%'  or a.user_name like '%" + key + "%' ; ";
            result = jdbcTemplate.query(query, new RowMapper<Guider>() {
                public Guider mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Guider(rs.getLong("guider_id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getInt("age"), rs.getString("about_me"),
                            rs.getLong("contribution"), rs.getString("city"),
                            generalService.checkForNull(rs.getArray("languages")),
                            rs.getBoolean("active"), rs.getLong("rated"), rs.getString("avartar"),
                            rs.getString("passion"));
                }
            });
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return result;
    }

    @Override
    public List<Guider> getTopGuiderByRate() {
        List<Guider> result = new ArrayList<>();
        try {
            String query = "SELECT * FROM guider order by rated desc limit 5";
            result = jdbcTemplate.query(query, new RowMapper<Guider>() {
                public Guider mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Guider(rs.getLong("guider_id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getInt("age"), rs.getString("about_me"),
                            rs.getLong("contribution"), rs.getString("city"),
                            generalService.checkForNull(rs.getArray("languages")),
                            rs.getBoolean("active"), rs.getLong("rated"), rs.getString("avatar"),
                            rs.getString("passion"));
                }
            });
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return result;
    }

    @Override
    public List<Guider> getTopGuiderByContribute() {
        List<Guider> result = new ArrayList<>();
        try {
            String query = "SELECT * FROM guider order by contribution desc limit 5";
            result = jdbcTemplate.query(query, new RowMapper<Guider>() {
                public Guider mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Guider(rs.getLong("guider_id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getInt("age"), rs.getString("about_me"),
                            rs.getLong("contribution"), rs.getString("city"),
                            generalService.checkForNull(rs.getArray("languages")),
                            rs.getBoolean("active"), rs.getLong("rated"), rs.getString("avatar"),
                            rs.getString("passion"));
                }
            });
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return result;
    }
}
