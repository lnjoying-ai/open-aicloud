package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeDrainInput implements Serializable
{
    @ApiModelProperty(example = "true")
    @SerializedName("force")
    @JsonProperty("force")
    private boolean                    force;
    
    @ApiModelProperty(example = "100")
    @SerializedName("timeout")
    @JsonProperty("timeout")
    private int                      timeout;
    
    @ApiModelProperty(example = "30")
    @SerializedName("grace_period")
    @JsonProperty("grace_period")
    private int                 gracePeriod;
    
    @ApiModelProperty(example = "true")
    @SerializedName("delete_local_data")
    @JsonProperty("delete_local_data")
    private boolean        deleteLocalData;
    
    @ApiModelProperty(example = "true")
    @SerializedName("ignore_daemonsets")
    @JsonProperty("ignore_daemonsets")
    private boolean        ignoreDaemonsets;
}
