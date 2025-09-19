package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.domain.model.AciNetworkProvider;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class NetworkAddonInfo implements Serializable
{
    @ApiModelProperty(example = "calico")
    @SerializedName("provider")
    @JsonProperty("provider")
    private  String                                provider;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("options")
    @JsonProperty("options")
    private Map<String, String>                   options;
    
    
    @ApiModelProperty(example = "{}")
    @SerializedName("node_selector")
    @JsonProperty("node_selector")
    private Map<String, String>                   nodeSelector;
    
    @ApiModelProperty(example = "[{},{}]")
    @SerializedName("tolerations")
    @JsonProperty("tolerations")
    private List<Toleration> tolerations;
    
    @ApiModelProperty(example = "1450")
    @SerializedName("mtu")
    @JsonProperty("mtu")
    private  int                                       mtu;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("aci_network_provider")
    @JsonProperty("aci_network_provider")
    private AciNetworkProvider aciNetworkProvider;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("calico_network_provider")
    @JsonProperty("calico_network_provider")
    private CalicoNetworkProvider calicoNetworkProvider;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("canal_network_provider")
    @JsonProperty("canal_network_provider")
    private CanalNetworkProvider     canalNetworkProvider;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("flannel_network_provider")
    @JsonProperty("flannel_network_provider")
    private  FlannelNetworkProvider  flannelNetworkProvider;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("weave_network_provider")
    @JsonProperty("weave_network_provider")
    private WeaveNetworkProvider weaveNetworkProvider;
    
    @ApiModelProperty(example = "{\"key\",\"value\"}")
    @SerializedName("update_strategy")
    @JsonProperty("update_strategy")
    private DaemonSetUpdateStrategy                updateStrategy;
}
