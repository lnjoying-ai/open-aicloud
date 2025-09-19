package com.lnjoying.justice.scheduler.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class GpuInfo implements Serializable
{
    private String gpuId;

    private String gpuType;

    private String gpuModel;

    private String driverVersion;

    private String cudaVersion;

    private String cudnnVersion;

    private String nodeId;

    private Integer index;

    private Integer status;

    private String refId;

    private Date createTime;

    private Date updateTime;
}
