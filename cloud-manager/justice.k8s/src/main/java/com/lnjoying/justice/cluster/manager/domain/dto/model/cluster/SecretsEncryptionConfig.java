package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Regulus
 * @Date: 12/14/21 3:30 PM
 * @Description:
 */
@Data
public class SecretsEncryptionConfig implements Serializable
{
    @ApiModelProperty(example = "false")
    @SerializedName("enable")
    @JsonProperty("enable")
    private Boolean enabled;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("custom_config")
    @JsonProperty("custom_config")
    private EncryptionConfiguration customConfig;
}
