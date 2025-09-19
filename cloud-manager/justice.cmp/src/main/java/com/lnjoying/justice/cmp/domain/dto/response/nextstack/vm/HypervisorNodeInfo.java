package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class HypervisorNodeInfo
{
    private String nodeId;

    private String nodeName;

    private String manageIp;

    private Integer availableGpuCount;

    @JsonIgnore
    private Integer pciDevicePhaseStatus;

    @JsonIgnore
    private Integer nodePhaseStatus;

    @JsonIgnore
    private String pciDeviceType;

    @JsonIgnore
    private String pciDeviceGroupId;

    private Integer memTotal;

    private Integer cpuLogCount;

    private String cpuModel;

    private Integer cpuAllocation;

    private Integer memAllocation;
}
