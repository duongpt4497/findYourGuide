package services.trip;

import entities.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface TripService {
    public void createTrip(Order newOrder) throws Exception;

    public int checkTripExist(int id) throws Exception;

    public Order findTripById(int trip_id) throws Exception;

    public List<Order> findTripByStatus(String role, int id, String status) throws Exception;

    public boolean acceptTrip(int trip_id) throws Exception;

    public boolean cancelTrip(int trip_id) throws Exception;

    public boolean finishTrip(int trip_id) throws Exception;

    public void getTripGuiderId_FinishDate(Order newOrder) throws Exception;

    public int checkAvailabilityOfTrip(Order newOrder) throws Exception;

    public ArrayList<String> getGuiderAvailableHours(LocalDate date, int post_id, int guider_id) throws Exception;

    public String getClosestTripFinishDate(LocalDate date, int guider_id) throws Exception;

    public List<Order> getTripByWeek(int id, Date start, Date end) throws Exception;

    public boolean checkTripReach48Hours(Order cancelOrder, LocalDateTime rightNow) throws Exception;

    public String getExpectedEndTripTime(int post_id, LocalDateTime begin_date) throws Exception;

}
