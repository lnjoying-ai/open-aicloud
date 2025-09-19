package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;
import java.util.Date;

public class TblCmpOsVolumeAttachment extends TblCmpOsVolumeAttachmentKey implements Serializable {
    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private Short deleted;

    private String volumeId;

    private String attachedHost;

    private String instanceUuid;

    private String mountpoint;

    private Date attachTime;

    private Date detachTime;

    private String attachMode;

    private String attachStatus;

    private String connectionInfo;

    private String connector;

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

    public String getAttachedHost() {
        return attachedHost;
    }

    public void setAttachedHost(String attachedHost) {
        this.attachedHost = attachedHost == null ? null : attachedHost.trim();
    }

    public String getInstanceUuid() {
        return instanceUuid;
    }

    public void setInstanceUuid(String instanceUuid) {
        this.instanceUuid = instanceUuid == null ? null : instanceUuid.trim();
    }

    public String getMountpoint() {
        return mountpoint;
    }

    public void setMountpoint(String mountpoint) {
        this.mountpoint = mountpoint == null ? null : mountpoint.trim();
    }

    public Date getAttachTime() {
        return attachTime;
    }

    public void setAttachTime(Date attachTime) {
        this.attachTime = attachTime;
    }

    public Date getDetachTime() {
        return detachTime;
    }

    public void setDetachTime(Date detachTime) {
        this.detachTime = detachTime;
    }

    public String getAttachMode() {
        return attachMode;
    }

    public void setAttachMode(String attachMode) {
        this.attachMode = attachMode == null ? null : attachMode.trim();
    }

    public String getAttachStatus() {
        return attachStatus;
    }

    public void setAttachStatus(String attachStatus) {
        this.attachStatus = attachStatus == null ? null : attachStatus.trim();
    }

    public String getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(String connectionInfo) {
        this.connectionInfo = connectionInfo == null ? null : connectionInfo.trim();
    }

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector == null ? null : connector.trim();
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