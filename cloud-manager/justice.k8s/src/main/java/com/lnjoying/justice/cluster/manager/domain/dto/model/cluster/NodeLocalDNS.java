package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeLocalDNS implements Serializable
{
    @ApiModelProperty(required = true, example = "aws")
    @SerializedName("ip_address")
    @JsonProperty("ip_address")
    private String                               ipAddress;
    
    @ApiModelProperty(required = true, example = "{}")
    @SerializedName("node_selector")
    @JsonProperty("node_selector")
    private Map<String,String>                nodeSelector;
    
    @ApiModelProperty(required = true, example = "{}")
    @SerializedName("update_strategy")
    @JsonProperty("update_strategy")
    private DaemonSetUpdateStrategy         updateStrategy;
    
    @ApiModelProperty(required = true, example = "aws")
    @SerializedName("node_local_dns_priority_class_name")
    @JsonProperty("node_local_dns_priority_class_name")
    private String       nodeLocalDnsPriorityClassName;
}
