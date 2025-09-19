package com.lnjoying.justice.ecrm.process.service;

import com.lnjoying.justice.ecrm.process.processor.EdgeDevCheckProcessor;
import com.lnjoying.justice.ecrm.process.processor.ServiceWorkerCheckProcessor;
import com.micro.core.process.service.ScheduleProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;

@Service("devIFStateCheckStrategy")
public class DevIFStateCheckStrategy extends ScheduleProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    EdgeDevCheckProcessor edgeDevCheckProcessor;

    @Autowired
    ServiceWorkerCheckProcessor serviceWorkerCheckProcessor;

    //300s,5分钟
    Integer cycle = 300000;
    
    private BlockingQueue<Object> renderQueue;
    public DevIFStateCheckStrategy()
    {
        super("dev if process", 2);
        LOGGER.info("init dev if scheduler strategy");
    }

    @PostConstruct
    public void start()
    {
        LOGGER.info("start dev if processor");
        super.start(() -> edgeDevCheckProcessor, 20000, cycle, null);
        super.start(() -> serviceWorkerCheckProcessor, 100000, cycle, null);
    }
}
