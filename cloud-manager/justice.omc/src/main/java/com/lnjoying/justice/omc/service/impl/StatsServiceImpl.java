package com.lnjoying.justice.omc.service.impl;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.model.Sort;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.omc.domain.dto.rsp.GetNodeResourceSortMetricsRsp;
import com.lnjoying.justice.omc.domain.dto.rsp.StatsCountsRsp;
import com.lnjoying.justice.omc.domain.model.DailyStatusCount;
import com.lnjoying.justice.omc.domain.model.stat.*;
import com.lnjoying.justice.omc.prometheus.client.PrometheusClient;
import com.lnjoying.justice.omc.prometheus.query.MetricsResponse;
import com.lnjoying.justice.omc.prometheus.query.PromQLQueryHelper;
import com.lnjoying.justice.omc.prometheus.query.process.Metric;
import com.lnjoying.justice.omc.service.CombRpcService;
import com.lnjoying.justice.omc.service.PrometheusService;
import com.lnjoying.justice.omc.service.StatsService;
import com.lnjoying.justice.omc.util.DateUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.entity.stat.GetClusterNumMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.GetStatusMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.GetVmInstanceStatusMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.NodesStatusMetrics;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;
import static com.lnjoying.justice.omc.prometheus.query.PromQLQueryHelper.getMetricLabelMap;
import static com.lnjoying.justice.omc.prometheus.query.PromQLQueryHelper.parseNodeMetricResponse;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/1 11:25
 */

@Slf4j
@Service
public class StatsServiceImpl implements StatsService
{

    @Autowired
    private CombRpcService combRpcService;


    @Override
    public StatsCountsRsp getStatsCounts(Date startDate, Date endDate)
    {
        try {
            String bpId = queryBpId();
            String userId = queryUserId();
            CompletableFuture<Object> vmsFuture = CompletableFuture.supplyAsync(() -> getVmStatsCounts(bpId, userId));
            CompletableFuture<Object> nodeFuture = CompletableFuture.supplyAsync(() -> getNodeStatsCounts(bpId, userId));
            CompletableFuture<Object> clusterFuture = CompletableFuture.supplyAsync(() -> getClusterStatsCounts(bpId, userId));
            CompletableFuture<Object> containerFuture = CompletableFuture.supplyAsync(() -> getContainerStatsCounts(bpId, userId));

            Object vmStatsCounts = vmsFuture.get();
            Object nodeStatsCounts = nodeFuture.get();
            Object clusterStatsCounts = clusterFuture.get();
            Object containerStatsCounts = containerFuture.get();

            return StatsCountsRsp.builder()
                    .totalVms(vmStatsCounts)
                    .totalNodes(nodeStatsCounts)
                    .totalClusters(clusterStatsCounts)
                    .totalContainers(containerStatsCounts)
                    .build();
            } catch (Exception e) {
                log.error("getStatsCounts error", e);
            }

        return StatsCountsRsp.builder()
                .totalVms(new StatsCountsRsp.StatsCounts())
                .totalNodes(new StatsCountsRsp.StatsCounts())
                .totalClusters(new StatsCountsRsp.StatsCounts())
                .totalContainers(new StatsCountsRsp.StatsCounts())
                .build();
    }

    private Object getVmStatsCounts(String bpId, String userId)
    {
        GetVmInstanceStatusMetricsRsp vmInstanceStatusMetrics = new GetVmInstanceStatusMetricsRsp();

        try
        {
            vmInstanceStatusMetrics = combRpcService.getCloudManagerService().getCloudVmInstanceStatusMetrics(userId, bpId);
        }
        catch (Exception e)
        {
            log.error("getVmStatsCounts fail", e);
        }
        catch (Error e)
        {
            log.error("getVmStatsCounts error", e);
        }

        return vmInstanceStatusMetrics;
    }

    private  GetStatusMetricsRsp getNodeStatsCounts(String bpId, String userId)
    {
        GetStatusMetricsRsp edgeStatusMetrics = new GetStatusMetricsRsp();

        try
        {
            edgeStatusMetrics = combRpcService.getRegionResourceService().getEdgeStatusMetrics(bpId, userId);
        }
        catch (Exception e)
        {
            log.error("getEdgeStatusMetrics fail", e);
        }
        catch (Error e)
        {
            log.error("getEdgeStatusMetrics error", e);
        }

        return edgeStatusMetrics;
    }

