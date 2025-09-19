package com.lnjoying.justice.cluster.manager.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import com.lnjoying.justice.commonweb.mapper.MapperModel;

import java.io.Serializable;
import java.util.Date;

@MapperModel(value="cluster")
public class TblClusterInfo implements Serializable {
    private String id;

    private String tmplVersionId;

    @ResourceInstanceName
    private String name;

    private String description;

    private String clusterEngineConfig;

    private String devNeed;

    private String targetNodes;

    private String members;

    private String createType;

    private String clusterType;

    private String labels;

    private String annotations;

    private String owner;

    private String bp;

    private String token;

    private String creator;

    private Integer status;

    private Integer serviceState;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTmplVersionId() {
        return tmplVersionId;
    }

    public void setTmplVersionId(String tmplVersionId) {
        this.tmplVersionId = tmplVersionId == null ? null : tmplVersionId.trim();
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

    public String getClusterEngineConfig() {
        return clusterEngineConfig;
    }

    public void setClusterEngineConfig(String clusterEngineConfig) {
        this.clusterEngineConfig = clusterEngineConfig == null ? null : clusterEngineConfig.trim();
    }

    public String getDevNeed() {
        return devNeed;
    }

    public void setDevNeed(String devNeed) {
        this.devNeed = devNeed == null ? null : devNeed.trim();
    }

    public String getTargetNodes() {
        return targetNodes;
    }

    public void setTargetNodes(String targetNodes) {
        this.targetNodes = targetNodes == null ? null : targetNodes.trim();
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members == null ? null : members.trim();
    }

    public String getCreateType() {
        return createType;
    }

    public void setCreateType(String createType) {
        this.createType = createType == null ? null : createType.trim();
    }

    public String getClusterType() {
        return clusterType;
    }

    public void setClusterType(String clusterType) {
        this.clusterType = clusterType == null ? null : clusterType.trim();
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels == null ? null : labels.trim();
    }

    public String getAnnotations() {
        return annotations;
    }

    public void setAnnotations(String annotations) {
        this.annotations = annotations == null ? null : annotations.trim();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp == null ? null : bp.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getServiceState() {
        return serviceState;
    }

    public void setServiceState(Integer serviceState) {
        this.serviceState = serviceState;
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