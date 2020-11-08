package orderbook.service;

import orderbook.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderBookImpl implements OrderBook {
    static Logger LOG = LoggerFactory.getLogger(OrderBookImpl.class);


    @Override
    public List<Order> getBids() {
        return null;
    }

    @Override
    public List<Order> getAsks() {
        return null;
    }

    @Override
    public void addOrder(Order oe) {
        LOG.info("got order to get Added: ",oe);


    }

    @Override
    public void modOrder(Order oe) {

    }

    @Override
    public void delOrder(Order oe) {

    }
}
