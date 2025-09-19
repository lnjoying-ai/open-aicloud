package com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpVolume;
import com.micro.core.common.Utils;
import lombok.Data;

import java.util.Date;

@Data
public class VolumeDetailInfoRsp
{
    private String volumeId;

    private String name;

    private String storagePoolId;

    private String storagePoolName;

    private String description;

    private String createTime;

    private String updateTime;

    private Integer size;

    private Integer type;

    private Integer imageOsType;

    private Integer imageOsVendor;

    private String imageName;

    private String imageId;

    private Integer phaseStatus;

    private String eeBp;
    private String eeUser;
    private String eeBpName;
    private String eeUserName;
    private Integer chargeType;
    private Integer priceUnit;
    private Integer period;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("expire_time")
    @SerializedName("expire_time")
    private Date expireTime;

    private String vmInstanceId;
    private String vmName;

    public void setVolumeDetailInfoRsp(TblCmpVolume volume)
    {
        this.volumeId = volume.getVolumeId();
        this.size = volume.getSize();
        this.storagePoolId = volume.getStoragePoolId();
        this.name = volume.getName();
        this.description = volume.getDescription();
        this.createTime = Utils.formatDate(volume.getCreateTime());
        this.updateTime = Utils.formatDate(volume.getUpdateTime());
        this.type = volume.getType();
        this.imageId = volume.getImageId();
        this.phaseStatus = volume.getPhaseStatus();

        this.eeBp = volume.getEeBp();
        this.eeUser = volume.getEeUser();
        this.chargeType = volume.getChargeType();
        this.priceUnit = volume.getPriceUnit();
        this.period = volume.getPeriod();
        this.expireTime = volume.getExpireTime();

        this.vmInstanceId = volume.getVmId();
    }
}
