package com.lnjoying.justice.ecrm.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemInfo
{
	@ApiModelProperty(example = "0")
	@SerializedName("mem_total")
	@JsonProperty("mem_total")
	long memTotal;
}
