package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: Regulus
 * @Date: 11/22/21 4:26 PM
 * @Description:
 */
@Data
public class BaseService implements Serializable
{
    @ApiModelProperty(example = "{\"key\":\"value\"}")
    @SerializedName("extra_args")
    @JsonProperty("extra_args")
    private Map<String,String> extraArgs;
    
    // Extra binds added to the nodes
    @ApiModelProperty(example = "{\"key\":\"value\"}")
    @SerializedName("extra_binds")
    @JsonProperty("extra_binds")
    private List<String> extraBinds;
    
    // this is to provide extra env variable to the docker container running kubernetes service
    @ApiModelProperty(example = "[\"value\"]")
    @SerializedName("extra_envs")
    @JsonProperty("extra_envs")
    private List<String>    extraEnvs;
}
