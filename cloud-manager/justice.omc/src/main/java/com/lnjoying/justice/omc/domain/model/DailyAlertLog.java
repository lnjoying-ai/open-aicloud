package com.lnjoying.justice.omc.domain.model;

import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/12/19 15:04
 */

@Data
public class DailyAlertLog
{
    private Date alertDay;

    private int level;

    private long alertCount;
}
