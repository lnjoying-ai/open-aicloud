package com.lnjoying.justice.scheduler.db.model;

import java.io.Serializable;
import java.util.Date;

public class TblEdgeComputeGpuInfo implements Serializable {
    private String gpuId;

    private String gpuType;

    private String gpuModel;

    private String driverVersion;

    private String cudaVersion;

    private String cudnnVersion;

    private String nodeId;

    private Integer index;

    private Integer status;

    private String refId;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getGpuId() {
        return gpuId;
    }

    public void setGpuId(String gpuId) {
        this.gpuId = gpuId == null ? null : gpuId.trim();
    }

    public String getGpuType() {
        return gpuType;
    }

    public void setGpuType(String gpuType) {
        this.gpuType = gpuType == null ? null : gpuType.trim();
    }

    public String getGpuModel() {
        return gpuModel;
    }

    public void setGpuModel(String gpuModel) {
        this.gpuModel = gpuModel == null ? null : gpuModel.trim();
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion == null ? null : driverVersion.trim();
    }

    public String getCudaVersion() {
        return cudaVersion;
    }

    public void setCudaVersion(String cudaVersion) {
        this.cudaVersion = cudaVersion == null ? null : cudaVersion.trim();
    }

    public String getCudnnVersion() {
        return cudnnVersion;
    }

    public void setCudnnVersion(String cudnnVersion) {
        this.cudnnVersion = cudnnVersion == null ? null : cudnnVersion.trim();
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId == null ? null : refId.trim();
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