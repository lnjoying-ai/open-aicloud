package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;

import java.io.Serializable;
import java.util.Date;

public class TblCmpEipMap extends TblCmpEipMapKey implements Serializable {

    @ResourceInstanceName
    private String mapName;

    private String eipId;

    private String subnetId;

    private String userId;

    private String portId;

    private Boolean isStaticIp;

    private String insideIp;

    private Short status;

    private String bandwidth;

    private Boolean isOneToOne;

    private Date createTime;

    private Date updateTime;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private static final long serialVersionUID = 1L;

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName == null ? null : mapName.trim();
    }

    public String getEipId() {
        return eipId;
    }

    public void setEipId(String eipId) {
        this.eipId = eipId == null ? null : eipId.trim();
    }

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId == null ? null : subnetId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId == null ? null : portId.trim();
    }

    public Boolean getIsStaticIp() {
        return isStaticIp;
    }

    public void setIsStaticIp(Boolean isStaticIp) {
        this.isStaticIp = isStaticIp;
    }

    public String getInsideIp() {
        return insideIp;
    }

    public void setInsideIp(String insideIp) {
        this.insideIp = insideIp == null ? null : insideIp.trim();
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth == null ? null : bandwidth.trim();
    }

    public Boolean getIsOneToOne() {
        return isOneToOne;
    }

    public void setIsOneToOne(Boolean isOneToOne) {
        this.isOneToOne = isOneToOne;
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

    public String getEeBp() {
        return eeBp;
    }

    public void setEeBp(String eeBp) {
        this.eeBp = eeBp == null ? null : eeBp.trim();
    }

    public String getEeUser() {
        return eeUser;
    }

    public void setEeUser(String eeUser) {
        this.eeUser = eeUser == null ? null : eeUser.trim();
    }

    public Integer getEeStatus() {
        return eeStatus;
    }

    public void setEeStatus(Integer eeStatus) {
        this.eeStatus = eeStatus;
    }
}