package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpNicInfo extends TblCmpNicInfoKey implements Serializable {
    private String deviceId;

    private String nicName;

    private String linkState;

    private Short networkType;

    private String ipmiMac;

    private String ip;

    private Short addressType;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private static final long serialVersionUID = 1L;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getNicName() {
        return nicName;
    }

    public void setNicName(String nicName) {
        this.nicName = nicName == null ? null : nicName.trim();
    }

    public String getLinkState() {
        return linkState;
    }

    public void setLinkState(String linkState) {
        this.linkState = linkState == null ? null : linkState.trim();
    }

    public Short getNetworkType() {
        return networkType;
    }

    public void setNetworkType(Short networkType) {
        this.networkType = networkType;
    }

    public String getIpmiMac() {
        return ipmiMac;
    }

    public void setIpmiMac(String ipmiMac) {
        this.ipmiMac = ipmiMac == null ? null : ipmiMac.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Short getAddressType() {
        return addressType;
    }

    public void setAddressType(Short addressType) {
        this.addressType = addressType;
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