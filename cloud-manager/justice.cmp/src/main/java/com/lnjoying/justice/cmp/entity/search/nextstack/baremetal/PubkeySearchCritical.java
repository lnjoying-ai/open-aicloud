package com.lnjoying.justice.cmp.entity.search.nextstack.baremetal;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PubkeySearchCritical extends PageSearchCritical
{
    public String name;
}
