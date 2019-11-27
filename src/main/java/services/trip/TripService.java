package services.trip;

import entities.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface TripService {
    public void createTrip(Order newOrder);

    public int checkTripExist(int id);

    public Order findTripById(int trip_id);

    public List<Order> findTripByStatus(String role, int id, String status);

    public boolean acceptTrip(int trip_id);

    public boolean cancelTrip(int trip_id);

    public boolean finishTrip(int trip_id);

    public void getTripGuiderId_FinishDate(Order newOrder);

    public int checkAvailabilityOfTrip(Order newOrder);

    public ArrayList<String> getGuiderAvailableHours(LocalDate date, int post_id, int guider_id);

    public String getClosestTripFinishDate(LocalDate date, int guider_id);

    public List<Order> getTripByWeek(int id, Date start, Date end);

    public boolean checkTripReach48Hours(Order cancelOrder, LocalDateTime rightNow);

    public String getExpectedEndTripTime(int post_id, LocalDateTime begin_date);

}
