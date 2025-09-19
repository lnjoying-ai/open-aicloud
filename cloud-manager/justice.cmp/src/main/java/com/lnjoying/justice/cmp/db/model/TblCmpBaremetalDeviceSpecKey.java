package com.lnjoying.justice.cmp.db.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class TblCmpBaremetalDeviceSpecKey implements Serializable {
    private String cloudId;

    private String deviceSpecId;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getDeviceSpecId() {
        return deviceSpecId;
    }

    public void setDeviceSpecId(String deviceSpecId) {
        this.deviceSpecId = deviceSpecId == null ? null : deviceSpecId.trim();
    }
}