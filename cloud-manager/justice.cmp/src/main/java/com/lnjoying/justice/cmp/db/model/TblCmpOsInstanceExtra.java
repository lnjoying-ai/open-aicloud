package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;
import java.util.Date;

public class TblCmpOsInstanceExtra extends TblCmpOsInstanceExtraKey implements Serializable {
    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private Integer deleted;

    private Integer id;

    private String numaTopology;

    private String pciRequests;

    private String flavor;

    private String vcpuModel;

    private String migrationContext;

    private String keypairs;

    private String deviceMetadata;

    private String trustedCerts;

    private String vpmems;

    private String resources;

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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumaTopology() {
        return numaTopology;
    }

    public void setNumaTopology(String numaTopology) {
        this.numaTopology = numaTopology == null ? null : numaTopology.trim();
    }

    public String getPciRequests() {
        return pciRequests;
    }

    public void setPciRequests(String pciRequests) {
        this.pciRequests = pciRequests == null ? null : pciRequests.trim();
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor == null ? null : flavor.trim();
    }

    public String getVcpuModel() {
        return vcpuModel;
    }

    public void setVcpuModel(String vcpuModel) {
        this.vcpuModel = vcpuModel == null ? null : vcpuModel.trim();
    }

    public String getMigrationContext() {
        return migrationContext;
    }

    public void setMigrationContext(String migrationContext) {
        this.migrationContext = migrationContext == null ? null : migrationContext.trim();
    }

    public String getKeypairs() {
        return keypairs;
    }

    public void setKeypairs(String keypairs) {
        this.keypairs = keypairs == null ? null : keypairs.trim();
    }

    public String getDeviceMetadata() {
        return deviceMetadata;
    }

    public void setDeviceMetadata(String deviceMetadata) {
        this.deviceMetadata = deviceMetadata == null ? null : deviceMetadata.trim();
    }

    public String getTrustedCerts() {
        return trustedCerts;
    }

    public void setTrustedCerts(String trustedCerts) {
        this.trustedCerts = trustedCerts == null ? null : trustedCerts.trim();
    }

    public String getVpmems() {
        return vpmems;
    }

    public void setVpmems(String vpmems) {
        this.vpmems = vpmems == null ? null : vpmems.trim();
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources == null ? null : resources.trim();
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