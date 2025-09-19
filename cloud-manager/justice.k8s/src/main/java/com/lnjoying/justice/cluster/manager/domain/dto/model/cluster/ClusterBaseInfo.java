package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;
/**
 * desc cluster basic info
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.common.ClusterCreateType;
import com.lnjoying.justice.cluster.manager.config.ServiceConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class ClusterBaseInfo implements Serializable
{
    @ApiModelProperty(required = true, example = "cluster-name")
    @Pattern(regexp = ServiceConfig.PATTERN_NAME)
    @SerializedName("name")
    @JsonProperty("name")
    private String                     name;
    
    @ApiModelProperty(example = "cluster-desc")
    @SerializedName("desc")
    @JsonProperty("desc")
    private String 					   description;
    
    @ApiModelProperty(example = "business-partner")
    @SerializedName("bp")
    @JsonProperty("bp")
    private  String                       bp;
    
    @ApiModelProperty(example = "cluster-owner")
    @SerializedName("owner")
    @JsonProperty("owner")
    private String                    owner;
    
    @ApiModelProperty(example = "custom")
    @SerializedName("create_type")
    @JsonProperty("create_type")
    private String                    createType = ClusterCreateType.CUSTOM;
    
    @ApiModelProperty(required = true, example = "k8s")
    @SerializedName("cluster_type")
    @JsonProperty("cluster_type")
    private String                    clusterType;
    
    
    @ApiModelProperty(example = "k8s-export-templates")
    @SerializedName("tmpl_ver_id")
    @JsonProperty("tmpl_ver_id")
    private String                   tmplVerId;
    
    @Valid
    @ApiModelProperty(example = "{\"k8s_version\":\"v1.27\"}")
    @SerializedName("jke_config")
    @JsonProperty("jke_config")
    private JkeConfig               jkeConfig;

    @Valid
    @ApiModelProperty(example = "{\"k8s_version\":\"v1.27\"}")
    @SerializedName("k3s_config")
    @JsonProperty("k3s_config")
    private K3sConfig               k3sConfig;
    
    @ApiModelProperty(example = "cluster-shared-members")
    @SerializedName("members")
    @JsonProperty("members")
    private List<MemberInfo>        members;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("labels")
    @JsonProperty("labels")
    private Map<String,String>     labels;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("annotations")
    @JsonProperty("annotations")
    private  Map<String,String>    annotations;
}
