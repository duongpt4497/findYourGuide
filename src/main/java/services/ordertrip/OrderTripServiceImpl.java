package services.ordertrip;

import entities.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Repository
public class OrderTripServiceImpl implements OrderTripService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;

    @Value("${order.buffer}")
    private String bufferPercent;

    public OrderTripServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int createOrder(Order newOrder) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            String insertQuery = "insert into ordertrip (traveler_id,guider_id,post_id,begin_date,finish_date,adult_quantity,children_quantity,fee_paid,canceled)" +
                    "values (?,?,?,?,?,?,?,?,?)";
            double totalHour = this.getTourTotalHour(newOrder.getPost_id());
            long bufferHour = (long) java.lang.Math.ceil(totalHour / 100 * Integer.parseInt(bufferPercent));
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(insertQuery, new String[]{"order_id"});
                ps.setLong(1, newOrder.getTraveler_id());
                ps.setLong(2, newOrder.getGuider_id());
                ps.setLong(3, newOrder.getPost_id());
                ps.setTimestamp(4, Timestamp.valueOf(newOrder.getBegin_date()));
                ps.setTimestamp(5, Timestamp.valueOf(newOrder.getFinish_date().plusHours(bufferHour).minusMinutes(30)));
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
                            rs.getTimestamp("begin_date").toLocalDateTime(),
                            rs.getTimestamp("finish_date").toLocalDateTime(),
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
                    Timestamp finishDate = Timestamp.valueOf(newOrder.getBegin_date().plusHours(rs.getLong("total_hour")));
                    newOrder.setFinish_date(finishDate.toLocalDateTime());
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
            Timestamp acceptableBeginDate = Timestamp.valueOf(newOrder.getBegin_date());
            Timestamp acceptableFinishDate = Timestamp.valueOf(newOrder.getFinish_date());
            count = jdbcTemplate.queryForObject(query, new Object[]{guider_id, acceptableBeginDate,
                    acceptableFinishDate, acceptableBeginDate, acceptableFinishDate}, int.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return count;
    }

    @Override
    public ArrayList<String> getGuiderAvailableHours(LocalDate date, int post_id, int guider_id) {
        // Create default available hours
        ArrayList<String> availableHours = new ArrayList<>();
        this.createHourArray(availableHours, 0, 24);

        // Get guider schedule
        List<Order> guiderSchedule = this.getGuiderSchedule(guider_id, date);
        if (!guiderSchedule.isEmpty()) {
            // Get occupy hours
            ArrayList<String> occupyHour = this.getOccupyHours(guiderSchedule, date);
            // Clear out occupy hours from available hours
            availableHours.removeAll(occupyHour);
        }

        // Get unacceptable hours
        List<Order> guiderScheduleNextDay = this.getGuiderSchedule(guider_id, date.plusDays(1));
        ArrayList<String> nextDayOccupyHour = this.getOccupyHours(guiderScheduleNextDay, date.plusDays(1));
        ArrayList<String> unacceptableHours = this.getUnacceptableHours(post_id, availableHours, nextDayOccupyHour, date);
        if (!unacceptableHours.isEmpty()) {
            // Clear out unacceptable hours
            availableHours.removeAll(unacceptableHours);
        }
        return availableHours;
    }

    private double getTourTotalHour(int post_id) {
        double total_hour = 0;
        try {
            String query = "SELECT total_hour FROM post where post_id = ?";
            total_hour = (double) jdbcTemplate.queryForObject(query, new Object[]{post_id}, int.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return total_hour;
    }

    private void createHourArray(ArrayList<String> array, int startHour, int endHour) {
        String[] hourTails = {"00", "30"};
        for (int i = startHour; i < endHour; i++) {
            for (int j = 0; j < 2; j++) {
                String availableTime = i + ":" + hourTails[j];
                if (i < 10) {
                    availableTime = "0" + availableTime;
                }
                if (!array.contains(availableTime)) {
                    array.add(availableTime);
                }
            }
        }
    }

    private List<Order> getGuiderSchedule(int guider_id, LocalDate date) {
        List<Order> guiderSchedule = new ArrayList<>();
        String query = "SELECT begin_date, finish_date FROM ordertrip " +
                "where guider_id = ? " +
                "and Date(begin_date) = ? " +
                "or Date(finish_date) = ? " +
                "order by begin_date";
        try {
            guiderSchedule = jdbcTemplate.query(query, new RowMapper<Order>() {
                @Override
                public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Order temp = new Order();
                    temp.setBegin_date(rs.getTimestamp("begin_date").toLocalDateTime());
                    temp.setFinish_date(rs.getTimestamp("finish_date").toLocalDateTime());
                    return temp;
                }
            }, guider_id, date, date);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return guiderSchedule;
    }

    private ArrayList<String> getOccupyHours(List<Order> guiderSchedule, LocalDate date) {
        ArrayList<String> occupyHour = new ArrayList<>();
        for (Order order : guiderSchedule) {
            int beginHour = order.getBegin_date().getHour();
            int finishHour = order.getFinish_date().getHour();
            int beginMinute = order.getBegin_date().getMinute();
            int finishMinute = order.getFinish_date().getMinute();

            if (order.getBegin_date().getDayOfMonth() < date.getDayOfMonth()) {
                this.createHourArray(occupyHour, 0, finishHour + 1);
                if (finishMinute == 0) {
                    this.removeHour(occupyHour, finishHour, "right");
                }
            } else if (order.getFinish_date().getDayOfMonth() > date.getDayOfMonth()) {
                boolean frontExist = this.checkDuplicateHours(occupyHour, beginHour);
                this.createHourArray(occupyHour, beginHour, 24);
                if (!frontExist) {
                    if (beginMinute != 0) {
                        this.removeHour(occupyHour, beginHour, "left");
                    }
                }
            } else {
                boolean frontExist = this.checkDuplicateHours(occupyHour, beginHour);
                this.createHourArray(occupyHour, beginHour, finishHour + 1);
                if (beginMinute != 0 && finishMinute == 0) {
                    if (!frontExist) {
                        this.removeHour(occupyHour, beginHour, "left");
                    }
                    this.removeHour(occupyHour, finishHour, "right");
                } else if (beginMinute == 0 && finishMinute == 0) {
                    this.removeHour(occupyHour, finishHour, "right");
                } else if (beginMinute != 0 && finishMinute != 0) {
                    if (!frontExist) {
                        this.removeHour(occupyHour, beginHour, "left");
                    }
                }
            }
        }
        Collections.sort(occupyHour);
        return occupyHour;
    }

    private ArrayList<String> getUnacceptableHours(int post_id, ArrayList<String> availableHours,
                                                   ArrayList<String> nextDayOccupyHour, LocalDate date) {
        ArrayList<String> unacceptableHours = new ArrayList<>();
        double totalHour = this.getTourTotalHour(post_id);
        long bufferHour = (long) java.lang.Math.ceil(totalHour / 100 * Integer.parseInt(bufferPercent));
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (String hour : availableHours) {
            try {
                Date temp = format.parse(date + " " + hour);
                calendar.setTime(temp);
                calendar.add(Calendar.HOUR_OF_DAY, (int) totalHour);
                calendar.add(Calendar.HOUR_OF_DAY, (int) bufferHour);
                calendar.add(Calendar.MINUTE, -30);
                String checkTime = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                        String.format("%02d", calendar.get(Calendar.MINUTE));
                if (calendar.get(Calendar.DAY_OF_MONTH) > date.getDayOfMonth()) {
                    if (nextDayOccupyHour.contains(checkTime)) {
                        unacceptableHours.add(hour);
                    }
                } else {
                    if (!availableHours.contains(checkTime)) {
                        unacceptableHours.add(hour);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return unacceptableHours;
    }

    private void removeHour(ArrayList<String> array, int hour, String type) {
        if (hour < 10) {
            array.remove("0" + hour + (type.equals("left") ? ":00" : ":30"));
        } else {
            array.remove(hour + (type.equals("left") ? ":00" : ":30"));
        }
    }

    private boolean checkDuplicateHours(ArrayList<String> occupyHour, int hour) {
        if (hour < 10) {
            if (occupyHour.contains("0" + hour + ":00") || occupyHour.contains("0" + hour + ":30")) {
                return true;
            }
        } else {
            if (occupyHour.contains(hour + ":00") || occupyHour.contains(hour + ":30")) {
                return true;
            }
        }
        return false;
    }
}
