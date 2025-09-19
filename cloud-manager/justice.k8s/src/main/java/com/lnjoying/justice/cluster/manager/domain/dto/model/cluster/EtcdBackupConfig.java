/**
 * desc etcd back strategy
 */
package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.common.K8sDefaultValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EtcdBackupConfig implements Serializable
{
	@ApiModelProperty(example = "true")
	@SerializedName("enable")
	@JsonProperty("enable")
	private Boolean          enable;
	
	@ApiModelProperty(example = "120")
	@SerializedName("time_out")
	@JsonProperty("time_out")
	private int           timeOut = K8sDefaultValue.DefaultEtcdBackupConfigTimeout;
	
	@ApiModelProperty(example = "12")
	@SerializedName("interval_hours")
	@JsonProperty("interval_hours")
	private int     intervalHours = K8sDefaultValue.DefaultEtcdBackupConfigIntervalHours;
	
//	@ApiModelProperty(example = "2")
//	@SerializedName("backup_replicas")
//	@JsonProperty("backup_replicas")
//	int    backupReplicas;
	
	@ApiModelProperty(example = "6")
	@SerializedName("retention")
	@JsonProperty("retention")
	private int retention = K8sDefaultValue.DefaultEtcdBackupConfigRetention;
}
