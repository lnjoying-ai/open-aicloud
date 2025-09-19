package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpIpPoolKey implements Serializable {
    private String cloudId;

    private String poolId;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId == null ? null : poolId.trim();
    }
}