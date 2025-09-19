package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.domain.dto.model.secrect.ProviderConfiguration;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Regulus
 * @Date: 12/14/21 3:33 PM
 * @Description:
 */
@Data
public class ResourceConfiguration implements Serializable
{
    @ApiModelProperty(example = "[]")
    @SerializedName("resources")
    @JsonProperty("resources")
    private List<String> resources;
    
    @ApiModelProperty(example = "[]")
    @SerializedName("providers")
    @JsonProperty("providers")
    private List<ProviderConfiguration> providers;
}
