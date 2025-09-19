package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;

import java.io.Serializable;
import java.util.Date;

public class TblCmpOsNetworks extends TblCmpOsNetworksKey implements Serializable {
    private String projectId;

    @ResourceInstanceName
    private String name;

    private String status;

    private Short adminStateUp;

    private Short vlanTransparent;

    private Long standardAttrId;

    private String availabilityZoneHints;

    private Integer mtu;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private Short shared;

    private Date createdAt;

    private Date updatedAt;

    private String description;

    private Long revisionNumber;

    private Short isDefault;

    private String dnsDomain;

    private Short portSecurityEnabled;

    private String policyId;

    private static final long serialVersionUID = 1L;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Short getAdminStateUp() {
        return adminStateUp;
    }

    public void setAdminStateUp(Short adminStateUp) {
        this.adminStateUp = adminStateUp;
    }

    public Short getVlanTransparent() {
        return vlanTransparent;
    }

    public void setVlanTransparent(Short vlanTransparent) {
        this.vlanTransparent = vlanTransparent;
    }

    public Long getStandardAttrId() {
        return standardAttrId;
    }

    public void setStandardAttrId(Long standardAttrId) {
        this.standardAttrId = standardAttrId;
    }

    public String getAvailabilityZoneHints() {
        return availabilityZoneHints;
    }

    public void setAvailabilityZoneHints(String availabilityZoneHints) {
        this.availabilityZoneHints = availabilityZoneHints == null ? null : availabilityZoneHints.trim();
    }

    public Integer getMtu() {
        return mtu;
    }

    public void setMtu(Integer mtu) {
        this.mtu = mtu;
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

    public Short getShared() {
        return shared;
    }

    public void setShared(Short shared) {
        this.shared = shared;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Long getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(Long revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public Short getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Short isDefault) {
        this.isDefault = isDefault;
    }

    public String getDnsDomain() {
        return dnsDomain;
    }

    public void setDnsDomain(String dnsDomain) {
        this.dnsDomain = dnsDomain == null ? null : dnsDomain.trim();
    }

    public Short getPortSecurityEnabled() {
        return portSecurityEnabled;
    }

    public void setPortSecurityEnabled(Short portSecurityEnabled) {
        this.portSecurityEnabled = portSecurityEnabled;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId == null ? null : policyId.trim();
    }
}