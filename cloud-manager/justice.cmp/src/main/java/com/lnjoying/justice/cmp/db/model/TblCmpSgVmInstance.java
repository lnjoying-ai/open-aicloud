package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;
import java.util.Date;

public class TblCmpSgVmInstance extends TblCmpSgVmInstanceKey implements Serializable {
    private String sgId;

    private String instanceId;

    private Integer phaseStatus;

    private String sgIdFromAgent;

    private String vpcIdFromAgent;

    private Date createTime;

    private Date updateTime;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private static final long serialVersionUID = 1L;

    public String getSgId() {
        return sgId;
    }

    public void setSgId(String sgId) {
        this.sgId = sgId == null ? null : sgId.trim();
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId == null ? null : instanceId.trim();
    }

    public Integer getPhaseStatus() {
        return phaseStatus;
    }

    public void setPhaseStatus(Integer phaseStatus) {
        this.phaseStatus = phaseStatus;
    }

    public String getSgIdFromAgent() {
        return sgIdFromAgent;
    }

    public void setSgIdFromAgent(String sgIdFromAgent) {
        this.sgIdFromAgent = sgIdFromAgent == null ? null : sgIdFromAgent.trim();
    }

    public String getVpcIdFromAgent() {
        return vpcIdFromAgent;
    }

    public void setVpcIdFromAgent(String vpcIdFromAgent) {
        this.vpcIdFromAgent = vpcIdFromAgent == null ? null : vpcIdFromAgent.trim();
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