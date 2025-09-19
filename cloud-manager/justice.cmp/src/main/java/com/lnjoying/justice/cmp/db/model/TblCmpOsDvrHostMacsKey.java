package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpOsDvrHostMacsKey implements Serializable {
    private String cloudId;

    private String host;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host == null ? null : host.trim();
    }
}