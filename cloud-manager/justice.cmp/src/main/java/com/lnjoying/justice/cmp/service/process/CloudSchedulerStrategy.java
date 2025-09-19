package com.lnjoying.justice.cmp.service.process;

import com.lnjoying.justice.cmp.service.process.processor.ScanProcessor;
import com.lnjoying.justice.cmp.service.process.processor.SyncFullDataProcessor;
import com.micro.core.process.service.ScheduleProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("cloudSchedulerStrategy")
public class CloudSchedulerStrategy extends ScheduleProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ScanProcessor scanProcessor;

    @Autowired
    private SyncFullDataProcessor syncDataProcessor;

    private  Integer cycle = 2000;
    public CloudSchedulerStrategy()
    {
        super("scheduler strategy", 2);
        LOGGER.info("init scheduler strategy");
    }
    
    @PostConstruct
    public void start()
    {
        LOGGER.info("start cloud scheduler service");
        super.start(() -> scanProcessor, 5000, cycle, null);
        super.start(() -> syncDataProcessor, 5000, cycle, null);
    }
}
