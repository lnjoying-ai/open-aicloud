package com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal;

import lombok.Data;

@Data
public class InstanceStatsRsp
{
    private int instanceTotal;

    private int instanceCreateFailed;

    private int instancePowerOff;

    private int instanceRunning;

    private int instanceCreating;
}
