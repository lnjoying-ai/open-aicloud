package com.lnjoying.justice.schema.entity.stat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class NodeStatusMetrics
{
	@SerializedName("active_num")
	@JsonProperty("active_num")
	Integer activeNum;

	@SerializedName("inactive_num")
	@JsonProperty("inactive_num")
	Integer inactiveNum;

	@SerializedName("upgrade_num")
	@JsonProperty("upgrade_num")
	Integer upgradeNum;

	@SerializedName("online_num")
	@JsonProperty("online_num")
	Integer onlineNum;

	@SerializedName("offline_num")
	@JsonProperty("offline_num")
	Integer offlineNum;

	@SerializedName("core_num")
	@JsonProperty("core_num")
	Integer coreNum;
}
