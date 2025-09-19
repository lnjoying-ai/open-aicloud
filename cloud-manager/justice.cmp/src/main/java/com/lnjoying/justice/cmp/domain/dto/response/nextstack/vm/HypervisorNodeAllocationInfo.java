package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Date;

@Data
public class HypervisorNodeAllocationInfo
{
    private String nodeId;

    private String name;

    private String manageIp;

    private Integer memTotal;

    private Integer cpuLogCount;

    private String cpuModel;

    @JsonProperty("availableGpuCount")
    @SerializedName("availableGpuCount")
    private Integer gpuCount;

    private String gpuName;

    @JsonProperty("usedCpuSum")
    @SerializedName("usedCpuSum")
    private Integer cpuSum;

    @JsonProperty("usedMemSum")
    @SerializedName("usedMemSum")
    private Integer memSum;

    private Integer memRecycle;
    
    private Integer cpuRecycle;

    @JsonProperty("phaseStatus")
    @SerializedName("phaseStatus")
    private Integer nodePhaseStatus;

    @JsonIgnore
    private Integer vmInstancePhaseStatus;

    private Integer availableIbCount;

    private Integer errorCount;

    private Integer ibTotal;

    private Long gpuTotal;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss", timezone ="GMT+8")
    private Date createTime;
}
