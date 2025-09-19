package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "TblCmpBaremetalInstance")
public class TblCmpBaremetalInstance extends TblCmpBaremetalInstanceKey implements Serializable {

    @ResourceInstanceName
    private String name;

    private String deviceId;

    private Integer phaseStatus;

    private String imageId;

    private String vpcId;

    private String subnetId;

    private String staticIp;

    private String hostName;

    private String sysUsername;

    private String sysPassword;

    private String pubkeyId;

    private String iscsiTarget;

    private String iscsiInitiator;

    private String iscsiIpport;

    private String shareIdFromAgent;

    private String nicIdFromAgent;

    private String portIdFromAgent;

    private String instanceIdFromAgent;

    private String description;

    private Date createTime;

    private Date updateTime;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private static final long serialVersionUID = 1L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public Integer getPhaseStatus() {
        return phaseStatus;
    }

    public void setPhaseStatus(Integer phaseStatus) {
        this.phaseStatus = phaseStatus;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId == null ? null : imageId.trim();
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId == null ? null : vpcId.trim();
    }

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId == null ? null : subnetId.trim();
    }

    public String getStaticIp() {
        return staticIp;
    }

    public void setStaticIp(String staticIp) {
        this.staticIp = staticIp == null ? null : staticIp.trim();
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName == null ? null : hostName.trim();
    }

    public String getSysUsername() {
        return sysUsername;
    }

    public void setSysUsername(String sysUsername) {
        this.sysUsername = sysUsername == null ? null : sysUsername.trim();
    }

    public String getSysPassword() {
        return sysPassword;
    }

    public void setSysPassword(String sysPassword) {
        this.sysPassword = sysPassword == null ? null : sysPassword.trim();
    }

    public String getPubkeyId() {
        return pubkeyId;
    }

    public void setPubkeyId(String pubkeyId) {
        this.pubkeyId = pubkeyId == null ? null : pubkeyId.trim();
    }

    public String getIscsiTarget() {
        return iscsiTarget;
    }

    public void setIscsiTarget(String iscsiTarget) {
        this.iscsiTarget = iscsiTarget == null ? null : iscsiTarget.trim();
    }

    public String getIscsiInitiator() {
        return iscsiInitiator;
    }

    public void setIscsiInitiator(String iscsiInitiator) {
        this.iscsiInitiator = iscsiInitiator == null ? null : iscsiInitiator.trim();
    }

    public String getIscsiIpport() {
        return iscsiIpport;
    }

    public void setIscsiIpport(String iscsiIpport) {
        this.iscsiIpport = iscsiIpport == null ? null : iscsiIpport.trim();
    }

    public String getShareIdFromAgent() {
        return shareIdFromAgent;
    }

    public void setShareIdFromAgent(String shareIdFromAgent) {
        this.shareIdFromAgent = shareIdFromAgent == null ? null : shareIdFromAgent.trim();
    }

    public String getNicIdFromAgent() {
        return nicIdFromAgent;
    }

    public void setNicIdFromAgent(String nicIdFromAgent) {
        this.nicIdFromAgent = nicIdFromAgent == null ? null : nicIdFromAgent.trim();
    }

    public String getPortIdFromAgent() {
        return portIdFromAgent;
    }

    public void setPortIdFromAgent(String portIdFromAgent) {
        this.portIdFromAgent = portIdFromAgent == null ? null : portIdFromAgent.trim();
    }

    public String getInstanceIdFromAgent() {
        return instanceIdFromAgent;
    }

    public void setInstanceIdFromAgent(String instanceIdFromAgent) {
        this.instanceIdFromAgent = instanceIdFromAgent == null ? null : instanceIdFromAgent.trim();
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