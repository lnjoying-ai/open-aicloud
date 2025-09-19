package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "TblCmpBaremetalDevice")
public class TblCmpBaremetalDevice extends TblCmpBaremetalDeviceKey implements Serializable {

    @ResourceInstanceName
    private String name;

    private Integer phaseStatus;

    private String clusterId;

    private String deviceSpecId;

    private String userId;

    private Short addressType;

    private String ipmiIp;

    private Short ipmiPort;

    private String ipmiUsername;

    private String ipmiPassword;

    private String bmctype;

    private String ipmiMac;

    private String deviceIdFromAgent;

    private String description;

    private Date createTime;

    private Date updateTime;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private static final long serialVersionUID = 1L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getPhaseStatus() {
        return phaseStatus;
    }

    public void setPhaseStatus(Integer phaseStatus) {
        this.phaseStatus = phaseStatus;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId == null ? null : clusterId.trim();
    }

    public String getDeviceSpecId() {
        return deviceSpecId;
    }

    public void setDeviceSpecId(String deviceSpecId) {
        this.deviceSpecId = deviceSpecId == null ? null : deviceSpecId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Short getAddressType() {
        return addressType;
    }

    public void setAddressType(Short addressType) {
        this.addressType = addressType;
    }

    public String getIpmiIp() {
        return ipmiIp;
    }

    public void setIpmiIp(String ipmiIp) {
        this.ipmiIp = ipmiIp == null ? null : ipmiIp.trim();
    }

    public Short getIpmiPort() {
        return ipmiPort;
    }

    public void setIpmiPort(Short ipmiPort) {
        this.ipmiPort = ipmiPort;
    }

    public String getIpmiUsername() {
        return ipmiUsername;
    }

    public void setIpmiUsername(String ipmiUsername) {
        this.ipmiUsername = ipmiUsername == null ? null : ipmiUsername.trim();
    }

    public String getIpmiPassword() {
        return ipmiPassword;
    }

    public void setIpmiPassword(String ipmiPassword) {
        this.ipmiPassword = ipmiPassword == null ? null : ipmiPassword.trim();
    }

    public String getBmctype() {
        return bmctype;
    }

    public void setBmctype(String bmctype) {
        this.bmctype = bmctype == null ? null : bmctype.trim();
    }

    public String getIpmiMac() {
        return ipmiMac;
    }

    public void setIpmiMac(String ipmiMac) {
        this.ipmiMac = ipmiMac == null ? null : ipmiMac.trim();
    }

    public String getDeviceIdFromAgent() {
        return deviceIdFromAgent;
    }

    public void setDeviceIdFromAgent(String deviceIdFromAgent) {
        this.deviceIdFromAgent = deviceIdFromAgent == null ? null : deviceIdFromAgent.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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