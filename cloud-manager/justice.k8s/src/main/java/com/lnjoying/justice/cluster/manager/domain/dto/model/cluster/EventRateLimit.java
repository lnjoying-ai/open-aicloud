package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Regulus
 * @Date: 11/19/21 8:14 PM
 * @Description:
 */
@Data
public class EventRateLimit implements Serializable
{
    @ApiModelProperty(example = "false")
    @SerializedName("enabled")
    @JsonProperty("enabled")
    private Boolean enabled;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("configuration")
    @JsonProperty("configuration")
    private Configuration configuration;
}
