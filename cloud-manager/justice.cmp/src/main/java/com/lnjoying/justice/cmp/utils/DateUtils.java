package com.lnjoying.justice.cmp.utils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/6/25 20:59
 */

public class DateUtils
{

    public static final String TIME_ZONE_DEFAULT = "GMT+8";


    public static final long SECOND_MILLIS = 1000;

    public static final String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_YEAR_MONTH = "yyyy-MM";

    public static final String FORMAT_HOUR_MINUTE_SECOND = "HH:mm:ss";


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

    public static Date firstDayOfMonth()
    {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        return  of(firstDayOfMonth);
    }

    public static Date firstDayOfMonth(Date date)
    {
        LocalDateTime currentDateTime = of(date);
        LocalDate firstDayOfMonth = currentDateTime.toLocalDate().withDayOfMonth(1);
        return  of(firstDayOfMonth);
    }


    public static Date getPreHourStartTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return of(currentDateTime.minusHours(1).withMinute(0).withSecond(0).withNano(0));
    }

    public static Date getStartOfHour(Date date) {
        // 将 Date 转换为 LocalDateTime
        LocalDateTime dateTime = of(date);
        LocalDateTime startHour = dateTime.withMinute(0).withSecond(0).withNano(0);
        return of(startHour);
    }

    public static Date getHourStartTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return of(currentDateTime.withMinute(0).withSecond(0).withNano(0));
    }

    public static Date getNextHourStartTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return of(currentDateTime.plusHours(1).withMinute(0).withSecond(0).withNano(0));
    }

    public static Date getNextStartOfHour(Date date) {
        // 将 Date 转换为 LocalDateTime
        LocalDateTime dateTime = of(date);
        LocalDateTime startHour = dateTime.plusHours(1).withMinute(0).withSecond(0).withNano(0);
        return of(startHour);
    }

    public static Date getNextStartOfHour(LocalDateTime dateTime) {
        // 将 Date 转换为 LocalDateTime
        LocalDateTime startHour = dateTime.plusHours(1).withMinute(0).withSecond(0).withNano(0);
        return of(startHour);
    }

    public static LocalDateTime getNextStartOfHourTime(LocalDateTime dateTime) {
        // 将 Date 转换为 LocalDateTime
        LocalDateTime startHour = dateTime.plusHours(1).withMinute(0).withSecond(0).withNano(0);
        return startHour;
    }

    public static Date getPreDayStartTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return of(currentDateTime.with(LocalTime.MIN).minusDays(1));
    }

    public static Date getDayStartTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return of(currentDateTime.with(LocalTime.MIN));
    }

    public static LocalDate getLocalDayStartTime() {
        LocalDate currentDateTime = LocalDate.now();
        return currentDateTime;
    }

    public static Date getNextDayStartTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return of(currentDateTime.with(LocalTime.MIN).plusDays(1));
    }

    public static Date getDayStartTime(Date givenDate) {
        LocalDateTime currentDateTime = of(givenDate);
        return of(currentDateTime.with(LocalTime.MIN));
    }

    public static Date getNextDayStartTime(Date givenDate) {
        LocalDateTime currentDateTime = of(givenDate);
        return of(currentDateTime.with(LocalTime.MIN).plusDays(1));
    }

    public static Date getPreMonthStartTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return of(currentDateTime.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
    }

    public static Date getMonthStartTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return of(currentDateTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
    }

    public static LocalDate getLocalMonthStartTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0).toLocalDate();
    }

    public static Date getMonthStartTime(Date givenDate) {
        LocalDateTime currentDateTime = of(givenDate);
        return of(currentDateTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
    }

    public static Date getNextMonthStartTime(Date givenDate) {
        LocalDateTime currentDateTime = of(givenDate);
        return of(currentDateTime.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
    }

    public static Date getNextMonthStartTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return of(currentDateTime.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
    }

    public static Date getHourEndTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return of(currentDateTime.withMinute(59).withSecond(59).withNano(999999999));
    }

    public static Date getLastMonthDay(int dayOfMonth) {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastMonth = currentDate.minusMonths(1);
        LocalDate lastMonthDay = lastMonth.withDayOfMonth(dayOfMonth);
        return of(lastMonthDay);
    }

    public static int daysOfMonth()
    {
        YearMonth currentYearMonth = YearMonth.now();
        return currentYearMonth.lengthOfMonth();
    }

    public static int daysOfYear()
    {
        Year currentYear = Year.now();
        return currentYear.length();
    }

    public static boolean isWithinSameHour(Date date) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate nowDate = now.toLocalDate();
        LocalDateTime dateTime = of(date);

        return nowDate.equals(dateTime.toLocalDate()) && now.getHour() == dateTime.getHour();
    }

    /**
     * date1 > date2
     * @param date1
     * @param date2
     * @return
     */
    public static double hoursBetweenDates(Date date1, Date date2) {
        Instant instant1 = date1.toInstant();
        Instant instant2 = date2.toInstant();

        // 计算两个 Instant 之间的小时差，包含小数部分
        double hours = ChronoUnit.SECONDS.between(instant1, instant2) / (60.0 * 60.0);

        return hours;
    }

    public static boolean isYesterday(Date date)
    {
        LocalDate localDate = of(date).toLocalDate();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return localDate.equals(yesterday);
    }

    public static boolean afterYesterday(Date date)
    {
        LocalDate givenDate = of(date).toLocalDate();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return givenDate.isAfter(yesterday);
    }

    public static boolean isOrAfterYesterday(Date date)
    {
        return isYesterday(date) || afterYesterday(date);
    }

    public static boolean isLastMonth(Date date)
    {
        LocalDate localDate = of(date).toLocalDate();
        LocalDate yesterday = LocalDate.now().minusMonths(1);
        return localDate.equals(yesterday);
    }

    public static boolean afterLastMonth(Date date)
    {
        LocalDate givenDate = of(date).toLocalDate();
        LocalDate yesterday = LocalDate.now().minusMonths(1);
        return givenDate.isAfter(yesterday);
    }

    public static boolean isOrAfterLastMonth(Date date)
    {
        return isLastMonth(date) || afterLastMonth(date);
    }

    public static LocalDate monthsDiff(LocalDate startDate, int monthDiff)
    {
        LocalDate currentDate = LocalDate.now();
        long monthsDiff = ChronoUnit.MONTHS.between(startDate, currentDate);
        if (monthsDiff >= monthDiff)
        {
            return currentDate.minusMonths(3).withDayOfMonth(1);
        }
        else
        {
            return startDate;
        }
    }



    public static boolean isCurrentMonth(Date givenDate)
    {
        LocalDateTime givenDateTime = of(givenDate);

        return isCurrentMonth(givenDateTime);
    }


    public static boolean isCurrentDay(Date givenDate)
    {
        LocalDateTime givenDateTime = of(givenDate);

        return isCurrentDay(givenDateTime);
    }

    public static boolean isCurrentDay(LocalDateTime givenDateTime)
    {
        LocalDate currentDate = LocalDate.now();
        boolean isCurrentDay = givenDateTime.getMonth() == currentDate.getMonth()
                && givenDateTime.getYear() == currentDate.getYear()
                && givenDateTime.getDayOfMonth() == currentDate.getDayOfMonth();
        return isCurrentDay;
    }

    public static boolean isCurrentMonth(LocalDateTime givenDateTime)
    {
        LocalDate currentDate = LocalDate.now();
        boolean isCurrentMonth = givenDateTime.getMonth() == currentDate.getMonth()
                && givenDateTime.getYear() == currentDate.getYear();
        return isCurrentMonth;
    }

    /**
     * 将 Date 转换成 LocalDateTime
     *
     * @param date Date
     * @return LocalDateTime
     */
    public static LocalDateTime of(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    @Deprecated
    public static Date addTime(Duration duration) {
        return new Date(System.currentTimeMillis() + duration.toMillis());
    }

    public static boolean isExpired(Date time) {
        return System.currentTimeMillis() > time.getTime();
    }

    public static boolean isExpired(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(time);
    }

    public static long diff(Date endTime, Date startTime) {
        return endTime.getTime() - startTime.getTime();
    }


    public static Date buildTime(int year, int mouth, int day) {
        return buildTime(year, mouth, day, 0, 0, 0);
    }


    public static Date buildTime(int year, int mouth, int day,
                                 int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, mouth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date max(Date a, Date b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return a.compareTo(b) > 0 ? a : b;
    }

    public static LocalDateTime max(LocalDateTime a, LocalDateTime b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return a.isAfter(b) ? a : b;
    }


    public static Date addDate(int field, int amount) {
        return addDate(null, field, amount);
    }


    public static Date addDate(Date date, int field, int amount) {
        if (amount == 0) {
            return date;
        }
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        c.add(field, amount);
        return c.getTime();
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);
        return sdf.format(date);
    }

    public static Date systemStartDate()
    {
        long epochTimeInSeconds = 0L;

        Date systemStartTime = new Date(epochTimeInSeconds * 1000);
        return systemStartTime;
    }

    public static LocalDate getStartOfPreviousMonths(int numberOfMonths) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        YearMonth previousYearMonth = YearMonth.from(currentDateTime.minusMonths(numberOfMonths));
        LocalDateTime startOfPreviousMonth = previousYearMonth.atDay(1).atStartOfDay();

        return startOfPreviousMonth.toLocalDate();
    }

    public static LocalDate getStartOfPreviousMonths(int numberOfMonths, LocalDate localDate) {
        YearMonth previousYearMonth = YearMonth.from(localDate.minusMonths(numberOfMonths));
        LocalDateTime startOfPreviousMonth = previousYearMonth.atDay(1).atStartOfDay();

        return startOfPreviousMonth.toLocalDate();
    }

    public static boolean isSameMonth(Date date, YearMonth yearMonth) {
        YearMonth yearMonthFromDate = YearMonth.from(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        return yearMonth.equals(yearMonthFromDate);
    }

    public static String localDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH);
        String formattedDate = date.format(formatter);
        return formattedDate;
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

    public static Date getExpireData(Date date, Integer chargeType, Integer priceUnit, Integer period)
    {
        Date expireTime = null;
        if (chargeType != null && chargeType == 1)
        {
            Date startData = date == null ? new Date() : date;
            switch (priceUnit)
            {
                case 1:
                    expireTime = of(of(startData).plusHours(period));
                    break;
                case 2:
                    expireTime = of(of(startData).plusMonths(period));
                    break;
                case 3:
                    expireTime = of(of(startData).plusYears(period));
                    break;
            }
        }
        return expireTime;
    }
}
