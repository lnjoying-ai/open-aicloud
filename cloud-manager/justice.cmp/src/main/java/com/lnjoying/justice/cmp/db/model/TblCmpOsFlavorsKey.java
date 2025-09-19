package com.lnjoying.justice.cmp.db.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class TblCmpOsFlavorsKey implements Serializable {
    private String cloudId;

    private String flavorid;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getFlavorid() {
        return flavorid;
    }

    public void setFlavorid(String flavorid) {
        this.flavorid = flavorid == null ? null : flavorid.trim();
    }
}