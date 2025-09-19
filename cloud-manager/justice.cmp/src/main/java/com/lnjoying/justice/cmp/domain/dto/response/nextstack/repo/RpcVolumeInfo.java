package com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RpcVolumeInfo implements Serializable
{
    private String volumeName;
    private String volumeId;
    private Integer size;
    private Integer type;
    private Integer phaseStatus;
    private Integer source;
    private Integer chargeType;
    private Integer priceUnit;
    private Integer period;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("expire_time")
    @SerializedName("expire_time")
    private Date expireTime;
}