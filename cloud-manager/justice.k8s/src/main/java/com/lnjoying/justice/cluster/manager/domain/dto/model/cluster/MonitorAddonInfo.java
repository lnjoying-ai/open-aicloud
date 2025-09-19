package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.common.K8sDefaultValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class MonitorAddonInfo implements Serializable
{
    @ApiModelProperty(required = true, example = "metrics-server")
    @SerializedName("provider")
    @JsonProperty("provider")
    private String                           provider = K8sDefaultValue.DefaultMonitoringProvider;
    
    @ApiModelProperty(required = true, example = "{}")
    @SerializedName("options")
    @JsonProperty("options")
    private Map<String, String> options;
    
    @ApiModelProperty(required = true, example = "2")
    @SerializedName("replicas")
    @JsonProperty("replicas")
    private int                              replicas = 1;
    
    @ApiModelProperty(required = true, example = "[{},{}]")
    @SerializedName("tolerations")
    @JsonProperty("tolerations")
    private List<Toleration> tolerations;
    
    @ApiModelProperty(required = true, example = "{}")
    @SerializedName("node_selector")
    @JsonProperty("node_selector")
    private Map<String,String>          nodeSelector;
    
    @ApiModelProperty(required = true, example = "{}")
    @SerializedName("update_strategy")
    @JsonProperty("update_strategy")
    private DeploymentStrategy        updateStrategy;
    
    @ApiModelProperty(required = true, example = "")
    @SerializedName("metrics_server_priority_class_name")
    @JsonProperty("metrics_server_priority_class_name")
    private String    metricsServerPriorityClassName;
}
