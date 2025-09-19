package com.lnjoying.justice.cmp.entity.search.nextstack.repo;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VolumeSearchCritical extends PageSearchCritical
{
    private String poolId;

    private String volumeName;

    private Integer phaseStatus;

    private Boolean isRoot;

    private Boolean isEqPhaseStatus;
}
