package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;

import java.io.Serializable;
import java.util.Date;

public class TblCmpOsSubnets extends TblCmpOsSubnetsKey implements Serializable {
    private String projectId;

    @ResourceInstanceName
    private String name;

    private String networkId;

    private Integer ipVersion;

    private String cidr;

    private String gatewayIp;

    private Short enableDhcp;

    private String ipv6RaMode;

    private String ipv6AddressMode;

    private String subnetpoolId;

    private Long standardAttrId;

    private String segmentId;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private Date createdAt;

    private Date updatedAt;

    private String description;

    private Long revisionNumber;

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

    public Integer getIpVersion() {
        return ipVersion;
    }

    public void setIpVersion(Integer ipVersion) {
        this.ipVersion = ipVersion;
    }

    public String getCidr() {
        return cidr;
    }

    public void setCidr(String cidr) {
        this.cidr = cidr == null ? null : cidr.trim();
    }

    public String getGatewayIp() {
        return gatewayIp;
    }

    public void setGatewayIp(String gatewayIp) {
        this.gatewayIp = gatewayIp == null ? null : gatewayIp.trim();
    }

    public Short getEnableDhcp() {
        return enableDhcp;
    }

    public void setEnableDhcp(Short enableDhcp) {
        this.enableDhcp = enableDhcp;
    }

    public String getIpv6RaMode() {
        return ipv6RaMode;
    }

    public void setIpv6RaMode(String ipv6RaMode) {
        this.ipv6RaMode = ipv6RaMode == null ? null : ipv6RaMode.trim();
    }

    public String getIpv6AddressMode() {
        return ipv6AddressMode;
    }

    public void setIpv6AddressMode(String ipv6AddressMode) {
        this.ipv6AddressMode = ipv6AddressMode == null ? null : ipv6AddressMode.trim();
    }

    public String getSubnetpoolId() {
        return subnetpoolId;
    }

    public void setSubnetpoolId(String subnetpoolId) {
        this.subnetpoolId = subnetpoolId == null ? null : subnetpoolId.trim();
    }

    public Long getStandardAttrId() {
        return standardAttrId;
    }

    public void setStandardAttrId(Long standardAttrId) {
        this.standardAttrId = standardAttrId;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId == null ? null : segmentId.trim();
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
}