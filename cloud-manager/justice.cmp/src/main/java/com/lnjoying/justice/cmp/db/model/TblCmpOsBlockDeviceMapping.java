package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;
import java.util.Date;

public class TblCmpOsBlockDeviceMapping extends TblCmpOsBlockDeviceMappingKey implements Serializable {
    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private Integer id;

    private String deviceName;

    private Short deleteOnTermination;

    private String snapshotId;

    private String volumeId;

    private Integer volumeSize;

    private Short noDevice;

    private String connectionInfo;

    private String instanceUuid;

    private Integer deleted;

    private String sourceType;

    private String destinationType;

    private String guestFormat;

    private String deviceType;

    private String diskBus;

    private Integer bootIndex;

    private String imageId;

    private String tag;

    private String attachmentId;

    private String volumeType;

    private Short encrypted;

    private String encryptionSecretUuid;

    private String encryptionFormat;

    private String encryptionOptions;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName == null ? null : deviceName.trim();
    }

    public Short getDeleteOnTermination() {
        return deleteOnTermination;
    }

    public void setDeleteOnTermination(Short deleteOnTermination) {
        this.deleteOnTermination = deleteOnTermination;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId == null ? null : snapshotId.trim();
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId == null ? null : volumeId.trim();
    }

    public Integer getVolumeSize() {
        return volumeSize;
    }

    public void setVolumeSize(Integer volumeSize) {
        this.volumeSize = volumeSize;
    }

    public Short getNoDevice() {
        return noDevice;
    }

    public void setNoDevice(Short noDevice) {
        this.noDevice = noDevice;
    }

    public String getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(String connectionInfo) {
        this.connectionInfo = connectionInfo == null ? null : connectionInfo.trim();
    }

    public String getInstanceUuid() {
        return instanceUuid;
    }

    public void setInstanceUuid(String instanceUuid) {
        this.instanceUuid = instanceUuid == null ? null : instanceUuid.trim();
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType == null ? null : sourceType.trim();
    }

    public String getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType == null ? null : destinationType.trim();
    }

    public String getGuestFormat() {
        return guestFormat;
    }

    public void setGuestFormat(String guestFormat) {
        this.guestFormat = guestFormat == null ? null : guestFormat.trim();
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType == null ? null : deviceType.trim();
    }

    public String getDiskBus() {
        return diskBus;
    }

    public void setDiskBus(String diskBus) {
        this.diskBus = diskBus == null ? null : diskBus.trim();
    }

    public Integer getBootIndex() {
        return bootIndex;
    }

    public void setBootIndex(Integer bootIndex) {
        this.bootIndex = bootIndex;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId == null ? null : imageId.trim();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId == null ? null : attachmentId.trim();
    }

    public String getVolumeType() {
        return volumeType;
    }

    public void setVolumeType(String volumeType) {
        this.volumeType = volumeType == null ? null : volumeType.trim();
    }

    public Short getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(Short encrypted) {
        this.encrypted = encrypted;
    }

    public String getEncryptionSecretUuid() {
        return encryptionSecretUuid;
    }

    public void setEncryptionSecretUuid(String encryptionSecretUuid) {
        this.encryptionSecretUuid = encryptionSecretUuid == null ? null : encryptionSecretUuid.trim();
    }

    public String getEncryptionFormat() {
        return encryptionFormat;
    }

    public void setEncryptionFormat(String encryptionFormat) {
        this.encryptionFormat = encryptionFormat == null ? null : encryptionFormat.trim();
    }

    public String getEncryptionOptions() {
        return encryptionOptions;
    }

    public void setEncryptionOptions(String encryptionOptions) {
        this.encryptionOptions = encryptionOptions == null ? null : encryptionOptions.trim();
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
}