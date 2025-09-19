package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberInfo
{
	@ApiModelProperty(example = "user id")
	@SerializedName("user_id")
	@JsonProperty("user_id")
	private String     userId;
	
	@ApiModelProperty(example = "user name")
	@SerializedName("user_name")
	@JsonProperty("user_name")
	private String	 userName;
	
	@ApiModelProperty(example = "access role")
	@SerializedName("access_role")
	@JsonProperty("access_role")
	private String accessRole;
}
