package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "TblCmpImage")
public class TblCmpImage extends TblCmpImageKey implements Serializable {
    private String fileIdFromAgent;

    private String userId;

    private Short imageOsType;

    private Short imageOsVendor;

    private String imageOsVersion;

    @ResourceInstanceName
    private String imageName;

    private Short imageFormat;

    private String agentIp;

    private Short phaseStatus;

    private String phaseInfo;

    private Boolean isPublic;

    private Date createTime;

    private Date updateTime;

    private String description;

    private String vmInstanceId;

    private String imageBase;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private Boolean eeVisibility;

    private static final long serialVersionUID = 1L;

    public String getFileIdFromAgent() {
        return fileIdFromAgent;
    }

    public void setFileIdFromAgent(String fileIdFromAgent) {
        this.fileIdFromAgent = fileIdFromAgent == null ? null : fileIdFromAgent.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Short getImageOsType() {
        return imageOsType;
    }

    public void setImageOsType(Short imageOsType) {
        this.imageOsType = imageOsType;
    }

    public Short getImageOsVendor() {
        return imageOsVendor;
    }

    public void setImageOsVendor(Short imageOsVendor) {
        this.imageOsVendor = imageOsVendor;
    }

    public String getImageOsVersion() {
        return imageOsVersion;
    }

    public void setImageOsVersion(String imageOsVersion) {
        this.imageOsVersion = imageOsVersion == null ? null : imageOsVersion.trim();
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName == null ? null : imageName.trim();
    }

    public Short getImageFormat() {
        return imageFormat;
    }

    public void setImageFormat(Short imageFormat) {
        this.imageFormat = imageFormat;
    }

    public String getAgentIp() {
        return agentIp;
    }

    public void setAgentIp(String agentIp) {
        this.agentIp = agentIp == null ? null : agentIp.trim();
    }

    public Short getPhaseStatus() {
        return phaseStatus;
    }

    public void setPhaseStatus(Short phaseStatus) {
        this.phaseStatus = phaseStatus;
    }

    public String getPhaseInfo() {
        return phaseInfo;
    }

    public void setPhaseInfo(String phaseInfo) {
        this.phaseInfo = phaseInfo == null ? null : phaseInfo.trim();
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getVmInstanceId() {
        return vmInstanceId;
    }

    public void setVmInstanceId(String vmInstanceId) {
        this.vmInstanceId = vmInstanceId == null ? null : vmInstanceId.trim();
    }

    public String getImageBase() {
        return imageBase;
    }

    public void setImageBase(String imageBase) {
        this.imageBase = imageBase == null ? null : imageBase.trim();
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

    public Boolean getEeVisibility() {
        return eeVisibility;
    }

    public void setEeVisibility(Boolean eeVisibility) {
        this.eeVisibility = eeVisibility;
    }
}