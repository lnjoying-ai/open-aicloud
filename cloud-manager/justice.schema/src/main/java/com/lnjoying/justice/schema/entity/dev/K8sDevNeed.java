package com.lnjoying.justice.schema.entity.dev;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class K8sDevNeed
{
	@NotNull(message = "role is required")
	@ApiModelProperty(example = "controller")
	@SerializedName("cluster_roles")
	@JsonProperty("cluster_roles")
	private String                               roles;
	
	@ApiModelProperty(example = "11")
	@SerializedName("num")
	@JsonProperty("num")
	private int                                    num;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("dev_spec")
	@JsonProperty("dev_spec")
	private DevNeedInfo                           spec;
}
