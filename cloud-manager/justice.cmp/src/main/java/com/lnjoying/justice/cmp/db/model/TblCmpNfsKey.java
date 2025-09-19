package com.lnjoying.justice.cmp.db.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class TblCmpNfsKey implements Serializable {
    private String cloudId;

    private String nfsId;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getNfsId() {
        return nfsId;
    }

    public void setNfsId(String nfsId) {
        this.nfsId = nfsId == null ? null : nfsId.trim();
    }
}