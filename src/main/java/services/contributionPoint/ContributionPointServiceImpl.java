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
import java.util.Date;
import java.util.List;

@Repository
public class ContributionPointServiceImpl {
    @Value("${percentage.on.totalmoney}")
    private String TotalMoney;

    @Value("${percentage.on.travellerreturn}")
    private String OnTravellerReturn;

    @Value("${percentage.on.ratedStar}")
    private String OnRatedStar;

    @Value("${point.being.minus.onInactivateMonth}")
    private String OnInActivateMonth;

    private int percentageOnTotalMoney;
    private int percentageOnTravellerReturn;
    private int percentageOnRatedStar;
    private int pointBeingMinusOnInactivateMonth;

    private JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(ContributionPointServiceImpl.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public ContributionPointServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Scheduled(cron = "0 59 23 1/1 * ? *")
    public void updatePointAtTheEndOfTheDay(){
        percentageOnTotalMoney = Integer.parseInt(TotalMoney);
        percentageOnTravellerReturn = Integer.parseInt(OnTravellerReturn);
        percentageOnRatedStar = Integer.parseInt(OnRatedStar);
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        int day = localDate.getDayOfMonth();

        List<Order> orders = jdbcTemplate.query("select * from ordertrip where date_part('month',finish_date) = ? and " +
                "date_part('year',finish_date)= ? and date_part('day',finish_date)= ?", new RowMapper<Order>() {
            @Override
            public Order mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Order(
                        resultSet.getLong("guider_id"),
                        resultSet.getLong("traveler_id"),
                        resultSet.getLong("fee_paid")

                );
            }
        },month,year,day);

        for ( Order order: orders){
            long guider_id = order.getGuider_id();
            long fee_paid = order.getFee_paid();
            long traveler_id = order.getTraveler_id();
            long amountReturnOfATraveller = jdbcTemplate.queryForObject("SELECT count(*) FROM ordertrip WHERE guider_id = ? and traveler_id = ?", Long.class, guider_id, traveler_id);
            long contributionPoint = amountReturnOfATraveller * percentageOnTravellerReturn + fee_paid * percentageOnTotalMoney;
            long oldContribution = jdbcTemplate.queryForObject("select contribution from guider where guider_id = ? ", Long.class, guider_id);
            long updatedContribution = oldContribution + contributionPoint;
            jdbcTemplate.update("update guider set contribution=  ? where guider_id = ?", updatedContribution, guider_id);
        }

        List<Review> reviews = jdbcTemplate.query("select * from review where date_part('month',post_date) = ? and " +
                "date_part('year',post_date)= ? and date_part('day',post_date)= ?", new RowMapper<Review>() {
            @Override
            public Review mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Review(
                        resultSet.getLong("guider_id"),
                        resultSet.getLong("rated")

                );
            }
        },month,year,day);

        for ( Review review:reviews){
            long guider_id = review.getGuider_id();
            long ratedStar = review.getRated_star();
            long contributionPoint = 0;
            if (ratedStar <= 3) {
                contributionPoint += (ratedStar - 4) * percentageOnRatedStar;
            } else {
                contributionPoint += ratedStar * percentageOnRatedStar;
            }
            long oldContribution = jdbcTemplate.queryForObject("select contribution from guider where guider_id = ? ", Long.class, guider_id);
            long updatedContribution = oldContribution + contributionPoint;
            jdbcTemplate.update("update guider set contribution=  ? where guider_id = ?", updatedContribution, guider_id);
        }
    }

    @Scheduled(cron = "0 0 0 1 1/1 ? *")
    public void updatePointAtTheEndOfTheMonth() {
        pointBeingMinusOnInactivateMonth = Integer.parseInt(OnInActivateMonth);
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        int lastMonth = 0;
        int lastYear = year - 1;
        if (month == 1) {
            lastMonth = 12;
            year = lastYear;
        } else {
            lastMonth = month - 1;
        }

        String sqlToCountTotalRecordsInAMonth = "WITH counttotal" +
                "     AS (select  guider_id as guider_id  from ordertrip where date_part('month',finish_date) ='11' group by guider_id)" +
                "   select g.guider_id,g.contribution from guider g left outer join counttotal c on g.guider_id = c.guider_id where c.guider_id is null";

        List<Guider> guiderBeingMinusPoint =jdbcTemplate.query("WITH counttotal AS (select  guider_id as guider_id  from ordertrip " +
                        "where date_part('month',finish_date) = ? and date_part('year',finish_date) =? group by guider_id) " +
                        "select g.guider_id,g.contribution from guider g left outer join counttotal c on g.guider_id = c.guider_id where c.guider_id is null",
                new RowMapper<Guider>() {
                    @Override
                    public Guider mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new Guider(
                                resultSet.getLong("guider_id"),
                                resultSet.getLong("contribution")
                        );
                    }
                },lastMonth,year);

        for (Guider guider : guiderBeingMinusPoint){
            guider.setContribution_point(guider.getContribution_point()-pointBeingMinusOnInactivateMonth);
            jdbcTemplate.update("update guider set contribution = ? where guider_id = ?",guider.getContribution_point(),guider.getGuider_id());
        }
        //log.info("The time is now {}", dateFormat.format(new Date()));
    }
}
