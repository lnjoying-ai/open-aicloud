package com.lnjoying.justice.cmp.entity.search.nextstack.repo;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StoragePoolSearchCritical extends PageSearchCritical
{
    private String poolName;

    private Integer poolType;
}
