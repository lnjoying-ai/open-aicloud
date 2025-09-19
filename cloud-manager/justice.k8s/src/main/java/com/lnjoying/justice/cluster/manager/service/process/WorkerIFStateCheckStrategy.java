package com.lnjoying.justice.cluster.manager.service.process;

import com.lnjoying.justice.cluster.manager.service.process.processor.EdgeWorkerCheckProcessor;
import com.micro.core.process.service.ScheduleProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;

@Service("workerIFStateCheckStrategy")
public class WorkerIFStateCheckStrategy extends ScheduleProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    EdgeWorkerCheckProcessor edgeWorkerCheckProcessor;

    //300s,5分钟
    Integer cycle = 300000;
    private BlockingQueue<Object> renderQueue;
    public WorkerIFStateCheckStrategy()
    {
        super("worker if process", 2);
        LOGGER.info("init worker if scheduler strategy");
    }

    @PostConstruct
    public void start()
    {
        LOGGER.info("start worker if processor");
        super.start(() -> edgeWorkerCheckProcessor, 100000, cycle, null);
    }
}
