package com.lnjoying.justice.ecrm.domain.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MachineInfo
{
	@ApiModelProperty(example = "")
	@SerializedName("vender")
	@JsonProperty("vender")
	String       vender;
	
	@ApiModelProperty(example = "")
	@SerializedName("uuid")
	@JsonProperty("uuid")
	String         uuid;
	
	@ApiModelProperty(example = "")
	@SerializedName("product")
	@JsonProperty("product")
	String      product;
	
	@ApiModelProperty(example = "")
	@SerializedName("sn")
	@JsonProperty("sn")
	String           sn;
	
	@ApiModelProperty(example = "")
	@SerializedName("hostname")
	@JsonProperty("hostname")
	String     hostname;
	
	@ApiModelProperty(example = "")
	@SerializedName("os")
	@JsonProperty("os")
	String           os;
	
	@ApiModelProperty(example = "")
	@SerializedName("core_version")
	@JsonProperty("core_version")
	String coreVersion;
}
