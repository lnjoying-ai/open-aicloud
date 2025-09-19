package com.lnjoying.justice.omc.domain.model.stat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class NetworkMetrics
{
	public NetworkMetrics()
	{
		transmit = new ArrayList<>();
		recv = new ArrayList<>();
	}

	@SerializedName("transmit")
	@JsonProperty("transmit")
	List<Map<String, Float>> transmit;

	@SerializedName("recv")
	@JsonProperty("recv")
	List<Map<String, Float>> recv;
}
