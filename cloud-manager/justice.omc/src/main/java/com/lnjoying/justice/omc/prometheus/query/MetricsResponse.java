package com.lnjoying.justice.omc.prometheus.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lnjoying.justice.omc.domain.model.stat.NodeMetrics;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * <data> { \"status\": \"success\", \"data\": { \"resultType\": \"vector\", \"result\": [ { \"metric\": { \"node_id\": \"FFAHcEEFBN7MThdpwSFabhmZmnYu8CVG1it91\" }, \"value\": [ 1704712420.439, \"23.974727272698622\" ] }, { \"metric\": { \"node_id\": \"FFAHnF7518VGuXLTm2Gfg3HLsvoZDWjvuZdZk\" }, \"value\": [ 1704712420.439, \"11.982545454562219\" ] }, { \"metric\": { \"node_id\": \"FFAHkKT6bejJQdw5sdegR6Rj5YNQUMAsmjKHG\" }, \"value\": [ 1704712420.439, \"11.974909090900015\" ] }, { \"metric\": { \"node_id\": \"FFAHajxJDfWR818pPwAr2KnpwuYnNrSpJoUNg\" }, \"value\": [ 1704712420.439, \"11.954545454482666\" ] } ] } } </data>
 * @Author: Merak
 * @Date: 2024/1/9 9:58
 */

@Data
public class MetricsResponse
{

    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    private MetricsData data;

    public enum ResponseStatus {
        SUCCESS("success"),
        ERROR("error");

        private final String value;

        ResponseStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static ResponseStatus fromValue(String value) {
            for (ResponseStatus status : values()) {
                if (status.value.equals(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown status value: " + value);
        }
    }

    @Data
    public static class MetricsData {

        // "resultType": "matrix" | "vector" | "scalar" | "string",
        @JsonProperty("resultType")
        private String resultType;

        @JsonProperty("result")
        private List<MetricsResult> result;


        public <T> List<MetricsResult> processResults(Class<T> metricClass) {
            switch (resultType) {
                case "matrix":
                    return processMatrixResults();
                case "vector":
                    return processVectorResults();
                case "scalar":
                    return processScalarResults();
                case "string":
                    return processStringResults();
                default:
                    System.out.println("Unsupported resultType: " + resultType);
            }
            return null;
        }


        private List<MetricsResult> processMatrixResults()
        {
            // todo
            return null;
        }

        private List<MetricsResult> processVectorResults()
        {
            return result;
        }

        private List<MetricsResult> processScalarResults()
        {
            // todo
            return null;
        }

        private List<MetricsResult> processStringResults()
        {
            // todo
            return null;
        }
    }

    @Data
    public static class MetricsResult {

        @JsonProperty("metric")
        private Map<String, String> metric;

        @JsonProperty("value")
        private List<Object> value;

    }

}
