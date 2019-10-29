package services.ordertrip;

import entities.Order;

import java.time.LocalDate;
import java.util.ArrayList;

public interface OrderTripService {
    public int createOrder(Order newOrder);

    public Order findOrder(int order_id);

    public int editOrder(Order orderNeedUpdate);

    public int cancelOrder(int id);

    public void getOrderGuiderId_Price_EndDate(Order newOrder);

    public int checkAvailabilityOfOrder(Order newOrder);

    public ArrayList<String> getGuiderAvailableHours(LocalDate date, int post_id, int guider_id);
}
