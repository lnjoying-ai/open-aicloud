package com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal;

import lombok.Data;

import java.util.List;

@Data
public class BaremetalDevicesConfigsRsp
{
    private List<Long> memTotals;

    private List<Integer> cpuNums;
}
