package com.lnjoying.justice.cmp.entity.search;

import com.lnjoying.justice.schema.entity.search.ISearchCritical;
import lombok.Data;

@Data
public class CmpSearchCritical implements ISearchCritical
{
    private String eeBp;
    private String eeUser;
    private Integer eeStatus;
}
