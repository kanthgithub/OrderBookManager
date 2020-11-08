package orderbook.utils;

import com.google.gson.Gson;
import orderbook.model.CoinBaseOrderEvent;
import orderbook.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;

import static orderbook.utils.DateUtility.parseLocalDateTime;
import static orderbook.utils.MathUtility.parseDouble;

public class OrderEventParser {
    static Logger LOG = LoggerFactory.getLogger(OrderEventParser.class);

    public static Order parseOrderEvent(String orderEventString){
         Gson gson = new Gson();
        CoinBaseOrderEvent coinBaseOrderEvent =
                gson.fromJson(orderEventString, CoinBaseOrderEvent.class);


        if(coinBaseOrderEvent == null){
            LOG.error("failed to parse the coinbase event: ",orderEventString);
            return  null;
        }

        LOG.info("parsed coinBaseOrderEvent: ",coinBaseOrderEvent);
        Order order = Order.builder().price(parseDouble(coinBaseOrderEvent.getPrice()))
                .openPrice(parseDouble(coinBaseOrderEvent.getOpen_24h()))
                .bestBid(parseDouble(coinBaseOrderEvent.getBest_bid()))
                .bestAsk(parseDouble(coinBaseOrderEvent.getBest_ask()))
                .ticker(coinBaseOrderEvent.getProduct_id())
                .side(coinBaseOrderEvent.getSide())
                .sequence(coinBaseOrderEvent.getSequence())
                .time(parseLocalDateTime(coinBaseOrderEvent.getTime()).toEpochSecond(ZoneOffset.UTC))
                .tradeId(coinBaseOrderEvent.getTrade_id())
                .lastSize(parseDouble(coinBaseOrderEvent.getLast_size()))
                .build();
        LOG.info("order parsed: "+order.toString());
        return order;
    }
}
