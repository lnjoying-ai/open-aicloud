package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

@Data
public class JkeConfig extends ClusterEngineConfig implements Serializable
{
	@NotBlank(message = "version is required")
	@ApiModelProperty(example = "v1.20.9")
	@SerializedName("k8s_version")
	@JsonProperty("k8s_version")
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_.]{3,20}$")
	private String                               k8sVersion;
	
	@ApiModelProperty(example = "300")
	@SerializedName("deploy_job_time")
	@JsonProperty("deploy_job_time")
	private int            	                     deployJobTime = 0;
	
	@ApiModelProperty(example = "v1.20.8")
	@SerializedName("enable_alerting")
	@JsonProperty("enable_alerting")
	private boolean                              enableAlerting;
	
	@ApiModelProperty(example = "false")
	@SerializedName("enable_monitoring")
	@JsonProperty("enable_monitoring")
	private boolean                              enableMonitoring;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("authentication")
	@JsonProperty("authentication")
	private Authentication	                     authentication;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("authorization")
	@JsonProperty("authorization")
	private Authorization	                     authorization;
	
	@ApiModelProperty(example = "18.06.1-ce")
	@SerializedName("docker_version")
	@JsonProperty("docker_version")
	private String                               dockerVersion;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("cloud_provider")
	@JsonProperty("cloud_provider")
	private CloudProvider                        cloudProvider;
	
	@ApiModelProperty(example = "[{},{}]")
	@SerializedName("registries")
	@JsonProperty("registries")
	private List<RegistryInfo>                   registries;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("system_addons")
	@JsonProperty("system_addons")
	private SystemAddonsInfo systemAddons;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("apps")
	@JsonProperty("apps")
	private ObjectNode                                    apps;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("services")
	@JsonProperty("services")
	private K8sServiceInfo                            services;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("upgrade_strategy")
	@JsonProperty("upgrade_strategy")
	private NodeUpgradeStrategy                    upgradeStrategy;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("local_cluster_auth_endpoint")
	@JsonProperty("local_cluster_auth_endpoint")
	private LocalClusterAuthEndPoint  localClusterAuthEndPoint;
	
	@JsonIgnore
	private EncryptionConfig encryptionConfig;
	
	//"dind","Deploy Kubernetes cluster in docker containers (experimental)"
	@JsonIgnore
	private Boolean dind = true;
	
	@JsonProperty("cri_dockerd_enabled")
	private Boolean CRIDockerdEnabled = false;
}
