package co.uk.fi.util;

import co.uk.fi.domain.Price;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.math.BigDecimal.*;
import static java.time.LocalDateTime.*;

public class TestDataUtil {
    public static Price createPrice() {
        return Price.builder().tickId("1TestInsId").instrumentName("Test Instrument").bid(valueOf(111)).ask(valueOf(112)).timestamp(now()).build();
    }

    public static Price createPrice(int offset) {
        return Price.builder().tickId("1TestInsId").instrumentName("Test Instrument").bid(valueOf(111 + offset)).ask(valueOf(112 + offset)).timestamp(now().minusMinutes(-1*offset)).build();
    }

    public static final String VALID_PRICE_SINGLE_RECORD = "110, EUR/JPY, 119.41,119.81,01-06-2020 12:01:02:110";

    public static String VALID_PRICE_MULTI_RECORDS = "118, EUR/JPY, 119.55,119.81,01-06-2020 12:01:02:110\n" +
            "110, EUR/JPY, 119.56,119.84,01-06-2020 12:01:03:110\n" +
            "111, EUR/JPY, 119.57,119.83,01-06-2020 12:01:04:110\n" +
            "115, EUR/JPY, 119.59,119.84,01-06-2020 12:01:05:110\n" +
            "113, EUR/JPY, 119.60,119.89,01-06-2020 12:01:06:110\n" +
            "112, EUR/JPY, 119.59,119.88,01-06-2020 12:01:07:110\n" +
            "119, EUR/JPY, 119.61,119.91,01-06-2020 12:01:10:110\n";

    public static String INVALID_PRICE_MULTI_RECORDS = "118, EUR/JPY, 119.55,119.81,01-06-2020 12:01:02:110\n" +
            "110, EUR/JPY, 119.56,119.84,01-06-2020 12:01:03:110\n" +
            "111, EUR/JPY, 119.57,119.83,01-06-2020 12:01:04:110\n" +
            "115, EUR/JPY, 119.59,119.84,01-06-2020 12:01:05:110\n" +
            "120, EUR/JPY,,,01-06-2020 12:01:05:110\n" +
            "113, EUR/JPY, 119.60,119.89,01-06-2020 12:01:06:110\n" +
            "112, EUR/JPY, 119.59,119.88,01-06-2020 12:01:07:110\n" +
            "119, EUR/JPY, 119.61,119.91,01-06-2020 12:01:10:110\n" +
            "121, EUR/JPY, 119.61,119.91,01-06-2020 12:01:10:112\n";

    public static String VALID_PRICES_MULTI_RECORDS = "0, EUR/JPY, 119.54,119.81,01-06-2020 12:01:02:110\n" +
            "1, EUR/JPY, 119.56,119.84,01-06-2020 12:01:03:110\n" +
            "11, EUR/GBP, 119.46,119.74,01-06-2020 12:01:03:110\n" +
            "2, EUR/JPY, 119.57,119.83,01-06-2020 12:01:03:113\n" +
            "12, EUR/GBP, 119.47,119.73,01-06-2020 12:01:04:110\n" +
            "3, EUR/JPY, 119.59,119.84,01-06-2020 12:01:05:114\n" +
            "13, EUR/GBP, 119.49,119.74,01-06-2020 12:01:05:110\n" +
            "4, EUR/JPY, 119.60,119.89,01-06-2020 12:01:06:115\n" +
            "14, EUR/GBP, 119.50,119.79,01-06-2020 12:01:06:110\n" +
            "5, EUR/JPY, 119.55,119.85,01-06-2020 12:01:07:110\n" +
            "15, EUR/GBP, 119.39,119.68,01-06-2020 12:01:07:110\n" +
            "6, EUR/JPY, 119.64,119.94,01-06-2020 12:01:10:110\n";
}
