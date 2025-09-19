package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpDiskInfoKey implements Serializable {
    private String cloudId;

    private String diskId;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId == null ? null : diskId.trim();
    }
}