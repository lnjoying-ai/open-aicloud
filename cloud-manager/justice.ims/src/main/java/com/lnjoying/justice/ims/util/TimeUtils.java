package com.lnjoying.justice.ims.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * time utils
 *
 * @author merak
 **/

public class TimeUtils
{
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
}
