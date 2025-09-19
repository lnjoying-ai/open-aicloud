package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;

import java.io.Serializable;
import java.util.Date;

public class TblCmpOsPorts extends TblCmpOsPortsKey implements Serializable {
    private String projectId;

    @ResourceInstanceName
    private String name;

    private String networkId;

    private String macAddress;

    private Short adminStateUp;

    private String status;

    private String deviceId;

    private String deviceOwner;

    private Long standardAttrId;

    private String ipAllocation;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private String dataPlaneStatus;

    private String currentDnsName;

    private String currentDnsDomain;

    private String previousDnsName;

    private String previousDnsDomain;

    private String dnsName;

    private String dnsDomain;

    private String policyId;

    private Short propagateUplinkStatus;

    private Short macLearningEnabled;

    private Date createdAt;

    private Date updatedAt;

    private String description;

    private Long revisionNumber;

    private Short portSecurityEnabled;

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

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId == null ? null : networkId.trim();
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress == null ? null : macAddress.trim();
    }

    public Short getAdminStateUp() {
        return adminStateUp;
    }

    public void setAdminStateUp(Short adminStateUp) {
        this.adminStateUp = adminStateUp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getDeviceOwner() {
        return deviceOwner;
    }

    public void setDeviceOwner(String deviceOwner) {
        this.deviceOwner = deviceOwner == null ? null : deviceOwner.trim();
    }

    public Long getStandardAttrId() {
        return standardAttrId;
    }

    public void setStandardAttrId(Long standardAttrId) {
        this.standardAttrId = standardAttrId;
    }

    public String getIpAllocation() {
        return ipAllocation;
    }

    public void setIpAllocation(String ipAllocation) {
        this.ipAllocation = ipAllocation == null ? null : ipAllocation.trim();
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

    public String getDataPlaneStatus() {
        return dataPlaneStatus;
    }

    public void setDataPlaneStatus(String dataPlaneStatus) {
        this.dataPlaneStatus = dataPlaneStatus == null ? null : dataPlaneStatus.trim();
    }

    public String getCurrentDnsName() {
        return currentDnsName;
    }

    public void setCurrentDnsName(String currentDnsName) {
        this.currentDnsName = currentDnsName == null ? null : currentDnsName.trim();
    }

    public String getCurrentDnsDomain() {
        return currentDnsDomain;
    }

    public void setCurrentDnsDomain(String currentDnsDomain) {
        this.currentDnsDomain = currentDnsDomain == null ? null : currentDnsDomain.trim();
    }

    public String getPreviousDnsName() {
        return previousDnsName;
    }

    public void setPreviousDnsName(String previousDnsName) {
        this.previousDnsName = previousDnsName == null ? null : previousDnsName.trim();
    }

    public String getPreviousDnsDomain() {
        return previousDnsDomain;
    }

    public void setPreviousDnsDomain(String previousDnsDomain) {
        this.previousDnsDomain = previousDnsDomain == null ? null : previousDnsDomain.trim();
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

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId == null ? null : policyId.trim();
    }

    public Short getPropagateUplinkStatus() {
        return propagateUplinkStatus;
    }

    public void setPropagateUplinkStatus(Short propagateUplinkStatus) {
        this.propagateUplinkStatus = propagateUplinkStatus;
    }

    public Short getMacLearningEnabled() {
        return macLearningEnabled;
    }

    public void setMacLearningEnabled(Short macLearningEnabled) {
        this.macLearningEnabled = macLearningEnabled;
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

    public Short getPortSecurityEnabled() {
        return portSecurityEnabled;
    }

    public void setPortSecurityEnabled(Short portSecurityEnabled) {
        this.portSecurityEnabled = portSecurityEnabled;
    }
}