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
public class LinearAutoscalerParams implements Serializable
{
    @ApiModelProperty(required = true, example = "10")
    @SerializedName("min")
    @JsonProperty("min")
    private int                               min;
    
    @ApiModelProperty(required = true, example = "20")
    @SerializedName("max")
    @JsonProperty("max")
    private int                               max;
    
    @ApiModelProperty(required = true, example = "10.3")
    @SerializedName("cores_per_replica")
    @JsonProperty("cores_per_replica")
    private  float               coresPerReplica;
    
    @ApiModelProperty(required = true, example = "20.9")
    @SerializedName("nodes_per_replica")
    @JsonProperty("nodes_per_replica")
    private  float               nodesPerReplica;
    
    @ApiModelProperty(required = true, example = "true")
    @SerializedName("prevent_single_point_failure")
    @JsonProperty("prevent_single_point_failure")
    private  boolean  preventSinglePointFailure;
}
