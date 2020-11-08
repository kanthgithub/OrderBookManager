package orderbook.websocket;

import orderbook.service.OrderBook;
import orderbook.utils.OrderEventParser;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
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

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@org.eclipse.jetty.websocket.api.annotations.WebSocket
public class CoinbaseWebSocketServiceImpl implements CoinbaseWebSocketService{

    static Logger LOG = LoggerFactory.getLogger(CoinbaseWebSocketServiceImpl.class);

    @Autowired
    private OrderBook orderBook;

    /**
     * Send this object as soon as we connect to the Coinbase Market API
     * to signify we want to subscribe to the BTC-USD feed.
     */

    static String payload_old = "{\"type\":\"subscribe\", " +
                    "\"channels\": [\"level2\",{ " +
                    "\"name\":\"ticker\",\"product_ids\":[\"BTC-USD\"]}]} ";

    static String payload = "{\"type\": \"subscribe\",\"channels\":[{\"name\": \"ticker\",\"product_ids\": [\"BTC-USD\"]}]}";



    private static String COINBASE_SUBSCRIBE_MESSAGE = payload;

    @PostConstruct
    public void init() throws Exception{
        initWebSocketCommunication();
    }

    private  void initWebSocketCommunication()  throws Exception{

        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");

        SSLContext sslContext = SSLContext.getDefault();

        SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1.2"}, // important
                null,
                NoopHostnameVerifier.INSTANCE);

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslConnectionFactory)
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .build();

        HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);
        HttpClient httpclient = HttpClientBuilder.create().setSSLSocketFactory(sslConnectionFactory)
                .setConnectionManager(ccm)
                .build();

        String destUri = "wss://ws-feed.pro.coinbase.com";


        //The websocket feed provides real-time market data updates for orders and trades.
        //String destUri = "wss://ws-feed.gdax.com";

        //To begin receiving feed messages,
        // you must first send a subscribe message to the server indicating
        // which channels and products to receive.
        // This message is mandatory — you will be disconnected if no subscribe has been received within 5 seconds.

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

//    var websocket = new Gdax.WebsocketClient(
//            ['BTC-USD'],
//    'wss://ws-feed.gdax.com',
//    {
//        key: API_KEY,
//                secret: API_SECRET,
//            passphrase: API_PASSPHRASE,
//    },
//    { heartbeat: true }
//)
//        webSocket.on('message', data => {
//        console.log(data);
//    });




    /**
     * Here we recive a Coinbase message and push it onto our queue.
     * @param msg The coinbase market data feed
     *     https://docs.exchange.coinbase.com/#websocket-feed
     */
    @OnWebSocketMessage
    public void onMessage(String msg) {
        LOG.debug("got coinbase msg {}", msg);
        System.out.println("got coinbase msg: "+msg);

        if(!msg.contains("error")){
            orderBook.addOrder(OrderEventParser.parseOrderEvent(msg));
        }else{
            LOG.error("Failed to receive a valid Order message from Coinbase: ",msg);
        }

    }
}
