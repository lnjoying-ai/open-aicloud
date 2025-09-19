package com.lnjoying.justice.cluster.manager.service.process;

import com.lnjoying.justice.cluster.manager.service.process.processor.ScanDeployProcessor;
import com.micro.core.process.service.ScheduleProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("clusterSchedulerStrategy")
public class ClusterSchedulerStrategy extends ScheduleProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private  ScanDeployProcessor scanDeployProcessor;
    
    private  Integer cycle = 2000;
    public ClusterSchedulerStrategy()
    {
        super("scheduler strategy", 1);
        LOGGER.info("init scheduler strategy");
    }
    
    @PostConstruct
    public void start()
    {
        LOGGER.info("start cluster scheduler service");
        super.start(() -> scanDeployProcessor, 5000, cycle, null);
    }
}
