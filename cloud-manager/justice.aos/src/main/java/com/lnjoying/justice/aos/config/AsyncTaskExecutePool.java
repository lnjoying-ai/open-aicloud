package com.lnjoying.justice.aos.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * async task execute pool
 *
 * @author merak
 **/

@Configuration
@Slf4j
public class AsyncTaskExecutePool
{
    @Autowired
    private AsyncTaskProperties config;

    @Bean
    public Executor executor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(config.getCorePoolSize() * 2);
        executor.setMaxPoolSize(config.getMaxPoolSize() * 4);
        executor.setQueueCapacity(config.getQueueCapacity() * 64);
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        executor.setThreadNamePrefix("aos-async-normal-task");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    public Executor timedExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(config.getCorePoolSize() * 2);
        executor.setMaxPoolSize(config.getMaxPoolSize() * 2);
        executor.setQueueCapacity(config.getQueueCapacity() * 8);
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        executor.setThreadNamePrefix("aos-async-timed-task");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
}
