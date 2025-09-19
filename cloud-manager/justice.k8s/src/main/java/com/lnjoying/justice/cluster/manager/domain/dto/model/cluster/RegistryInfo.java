package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.schema.entity.dev.Registry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class RegistryInfo extends Registry implements Serializable
{
    //public private
    @ApiModelProperty(example = "public")
    @SerializedName("type")
    @JsonProperty("type")
    private String                           type;
}
