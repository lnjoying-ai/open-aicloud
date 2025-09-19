package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpEsFirewallPolicies extends TblCmpEsFirewallPoliciesKey implements Serializable {
    private String name;

    private String tenantId;

    private String projectId;

    private Short shared;

    private String description;

    private Short audited;

    private Integer eeStatus;

    private String eeBp;

    private String eeUser;

    private static final long serialVersionUID = 1L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId == null ? null : tenantId.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public Short getShared() {
        return shared;
    }

    public void setShared(Short shared) {
        this.shared = shared;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Short getAudited() {
        return audited;
    }

    public void setAudited(Short audited) {
        this.audited = audited;
    }

    public Integer getEeStatus() {
        return eeStatus;
    }

    public void setEeStatus(Integer eeStatus) {
        this.eeStatus = eeStatus;
    }

    public String getEeBp() {
        return eeBp;
    }

    public void setEeBp(String eeBp) {
        this.eeBp = eeBp == null ? null : eeBp.trim();
    }

    public String getEeUser() {
        return eeUser;
    }

    public void setEeUser(String eeUser) {
        this.eeUser = eeUser == null ? null : eeUser.trim();
    }
}