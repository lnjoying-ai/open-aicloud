package com.lnjoying.justice.cmp.db.model.search;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EipPortMapSearchCritical extends PageSearchCritical
{
    String name;
    String eipId;
}
