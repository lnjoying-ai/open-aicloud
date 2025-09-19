package com.lnjoying.justice.ecrm.domain.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DiskPartInfo
{
	@ApiModelProperty(example = "")
	@SerializedName("disk_name")
	@JsonProperty("disk_name")
	String diskName;
	
	@ApiModelProperty(example = "")
	@SerializedName("disk_total")
	@JsonProperty("disk_total")
	long  diskTotal;
	
	@ApiModelProperty(example = "")
	@SerializedName("disk_type")
	@JsonProperty("disk_type")
	String diskType;
	
	@ApiModelProperty(example = "")
	@SerializedName("mountpoint")
	@JsonProperty("mountpoint")
	String mountpoint;
}
