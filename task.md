The goal of the exercise is to receive market data from an exchange,
build an order book of 10 levels, and print the order book on each
"tick".

For the purposes of this exercise, we only care about order book
updates. The API in question is the Coinbase Pro API. You will want to
use the websocket feed, and then connect to it, subscribe to the data
(hint: use the level-2 data).
 
Once you have the data coming in, you'll want to process it, then build
and maintain your order book of 10 levels (of bids and asks), and on
each update, print out to the screen the bids and asks.
 
The instrument to subscribe will be passed as a command line argument
to the application, Ctrl+C will terminate the application.
