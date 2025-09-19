package com.lnjoying.justice.iam.config.opa;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lnjoying.justice.iam.authz.opa.client.OpaClient;
import com.lnjoying.justice.iam.authz.opa.data.DataEventHandler;
import com.lnjoying.justice.iam.authz.opa.policy.PolicyEventHandler;
import com.lnjoying.justice.iam.authz.opa.topic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/2 16:34
 */

@Configuration
public class DisruptorConfiguration
{
    @Bean
    @ConditionalOnClass({ Disruptor.class })
    public DisruptorManager disruptorManager(Disruptor disruptor)
    {
        return new DisruptorManager(disruptor);
    }

    @Bean
    @ConditionalOnClass({ Disruptor.class })
    public Disruptor<DisruptorEvent> disruptor(DisruptorProperties properties, WaitStrategy waitStrategy,
                                               ThreadFactory threadFactory, EventFactory<DisruptorEvent> eventFactory,
                                               DisruptorEventDispatcher disruptorEventDispatcher)
    {
        Disruptor<DisruptorEvent> disruptor = new Disruptor<>(eventFactory, properties.getRingBufferSize(),
                threadFactory, ProducerType.MULTI, waitStrategy);

        disruptor.handleEventsWith(disruptorEventDispatcher);
        disruptor.start();
        Runtime.getRuntime().addShutdownHook(new DisruptorShutdownHook(disruptor));

        return disruptor;
    }

    @Bean
    public DisruptorEventDispatcher disruptorEventDispatcher(DisruptorEventHandler[] disruptorEventHandlers)
    {
        return new DisruptorEventDispatcher(Arrays.asList(disruptorEventHandlers));
    }

    @Bean
    public DataEventHandler dataEventHandler(OpaClient opaClient)
    {
        return new DataEventHandler(opaClient);
    }

    @Bean
    public PolicyEventHandler policyEventHandler(OpaClient opaClient)
    {
        return new PolicyEventHandler(opaClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public ThreadFactory threadFactory() {
        return  new CustomizableThreadFactory("disruptor-handler-");
    }

    @Bean
    @ConditionalOnMissingBean
    public EventFactory<DisruptorEvent> eventFactory() {
        return new DisruptorEventFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public WaitStrategy waitStrategy() {
        return new BlockingWaitStrategy();
    }

}
