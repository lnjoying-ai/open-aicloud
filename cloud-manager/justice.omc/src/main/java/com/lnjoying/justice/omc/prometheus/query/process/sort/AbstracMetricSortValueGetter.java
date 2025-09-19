package com.lnjoying.justice.omc.prometheus.query.process.sort;

import com.lnjoying.justice.omc.prometheus.query.process.MetricSortValueGetter;
import com.lnjoying.justice.omc.service.CombRpcService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/1/18 20:22
 */

public abstract class AbstracMetricSortValueGetter implements MetricSortValueGetter
{
    @Autowired
    protected CombRpcService combRpcService;
}
