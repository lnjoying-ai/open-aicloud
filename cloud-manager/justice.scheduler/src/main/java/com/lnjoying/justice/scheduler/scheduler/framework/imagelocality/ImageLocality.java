package com.lnjoying.justice.scheduler.scheduler.framework.imagelocality;

import com.lnjoying.justice.scheduler.common.constant.SchedulerElement;
import com.lnjoying.justice.scheduler.scheduler.framework.BaseElement;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import org.springframework.stereotype.Component;

@Component
public class ImageLocality extends BaseElement
{
    ImageLocality()
    {
        schedulerElement = SchedulerElement.IMAGE_LOCALITY;
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
