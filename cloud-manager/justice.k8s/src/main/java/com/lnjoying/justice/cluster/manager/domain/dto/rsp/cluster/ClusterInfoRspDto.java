package com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.common.ClusterStatus;
import com.lnjoying.justice.cluster.manager.common.ClusterType;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterInfo;
import com.lnjoying.justice.cluster.manager.domain.dto.model.K8sStatusCode;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.Cluster2Template;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.ClusterBaseInfo;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.JkeConfig;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.K3sConfig;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.micro.core.common.Utils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ApiModel(value = "GetClusterRsp")
@Slf4j
public class ClusterInfoRspDto extends ClusterBaseInfo
{
    @ApiModelProperty(required = true, example = "cluster_id")
    @SerializedName("id")
    @JsonProperty("id")
    private String                       id;
    
    @ApiModelProperty(example = "cluster-token")
    @SerializedName("cluster_token")
    @JsonProperty("cluster_token")
    private String             clusterToken;
    
    @ApiModelProperty(example = "k8s-export-templates")
    @SerializedName("templates")
    @JsonProperty("templates")
    private List<Cluster2Template> templates;
    
    @ApiModelProperty(example = "2019-07-16 21:57:25.244")
    @SerializedName("create_time")
    @JsonProperty("create_time")
    private String               createTime;
    
    @ApiModelProperty(example = "2019-07-16 21:57:25.244")
    @SerializedName("update_time")
    @JsonProperty("update_time")
    private String               updateTime;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("status")
    @JsonProperty("status")
    private K8sStatusCode status;

    @ApiModelProperty(example = "{}")
    @SerializedName("dashboard_url")
    @JsonProperty("dashboard_url")
    private String dashboardUrl;
    
    @ApiModelProperty(example = "{}")
    @SerializedName("service_state")
    @JsonProperty("service_state")
    private int serviceState;

    @ApiModelProperty(example = "{}")
    @SerializedName("owner_name")
    @JsonProperty("owner_name")
    private String ownerName;

    @ApiModelProperty(example = "{}")
    @SerializedName("bp_name")
    @JsonProperty("bp_name")
    private String bpName;

    @ApiModelProperty(example = "{}")
    @SerializedName("online_state")
    @JsonProperty("online_state")
    private int onlineState;
    
    
    public static ClusterInfoRspDto of(TblClusterInfo tblClusterInfo)
    {
        ClusterInfoRspDto clusterInfoRsp = new ClusterInfoRspDto();
        BeanUtils.copyProperties(tblClusterInfo, clusterInfoRsp);
        clusterInfoRsp.setTmplVerId(tblClusterInfo.getTmplVersionId());
        if (CollectionUtils.hasContent(tblClusterInfo.getClusterEngineConfig()))
        {
            if (ClusterType.K8S.equalsIgnoreCase(tblClusterInfo.getClusterType()))
            {
                try {
                    clusterInfoRsp.setJkeConfig(JsonUtils.fromJson(tblClusterInfo.getClusterEngineConfig(), JkeConfig.class));
                }
                catch (Exception e)
                {
                    log.error("set jkeconfig error:{}", e);
                    clusterInfoRsp.setJkeConfig(null);
                }
            }
            else if (ClusterType.K3S.equalsIgnoreCase(tblClusterInfo.getClusterType()))
            {
                try {
                    clusterInfoRsp.setK3sConfig(JsonUtils.fromJson(tblClusterInfo.getClusterEngineConfig(), K3sConfig.class));
                }
                catch (Exception e)
                {
                    log.error("set k3sConfig error:{}", e);
                    clusterInfoRsp.setK3sConfig(null);
                }
            }

        }
        
        K8sStatusCode statusCode = K8sStatusCode.builder().code(tblClusterInfo.getStatus()).build();
        Map<String, String> statusDesc = new HashMap<>();
        statusDesc.put("en", ClusterStatus.fromCode(tblClusterInfo.getStatus()).getMessage().get("en"));
        statusCode.setDesc(statusDesc);
        clusterInfoRsp.setStatus(statusCode);
        clusterInfoRsp.setServiceState(tblClusterInfo.getServiceState());
        clusterInfoRsp.setUpdateTime(Utils.formatDate(tblClusterInfo.getUpdateTime()));
        clusterInfoRsp.setCreateTime(Utils.formatDate(tblClusterInfo.getCreateTime()));
        return clusterInfoRsp;
    }
}
