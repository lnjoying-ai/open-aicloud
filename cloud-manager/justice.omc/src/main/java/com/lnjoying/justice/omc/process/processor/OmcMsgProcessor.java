package com.lnjoying.justice.omc.process.processor;

import com.lnjoying.justice.omc.common.OmcMsgType;
import com.lnjoying.justice.omc.service.AlertSenderService;
import com.lnjoying.justice.omc.service.IntegrationTaskService;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.common.Pair;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/30 19:15
 */

@Component
@Slf4j
public class OmcMsgProcessor extends AbstractRunnableProcessor
{

    @Autowired
    private IntegrationTaskService integrationTaskService;

    @Autowired
    private AlertSenderService alertSenderService;

    @Override
    public void start()
    {
        log.info("omc message process started");
    }

    @Override
    public void stop()
    {
        log.info("omc message process stopped");
    }

    @Override
    public void run()
    {
        BlockingQueue<Object> queue = getBlockQueue();
        while (true)
        {
            try
            {
                MessagePack pack = (MessagePack) queue.take();
                String msgType = pack.getMsgType();
                switch(msgType)
                {
                    case OmcMsgType.BUILD_STACK:
                        integrationTaskService.processBuildStack((String) pack.getMessageObj());
                        break;
                    case OmcMsgType.CHECK_STACK_RUNNING_STATUS:
                        integrationTaskService.processCheckStackRunningStatus((String) pack.getMessageObj());
                        break;
                    case OmcMsgType.PORT_MAPPING:
                        integrationTaskService.processPortMapping((String) pack.getMessageObj());
                        break;
                    case OmcMsgType.ADD_EXPORTER_SCRAPE_TARGET:
                        integrationTaskService.processAddExporterScrapeTarget((String) pack.getMessageObj());
                        break;
                    case OmcMsgType.DELETE_EXPORTER_SCRAPE_TARGET:
                        integrationTaskService.processDeleteExporterScrapeTarget((String) pack.getMessageObj());
                        break;
                    case OmcMsgType.REMOVE_PORT_MAPPING:
                        integrationTaskService.processRemovePortMapping((String) pack.getMessageObj());
                        break;
                    case OmcMsgType.REMOVE_STACK:
                        integrationTaskService.processRemoveStack((String) pack.getMessageObj());
                        break;
                    case OmcMsgType.SEND_ALERT:
                        alertSenderService.processAlertSend((Pair) pack.getMessageObj());
                        break;
                    default:
                        log.warn("unknown message type:{}", msgType);

                }
            }
            catch (Exception e)
            {
                log.error("omc message process error:{}", e);
            }
        }
    }
}
