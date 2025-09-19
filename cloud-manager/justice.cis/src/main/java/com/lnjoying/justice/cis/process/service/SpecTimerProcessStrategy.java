package com.lnjoying.justice.cis.process.service;

import com.lnjoying.justice.cis.process.processor.SpecTimerProcessor;
import com.micro.core.process.service.ScheduleProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("specTimerProcessStrategy")
public class SpecTimerProcessStrategy extends ScheduleProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    SpecTimerProcessor specTimerProcessor;

    //60s
    Integer cycle = 10000;

    public SpecTimerProcessStrategy()
    {
        super("spec timer service",1);
        LOGGER.info("init SpecTimerProcessStrategy");
    }

    @PostConstruct
    public void start()
    {
        LOGGER.info("start processor");

        {
            super.start(() -> {return specTimerProcessor;}, 10000, cycle, null);

        }
    }
}
