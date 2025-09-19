package com.lnjoying.justice.scheduler.scheduler.framework.nodeports;

import com.lnjoying.justice.scheduler.common.constant.SchedulerElement;
import com.lnjoying.justice.scheduler.scheduler.framework.BaseElement;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.scheduler.scheduler.framework.ElementInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class NodePorts extends BaseElement
{
    NodePorts()
    {
        schedulerElement = SchedulerElement.NODE_PORTS;
    }

    @Override
    public void doScheduler(SchedulerBean schedulerBean)
    {
        if (!isScheduling(schedulerBean.getSchedulerState()) || isSkipElement(schedulerBean.getStrategyMask()))
        {
            return;
        }

        LOGGER.info("[element]name: {} mask: {}.", getElementName(), Integer.toBinaryString(getElementMask()));


    }
}
