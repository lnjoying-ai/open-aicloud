package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygrouprules;
import lombok.Data;

import java.util.Date;

@Data
public class OSSecurityGroupRuleInfo
{
    private static final long serialVersionUID = 1L;
    @JsonProperty("remote_group_id")
    @SerializedName("remote_group_id")
    private String remoteGroupId;
    private String direction;
    private String protocol;
    private String ethertype;
    @JsonProperty("port_range_max")
    @SerializedName("port_range_max")
    private Integer portRangeMax;
    @JsonProperty("security_group_id")
    @SerializedName("security_group_id")
    private String securityGroupId;
    @JsonProperty("tenant_id")
    @SerializedName("tenant_id")
    private String tenantId;
    @JsonProperty("project_id")
    @SerializedName("project_id")
    private String projectId;
    @JsonProperty("port_range_min")
    @SerializedName("port_range_min")
    private Integer portRangeMin;
    @JsonProperty("remote_ip_prefix")
    @SerializedName("remote_ip_prefix")
    private String remoteIpPrefix;
    @JsonProperty("created_at")
    @SerializedName("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    @JsonProperty("updated_at")
    @SerializedName("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
    @JsonProperty("revision_number")
    @SerializedName("revision_number")
    private Integer revisionNumber;
    private String id;
    private String description;
    @JsonProperty("belongs_to_default_sg")
    @SerializedName("belongs_to_default_sg")
    private Boolean belongsToDefaultSg;

    public void setSecurityGroupRuleInfo(TblCmpOsSecuritygrouprules tblCmpOsSecuritygrouprule)
    {
        this.remoteGroupId = tblCmpOsSecuritygrouprule.getRemoteGroupId();
        this.direction = tblCmpOsSecuritygrouprule.getDirection();
        this.protocol = tblCmpOsSecuritygrouprule.getProtocol();
        this.ethertype = tblCmpOsSecuritygrouprule.getEthertype();
        this.portRangeMax = tblCmpOsSecuritygrouprule.getPortRangeMax();
        this.securityGroupId = tblCmpOsSecuritygrouprule.getSecurityGroupId();
        this.tenantId = tblCmpOsSecuritygrouprule.getProjectId();
        this.projectId = tblCmpOsSecuritygrouprule.getProjectId();
        this.portRangeMin = tblCmpOsSecuritygrouprule.getPortRangeMin();
        this.remoteIpPrefix = tblCmpOsSecuritygrouprule.getRemoteIpPrefix();
        this.createdAt = tblCmpOsSecuritygrouprule.getCreatedAt();
        this.updatedAt = tblCmpOsSecuritygrouprule.getUpdatedAt();
        this.revisionNumber = tblCmpOsSecuritygrouprule.getRevisionNumber() == null ? null : tblCmpOsSecuritygrouprule.getRevisionNumber().intValue();
        this.id = tblCmpOsSecuritygrouprule.getId();
        this.description = tblCmpOsSecuritygrouprule.getDescription();
    }
}
