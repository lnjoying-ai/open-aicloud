package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpNodeStoragePoolKey implements Serializable {
    private String cloudId;

    private String nodeStoragePoolId;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getNodeStoragePoolId() {
        return nodeStoragePoolId;
    }

    public void setNodeStoragePoolId(String nodeStoragePoolId) {
        this.nodeStoragePoolId = nodeStoragePoolId == null ? null : nodeStoragePoolId.trim();
    }
}