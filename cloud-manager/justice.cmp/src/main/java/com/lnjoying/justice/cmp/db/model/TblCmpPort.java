package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;

import java.io.Serializable;
import java.util.Date;

public class TblCmpPort extends TblCmpPortKey implements Serializable {
    private String subnetId;

    private String portIdFromAgent;

    @ResourceInstanceName
    private String ipAddress;

    private String macAddress;

    private Short phaseStatus;

    private Date createTime;

    private Date updateTime;

    private Short type;

    private String instanceId;

    private Integer ofPort;

    private String hostIdFromAgent;

    private String nodePortId;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private String eipId;

    private Integer eipPhaseStatus;

    private static final long serialVersionUID = 1L;

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId == null ? null : subnetId.trim();
    }

    public String getPortIdFromAgent() {
        return portIdFromAgent;
    }

    public void setPortIdFromAgent(String portIdFromAgent) {
        this.portIdFromAgent = portIdFromAgent == null ? null : portIdFromAgent.trim();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress == null ? null : macAddress.trim();
    }

    public Short getPhaseStatus() {
        return phaseStatus;
    }

    public void setPhaseStatus(Short phaseStatus) {
        this.phaseStatus = phaseStatus;
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

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId == null ? null : instanceId.trim();
    }

    public Integer getOfPort() {
        return ofPort;
    }

    public void setOfPort(Integer ofPort) {
        this.ofPort = ofPort;
    }

    public String getHostIdFromAgent() {
        return hostIdFromAgent;
    }

    public void setHostIdFromAgent(String hostIdFromAgent) {
        this.hostIdFromAgent = hostIdFromAgent == null ? null : hostIdFromAgent.trim();
    }

    public String getNodePortId() {
        return nodePortId;
    }

    public void setNodePortId(String nodePortId) {
        this.nodePortId = nodePortId == null ? null : nodePortId.trim();
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

    public String getEipId() {
        return eipId;
    }

    public void setEipId(String eipId) {
        this.eipId = eipId == null ? null : eipId.trim();
    }

    public Integer getEipPhaseStatus() {
        return eipPhaseStatus;
    }

    public void setEipPhaseStatus(Integer eipPhaseStatus) {
        this.eipPhaseStatus = eipPhaseStatus;
    }
}