package orderbook.websocket;

import orderbook.service.OrderBook;
import orderbook.service.OrderReportGenerator;
import orderbook.utils.OrderEventParser;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@org.eclipse.jetty.websocket.api.annotations.WebSocket
public class CoinbaseWebSocketServiceImpl implements CoinbaseWebSocketService{

    static Logger LOG = LoggerFactory.getLogger(CoinbaseWebSocketServiceImpl.class);

    @Autowired
    private OrderBook orderBook;

    @Autowired
    private OrderReportGenerator orderReportGenerator;

    String COINBASE_SUBSCRIBE_MESSAGE;

    {
        try {
            URL jsonResource = getClass().getClassLoader().getResource("l2subscribe.json");
            Path filePath = Paths.get(jsonResource.toURI());
            COINBASE_SUBSCRIBE_MESSAGE = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            LOG.info("JSON extracted: ",COINBASE_SUBSCRIBE_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void startOrderBook(String ticker) throws Exception {
        LOG.info("starting Building Order-Book for Ticker: "+ticker);
        COINBASE_SUBSCRIBE_MESSAGE = COINBASE_SUBSCRIBE_MESSAGE.replace("tickerReplaceableByCommandLineArgument",ticker);
        LOG.info("COINBASE_SUBSCRIBE_MESSAGE after commandline: "+COINBASE_SUBSCRIBE_MESSAGE);
        initWebSocketCommunication();
    }

    private  void initWebSocketCommunication()  throws Exception{

        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");

        String destUri = "wss://ws-feed.pro.coinbase.com";

        WebSocketClient client = new WebSocketClient(new SslContextFactory());
        try {
            LOG.info("connecting to coinbase-feed");
            client.start();
            URI echoUri = new URI(destUri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.setMaxBinaryMessageBufferSize(169406);
            client.setMaxTextMessageBufferSize(169406);
            client.connect(this, echoUri, request);
            LOG.info("done connecting");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Handler to cleanup on close.
     * @param statusCode Status code of closing
     * @param reason Reason the socket is being closed
     */
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        LOG.info("Connection closed: {} - %s{}", statusCode, reason);
        try {
            this.start();
        } catch (IOException e) {
            LOG.error("Failure to restart web socket {}", e.getMessage());
        }
    }


    public boolean start() throws IOException {
        String destUri = "wss://ws-feed.exchange.coinbase.com";
        WebSocketClient client = new WebSocketClient(new SslContextFactory());
        try {
            LOG.info("connecting to coinbsae feed");
            client.start();
            URI echoUri = new URI(destUri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(this, echoUri, request);
            LOG.info("done connecting");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return true;
    }



    /**
     * Upon connection to the Coinbase Websocket market feed, we send our
     * subscription message to indicate the product we want to subscribe to.
     * @param session Websocket session
     */
    @OnWebSocketConnect
    public void onConnect(Session session) {
        LOG.info("Got connect: %s%n", session);
        try {
            Future<Void> fut;
            fut = session.getRemote().sendStringByFuture(COINBASE_SUBSCRIBE_MESSAGE);
            fut.get(2, TimeUnit.SECONDS);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Here we receive a Coinbase message and push it onto our orderBook.
     * @param msg The coinbase market data feed
     *     https://docs.exchange.coinbase.com/#websocket-feed
     */
    @OnWebSocketMessage
    public void onMessage(String msg) {
        if(!msg.contains("error") && !msg.contains("subscriptions")){
            orderBook.addOrder(OrderEventParser.parseOrderEvent(msg));
            String orderBookReport = orderReportGenerator.generateOrderBookReport();
            LOG.info("\n -------------------------------------------------------\n");
            LOG.info("\n\n OrderBookReport: \n\n"+orderBookReport);
            LOG.info("\n -------------------------------------------------------\n");
        }else{
            LOG.error("Failed to receive a valid Order message from Coinbase: ",msg);
        }

    }
}
