package services.ordertrip;

import entities.Order;

import java.util.List;

public interface OrderTripService {
    public long createOrder(Order newOrder);

    public Order findOrder(long order_id);

    public long editOrder(Order orderNeedUpdate);

    public long cancelOrder(long id);
}
