package com.lnjoying.justice.schema.entity.dev;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class TargetNode implements Serializable
{
	public TargetNode()
	{
	
	}
	public TargetNode(String regionId, String siteId, String nodeId)
	{
		dstNodeId   = nodeId;
		dstRegionId = regionId;
		dstSiteId   = siteId;
	}
	
	@ApiModelProperty(required = true, example = "")
	@SerializedName("dst_region_id")
	@JsonProperty("dst_region_id")
	String dstRegionId;
	
	@ApiModelProperty(required = true, example = "")
	@SerializedName("dst_site_id")
	@JsonProperty("dst_site_id")
	String dstSiteId;
	
	@ApiModelProperty(required = true, example = "")
	@SerializedName("dst_node_id")
	@JsonProperty("dst_node_id")
	String dstNodeId;
}
