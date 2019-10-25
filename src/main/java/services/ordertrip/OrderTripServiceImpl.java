package services.ordertrip;

import entities.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;

@Repository
public class OrderTripServiceImpl implements OrderTripService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;

    public OrderTripServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int createOrder(Order newOrder) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            String insertQuery = "insert into ordertrip (traveler_id,guider_id,post_id,begin_date,finish_date,adult_quantity,children_quantity,fee_paid,canceled)" +
                    "values (?,?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(insertQuery, new String[]{"order_id"});
                ps.setLong(1, newOrder.getTraveler_id());
                ps.setLong(2, newOrder.getGuider_id());
                ps.setLong(3, newOrder.getPost_id());
                ps.setTimestamp(4, newOrder.getBegin_date());
                ps.setTimestamp(5, newOrder.getFinish_date());
                ps.setInt(6, newOrder.getAdult_quantity());
                ps.setInt(7, newOrder.getChildren_quantity());
                ps.setDouble(8, newOrder.getFee_paid());
                ps.setBoolean(9, false);
                return ps;
            }, keyHolder);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return (int) keyHolder.getKey();
    }

    @Override
    public Order findOrder(int order_id) {
        Order searchOrder = new Order();
        try {
            String query = "select * from ordertrip where order_id = ?";
            searchOrder = jdbcTemplate.queryForObject(query, new RowMapper<Order>() {
                @Override
                public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Order(rs.getInt("order_id"), rs.getInt("traveler_id"),
                            rs.getInt("guider_id"), rs.getInt("post_id"),
                            rs.getTimestamp("begin_date"), rs.getTimestamp("finish_date"),
                            rs.getInt("adult_quantity"), rs.getInt("children_quantity"),
                            rs.getLong("fee_paid"), rs.getBoolean("canceled"));
                }
            }, order_id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return searchOrder;
    }

    @Override
    public int editOrder(Order orderNeedUpdate) {
        return 0;
    }

    @Override
    public int cancelOrder(int id) {
        try {
            String query = "update ordertrip set canceled = true where order_id = ?";
            jdbcTemplate.update(query, id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return id;
    }

    @Override
    public void getOrderGuiderId_Price_EndDate(Order newOrder) {
        try {
            String query = "SELECT guider_id, total_hour, price FROM post where post_id = ?";
            jdbcTemplate.queryForObject(query, new RowMapper<Order>() {
                @Override
                public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
                    newOrder.setGuider_id(rs.getInt("guider_id"));
                    Timestamp finishDate = Timestamp.from(newOrder.getBegin_date().toInstant().plus(rs.getLong("total_hour"), ChronoUnit.HOURS));
                    newOrder.setFinish_date(finishDate);
                    double price = rs.getDouble("price");
                    double fee = price * newOrder.getAdult_quantity() + (price / 2) * newOrder.getChildren_quantity();
                    newOrder.setFee_paid(fee);
                    return newOrder;
                }
            }, newOrder.getPost_id());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public int checkAvailabilityOfOrder(Order newOrder) {
        int count = 0;
        try {
            String query = "SELECT count (order_id) FROM ordertrip " +
                    "where (guider_id = ?) " +
                    "and (begin_date between ? and ?) " +
                    "or (finish_date between ? and ?)";
            int guider_id = newOrder.getGuider_id();
            Timestamp acceptableBeginDate = Timestamp.from(newOrder.getBegin_date().toInstant().minus(1, ChronoUnit.HOURS));
            Timestamp acceptableFinishDate = Timestamp.from(newOrder.getFinish_date().toInstant().plus(1, ChronoUnit.HOURS));
            count = jdbcTemplate.queryForObject(query, new Object[]{guider_id, acceptableBeginDate,
                    acceptableFinishDate, acceptableBeginDate, acceptableFinishDate}, int.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return count;
    }
}
