package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.common.K8sDefaultValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class KubeControllerServiceInfo extends BaseService implements Serializable
{
	@ApiModelProperty(required = true, example = "10.42.0.0/16")
	@SerializedName("cluster_cidr")
	@JsonProperty("cluster_cidr")
	private String              clusterCidr = K8sDefaultValue.DefaultClusterCIDR;
	
	@ApiModelProperty(required = true, example = "10.43.0.0/16")
	@SerializedName("service_cluster_ip_range")
	@JsonProperty("service_cluster_ip_range")
	private String    serviceClusterIpRange = K8sDefaultValue.DefaultServiceClusterIPRange;
}
