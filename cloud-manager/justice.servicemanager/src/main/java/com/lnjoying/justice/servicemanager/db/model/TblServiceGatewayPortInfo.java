package com.lnjoying.justice.servicemanager.db.model;

import java.io.Serializable;
import java.util.Date;

public class TblServiceGatewayPortInfo implements Serializable {
    private String id;

    private String serviceGatewayId;

    private String internalIp;

    private String externalIp;

    private Integer portRangeMin;

    private Integer portRangeMax;

    private Integer listenPortRangeMin;

    private Integer listenPortRangeMax;

    private String description;

    private Integer total;

    private Integer left;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getServiceGatewayId() {
        return serviceGatewayId;
    }

    public void setServiceGatewayId(String serviceGatewayId) {
        this.serviceGatewayId = serviceGatewayId == null ? null : serviceGatewayId.trim();
    }

    public String getInternalIp() {
        return internalIp;
    }

    public void setInternalIp(String internalIp) {
        this.internalIp = internalIp == null ? null : internalIp.trim();
    }

    public String getExternalIp() {
        return externalIp;
    }

    public void setExternalIp(String externalIp) {
        this.externalIp = externalIp == null ? null : externalIp.trim();
    }

    public Integer getPortRangeMin() {
        return portRangeMin;
    }

    public void setPortRangeMin(Integer portRangeMin) {
        this.portRangeMin = portRangeMin;
    }

    public Integer getPortRangeMax() {
        return portRangeMax;
    }

    public void setPortRangeMax(Integer portRangeMax) {
        this.portRangeMax = portRangeMax;
    }

    public Integer getListenPortRangeMin() {
        return listenPortRangeMin;
    }

    public void setListenPortRangeMin(Integer listenPortRangeMin) {
        this.listenPortRangeMin = listenPortRangeMin;
    }

    public Integer getListenPortRangeMax() {
        return listenPortRangeMax;
    }

    public void setListenPortRangeMax(Integer listenPortRangeMax) {
        this.listenPortRangeMax = listenPortRangeMax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
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
}