package com.lnjoying.justice.cmp.entity.search.nextstack.network;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class VpcSearchCritical extends PageSearchCritical
{
    private String name;
    private Integer phaseStatus;
}
