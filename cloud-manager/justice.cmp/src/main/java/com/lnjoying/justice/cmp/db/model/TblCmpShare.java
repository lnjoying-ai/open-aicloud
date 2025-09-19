package com.lnjoying.justice.cmp.db.model;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "TblCmpShare")
public class TblCmpShare extends TblCmpShareKey implements Serializable {
    private String shareIdFromAgent;

    private String userId;

    private String baremetalId;

    private String imageId;

    private String iscsiInitiator;

    private String iscsiTarget;

    private String iscsiIpport;

    private Short phaseStatus;

    private String phaseInfo;

    private Date createTime;

    private Date updateTime;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private static final long serialVersionUID = 1L;

    public String getShareIdFromAgent() {
        return shareIdFromAgent;
    }

    public void setShareIdFromAgent(String shareIdFromAgent) {
        this.shareIdFromAgent = shareIdFromAgent == null ? null : shareIdFromAgent.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getBaremetalId() {
        return baremetalId;
    }

    public void setBaremetalId(String baremetalId) {
        this.baremetalId = baremetalId == null ? null : baremetalId.trim();
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId == null ? null : imageId.trim();
    }

    public String getIscsiInitiator() {
        return iscsiInitiator;
    }

    public void setIscsiInitiator(String iscsiInitiator) {
        this.iscsiInitiator = iscsiInitiator == null ? null : iscsiInitiator.trim();
    }

    public String getIscsiTarget() {
        return iscsiTarget;
    }

    public void setIscsiTarget(String iscsiTarget) {
        this.iscsiTarget = iscsiTarget == null ? null : iscsiTarget.trim();
    }

    public String getIscsiIpport() {
        return iscsiIpport;
    }

    public void setIscsiIpport(String iscsiIpport) {
        this.iscsiIpport = iscsiIpport == null ? null : iscsiIpport.trim();
    }

    public Short getPhaseStatus() {
        return phaseStatus;
    }

    public void setPhaseStatus(Short phaseStatus) {
        this.phaseStatus = phaseStatus;
    }

    public String getPhaseInfo() {
        return phaseInfo;
    }

    public void setPhaseInfo(String phaseInfo) {
        this.phaseInfo = phaseInfo == null ? null : phaseInfo.trim();
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