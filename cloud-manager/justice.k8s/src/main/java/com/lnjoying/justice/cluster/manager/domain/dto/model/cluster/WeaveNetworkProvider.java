package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WeaveNetworkProvider implements Serializable
{
    @ApiModelProperty(example = "password")
    @SerializedName("password")
    @JsonProperty("password")
    private String               password;
}
