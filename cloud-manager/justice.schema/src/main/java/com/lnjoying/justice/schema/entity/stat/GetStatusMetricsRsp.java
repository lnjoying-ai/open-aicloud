package com.lnjoying.justice.schema.entity.stat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.schema.entity.StatusCode;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
public class GetStatusMetricsRsp
{
    @SerializedName("total_num")
    @JsonProperty("total_num")
    private int totalNum;

    @SerializedName("status_metrics")
    @JsonProperty("status_metrics")
    private List<StatusMetrics> statusMetrics;

    @Data
    @EqualsAndHashCode(of = "status")
    public static class StatusMetrics
    {
        private int count;
        private StatusCode status;
    }
}
