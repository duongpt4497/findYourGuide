package services.ordertrip;

import entities.Order;

public interface OrderTripService {
    public int createOrder(Order newOrder);

    public Order findOrder(int order_id);

    public int editOrder(Order orderNeedUpdate);

    public int cancelOrder(int id);

    public void getOrderGuiderId_Price_EndDate(Order newOrder);

    public int checkAvailabilityOfOrder(Order newOrder);
}
