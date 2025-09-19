package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class LocalClusterAuthEndPoint implements Serializable
{
	@ApiModelProperty(required = true, example = "access url")
	@SerializedName("fqdn")
	@JsonProperty("fqdn")
	String              fqdn;
	
	@ApiModelProperty(required = true, example = "------xxxxxxxxxx--------")
	@SerializedName("cacert")
	@JsonProperty("cacert")
	String            cacert;
	
	@ApiModelProperty(required = true, example = "false")
	@SerializedName("enable")
	@JsonProperty("enable")
	private boolean           enable;
}
