package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "TblCmpHypervisorNode")
public class TblCmpHypervisorNode extends TblCmpHypervisorNodeKey implements Serializable {
    private String instanceId;

    @ResourceInstanceName
    private String name;

    private Integer phaseStatus;

    private String manageIp;

    private String hostName;

    private String sysUsername;

    private String sysPassword;

    private String pubkeyId;

    private String description;

    private Date createTime;

    private Date updateTime;

    private String backupNodeId;

    private Integer errorCount;

    private String cpuModel;

    private Integer cpuLogCount;

    private Integer memTotal;

    private Integer cpuPhyCount;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private Integer availableIbCount;

    private Integer ibCount;

    private static final long serialVersionUID = 1L;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId == null ? null : instanceId.trim();
    }

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

    public String getManageIp() {
        return manageIp;
    }

    public void setManageIp(String manageIp) {
        this.manageIp = manageIp == null ? null : manageIp.trim();
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName == null ? null : hostName.trim();
    }

    public String getSysUsername() {
        return sysUsername;
    }

    public void setSysUsername(String sysUsername) {
        this.sysUsername = sysUsername == null ? null : sysUsername.trim();
    }

    public String getSysPassword() {
        return sysPassword;
    }

    public void setSysPassword(String sysPassword) {
        this.sysPassword = sysPassword == null ? null : sysPassword.trim();
    }

    public String getPubkeyId() {
        return pubkeyId;
    }

    public void setPubkeyId(String pubkeyId) {
        this.pubkeyId = pubkeyId == null ? null : pubkeyId.trim();
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

    public String getBackupNodeId() {
        return backupNodeId;
    }

    public void setBackupNodeId(String backupNodeId) {
        this.backupNodeId = backupNodeId == null ? null : backupNodeId.trim();
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public String getCpuModel() {
        return cpuModel;
    }

    public void setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel == null ? null : cpuModel.trim();
    }

    public Integer getCpuLogCount() {
        return cpuLogCount;
    }

    public void setCpuLogCount(Integer cpuLogCount) {
        this.cpuLogCount = cpuLogCount;
    }

    public Integer getMemTotal() {
        return memTotal;
    }

    public void setMemTotal(Integer memTotal) {
        this.memTotal = memTotal;
    }

    public Integer getCpuPhyCount() {
        return cpuPhyCount;
    }

    public void setCpuPhyCount(Integer cpuPhyCount) {
        this.cpuPhyCount = cpuPhyCount;
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

    public Integer getAvailableIbCount() {
        return availableIbCount;
    }

    public void setAvailableIbCount(Integer availableIbCount) {
        this.availableIbCount = availableIbCount;
    }

    public Integer getIbCount() {
        return ibCount;
    }

    public void setIbCount(Integer ibCount) {
        this.ibCount = ibCount;
    }
}