package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SystemAddonsInfo implements Serializable
{
    @ApiModelProperty(example = "{}")
    @SerializedName("dns")
    @JsonProperty("dns")
    private DNSAddonInfo dns;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("ingress")
    @JsonProperty("ingress")
    private IngressAddonInfo ingress;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("monitor")
    @JsonProperty("monitor")
    private MonitorAddonInfo monitor;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("network")
    @JsonProperty("network")
    private NetworkAddonInfo network;
}
