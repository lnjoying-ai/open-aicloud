package com.lnjoying.justice.servicemanager.db.model;

import java.io.Serializable;
import java.util.Date;

public class TblServiceGatewayPortAllocation implements Serializable {
    private String id;

    private String portRangeId;

    private String externalIp;

    private String internalIp;

    private Integer port;

    private Integer listenPort;

    private Integer status;

    private String servicePortTargetServiceId;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPortRangeId() {
        return portRangeId;
    }

    public void setPortRangeId(String portRangeId) {
        this.portRangeId = portRangeId == null ? null : portRangeId.trim();
    }

    public String getExternalIp() {
        return externalIp;
    }

    public void setExternalIp(String externalIp) {
        this.externalIp = externalIp == null ? null : externalIp.trim();
    }

    public String getInternalIp() {
        return internalIp;
    }

    public void setInternalIp(String internalIp) {
        this.internalIp = internalIp == null ? null : internalIp.trim();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getListenPort() {
        return listenPort;
    }

    public void setListenPort(Integer listenPort) {
        this.listenPort = listenPort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getServicePortTargetServiceId() {
        return servicePortTargetServiceId;
    }

    public void setServicePortTargetServiceId(String servicePortTargetServiceId) {
        this.servicePortTargetServiceId = servicePortTargetServiceId == null ? null : servicePortTargetServiceId.trim();
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