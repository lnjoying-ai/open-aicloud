package com.lnjoying.justice.ecrm.db.model;

import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.ecrm.domain.dto.model.Location;
import com.lnjoying.justice.ecrm.domain.dto.model.RegionSiteInfo;
import com.lnjoying.justice.ecrm.domain.dto.model.SiteInfo;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


@ApiModel(value = "TblSiteInfo")
public class TblSiteInfo implements Serializable {
    private String siteId;

    private String regionId;

    private String siteName;

    private String siteLocation;

    private String siteDesc;

    private String scope;

    private String userId;

    private String bpId;

    private String adcode;

    private Integer status;

    private String labels;

    private String taints;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId == null ? null : siteId.trim();
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName == null ? null : siteName.trim();
    }

    public String getSiteLocation() {
        return siteLocation;
    }

    public void setSiteLocation(String siteLocation) {
        this.siteLocation = siteLocation == null ? null : siteLocation.trim();
    }

    public String getSiteDesc() {
        return siteDesc;
    }

    public void setSiteDesc(String siteDesc) {
        this.siteDesc = siteDesc == null ? null : siteDesc.trim();
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope == null ? null : scope.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getBpId() {
        return bpId;
    }

    public void setBpId(String bpId) {
        this.bpId = bpId == null ? null : bpId.trim();
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode == null ? null : adcode.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getTaints() {
        return taints;
    }

    public void setTaints(String taints) {
        this.taints = taints;
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

    public SiteInfo of(TblSiteInfo tblSiteInfo, TblRegionInfo tblRegionInfo)
    {
        RegionSiteInfo regionSiteInfo = new RegionSiteInfo();
        SiteInfo siteInfo = new SiteInfo();
        siteInfo.setRegion_name(tblRegionInfo.getRegionName());
        siteInfo.setInfo(tblSiteInfo);
        if (StringUtils.isNotEmpty(tblSiteInfo.getLabels()))
        {
            Map<String, String> labels = JsonUtils.fromJson(tblSiteInfo.getLabels(), new TypeToken<Map<String, String>>(){}.getType());
            siteInfo.setLabels(labels);
        }
        if (StringUtils.isNotEmpty(tblSiteInfo.getTaints()))
        {
            Map<String, String> taints = JsonUtils.fromJson(tblSiteInfo.getTaints(), new TypeToken<Map<String, String>>(){}.getType());
            siteInfo.setTaints(taints);
        }
        siteInfo.setLocation(JsonUtils.fromJson(tblSiteInfo.getSiteLocation(), Location.class));
        return siteInfo;
    }
}