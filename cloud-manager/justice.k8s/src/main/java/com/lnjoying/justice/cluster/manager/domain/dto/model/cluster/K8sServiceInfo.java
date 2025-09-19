package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class K8sServiceInfo implements Serializable
{
	public K8sServiceInfo ()
	{
		etcdServiceInfo = new EtcdServiceInfo();
		kubeApiService = new KubeApiServiceInfo();
		kubeletServiceInfo = new KubeletServiceInfo();
		kubeControllerService = new KubeControllerServiceInfo();
		kubeSchedulerServiceInfo = new KubeSchedulerServiceInfo();
		kubeProxyServiceInfo = new KubeProxyServiceInfo();
	}
	
	@ApiModelProperty(example = "{}")
	@SerializedName("etcd")
	@JsonProperty("etcd")
	private EtcdServiceInfo   etcdServiceInfo;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("kube_apiserver")
	@JsonProperty("kube_apiserver")
	private KubeApiServiceInfo kubeApiService;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("kubelet")
	@JsonProperty("kubelet")
	private KubeletServiceInfo kubeletServiceInfo;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("kube_controller")
	@JsonProperty("kube_controller")
	private KubeControllerServiceInfo kubeControllerService;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("kube_scheduler")
	@JsonProperty("kube_scheduler")
	private KubeSchedulerServiceInfo kubeSchedulerServiceInfo;
	
	@ApiModelProperty(example = "{}")
	@SerializedName("kube_proxy")
	@JsonProperty("kube_proxy")
	private KubeProxyServiceInfo kubeProxyServiceInfo;
}
