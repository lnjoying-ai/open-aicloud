package com.lnjoying.justice.omc.prometheus.query;

import com.lnjoying.justice.commonweb.model.Sort;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.omc.domain.model.stat.CpuMetrics;
import com.lnjoying.justice.omc.domain.model.stat.NodeMetrics;
import com.lnjoying.justice.omc.prometheus.client.PrometheusClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/1/8 11:34
 */

@Slf4j
@Component
public class PromQLQueryHelper
{
    @Autowired
    private  PrometheusClient prometheusClient;

    private static PromQLQueryHelper promQLQueryHelper;
    @PostConstruct
    public void init() {
        promQLQueryHelper = this;
        promQLQueryHelper.prometheusClient = this.prometheusClient;
    }

    public static String cpuUsageSort(Sort.Direction direction, Integer k, String timeRange, Map<String, Object> metricMap)
    {
        String labelStr = getMetricLabels(metricMap);

        String groupByClause = String.join(", ", new String[]{"node_id"});
        String metric = String.format("100 - (avg(irate(node_cpu_seconds_total{mode=\"idle\",%s}[%s])) by (%s)  * 100)", labelStr, timeRange, groupByClause);

        return doGenerateQuery(direction, k, metric);
    }




    public static String memoryUsageSort(Sort.Direction direction, Integer k, String timeRange, Map<String, Object> metricLabelMap)
    {
        String labelStr = getMetricLabels(metricLabelMap);
        String groupByClause = String.join(", ", new String[]{"node_id"});
        String metric = String.format("100 - (avg(node_memory_MemAvailable_bytes{%s} / node_memory_MemTotal_bytes{%s}) by (%s) * 100)", labelStr, labelStr, groupByClause);

        return doGenerateQuery(direction, k, metric);
    }

    public static String networkOutRateSort(Sort.Direction direction, Integer k, String timeRange, Map<String, Object> metricLabelMap)
    {
        String labelStr = getMetricLabels(metricLabelMap);
        String groupByClause = String.join(", ", new String[]{"node_id"});
        String metric = String.format("avg(irate(node_network_transmit_bytes_total{device!~\"'tap.*|veth.*|br.*|docker.*|virbr.*|lo.*|cni.*'\", %s}[%s])) by (%s)", labelStr,
                timeRange, groupByClause);

        return doGenerateQuery(direction, k, metric);
    }

    public static String networkInRateSort(Sort.Direction direction, Integer k, String timeRange, Map<String, Object> metricLabelMap)
    {
        String labelStr = getMetricLabels(metricLabelMap);
        String groupByClause = String.join(", ", new String[]{"node_id"});
        String metric = String.format("avg(irate(node_network_receive_bytes_total{device!~\"'tap.*|veth.*|br.*|docker.*|virbr.*|lo.*|cni.*'\", %s}[%s])) by (%s)", labelStr,
                timeRange, groupByClause);

        return doGenerateQuery(direction, k, metric);
    }

    public static String  diskReadRateSort(Sort.Direction direction, Integer k, String timeRange, Map<String, Object> metricLabelMap)
    {
        String labelStr = getMetricLabels(metricLabelMap);
        String groupByClause = String.join(", ", new String[]{"node_id"});
        String metric = String.format("avg(irate(node_disk_reads_completed_total{device=~\".*\", %s}[%s]))  by (%s)", labelStr,
                timeRange, groupByClause);

        return doGenerateQuery(direction, k, metric);
    }

    public static String diskWriteRateSort(Sort.Direction direction, Integer k, String timeRange, Map<String, Object> metricLabelMap)
    {
        String labelStr = getMetricLabels(metricLabelMap);
        String groupByClause = String.join(", ", new String[]{"node_id"});
        String metric = String.format("avg(irate(node_disk_writes_completed_total{device=~\".*\", %s}[%s]))  by (%s)", labelStr,
                timeRange, groupByClause);

        return doGenerateQuery(direction, k, metric);
    }

    public static String mapToCustomString(Map<String, Object> keyValueMap) {
        StringBuilder result = new StringBuilder("");

        for (Map.Entry<String, Object> entry : keyValueMap.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String)
            {
                result.append(entry.getKey()).append("=\"").append(value).append("\", ");
            }
            else if (value instanceof List)
            {
                List<String> ValueList = (List<String>) value;
                StringBuilder nodeLabel = new StringBuilder();
                nodeLabel.append("(");
                String labelValues = StringUtils.arrayToDelimitedString(ValueList.toArray(new String[0]), "|");
                nodeLabel.append(labelValues);
                nodeLabel.append(")");
                result.append(entry.getKey()).append("=~\"").append(nodeLabel).append("\", ");
            }
            else
            {
                log.error("label value type is not String or List, not supported");
            }

        }

        if (result.length() > 1) {
            result.setLength(result.length() - 2);
        }

        result.append("");

        return result.toString();
    }

    public static Map<String, Object> getMetricLabelMap(String bpId, String userId, List<String> nodeIds)
    {
        Map<String, Object> labels = new HashMap<>();
        if (StringUtils.hasText(bpId))
        {
            labels.put("bp_id", bpId);
        }

        if (StringUtils.hasText(userId))
        {
            labels.put("user_id", userId);
        }

        if (!CollectionUtils.isEmpty(nodeIds))
        {
            labels.put("node_id", nodeIds);
        }

        return labels;
    }


    public static List<MetricsResponse.MetricsResult> parseNodeMetricResponse(String promQl)
    {
        String result = promQLQueryHelper.prometheusClient.query(promQl, null, null);
        List<NodeMetrics> nodeMetricsList = new ArrayList<>();
        if (StringUtils.hasText(result))
        {
            MetricsResponse metricsResponse = JacksonUtils.strToObjDefault(result, MetricsResponse.class);
            if (Objects.nonNull(metricsResponse))
            {
                if (MetricsResponse.ResponseStatus.SUCCESS.getValue().equals(metricsResponse.getStatus()))
                {
                    MetricsResponse.MetricsData data = metricsResponse.getData();
                    if (Objects.nonNull(data))
                    {
                        List<MetricsResponse.MetricsResult> metricsResults = data.processResults(null);
                        if (!CollectionUtils.isEmpty(metricsResults))
                        {
                            return metricsResults;
                        }
                    }
                }
                else
                {
                    log.error("query error, status:{}", metricsResponse.getStatus());
                }
            }
        }

        return null;
    }

    private static String getMetricLabels(Map<String, Object> labels)
    {

        String labelStr = mapToCustomString(labels);
        return labelStr;
    }

    private static String doGenerateQuery(Sort.Direction direction, Integer k, String metric)
    {
        if (Objects.isNull(direction))
        {
            return PromQLQueryBuilder.generateQuery(metric);
        }

        if (direction == Sort.Direction.ASC)
        {
            return PromQLQueryBuilder.generateBottomKQuery(k, metric);
        }
        else
        {
            return PromQLQueryBuilder.generateTopKQuery(k, metric);
        }
    }
}
