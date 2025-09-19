package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;

import java.io.Serializable;
import java.util.Date;

public class TblCmpOsFlavors extends TblCmpOsFlavorsKey implements Serializable {
    private Date createdAt;

    private Date updatedAt;

    @ResourceInstanceName
    private String name;

    private Integer id;

    private Integer memoryMb;

    private Integer vcpus;

    private Integer swap;

    private Integer vcpuWeight;

    private Float rxtxFactor;

    private Integer rootGb;

    private Integer ephemeralGb;

    private Short disabled;

    private Short isPublic;

    private String description;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemoryMb() {
        return memoryMb;
    }

    public void setMemoryMb(Integer memoryMb) {
        this.memoryMb = memoryMb;
    }

    public Integer getVcpus() {
        return vcpus;
    }

    public void setVcpus(Integer vcpus) {
        this.vcpus = vcpus;
    }

    public Integer getSwap() {
        return swap;
    }

    public void setSwap(Integer swap) {
        this.swap = swap;
    }

    public Integer getVcpuWeight() {
        return vcpuWeight;
    }

    public void setVcpuWeight(Integer vcpuWeight) {
        this.vcpuWeight = vcpuWeight;
    }

    public Float getRxtxFactor() {
        return rxtxFactor;
    }

    public void setRxtxFactor(Float rxtxFactor) {
        this.rxtxFactor = rxtxFactor;
    }

    public Integer getRootGb() {
        return rootGb;
    }

    public void setRootGb(Integer rootGb) {
        this.rootGb = rootGb;
    }

    public Integer getEphemeralGb() {
        return ephemeralGb;
    }

    public void setEphemeralGb(Integer ephemeralGb) {
        this.ephemeralGb = ephemeralGb;
    }

    public Short getDisabled() {
        return disabled;
    }

    public void setDisabled(Short disabled) {
        this.disabled = disabled;
    }

    public Short getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Short isPublic) {
        this.isPublic = isPublic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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