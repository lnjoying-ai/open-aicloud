package com.lnjoying.justice.cluster.manager.db.model;

import java.io.Serializable;
import java.util.Date;

public class TblClusterNodeInfo implements Serializable {
    private String clusterId;

    private String regionId;

    private String siteId;

    private String nodeId;

    private String nodeName;

    private String clusterRoles;

    private String internalAddress;

    private String externalAddress;

    private String dockerInfo;

    private Integer status;

    private String labels;

    private String taints;

    private String annotations;

    private String deployPlan;

    private String undeployPlan;

    private String monitorPlan;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId == null ? null : clusterId.trim();
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId == null ? null : siteId.trim();
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName == null ? null : nodeName.trim();
    }

    public String getClusterRoles() {
        return clusterRoles;
    }

    public void setClusterRoles(String clusterRoles) {
        this.clusterRoles = clusterRoles == null ? null : clusterRoles.trim();
    }

    public String getInternalAddress() {
        return internalAddress;
    }

    public void setInternalAddress(String internalAddress) {
        this.internalAddress = internalAddress == null ? null : internalAddress.trim();
    }

    public String getExternalAddress() {
        return externalAddress;
    }

    public void setExternalAddress(String externalAddress) {
        this.externalAddress = externalAddress == null ? null : externalAddress.trim();
    }

    public String getDockerInfo() {
        return dockerInfo;
    }

    public void setDockerInfo(String dockerInfo) {
        this.dockerInfo = dockerInfo == null ? null : dockerInfo.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels == null ? null : labels.trim();
    }

    public String getTaints() {
        return taints;
    }

    public void setTaints(String taints) {
        this.taints = taints == null ? null : taints.trim();
    }

    public String getAnnotations() {
        return annotations;
    }

    public void setAnnotations(String annotations) {
        this.annotations = annotations == null ? null : annotations.trim();
    }

    public String getDeployPlan() {
        return deployPlan;
    }

    public void setDeployPlan(String deployPlan) {
        this.deployPlan = deployPlan == null ? null : deployPlan.trim();
    }

    public String getUndeployPlan() {
        return undeployPlan;
    }

    public void setUndeployPlan(String undeployPlan) {
        this.undeployPlan = undeployPlan == null ? null : undeployPlan.trim();
    }

    public String getMonitorPlan() {
        return monitorPlan;
    }

    public void setMonitorPlan(String monitorPlan) {
        this.monitorPlan = monitorPlan == null ? null : monitorPlan.trim();
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