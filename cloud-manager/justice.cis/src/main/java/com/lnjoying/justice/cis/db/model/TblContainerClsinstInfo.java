package com.lnjoying.justice.cis.db.model;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
public class TblContainerClsinstInfo implements Serializable {
    private String instId;

    private String refId;

    private String nodeId;

    private String siteId;

    private String regionId;

    private String containerName;

    private Integer status;

    private String params;

    private String inspetParams;

    private Boolean autoRun;

    private String orchType;

    private String registryInfo;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId == null ? null : instId.trim();
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId == null ? null : refId.trim();
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId == null ? null : siteId.trim();
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName == null ? null : containerName.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params == null ? null : params.trim();
    }

    public String getInspetParams() {
        return inspetParams;
    }

    public void setInspetParams(String inspetParams) {
        this.inspetParams = inspetParams == null ? null : inspetParams.trim();
    }

    public Boolean getAutoRun() {
        return autoRun;
    }

    public void setAutoRun(Boolean autoRun) {
        this.autoRun = autoRun;
    }

    public String getOrchType() {
        return orchType;
    }

    public void setOrchType(String orchType) {
        this.orchType = orchType == null ? null : orchType.trim();
    }

    public String getRegistryInfo() {
        return registryInfo;
    }

    public void setRegistryInfo(String registryInfo) {
        this.registryInfo = registryInfo == null ? null : registryInfo.trim();
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