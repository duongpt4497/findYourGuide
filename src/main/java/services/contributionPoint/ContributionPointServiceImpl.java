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
public class ContributionPointServiceImpl implements ContributionPointService {
    @Value("${percentage.on.totalmoney}")
    private String TotalMoney;

    @Value("${percentage.on.travellerreturn}")
    private String OnTravellerReturn;

    @Value("${percentage.on.ratedStar}")
    private String OnRatedStar;


    private int percentageOnTotalMoney;
    private int percentageOnTravellerReturn;
    private int percentageOnRatedStar;

    private JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(ContributionPointServiceImpl.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public ContributionPointServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public long getAmountReturnOfATraveller(long guider_id, long traveller_id) {
        long amountReturnOfATraveller = jdbcTemplate.queryForObject("SELECT count(*) FROM ordertrip WHERE guider_id = ? and traveler_id = ?", Long.class, guider_id, traveller_id);
        return amountReturnOfATraveller;
    }


    @Override
    public long calculatePointAfterEachOrder(Order order) {
        percentageOnTotalMoney = Integer.parseInt(TotalMoney);
        percentageOnTravellerReturn = Integer.parseInt(OnTravellerReturn);

        long amountReturnOfATraveller = getAmountReturnOfATraveller(order.getGuider_id(), order.getTraveler_id());
        long getTotalMoneyOfOrder = order.getFee_paid();
        long totalTripsInOneMonth = 0;
        long contributionPoint = 0;

        contributionPoint = amountReturnOfATraveller * percentageOnTravellerReturn + getTotalMoneyOfOrder * percentageOnTotalMoney;
        if (totalTripsInOneMonth == 20) {
            contributionPoint += 2;
        }
        return contributionPoint;
    }

    @Override
    public long calculatePointAfterEachReview(Review review) {
        long ratedStar = review.getRated_star();
        long contributionPoint = 0;
        if (ratedStar <= 3) {
            contributionPoint += (ratedStar - 4) * percentageOnRatedStar;
        } else {
            contributionPoint += ratedStar * percentageOnRatedStar;
        }
        return contributionPoint;
    }

    @Override
    public void updateContributionPoint(long guider_id, long contributionPoint) {
        long oldContribution = jdbcTemplate.queryForObject("select contribution from guider where guider_id = ? ", Long.class, guider_id);
        long updatedContribution = oldContribution + contributionPoint;
        jdbcTemplate.update("update guider set contribution=  ? where guider_id = ?", updatedContribution, guider_id);
    }

    @Scheduled(cron = "0 0 0 1 1/1 ? *")
    public void reportCurrentTime() {
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
            guider.setContribution_point(guider.getContribution_point()-) =
        }
        log.info("The time is now {}", dateFormat.format(new Date()));
    }
}
