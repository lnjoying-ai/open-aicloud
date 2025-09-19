package com.lnjoying.justice.schema.entity.stat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RegionNodeMetrics
{
	@SerializedName("id")
	@JsonProperty("id")
	String id;

	@SerializedName("name")
	@JsonProperty("name")
	String name;

	@SerializedName("node_status_metrics")
	@JsonProperty("node_status_metrics")
	NodeStatusMetrics nodeStatusMetrics;
}
