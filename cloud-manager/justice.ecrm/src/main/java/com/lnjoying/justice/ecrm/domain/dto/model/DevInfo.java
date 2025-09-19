package com.lnjoying.justice.ecrm.domain.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.ecrm.domain.MemInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DevInfo
{
	@ApiModelProperty(example = "")
	@SerializedName("machine_info")
	@JsonProperty("machine_info")
	MachineInfo            machineInfo;
	
	@ApiModelProperty(example = "")
	@SerializedName("cpu_info")
	@JsonProperty("cpu_info")
	CpuInfo		               cpuInfo;
	
	@ApiModelProperty(example = "")
	@SerializedName("mem_info")
	@JsonProperty("mem_info")
	MemInfo                    memInfo;
	
	@ApiModelProperty(example = "")
	@SerializedName("disk_info")
	@JsonProperty("disk_info")
	DiskInfo                  diskInfo;
	
	@ApiModelProperty(example = "")
	@SerializedName("software_info")
	@JsonProperty("software_info")
	List<SoftwareVersion> softwareInfo;

	@ApiModelProperty(example = "")
	@SerializedName("software_map")
	@JsonProperty("software_map")
	Map<String, String> softwareMap;
	
	@ApiModelProperty(example = "")
	@SerializedName("network_info")
	@JsonProperty("network_info")
	List<NetworkInfo>      networkInfo;
	
	@ApiModelProperty(example = "")
	@SerializedName("gpu_info")
	@JsonProperty("gpu_info")
	List<GpuInfo>		       gpuInfo;
}
