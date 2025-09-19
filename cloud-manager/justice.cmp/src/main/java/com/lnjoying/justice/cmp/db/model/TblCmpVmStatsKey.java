package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpVmStatsKey implements Serializable {
    private String cloudId;

    private String statsId;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getStatsId() {
        return statsId;
    }

    public void setStatsId(String statsId) {
        this.statsId = statsId == null ? null : statsId.trim();
    }
}