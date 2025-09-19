package com.lnjoying.justice.ecrm.domain.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SoftwareVersion
{
	@ApiModelProperty(example = "")
	@SerializedName("name")
	@JsonProperty("name")
	String    name;
	
	@ApiModelProperty(example = "")
	@SerializedName("version")
	@JsonProperty("version")
	String version;
}
