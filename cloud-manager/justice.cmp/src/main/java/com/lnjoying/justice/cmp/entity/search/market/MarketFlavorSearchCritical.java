package com.lnjoying.justice.cmp.entity.search.market;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MarketFlavorSearchCritical extends PageSearchCritical
{
    private String cloudId;
    private String gpuName;
}
