package services.guider;

import entities.Contract;
import entities.Guider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import services.GeneralService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class GuiderServiceImpl implements GuiderService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;
    private GeneralService generalService;

    @Autowired
    public GuiderServiceImpl(JdbcTemplate jdbcTemplate, GeneralService generalService) {
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
                            rs.getString("last_name"), rs.getInt("age"), rs.getString("phone"),
                            rs.getString("about_me"),
                            rs.getLong("contribution"), rs.getString("city"),
                            generalService.checkForNull(rs.getArray("languages")),
                            rs.getBoolean("active"), rs.getLong("rated"), rs.getString("avatar"),
                            rs.getString("passion"));
                }
            }, id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
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
                            rs.getString("last_name"), rs.getInt("age"), rs.getString("phone"),
                            rs.getString("about_me"),
                            rs.getLong("contribution"), rs.getString("city"),
                            generalService.checkForNull(rs.getArray("languages")),
                            rs.getBoolean("active"), rs.getLong("rated"), rs.getString("avatar"),
                            rs.getString("passion"));
                }
            }, id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @Override
    public Contract findGuiderContract(long id) {
        Contract result = new Contract();
        try {
            String query = "select contract_detail.* from contract_detail inner join contract on contract.guider_id = ?";
            result = jdbcTemplate.queryForObject(query, new RowMapper<Contract>() {
                @Override
                public Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Contract ct = new Contract(rs.getLong("contract_id"), rs.getString("name"),
                            rs.getString("nationality"),
                            rs.getTimestamp("date_of_birth").toLocalDateTime(), rs.getInt("gender"),
                            rs.getString("hometown"),
                            rs.getString("address"), rs.getString("identity_card_number"),
                            rs.getTimestamp("card_issued_date").toLocalDateTime(), rs.getString("card_issued_province"),
                            rs.getTimestamp("account_active_date").toLocalDateTime(),
                            (rs.getTimestamp("account_deactive_date") != null ? rs.getTimestamp("account_deactive_date").toLocalDateTime() : null));
                    return ct;
                }
            }, id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return result;
    }

    @Override
    public long createGuider(Guider newGuider) {
        try {
            String query = "insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(query, newGuider.getGuider_id(), newGuider.getFirst_name(), newGuider.getLast_name(),
                    newGuider.getAge(), newGuider.getPhone(), newGuider.getAbout_me(), newGuider.getContribution(),
                    newGuider.getCity(), generalService.createSqlArray(Arrays.asList(newGuider.getLanguages())), true,
                    newGuider.getRated(), newGuider.getAvatar(), newGuider.getPassion());
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
        return newGuider.getGuider_id();
    }

    @Override
    public long createGuiderContract(Contract contract) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            String query = "insert into contract_detail (name,nationality,date_of_birth,gender,hometown,address,identity_card_number,card_issued_date,card_issued_province,account_active_date)" +
                    "values (?,?,?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(query, new String[]{"contract_id"});
                ps.setString(1, contract.getName());
                ps.setString(2, contract.getNationality());
                ps.setTimestamp(3, Timestamp.valueOf(contract.getDate_of_birth()));
                ps.setLong(4, contract.getGender());
                ps.setString(5, contract.getHometown());
                ps.setString(6, contract.getAddress());
                ps.setString(7, contract.getIdentity_card_number());
                ps.setTimestamp(8, Timestamp.valueOf(contract.getCard_issued_date()));
                ps.setString(9, contract.getCard_issued_province());
                ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                return ps;
            }, keyHolder);
            return (int) keyHolder.getKey();
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public long updateGuiderWithId(Guider guiderUpdate) {
        try {
            String query = "update guider set first_name = ?, last_name = ?, age = ?, phone = ?, about_me = ?, city = ?, " +
                    "languages = ?, avatar = ?, passion = ? where guider_id = ?";
            jdbcTemplate.update(query, guiderUpdate.getFirst_name(), guiderUpdate.getLast_name(),
                    guiderUpdate.getAge(), guiderUpdate.getPhone(), guiderUpdate.getAbout_me(), guiderUpdate.getCity(),
                    generalService.createSqlArray(Arrays.asList(guiderUpdate.getLanguages())), guiderUpdate.getAvatar(),
                    guiderUpdate.getPassion(), guiderUpdate.getGuider_id());
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
        return guiderUpdate.getGuider_id();
    }

    @Override
    public long activateGuider(long id) {
        try {
            String query = "update guider set active = true where guider_id = ? and active = false";
            jdbcTemplate.update(query, id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
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
                            rs.getString("last_name"), rs.getInt("age"), rs.getString("phone"),
                            rs.getString("about_me"),
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
    public List<Guider> getTopGuiderByRate() {
        List<Guider> result = new ArrayList<>();
        try {
            String query = "SELECT * FROM guider order by rated desc limit 6";
            result = jdbcTemplate.query(query, new RowMapper<Guider>() {
                public Guider mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Guider(rs.getLong("guider_id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getInt("age"), rs.getString("phone"),
                            rs.getString("about_me"),
                            rs.getLong("contribution"), rs.getString("city"),
                            generalService.checkForNull(rs.getArray("languages")),
                            rs.getBoolean("active"), rs.getLong("rated"), rs.getString("avatar"),
                            rs.getString("passion"));
                }
            });
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
        return result;
    }

    @Override
    public List<Guider> getTopGuiderByContribute() {
        List<Guider> result = new ArrayList<>();
        try {
            String query = "SELECT * FROM guider order by contribution desc limit 6";
            result = jdbcTemplate.query(query, new RowMapper<Guider>() {
                public Guider mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Guider(rs.getLong("guider_id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getInt("age"), rs.getString("phone"),
                            rs.getString("about_me"),
                            rs.getLong("contribution"), rs.getString("city"),
                            generalService.checkForNull(rs.getArray("languages")),
                            rs.getBoolean("active"), rs.getLong("rated"), rs.getString("avatar"),
                            rs.getString("passion"));
                }
            });
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
        return result;
    }

    @Override
    public void linkGuiderWithContract(long guider_id, long contract_id) {
        try {
            String check = "select count(guider_id) from contract where guider_id = ?";
            int count = jdbcTemplate.queryForObject(check, new Object[]{guider_id}, int.class);
            if (count == 0) {
                String query = "insert into contract (guider_id, contract_id) values (?,?)";
                jdbcTemplate.update(query, guider_id, contract_id);
            } else {
                String query = "update contract set contract_id = ? where guider_id = ?";
                jdbcTemplate.update(query, contract_id, guider_id);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }
}
