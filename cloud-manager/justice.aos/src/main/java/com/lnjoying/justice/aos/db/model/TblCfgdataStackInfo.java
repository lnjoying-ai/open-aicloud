package com.lnjoying.justice.aos.db.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public class TblCfgdataStackInfo implements Serializable {
    private String cfgVolumeId;

    private String dataId;

    private String dataGroup;

    private String dataHash;

    private String userId;

    private String nodeId;

    private String stackId;

    private Integer syncState;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getCfgVolumeId() {
        return cfgVolumeId;
    }

    public void setCfgVolumeId(String cfgVolumeId) {
        this.cfgVolumeId = cfgVolumeId == null ? null : cfgVolumeId.trim();
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId == null ? null : dataId.trim();
    }

    public String getDataGroup() {
        return dataGroup;
    }

    public void setDataGroup(String dataGroup) {
        this.dataGroup = dataGroup == null ? null : dataGroup.trim();
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash == null ? null : dataHash.trim();
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

    public String getStackId() {
        return stackId;
    }

    public void setStackId(String stackId) {
        this.stackId = stackId == null ? null : stackId.trim();
    }

    public Integer getSyncState() {
        return syncState;
    }

    public void setSyncState(Integer syncState) {
        this.syncState = syncState;
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
}