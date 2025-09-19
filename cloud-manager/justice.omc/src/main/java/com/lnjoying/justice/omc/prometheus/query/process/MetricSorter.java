package com.lnjoying.justice.omc.prometheus.query.process;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/1/18 19:32
 */

public interface MetricSorter
{
    List<Metric> sort(List<Metric> metrics);
}
