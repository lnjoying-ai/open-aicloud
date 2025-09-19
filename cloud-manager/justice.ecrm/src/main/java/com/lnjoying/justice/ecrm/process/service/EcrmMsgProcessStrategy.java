package com.lnjoying.justice.ecrm.process.service;

import com.lnjoying.justice.ecrm.process.processor.EcrmMsgProcessor;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.process.service.ProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("ecrmMsgProcessStrategy")
public class EcrmMsgProcessStrategy extends ProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    EcrmMsgProcessor ecrmMsgProcessor;
    public EcrmMsgProcessStrategy()
    {
        super("ecrm message service",2, 10000);
        LOGGER.info("init EcrmMsgProcessStrategy");
    }

    @PostConstruct
    public void start()
    {
        super.start(() -> ecrmMsgProcessor,1);
    }
}
