package com.lnjoying.justice.cluster.manager.db.model;

import java.io.Serializable;
import java.util.Date;

public class TblClusterTmplVerInfo implements Serializable {
    private String versionId;

    private String templateId;

    private String version;

    private String description;

    private String clusterEngineConfig;

    private String tags;

    private Boolean enable;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId == null ? null : versionId.trim();
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId == null ? null : templateId.trim();
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
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

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }
}