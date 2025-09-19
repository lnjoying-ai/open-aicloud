package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;

import java.io.Serializable;
import java.util.Date;

public class TblCmpOsBackups extends TblCmpOsBackupsKey implements Serializable {
    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private Short deleted;

    private String volumeId;

    private String userId;

    private String projectId;

    private String host;

    private String availabilityZone;

    @ResourceInstanceName
    private String displayName;

    private String displayDescription;

    private String container;

    private String status;

    private String failReason;

    private String serviceMetadata;

    private String service;

    private Integer size;

    private Integer objectCount;

    private String parentId;

    private String tempVolumeId;

    private String tempSnapshotId;

    private Integer numDependentBackups;

    private String snapshotId;

    private Date dataTimestamp;

    private String restoreVolumeId;

    private String encryptionKeyId;

    private Integer eeStatus;

    private String eeBp;

    private String eeUser;

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

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId == null ? null : volumeId.trim();
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

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public void setAvailabilityZone(String availabilityZone) {
        this.availabilityZone = availabilityZone == null ? null : availabilityZone.trim();
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

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container == null ? null : container.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason == null ? null : failReason.trim();
    }

    public String getServiceMetadata() {
        return serviceMetadata;
    }

    public void setServiceMetadata(String serviceMetadata) {
        this.serviceMetadata = serviceMetadata == null ? null : serviceMetadata.trim();
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service == null ? null : service.trim();
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(Integer objectCount) {
        this.objectCount = objectCount;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getTempVolumeId() {
        return tempVolumeId;
    }

    public void setTempVolumeId(String tempVolumeId) {
        this.tempVolumeId = tempVolumeId == null ? null : tempVolumeId.trim();
    }

    public String getTempSnapshotId() {
        return tempSnapshotId;
    }

    public void setTempSnapshotId(String tempSnapshotId) {
        this.tempSnapshotId = tempSnapshotId == null ? null : tempSnapshotId.trim();
    }

    public Integer getNumDependentBackups() {
        return numDependentBackups;
    }

    public void setNumDependentBackups(Integer numDependentBackups) {
        this.numDependentBackups = numDependentBackups;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId == null ? null : snapshotId.trim();
    }

    public Date getDataTimestamp() {
        return dataTimestamp;
    }

    public void setDataTimestamp(Date dataTimestamp) {
        this.dataTimestamp = dataTimestamp;
    }

    public String getRestoreVolumeId() {
        return restoreVolumeId;
    }

    public void setRestoreVolumeId(String restoreVolumeId) {
        this.restoreVolumeId = restoreVolumeId == null ? null : restoreVolumeId.trim();
    }

    public String getEncryptionKeyId() {
        return encryptionKeyId;
    }

    public void setEncryptionKeyId(String encryptionKeyId) {
        this.encryptionKeyId = encryptionKeyId == null ? null : encryptionKeyId.trim();
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
}