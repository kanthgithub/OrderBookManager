package orderbook.service;

import orderbook.model.Order;
import orderbook.model.OrderComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class OrderBookImpl implements OrderBook {
    static Logger LOG = LoggerFactory.getLogger(OrderBookImpl.class);

    LinkedList<Order> bids = new LinkedList<>();
    LinkedList<Order> asks = new LinkedList<>();

    @Override
    public List<Order> getBids() {
        return bids;
    }

    @Override
    public List<Order> getAsks() {
        return asks;
    }

    @Override
    public void addOrder(Order order) {
        LOG.debug("got order to get Added: {} ", order);

        String direction = order.getSide();

        switch (direction){
            case "buy":{
                addOrderToBids(order);
                break;
            }

            case "sell":{
                addOrderToAsks(order);
                break;
            }

            default:
                break;
        }
    }

    public void addOrderToBids(Order order){

        if(bids.size() > 0){

            if(bids.peekFirst().getPrice() < order.getPrice()){

                if(bids.size() == 10) {
                    Order order_Polled = bids.pollLast();
                    LOG.info("KnockingOut Bid-Order: " + order_Polled);
                }

                LOG.info("Offering best Bid-Order: "+order);
                bids.offerFirst(order);
            } else {
                if (bids.size() < 10) {
                    LOG.info("filling Bid-Order: "+order);
                    bids.offerLast(order);
                }
            }

            Collections.sort(bids, OrderComparator.DescBidComparer());

        }else{
            LOG.info("Offering best Bid-Order: "+order);
            bids.offerLast(order);
        }

    }

    public void addOrderToAsks(Order order){


        if(asks.size() > 0){

            if(asks.peekFirst().getPrice() > order.getPrice()){

                if(asks.size() == 10) {
                    Order order_Polled = asks.pollLast();
                    LOG.info("KnockingOut Ask-Order: " + order_Polled);
                }

                LOG.info("Offering best Ask-Order: "+order);
                asks.offerFirst(order);
            } else {
                if (asks.size() < 10) {
                    LOG.info("filling Ask-Order: "+order);
                    asks.offerLast(order);
                }
            }

            Collections.sort(asks, OrderComparator.DescAskComparer());

        }else{
            LOG.info("Offering best Ask-Order: "+order);
            asks.offerLast(order);
        }
    }
}