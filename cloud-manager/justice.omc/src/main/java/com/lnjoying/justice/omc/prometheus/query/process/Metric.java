package com.lnjoying.justice.omc.prometheus.query.process;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/1/18 19:31
 */

public interface Metric
{
    String getType();

    double getValue();
}
