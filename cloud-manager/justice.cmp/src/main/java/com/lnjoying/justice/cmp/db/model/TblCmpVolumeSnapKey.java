package com.lnjoying.justice.cmp.db.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class TblCmpVolumeSnapKey implements Serializable {
    private String cloudId;

    private String volumeSnapId;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getVolumeSnapId() {
        return volumeSnapId;
    }

    public void setVolumeSnapId(String volumeSnapId) {
        this.volumeSnapId = volumeSnapId == null ? null : volumeSnapId.trim();
    }
}