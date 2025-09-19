package com.lnjoying.justice.ims.util;

import java.time.LocalDateTime;

/**
 * date time transform
 *
 * @author merak
 **/

public class ThreetenUtils
{
    /**
     * threeten LocalDateTime convert to java8 LocalDateTime
     *
     * @param ttOdt
     * @return
     */
    public static java.time.LocalDateTime convertFrom(org.threeten.bp.LocalDateTime ttOdt)
    {
        return LocalDateTime.of(ttOdt.getYear(), ttOdt.getMonthValue(), ttOdt.getDayOfMonth(),
                ttOdt.getHour(), ttOdt.getMinute(), ttOdt.getSecond());
    }
}
