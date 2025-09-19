package com.lnjoying.justice.schema.entity.cmp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcGpuUsages
{
    private Integer gpuTotal = 0;
    private Integer availableGpuCount = 0;
    Map<String, RpcGpuUsage> gpuUsages;
}
