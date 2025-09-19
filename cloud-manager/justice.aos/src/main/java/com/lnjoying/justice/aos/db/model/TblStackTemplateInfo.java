package com.lnjoying.justice.aos.db.model;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "TblStackTemplateInfo")
public class TblStackTemplateInfo implements Serializable {
    private String templateId;

    /**
     * refer TblStackTemplateBaseInfo id
     */
    private String rootId;

    private String version;

    private String aosType;

    private Boolean isUsed;

    private String contentType;

    private String stackCompose;

    private String justiceCompose;

    private String showInputs;

    private Integer status;

    private String description;

    private Date createTime;

    private Date updateTime;

    private String labels;

    private static final long serialVersionUID = 1L;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId == null ? null : templateId.trim();
    }

    public String getRootId()
    {
        return rootId;
    }

    public void setRootId(String rootId)
    {
        this.rootId = rootId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    public String getAosType() {
        return aosType;
    }

    public void setAosType(String aosType) {
        this.aosType = aosType == null ? null : aosType.trim();
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType == null ? null : contentType.trim();
    }

    public String getStackCompose() {
        return stackCompose;
    }

    public void setStackCompose(String stackCompose) {
        this.stackCompose = stackCompose == null ? null : stackCompose.trim();
    }

    public String getJusticeCompose() {
        return justiceCompose;
    }

    public void setJusticeCompose(String justiceCompose) {
        this.justiceCompose = justiceCompose == null ? null : justiceCompose.trim();
    }

    public String getShowInputs() {
        return showInputs;
    }

    public void setShowInputs(String showInputs) {
        this.showInputs = showInputs == null ? null : showInputs.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels == null ? null : labels.trim();
    }
}