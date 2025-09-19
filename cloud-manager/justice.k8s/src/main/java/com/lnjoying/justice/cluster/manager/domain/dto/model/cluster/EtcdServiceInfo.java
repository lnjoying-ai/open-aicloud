package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.common.K8sDefaultValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EtcdServiceInfo extends BaseService implements Serializable
{
	@ApiModelProperty(example = "{\"key\":\"value\"}")
	@SerializedName("backup_config")
	@JsonProperty("backup_config")
	private EtcdBackupConfig backupConfig;
	
	@ApiModelProperty(example = "{\"key\":\"value\"}")
	@SerializedName("external_config")
	@JsonProperty("external_config")
	private ExternalEtcdConfig externalConfig;
	
	@ApiModelProperty(example = "true")
	@SerializedName("snap_short")
	@JsonProperty("snap_short")
	private Boolean snapShort  = K8sDefaultValue.DefaultEtcdSnapshot;
	
	@ApiModelProperty(example = "12h")
	@SerializedName("creation")
	@JsonProperty("creation")
	private String  creation   = K8sDefaultValue.DefaultEtcdBackupCreationPeriod;
	
	@ApiModelProperty(example = "72h")
	@SerializedName("retention")
	@JsonProperty("retention")
	private String  retention  = K8sDefaultValue.DefaultEtcdBackupRetentionPeriod;
}
