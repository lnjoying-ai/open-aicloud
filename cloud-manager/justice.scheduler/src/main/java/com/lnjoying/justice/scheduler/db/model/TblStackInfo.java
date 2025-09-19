package com.lnjoying.justice.scheduler.db.model;

import java.io.Serializable;
import java.util.Date;

public class TblStackInfo implements Serializable {
    private String stackId;

    private String specId;

    private String name;

    private String bpId;

    private String userId;

    private String createUserId;

    private Integer status;

    private String dstNode;

    private String labels;

    private String devNeedInfo;

    private Boolean autoRun;

    private Date createTime;

    private Date updateTime;

    private Integer sendCreateNum;

    private Integer startNum;

    private Integer failNum;

    private Integer eventType;

    private Date reportTime;

    private static final long serialVersionUID = 1L;

    public String getStackId() {
        return stackId;
    }

    public void setStackId(String stackId) {
        this.stackId = stackId == null ? null : stackId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getBpId() {
        return bpId;
    }

    public void setBpId(String bpId) {
        this.bpId = bpId == null ? null : bpId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
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

    public String getDevNeedInfo() {
        return devNeedInfo;
    }

    public void setDevNeedInfo(String devNeedInfo) {
        this.devNeedInfo = devNeedInfo == null ? null : devNeedInfo.trim();
    }

    public Boolean getAutoRun() {
        return autoRun;
    }

    public void setAutoRun(Boolean autoRun) {
        this.autoRun = autoRun;
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

    public Integer getSendCreateNum() {
        return sendCreateNum;
    }

    public void setSendCreateNum(Integer sendCreateNum) {
        this.sendCreateNum = sendCreateNum;
    }

    public Integer getStartNum() {
        return startNum;
    }

    public void setStartNum(Integer startNum) {
        this.startNum = startNum;
    }

    public Integer getFailNum() {
        return failNum;
    }

    public void setFailNum(Integer failNum) {
        this.failNum = failNum;
    }

    public String getSpecId()
    {
        return specId;
    }

    public void setSpecId(String specId)
    {
        this.specId = specId;
    }

    public String getDstNode()
    {
        return dstNode;
    }

    public void setDstNode(String dstNode)
    {
        this.dstNode = dstNode;
    }

    public Integer getEventType()
    {
        return eventType;
    }

    public void setEventType(Integer eventType)
    {
        this.eventType = eventType;
    }

    public Date getReportTime()
    {
        return reportTime;
    }

    public void setReportTime(Date reportTime)
    {
        this.reportTime = reportTime;
    }
}