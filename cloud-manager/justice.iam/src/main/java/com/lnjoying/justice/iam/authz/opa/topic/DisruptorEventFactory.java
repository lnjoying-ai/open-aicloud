package com.lnjoying.justice.iam.authz.opa.topic;

import com.lmax.disruptor.EventFactory;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/1 20:27
 */

public class DisruptorEventFactory implements EventFactory<DisruptorEvent>
{
    @Override
    public DisruptorEvent newInstance()
    {
        return new DisruptorEvent();
    }
}
