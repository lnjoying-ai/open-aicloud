package com.lnjoying.justice.iam.authz.opa.topic;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.lnjoying.justice.iam.authz.opa.data.DataEvent.DATA_TYPE;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/2 16:45
 */

@Slf4j
public class DisruptorEventDispatcher implements EventHandler<DisruptorEvent>
{
    private final List<DisruptorEventHandler<DisruptorEvent>> handles;

    public DisruptorEventDispatcher(List<DisruptorEventHandler<DisruptorEvent>> handles)
    {
        // todo optimization
        this.handles = handles;
    }

    @Override
    public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch) throws Exception
    {
        doHandler(event);
    }

    private void doHandler(DisruptorEvent event) throws Exception
    {

        handles.stream().filter(handler -> handler.supports(event)).forEach(handler -> {
            try
            {
                DisruptorEvent convertEvent = EventConverter.INSTANCE.convert(event);
                handler.doHandler(convertEvent);
            }
            catch (Exception e)
            {
                log.error("handling event exceptions，event:{};error:{}", event, e);
            }});
    }

}
