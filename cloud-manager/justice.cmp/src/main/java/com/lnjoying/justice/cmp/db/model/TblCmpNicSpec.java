package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;
import java.util.Date;

public class TblCmpNicSpec extends TblCmpNicSpecKey implements Serializable {
    private String deviceSpecId;

    private String nicProductName;

    private String speed;

    private String mac;

    private Boolean linkIsUp;

    private String slotIdFromAgent;

    private Integer phaseStatus;

    private String switchIdFromAgent;

    private String switchInterface;

    private Date createTime;

    private Date updateTime;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private static final long serialVersionUID = 1L;

    public String getDeviceSpecId() {
        return deviceSpecId;
    }

    public void setDeviceSpecId(String deviceSpecId) {
        this.deviceSpecId = deviceSpecId == null ? null : deviceSpecId.trim();
    }

    public String getNicProductName() {
        return nicProductName;
    }

    public void setNicProductName(String nicProductName) {
        this.nicProductName = nicProductName == null ? null : nicProductName.trim();
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed == null ? null : speed.trim();
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac == null ? null : mac.trim();
    }

    public Boolean getLinkIsUp() {
        return linkIsUp;
    }

    public void setLinkIsUp(Boolean linkIsUp) {
        this.linkIsUp = linkIsUp;
    }

    public String getSlotIdFromAgent() {
        return slotIdFromAgent;
    }

    public void setSlotIdFromAgent(String slotIdFromAgent) {
        this.slotIdFromAgent = slotIdFromAgent == null ? null : slotIdFromAgent.trim();
    }

    public Integer getPhaseStatus() {
        return phaseStatus;
    }

    public void setPhaseStatus(Integer phaseStatus) {
        this.phaseStatus = phaseStatus;
    }

    public String getSwitchIdFromAgent() {
        return switchIdFromAgent;
    }

    public void setSwitchIdFromAgent(String switchIdFromAgent) {
        this.switchIdFromAgent = switchIdFromAgent == null ? null : switchIdFromAgent.trim();
    }

    public String getSwitchInterface() {
        return switchInterface;
    }

    public void setSwitchInterface(String switchInterface) {
        this.switchInterface = switchInterface == null ? null : switchInterface.trim();
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