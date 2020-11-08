package orderbook.utils;

import org.springframework.util.StringUtils;

public class MathUtility {

    public static Double parseDouble(String doubleString){
        return !StringUtils.isEmpty(doubleString) ? Double.valueOf(doubleString) : 0;
    }

    public static Long parseLong(String longString){
        return !StringUtils.isEmpty(longString) ? Long.valueOf(longString) : 0;
    }
}
