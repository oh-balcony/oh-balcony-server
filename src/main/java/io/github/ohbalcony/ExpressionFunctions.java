package io.github.ohbalcony;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;

/**
 * Functions which are callable within expressions.
 */
public class ExpressionFunctions {

    public static final DateTimeFormatter TIME_PARSER;

    static {
        TIME_PARSER = new DateTimeFormatterBuilder()
                .appendValue(ChronoField.HOUR_OF_DAY, 1, 2, SignStyle.NOT_NEGATIVE)
                .appendLiteral(':')
                .appendValue(ChronoField.MINUTE_OF_HOUR, 1, 2, SignStyle.NOT_NEGATIVE)
                .optionalStart()
                .appendLiteral(':')
                .appendValue(ChronoField.SECOND_OF_MINUTE, 1, 2, SignStyle.NOT_NEGATIVE)
                .toFormatter();
    }

    public static boolean timeBetween(String from, String to) {
        LocalDateTime now = LocalDateTime.now();
        LocalTime nowTime = now.toLocalTime();

        LocalTime fromTime = LocalTime.parse(from, TIME_PARSER);
        LocalTime toTime = LocalTime.parse(to, TIME_PARSER);

        return nowTime.isAfter(fromTime) && nowTime.isBefore(toTime);
    }
}
