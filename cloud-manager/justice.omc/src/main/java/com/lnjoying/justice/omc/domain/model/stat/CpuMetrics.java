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
public class CpuMetrics
{
	public CpuMetrics()
	{
		utilisations = new ArrayList<Map<String, Float>>();
		usages = new ArrayList<Map<String, Float>>();
	}

	public CpuMetrics(int cpuTotal)
	{
		this();
		this.cpuTotal = cpuTotal;
	}

	@SerializedName("cpu_total")
	@JsonProperty("cpu_total")
	private int cpuTotal;

	@SerializedName("utilisations")
	@JsonProperty("utilisations")
	private List<Map<String, Float>> utilisations;

	@SerializedName("usages")
	@JsonProperty("usages")
	private List<Map<String, Float>> usages;
}
