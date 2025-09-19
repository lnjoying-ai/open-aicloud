package com.lnjoying.justice.scheduler.scheduler.framework;

import com.lnjoying.justice.scheduler.common.constant.SchedulerElement;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseElement implements ElementInterface
{
    protected static Logger LOGGER = LogManager.getLogger();

    protected SchedulerElement schedulerElement;

    @Override
    public String getElementName()
    {
        return schedulerElement.getName();
    }

    @Override
    public Integer getElementMask()
    {
        return schedulerElement.getMask();
    }

    @Override
    public void doScheduler(SchedulerBean schedulerBean)
    {
    }

    protected boolean isScheduling(Integer schedulerState)
    {
        return schedulerState.equals(SchedulerState.SCHEDULING.getCode());
    }

    protected boolean isSkipElement(Integer strategyMask)
    {
        return (strategyMask & getElementMask()) == 0;
    }
}
