package com.lnjoying.justice.ecrm.domain.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CpuInfo
{
	@ApiModelProperty(example = "0")
	@SerializedName("cpu_num")
	@JsonProperty("cpu_num")
	private Integer cpuNum = 0;
	
	@ApiModelProperty(example = "")
	@SerializedName("cpu_model")
	@JsonProperty("cpu_model")
	private String cpuModel;
	
	@ApiModelProperty(example = "")
	@SerializedName("cpu_frequency")
	@JsonProperty("cpu_frequency")
	double	cpuFrequency;
	
	@ApiModelProperty(example = "")
	@SerializedName("cpu_logic_num")
	@JsonProperty("cpu_logic_num")
	int     cpuLogicNum;
	
	@ApiModelProperty(example = "")
	@SerializedName("cpu_physical_num")
	@JsonProperty("cpu_physical_num")
	int	 cpuPhysicalNum;
}
