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
public class DiskMetrics
{
	public DiskMetrics()
	{
		utilisations = new ArrayList<>();
		usages = new ArrayList<>();
	}

	public DiskMetrics(long diskTotal)
	{
		this();
		this.diskTotal = diskTotal;
	}

	@SerializedName("disk_total")
	@JsonProperty("disk_total")
	long diskTotal;

	@SerializedName("utilisations")
	@JsonProperty("utilisations")
	List<Map<String, Float>> utilisations;

	@SerializedName("usages")
	@JsonProperty("usages")
	List<Map<String, Long>> usages;

	@SerializedName("disk_reads")
	@JsonProperty("disk_reads")
	List<Map<String, Float>> diskReads;


	@SerializedName("diskWrites")
	@JsonProperty("diskWrites")
	List<Map<String, Float>> diskWrites;
}
