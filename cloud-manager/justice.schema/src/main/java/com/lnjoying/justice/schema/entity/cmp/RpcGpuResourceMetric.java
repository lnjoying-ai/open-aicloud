package com.lnjoying.justice.schema.entity.cmp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcGpuResourceMetric
{
    private String cloudId;
    private String cloudName;
    private String gpuName;
    private Integer gpuTotal;
    private Integer availableGpuCount;
    private Integer ibTotal;
    private Integer availableIbCount;
}
