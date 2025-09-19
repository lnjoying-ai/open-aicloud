package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpNodeImageKey implements Serializable {
    private String cloudId;

    private String nodeImageId;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getNodeImageId() {
        return nodeImageId;
    }

    public void setNodeImageId(String nodeImageId) {
        this.nodeImageId = nodeImageId == null ? null : nodeImageId.trim();
    }
}