package services.contributionPoint;

import entities.Guider;
import entities.Order;
import entities.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class ContributionPointServiceImpl implements ContributionPointService{

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

    private int percentageOnTotalMoney;
    private int percentageOnTravellerReturn;
    private int percentageOnRatedStar;

    private JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(ContributionPointServiceImpl.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy    HH:mm:ss");

    public ContributionPointServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //params: fee_paid in order, rated in review, no of turn count in order
    public long calculateContributionPerTour(float fee, float rated, int count) {
        return java.lang.Math.round(fee * Double.parseDouble(corMoney)
                + (rated - 3) * Double.parseDouble(corRated) + (count - 1) * Double.parseDouble(corTurn));
    }

    //query all order finished in the second day before
    //calculate contribute increasing
    //update contribute to guider
    @Scheduled(cron = "0 0 0 * * *")
    public void updateContributionbyDay() {
        List<Order> lo = new ArrayList<>();
        String query = "select o1.order_id, o1.traveler_id, o1.guider_id, o1.fee_paid, r2.rated from ordertrip as o1 "
                + " inner join review as r2 on o1.order_id = r2.order_id" 
//                + " where extract (epoch from (now() - o1.finish_date))::integer "
                //                + " < extract(epoch from TIMESTAMP '1970-1-3 00:00:00')::integer "
                //                + " and where extract (epoch from (now() - o1.finish_date))::integer "
                //                + " >= extract(epoch from TIMESTAMP '1970-1-2 00:00:00')::integer "
                //                + " and o1.status = 'FINISHED'  ; "
                ;
        lo = jdbcTemplate.query(query, new RowMapper<Order>() {
            @Override
            public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Order(rs.getLong("order_id"),
                        rs.getLong("traveler_id"),
                        rs.getLong("guider_id"),
                        rs.getFloat("fee_paid"),
                        rs.getFloat("rated"));
            }
        });
        String count = "select count(traveler_id) from ordertrip where guider_id = ? "
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
                    + " where guider_id = " + o.getOrder_id());
        }
        log.warn(update.toString());
        //jdbcTemplate.batchUpdate(update.toArray(new String[0])[10]);

    }

    //query all negative and positive guider
    //bonus or reduce contribute 
    //update to guider
    @Scheduled(cron = "0 0 1 */30 * *")
    public void updateContributionbyMonth() {

        String queryPositive = "select guider_id, count(guider_id) from ordertrip where "
//                + " extract (epoch from (now() - o1.finish_date))::integer "
//                + " <= extract(epoch from TIMESTAMP '1970-1-31 00:00:00')::integer and  "
                + " status = 'FINISHED' group by guider_id ; ";
        List<Map<String, Object>> positiveGuider = jdbcTemplate.queryForList(queryPositive);
        String queryNegative = " select guider_id from guider where contribution < ? except "
                + " select guider_id from ordertrip where "
//                + "  extract (epoch from (now() - o1.finish_date))::integer "
//                + " <= extract(epoch from TIMESTAMP '1970-1-31 00:00:00')::integer and  "
                + " status = 'FINISHED'  ; ";
        List<Map<String, Object>> negativeGuider = jdbcTemplate.queryForList(queryNegative, Integer.parseInt(minus));

        List<String> update = new ArrayList<>();
        for (Map m : positiveGuider) {

            String incomeQuery = "select sum(fee_paid) from ordertrip where guider_id = ? and "
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
                    + (long)Math.floor(bonus) + " where guider_id = " + m.get("guider_id"));
        }
        for (Map m : negativeGuider) {

            update.add("update guider set contribution = contribution - "
                    + minus + " where guider_id = " + m.get("guider_id"));
        }
        log.warn(update.toString());
        //jdbcTemplate.batchUpdate(update.toArray(new String[0])[10]);
    }

//                    Date date = new Date();
//        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        int month = localDate.getMonthValue();
//        int year = localDate.getYear();
//        int lastMonth = 0;
//        int lastYear = year - 1;
//        if (month == 1) {
//            lastMonth = 12;
//            year = lastYear;
//        } else {
//            lastMonth = month - 1;
//        }
    //log.info("The time is now {}", dateFormat.format(new Date()));
//                String sqlToCountTotalRecordsInAMonth = "WITH counttotal" +
//                "     AS (select  guider_id as guider_id  from ordertrip where date_part('month',finish_date) ='11' group by guider_id)" +
//                "   select g.guider_id,g.contribution from guider g left outer join counttotal c on g.guider_id = c.guider_id where c.guider_id is null";
//
//        List<Guider> guiderBeingMinusPoint =jdbcTemplate.query("WITH counttotal AS (select  guider_id as guider_id  from ordertrip " +
//                        "where date_part('month',finish_date) = ? and date_part('year',finish_date) =? group by guider_id) " +
//                        "select g.guider_id,g.contribution from guider g left outer join counttotal c on g.guider_id = c.guider_id where c.guider_id is null",
//                new RowMapper<Guider>() {
//                    @Override
//                    public Guider mapRow(ResultSet resultSet, int i) throws SQLException {
//                        return new Guider(
//                                resultSet.getLong("guider_id"),
//                                resultSet.getLong("contribution")
//                        );
//                    }
//                },lastMonth,year);
    //    @Override
//    public void updateContributionPoint(long guider_id, long contributionPoint) {
//        long oldContribution = jdbcTemplate.queryForObject("select contribution from guider where guider_id = ? ", Long.class, guider_id);
//        long updatedContribution = oldContribution + contributionPoint;
//        jdbcTemplate.update("update guider set contribution=  ? where guider_id = ?", updatedContribution, guider_id);
//    }
    //
//    @Override
//    public long getAmountReturnOfATraveller(long guider_id, long traveller_id) {
//        long amountReturnOfATraveller = jdbcTemplate.queryForObject("SELECT count(*) FROM ordertrip WHERE guider_id = ? and traveler_id = ?", Long.class, guider_id, traveller_id);
//        return amountReturnOfATraveller;
//    }
//    @Override
//    public long calculatePointAfterEachOrder(Order order) {
//        percentageOnTotalMoney = Integer.parseInt(TotalMoney);
//        percentageOnTravellerReturn = Integer.parseInt(OnTravellerReturn);
//
//        long amountReturnOfATraveller = getAmountReturnOfATraveller(order.getGuider_id(), order.getTraveler_id());
//        long getTotalMoneyOfOrder = order.getFee_paid();
//        long totalTripsInOneMonth = 0;
//        long contributionPoint = 0;
//
//        contributionPoint = amountReturnOfATraveller * percentageOnTravellerReturn + getTotalMoneyOfOrder * percentageOnTotalMoney;
//        if (totalTripsInOneMonth == 20) {
//            contributionPoint += 2;
//        }
//        return contributionPoint;
//        return 0;
//    }
//
//    @Override
//    public long calculatePointAfterEachReview(Review review) {
//        float ratedStar = review.getRated();
//        long contributionPoint = 0;
//        if (ratedStar <= 3) {
//            contributionPoint += (ratedStar - 3) * percentageOnRatedStar;
//        } else {
//            contributionPoint += ratedStar * percentageOnRatedStar;
//        }
//        return contributionPoint;
//    }
}
