package com.lnjoying.justice.ecrm.domain.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GpuInfo
{
	@ApiModelProperty(example = "")
	@SerializedName("gpu_id")
	@JsonProperty("gpu_id")
	String         gpuId;
	
	@ApiModelProperty(example = "")
	@SerializedName("gpu_type")
	@JsonProperty("gpu_type")
	String       gpuType;
	
	@ApiModelProperty(example = "")
	@SerializedName("gpu_model")
	@JsonProperty("gpu_model")
	String	    gpuModel;
	
	@ApiModelProperty(example = "")
	@SerializedName("driver_version")
	@JsonProperty("driver_version")
	String driverVersion;
	
	@ApiModelProperty(example = "")
	@SerializedName("cuda_version")
	@JsonProperty("cuda_version")
	String   cudaVersion;
	
	@ApiModelProperty(example = "")
	@SerializedName("cudnn_version")
	@JsonProperty("cudnn_version")
	String  cudnnVersion;
	int             index;
}
