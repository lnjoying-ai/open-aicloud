package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import lombok.Data;

@Data
public class AvailableGPURsp
{
    String nodeId;

    String gpuName;

    Integer gpuCount;
}
