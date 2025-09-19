package com.lnjoying.justice.schema.entity.dev;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MemNeed implements Serializable
{
    @ApiModelProperty(example = "0")
    @SerializedName("mem_limit")
    @JsonProperty("mem_limit")
    private Integer memLimit = 0;
}
