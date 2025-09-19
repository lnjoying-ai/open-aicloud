package com.lnjoying.justice.schema.entity.cmp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcGpuResourceMetrics
{
    private List<RpcGpuResourceMetric> gpuResourceMetrics;
    private Long totalNum;
}
