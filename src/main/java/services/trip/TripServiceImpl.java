package services.trip;

import com.paypal.base.rest.PayPalRESTException;
import entities.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import services.Paypal.PaypalService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class TripServiceImpl implements TripService {
    private static final String UNCONFIRMED = "UNCONFIRMED";
    private static final String ONGOING = "ONGOING";
    private static final String FINISHED = "FINISHED";
    private static final String CANCELLED = "CANCELLED";

    private static final String HOUR_TAIL_0 = ":00";
    private static final String HOUR_TAIL_30 = ":30";
    private static final String HOUR_POSITION_BEFORE = "before";
    private static final String HOUR_POSITION_AFTER = "after";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;

    @Value("${order.buffer}")
    private String bufferPercent;
    @Autowired
    private PaypalService payment;

    @Autowired
    public TripServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTrip(Order newOrder) throws Exception {
        String insertQuery = "insert into trip (traveler_id,post_id,begin_date,finish_date,adult_quantity,children_quantity,fee_paid,transaction_id,status)"
                + "values (?,?,?,?,?,?,?,?,?)";
        double totalHour = this.getTourTotalHour(newOrder.getPost_id());
        long bufferHour = (long) java.lang.Math.ceil(totalHour / 100 * Integer.parseInt(bufferPercent));
        jdbcTemplate.update(insertQuery, newOrder.getTraveler_id(), newOrder.getPost_id(),
                Timestamp.valueOf(newOrder.getBegin_date()),
                Timestamp.valueOf(newOrder.getFinish_date().plusHours(bufferHour).minusMinutes(30)),
                newOrder.getAdult_quantity(), newOrder.getChildren_quantity(), newOrder.getFee_paid(),
                newOrder.getTransaction_id(), UNCONFIRMED);
    }

    @Override
    public Order findTripById(int trip_id) throws Exception {
        Order searchOrder = new Order();
        String query = "select * from trip where trip_id = ?";
        searchOrder = jdbcTemplate.queryForObject(query, new RowMapper<Order>() {
            @Override
            public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Order(rs.getInt("trip_id"), rs.getInt("traveler_id"),
                        rs.getInt("post_id"),
                        rs.getTimestamp("begin_date").toLocalDateTime(),
                        rs.getTimestamp("finish_date").toLocalDateTime(),
                        rs.getInt("adult_quantity"), rs.getInt("children_quantity"),
                        rs.getLong("fee_paid"), rs.getString("transaction_id"),
                        rs.getString("status"));
            }
        }, trip_id);
        return searchOrder;
    }

    @Override
    public List<Order> findTripByStatus(String role, int id, String status) throws Exception {
        List<Order> result = new ArrayList<>();
        String query = "";
        if (role.equalsIgnoreCase("guider")) {
            query = "SELECT o.*, p.guider_id, p.title, t.first_name, t.last_name FROM trip as o "
                    + " inner join traveler as t on o.traveler_id = t.traveler_id "
                    + " inner join post as p on p.post_id = o.post_id "
                    + " where p.guider_id = ? and status = ? "
                    + " order by begin_date desc ";
        } else if (role.equalsIgnoreCase("traveler")) {
            query = "SELECT o.*, p.guider_id, p.title, g.first_name, g.last_name " +
                    "FROM trip as o " +
                    "inner join post as p on p.post_id = o.post_id " +
                    "inner join guider as g on p.guider_id = g.guider_id " +
                    "where o.traveler_id = ? and status = ? " +
                    "order by begin_date desc";
        } else {
            throw new Exception("wrong role");
        }

        result = jdbcTemplate.query(query, new RowMapper<Order>() {
            @Override
            public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Order(rs.getInt("trip_id"),
                        rs.getInt("traveler_id"),
                        rs.getInt("guider_id"),
                        rs.getInt("post_id"),
                        rs.getTimestamp("begin_date").toLocalDateTime(),
                        rs.getTimestamp("finish_date").toLocalDateTime(),
                        rs.getInt("adult_quantity"),
                        rs.getInt("children_quantity"),
                        rs.getDouble("fee_paid"),
                        rs.getString("transaction_id"),
                        rs.getString("status"),
                        rs.getString("title"),
                        rs.getString("first_name") + " " + rs.getString("last_name"));
            }
        }, id, status);
        System.out.println(query + id + status + result.size());
        return result;
    }

    @Override
    public boolean acceptTrip(int trip_id) throws Exception {
        String check = "select count(trip_id) from trip where trip_id = ? and status = ?";
        int count = jdbcTemplate.queryForObject(check, new Object[]{trip_id, UNCONFIRMED}, int.class);
        if (count == 0) {
            return false;
        }
        String query = "update trip set status = ? where trip_id = ?";
        jdbcTemplate.update(query, ONGOING, trip_id);
        return true;
    }

    @Override
    public boolean cancelTrip(int trip_id) throws Exception {
        String check = "select count(trip_id) from trip where trip_id = ? and status = ? or status = ?";
        int count = jdbcTemplate.queryForObject(check, new Object[]{trip_id, UNCONFIRMED, ONGOING}, int.class);
        if (count == 0) {
            return false;
        }
        String query = "update trip set status = ? where trip_id = ?";
        jdbcTemplate.update(query, CANCELLED, trip_id);
        return true;
    }

    @Override
    public boolean finishTrip(int trip_id) throws Exception {
        String check = "select count(trip_id) from trip where trip_id = ? and status = ?";
        int count = jdbcTemplate.queryForObject(check, new Object[]{trip_id, ONGOING}, int.class);
        if (count == 0) {
            return false;
        }
        String query = "update trip set status = ? where trip_id = ?";
        jdbcTemplate.update(query, FINISHED, trip_id);
        return true;
    }

    @Override
    public void getTripGuiderId_FinishDate(Order newOrder) throws Exception {
        String query = "SELECT guider_id, total_hour FROM post where post_id = ?";
        jdbcTemplate.queryForObject(query, new RowMapper<Order>() {
            @Override
            public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
                newOrder.setGuider_id(rs.getInt("guider_id"));
                Timestamp finishDate = Timestamp.valueOf(newOrder.getBegin_date().plusHours(rs.getLong("total_hour")));
                newOrder.setFinish_date(finishDate.toLocalDateTime());
                return newOrder;
            }
        }, newOrder.getPost_id());
    }


    @Override
    public int checkTripExist(int id) throws Exception {
        int count = 0;
        String query = "SELECT count (trip.trip_id) FROM trip inner join post on guider_id = ?";
        count = jdbcTemplate.queryForObject(query, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("count");

            }
        }, id);
        return count;
    }

    @Override
    public int checkAvailabilityOfTrip(Order newOrder) throws Exception {
        int count = 0;
        String query = "SELECT count (trip_id) FROM trip " +
                "inner join post on guider_id = ? " +
                "where (status = ?) " +
                "and ((begin_date between ? and ?) " +
                "or (finish_date between ? and ?))";
        int guider_id = newOrder.getGuider_id();
        Timestamp acceptableBeginDate = Timestamp.valueOf(newOrder.getBegin_date());
        Timestamp acceptableFinishDate = Timestamp.valueOf(newOrder.getFinish_date());
        count = jdbcTemplate.queryForObject(query, new Object[]{guider_id, ONGOING, acceptableBeginDate,
                acceptableFinishDate, acceptableBeginDate, acceptableFinishDate}, int.class);
        return count;
    }

    @Override
    public ArrayList<String> getGuiderAvailableHours(LocalDate date, int post_id, int guider_id) throws Exception {
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

    @Override
    public String getClosestTripFinishDate(LocalDate date, int guider_id) throws Exception {
        String closestFinishDate = "";
        String query = "SELECT finish_date FROM trip "
                + "inner join post on guider_id = ? "
                + "where finish_date < ? "
                + "and status = ? "
                + "order by finish_date desc "
                + "limit 1";
        List<String> result = jdbcTemplate.query(query, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        }, guider_id, date, FINISHED);
        if (result == null || result.isEmpty()) {
            return "";
        } else {
            closestFinishDate = result.get(0);
        }
        closestFinishDate = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(closestFinishDate));
        return closestFinishDate;
    }

    @Override
    public boolean checkTripReach48Hours(Order cancelOrder, LocalDateTime rightNow) throws Exception {
        int dayCheck = rightNow.toLocalDate().compareTo(cancelOrder.getBegin_date().toLocalDate().minusDays(2));
        // check day
        if (dayCheck == 0) {
            // check hour
            int beginHour = cancelOrder.getBegin_date().getHour();
            int hourCheck = Integer.compare(beginHour, rightNow.getHour());
            if (hourCheck < 0) {
                return true;
            } else if (hourCheck > 0) {
                return false;
            } else {
                // check minute
                int beginMinute = cancelOrder.getBegin_date().getMinute();
                int minuteCheck = Integer.compare(beginMinute, rightNow.getMinute());
                if (minuteCheck <= 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } else if (dayCheck < 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String getExpectedEndTripTime(int post_id, LocalDateTime begin_date) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        double totalHour = this.getTourTotalHour(post_id);
        long bufferHour = (long) java.lang.Math.ceil(totalHour / 100 * Integer.parseInt(bufferPercent));
        long duration = (long) totalHour + bufferHour;
        LocalDateTime end_date = begin_date.plusHours(duration).minusMinutes(30);
        return end_date.format(formatter);
    }

    private double getTourTotalHour(int post_id) throws Exception {
        double total_hour = 0;
        String query = "SELECT total_hour FROM post where post_id = ?";
        total_hour = (double) jdbcTemplate.queryForObject(query, new Object[]{post_id}, int.class);
        return total_hour;
    }

    private void createHourArray(ArrayList<String> array, int startHour, int endHour) throws Exception {
        String[] hourTails = {HOUR_TAIL_0, HOUR_TAIL_30};
        for (int i = startHour; i < endHour; i++) {
            for (int j = 0; j < 2; j++) {
                String availableTime = i + hourTails[j];
                if (i < 10) {
                    availableTime = "0" + availableTime;
                }
                if (!array.contains(availableTime)) {
                    array.add(availableTime);
                }
            }
        }
    }

    private List<Order> getGuiderSchedule(int guider_id, LocalDate date) throws Exception {
        List<Order> guiderSchedule = new ArrayList<>();
        String query = "SELECT begin_date, finish_date FROM trip "
                + "inner join post on guider_id = ? "
                + "where status = ? "
                + "and Date(begin_date) = ? "
                + "or Date(finish_date) = ? "
                + "order by begin_date";
        guiderSchedule = jdbcTemplate.query(query, new RowMapper<Order>() {
            @Override
            public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
                Order temp = new Order();
                temp.setBegin_date(rs.getTimestamp("begin_date").toLocalDateTime());
                temp.setFinish_date(rs.getTimestamp("finish_date").toLocalDateTime());
                return temp;
            }
        }, guider_id, ONGOING, date, date);
        return guiderSchedule;
    }

    private ArrayList<String> getOccupyHours(List<Order> guiderSchedule, LocalDate date) throws Exception {
        ArrayList<String> occupyHour = new ArrayList<>();
        for (Order order : guiderSchedule) {
            int beginHour = order.getBegin_date().getHour();
            int finishHour = order.getFinish_date().getHour();
            int beginMinute = order.getBegin_date().getMinute();
            int finishMinute = order.getFinish_date().getMinute();

            if (order.getBegin_date().getDayOfMonth() < date.getDayOfMonth()) {
                this.createHourArray(occupyHour, 0, finishHour + 1);
                if (finishMinute == 0) {
                    this.removeHour(occupyHour, finishHour, HOUR_POSITION_AFTER);
                }
            } else if (order.getFinish_date().getDayOfMonth() > date.getDayOfMonth()) {
                boolean frontExist = this.checkDuplicateHours(occupyHour, beginHour);
                this.createHourArray(occupyHour, beginHour, 24);
                if (!frontExist) {
                    if (beginMinute != 0) {
                        this.removeHour(occupyHour, beginHour, HOUR_POSITION_BEFORE);
                    }
                }
            } else {
                boolean frontExist = this.checkDuplicateHours(occupyHour, beginHour);
                this.createHourArray(occupyHour, beginHour, finishHour + 1);
                if (beginMinute != 0 && finishMinute == 0) {
                    if (!frontExist) {
                        this.removeHour(occupyHour, beginHour, HOUR_POSITION_BEFORE);
                    }
                    this.removeHour(occupyHour, finishHour, HOUR_POSITION_AFTER);
                } else if (beginMinute == 0 && finishMinute == 0) {
                    this.removeHour(occupyHour, finishHour, HOUR_POSITION_AFTER);
                } else if (beginMinute != 0 && finishMinute != 0) {
                    if (!frontExist) {
                        this.removeHour(occupyHour, beginHour, HOUR_POSITION_BEFORE);
                    }
                }
            }
        }
        Collections.sort(occupyHour);
        return occupyHour;
    }

    private ArrayList<String> getUnacceptableHours(int post_id, ArrayList<String> availableHours,
                                                   ArrayList<String> nextDayOccupyHour, LocalDate date) throws Exception {
        ArrayList<String> unacceptableHours = new ArrayList<>();
        double totalHour = this.getTourTotalHour(post_id);
        long bufferHour = (long) java.lang.Math.ceil(totalHour / 100 * Integer.parseInt(bufferPercent));
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (String hour : availableHours) {
            Date temp = format.parse(date + " " + hour);
            calendar.setTime(temp);
            calendar.add(Calendar.HOUR_OF_DAY, (int) totalHour);
            calendar.add(Calendar.HOUR_OF_DAY, (int) bufferHour);
            calendar.add(Calendar.MINUTE, -30);
            String checkTime = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                    + String.format("%02d", calendar.get(Calendar.MINUTE));
            if (calendar.get(Calendar.DAY_OF_MONTH) > date.getDayOfMonth()) {
                if (nextDayOccupyHour.contains(checkTime)) {
                    unacceptableHours.add(hour);
                }
            } else {
                if (!availableHours.contains(checkTime)) {
                    unacceptableHours.add(hour);
                }
            }
        }
        return unacceptableHours;
    }

    private void removeHour(ArrayList<String> array, int hour, String type) throws Exception {
        if (hour < 10) {
            array.remove("0" + hour + (type.equals(HOUR_POSITION_BEFORE) ? HOUR_TAIL_0 : HOUR_TAIL_30));
        } else {
            array.remove(hour + (type.equals(HOUR_POSITION_BEFORE) ? HOUR_TAIL_0 : HOUR_TAIL_30));
        }
    }

    private boolean checkDuplicateHours(ArrayList<String> occupyHour, int hour) throws Exception {
        if (hour < 10) {
            if (occupyHour.contains("0" + hour + HOUR_TAIL_0) || occupyHour.contains("0" + hour + HOUR_TAIL_30)) {
                return true;
            }
        } else {
            if (occupyHour.contains(hour + HOUR_TAIL_0) || occupyHour.contains(hour + HOUR_TAIL_30)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Order> getTripByWeek(int id, Date start, Date end) throws Exception {
        List<Order> result = new ArrayList<>();
        String query = "SELECT o.*, p.guider_id, p.title, t.first_name, t.last_name FROM trip as o "
                + " inner join traveler as t on o.traveler_id = t.traveler_id "
                + " inner join post as p on p.post_id = o.post_id "
                + " and p.guider_id = ? and (status = 'ONGOING') "
                + " and (begin_date between ? and ?)   "
                + " order by begin_date desc ";
        result = jdbcTemplate.query(query, new RowMapper<Order>() {
            @Override
            public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Order(rs.getInt("trip_id"),
                        rs.getInt("traveler_id"),
                        rs.getInt("guider_id"),
                        rs.getInt("post_id"),
                        rs.getTimestamp("begin_date").toLocalDateTime(),
                        rs.getTimestamp("finish_date").toLocalDateTime(),
                        rs.getInt("adult_quantity"),
                        rs.getInt("children_quantity"),
                        rs.getDouble("fee_paid"),
                        rs.getString("transaction_id"),
                        rs.getString("status"),
                        rs.getString("title"),
                        rs.getString("first_name") + " " + rs.getString("last_name"));
            }
        }, id, new java.sql.Date(start.getTime()), new java.sql.Date(end.getTime()));
        System.out.println(query + id + result.get(0).getObject());
        return result;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void orderFilter() throws PayPalRESTException {
        List<Map<String, Object>> lo = new ArrayList<>();
        String query = "select trip_id, transaction_id "
                + " where extract (epoch from (now() - order_time))::integer "
                + " < extract(epoch from TIMESTAMP '1970-1-1 05:00:00')::integer "
                + " and status = 'UNCONFIRMED'  ; ";
        lo = jdbcTemplate.queryForList(query);

        List<String> update = new ArrayList<>();
        for (Map m : lo) {
            payment.refundPayment(m.get("transaction_id").toString());
            update.add("update guider set status = 'CANCELLED' where trip_id = " + m.get("trip_id"));
        }
        logger.warn(update.toString());
        //jdbcTemplate.batchUpdate(update.toArray(new String[0])[10]);
    }
}