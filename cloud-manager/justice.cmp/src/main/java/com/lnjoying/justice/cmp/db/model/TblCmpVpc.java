package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "TblCmpVpc")
public class TblCmpVpc extends TblCmpVpcKey implements Serializable {
    private String vpcIdFromAgent;

    @ResourceInstanceName
    private String name;

    private String userId;

    private Integer vlanId;

    private Short phaseStatus;

    private String phaseInfo;

    private Short addressType;

    private String vpcCidr;

    private Date createTime;

    private Date updateTime;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private String description;

    private String eeId;

    private static final long serialVersionUID = 1L;

    public String getVpcIdFromAgent() {
        return vpcIdFromAgent;
    }

    public void setVpcIdFromAgent(String vpcIdFromAgent) {
        this.vpcIdFromAgent = vpcIdFromAgent == null ? null : vpcIdFromAgent.trim();
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

    public Integer getVlanId() {
        return vlanId;
    }

    public void setVlanId(Integer vlanId) {
        this.vlanId = vlanId;
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

    public String getVpcCidr() {
        return vpcCidr;
    }

    public void setVpcCidr(String vpcCidr) {
        this.vpcCidr = vpcCidr == null ? null : vpcCidr.trim();
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