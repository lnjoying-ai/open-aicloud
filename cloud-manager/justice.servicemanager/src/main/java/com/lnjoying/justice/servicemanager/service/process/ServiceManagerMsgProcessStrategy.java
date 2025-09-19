package com.lnjoying.justice.servicemanager.service.process;

import com.lnjoying.justice.servicemanager.common.ProcessorName;
import com.lnjoying.justice.servicemanager.service.process.processor.ServiceManagerMsgProcessor;
import com.micro.core.process.service.ProcessMultiStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("serviceManagerMsgProcessStrategy")
public class ServiceManagerMsgProcessStrategy extends ProcessMultiStrategy
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ServiceManagerMsgProcessor serviceManagerMsgProcessor;

    public ServiceManagerMsgProcessStrategy(String desc, int threadNum, int maxQueueSize)
    {
        super(desc, threadNum, maxQueueSize);
    }

    public ServiceManagerMsgProcessStrategy()
    {
        super("service manager message service",1, 10000);
        LOGGER.info("init serviceManagerMsgProcessStrategy");
    }

    @PostConstruct
    public void start()
    {
        super.start(() -> serviceManagerMsgProcessor, 1, ProcessorName.SERVICE_MANAGER_MSG);
    }
}
