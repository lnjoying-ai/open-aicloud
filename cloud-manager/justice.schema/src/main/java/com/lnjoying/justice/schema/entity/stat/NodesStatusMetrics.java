package com.lnjoying.justice.schema.entity.stat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class NodesStatusMetrics
{
    @SerializedName("region_node_metrics")
    @JsonProperty("region_node_metrics")
    List<RegionNodeMetrics> regionNodeMetrics;

    @SerializedName("site_node_metrics")
    @JsonProperty("site_node_metrics")
    List<SiteNodeMetrics> siteNodeMetrics;

    @SerializedName("all_node_metrics")
    @JsonProperty("all_node_metrics")
    NodeStatusMetrics allNodeMetrics;
}
