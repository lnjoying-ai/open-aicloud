package com.lnjoying.justice.cluster.manager.domain.dto.model.secrect;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Regulus
 * @Date: 12/14/21 3:40 PM
 * @Description:
 */
@Data
public class KMSConfiguration implements Serializable
{
    @ApiModelProperty(example = "")
    @SerializedName("name")
    @JsonProperty("name")
    private String name;
    
    @ApiModelProperty(example = "")
    @SerializedName("cachesize")
    @JsonProperty("cachesize")
    int cachesize;
    
    @ApiModelProperty(example = "")
    @SerializedName("endpoint")
    @JsonProperty("endpoint")
    private String endpoint;
    
    @ApiModelProperty(example = "")
    @SerializedName("duration")
    @JsonProperty("duration")
    private long duration;
}
