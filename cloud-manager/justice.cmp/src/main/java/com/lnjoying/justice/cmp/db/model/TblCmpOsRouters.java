package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;

import java.io.Serializable;
import java.util.Date;

public class TblCmpOsRouters extends TblCmpOsRoutersKey implements Serializable {
    private String projectId;

    @ResourceInstanceName
    private String name;

    private String status;

    private Short adminStateUp;

    private String gwPortId;

    private Short enableSnat;

    private Long standardAttrId;

    private String flavorId;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private Short distributed;

    private Short serviceRouter;

    private Short ha;

    private Integer haVrId;

    private String availabilityZoneHints;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Short getAdminStateUp() {
        return adminStateUp;
    }

    public void setAdminStateUp(Short adminStateUp) {
        this.adminStateUp = adminStateUp;
    }

    public String getGwPortId() {
        return gwPortId;
    }

    public void setGwPortId(String gwPortId) {
        this.gwPortId = gwPortId == null ? null : gwPortId.trim();
    }

    public Short getEnableSnat() {
        return enableSnat;
    }

    public void setEnableSnat(Short enableSnat) {
        this.enableSnat = enableSnat;
    }

    public Long getStandardAttrId() {
        return standardAttrId;
    }

    public void setStandardAttrId(Long standardAttrId) {
        this.standardAttrId = standardAttrId;
    }

    public String getFlavorId() {
        return flavorId;
    }

    public void setFlavorId(String flavorId) {
        this.flavorId = flavorId == null ? null : flavorId.trim();
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

    public Short getDistributed() {
        return distributed;
    }

    public void setDistributed(Short distributed) {
        this.distributed = distributed;
    }

    public Short getServiceRouter() {
        return serviceRouter;
    }

    public void setServiceRouter(Short serviceRouter) {
        this.serviceRouter = serviceRouter;
    }

    public Short getHa() {
        return ha;
    }

    public void setHa(Short ha) {
        this.ha = ha;
    }

    public Integer getHaVrId() {
        return haVrId;
    }

    public void setHaVrId(Integer haVrId) {
        this.haVrId = haVrId;
    }

    public String getAvailabilityZoneHints() {
        return availabilityZoneHints;
    }

    public void setAvailabilityZoneHints(String availabilityZoneHints) {
        this.availabilityZoneHints = availabilityZoneHints == null ? null : availabilityZoneHints.trim();
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