package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.common.K8sDefaultValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class KubeletServiceInfo extends BaseService implements Serializable
{
	@ApiModelProperty(example = "false")
	@SerializedName("fail_swap_on")
	@JsonProperty("fail_swap_on")
	private Boolean                 failSwapOn;
	
	@ApiModelProperty(example = "domain")
	@SerializedName("cluster_domain")
	@JsonProperty("cluster_domain")
	private String               clusterDomain = K8sDefaultValue.DefaultClusterDomain;
	
	@ApiModelProperty(example = "dns-server")
	@SerializedName("cluster_dns_server")
	@JsonProperty("cluster_dns_server")
	private String           clusterDnsServer = K8sDefaultValue.DefaultClusterDNSService;
	
	@ApiModelProperty(example = "------xxxxxx-------")
	@SerializedName("generate_serving_certificate")
	@JsonProperty("generate_serving_certificate")
	private Boolean generateServingCertificate;
}
