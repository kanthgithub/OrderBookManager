# OrderBook Builder and Report-Generator


## Tech-Stack:
1. Algorithm is implemented in jdk-8 and runs on jre-8
3. Maven project
4. Spring-Boot for Dependency Injection and Application Run


## Pre-Requisites to build and run the application:

### Maven based package and start:

1.  Essentials:  maven 3.3.9 + and jdk-8, jre-8


## How to Run application from command-line:

- Accept ticker from the commandLine Argument

 ```shell
  ./orderbook_manager_console.sh

 ``` 


## How to Run application from IntelliJ:



## get Order Data from L2 Data of Coinbase:

 
### OrderBook Command execution Flow: 
 
- If you have issues to run command on console, make sure you have set
  Maven and Java Home in your environment variables
  
- As a workaround, you can run the mvn clean install & mvn
  spring-boot:run from your intelliJ Run configurations

- Connect to Coinbase-pro websocket feed 

- Generate Subscription Message for L2 Data of Coinbase

- Subscribe to the L2 data

```java
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
```

