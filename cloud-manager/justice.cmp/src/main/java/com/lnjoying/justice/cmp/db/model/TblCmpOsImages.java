package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;

import java.io.Serializable;
import java.util.Date;

public class TblCmpOsImages extends TblCmpOsImagesKey implements Serializable {

    @ResourceInstanceName
    private String name;

    private Long size;

    private String status;

    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private Short deleted;

    private String diskFormat;

    private String containerFormat;

    private String checksum;

    private String owner;

    private Integer minDisk;

    private Integer minRam;

    private Short protect;

    private Long virualSize;

    private String visibility;

    private Short osHidden;

    private String osHashAlgo;

    private String osHashValue;

    private Integer eeStatus;

    private String eeBp;

    private String eeUser;

    private String properties;

    private static final long serialVersionUID = 1L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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

    public String getDiskFormat() {
        return diskFormat;
    }

    public void setDiskFormat(String diskFormat) {
        this.diskFormat = diskFormat == null ? null : diskFormat.trim();
    }

    public String getContainerFormat() {
        return containerFormat;
    }

    public void setContainerFormat(String containerFormat) {
        this.containerFormat = containerFormat == null ? null : containerFormat.trim();
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum == null ? null : checksum.trim();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    public Integer getMinDisk() {
        return minDisk;
    }

    public void setMinDisk(Integer minDisk) {
        this.minDisk = minDisk;
    }

    public Integer getMinRam() {
        return minRam;
    }

    public void setMinRam(Integer minRam) {
        this.minRam = minRam;
    }

    public Short getProtected() {
        return protect;
    }

    public void setProtected(Short protect) {
        this.protect = protect;
    }

    public Long getVirualSize() {
        return virualSize;
    }

    public void setVirualSize(Long virualSize) {
        this.virualSize = virualSize;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility == null ? null : visibility.trim();
    }

    public Short getOsHidden() {
        return osHidden;
    }

    public void setOsHidden(Short osHidden) {
        this.osHidden = osHidden;
    }

    public String getOsHashAlgo() {
        return osHashAlgo;
    }

    public void setOsHashAlgo(String osHashAlgo) {
        this.osHashAlgo = osHashAlgo == null ? null : osHashAlgo.trim();
    }

    public String getOsHashValue() {
        return osHashValue;
    }

    public void setOsHashValue(String osHashValue) {
        this.osHashValue = osHashValue == null ? null : osHashValue.trim();
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

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties == null ? null : properties.trim();
    }
}