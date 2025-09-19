package com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PciDeviceDetailInfo
{
    private String nodeId;

    private String nodeName;

    private String pciDeviceGroupId;

    private String pciDeviceName;

    private String pciDeviceType;

    private String userId;

    private Integer phaseStatus;

    private String deviceId;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;

    private String vmInstanceId;

    private String vmInstanceName;
}
