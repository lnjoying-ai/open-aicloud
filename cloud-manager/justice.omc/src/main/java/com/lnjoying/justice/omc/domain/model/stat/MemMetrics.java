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
public class MemMetrics
{
	public MemMetrics()
	{
		utilisations = new ArrayList<>();
		usages = new ArrayList<>();
	}

	public MemMetrics(long memTotal)
	{
		this();
		this.memTotal = memTotal;
	}

	@SerializedName("mem_total")
	@JsonProperty("mem_total")
	long memTotal;

	@SerializedName("utilisations")
	@JsonProperty("utilisations")
	List<Map<String, Float>> utilisations;

	@SerializedName("usages")
	@JsonProperty("usages")
	List<Map<String, Long>> usages;
}
