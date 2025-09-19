package com.lnjoying.justice.cmp.domain.info.baremetal;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class BaremetalDeviceInfo
{
    private String deviceId;

    private String name;

    private Integer phaseStatus;

    private String clusterId;

    private String userId;

    private String ipmiIp;

    private Integer ipmiPort;

    private String architecture;

    private int cpuNum;

    private double cpuFrequency;

    private long memTotal;

    private long diskTotal;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;
}
