package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "TblCmpVmInstance")
public class TblCmpVmInstance extends TblCmpVmInstanceKey implements Serializable {
    private String instanceIdFromAgent;

    @ResourceInstanceName
    private String name;

    private Integer phaseStatus;

    private String userId;

    private String nodeId;

    private String flavorId;

    private String imageId;

    private String vpcId;

    private String subnetId;

    private String portId;

    private String staticIp;

    private String hostName;

    private String sysUsername;

    private String sysPassword;

    private String pubkeyId;

    private String description;

    private Date createTime;

    private Date updateTime;

    private String lastNodeId;

    private String destNodeId;

    private String volumeId;

    private String storagePoolId;

    private String instanceGroupId;

    private String bootDev;

    private String osType;

    private String cmpTenantId;

    private String cmpUserId;

    private Integer cpuCount;

    private Integer memSize;

    private String eipId;

    private Integer rootDisk;

    private Integer recycleMemSize;

    private Integer recycleCpuCount;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private Integer chargeType;

    private Integer priceUnit;

    private Integer period;

    private Date expireTime;

    private String eeId;

    private static final long serialVersionUID = 1L;

    public String getInstanceIdFromAgent() {
        return instanceIdFromAgent;
    }

    public void setInstanceIdFromAgent(String instanceIdFromAgent) {
        this.instanceIdFromAgent = instanceIdFromAgent == null ? null : instanceIdFromAgent.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getPhaseStatus() {
        return phaseStatus;
    }

    public void setPhaseStatus(Integer phaseStatus) {
        this.phaseStatus = phaseStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    public String getFlavorId() {
        return flavorId;
    }

    public void setFlavorId(String flavorId) {
        this.flavorId = flavorId == null ? null : flavorId.trim();
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

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId == null ? null : portId.trim();
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

    public String getLastNodeId() {
        return lastNodeId;
    }

    public void setLastNodeId(String lastNodeId) {
        this.lastNodeId = lastNodeId == null ? null : lastNodeId.trim();
    }

    public String getDestNodeId() {
        return destNodeId;
    }

    public void setDestNodeId(String destNodeId) {
        this.destNodeId = destNodeId == null ? null : destNodeId.trim();
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId == null ? null : volumeId.trim();
    }

    public String getStoragePoolId() {
        return storagePoolId;
    }

    public void setStoragePoolId(String storagePoolId) {
        this.storagePoolId = storagePoolId == null ? null : storagePoolId.trim();
    }

    public String getInstanceGroupId() {
        return instanceGroupId;
    }

    public void setInstanceGroupId(String instanceGroupId) {
        this.instanceGroupId = instanceGroupId == null ? null : instanceGroupId.trim();
    }

    public String getBootDev() {
        return bootDev;
    }

    public void setBootDev(String bootDev) {
        this.bootDev = bootDev == null ? null : bootDev.trim();
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType == null ? null : osType.trim();
    }

    public String getCmpTenantId() {
        return cmpTenantId;
    }

    public void setCmpTenantId(String cmpTenantId) {
        this.cmpTenantId = cmpTenantId == null ? null : cmpTenantId.trim();
    }

    public String getCmpUserId() {
        return cmpUserId;
    }

    public void setCmpUserId(String cmpUserId) {
        this.cmpUserId = cmpUserId == null ? null : cmpUserId.trim();
    }

    public Integer getCpuCount() {
        return cpuCount;
    }

    public void setCpuCount(Integer cpuCount) {
        this.cpuCount = cpuCount;
    }

    public Integer getMemSize() {
        return memSize;
    }

    public void setMemSize(Integer memSize) {
        this.memSize = memSize;
    }

    public String getEipId() {
        return eipId;
    }

    public void setEipId(String eipId) {
        this.eipId = eipId == null ? null : eipId.trim();
    }

    public Integer getRootDisk() {
        return rootDisk;
    }

    public void setRootDisk(Integer rootDisk) {
        this.rootDisk = rootDisk;
    }

    public Integer getRecycleMemSize() {
        return recycleMemSize;
    }

    public void setRecycleMemSize(Integer recycleMemSize) {
        this.recycleMemSize = recycleMemSize;
    }

    public Integer getRecycleCpuCount() {
        return recycleCpuCount;
    }

    public void setRecycleCpuCount(Integer recycleCpuCount) {
        this.recycleCpuCount = recycleCpuCount;
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

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    public Integer getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(Integer priceUnit) {
        this.priceUnit = priceUnit;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getEeId() {
        return eeId;
    }

    public void setEeId(String eeId) {
        this.eeId = eeId == null ? null : eeId.trim();
    }
}