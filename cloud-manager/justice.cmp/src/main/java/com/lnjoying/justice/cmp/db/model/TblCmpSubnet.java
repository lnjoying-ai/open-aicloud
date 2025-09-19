package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "TblCmpSubnet")
public class TblCmpSubnet extends TblCmpSubnetKey implements Serializable {
    private String subnetIdFromAgent;

    @ResourceInstanceName
    private String name;

    private String userId;

    private String vpcId;

    private Short phaseStatus;

    private String phaseInfo;

    private Short addressType;

    private String subnetCidr;

    private String gatewayIp;

    private Date createTime;

    private Date updateTime;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private String description;

    private String eeId;

    private static final long serialVersionUID = 1L;

    public String getSubnetIdFromAgent() {
        return subnetIdFromAgent;
    }

    public void setSubnetIdFromAgent(String subnetIdFromAgent) {
        this.subnetIdFromAgent = subnetIdFromAgent == null ? null : subnetIdFromAgent.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId == null ? null : vpcId.trim();
    }

    public Short getPhaseStatus() {
        return phaseStatus;
    }

    public void setPhaseStatus(Short phaseStatus) {
        this.phaseStatus = phaseStatus;
    }

    public String getPhaseInfo() {
        return phaseInfo;
    }

    public void setPhaseInfo(String phaseInfo) {
        this.phaseInfo = phaseInfo == null ? null : phaseInfo.trim();
    }

    public Short getAddressType() {
        return addressType;
    }

    public void setAddressType(Short addressType) {
        this.addressType = addressType;
    }

    public String getSubnetCidr() {
        return subnetCidr;
    }

    public void setSubnetCidr(String subnetCidr) {
        this.subnetCidr = subnetCidr == null ? null : subnetCidr.trim();
    }

    public String getGatewayIp() {
        return gatewayIp;
    }

    public void setGatewayIp(String gatewayIp) {
        this.gatewayIp = gatewayIp == null ? null : gatewayIp.trim();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getEeId() {
        return eeId;
    }

    public void setEeId(String eeId) {
        this.eeId = eeId == null ? null : eeId.trim();
    }
}