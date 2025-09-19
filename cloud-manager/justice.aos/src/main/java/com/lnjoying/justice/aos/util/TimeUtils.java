package com.lnjoying.justice.aos.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * time utils
 *
 * @author merak
 **/

public class TimeUtils
{

    private static final String DATE_FORMATTER= "yyyy-MM-dd HH:mm:ss";

    private static final  ZoneId zoneId = ZoneId.of("Asia/Shanghai");

    /**
     * convert epoch milliseconds to LocalDateTime
     *
     * @param millis
     * @return
     */
    public static LocalDateTime millsToLocalDateTime(long millis)
    {
        Instant instant = Instant.ofEpochMilli(millis);
        LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return date;
    }

    public static Instant localDateTime2Instant(LocalDateTime localDateTime)
    {
        return localDateTime.atZone(zoneId).toInstant();
    }

    public static LocalDateTime Instant2LocalDateTime(Instant instant)
    {
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    public static LocalDateTime parseFromString(String formattedDateTime)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        return LocalDateTime.parse(formattedDateTime, formatter);
    }

    public static String parseFromLocalDateTime(LocalDateTime localDateTime)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        return localDateTime.format(formatter);
    }

    /**
     * ture means now > localDateTime
     * @param localDateTime
     * @return
     */
    public static boolean compareNow(LocalDateTime localDateTime)
    {

        LocalDateTime now = LocalDateTime.now(zoneId);
        return now.isAfter(localDateTime);
    }

    public static String now()
    {
        return parseFromLocalDateTime(LocalDateTime.now(zoneId));
    }
}
