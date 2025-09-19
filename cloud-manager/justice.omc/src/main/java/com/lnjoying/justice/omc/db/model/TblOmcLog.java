package com.lnjoying.justice.omc.db.model;

import com.lnjoying.justice.omc.domain.dto.model.ApiLogDto;

import java.io.Serializable;
import java.util.Date;

public class TblOmcLog implements Serializable {
    private String id;

    private String level;

    private String bpId;

    private String service;

    private String resource;

    private String resourceId;

    private String action;

    private String accessIp;

    private String userAgent;

    private String description;

    private String httpMethod;

    private String requestPath;

    private String inParams;

    private String outParams;

    private String result;

    private Integer executionTime;

    private String userId;

    private String operator;

    private Date triggerTime;

    private Date createdAt;

    private String operationType;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getBpId() {
        return bpId;
    }

    public void setBpId(String bpId) {
        this.bpId = bpId == null ? null : bpId.trim();
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service == null ? null : service.trim();
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource == null ? null : resource.trim();
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId == null ? null : resourceId.trim();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action == null ? null : action.trim();
    }

    public String getAccessIp() {
        return accessIp;
    }

    public void setAccessIp(String accessIp) {
        this.accessIp = accessIp == null ? null : accessIp.trim();
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent == null ? null : userAgent.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getHttpMethod()
    {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod)
    {
        this.httpMethod = httpMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath == null ? null : requestPath.trim();
    }

    public String getInParams() {
        return inParams;
    }

    public void setInParams(String inParams) {
        this.inParams = inParams == null ? null : inParams.trim();
    }

    public String getOutParams() {
        return outParams;
    }

    public void setOutParams(String outParams) {
        this.outParams = outParams == null ? null : outParams.trim();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result == null ? null : result.trim();
    }

    public Integer getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getOperationType()
    {
        return operationType;
    }

    public void setOperationType(String operationType)
    {
        this.operationType = operationType;
    }

    public ApiLogDto toDto()
    {
        return ApiLogDto.builder()
                .id(this.getId())
                .bpId(this.getBpId())
                .userId(this.getUserId())
                .accessIp(this.getAccessIp())
                .userAgent(this.getUserAgent())
                .requestPath(this.getRequestPath())
                .httpMethod(this.getHttpMethod())
                .service(this.getService())
                .resource(this.getResource())
                .resourceId(this.getResourceId())
                .action(this.getAction())
                .description(this.getDescription())
                .inParams(this.getInParams())
                .outParams(this.getOutParams())
                .level(this.getLevel())
                .result(this.getResult())
                .executionTime(this.getExecutionTime())
                .operator(this.getOperator())
                .triggerTime(this.getTriggerTime())
                .operationType(this.getOperationType())
                .build();
    }
}