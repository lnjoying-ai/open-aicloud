package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygrouprules;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygroups;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OSSecurityGroupInfo
{
    private static final long serialVersionUID = 1L;
    private String id;
    @JsonProperty("tenant_id")
    @SerializedName("tenant_id")
    private String tenantId;
    @JsonProperty("project_id")
    @SerializedName("project_id")
    private String projectId;
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
    private String name;
    private String description;
    @JsonProperty("security_group_rules")
    @SerializedName("security_group_rules")
    private List<OSSecurityGroupRuleInfo> securityGroupRules;
    private List<String> tags;
    private Boolean stateful;
    private Boolean shared;
    @JsonProperty("ee_name")
    @SerializedName("ee_name")
    private String eeName;

    public void setSecurityGroupInfo(TblCmpOsSecuritygroups tblCmpOsSecuritygroup)
    {
        this.id = tblCmpOsSecuritygroup.getId();
        this.tenantId = tblCmpOsSecuritygroup.getProjectId();
        this.projectId = tblCmpOsSecuritygroup.getProjectId();
        this.createdAt = tblCmpOsSecuritygroup.getCreatedAt();
        this.updatedAt = tblCmpOsSecuritygroup.getUpdatedAt();
        this.revisionNumber = tblCmpOsSecuritygroup.getRevisionNumber() == null ? null : tblCmpOsSecuritygroup.getRevisionNumber().intValue();
        this.name = tblCmpOsSecuritygroup.getEeName();
        if (StringUtils.isEmpty(this.name))
        {
            this.name = tblCmpOsSecuritygroup.getName();
        }
        this.description = tblCmpOsSecuritygroup.getDescription();
        this.shared =  BoolUtil.short2Bool(tblCmpOsSecuritygroup.getShared());
        this.eeName = tblCmpOsSecuritygroup.getName();
    }

    public void setSecurityGroupRule(List<TblCmpOsSecuritygrouprules> securitygrouprules)
    {
        if (! CollectionUtils.isEmpty(securitygrouprules))
        {
            this.securityGroupRules = securitygrouprules.stream().map(securitygrouprule -> {
                OSSecurityGroupRuleInfo osSecurityGroupRuleInfo = new OSSecurityGroupRuleInfo();
                osSecurityGroupRuleInfo.setSecurityGroupRuleInfo(securitygrouprule);
                return osSecurityGroupRuleInfo;
            }).collect(Collectors.toList());
        }
    }
}
