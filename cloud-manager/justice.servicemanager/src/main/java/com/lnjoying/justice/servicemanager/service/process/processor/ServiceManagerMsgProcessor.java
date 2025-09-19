package com.lnjoying.justice.servicemanager.service.process.processor;

import com.lnjoying.justice.servicemanager.common.ServiceManagerMsg;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.lnjoying.justice.servicemanager.service.ServicePortService;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class ServiceManagerMsgProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    ServicePortService servicePortService;

    @Override
    public void start()
    {
        LOGGER.info("service manager message process started");
    }

    @Override
    public void stop()
    {
        LOGGER.info("service manager message process stopped");
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
                switch(pack.getMsgType())
                {
                    case ServiceManagerMsg.RELEASE_PORT:
                        servicePortService.releasePort((String)pack.getMessageObj());
                        break;
                    case ServiceManagerMsg.ALLOC_PORT:
                        servicePortService.allocPort((String)pack.getMessageObj());
                        break;
                    case ServiceManagerMsg.ADD_SERVICE_PORT_CALLBACK:
                        servicePortService.addServicePortCallback((String)pack.getMessageObj());
                        break;
                    default:
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                LOGGER.error("ServiceManagerMsgProcessor.run error {}", e.getMessage());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                LOGGER.error("ServiceManagerMsgProcessor.run error {}", e.getMessage());
            }
        }

    }

    @Override
    public void exceptionThrown(Exception e)
    {
        return;
    }
}
