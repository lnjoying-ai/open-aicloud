package com.lnjoying.justice.schema.entity.stat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.schema.entity.StatusCode;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetVmInstanceStatusMetricsRsp
{
    @SerializedName("vm_instance_status_metrics")
    @JsonProperty("vm_instance_status_metrics")
    private List<CloudVmInstanceStatusMetrics> cloudVmInstanceStatusMetrics = new ArrayList<>();

    @Data
    public static class CloudVmInstanceStatusMetrics
    {
        @SerializedName("cloud_id")
        @JsonProperty("cloud_id")
        private String cloudId;

        @SerializedName("cloud_name")
        @JsonProperty("cloud_name")
        private String cloudName;

        private String product;

        @SerializedName("status_metrics")
        @JsonProperty("status_metrics")
        private List<StatusMetrics2> statusMetrics;
    }

    @Data
    @ApiModel(value = "CloudVmStatusMetrics")
    public static class StatusMetrics2
    {
        private int count;
        private StatusCode status;
    }
}
