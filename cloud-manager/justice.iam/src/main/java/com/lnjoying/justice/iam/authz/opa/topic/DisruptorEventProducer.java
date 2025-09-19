package com.lnjoying.justice.iam.authz.opa.topic;

import com.lmax.disruptor.RingBuffer;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/1 20:29
 */

@Slf4j
public class DisruptorEventProducer
{
    private final RingBuffer<DisruptorEvent> ringBuffer;


    public DisruptorEventProducer(RingBuffer<DisruptorEvent> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }

    public void publish(Object msg, String type)
    {
        long next = ringBuffer.next();
        try
        {
            DisruptorEvent disruptorEvent = ringBuffer.get(next);
            disruptorEvent.setType(type);
            disruptorEvent.setSource(msg);
        }
        catch (Exception e)
        {
            log.error("An exception occurred when storing data [{}] into the Ring Buffer queue.=>{}", msg, e);
        }
        finally
        {
            ringBuffer.publish(next);
        }
    }
}
