package com.lnjoying.justice.cmp.service.process;

import com.lnjoying.justice.cmp.service.process.processor.CloudMsgProcessor;
import com.micro.core.process.service.ProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("cloudMsgProcessStrategy")
public class CloudMsgProcessStrategy extends ProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CloudMsgProcessor cloudMsgProcessor;
    public CloudMsgProcessStrategy()
    {
        super("cmp message service",1, 10000);
        LOGGER.info("init CloudMsgProcessStrategy");
    }

    @PostConstruct
    public void start()
    {
        super.start(() -> cloudMsgProcessor,1);
    }
}
