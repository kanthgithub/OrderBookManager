package orderbook;

import orderbook.websocket.CoinbaseWebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Scanner;


@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);

    @Autowired
    orderbook.websocket.CoinbaseWebSocketService coinbaseWebSocketService;

    @Autowired
    public CommandLineAppStartupRunner(CoinbaseWebSocketService _coinbaseWebSocketService) {
        this.coinbaseWebSocketService = _coinbaseWebSocketService;
    }

    @Override
    public void run(String... args) {

        logger.info(
                "\n\n Please enter the ticker and press ENTER to start building OrderBook. \n\n To kill this application, press Ctrl + C. \n\n  ticker: ",
                Arrays.toString(args));

        Scanner scanner = new Scanner(System.in);

        String ticker = scanner.next();

        try {
            coinbaseWebSocketService.startOrderBook(ticker);
        } catch (Exception e) {
            e.printStackTrace();
        }

        scanner.close();

        //System.exit(0);
    }
}