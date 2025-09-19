package com.lnjoying.justice.scheduler.scheduler.strategy;

import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;

public interface StrategyInterface
{
    String getStrategyName();

    Integer getStrategyMask();

    void doScheduler(SchedulerBean schedulerBean);
}
