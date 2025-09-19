package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpOsMl2DistributedPortBindings extends TblCmpOsMl2DistributedPortBindingsKey implements Serializable {
    private String routerId;

    private String vifType;

    private String vifDetails;

    private String vnicType;

    private String profile;

    private String status;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private static final long serialVersionUID = 1L;

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId == null ? null : routerId.trim();
    }

    public String getVifType() {
        return vifType;
    }

    public void setVifType(String vifType) {
        this.vifType = vifType == null ? null : vifType.trim();
    }

    public String getVifDetails() {
        return vifDetails;
    }

    public void setVifDetails(String vifDetails) {
        this.vifDetails = vifDetails == null ? null : vifDetails.trim();
    }

    public String getVnicType() {
        return vnicType;
    }

    public void setVnicType(String vnicType) {
        this.vnicType = vnicType == null ? null : vnicType.trim();
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile == null ? null : profile.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getEeBp() {
        return eeBp;
    }

    public void setEeBp(String eeBp) {
        this.eeBp = eeBp == null ? null : eeBp.trim();
    }

    public String getEeUser() {
        return eeUser;
    }

    public void setEeUser(String eeUser) {
        this.eeUser = eeUser == null ? null : eeUser.trim();
    }

    public Integer getEeStatus() {
        return eeStatus;
    }

    public void setEeStatus(Integer eeStatus) {
        this.eeStatus = eeStatus;
    }
}