package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.common.K8sDefaultValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Regulus
 * @Date: 11/19/21 6:35 PM
 * @Description:
 */
@Data
public class Authentication implements Serializable
{
    @ApiModelProperty(example = "X509")
    @SerializedName("strategy")
    @JsonProperty("strategy")
    private String           strategy  = K8sDefaultValue.DefaultAuthStrategy;
    
    @ApiModelProperty(example = "[]")
    @SerializedName("sans")
    @JsonProperty("sans")
    private  List<String>         sans;
    
    @ApiModelProperty(example = "[]")
    @SerializedName("webhook")
    @JsonProperty("webhook")
    private AuthWebhookConfig webhook;
}
