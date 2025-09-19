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
public class GpuMetrics
{
	public GpuMetrics()
	{
		utilisations = new ArrayList<>();
		usages = new ArrayList<>();
	}

	public GpuMetrics(int gpuTotal)
	{
		this();
		this.gpuTotal = gpuTotal;
	}

	@SerializedName("gpu_total")
	@JsonProperty("gpu_total")
	int gpuTotal;

	@SerializedName("utilisations")
	@JsonProperty("utilisations")
	List<Map<String, Float>> utilisations;

	@SerializedName("usages")
	@JsonProperty("usages")
	List<Map<String, Float>> usages;
}
