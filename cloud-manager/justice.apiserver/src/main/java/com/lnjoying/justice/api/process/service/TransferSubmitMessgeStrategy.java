package com.lnjoying.justice.api.process.service;

import com.lnjoying.justice.api.process.processor.SubmitMessageProcessor;
import com.lnjoying.justice.schema.msg.MessagePack;

import com.micro.core.process.service.ProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("transferSubmitMessageService")
public class TransferSubmitMessgeStrategy extends ProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    SubmitMessageProcessor submitMessageProcessor;

    public TransferSubmitMessgeStrategy()
    {
        super("Transfer submit message service",2, 10000);
        LOGGER.info("init TransferSubmitMessgeService");
    }

    @PostConstruct
    public void start()
    {
        super.start(() -> submitMessageProcessor,2);
    }
}
