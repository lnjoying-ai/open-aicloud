package com.lnjoying.justice.aos.process.service;

import com.lnjoying.justice.aos.process.processor.DamonsetStackTimerProcessor;
import com.lnjoying.justice.aos.process.processor.StackTimerProcessor;
import com.micro.core.process.service.ScheduleProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("stackTimerProcessStrategy")
public class StackTimerProcessStrategy extends ScheduleProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    StackTimerProcessor stackTimerProcessor;
    
    @Autowired
    DamonsetStackTimerProcessor damonsetStackTimerProcessor;

    //10s
    Integer cycle = 10000;
    
    //60s
    Integer daemonCycle = 60000;

    public StackTimerProcessStrategy()
    {
        super("stack timer service",2);
        LOGGER.info("init StackTimerProcessStrategy");
    }

    @PostConstruct
    public void start()
    {
        LOGGER.info("start processor");

        {
            super.start(() -> stackTimerProcessor, 10000, cycle, null);
            super.start(() -> damonsetStackTimerProcessor, 10000, daemonCycle, null);
        }
    }
}
