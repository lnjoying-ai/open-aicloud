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
public class Toleration implements Serializable
{
    @ApiModelProperty(example = "key")
    @SerializedName("key")
    @JsonProperty("key")
    private String                    key;
    
    @ApiModelProperty(example = "value")
    @SerializedName("value")
    @JsonProperty("value")
    private String                  value;
    
    @ApiModelProperty(example = "NoSchedule")
    @SerializedName("effect")
    @JsonProperty("effect")
    private String                 effect;
    
    @ApiModelProperty(example = "Equal")
    @SerializedName("operator")
    @JsonProperty("operator")
    private String               operator;
    
    @ApiModelProperty(example = "300")
    @SerializedName("toleration_seconds")
    @JsonProperty("toleration_seconds")
    private long       tolerationSeconds;
}
