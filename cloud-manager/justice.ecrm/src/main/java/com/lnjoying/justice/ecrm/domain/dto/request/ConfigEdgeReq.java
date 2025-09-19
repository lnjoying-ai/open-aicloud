package com.lnjoying.justice.ecrm.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.ecrm.domain.dto.model.ResourceLimit;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigEdgeReq
{
	@ApiModelProperty(example = "")
	@SerializedName("resource_limit")
	@JsonProperty("resource_limit")
	ResourceLimit resourceLimit;
	
	@ApiModelProperty(example = "")
	@SerializedName("labels")
	@JsonProperty("labels")
	Map<String, String>   labels;
	
	@ApiModelProperty(example = "")
	@SerializedName("taints")
	@JsonProperty("taints")
	Map<String, String>   taints;

	@ApiModelProperty(example = "")
	@SerializedName("region_id")
	@JsonProperty("region_id")
	String regionId;

	@ApiModelProperty(example = "")
	@SerializedName("site_id")
	@JsonProperty("site_id")
	String siteId;

	@ApiModelProperty(example = "")
	@SerializedName("node_name")
	@JsonProperty("node_name")
	String nodeName;
}
