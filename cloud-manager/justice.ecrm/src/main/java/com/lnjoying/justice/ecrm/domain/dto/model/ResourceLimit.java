package com.lnjoying.justice.ecrm.domain.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceLimit
{
	@ApiModelProperty(example = "")
	@SerializedName("mem_limit")
	@JsonProperty("mem_limit")
	Long  memLimit;
	
	
	@ApiModelProperty(example = "")
	@SerializedName("cpu_limit")
	@JsonProperty("cpu_limit")
	Integer   cpuLimit;
	
	@ApiModelProperty(example = "")
	@SerializedName("disk_limit")
	@JsonProperty("disk_limit")
	Long diskLimit;
}
