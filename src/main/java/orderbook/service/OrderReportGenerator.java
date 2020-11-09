package orderbook.service;

import orderbook.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderReportGenerator {

    static Logger LOG = LoggerFactory.getLogger(OrderReportGenerator.class);

    @Autowired
    OrderBook orderBook;

  public String generateOrderBookReport(){

      List<Order> bidOrders = orderBook.getBids();
      List<Order> askOrders = orderBook.getAsks();

      StringBuffer orderBookReport = new StringBuffer();

      orderBookReport.append("\n -------------------------------------------------------\n");
      orderBookReport.append("\t\t\tBid-Orders:\t\t\t\n");
      orderBookReport.append("\n -------------------------------------------------------\n");

      bidOrders.stream().forEach(order ->orderBookReport.append((order.toString()+"\n")));
      orderBookReport.append("\n -------------------------------------------------------\n");


      orderBookReport.append("\n -------------------------------------------------------\n");
      orderBookReport.append("\t\t\tAsk-Orders:\t\t\t\n");
      orderBookReport.append("\n -------------------------------------------------------\n");
      askOrders.stream().forEach(order ->orderBookReport.append((order.toString()+"\n")));
      orderBookReport.append("\n -------------------------------------------------------\n");

      return  orderBookReport.toString();
  }

}
