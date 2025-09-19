package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "TblCmpStoragePool")
public class TblCmpStoragePool extends TblCmpStoragePoolKey implements Serializable {
    private String storagePoolIdFromAgent;

    private Integer type;

    private String sid;

    private String paras;

    private Integer phaseStatus;

    private Date createTime;

    private Date updateTime;

    @ResourceInstanceName
    private String name;

    private String description;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private static final long serialVersionUID = 1L;

    public String getStoragePoolIdFromAgent() {
        return storagePoolIdFromAgent;
    }

    public void setStoragePoolIdFromAgent(String storagePoolIdFromAgent) {
        this.storagePoolIdFromAgent = storagePoolIdFromAgent == null ? null : storagePoolIdFromAgent.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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