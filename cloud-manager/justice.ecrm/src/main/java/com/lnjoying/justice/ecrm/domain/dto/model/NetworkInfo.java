package com.lnjoying.justice.ecrm.domain.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NetworkInfo
{
	@ApiModelProperty(example = "")
	@SerializedName("mac")
	@JsonProperty("mac")
	String      mac;
	
	@ApiModelProperty(example = "")
	@SerializedName("ipv4")
	@JsonProperty("ipv4")
	String     ipv4;
	
	@ApiModelProperty(example = "")
	@SerializedName("ipv6")
	@JsonProperty("ipv6")
	String     ipv6;
	
	@ApiModelProperty(example = "")
	@SerializedName("nic_name")
	@JsonProperty("nic_name")
	String nicName;
}
