package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.common.K8sDefaultValue;
import com.lnjoying.justice.cluster.manager.domain.dto.model.admission.AdmissionConfiguration;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class KubeApiServiceInfo extends BaseService implements Serializable
{
	@ApiModelProperty(example = "true")
	@SerializedName("always_pull_images")
	@JsonProperty("always_pull_images")
	private Boolean        alwaysPullImages;
	
	@ApiModelProperty(example = "true")
	@SerializedName("pod_security_policy")
	@JsonProperty("pod_security_policy")
	private Boolean       podSecurityPolicy;
	
	@ApiModelProperty(example = "30000-41000")
	@SerializedName("service_node_port_range")
	@JsonProperty("service_node_port_range")
	private String    serviceNodePortRange = K8sDefaultValue.DefaultNodePortRange;
	
	@ApiModelProperty(example = "10.43.0.0/16")
	@SerializedName("service_cluster_ip_range")
	@JsonProperty("service_cluster_ip_range")
	private String   serviceClusterIpRange = K8sDefaultValue.DefaultServiceClusterIPRange;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("event_rate_limit")
	@JsonProperty("event_rate_limit")
	private EventRateLimit eventRateLimit;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("audit_log")
	@JsonProperty("audit_log")
	private AuditLog auditLog;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("secrets_encryption_config")
	@JsonProperty("secrets_encryption_config")
	private SecretsEncryptionConfig secretsEncryptionConfig;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("admission_configuration")
	@JsonProperty("admission_configuration")
	private AdmissionConfiguration admissionConfiguration;
}
