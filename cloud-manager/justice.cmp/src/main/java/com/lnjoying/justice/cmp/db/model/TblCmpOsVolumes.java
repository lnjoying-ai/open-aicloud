package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;

import java.io.Serializable;
import java.util.Date;

public class TblCmpOsVolumes extends TblCmpOsVolumesKey implements Serializable {
    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private Short deleted;

    private String ec2Id;

    private String userId;

    private String projectId;

    private String host;

    private Integer size;

    private String availabilityZone;

    private String status;

    private String attachStatus;

    private Date scheduledAt;

    private Date launchedAt;

    private Date terminatedAt;

    @ResourceInstanceName
    private String displayName;

    private String displayDescription;

    private String providerLocation;

    private String providerAuth;

    private String snapshotId;

    private String volumeTypeId;

    private String sourceVolid;

    private Short bootable;

    private String providerGeometry;

    private String nameId;

    private String encryptionKeyId;

    private String migrationStatus;

    private String replicationStatus;

    private String replicationExtendedStatus;

    private String replicationDriverData;

    private String consistencygroupId;

    private String providerId;

    private Short multiattch;

    private String previousStatus;

    private String clusterName;

    private String groupId;

    private String serviceUuid;

    private Short sharedTargets;

    private Integer eeStatus;

    private String eeBp;

    private String eeUser;

    private Integer chargeType;

    private Integer priceUnit;

    private Integer period;

    private String volumeGlanceMetadata;

    private Date expireTime;

    private static final long serialVersionUID = 1L;

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

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Short getDeleted() {
        return deleted;
    }

    public void setDeleted(Short deleted) {
        this.deleted = deleted;
    }

    public String getEc2Id() {
        return ec2Id;
    }

    public void setEc2Id(String ec2Id) {
        this.ec2Id = ec2Id == null ? null : ec2Id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host == null ? null : host.trim();
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public void setAvailabilityZone(String availabilityZone) {
        this.availabilityZone = availabilityZone == null ? null : availabilityZone.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getAttachStatus() {
        return attachStatus;
    }

    public void setAttachStatus(String attachStatus) {
        this.attachStatus = attachStatus == null ? null : attachStatus.trim();
    }

    public Date getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(Date scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public Date getLaunchedAt() {
        return launchedAt;
    }

    public void setLaunchedAt(Date launchedAt) {
        this.launchedAt = launchedAt;
    }

    public Date getTerminatedAt() {
        return terminatedAt;
    }

    public void setTerminatedAt(Date terminatedAt) {
        this.terminatedAt = terminatedAt;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName == null ? null : displayName.trim();
    }

    public String getDisplayDescription() {
        return displayDescription;
    }

    public void setDisplayDescription(String displayDescription) {
        this.displayDescription = displayDescription == null ? null : displayDescription.trim();
    }

    public String getProviderLocation() {
        return providerLocation;
    }

    public void setProviderLocation(String providerLocation) {
        this.providerLocation = providerLocation == null ? null : providerLocation.trim();
    }

    public String getProviderAuth() {
        return providerAuth;
    }

    public void setProviderAuth(String providerAuth) {
        this.providerAuth = providerAuth == null ? null : providerAuth.trim();
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId == null ? null : snapshotId.trim();
    }

    public String getVolumeTypeId() {
        return volumeTypeId;
    }

    public void setVolumeTypeId(String volumeTypeId) {
        this.volumeTypeId = volumeTypeId == null ? null : volumeTypeId.trim();
    }

    public String getSourceVolid() {
        return sourceVolid;
    }

    public void setSourceVolid(String sourceVolid) {
        this.sourceVolid = sourceVolid == null ? null : sourceVolid.trim();
    }

    public Short getBootable() {
        return bootable;
    }

    public void setBootable(Short bootable) {
        this.bootable = bootable;
    }

    public String getProviderGeometry() {
        return providerGeometry;
    }

    public void setProviderGeometry(String providerGeometry) {
        this.providerGeometry = providerGeometry == null ? null : providerGeometry.trim();
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId == null ? null : nameId.trim();
    }

    public String getEncryptionKeyId() {
        return encryptionKeyId;
    }

    public void setEncryptionKeyId(String encryptionKeyId) {
        this.encryptionKeyId = encryptionKeyId == null ? null : encryptionKeyId.trim();
    }

    public String getMigrationStatus() {
        return migrationStatus;
    }

    public void setMigrationStatus(String migrationStatus) {
        this.migrationStatus = migrationStatus == null ? null : migrationStatus.trim();
    }

    public String getReplicationStatus() {
        return replicationStatus;
    }

    public void setReplicationStatus(String replicationStatus) {
        this.replicationStatus = replicationStatus == null ? null : replicationStatus.trim();
    }

    public String getReplicationExtendedStatus() {
        return replicationExtendedStatus;
    }

    public void setReplicationExtendedStatus(String replicationExtendedStatus) {
        this.replicationExtendedStatus = replicationExtendedStatus == null ? null : replicationExtendedStatus.trim();
    }

    public String getReplicationDriverData() {
        return replicationDriverData;
    }

    public void setReplicationDriverData(String replicationDriverData) {
        this.replicationDriverData = replicationDriverData == null ? null : replicationDriverData.trim();
    }

    public String getConsistencygroupId() {
        return consistencygroupId;
    }

    public void setConsistencygroupId(String consistencygroupId) {
        this.consistencygroupId = consistencygroupId == null ? null : consistencygroupId.trim();
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId == null ? null : providerId.trim();
    }

    public Short getMultiattch() {
        return multiattch;
    }

    public void setMultiattch(Short multiattch) {
        this.multiattch = multiattch;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus == null ? null : previousStatus.trim();
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName == null ? null : clusterName.trim();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    public String getServiceUuid() {
        return serviceUuid;
    }

    public void setServiceUuid(String serviceUuid) {
        this.serviceUuid = serviceUuid == null ? null : serviceUuid.trim();
    }

    public Short getSharedTargets() {
        return sharedTargets;
    }

    public void setSharedTargets(Short sharedTargets) {
        this.sharedTargets = sharedTargets;
    }

    public Integer getEeStatus() {
        return eeStatus;
    }

    public void setEeStatus(Integer eeStatus) {
        this.eeStatus = eeStatus;
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

    public String getVolumeGlanceMetadata() {
        return volumeGlanceMetadata;
    }

    public void setVolumeGlanceMetadata(String volumeGlanceMetadata) {
        this.volumeGlanceMetadata = volumeGlanceMetadata == null ? null : volumeGlanceMetadata.trim();
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}