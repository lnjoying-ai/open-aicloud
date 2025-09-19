package com.lnjoying.justice.ecrm.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.ecrm.domain.dto.model.ServiceAgent;
import lombok.Data;

import java.util.List;

@Data
public class GetServiceAgentsRsp
{
	@SerializedName("total_num")
	@JsonProperty("total_num")
	private long totalNum;

	@SerializedName("service_agents")
	@JsonProperty("service_agents")
	List<ServiceAgent> serviceAgents;
}
