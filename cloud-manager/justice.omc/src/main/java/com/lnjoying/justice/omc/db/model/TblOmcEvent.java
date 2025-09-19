package com.lnjoying.justice.omc.db.model;

import com.lnjoying.justice.omc.domain.dto.model.OperationEventDto;

import java.io.Serializable;
import java.util.Date;

public class TblOmcEvent implements Serializable {
    private String id;

    private String name;

    private String bpId;

    private String service;

    private String resource;

    private String action;

    private String resourceId;

    private String resourceInstName;

    private String type;

    private String level;

    private String requestPath;

    private String description;

    private String result;

    private String userId;

    private String operator;

    private Date triggerTime;

    private Date createdAt;

    private String friendlyDescription;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action == null ? null : action.trim();
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId == null ? null : resourceId.trim();
    }

    public String getResourceInstName()
    {
        return resourceInstName;
    }

    public void setResourceInstName(String resourceInstName)
    {
        this.resourceInstName = resourceInstName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath == null ? null : requestPath.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result == null ? null : result.trim();
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

    public String getFriendlyDescription()
    {
        return friendlyDescription;
    }

    public void setFriendlyDescription(String friendlyDescription)
    {
        this.friendlyDescription = friendlyDescription;
    }

    public OperationEventDto toDto()
    {
        return OperationEventDto.builder()
                .id(this.getId())
                .name(this.getName())
                .bpId(this.getBpId())
                .userId(this.getUserId())
                .requestPath(this.getRequestPath())
                .service(this.getService())
                .resource(this.getResource())
                .resourceId(this.getResourceId())
                .resourceInstName(this.getResourceInstName())
                .type(this.getType())
                .action(this.getAction())
                .description(this.getDescription())
                .level(this.getLevel())
                .result(this.getResult())
                .operator(this.getOperator())
                .triggerTime(this.getTriggerTime())
                .friendlyDescription(this.getFriendlyDescription())
                .build();
    }
}