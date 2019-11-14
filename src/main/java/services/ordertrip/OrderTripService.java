package services.ordertrip;

import entities.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface OrderTripService {
    public void createOrder(Order newOrder);

    public int checkOrderExist(int id);

    public Order findOrderById(int order_id);

    public List<Order> findOrderByStatusAsGuider(String role, int id, String status);

    public boolean acceptOrder(int order_id);

    public boolean cancelOrder(int order_id);

    public boolean finishOrder(int order_id);

    public void getOrderGuiderId_FinishDate(Order newOrder);

    public int checkAvailabilityOfOrder(Order newOrder);

    public ArrayList<String> getGuiderAvailableHours(LocalDate date, int post_id, int guider_id);

    public String getClosestTourFinishDate(LocalDate date, int guider_id);

    public List<Order> getOrderByWeek(int id, Date start, Date end);

    public boolean checkOrderReach48Hours(Order cancelOrder, LocalDateTime rightNow);

    public String getExpectedEndTourTime(int post_id, LocalDateTime begin_date);

}
