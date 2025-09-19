package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpPortMapKey implements Serializable {
    private String cloudId;

    private String portMapId;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getPortMapId() {
        return portMapId;
    }

    public void setPortMapId(String portMapId) {
        this.portMapId = portMapId == null ? null : portMapId.trim();
    }
}