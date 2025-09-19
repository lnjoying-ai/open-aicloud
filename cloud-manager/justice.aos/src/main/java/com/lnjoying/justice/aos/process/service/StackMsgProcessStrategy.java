package com.lnjoying.justice.aos.process.service;

import com.lnjoying.justice.aos.process.processor.DaemonsetStackMsgProcessor;
import com.lnjoying.justice.aos.process.processor.ProcessorName;
import com.lnjoying.justice.aos.process.processor.StackMsgProcessor;
import com.micro.core.process.service.ProcessMultiStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("stackMsgProcessStrategy")
public class StackMsgProcessStrategy extends ProcessMultiStrategy
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    StackMsgProcessor stackMsgProcessor;
    
    @Autowired
    DaemonsetStackMsgProcessor daemonsetStackMsgProcessor;

    public StackMsgProcessStrategy()
    {
        super("stack message service",2, 10000);
        LOGGER.info("init StackMsgProcessStrategy");
    }

    @PostConstruct
    public void start()
    {
        super.start(() -> stackMsgProcessor, ProcessorName.DEPLOYMENT_MSG_PROCESSOR,1);
        super.start(() -> daemonsetStackMsgProcessor, ProcessorName.DAEMONSET_MSG_PROCESSOR,1);
    }
}
