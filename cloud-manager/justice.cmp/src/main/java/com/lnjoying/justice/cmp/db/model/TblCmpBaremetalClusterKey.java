package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpBaremetalClusterKey implements Serializable {
    private String cloudId;

    private String clusterId;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId == null ? null : clusterId.trim();
    }
}