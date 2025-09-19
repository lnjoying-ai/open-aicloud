package com.lnjoying.justice.omc.domain.dto.rsp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.omc.domain.model.stat.NodeMetrics;
import lombok.Data;

import java.util.List;

@Data
public class GetNodeResourceSortMetricsRsp
{
    @SerializedName("node_metrics")
    @JsonProperty("node_metrics")
    private List<NodeMetrics> nodeMetrics;
}
