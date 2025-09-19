package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;
import java.util.Date;

public class TblCmpNodeStoragePool extends TblCmpNodeStoragePoolKey implements Serializable {
    private String storagePoolId;

    private String storagePoolIdFromAgent;

    private String nodeIp;

    private String sid;

    private String paras;

    private Integer phaseStatus;

    private Date createTime;

    private Date updateTime;

    private Integer type;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private static final long serialVersionUID = 1L;

    public String getStoragePoolId() {
        return storagePoolId;
    }

    public void setStoragePoolId(String storagePoolId) {
        this.storagePoolId = storagePoolId == null ? null : storagePoolId.trim();
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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid == null ? null : sid.trim();
    }

    public String getParas() {
        return paras;
    }

    public void setParas(String paras) {
        this.paras = paras == null ? null : paras.trim();
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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