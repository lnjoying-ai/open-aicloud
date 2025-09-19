package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.common.K8sDefaultValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class IngressAddonInfo implements Serializable
{
    @ApiModelProperty(example = "nginx")
    @SerializedName("provider")
    @JsonProperty("provider")
    private String                                        provider = K8sDefaultValue.NginxIngressController;
    
    @ApiModelProperty(example = "{\"key\":\"value\"}")
    @SerializedName("options")
    @JsonProperty("options")
    private  Map<String,String>                             options;
    
    @ApiModelProperty(example = "8080")
    @SerializedName("http_port")
    @JsonProperty("http_port")
    private  int                                          httpPort;
    
    @ApiModelProperty(example = "8443")
    @SerializedName("https_port")
    @JsonProperty("https_port")
    private int                                         httpsPort;
    
    @ApiModelProperty(example = "Default")
    @SerializedName("dns_policy")
    @JsonProperty("dns_policy")
    private  String                                      dnsPolicy;
    
    @ApiModelProperty(example = "{\"key\",\"value\"}")
    @SerializedName("extra_args")
    @JsonProperty("extra_args")
    private  Map<String,String>                          extraArgs;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("extra_envs")
    @JsonProperty("extra_envs")
    private ObjectNode                                 extraEnvs;
    
    @ApiModelProperty(example = "[{},{}]")
    @SerializedName("tolerations")
    @JsonProperty("tolerations")
    private List<Toleration>                           tolerations;
    
    @ApiModelProperty(example = "bridge")
    @SerializedName("network_mode")
    @JsonProperty("network_mode")
    private String                                    networkMode;
    
    @ApiModelProperty(example = "{\"key\",\"value\"}")
    @SerializedName("node_selector")
    @JsonProperty("node_selector")
    private Map<String,String>                       nodeSelector;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("extra_volumes")
    @JsonProperty("extra_volumes")
    private ObjectNode                               extraVolumes;
    
    @ApiModelProperty(example = "{\"key\",\"value\"}")
    @SerializedName("update_strategy")
    @JsonProperty("update_strategy")
    private  DaemonSetUpdateStrategy                updateStrategy;
    
    @ApiModelProperty(example = "true")
    @SerializedName("default_backend")
    @JsonProperty("default_backend")
    private Boolean                                defaultBackend;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("extra_volume_mounts")
    @JsonProperty("extra_volume_mounts")
    private ObjectNode                          extraVolumeMounts;
    
    @ApiModelProperty(example = "")
    @SerializedName("default_http_backend_priority_class_name")
    @JsonProperty("default_http_backend_priority_class_name")
    private String        defaultHttpBackendPriorityClassName;
    
    @ApiModelProperty(example = "")
    @SerializedName("nginx_ingress_controller_priority_class_name")
    @JsonProperty("nginx_ingress_controller_priority_class_name")
    private  String    nginxIngressControllerPriorityClassName;
    
    @ApiModelProperty(example = "")
    @SerializedName("default_ingress_class")
    @JsonProperty("default_ingress_class")
    private Boolean defaultIngressClass;
}
