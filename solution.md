https://github.com/irufus/gdax-java/blob/master/websocketfeed/src/main/java/com/coinbase/exchange/websocketfeed/OpenedOrderBookMessage.java


 A valid order has been received and is now active. This message is
 * emitted for every single valid order as soon as the matching engine
 * receives it whether it fills immediately or not.
 *
 * The received message does not indicate a resting order on the order book.
 * It simply indicates a new incoming order which as been accepted by the
 * matching engine for processing. Received orders may cause match message
 * to follow if they are able to begin being filled (taker behavior).
 * Self-trade prevention may also trigger change messages to follow if the
 * order size needs to be adjusted. Orders which are not fully filled or
 * canceled due to self-trade prevention result in an open message and
 * become resting orders on the order book.
 *
 * Market orders (indicated by the order_type field) may have an optional
 * funds field which indicates how much quote currency will be used to buy
 * or sell. For example, a funds field of 100.00 for the BTC-USD product
 * would indicate a purchase of up to 100.00 USD worth of bitcoin.