    private  GetStatusMetricsRsp getClusterStatsCounts(String bpId, String userId)
    {
        GetStatusMetricsRsp clusterStatusMetrics = new GetStatusMetricsRsp();

        try
        {
            clusterStatusMetrics = combRpcService.getClusterManagerService().getClusterStatusMetrics(userId, bpId);
        }
        catch (Exception e)
        {
            log.error("getClusterStatusMetrics fail", e);
        }
        catch (Error e)
        {
            log.error("getClusterStatusMetrics error", e);
        }

        return clusterStatusMetrics;
    }

    private GetStatusMetricsRsp getContainerStatsCounts(String bpId, String userId)
    {
        GetStatusMetricsRsp containerInstanceStatusMetrics = new GetStatusMetricsRsp();

        try
        {
            containerInstanceStatusMetrics = combRpcService.getContainerInstService().getContainerInstanceStatusMetrics(bpId, userId);

        }
        catch (Exception e)
        {
            log.error("getContainerInstanceStatusMetrics fail", e);
        }
        catch (Error e)
        {
            log.error("getContainerInstanceStatusMetrics error", e);
        }

        return containerInstanceStatusMetrics;
    }


    @Override
    public List<DailyStatusCount> getDailyStatsCounts(Date startDate, Date endDate, int resourceType)
    {

        // vm
        if (resourceType == 1)
        {
            List<DailyStatusCount> dailyStatusCounts = generateVmDailyTrendData(DateUtils.of(startDate), DateUtils.of(endDate), null);
            return dailyStatusCounts;
        }
        // node
        else if (resourceType == 2)
        {
            List<DailyStatusCount> dailyStatusCounts = generateNodeDailyTrendData(DateUtils.of(startDate), DateUtils.of(endDate), null);
            return dailyStatusCounts;
        }
        else
        {
            log.error("unsupported resource type");
        }
        return null;
    }

    public Object getStatusMetrics(String filter, String userId, String bpId)
    {
        try
        {
            switch (filter.toLowerCase(Locale.ROOT))
            {
                case "cloud":
                    return combRpcService.getCloudManagerService().getCloudStatusMetrics(userId, bpId);
                case "cluster":
                    return combRpcService.getClusterManagerService().getClusterStatusMetrics(userId, bpId);
                case "cloud-health":
                    return combRpcService.getCloudManagerService().getCloudHealthStatusMetrics(userId, bpId);
                case "cloud-vm-instance":
                    return combRpcService.getCloudManagerService().getCloudVmInstanceStatusMetrics(userId, bpId);
                case "node-online":
                    return combRpcService.getRegionResourceService().getEdgeStatusMetrics(userId, bpId);
                default:
                    throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
            }
        }
        catch (Exception e)
        {
            log.error("get {} status metrics error, reason {}", filter, e.getMessage());
            return null;
        }
    }

    public GetClusterNumMetricsRsp getClusterNumMetrics(String userId, String bpId)
    {
        try
        {
            return combRpcService.getClusterManagerService().getClusterNumMetrics(userId, bpId);
        }
        catch (Exception e)
        {
            log.error("get cluster num from cls error, reason {}", e.getMessage());
            return null;
        }
    }

    @Override
    public NodesStatusMetrics getNodeStatusMetrics(String regionId, String siteId, String filter, String bpId, String userId)
    {
        return combRpcService.getRegionResourceService().getNodeStatusMetrics(regionId, siteId, filter, bpId, userId);
    }

