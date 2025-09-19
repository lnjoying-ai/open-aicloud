package com.lnjoying.justice.cis.process.service;

import com.lnjoying.justice.cis.process.processor.SpecMsgProcessor;
import com.micro.core.process.service.ProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("stackMsgProcessStrategy")
public class SpecMsgProcessStrategy extends ProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    SpecMsgProcessor specMsgProcessor;

    public SpecMsgProcessStrategy()
    {
        super("spec message service",2, 10000);
        LOGGER.info("init SpecMsgProcessStrategy");
    }

    @PostConstruct
    public void start()
    {
        super.start(() -> specMsgProcessor,1);
    }
}
