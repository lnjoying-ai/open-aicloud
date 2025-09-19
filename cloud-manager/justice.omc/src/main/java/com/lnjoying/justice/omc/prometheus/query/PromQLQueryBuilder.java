package com.lnjoying.justice.omc.prometheus.query;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/1/8 11:32
 */

public class PromQLQueryBuilder
{

    public static String generateTopKQuery(int k, String[] groupByLabels, String metric, String metricLabels, String timeRange) {
        String groupByClause = String.join(", ", groupByLabels);
        return String.format("topk(%d, sum by (%s) (rate(%s%s[%s])))", k, groupByClause, metric, metricLabels, timeRange);
    }

    public static String generateBottomKQuery(int k, String[] groupByLabels, String metric, String metricLabels, String timeRange) {
        String groupByClause = String.join(", ", groupByLabels);
        return String.format("bottomk(%d, sum by (%s) (rate(%s%s[%s])))", k, groupByClause, metric, metricLabels, timeRange);
    }


    public static String generateTopKQuery(int k, String metric) {

        return String.format("topk(%d, %s)",k, metric);
    }

    public static String generateBottomKQuery(int k, String metric) {
        return String.format("bottomk(%d, %s)", k, metric);
    }

    public static String generateQuery(String metric) {
        return String.format(" %s", metric);
    }

}
