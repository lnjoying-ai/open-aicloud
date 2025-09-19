package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpOsInstanceMetadataKey implements Serializable {
    private String cloudId;

    private Integer id;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}