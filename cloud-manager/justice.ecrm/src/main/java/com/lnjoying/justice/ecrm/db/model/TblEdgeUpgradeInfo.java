package com.lnjoying.justice.ecrm.db.model;

import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@ToString
public class TblEdgeUpgradeInfo implements Serializable {
    private String nodeId;

    private Integer upgradeStatus;

    private Date createTime;

    private Date updateTime;

    private String upgradePlan;

    private String oldVersion;

    private String newVersion;

    private String operUser;

    private String operBp;

    private String regionId;

    private String siteId;

    private static final long serialVersionUID = 1L;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    public Integer getUpgradeStatus() {
        return upgradeStatus;
    }

    public void setUpgradeStatus(Integer upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
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

    public String getUpgradePlan() {
        return upgradePlan;
    }

    public void setUpgradePlan(String upgradePlan) {
        this.upgradePlan = upgradePlan == null ? null : upgradePlan.trim();
    }

    public String getOldVersion() {
        return oldVersion;
    }

    public void setOldVersion(String oldVersion) {
        this.oldVersion = oldVersion == null ? null : oldVersion.trim();
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion == null ? null : newVersion.trim();
    }

    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser == null ? null : operUser.trim();
    }

    public String getOperBp() {
        return operBp;
    }

    public void setOperBp(String operBp) {
        this.operBp = operBp == null ? null : operBp.trim();
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId == null ? null : siteId.trim();
    }
}