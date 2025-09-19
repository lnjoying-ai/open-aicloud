package com.lnjoying.justice.omc.prometheus.query.process.sort;

import com.lnjoying.justice.commonweb.model.Sort;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.omc.domain.model.stat.CpuMetrics;
import com.lnjoying.justice.omc.domain.model.stat.MetricType;
import com.lnjoying.justice.omc.domain.model.stat.NodeMetrics;
import com.lnjoying.justice.omc.prometheus.query.MetricsResponse;
import com.lnjoying.justice.omc.prometheus.query.PromQLQueryHelper;
import com.lnjoying.justice.omc.prometheus.query.process.MetricSortValueGetter;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.omc.prometheus.query.PromQLQueryHelper.parseNodeMetricResponse;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/1/18 20:21
 */

@Slf4j
public class CpuMetricSortValueGetter extends AbstracMetricSortValueGetter
{

    @Override
    public MetricType getType()
    {
        return MetricType.CPU;
    }

    @Override
    public List<String> getCpuSortMetricsResult(int limit, String timeRange, Sort.Direction direction, List<NodeMetrics> nodeMetricsList, Map<String, Object> sortMetricLabelMap)
    {
        String sortPromQl = PromQLQueryHelper.cpuUsageSort(direction, limit, timeRange, sortMetricLabelMap);
        List<MetricsResponse.MetricsResult> metricsResults = parseNodeMetricResponse(sortPromQl);

        List<String> nodeIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(metricsResults))
        {
            List<NodeMetrics>  nodeMetricsCollection = metricsResults.stream().map(metricsResult ->
            {
                NodeMetrics nodeMetrics = new NodeMetrics();

                Map<String, String> metricMap = metricsResult.getMetric();
                if (!CollectionUtils.isEmpty(metricMap))
                {
                    String nodeId = metricMap.get("node_id");
                    if (StringUtils.isNotBlank(nodeId))
                    {
                        nodeIds.add(nodeId);
                        nodeMetrics.setNodeId(nodeId);
                        try
                        {
                            RegionResourceService.NodeInfo nodeInfo = combRpcService.getRegionResourceService().getNodeInfoByNodeId(nodeId);
                            if (Objects.nonNull(nodeInfo))
                            {
                                nodeMetrics.setNodeName(nodeInfo.getNodeName());
                                nodeMetrics.setRegionId(nodeInfo.getRegionId());
                                nodeMetrics.setRegionName(nodeInfo.getRegionName());
                                nodeMetrics.setSiteId(nodeInfo.getSiteId());
                                nodeMetrics.setSiteName(nodeInfo.getSiteName());
                            }
                        }
                        catch (Exception e)
                        {
                            log.error("get node name error, reason {}", e.getMessage());
                        }

                        CpuMetrics cpuMetrics = new CpuMetrics();
                        cpuMetrics.setUtilisations(Collections.singletonList(Collections.singletonMap(String.valueOf(metricsResult.getValue().get(0)),
                                Float.valueOf((String) metricsResult.getValue().get(1)))));
                        nodeMetrics.setCpuMetrics(cpuMetrics);
                    }

                }

                return nodeMetrics;
            }).collect(Collectors.toList());
            nodeMetricsList.addAll(nodeMetricsCollection);
        }
        return nodeIds;
    }
}
