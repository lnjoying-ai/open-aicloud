package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.common.K8sDefaultValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: Regulus
 * @Date: 11/22/21 8:04 PM
 * @Description:
 */
@Data
public class Authorization implements Serializable
{
    @ApiModelProperty(example = "rbac")
    @JsonProperty("mode")
    @SerializedName("mode")
    private String                mode = K8sDefaultValue.RBACAuthorizationMode;
//    Mode string `yaml:"mode" json:"mode,omitempty"`
    // Authorization mode options
    @ApiModelProperty(example = "{\"key\":\"value\"}")
    @JsonProperty("options")
    @SerializedName("options")
    private Map<String,String> options;
}
