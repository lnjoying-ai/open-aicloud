package com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Date;

@Data
public class RootVolumeVo
{
    private String volumeId;

    private String userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private String storagePoolId;

    private String name;

    private Integer size;

    private Integer phaseStatus;

    private String imageId;

    private Integer imageOsType;

    private Integer imageOsVendor;

    private String imageName;

    private Integer type;

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
}
