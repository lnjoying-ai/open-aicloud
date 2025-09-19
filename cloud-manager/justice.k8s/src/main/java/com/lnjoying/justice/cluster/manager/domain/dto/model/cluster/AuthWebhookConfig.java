package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Regulus
 * @Date: 11/19/21 6:36 PM
 * @Description:
 */
@Data
public class AuthWebhookConfig implements Serializable
{
    @ApiModelProperty(example = "")
    @SerializedName("config_file")
    @JsonProperty("config_file")
    private String configFile;
    
    @ApiModelProperty(example = "20")
    @SerializedName("cache_timeout")
    @JsonProperty("cache_timeout")
    private String cacheTimeout;
}
