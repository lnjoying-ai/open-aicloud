package com.lnjoying.justice.schema.entity.dev;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DiskNeed implements Serializable
{
    @ApiModelProperty(example = "HDD")
    @SerializedName("disk_type")
    @JsonProperty("disk_type")
    private String diskType;
    
    @ApiModelProperty(example = "0")
    @SerializedName("disk_limit")
    @JsonProperty("disk_limit")
    private Integer diskLimit=0;
}
