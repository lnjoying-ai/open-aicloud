package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ExternalEtcdConfig implements Serializable
{
	@ApiModelProperty(example = "etcd_svr1")
	@SerializedName("key")
	@JsonProperty("key")
	private String            key;
	
	@ApiModelProperty(example = "[\"url1\", \"url2\"]")
	@SerializedName("urls")
	@JsonProperty("urls")
	private List<String>     urls;
	
	@ApiModelProperty(example = "-------xxxx--------")
	@SerializedName("ca_cert")
	@JsonProperty("ca_cert")
	private String        caCert;
	
	@ApiModelProperty(example = "-------xxxx--------")
	@SerializedName("client_cert")
	@JsonProperty("client_cert")
	private String    clientCert;
}
