package com.micro.core.util;

import com.micro.core.common.Pair;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    public static final String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_NO_SPACE = "yyyyMMddHHmmss";

    public static final String FORMAT_YEAR_MONTH = "yyyy-MM";

    public static final String FORMAT_YEAR = "yyyy";

    public static final String FORMAT_HOUR_MINUTE_SECOND = "HH:mm:ss";


    public static Date of(LocalDateTime date) {
        if (Objects.isNull(date))
        {
            return null;
        }
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

    public static Date getNextYearStartTime(Date givenDate) {
        LocalDateTime currentDateTime = of(givenDate);
        return of(currentDateTime.plusYears(1).withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
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

    public static double hoursDiff(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("date parameter cannot be null");
        }

        long diffInMillis = endDate.getTime() - startDate.getTime();

        double diffInHours = (double) diffInMillis / TimeUnit.HOURS.toMillis(1);

        BigDecimal bd = new BigDecimal(diffInHours);
        bd = bd.setScale(3, RoundingMode.HALF_UP);

        return bd.doubleValue();
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

    public static String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_NO_SPACE);
        return dateFormat.format(new Date());
    }

    public static String getTimestamp(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_NO_SPACE);
        return dateFormat.format(date);
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

    public static String localDateToStringYearMonth(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH);
        String formattedDate = date.format(formatter);
        return formattedDate;
    }

    public static String localDateToStringYearMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YEAR_MONTH);
        return sdf.format(date);

    }

    public static String localDateTimeToString(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);
        String formattedDate = date.format(formatter);
        return formattedDate;
    }

    public static String dateToStringYearMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YEAR_MONTH);
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public static String dateToStringYear(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YEAR);
        String formattedDate = sdf.format(date);
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

    public static Date stringYearMonthToDate(String dateString)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YEAR_MONTH);
        try {
            Date date = sdf.parse(dateString);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static Date stringYearToDate(String dateString)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YEAR);
        try {
            Date date = sdf.parse(dateString);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDateTime parseString(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);
            return LocalDateTime.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("parse date failed：" + e.getMessage());
            return null;
        }
    }

    public static LocalDateTime parseYearMonthString(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH);
            return YearMonth.parse(dateString, formatter).atDay(1).atStartOfDay();
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static LocalDateTime parseString(String dateString, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDateTime.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static Date addYears(Date startTime, Integer period)
    {
        return of(of(startTime).plusYears(period));
    }

    public static Date addMonths(Date startTime, Integer period)
    {
        return of(of(startTime).plusMonths(period));
    }

    public static Date addHours(Date startTime, Integer period)
    {
        return of(of(startTime).plusHours(period));
    }

    public static double calculatePercentageToNextHour(LocalDateTime localDateTime) {
        LocalTime currentTime = localDateTime.toLocalTime();
        LocalTime nextHour = currentTime.truncatedTo(ChronoUnit.HOURS).plusHours(1);
        long timeToNextHour = currentTime.until(nextHour, ChronoUnit.SECONDS);
        long totalSecondsInHour = 3600;
        return (double)timeToNextHour / totalSecondsInHour;
    }

    public static BigDecimal calculatePercentageToNextHour(LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime) {
        LocalTime startLocalTime = startLocalDateTime.toLocalTime();
        LocalTime currentTime = endLocalDateTime.toLocalTime();
        LocalTime nextHour = currentTime.truncatedTo(ChronoUnit.HOURS).plusHours(1);
        long timeToNextHour = startLocalTime.until(nextHour, ChronoUnit.SECONDS);
        long timeToEnd = startLocalTime.until(currentTime, ChronoUnit.SECONDS);
        return  BigDecimal.valueOf(timeToEnd).divide(BigDecimal.valueOf(timeToNextHour), 4, RoundingMode.HALF_UP);
    }

    /**
     * left startHour; right endHour
     * @param giverDateTime
     * @return
     */
    public static Pair<Date, Date> calculateCurrentOrRecentHour(LocalDateTime giverDateTime)
    {

        LocalDateTime startHour = giverDateTime.withMinute(0).withSecond(0).withNano(0);
        if (giverDateTime.getMinute() < 30) {
            startHour = startHour.minusHours(1);
        }

        LocalDateTime endHour = startHour.plusHours(1);

        return new Pair(DateUtils.of(startHour), DateUtils.of(endHour));
    }


    /**
     * left: startLocalDate right:endLocalDate
     * @param startMonth
     * @param endMonth
     * @param startOffset
     * @param endOffset
     * @return
     */
    public static Pair<LocalDate, LocalDate> getDate(String startMonth, String endMonth, int endOffset, int startOffset)
    {
        LocalDate startLocalDate = null;
        LocalDate endLocalDate = null;


        if (StringUtils.isBlank(endMonth))
        {
            endLocalDate = getStartOfPreviousMonths(endOffset);
        }
        else
        {
            endLocalDate = nextStartOfMonth(endMonth);
        }

        if (StringUtils.isBlank(startMonth))
        {
            startLocalDate = getStartOfPreviousMonths(startOffset, endLocalDate);

        }
        else
        {
            startLocalDate = parseStringToDate(startMonth);
        }

        return new Pair<LocalDate, LocalDate>(startLocalDate, endLocalDate);
    }

    public static LocalDate getMonthStartDateOfRangeMonths(List<String> months) {
        YearMonth yearMonth = months.stream()
                .map(YearMonth::parse)
                .min(Comparator.naturalOrder())
                .orElseThrow(IllegalArgumentException::new);
        return yearMonth.atDay(1);
    }

    public static LocalDate getYearStartDateOfRangeYears(List<String> years) {
        Year year = years.stream()
                .map(Year::parse)
                .min(Comparator.naturalOrder())
                .orElseThrow(IllegalArgumentException::new);
        return year.atDay(1);
    }

    public static LocalDate getMonthEndDateOfRangeMonths(List<String> months) {
        YearMonth yearMonth = months.stream()
                .map(YearMonth::parse)
                .max(Comparator.naturalOrder())
                .orElseThrow(IllegalArgumentException::new);
        return yearMonth.atEndOfMonth();
    }

    public static LocalDate getYearEndDateOfRangeYears(List<String> years) {
        Year year = years.stream()
                .map(Year::parse)
                .max(Comparator.naturalOrder())
                .orElseThrow(IllegalArgumentException::new);
        return year.atDay(1).with(TemporalAdjusters.lastDayOfYear());
    }

    public static LocalDateTime EMPTY = DateUtils.of(buildTime(1970, 1, 1));

    public static boolean beforeNow(LocalDateTime date) {
        return date.isBefore(LocalDateTime.now());
    }

    public static boolean afterNow(LocalDateTime date) {
        return date.isAfter(LocalDateTime.now());
    }


    public static LocalDateTime[] buildBetweenTime(int year1, int mouth1, int day1,
                                                   int year2, int mouth2, int day2) {
        return new LocalDateTime[]{DateUtils.of(buildTime(year1, mouth1, day1)), DateUtils.of(buildTime(year2, mouth2, day2))};
    }

    public static LocalDate parseStringToDate(String dateStr) {
        try {
            String fullDateStr = dateStr + "-01";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(fullDateStr, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDate nextStartOfMonth(String dateStr)
    {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dateStr))
        {
            LocalDate localDate = parseStringToDate(dateStr);
            return localDate.plusMonths(1);
        }

        return null;

    }

    public static Pair<Date, Date> convertDate(String startDateStr, String endDateStr)
    {
        Date startDate = null, endDate = null;
        if (StringUtils.isNotBlank(startDateStr))
        {
            startDate = DateUtils.stringToDate(startDateStr);
        }

        if (StringUtils.isNotBlank(endDateStr))
        {
            endDate = DateUtils.stringToDate(endDateStr);
        }

        return new Pair<>(startDate, endDate);
    }

}
