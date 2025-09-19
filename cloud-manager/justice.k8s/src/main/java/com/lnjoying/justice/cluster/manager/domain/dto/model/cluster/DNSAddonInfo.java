package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class DNSAddonInfo implements Serializable
{
    @ApiModelProperty(example = "aws")
    @SerializedName("provider")
    @JsonProperty("provider")
    private String                                 provider;
    
    @ApiModelProperty(example = "{\"key\":\"value\"}")
    @SerializedName("options")
    @JsonProperty("options")
    private Map<String,String>                      options;
    
    @ApiModelProperty(example = "{\"key\":\"value\"}")
    @SerializedName("node_local")
    @JsonProperty("node_local")
    private NodeLocalDNS nodeLocal;
    
    @ApiModelProperty(example = "[{\"key\":\"value\"}]")
    @SerializedName("tolerations")
    @JsonProperty("tolerations")
    private List<Toleration>                     tolerations;
    
    @ApiModelProperty(example = "[\"value1\",\"value2\"]")
    @SerializedName("stub_domains")
    @JsonProperty("stub_domains")
    private List<String>                         stubDomains;
    
    @ApiModelProperty(example = "[\"value1\",\"value2\"]")
    @SerializedName("reverse_cidrs")
    @JsonProperty("reverse_cidrs")
    private List<String>                        reverseCidrs;
    
    @ApiModelProperty(example = "{\"key\":\"value\"}")
    @SerializedName("node_selector")
    @JsonProperty("node_selector")
    private Map<String,String>                  nodeSelector;
    
    @ApiModelProperty(example = "{\"strategy\":\"RollingUpdate\"，...}")
    @SerializedName("update_strategy")
    @JsonProperty("update_strategy")
    private DeploymentStrategy                updateStrategy;
    
    @ApiModelProperty(example = "[]")
    @SerializedName("upstream_name_servers")
    @JsonProperty("upstream_name_servers")
    private List<String>                  upstreamNameServers;
    
    @ApiModelProperty(example = "{\"key\":\"value\"}")
    @SerializedName("linear_autoscaler_params")
    @JsonProperty("linear_autoscaler_params")
    private  LinearAutoscalerParams     linearAutoscalerParams;
}
