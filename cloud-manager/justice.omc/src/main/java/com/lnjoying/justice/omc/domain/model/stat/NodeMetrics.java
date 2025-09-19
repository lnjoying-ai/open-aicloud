package com.lnjoying.justice.omc.domain.model.stat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class NodeMetrics
{
    @SerializedName("node_id")
    @JsonProperty("node_id")
    private String nodeId;

    @SerializedName("node_name")
    @JsonProperty("node_name")
    private String nodeName;

    @SerializedName("region_id")
    @JsonProperty("region_id")
    private String regionId;

    @SerializedName("region_name")
    @JsonProperty("region_name")
    private String regionName;

    @SerializedName("site_id")
    @JsonProperty("site_id")
    private String siteId;

    @SerializedName("site_name")
    @JsonProperty("site_name")
    private String siteName;

    @SerializedName("cpu_metrics")
    @JsonProperty("cpu_metrics")
    private CpuMetrics cpuMetrics;

    @SerializedName("mem_metrics")
    @JsonProperty("mem_metrics")
    private MemMetrics memMetrics;

    @SerializedName("disk_metrics")
    @JsonProperty("disk_metrics")
    private DiskMetrics diskMetrics;

    @SerializedName("gpu_metrics")
    @JsonProperty("gpu_metrics")
    private GpuMetrics gpuMetrics;

    @SerializedName("network_metrics")
    @JsonProperty("network_metrics")
    private NetworkMetrics networkMetrics;
}
