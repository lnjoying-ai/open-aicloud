package com.lnjoying.justice.iam.authz.opa.topic;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/1 20:39
 */

@Slf4j
public class DisruptorManager
{

    private final Disruptor<DisruptorEvent> disruptor;

    private DisruptorEventProducer producer;


    public DisruptorManager(Disruptor disruptor)
    {
        this.disruptor = disruptor;
        this.producer = new DisruptorEventProducer(this.disruptor.getRingBuffer());
    }


    public void publish(Object msg, String type)
    {
        this.producer.publish(msg, type);
    }

    public long getCursor()
    {
        return this.disruptor.getCursor();
    }

    public void stop()
    {
        try
        {
            this.disruptor.shutdown(2, TimeUnit.SECONDS);
        }
        catch (TimeoutException e)
        {
            log.error("stop disruptor error:{}", e);
            throw new RuntimeException(e);
        }
    }
}
