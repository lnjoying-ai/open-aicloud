package com.lnjoying.justice.cmp.entity.search.nextstack.vm;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class HypervisorNodeSearchCritical extends PageSearchCritical
{
    private String name;

    private String nodeId;

    private Boolean isHealthy;
}
