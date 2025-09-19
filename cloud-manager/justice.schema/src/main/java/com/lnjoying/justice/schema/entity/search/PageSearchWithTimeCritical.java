package com.lnjoying.justice.schema.entity.search;

import lombok.Data;

@Data
public class PageSearchWithTimeCritical extends PageSearchCritical implements ISearchCritical
{
    String startTime;
    String endTime;
}
