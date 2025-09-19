package com.lnjoying.justice.iam.authz.opa.topic;

import com.lmax.disruptor.dsl.Disruptor;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/2 17:28
 */

public class DisruptorShutdownHook extends Thread
{

    private Disruptor<DisruptorEvent> disruptor;

    public DisruptorShutdownHook(Disruptor<DisruptorEvent> disruptor)
    {
        this.disruptor = disruptor;
    }

    @Override
    public void run()
    {
       disruptor.shutdown();
    }
}
