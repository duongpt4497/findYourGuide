package services.ordertrip;

import entities.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface OrderTripService {
    public int createOrder(Order newOrder);

    public Order findOrderById(int order_id);

    public List<Order> findOrderByStatus(int guider_id, String status);

    public boolean acceptOrder(int order_id);

    public boolean cancelOrder(int order_id);

    public boolean finishOrder(int order_id);

    public void getOrderGuiderId_FinishDate(Order newOrder);

    public int checkAvailabilityOfOrder(Order newOrder);

    public ArrayList<String> getGuiderAvailableHours(LocalDate date, int post_id, int guider_id);
}
