package services.ordertrip;

import entities.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

@Repository
public class OrderTripServiceImpl implements OrderTripService {
    private Logger logger;
    private JdbcTemplate jdbcTemplate;

    public OrderTripServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long createOrder(Order newOrder) {
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
                ps.setDate(4, new java.sql.Date(newOrder.getBegin_date().getTime()));
                ps.setDate(5, new java.sql.Date(newOrder.getFinish_date().getTime()));
                ps.setInt(6, newOrder.getAdult_quantity());
                ps.setInt(7, newOrder.getChildren_quantity());
                ps.setLong(8, newOrder.getFee_paid());
                ps.setBoolean(9, false);
                return ps;
            }, keyHolder);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return (long) keyHolder.getKey();
    }

    @Override
    public Order findOrder(long order_id) {
        Order searchOrder = new Order();
        try {
            String query = "select * from ordertrip where order_id = ?";
            searchOrder = jdbcTemplate.queryForObject(query, new RowMapper<Order>() {
                @Override
                public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Order(rs.getLong("order_id"), rs.getLong("traveler_id"),
                            rs.getLong("guider_id"), rs.getLong("post_id"),
                            rs.getDate("begin_date"), rs.getDate("finish_date"),
                            rs.getInt("adult_quantity"), rs.getInt("children_quantity"),
                            rs.getLong("fee_paid"), rs.getBoolean("canceled"));
                }
            }, order_id);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return searchOrder;
    }

    @Override
    public long editOrder(Order orderNeedUpdate) {
        return 0;
    }

    @Override
    public long cancelOrder(long id) {
        try {
            String query = "update ordertrip set canceled = true where order_id = ?";
            jdbcTemplate.update(query, id);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return id;
    }
}
