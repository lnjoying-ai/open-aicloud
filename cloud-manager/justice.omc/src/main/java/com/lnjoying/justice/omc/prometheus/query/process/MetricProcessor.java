package com.lnjoying.justice.omc.prometheus.query.process;

import com.lnjoying.justice.commonweb.model.Sort;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.omc.domain.model.stat.*;
import com.lnjoying.justice.omc.prometheus.query.MetricsResponse;
import com.lnjoying.justice.omc.prometheus.query.PromQLQueryHelper;
import com.lnjoying.justice.omc.service.CombRpcService;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.omc.prometheus.query.PromQLQueryHelper.parseNodeMetricResponse;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/1/18 19:33
 */

@Slf4j
@Component
public class MetricProcessor
{
    @Autowired
    private CombRpcService combRpcService;


    private List<String> getMemorySortMetricsResult(int limit, String timeRange, Sort.Direction direction, List<NodeMetrics> nodeMetricsList,
                                                    Map<String, Object> sortMetricLabelMap)
    {
        String sortPromQl = PromQLQueryHelper.memoryUsageSort(direction, limit, timeRange, sortMetricLabelMap);
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

                        MemMetrics memMetrics = new MemMetrics();
                        memMetrics.setUtilisations(Collections.singletonList(Collections.singletonMap(String.valueOf(metricsResult.getValue().get(0)),
                                Float.valueOf((String) metricsResult.getValue().get(1)))));
                        nodeMetrics.setMemMetrics(memMetrics);
                    }

                }

                return nodeMetrics;
            }).collect(Collectors.toList());
            nodeMetricsList.addAll(nodeMetricsCollection);
        }
        return nodeIds;
    }

    private List<String> getDiskWriteSortMetricsResult(int limit, String timeRange, Sort.Direction direction, List<NodeMetrics> nodeMetricsList,
                                                       Map<String, Object> sortMetricLabelMap)
    {
        String sortPromQl = PromQLQueryHelper.diskWriteRateSort(direction, limit, timeRange, sortMetricLabelMap);
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
                        DiskMetrics diskMetrics = new DiskMetrics();

                        diskMetrics.setDiskWrites(Collections.singletonList(Collections.singletonMap(String.valueOf(metricsResult.getValue().get(0)),
                                Float.valueOf((String) metricsResult.getValue().get(1)))));
                        nodeMetrics.setDiskMetrics(diskMetrics);
                    }

                }

                return nodeMetrics;
            }).collect(Collectors.toList());
            nodeMetricsList.addAll(nodeMetricsCollection);
        }
        return nodeIds;
    }

    private List<String> getDiskReadSortMetricsResult(int limit, String timeRange, Sort.Direction direction, List<NodeMetrics> nodeMetricsList,
                                                      Map<String, Object> sortMetricLabelMap)
    {
        String sortPromQl = PromQLQueryHelper.diskReadRateSort(direction, limit, timeRange, sortMetricLabelMap);
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
                        DiskMetrics diskMetrics = new DiskMetrics();

                        diskMetrics.setDiskReads(Collections.singletonList(Collections.singletonMap(String.valueOf(metricsResult.getValue().get(0)),
                                Float.valueOf((String) metricsResult.getValue().get(1)))));
                        nodeMetrics.setDiskMetrics(diskMetrics);
                    }

                }

                return nodeMetrics;
            }).collect(Collectors.toList());
            nodeMetricsList.addAll(nodeMetricsCollection);
        }
        return nodeIds;
    }

    private List<String> getNetworkInSortMetricsResult(int limit, String timeRange, Sort.Direction direction, List<NodeMetrics> nodeMetricsList,
                                                       Map<String, Object> sortMetricLabelMap)
    {
        String sortPromQl = PromQLQueryHelper.networkInRateSort(direction, limit, timeRange, sortMetricLabelMap);
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
                        NetworkMetrics networkMetrics = new NetworkMetrics();

                        networkMetrics.setRecv(Collections.singletonList(Collections.singletonMap(String.valueOf(metricsResult.getValue().get(0)),
                                Float.valueOf((String) metricsResult.getValue().get(1)))));
                        nodeMetrics.setNetworkMetrics(networkMetrics);
                    }

                }

                return nodeMetrics;
            }).collect(Collectors.toList());
            nodeMetricsList.addAll(nodeMetricsCollection);
        }
        return nodeIds;
    }

    private List<String> getNetworkOutSortMetricsResult(int limit, String timeRange, Sort.Direction direction, List<NodeMetrics> nodeMetricsList,
                                                        Map<String, Object> sortMetricLabelMap)
    {
        String sortPromQl = PromQLQueryHelper.networkOutRateSort(direction, limit, timeRange, sortMetricLabelMap);
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
                        NetworkMetrics networkMetrics = new NetworkMetrics();

                        networkMetrics.setTransmit(Collections.singletonList(Collections.singletonMap(String.valueOf(metricsResult.getValue().get(0)),
                                Float.valueOf((String) metricsResult.getValue().get(1)))));
                        nodeMetrics.setNetworkMetrics(networkMetrics);
                    }

                }

                return nodeMetrics;
            }).collect(Collectors.toList());
            nodeMetricsList.addAll(nodeMetricsCollection);
        }
        return nodeIds;
    }

    private static void getCpuMetricsResults(String timeRange, Map<String, NodeMetrics> nodeMetricsMap, Map<String, Object> metricLabelMap)
    {
        String memoryPromQl = PromQLQueryHelper.cpuUsageSort(null, null, timeRange, metricLabelMap);
        List<MetricsResponse.MetricsResult>cpuMetricsResults = parseNodeMetricResponse(memoryPromQl);
        if (!CollectionUtils.isEmpty(cpuMetricsResults))
        {
            cpuMetricsResults.stream().forEach(metricsResult ->
            {
                CpuMetrics cpuMetrics = new CpuMetrics();
                cpuMetrics.setUtilisations(Collections.singletonList(Collections.singletonMap(String.valueOf(metricsResult.getValue().get(0)),
                        Float.valueOf((String) metricsResult.getValue().get(1)))));
                Map<String, String> metricMap = metricsResult.getMetric();
                if (!CollectionUtils.isEmpty(metricMap))
                {
                    String nodeId = metricMap.get("node_id");
                    if (StringUtils.isNotBlank(nodeId))
                    {
                        nodeMetricsMap.get(nodeId).setCpuMetrics(cpuMetrics);
                    }
                }
            });
        }
    }

    private static void getMemoryMetricsResults(String timeRange, Map<String, NodeMetrics> nodeMetricsMap, Map<String, Object> metricLabelMap)
    {
        String memoryPromQl = PromQLQueryHelper.memoryUsageSort(null, null, timeRange, metricLabelMap);
        List<MetricsResponse.MetricsResult> memoryMetricsResults = parseNodeMetricResponse(memoryPromQl);
        if (!CollectionUtils.isEmpty(memoryMetricsResults))
        {
            memoryMetricsResults.stream().forEach(metricsResult ->
            {
                MemMetrics memMetrics = new MemMetrics();
                memMetrics.setUtilisations(Collections.singletonList(Collections.singletonMap(String.valueOf(metricsResult.getValue().get(0)),
                        Float.valueOf((String) metricsResult.getValue().get(1)))));
                Map<String, String> metricMap = metricsResult.getMetric();
                if (!CollectionUtils.isEmpty(metricMap))
                {
                    String nodeId = metricMap.get("node_id");
                    if (StringUtils.isNotBlank(nodeId))
                    {
                        nodeMetricsMap.get(nodeId).setMemMetrics(memMetrics);
                    }
                }
            });
        }
    }

    private static void getDiskWriteMetricsResults(String timeRange, Map<String, NodeMetrics> nodeMetricsMap, Map<String, Object> metricLabelMap)
    {
        String diskWritePromQl = PromQLQueryHelper.diskWriteRateSort(null, null, timeRange, metricLabelMap);
        List<MetricsResponse.MetricsResult> diskWriteMetricsResults = parseNodeMetricResponse(diskWritePromQl);
        if (!CollectionUtils.isEmpty(diskWriteMetricsResults))
        {
            diskWriteMetricsResults.stream().forEach(metricsResult ->
            {
                DiskMetrics diskMetrics = new DiskMetrics();
                diskMetrics.setDiskWrites(Collections.singletonList(Collections.singletonMap(String.valueOf(metricsResult.getValue().get(0)),
                        Float.valueOf((String) metricsResult.getValue().get(1)))));
                Map<String, String> metricMap = metricsResult.getMetric();
                if (!CollectionUtils.isEmpty(metricMap))
                {
                    String nodeId = metricMap.get("node_id");
                    if (StringUtils.isNotBlank(nodeId))
                    {
                        DiskMetrics dm = nodeMetricsMap.get(nodeId).getDiskMetrics();
                        if (Objects.nonNull(dm))
                        {
                            dm.setDiskWrites(diskMetrics.getDiskWrites());
                            nodeMetricsMap.get(nodeId).setDiskMetrics(dm);
                        }
                        else
                        {
                            nodeMetricsMap.get(nodeId).setDiskMetrics(diskMetrics);
                        }
                    }
                }
            });
        }
    }

    private static void getDiskReadMetricsResults(String timeRange, Map<String, NodeMetrics> nodeMetricsMap, Map<String, Object> metricLabelMap)
    {
        String diskReadPromQl = PromQLQueryHelper.diskReadRateSort(null, null, timeRange, metricLabelMap);
        List<MetricsResponse.MetricsResult> diskReadMetricsResults = parseNodeMetricResponse(diskReadPromQl);
        if (!CollectionUtils.isEmpty(diskReadMetricsResults))
        {
            diskReadMetricsResults.stream().forEach(metricsResult ->
            {
                DiskMetrics diskMetrics = new DiskMetrics();
                diskMetrics.setDiskReads(Collections.singletonList(Collections.singletonMap(String.valueOf(metricsResult.getValue().get(0)),
                        Float.valueOf((String) metricsResult.getValue().get(1)))));
                Map<String, String> metricMap = metricsResult.getMetric();
                if (!CollectionUtils.isEmpty(metricMap))
                {
                    String nodeId = metricMap.get("node_id");
                    if (StringUtils.isNotBlank(nodeId))
                    {
                        DiskMetrics dm = nodeMetricsMap.get(nodeId).getDiskMetrics();
                        if (Objects.nonNull(dm))
                        {
                            dm.setDiskReads(diskMetrics.getDiskReads());
                            nodeMetricsMap.get(nodeId).setDiskMetrics(dm);
                        }
                        else
                        {
                            nodeMetricsMap.get(nodeId).setDiskMetrics(diskMetrics);
                        }
                    }
                }
            });
        }
    }

    private static void getNetworkInMetricsResults(String timeRange, Map<String, NodeMetrics> nodeMetricsMap, Map<String, Object> metricLabelMap)
    {
        String networkOutPromQl = PromQLQueryHelper.networkOutRateSort(null, null, timeRange, metricLabelMap);
        List<MetricsResponse.MetricsResult> networkOutMetricsResults = parseNodeMetricResponse(networkOutPromQl);
        if (!CollectionUtils.isEmpty(networkOutMetricsResults))
        {
            networkOutMetricsResults.stream().forEach(metricsResult ->
            {
                NetworkMetrics networkMetrics = new NetworkMetrics();
                networkMetrics.setTransmit(Collections.singletonList(Collections.singletonMap(String.valueOf(metricsResult.getValue().get(0)),
                        Float.valueOf((String) metricsResult.getValue().get(1)))));
                Map<String, String> metricMap = metricsResult.getMetric();
                if (!CollectionUtils.isEmpty(metricMap))
                {
                    String nodeId = metricMap.get("node_id");
                    if (StringUtils.isNotBlank(nodeId))
                    {
                        NetworkMetrics nm = nodeMetricsMap.get(nodeId).getNetworkMetrics();
                        if (Objects.nonNull(nm))
                        {
                            nm.setTransmit(networkMetrics.getTransmit());
                            nodeMetricsMap.get(nodeId).setNetworkMetrics(nm);
                        }
                        else
                        {
                            nodeMetricsMap.get(nodeId).setNetworkMetrics(networkMetrics);
                        }
                    }
                }
            });
        }
    }

    private static void getNetworkOutMetricsResults(String timeRange, Map<String, NodeMetrics> nodeMetricsMap, Map<String, Object> metricLabelMap)
    {
        String networkInPromQl = PromQLQueryHelper.networkInRateSort(null, null, timeRange, metricLabelMap);
        List<MetricsResponse.MetricsResult> networkInMetricsResults = parseNodeMetricResponse(networkInPromQl);
        if (!CollectionUtils.isEmpty(networkInMetricsResults))
        {
            networkInMetricsResults.stream().forEach(metricsResult ->
            {
                NetworkMetrics networkMetrics = new NetworkMetrics();
                networkMetrics.setRecv(Collections.singletonList(Collections.singletonMap(String.valueOf(metricsResult.getValue().get(0)),
                        Float.valueOf((String) metricsResult.getValue().get(1)))));
                Map<String, String> metricMap = metricsResult.getMetric();
                if (!CollectionUtils.isEmpty(metricMap))
                {
                    String nodeId = metricMap.get("node_id");
                    if (StringUtils.isNotBlank(nodeId))
                    {
                        NetworkMetrics nm = nodeMetricsMap.get(nodeId).getNetworkMetrics();
                        if (Objects.nonNull(nm))
                        {
                            nm.setRecv(networkMetrics.getRecv());
                            nodeMetricsMap.get(nodeId).setNetworkMetrics(nm);
                        }
                        else
                        {
                            nodeMetricsMap.get(nodeId).setNetworkMetrics(networkMetrics);
                        }
                    }
                }
            });
        }
    }
}
