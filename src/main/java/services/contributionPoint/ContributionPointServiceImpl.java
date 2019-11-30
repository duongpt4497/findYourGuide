package services.contributionPoint;

import entities.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ContributionPointServiceImpl implements ContributionPointService {
    private static final Logger log = LoggerFactory.getLogger(ContributionPointServiceImpl.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy    HH:mm:ss");

    @Value("${correlation.price}")
    private String corMoney;
    @Value("${correlation.return}")
    private String corTurn;
    @Value("${correlation.rated}")
    private String corRated;
    @Value("${point.being.minus.onInactivateMonth}")
    private String minus;
    @Value("${percent.bonus.perMonth.20}")
    private String bonus20;
    @Value("${percent.bonus.perMonth.15}")
    private String bonus15;
    @Value("${percent.bonus.perMonth.10}")
    private String bonus10;
    @Value("${percent.bonus.perMonth.5}")
    private String bonus5;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ContributionPointServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //params: fee_paid in order, rated in review, no of turn count in order
    public long calculateContributionPerTour(double fee, float rated, int count) {
        return java.lang.Math.round(fee * Double.parseDouble(corMoney)
                + (rated - 3) * Double.parseDouble(corRated) + (count - 1) * Double.parseDouble(corTurn));
    }

    //query all order finished in the second day before
    //calculate contribute increasing
    //update contribute to guider
    @Scheduled(cron = "0 1 0 * * *")
    public void updateContributionbyDay() {
        List<Order> lo = new ArrayList<>();
        String query = "select o1.trip_id, o1.traveler_id, o1.guider_id, o1.fee_paid, r2.rated from trip as o1 "
                + " inner join review as r2 on o1.trip_id = r2.trip_id"
//                + " where extract (epoch from (now() - o1.finish_date))::integer "
                //                + " < extract(epoch from TIMESTAMP '1970-1-3 00:00:00')::integer "
                //                + " and where extract (epoch from (now() - o1.finish_date))::integer "
                //                + " >= extract(epoch from TIMESTAMP '1970-1-2 00:00:00')::integer "
                //                + " and o1.status = 'FINISHED'  ; "
                ;
        lo = jdbcTemplate.query(query, new RowMapper<Order>() {
            @Override
            public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Order(rs.getInt("trip_id"),
                        rs.getInt("traveler_id"),
                        rs.getInt("guider_id"),
                        rs.getFloat("fee_paid"),
                        rs.getFloat("rated"));
            }
        });
        String count = "select count(traveler_id) from trip where guider_id = ? "
                + " and traveler_id= ? group by traveler_id, guider_id; ";
        List<String> update = new ArrayList<>();
        for (Order o : lo) {
            int turn = jdbcTemplate.queryForObject(count, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getInt("count");

                }
            }, o.getGuider_id(), o.getTraveler_id());
//            log.warn("count result :" + turn);
            update.add("update guider set contribution = contribution + "
                    + calculateContributionPerTour(o.getFee_paid(), o.getRated(), turn)
                    + " where guider_id = " + o.gettrip_id());
        }
        log.warn(update.toString());
        //jdbcTemplate.batchUpdate(update.toArray(new String[0])[10]);

    }

    //query all negative and positive guider
    //bonus or reduce contribute 
    //update to guider
    @Scheduled(cron = "0 0 1 */30 * *")
    public void updateContributionbyMonth() {

        String queryPositive = "select guider_id, count(guider_id) from trip where "
//                + " extract (epoch from (now() - o1.finish_date))::integer "
//                + " <= extract(epoch from TIMESTAMP '1970-1-31 00:00:00')::integer and  "
                + " status = 'FINISHED' group by guider_id ; ";
        List<Map<String, Object>> positiveGuider = jdbcTemplate.queryForList(queryPositive);
        String queryNegative = " select guider_id from guider where contribution < ? except "
                + " select guider_id from trip where "
//                + "  extract (epoch from (now() - o1.finish_date))::integer "
//                + " <= extract(epoch from TIMESTAMP '1970-1-31 00:00:00')::integer and  "
                + " status = 'FINISHED'  ; ";
        List<Map<String, Object>> negativeGuider = jdbcTemplate.queryForList(queryNegative, Integer.parseInt(minus));

        List<String> update = new ArrayList<>();
        for (Map m : positiveGuider) {

            String incomeQuery = "select sum(fee_paid) from trip where guider_id = ? and "
//                    + "  extract (epoch from (now() - o1.finish_date))::integer "
//                    + " <= extract(epoch from TIMESTAMP '1970-1-31 00:00:00')::integer and  "
                    + " status = 'FINISHED' group by guider_id; ";
            int income = jdbcTemplate.queryForObject(incomeQuery, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getInt("sum");

                }
            }, m.get("guider_id"));

            double bonus;
            long count = (long) m.get("count");
            if (count >= 20) {
                bonus = income * Double.parseDouble(bonus20);
            } else if (count >= 15) {
                bonus = income * Double.parseDouble(bonus15);
            } else if (count >= 10) {
                bonus = income * Double.parseDouble(bonus10);
            } else if (count >= 5) {
                bonus = income * Double.parseDouble(bonus5);
            } else {
                bonus = income;
            }

            update.add("update guider set contribution = contribution + "
                    + (long) Math.floor(bonus) + " where guider_id = " + m.get("guider_id"));
        }

        for (Map m : negativeGuider) {
            update.add("update guider set contribution = contribution - "
                    + minus + " where guider_id = " + m.get("guider_id"));
        }
        log.warn(update.toString());
        //jdbcTemplate.batchUpdate(update.toArray(new String[0])[10]);
    }

    @Override
    public void penaltyGuiderForCancel(int guider_id) throws Exception {
        String query = "select contribution from guider where guider_id = ?";
        int point = jdbcTemplate.queryForObject(query, new Object[]{guider_id}, int.class);
        point -= 500;
        if (point < 0) {
            point = 0;
        }
        String queryUpdate = "update guider set contribution = ? where guider_id = ?";
        jdbcTemplate.update(queryUpdate, point, guider_id);
    }
}
