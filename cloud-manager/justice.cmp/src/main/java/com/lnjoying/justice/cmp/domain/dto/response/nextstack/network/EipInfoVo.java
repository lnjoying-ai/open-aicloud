package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class EipInfoVo
{
    private String eipId;
    private String ipAddress;
    private Integer addressType;
    private String createTime;
    private String updateTime;
    private String eipPoolId;
    private String boundId;

    private String boundType;

    private String boundName;

    private Integer phaseStatus;

    private String publicIp;

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
