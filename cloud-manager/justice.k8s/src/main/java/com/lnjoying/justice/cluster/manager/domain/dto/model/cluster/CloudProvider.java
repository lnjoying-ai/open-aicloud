package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

import java.io.Serializable;

@Data
public class CloudProvider implements Serializable
{
    @ApiModelProperty(example = "aws")
    @SerializedName("provider")
    @JsonProperty("provider")
    private String provider;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("cloud_args")
    @JsonProperty("cloud_args")
    private ObjectNode cloudArgs;
    
    @Ignore
    private String cloudConfigFile;
}
