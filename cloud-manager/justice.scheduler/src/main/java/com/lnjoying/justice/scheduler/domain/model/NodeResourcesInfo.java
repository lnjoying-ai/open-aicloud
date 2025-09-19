package com.lnjoying.justice.scheduler.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.schema.entity.dev.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NodeResourcesInfo implements Serializable
{
    @ApiModelProperty(example = "")
    @SerializedName("cpu")
    @JsonProperty("cpu")
    private CpuInfo cpu;
    
    @ApiModelProperty(example = "")
    @SerializedName("mem")
    @JsonProperty("mem")
    private MemInfo mem;
    
    @ApiModelProperty(example = "")
    @SerializedName("disk")
    @JsonProperty("disk")
    private DiskInfo disk;
    
    @ApiModelProperty(example = "")
    @SerializedName("gpu")
    @JsonProperty("gpu")
    private List<GpuInfo> gpu;
    private boolean inUse;
    private boolean monopoly;

    private String nodeId;
    private String siteId;
    private String regionId;

    private Integer bindInstNum;
    private Integer bindStackNum;
}
