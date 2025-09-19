package com.lnjoying.justice.cluster.manager.domain.dto.model.admission;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Regulus
 * @Date: 12/15/21 10:53 AM
 * @Description:
 */
@Data
public class AdmissionPluginConfiguration implements Serializable
{
    @ApiModelProperty(example = "")
    @SerializedName("name")
    @JsonProperty("name")
    private String name;
    
    @ApiModelProperty(example = "")
    @SerializedName("path")
    @JsonProperty("path")
    private String path;
    
    @ApiModelProperty(example = "")
    @SerializedName("configuration")
    @JsonProperty("configuration")
    private RunTimeUnknow configuration;
}
