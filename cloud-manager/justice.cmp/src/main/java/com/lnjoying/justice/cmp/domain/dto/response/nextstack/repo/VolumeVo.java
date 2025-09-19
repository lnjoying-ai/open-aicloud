package com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpVolume;
import lombok.Data;

import java.util.Date;

@Data
public class VolumeVo
{
    private String volumeId;

    private String userId;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;

    private String storagePoolId;

    private String name;

    private Integer size;

    private Integer type;

    private String vmInstanceId;

    private String vmName;

    private Integer phaseStatus;

    private String imageId;

    private String eeBp;
    private String eeUser;
    private String eeBpName;
    private String eeUserName;
    private Integer eeStatus;
    private Integer chargeType;
    private Integer priceUnit;
    private Integer period;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("expire_time")
    @SerializedName("expire_time")
    private Date expireTime;
    private Integer source;

    public void setVolumeVol(TblCmpVolume tblVolume)
    {

        this.volumeId = tblVolume.getVolumeId();

        this.name = tblVolume.getName();

        this.storagePoolId = tblVolume.getStoragePoolId();

        this.userId = tblVolume.getUserId();

        this.size = tblVolume.getSize();

        this.type = tblVolume.getType();

        this.vmInstanceId = tblVolume.getVmId();

        this.phaseStatus = tblVolume.getPhaseStatus();

        this.imageId = tblVolume.getImageId();

        this.chargeType = tblVolume.getChargeType();
        this.priceUnit = tblVolume.getPriceUnit();
        this.period = tblVolume.getPeriod();
        this.expireTime = tblVolume.getExpireTime();
    }
}
