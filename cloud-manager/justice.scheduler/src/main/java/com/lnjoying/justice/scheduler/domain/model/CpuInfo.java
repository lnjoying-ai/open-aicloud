package com.lnjoying.justice.scheduler.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CpuInfo implements Serializable
{
    @ApiModelProperty(example = "0")
    @SerializedName("cpu_num")
    @JsonProperty("cpu_num")
    private Integer cpuNum = 0;
    
    @ApiModelProperty(example = "")
    @SerializedName("cpu_model")
    @JsonProperty("cpu_model")
    private String cpuModel;
}
