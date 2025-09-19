package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "TblCmpPortMap")
public class TblCmpPortMap extends TblCmpPortMapKey implements Serializable {
    private String eipMapId;

    private Short protocol;

    @ResourceInstanceName
    private Integer globalPort;

    private Integer localPort;

    private Short status;

    private Date createTime;

    private Date updateTime;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private static final long serialVersionUID = 1L;

    public String getEipMapId() {
        return eipMapId;
    }

    public void setEipMapId(String eipMapId) {
        this.eipMapId = eipMapId == null ? null : eipMapId.trim();
    }

    public Short getProtocol() {
        return protocol;
    }

    public void setProtocol(Short protocol) {
        this.protocol = protocol;
    }

    public Integer getGlobalPort() {
        return globalPort;
    }

    public void setGlobalPort(Integer globalPort) {
        this.globalPort = globalPort;
    }

    public Integer getLocalPort() {
        return localPort;
    }

    public void setLocalPort(Integer localPort) {
        this.localPort = localPort;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
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