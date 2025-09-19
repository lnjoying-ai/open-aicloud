package com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClusterTerminalRspDto
{
    @JsonProperty("pod_name")
    private String podName;

    @JsonProperty("container_name")
    private String containerName;

    @JsonProperty("namespace")
    private String namespace;

    @JsonProperty("status")
    private String status;

    @JsonProperty("dashboard_url")
    private String dashboardUrl;
}
