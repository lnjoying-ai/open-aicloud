package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpOsExtradhcpoptsKey implements Serializable {
    private String cloudId;

    private String portId;

    private String optName;

    private Integer ipVersion;

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

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName == null ? null : optName.trim();
    }

    public Integer getIpVersion() {
        return ipVersion;
    }

    public void setIpVersion(Integer ipVersion) {
        this.ipVersion = ipVersion;
    }
}