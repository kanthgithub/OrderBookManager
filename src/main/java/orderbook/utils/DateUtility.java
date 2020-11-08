package orderbook.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateUtility {

    public static final DateTimeFormatter orderTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    public static final ZoneOffset ZONE_OFFSET = ZoneOffset.ofHours(8);

    public static LocalDateTime parseLocalDateTime(String localDateTimeString) {
        //2020-11-08T07:24:26.014269Z
        LocalDateTime dateTime = LocalDateTime.parse(localDateTimeString.replace("Z",""));
        return dateTime;
    }

    public static Long getCurrentTimeStampInEpochMillis(){

        Instant instant = Instant.now().atOffset(ZONE_OFFSET).toInstant();
        return instant.toEpochMilli();
    }

    public static Long getTimeStampInEpochMillis(LocalDateTime localDateTime){

        Instant instant = localDateTime.toInstant(ZONE_OFFSET);
        return instant.toEpochMilli();
    }

    public static LocalDateTime getCurrentTimeStamp(){

        return LocalDateTime.now().atOffset(ZONE_OFFSET).toLocalDateTime();
    }

    public static String getDateAsFormattedString(String format,LocalDateTime date){

        LocalDateTime dateArgument = date==null ? getCurrentTimeStamp() : date;

        String formatArgument = format!=null ? format : "yyyyMMdd";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formatArgument);

        return dtf.format(dateArgument);
    }
}