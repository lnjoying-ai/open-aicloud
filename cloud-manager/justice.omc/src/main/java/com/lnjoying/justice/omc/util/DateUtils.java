package com.lnjoying.justice.omc.util;

import com.micro.core.common.Pair;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/26 19:09
 */

public class DateUtils
{

    public static final String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd";

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);
        return sdf.format(date);
    }

    public static Date stringToDate(String dateString)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);
        try {
            Date date = sdf.parse(dateString);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getStartOfDay() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getStartOfDayWithOffset(int daysOffset) {
        LocalDate today = LocalDate.now().plusDays(daysOffset);
        LocalDateTime startOfDay = today.atStartOfDay();
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }


    public static Date getStartOfHourWithOffset(int hoursOffset) {
        LocalDateTime today = LocalDateTime.now().plusHours(hoursOffset);
        LocalDateTime startOfHour = today.withMinute(0).withSecond(0).withNano(0);
        return Date.from(startOfHour.atZone(ZoneId.systemDefault()).toInstant());
    }


    public static Date of(LocalDateTime date) {
        ZonedDateTime zonedDateTime = date.atZone(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }

    public static Date of(LocalDate localDate)
    {
        if (Objects.isNull(localDate))
        {
            return null;
        }
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }

    public static LocalDateTime of(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH_DAY);
        return localDateTime.format(formatter);
    }


    public static Pair<Date, Date> getDate(String startDate, String endDate, int startDayOffset, int endDayOffset)
    {
        Date startLocalDate = null;
        Date endLocalDate = null;


        if (StringUtils.isBlank(endDate))
        {
            endLocalDate = getStartOfDayWithOffset(endDayOffset);
        }
        else
        {
            endLocalDate = DateUtils.stringToDate(endDate);
        }

        if (StringUtils.isBlank(startDate))
        {
            startLocalDate = getStartOfDayWithOffset(startDayOffset);

        }
        else
        {
            startLocalDate = DateUtils.stringToDate(startDate);
        }

        return new Pair<Date, Date>(startLocalDate, endLocalDate);
    }

    public static Pair<Date, Date> getHour(String startDate, String endDate, int startHourOffset, int endHourOffset)
    {
        Date startLocalDate = null;
        Date endLocalDate = null;


        if (StringUtils.isBlank(endDate))
        {
            endLocalDate = getStartOfHourWithOffset(endHourOffset);
        }
        else
        {
            endLocalDate = DateUtils.stringToDate(endDate);
        }

        if (StringUtils.isBlank(startDate))
        {
            startLocalDate = getStartOfHourWithOffset(startHourOffset);

        }
        else
        {
            startLocalDate = DateUtils.stringToDate(startDate);
        }

        return new Pair<Date, Date>(startLocalDate, endLocalDate);
    }

    public static Date getXxMonthAgoDate(int monthAgo)
    {
        ZonedDateTime nowInSystemZone = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime xxMonthAgoDateTime = nowInSystemZone.minus(monthAgo, ChronoUnit.MONTHS);
        return Date.from(xxMonthAgoDateTime.toInstant());
    }

    public static boolean isInAlarmPeriod(Date start, Date end) {
        LocalTime now = LocalTime.now();
        LocalTime startTime = LocalTime.MIN;
        LocalTime endTime = LocalTime.MAX;
        if (Objects.nonNull(start))
        {
            startTime = com.micro.core.util.DateUtils.of(start).toLocalTime();
        }

        if (Objects.nonNull(end))
        {
            endTime = com.micro.core.util.DateUtils.of(end).toLocalTime();
        }

        if (endTime.equals(startTime)) {
            return true;
        } else if (endTime.isBefore(startTime)) {
            return now.isAfter(startTime) || now.isBefore(endTime);
        } else {
            return now.isAfter(startTime) && now.isBefore(endTime);
        }
    }
}
