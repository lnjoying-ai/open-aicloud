package com.lnjoying.justice.scheduler.scheduler.framework;

import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;

public interface ElementInterface
{
    String getElementName();

    Integer getElementMask();

    void doScheduler(SchedulerBean schedulerBean);
}
