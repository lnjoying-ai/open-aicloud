package com.lnjoying.justice.ecrm.domain.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.ecrm.common.constant.InstanceState;
import com.lnjoying.justice.ecrm.db.model.TblEdgeComputeInfo;
import com.lnjoying.justice.ecrm.db.model.TblRegionInfo;
import com.lnjoying.justice.ecrm.db.model.TblServiceAgentInfo;
import com.lnjoying.justice.ecrm.db.model.TblSiteInfo;
import com.lnjoying.justice.schema.entity.StatusCode;
import com.micro.core.common.Utils;
import lombok.Data;

@Data
public class ServiceAgent
{
    @SerializedName("agent_id")
    @JsonProperty("agent_id")
    private String agentId;

    @SerializedName("image")
    @JsonProperty("image")
    private String image;

    @SerializedName("region_id")
    @JsonProperty("region_id")
    private String regionId;

    @SerializedName("region_name")
    @JsonProperty("region_name")
    private String regionName;

    @SerializedName("site_id")
    @JsonProperty("site_id")
    private String siteId;

    @SerializedName("site_name")
    @JsonProperty("site_name")
    private String siteName;

    @SerializedName("node_id")
    @JsonProperty("node_id")
    private String nodeId;

    @SerializedName("node_name")
    @JsonProperty("node_name")
    private String nodeName;

    @SerializedName("status")
    @JsonProperty("status")
    private StatusCode status;

    @SerializedName("online_status")
    @JsonProperty("online_status")
    private int onlineStatus;

    @SerializedName("type")
    @JsonProperty("type")
    private String type;

    @SerializedName("core_version")
    @JsonProperty("core_version")
    private String coreVersion;

    @SerializedName("description")
    @JsonProperty("description")
    private String description;

    @SerializedName("create_time")
    @JsonProperty("create_time")
    private String createTime;

    @SerializedName("update_time")
    @JsonProperty("update_time")
    private String updateTime;

    public static ServiceAgent of(TblServiceAgentInfo tblServiceAgentInfo)
    {
        ServiceAgent serviceAgent = new ServiceAgent();
        serviceAgent.setAgentId(tblServiceAgentInfo.getAgentId());
        serviceAgent.setImage(tblServiceAgentInfo.getImage());
        serviceAgent.setRegionId(tblServiceAgentInfo.getRegionId());
        serviceAgent.setSiteId(tblServiceAgentInfo.getSiteId());
        serviceAgent.setNodeId(tblServiceAgentInfo.getNodeId());
        serviceAgent.setStatus(InstanceState.fromCode(tblServiceAgentInfo.getStatus()).toStatusCode());
        serviceAgent.setOnlineStatus(tblServiceAgentInfo.getOnlineStatus());
        serviceAgent.setType(tblServiceAgentInfo.getType());
        serviceAgent.setCoreVersion(tblServiceAgentInfo.getCoreVersion());
        serviceAgent.setDescription(tblServiceAgentInfo.getDescription());
        serviceAgent.setCreateTime(Utils.formatDate(tblServiceAgentInfo.getCreateTime()));
        serviceAgent.setUpdateTime(Utils.formatDate(tblServiceAgentInfo.getUpdateTime()));
        return serviceAgent;
    }

    public void setName(TblRegionInfo tblRegionInfo, TblSiteInfo tblSiteInfo, TblEdgeComputeInfo tblEdgeComputeInfo)
    {
        if (tblRegionInfo != null) this.regionName = tblRegionInfo.getRegionName();
        if (tblSiteInfo != null) this.siteName = tblSiteInfo.getSiteName();
        if (tblEdgeComputeInfo != null) this.nodeName = tblEdgeComputeInfo.getNodeName();
    }
}
