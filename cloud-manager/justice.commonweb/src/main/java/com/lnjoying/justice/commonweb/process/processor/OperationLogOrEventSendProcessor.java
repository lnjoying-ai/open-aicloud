package com.lnjoying.justice.commonweb.process.processor;

import com.lnjoying.justice.commonweb.service.rpcserviceimpl.CommonWebCombRpcService;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.lnjoying.justice.schema.service.omc.OmcService;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月27日 11:03
 */
@Component("operationEventSendProcessor")
@Slf4j
public class OperationLogOrEventSendProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CommonWebCombRpcService commonWebCombRpcService;

    @Override
    public void start()
    {
        LOGGER.info("Operation event sending processor started.");
    }

    @Override
    public void stop()
    {
        LOGGER.info("Operation event sending processor stopped.");
    }

    @Override
    public void run()
    {
        BlockingQueue<Object> queue = getBlockQueue();
        while (true)
        {
            try
            {
                MessagePack operationEvent = (MessagePack) queue.take();
                switch (operationEvent.getMsgType())
                {
                    case "operationEvent":
                        commonWebCombRpcService.getOmcService()
                                .addEvent((OmcService.OperationEvent) operationEvent.getMessageObj());
                        break;
                    case "operationLog":
                        commonWebCombRpcService.getOmcService()
                                .addLog((OmcService.OperationLog) operationEvent.getMessageObj());
                        break;
                    default:
                        log.warn("未知的消息类型 {}", operationEvent.getMsgType());
                        break;
                }
            } catch (Throwable e)
            {
                e.printStackTrace();
                LOGGER.error("OperationEventSendProcessor.run error {}", e.getMessage());
            }
        }
    }

    @Override
    public void exceptionThrown(Exception e)
    {

    }
}
