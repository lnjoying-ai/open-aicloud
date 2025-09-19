package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;

import java.io.Serializable;
import java.util.Date;

public class TblCmpOsFloatingips extends TblCmpOsFloatingipsKey implements Serializable {
    private String projectId;

    @ResourceInstanceName
    private String floatingIpAddress;

    private String floatingNetworkId;

    private String floatingPortId;

    private String fixedPortId;

    private String fixedIpAddress;

    private String routerId;

    private String lastKnownRouterId;

    private String status;

    private Long standardAttrId;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private String dnsName;

    private String dnsDomain;

    private String publishedDnsName;

    private String publishedDnsDomain;

    private Date createdAt;

    private Date updatedAt;

    private String description;

    private Long revisionNumber;

    private String policyId;

    private static final long serialVersionUID = 1L;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getFloatingIpAddress() {
        return floatingIpAddress;
    }

    public void setFloatingIpAddress(String floatingIpAddress) {
        this.floatingIpAddress = floatingIpAddress == null ? null : floatingIpAddress.trim();
    }

    public String getFloatingNetworkId() {
        return floatingNetworkId;
    }

    public void setFloatingNetworkId(String floatingNetworkId) {
        this.floatingNetworkId = floatingNetworkId == null ? null : floatingNetworkId.trim();
    }

    public String getFloatingPortId() {
        return floatingPortId;
    }

    public void setFloatingPortId(String floatingPortId) {
        this.floatingPortId = floatingPortId == null ? null : floatingPortId.trim();
    }

    public String getFixedPortId() {
        return fixedPortId;
    }

    public void setFixedPortId(String fixedPortId) {
        this.fixedPortId = fixedPortId == null ? null : fixedPortId.trim();
    }

    public String getFixedIpAddress() {
        return fixedIpAddress;
    }

    public void setFixedIpAddress(String fixedIpAddress) {
        this.fixedIpAddress = fixedIpAddress == null ? null : fixedIpAddress.trim();
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId == null ? null : routerId.trim();
    }

    public String getLastKnownRouterId() {
        return lastKnownRouterId;
    }

    public void setLastKnownRouterId(String lastKnownRouterId) {
        this.lastKnownRouterId = lastKnownRouterId == null ? null : lastKnownRouterId.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Long getStandardAttrId() {
        return standardAttrId;
    }

    public void setStandardAttrId(Long standardAttrId) {
        this.standardAttrId = standardAttrId;
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

    public String getDnsName() {
        return dnsName;
    }

    public void setDnsName(String dnsName) {
        this.dnsName = dnsName == null ? null : dnsName.trim();
    }

    public String getDnsDomain() {
        return dnsDomain;
    }

    public void setDnsDomain(String dnsDomain) {
        this.dnsDomain = dnsDomain == null ? null : dnsDomain.trim();
    }

    public String getPublishedDnsName() {
        return publishedDnsName;
    }

    public void setPublishedDnsName(String publishedDnsName) {
        this.publishedDnsName = publishedDnsName == null ? null : publishedDnsName.trim();
    }

    public String getPublishedDnsDomain() {
        return publishedDnsDomain;
    }

    public void setPublishedDnsDomain(String publishedDnsDomain) {
        this.publishedDnsDomain = publishedDnsDomain == null ? null : publishedDnsDomain.trim();
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

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId == null ? null : policyId.trim();
    }
}