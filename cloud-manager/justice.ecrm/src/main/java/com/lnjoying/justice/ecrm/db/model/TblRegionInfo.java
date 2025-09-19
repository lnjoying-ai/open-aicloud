package com.lnjoying.justice.ecrm.db.model;

import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.ecrm.db.repo.RegionGwRepository;
import com.lnjoying.justice.ecrm.domain.dto.model.NameIDInfo;
import com.lnjoying.justice.ecrm.domain.dto.model.RegionInfo;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;


@ApiModel(value = "TblRegionInfo")
public class TblRegionInfo implements Serializable {
    private String regionId;

    @ResourceInstanceName
    private String regionName;

    private String regionDesc;

    private Integer status;

    private String labels;

    private String taints;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName == null ? null : regionName.trim();
    }

    public String getRegionDesc() {
        return regionDesc;
    }

    public void setRegionDesc(String regionDesc) {
        this.regionDesc = regionDesc == null ? null : regionDesc.trim();
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

    public RegionInfo of(TblRegionInfo tblRegionInfo, RegionGwRepository regionGwRepository)
    {
        RegionInfo regionInfo = new RegionInfo();
        regionInfo.setInfo(tblRegionInfo);
        if (StringUtils.isNotBlank(tblRegionInfo.getLabels()))
        {
            Map<String, String> labels = JsonUtils.fromJson(tblRegionInfo.getLabels(), new TypeToken<Map<String, String>>(){}.getType());
            regionInfo.setLabels(labels);
        }
        if (StringUtils.isNotBlank(tblRegionInfo.getTaints()))
        {
            Map<String, String> taints = JsonUtils.fromJson(tblRegionInfo.getTaints(), new TypeToken<Map<String, String>>(){}.getType());
            regionInfo.setTaints(taints);
        }
        List<NameIDInfo> gwBinds = regionGwRepository.getRegionBindGwInfo(tblRegionInfo.getRegionId());
        if (gwBinds != null)
        {
            regionInfo.setGw_node_ids(gwBinds);
        }

        return regionInfo;
    }
}