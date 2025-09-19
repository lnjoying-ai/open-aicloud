package com.lnjoying.justice.scheduler.process.service;

import com.lnjoying.justice.scheduler.process.processor.SchedulerProcessor;
import com.micro.core.process.service.ProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("schedulerProcessStrategy")
public class SchedulerProcessStrategy extends ProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    SchedulerProcessor schedulerProcessor;

    public SchedulerProcessStrategy()
    {
        super("scheduler service",1, 10000);
        LOGGER.info("init SchedulerProcessStrategy");
    }

    @PostConstruct
    public void start()
    {
        super.start(() -> schedulerProcessor,1);
    }
}
