package services.trip;

import entities.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface TripService {
    void createTrip(Order newOrder) throws Exception;

    int checkTripExist(int id) throws Exception;

    Order findTripById(int trip_id) throws Exception;

    List<Order> findTripByStatus(String role, int id, String status) throws Exception;

    boolean acceptTrip(int trip_id) throws Exception;

    boolean cancelTrip(int trip_id) throws Exception;

    boolean finishTrip(int trip_id) throws Exception;

    void getTripGuiderId_FinishDate(Order newOrder) throws Exception;

    int checkAvailabilityOfTrip(Order newOrder) throws Exception;

    ArrayList<String> getGuiderAvailableHours(LocalDate date, int post_id, int guider_id) throws Exception;

    String getClosestTripFinishDate(LocalDate date, int guider_id) throws Exception;

    List<Order> getTripByWeek(int id, Date start, Date end) throws Exception;

    boolean checkTripReach48Hours(Order cancelOrder, LocalDateTime rightNow) throws Exception;

    String getExpectedEndTripTime(int post_id, LocalDateTime begin_date) throws Exception;
}
