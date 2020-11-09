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

![spring-boot-run](./images/spring-boot-run.png)


## get Order Data from L2 Data of Coinbase:

 
### OrderBook Command execution Flow: 
 
- If you have issues to run command on console, make sure you have set
  Maven and Java Home in your environment variables
  
- As a workaround, you can run the mvn clean install & mvn
  spring-boot:run from your intelliJ Run configurations

- Connect to Coinbase-pro websocket feed 

- Generate Subscription Message for L2 Data of Coinbase

  ```json
   {
     "type": "subscribe",
     "channels": [
         "level2",
         {
             "name": "ticker",
             "product_ids": [
                 "BTC-USD"
               ]
           }
       ]
   }
   ```

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

## Command Line Arguments:

-  Once the Application is started, Application prompts for the
   ticker/pair for which order book has to be updated
   
- the console log looks like:

```shell
2020-11-09 16:26:51.861  INFO 59451 --- [  restartedMain] orderbook.CommandLineAppStartupRunner    : 

 Please enter the ticker and press ENTER to start building OrderBook. 

 To kill this application, press Ctrl + C. 

  ticker:  BTC-USD
```

- The Order Book start getting built immediately after you enter the
  ticker.
  
```shell
2020-11-09 16:27:00.289  INFO 59451 --- [  restartedMain] o.w.CoinbaseWebSocketServiceImpl         : starting Building Order-Book for Ticker: BTC-USD
2020-11-09 16:27:00.289  INFO 59451 --- [  restartedMain] o.w.CoinbaseWebSocketServiceImpl         : COINBASE_SUBSCRIBE_MESSAGE after commandline:  {
     "type": "subscribe",
     "channels": [
         "level2",
         {
             "name": "ticker",
             "product_ids": [
                 "BTC-USD"
               ]
           }
       ]
 }
2020-11-09 16:27:00.381  INFO 59451 --- [  restartedMain] o.w.CoinbaseWebSocketServiceImpl         : connecting to coinbase-feed
2020-11-09 16:27:00.811  INFO 59451 --- [  restartedMain] o.w.CoinbaseWebSocketServiceImpl         : done connecting
2020-11-09 16:27:02.033  INFO 59451 --- [t@1225541703-43] o.w.CoinbaseWebSocketServiceImpl         : Got connect: %s%n
2020-11-09 16:27:02.313 ERROR 59451 --- [t@1225541703-41] o.w.CoinbaseWebSocketServiceImpl         : Failed to receive a valid Order message from Coinbase: 
2020-11-09 16:27:02.313 ERROR 59451 --- [t@1225541703-41] o.w.CoinbaseWebSocketServiceImpl         : Failed to receive a valid Order message from Coinbase: 
2020-11-09 16:27:02.334  INFO 59451 --- [t@1225541703-41] orderbook.service.OrderBookImpl          : Offering best Bid-Order: 
| Order	time= 1604910420	tradeId = 108425477	ticker = BTC-USD	side = buy	price = 15238.81	quantity = 0.00157017
2020-11-09 16:27:02.335  INFO 59451 --- [t@1225541703-41] o.w.CoinbaseWebSocketServiceImpl         : 
 -------------------------------------------------------

2020-11-09 16:27:02.335  INFO 59451 --- [t@1225541703-41] o.w.CoinbaseWebSocketServiceImpl         : 

 OrderBookReport: 


 -------------------------------------------------------
			Bid-Orders:			

 -------------------------------------------------------

| Order	time= 1604910420	tradeId = 108425477	ticker = BTC-USD	side = buy	price = 15238.81	quantity = 0.00157017

 -------------------------------------------------------

 -------------------------------------------------------
			Ask-Orders:		
			
-------------------------------------------------------


 -------------------------------------------------------
			Bid-Orders:			

 -------------------------------------------------------

| Order	time= 1604910420	tradeId = 108425477	ticker = BTC-USD	side = buy	price = 15238.81	quantity = 0.00157017

| Order	time= 1604910424	tradeId = 108425479	ticker = BTC-USD	side = buy	price = 15238.81	quantity = 0.0128553

| Order	time= 1604910432	tradeId = 108425484	ticker = BTC-USD	side = buy	price = 15233.18	quantity = 0.23262896

| Order	time= 1604910432	tradeId = 108425483	ticker = BTC-USD	side = buy	price = 15233.17	quantity = 0.10798604

 -------------------------------------------------------

 -------------------------------------------------------
			Ask-Orders:			

 -------------------------------------------------------

| Order	time= 1604910431	tradeId = 108425482	ticker = BTC-USD	side = sell	price = 15233.81	quantity = 0.46943409

| Order	time= 1604910424	tradeId = 108425478	ticker = BTC-USD	side = sell	price = 15238.8	quantity = 0.00201795

| Order	time= 1604910431	tradeId = 108425480	ticker = BTC-USD	side = sell	price = 15238.8	quantity = 0.07755589

| Order	time= 1604910431	tradeId = 108425481	ticker = BTC-USD	side = sell	price = 15238.8	quantity = 0.00794267

 -------------------------------------------------------
			
				
```