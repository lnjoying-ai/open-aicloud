package com.lnjoying.justice.cmp.db.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class TblCmpEipPoolVpcRefKey implements Serializable {
    private String cloudId;

    private String poolVpcId;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getPoolVpcId() {
        return poolVpcId;
    }

    public void setPoolVpcId(String poolVpcId) {
        this.poolVpcId = poolVpcId == null ? null : poolVpcId.trim();
    }
}