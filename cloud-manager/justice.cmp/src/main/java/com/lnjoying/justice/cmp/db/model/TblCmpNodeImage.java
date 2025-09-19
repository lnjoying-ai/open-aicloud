package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;
import java.util.Date;

public class TblCmpNodeImage extends TblCmpNodeImageKey implements Serializable {
    private String imageId;

    private String storagePoolId;

    private String nodeImageFromAgent;

    private Integer phaseStatus;

    private Date createTime;

    private Date updateTime;

    private String userId;

    private String nodeId;

    private String storagePoolIdFromAgent;

    private String nodeIp;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private static final long serialVersionUID = 1L;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId == null ? null : imageId.trim();
    }

    public String getStoragePoolId() {
        return storagePoolId;
    }

    public void setStoragePoolId(String storagePoolId) {
        this.storagePoolId = storagePoolId == null ? null : storagePoolId.trim();
    }

    public String getNodeImageFromAgent() {
        return nodeImageFromAgent;
    }

    public void setNodeImageFromAgent(String nodeImageFromAgent) {
        this.nodeImageFromAgent = nodeImageFromAgent == null ? null : nodeImageFromAgent.trim();
    }

    public Integer getPhaseStatus() {
        return phaseStatus;
    }

    public void setPhaseStatus(Integer phaseStatus) {
        this.phaseStatus = phaseStatus;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    public String getStoragePoolIdFromAgent() {
        return storagePoolIdFromAgent;
    }

    public void setStoragePoolIdFromAgent(String storagePoolIdFromAgent) {
        this.storagePoolIdFromAgent = storagePoolIdFromAgent == null ? null : storagePoolIdFromAgent.trim();
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp == null ? null : nodeIp.trim();
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