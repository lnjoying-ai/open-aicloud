package com.lnjoying.justice.schema.entity.search;

import lombok.Data;

@Data
public class PageSearchCritical implements ISearchCritical
{
    Integer    pageNum    =   1;
    //Integer    pageSize   = 100;
    Integer    pageSize   = Integer.MAX_VALUE;
}
