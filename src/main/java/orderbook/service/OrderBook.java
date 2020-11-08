package orderbook.service;

import orderbook.model.Order;

import java.util.List;

public interface OrderBook {

    List<Order> getBids();

    List<Order> getAsks();

    void addOrder(Order oe);

    void modOrder(Order oe);

    void delOrder(Order oe);
}
