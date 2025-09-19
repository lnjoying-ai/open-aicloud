package com.lnjoying.justice.omc.service;

import com.lnjoying.justice.omc.domain.dto.rsp.GetNodeResourceSortMetricsRsp;
import com.lnjoying.justice.omc.domain.dto.rsp.IntegrationsRsp;
import com.lnjoying.justice.omc.domain.dto.rsp.StatsCountsRsp;
import com.lnjoying.justice.omc.domain.model.DailyStatusCount;
import com.lnjoying.justice.schema.entity.stat.GetClusterNumMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.GetStatusMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.NodesStatusMetrics;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/1 11:07
 */

public interface StatsService
{

    StatsCountsRsp getStatsCounts(Date startDate, Date endDate);

    List<DailyStatusCount> getDailyStatsCounts(Date startDate, Date endDate, int resourceType);

    Object getStatusMetrics(String filter, String userId, String bpId);

    GetClusterNumMetricsRsp getClusterNumMetrics(String userId, String bpId);

    NodesStatusMetrics getNodeStatusMetrics(String regionId, String siteId, String filter, String bpId, String userId);

    GetNodeResourceSortMetricsRsp getNodeResourceSortMetrics(String condition, String sort, int limit, String userId, String bpId, String timeRange);
}
