package orderbook.utils;

import java.time.LocalDateTime;

public class DateUtility {

    public static LocalDateTime parseLocalDateTime(String localDateTimeString) {
        //2020-11-08T07:24:26.014269Z
        LocalDateTime dateTime = LocalDateTime.parse(localDateTimeString.replace("Z",""));
        return dateTime;
    }

}