package com.lnjoying.justice.servicemanager.service.process;

import com.lnjoying.justice.servicemanager.service.process.processor.ScanProcessor;
import com.micro.core.process.service.ScheduleProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("serviceManagerSchedulerStrategy")
public class ServiceManagerSchedulerStrategy extends ScheduleProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ScanProcessor scanProcessor;

    private  Integer cycle = 2000;
    public ServiceManagerSchedulerStrategy()
    {
        super("scheduler strategy", 1);
        LOGGER.info("init scheduler strategy");
    }
    
    @PostConstruct
    public void start()
    {
        LOGGER.info("start service manager scheduler service");
        super.start(() -> scanProcessor, 5000, cycle, null);
    }
}
