package com.lnjoying.justice.cluster.agent.process;

import com.micro.core.process.service.ScheduleProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("clusterSchedulerStrategy")
public class HeartBeatStrategy extends ScheduleProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private  HeartBeatProcessor heartBeatProcessor;
    
    private  Integer cycle = 15000;
    public HeartBeatStrategy()
    {
        super("heart beat strategy", 1);
        LOGGER.info("init heart beat scheduler strategy");
    }
    
    @PostConstruct
    public void start()
    {
        LOGGER.info("start cluster heart beat service");
        super.start(() -> heartBeatProcessor, 1000, cycle, null);
    }
}
