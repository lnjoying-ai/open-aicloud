package com.lnjoying.justice.cmp.db.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class TblCmpEipMapKey implements Serializable {
    private String cloudId;

    private String eipMapId;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getEipMapId() {
        return eipMapId;
    }

    public void setEipMapId(String eipMapId) {
        this.eipMapId = eipMapId == null ? null : eipMapId.trim();
    }
}