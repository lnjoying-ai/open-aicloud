package com.lnjoying.justice.ecrm.domain.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DiskInfo
{
	@ApiModelProperty(example = "0")
	@SerializedName("disk_total")
	@JsonProperty("disk_total")
	long          diskTotal;
	
	@ApiModelProperty(example = "HDD")
	@SerializedName("disk_types")
	@JsonProperty("disk_types")
	String        diskTypes;
	
	@ApiModelProperty(example = "")
	@SerializedName("disks")
	@JsonProperty("disks")
	List<DiskPartInfo> disks;
}
