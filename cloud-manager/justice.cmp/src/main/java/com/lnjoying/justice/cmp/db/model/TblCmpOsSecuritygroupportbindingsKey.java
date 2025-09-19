package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpOsSecuritygroupportbindingsKey implements Serializable {
    private String cloudId;

    private String portId;

    private String securityGroupId;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId == null ? null : portId.trim();
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId == null ? null : securityGroupId.trim();
    }
}