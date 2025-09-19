package com.lnjoying.justice.cmp.db.model.search;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EipSearchCritical extends PageSearchCritical
{
    private String vpcId;

    private String ipAddress;

    private String eipPoolId;

    private String boundType;
}
