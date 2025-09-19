package com.lnjoying.justice.iam.authz.opa.topic;

import com.lmax.disruptor.EventHandler;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/1 20:37
 */

public interface DisruptorEventHandler<T extends DisruptorEvent>
{
    void doHandler(T event) throws Exception;

    boolean supports(DisruptorEvent event);

}
