package com.lnjoying.justice.cmp.entity.search.nextstack.baremetal;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class BaremetalInstanceSearchCritical extends PageSearchCritical
{
    private String name;
    private Boolean portIdIsNull;
    private Boolean eipMapIsUsing;
    private String subnetId;
}
