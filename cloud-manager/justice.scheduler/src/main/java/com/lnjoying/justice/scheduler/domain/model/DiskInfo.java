package com.lnjoying.justice.scheduler.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DiskInfo implements Serializable
{
    @ApiModelProperty(example = "HDD")
    @SerializedName("disk_type")
    @JsonProperty("disk_type")
    private String diskType;
    
    @ApiModelProperty(example = "0")
    @SerializedName("disk_limit")
    @JsonProperty("disk_limit")
    private Long diskLimit;
}
