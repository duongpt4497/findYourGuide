package services.guider;

import entities.Contract;
import entities.Guider;
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
    private JdbcTemplate jdbcTemplate;
    private GeneralService generalService;

    @Autowired
    public GuiderServiceImpl(JdbcTemplate jdbcTemplate, GeneralService generalService) {
        this.jdbcTemplate = jdbcTemplate;
        this.generalService = generalService;
    }

    @Override
    public Guider findGuiderWithID(long id) throws Exception {
        Guider result = new Guider();
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
        return result;
    }

    @Override
    public Guider findGuiderWithPostId(long id) throws Exception {
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
    }

    @Override
    public Contract findGuiderContract(long id) throws Exception {
        Contract result = new Contract();
        String query = "select contract_detail.* from contract_detail inner join contract on contract.guider_id = ?";
        result = jdbcTemplate.queryForObject(query, new RowMapper<Contract>() {
            @Override
            public Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
                Contract ct = new Contract(rs.getLong("contract_id"), rs.getInt("guider_id"),
                        rs.getString("name"), rs.getString("nationality"),
                        rs.getTimestamp("date_of_birth").toLocalDateTime(), rs.getInt("gender"),
                        rs.getString("hometown"),
                        rs.getString("address"), rs.getString("identity_card_number"),
                        rs.getTimestamp("card_issued_date").toLocalDateTime(), rs.getString("card_issued_province"),
                        (rs.getTimestamp("account_active_date") != null ? rs.getTimestamp("account_active_date").toLocalDateTime() : null),
                        (rs.getTimestamp("account_deactive_date") != null ? rs.getTimestamp("account_deactive_date").toLocalDateTime() : null));
                return ct;
            }
        }, id);
        return result;
    }

    @Override
    public long createGuider(Guider newGuider) throws Exception {
        String query = "insert into guider (guider_id,first_name,last_name,age,phone,about_me,contribution,city,languages,active,rated,avatar,passion)" +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(query, newGuider.getGuider_id(), newGuider.getFirst_name(), newGuider.getLast_name(),
                newGuider.getAge(), newGuider.getPhone(), newGuider.getAbout_me(), newGuider.getContribution(),
                newGuider.getCity(), generalService.createSqlArray(Arrays.asList(newGuider.getLanguages())), newGuider.isActive(),
                newGuider.getRated(), newGuider.getAvatar(), newGuider.getPassion());
        return newGuider.getGuider_id();
    }

    @Override
    public long createGuiderContract(Contract contract) throws Exception {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "insert into contract_detail (name,nationality,date_of_birth,gender,hometown,address,identity_card_number,card_issued_date,card_issued_province,guider_id)" +
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
            ps.setLong(10, contract.getGuider_id());
            return ps;
        }, keyHolder);
        return (int) keyHolder.getKey();
    }

    @Override
    public long updateGuiderWithId(Guider guiderUpdate) throws Exception {
        String query = "update guider set first_name = ?, last_name = ?, age = ?, phone = ?, about_me = ?, city = ?, " +
                "languages = ?, avatar = ?, passion = ? where guider_id = ?";
        jdbcTemplate.update(query, guiderUpdate.getFirst_name(), guiderUpdate.getLast_name(),
                guiderUpdate.getAge(), guiderUpdate.getPhone(), guiderUpdate.getAbout_me(), guiderUpdate.getCity(),
                generalService.createSqlArray(Arrays.asList(guiderUpdate.getLanguages())), guiderUpdate.getAvatar(),
                guiderUpdate.getPassion(), guiderUpdate.getGuider_id());
        return guiderUpdate.getGuider_id();
    }

    @Override
    public long activateGuider(long id) throws Exception {
        String query = "update guider set active = true where guider_id = ? and active = false";
        jdbcTemplate.update(query, id);
        return id;
    }

    @Override
    public long deactivateGuider(long id) throws Exception {
        String query = "update guider set active = false where guider_id = ? and active = true";
        jdbcTemplate.update(query, id);
        return id;
    }


    @Override
    public List<Guider> searchGuiderByName(String key) throws Exception {
        List<Guider> result = new ArrayList<>();
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
        return result;
    }

    @Override
    public List<Guider> getTopGuiderByRate() throws Exception {
        List<Guider> result = new ArrayList<>();
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
        return result;
    }

    @Override
    public List<Guider> getTopGuiderByContribute() throws Exception {
        List<Guider> result = new ArrayList<>();
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
        return result;
    }

    @Override
    public void linkGuiderWithContract(long guider_id, long contract_id) throws Exception {
        String check = "select count(guider_id) from contract where guider_id = ?";
        int count = jdbcTemplate.queryForObject(check, new Object[]{guider_id}, int.class);
        if (count == 0) {
            // if new guider with new contract
            // link contract
            String query = "insert into contract (guider_id, contract_id) values (?,?)";
            jdbcTemplate.update(query, guider_id, contract_id);

            // update active date
            String query2 = "update contract_detail set account_active_date = ? where contract_id = ?";
            jdbcTemplate.update(query2, Timestamp.valueOf(LocalDateTime.now()), contract_id);
        } else {
            // if old guider update their contract
            // de-active old contract
            String getOldContractQuery = "select contract_id from contract where guider_id = ?";
            int oldContractId = jdbcTemplate.queryForObject(getOldContractQuery, new Object[]{guider_id}, int.class);
            String deactivateOldContractQuery = "update contract_detail set account_deactive_date = ? where contract_id = ?";
            jdbcTemplate.update(deactivateOldContractQuery, Timestamp.valueOf(LocalDateTime.now()), oldContractId);

            // link to new contract
            String query = "update contract set contract_id = ? where guider_id = ?";
            jdbcTemplate.update(query, contract_id, guider_id);

            // update active date
            String query2 = "update contract_detail set account_active_date = ? where contract_id = ?";
            jdbcTemplate.update(query2, Timestamp.valueOf(LocalDateTime.now()), contract_id);
        }
    }

    @Override
    public List<Contract> getAllContract() throws Exception {
        List<Contract> list;
        String query = "select * from contract_detail where account_active_date is null and account_deactive_date is null " +
                "and contract_id not in (select contract_id from contract)";
        list = jdbcTemplate.query(query, new RowMapper<Contract>() {
            @Override
            public Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Contract(rs.getInt("contract_id"), rs.getInt("guider_id"), rs.getString("name"),
                        rs.getString("nationality"), rs.getTimestamp("date_of_birth").toLocalDateTime(),
                        rs.getInt("gender"), rs.getString("hometown"), rs.getString("address"),
                        rs.getString("identity_card_number"), rs.getTimestamp("card_issued_date").toLocalDateTime(),
                        rs.getString("card_issued_province"));
            }
        });
        return list;
    }

    @Override
    public void rejectContract(long contract_id) {
        String query = "update contract_detail set account_deactive_date = now() where contract_id = ?";
        jdbcTemplate.update(query, contract_id);
    }
}
