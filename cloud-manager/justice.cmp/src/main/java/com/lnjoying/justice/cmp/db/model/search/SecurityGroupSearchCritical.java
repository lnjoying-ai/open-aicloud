package com.lnjoying.justice.cmp.db.model.search;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class SecurityGroupSearchCritical extends PageSearchCritical
{
    String name;
    String sgId;
}
