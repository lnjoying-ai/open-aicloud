package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Regulus
 * @Date: 12/9/21 3:07 PM
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Limit implements Serializable
{
    @ApiModelProperty(example = "")
    @SerializedName("type")
    @JsonProperty("type")
    private  String type;
    
    @ApiModelProperty(example = "")
    @SerializedName("qps")
    @JsonProperty("qps")
    private int qps;
    
    @ApiModelProperty(example = "")
    @SerializedName("burst")
    @JsonProperty("burst")
    private int burst;
    
    @ApiModelProperty(example = "")
    @SerializedName("cache_size")
    @JsonProperty("cache_size")
    private int cacheSize;
}
