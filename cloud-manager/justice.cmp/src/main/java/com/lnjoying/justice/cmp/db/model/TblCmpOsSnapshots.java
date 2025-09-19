package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;

import java.io.Serializable;
import java.util.Date;

public class TblCmpOsSnapshots extends TblCmpOsSnapshotsKey implements Serializable {
    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private Short deleted;

    private String volumeId;

    private String userId;

    private String projectId;

    private String status;

    private String progress;

    private Integer volumeSize;

    private Date scheduledAt;

    @ResourceInstanceName
    private String displayName;

    private String displayDescription;

    private String providerLocation;

    private String encryptionKeyId;

    private String volumeTypeId;

    private String cgsnapshotId;

    private String providerId;

    private String providerAuth;

    private String groupSnapshotId;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress == null ? null : progress.trim();
    }

    public Integer getVolumeSize() {
        return volumeSize;
    }

    public void setVolumeSize(Integer volumeSize) {
        this.volumeSize = volumeSize;
    }

    public Date getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(Date scheduledAt) {
        this.scheduledAt = scheduledAt;
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

    public String getEncryptionKeyId() {
        return encryptionKeyId;
    }

    public void setEncryptionKeyId(String encryptionKeyId) {
        this.encryptionKeyId = encryptionKeyId == null ? null : encryptionKeyId.trim();
    }

    public String getVolumeTypeId() {
        return volumeTypeId;
    }

    public void setVolumeTypeId(String volumeTypeId) {
        this.volumeTypeId = volumeTypeId == null ? null : volumeTypeId.trim();
    }

    public String getCgsnapshotId() {
        return cgsnapshotId;
    }

    public void setCgsnapshotId(String cgsnapshotId) {
        this.cgsnapshotId = cgsnapshotId == null ? null : cgsnapshotId.trim();
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId == null ? null : providerId.trim();
    }

    public String getProviderAuth() {
        return providerAuth;
    }

    public void setProviderAuth(String providerAuth) {
        this.providerAuth = providerAuth == null ? null : providerAuth.trim();
    }

    public String getGroupSnapshotId() {
        return groupSnapshotId;
    }

    public void setGroupSnapshotId(String groupSnapshotId) {
        this.groupSnapshotId = groupSnapshotId == null ? null : groupSnapshotId.trim();
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