package com.lnjoying.justice.omc.prometheus.query.process;

import com.lnjoying.justice.commonweb.model.Sort;
import com.lnjoying.justice.omc.domain.model.stat.MetricType;
import com.lnjoying.justice.omc.domain.model.stat.NodeMetrics;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/1/18 19:32
 */

public interface MetricSortValueGetter
{
    MetricType getType();

    List<String> getCpuSortMetricsResult(int limit, String timeRange, Sort.Direction direction, List<NodeMetrics> nodeMetricsList, Map<String, Object> sortMetricLabelMap);
}
