package com.lnjoying.justice.commonweb.process.service;

import com.lnjoying.justice.commonweb.process.processor.OperationLogOrEventSendProcessor;
import com.micro.core.process.service.ProcessStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月27日 10:56
 */
@Service("operationEventSendProcessStrategy")
@Slf4j
public class OperationLogOrEventSendProcessStrategy extends ProcessStrategy
{
    @Autowired
    private OperationLogOrEventSendProcessor operationLogOrEventSendProcessor;

    public OperationLogOrEventSendProcessStrategy()
    {
        super("Operation event sending service", 1, 10000);
        log.info("Init OperationEventProcessStrategy");
    }

    @PostConstruct
    public void start()
    {
        super.start(() -> operationLogOrEventSendProcessor, 1);
    }
}
