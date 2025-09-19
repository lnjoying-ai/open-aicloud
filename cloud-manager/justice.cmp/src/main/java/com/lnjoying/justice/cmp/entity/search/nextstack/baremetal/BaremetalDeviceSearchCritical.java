package com.lnjoying.justice.cmp.entity.search.nextstack.baremetal;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class BaremetalDeviceSearchCritical extends PageSearchCritical
{
    private Integer baremetalDevicePhaseStatus;

    private Integer nicSpecPhaseStatus;

    private String userId;

    private Integer cpuNum;

    private Long memTotal;
}
