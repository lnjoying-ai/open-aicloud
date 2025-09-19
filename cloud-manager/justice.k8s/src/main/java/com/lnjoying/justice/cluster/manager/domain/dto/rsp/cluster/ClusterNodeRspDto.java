package com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.ClusterNodeInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClusterNodeRspDto
{
    @JsonProperty("cluster_type")
    private String clusterType;

    @JsonProperty("cluster_id")
    private String clusterId;

    @JsonProperty("cluster_name")
    private String clusterName;

    @JsonProperty("node_info")
    private ClusterNodeInfoDto nodeInfo;
}
