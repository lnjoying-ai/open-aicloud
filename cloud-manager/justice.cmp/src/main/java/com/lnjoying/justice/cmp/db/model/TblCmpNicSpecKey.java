package com.lnjoying.justice.cmp.db.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class TblCmpNicSpecKey implements Serializable {
    private String cloudId;

    private String nicSpecId;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getNicSpecId() {
        return nicSpecId;
    }

    public void setNicSpecId(String nicSpecId) {
        this.nicSpecId = nicSpecId == null ? null : nicSpecId.trim();
    }
}