    @Override
    public GetNodeResourceSortMetricsRsp getNodeResourceSortMetrics(String condition, String sort, int limit, String userId, String bpId, String timeRange)
    {
        GetNodeResourceSortMetricsRsp getNodeResourceSortMetricsRsp = new GetNodeResourceSortMetricsRsp();

        Sort.Direction direction = Sort.Direction.DESC;
        if (StringUtils.isNotBlank(sort))
        {
            direction = Sort.Direction.fromString(sort);
        }


        getNodeResourceSortMetricsRsp.setNodeMetrics(new ArrayList<>());

        List<NodeMetrics> nodeMetricsList = getNodeResourceSortMetricsRsp.getNodeMetrics();
        Map<String, Object> sortMetricLabelMap = getMetricLabelMap(queryBpId(), queryUserId(), null);
        List<String> nodeIds = new ArrayList<>();
        MetricType metricType = MetricType.fromName(condition);
        switch (metricType)
        {
            case CPU:
                nodeIds = getCpuSortMetricsResult(limit, timeRange, direction, nodeMetricsList, sortMetricLabelMap);
                break;
            case MEM:
                nodeIds = getMemorySortMetricsResult(limit, timeRange, direction, nodeMetricsList, sortMetricLabelMap);
                break;
            case DISK_READ:
                nodeIds = getDiskReadSortMetricsResult(limit, timeRange, direction, nodeMetricsList, sortMetricLabelMap);
                break;
            case DISK_WRITE:
                nodeIds = getDiskWriteSortMetricsResult(limit, timeRange, direction, nodeMetricsList, sortMetricLabelMap);
                break;
            case NETWORK_IN:
                nodeIds = getNetworkInSortMetricsResult(limit, timeRange, direction, nodeMetricsList, sortMetricLabelMap);
                break;
            case NETWORK_OUT:
                nodeIds = getNetworkOutSortMetricsResult(limit, timeRange, direction, nodeMetricsList, sortMetricLabelMap);
                break;
            default:
                log.error("unsupported metric type: {}", condition);
        }
        if (!CollectionUtils.isEmpty(nodeMetricsList))
        {
            Map<String, NodeMetrics> nodeMetricsMap = nodeMetricsList.stream().collect(Collectors.toMap(NodeMetrics::getNodeId, Function.identity()));
            Map<String, Object> metricLabelMap = getMetricLabelMap(queryBpId(), queryUserId(), nodeIds);

            switch (metricType)
            {
                case CPU:
                    getMemoryMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getNetworkInMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getNetworkOutMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getDiskReadMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getDiskWriteMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    break;
                case MEM:
                    getCpuMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getNetworkInMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getNetworkOutMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getDiskReadMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getDiskWriteMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    break;
                case DISK_READ:
                    getCpuMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getMemoryMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getNetworkInMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getNetworkOutMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getDiskWriteMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    break;
                case DISK_WRITE:
                    getCpuMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getMemoryMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getNetworkInMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getNetworkOutMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getDiskReadMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    break;
                case NETWORK_IN:
                    getCpuMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getMemoryMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getNetworkOutMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getDiskReadMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getDiskWriteMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    break;
                case NETWORK_OUT:
                    getCpuMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getMemoryMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getNetworkInMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getDiskReadMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    getDiskWriteMetricsResults(timeRange, nodeMetricsMap, metricLabelMap);
                    break;
                default:
                    log.error("unsupported metric type: {}", condition);
            }

            return getNodeResourceSortMetricsRsp;
        }


        return getNodeResourceSortMetricsRsp;
    }

    private List<String> getCpuSortMetricsResult(int limit, String timeRange, Sort.Direction direction, List<NodeMetrics> nodeMetricsList, Map<String, Object> sortMetricLabelMap)
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

    private static List<DailyStatusCount> generateNodeDailyTrendData(LocalDateTime startDay, LocalDateTime endDay, List<DailyStatusCount> dailyTrends)
    {
        List<DailyStatusCount> result = new ArrayList<>();
        while (!(startDay.isAfter(endDay) || startDay.isEqual(endDay))) {

            Map<String, Integer> statusCounts = Arrays.stream(NodeResourceStatus.values())
                    .collect(Collectors.toMap(NodeResourceStatus::name, state -> (int) (Math.random() * 101)));

            result.add(new DailyStatusCount(DateUtils.formatLocalDateTime(startDay), statusCounts));
            startDay  = startDay.plusDays(1);
        }

        return result;
    }


    private static List<DailyStatusCount> generateVmDailyTrendData(LocalDateTime startDay, LocalDateTime endDay, List<DailyStatusCount> dailyTrends)
    {
        List<DailyStatusCount> result = new ArrayList<>();
        while (!(startDay.isAfter(endDay) || startDay.isEqual(endDay))) {

            Map<String, Integer> statusCounts = Arrays.stream(VmResourceStatus.values())
                    .collect(Collectors.toMap(VmResourceStatus::name, state -> (int) (Math.random() * 3)));

            result.add(new DailyStatusCount(DateUtils.formatLocalDateTime(startDay), statusCounts));
            startDay  = startDay.plusDays(1);
        }

        return result;
    }


    public enum ResourceStatus {
        RUNNING("running"),
        ABNORMAL("abnormal");

        private final String value;

        ResourceStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum VmResourceStatus {
        RUNNING,
        STOPPED,
        PAUSED,
        SUSPENDED

    }


    public enum NodeResourceStatus {
        /**
         * 节点可用
         */
        AVAILABLE,
        /**
         * 节点不可用
         */
        UNAVAILABLE,
        /**
         * 节点正在初始化
         */
        INITIALIZING,
        /**
         * 节点正在伸缩
         */
        SCALING,
        /**
         * 节点异常
         */
        ABNORMAL
    }
}
