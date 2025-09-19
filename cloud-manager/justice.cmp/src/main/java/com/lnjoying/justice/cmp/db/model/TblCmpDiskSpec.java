package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpDiskSpec implements Serializable {
    private String cloudId;

    private String diskSpecId;

    private String deviceSpecId;

    private String diskType;

    private String vendor;

    private String model;

    private Long size;

    private Long transSpeed;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private static final long serialVersionUID = 1L;

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId == null ? null : cloudId.trim();
    }

    public String getDiskSpecId() {
        return diskSpecId;
    }

    public void setDiskSpecId(String diskSpecId) {
        this.diskSpecId = diskSpecId == null ? null : diskSpecId.trim();
    }

    public String getDeviceSpecId() {
        return deviceSpecId;
    }

    public void setDeviceSpecId(String deviceSpecId) {
        this.deviceSpecId = deviceSpecId == null ? null : deviceSpecId.trim();
    }

    public String getDiskType() {
        return diskType;
    }

    public void setDiskType(String diskType) {
        this.diskType = diskType == null ? null : diskType.trim();
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor == null ? null : vendor.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getTransSpeed() {
        return transSpeed;
    }

    public void setTransSpeed(Long transSpeed) {
        this.transSpeed = transSpeed;
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