package com.lnjoying.justice.schema.entity.dev;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class GpuNeed implements Serializable
{
    @ApiModelProperty(example = "")
    @SerializedName("gpu_type")
    @JsonProperty("gpu_type")
    private String gpuType;
    
    @ApiModelProperty(example = "")
    @SerializedName("gpu_model")
    @JsonProperty("gpu_model")
    private String gpuModel;
    
    @ApiModelProperty(example = "")
    @SerializedName("gpu_num")
    @JsonProperty("gpu_num")
    private Integer gpuNum;
    
    @ApiModelProperty(example = "")
    @SerializedName("driver_capabilities")
    @JsonProperty("driver_capabilities")
    private String driverCapabilities;
    
    @ApiModelProperty(example = "")
    @SerializedName("driver_version")
    @JsonProperty("driver_version")
    private String driverVersion;
    
    @ApiModelProperty(example = "")
    @SerializedName("cuda_version")
    @JsonProperty("cuda_version")
    private String cudaVersion;
    
    @ApiModelProperty(example = "")
    @SerializedName("cudnn_version")
    @JsonProperty("cudnn_version")
    private String cudnnVersion;
}
