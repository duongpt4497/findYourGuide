package services.Statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class StatisticServiceImpl implements StatisticService {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public StatisticServiceImpl(JdbcTemplate jt) {
        this.jdbcTemplate = jt;
    }

    @Override
    public List<String> getStatisticCompletedTrip() throws Exception {
        List<String> result;
        String query = "select date_trunc('month', finish_date) as fin_month, count (trip_id) as total_trip " +
                "from trip where status = 'FINISHED' and EXTRACT(YEAR FROM finish_date) = EXTRACT(YEAR FROM now()) " +
                "group by fin_month order by fin_month asc";
        result = jdbcTemplate.query(query, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                LocalDateTime fin_month = rs.getTimestamp("fin_month").toLocalDateTime();
                return "{ x: new Date(" + fin_month.getYear() + ", " + fin_month.getMonthValue() + "), y: " +
                        rs.getInt("total_trip") + " },";
            }
        });
        if (!result.isEmpty()) {
            int size = result.size();
            String lastItem = result.get(size - 1);
            lastItem = lastItem.substring(0, lastItem.length() - 1);
            result.set(size - 1, lastItem);
        }
        return result;
    }

    @Override
    public List<String> getStatisticTotalRevenue() throws Exception {
        List<String> result;
        String query = "select date_trunc('month', finish_date) as fin_month, sum(fee_paid) as revenue " +
                "from trip where status = 'FINISHED' and EXTRACT(YEAR FROM finish_date) = EXTRACT(YEAR FROM now()) " +
                "group by fin_month order by fin_month asc";
        result = jdbcTemplate.query(query, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                LocalDateTime fin_month = rs.getTimestamp("fin_month").toLocalDateTime();
                return "{ x: new Date(" + fin_month.getYear() + ", " + fin_month.getMonthValue() + "), y: " +
                        rs.getDouble("revenue") + " },";
            }
        });
        if (!result.isEmpty()) {
            int size = result.size();
            String lastItem = result.get(size - 1);
            lastItem = lastItem.substring(0, lastItem.length() - 1);
            result.set(size - 1, lastItem);
        }
        return result;
    }
}
