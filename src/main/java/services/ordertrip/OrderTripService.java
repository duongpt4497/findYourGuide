package services.ordertrip;

import entities.Order;

import java.time.LocalDate;
import java.util.ArrayList;

public interface OrderTripService {
    public void createOrder(Order newOrder);

    public Order findOrder(int order_id);

    public int editOrder(Order orderNeedUpdate);

    public int cancelOrder(int id);

    public void getOrderGuiderId_FinishDate(Order newOrder);

    public int checkAvailabilityOfOrder(Order newOrder);

    public ArrayList<String> getGuiderAvailableHours(LocalDate date, int post_id, int guider_id);
}
