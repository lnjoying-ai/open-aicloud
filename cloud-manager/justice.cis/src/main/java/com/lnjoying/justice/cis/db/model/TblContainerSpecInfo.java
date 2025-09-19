package com.lnjoying.justice.cis.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "TblContainerSpecInfo")
public class TblContainerSpecInfo implements Serializable {
    private String specId;

    @ResourceInstanceName
    private String specName;

    private String imageName;

    private String registryId;

    private String cmd;

    private String schedulingStrategy;

    private Integer cpuNum;

    private Long memLimit;

    private Long diskLimit;

    private Integer transmitBandLimit;

    private Integer recvBandLimit;

    private Integer gpuNum;

    private String devNeedInfo;

    private String containerParams;

    private Boolean autoRun;

    private Integer replicaNum;

    private String userId;

    private String bpId;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private String extraParams;

    private String restartPolicy;

    private String failoverPolicy;

    private static final long serialVersionUID = 1L;

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId == null ? null : specId.trim();
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName == null ? null : specName.trim();
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName == null ? null : imageName.trim();
    }

    public String getRegistryId() {
        return registryId;
    }

    public void setRegistryId(String registryId) {
        this.registryId = registryId == null ? null : registryId.trim();
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd == null ? null : cmd.trim();
    }

    public String getSchedulingStrategy() {
        return schedulingStrategy;
    }

    public void setSchedulingStrategy(String schedulingStrategy) {
        this.schedulingStrategy = schedulingStrategy == null ? null : schedulingStrategy.trim();
    }

    public Integer getCpuNum() {
        return cpuNum;
    }

    public void setCpuNum(Integer cpuNum) {
        this.cpuNum = cpuNum;
    }

    public Long getMemLimit() {
        return memLimit;
    }

    public void setMemLimit(Long memLimit) {
        this.memLimit = memLimit;
    }

    public Long getDiskLimit() {
        return diskLimit;
    }

    public void setDiskLimit(Long diskLimit) {
        this.diskLimit = diskLimit;
    }

    public Integer getTransmitBandLimit() {
        return transmitBandLimit;
    }

    public void setTransmitBandLimit(Integer transmitBandLimit) {
        this.transmitBandLimit = transmitBandLimit;
    }

    public Integer getRecvBandLimit() {
        return recvBandLimit;
    }

    public void setRecvBandLimit(Integer recvBandLimit) {
        this.recvBandLimit = recvBandLimit;
    }

    public Integer getGpuNum() {
        return gpuNum;
    }

    public void setGpuNum(Integer gpuNum) {
        this.gpuNum = gpuNum;
    }

    public String getDevNeedInfo() {
        return devNeedInfo;
    }

    public void setDevNeedInfo(String devNeedInfo) {
        this.devNeedInfo = devNeedInfo == null ? null : devNeedInfo.trim();
    }

    public String getContainerParams() {
        return containerParams;
    }

    public void setContainerParams(String containerParams) {
        this.containerParams = containerParams == null ? null : containerParams.trim();
    }

    public Boolean getAutoRun() {
        return autoRun;
    }

    public void setAutoRun(Boolean autoRun) {
        this.autoRun = autoRun;
    }

    public Integer getReplicaNum() {
        return replicaNum;
    }

    public void setReplicaNum(Integer replicaNum) {
        this.replicaNum = replicaNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getBpId() {
        return bpId;
    }

    public void setBpId(String bpId) {
        this.bpId = bpId == null ? null : bpId.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(String extraParams) {
        this.extraParams = extraParams;
    }

    public String getRestartPolicy()
    {
        return restartPolicy;
    }

    public void setRestartPolicy(String restartPolicy)
    {
        this.restartPolicy = restartPolicy;
    }

    public String getFailoverPolicy()
    {
        return failoverPolicy;
    }

    public void setFailoverPolicy(String failoverPolicy)
    {
        this.failoverPolicy = failoverPolicy;
    }
}