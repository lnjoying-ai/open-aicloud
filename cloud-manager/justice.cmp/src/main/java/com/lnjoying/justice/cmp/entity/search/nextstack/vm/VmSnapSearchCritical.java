package com.lnjoying.justice.cmp.entity.search.nextstack.vm;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class VmSnapSearchCritical extends PageSearchCritical
{
    private String vmInstanceId;
    private String name;
    private String vmSnapId;
}
