package com.lnjoying.justice.cmp.db.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class TblCmpSgVmInstanceKey implements Serializable {
    private String cloudId;

    private String sgVmId;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getSgVmId() {
        return sgVmId;
    }

    public void setSgVmId(String sgVmId) {
        this.sgVmId = sgVmId == null ? null : sgVmId.trim();
    }
}