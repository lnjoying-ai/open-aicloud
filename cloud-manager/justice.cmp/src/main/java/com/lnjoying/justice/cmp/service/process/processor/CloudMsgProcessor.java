package com.lnjoying.justice.cmp.service.process.processor;

import com.lnjoying.justice.cmp.common.CloudMsg;
import com.lnjoying.justice.cmp.service.agent.CloudAgentService;
import com.lnjoying.justice.schema.msg.EeNetMessageApi;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.common.Pair;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class CloudMsgProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CloudAgentService cloudAgentService;

    @Override
    public void start()
    {
        LOGGER.info("cmp message process started");
    }

    @Override
    public void stop()
    {
        LOGGER.info("cmp message process stopped");
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
                    case CloudMsg.IMPORT:
                        cloudAgentService.processImportCloud((String)pack.getMessageObj());
                         break;
                    case CloudMsg.UPDATE:
                        cloudAgentService.processUpdateCloud((String)pack.getMessageObj());
                        break;
                    case CloudMsg.DELETE:
                        cloudAgentService.processDeleteCloud((String)pack.getMessageObj());
                        break;
                    case CloudMsg.CREATE:
                        cloudAgentService.processCreateCloudAgent((String)pack.getMessageObj());
                        break;
                    case CloudMsg.SYNC_WORKER_IF_STATE:
                        cloudAgentService.processWorkerIFState((Pair<String, EeNetMessageApi.ee_net_message>) pack.getMessageObj());
                        break;
                    default:
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                LOGGER.error("CloudMsgProcessor.run error {}", e.getMessage());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                LOGGER.error("CloudMsgProcessor.run error {}", e.getMessage());
            }
        }

    }

    @Override
    public void exceptionThrown(Exception e)
    {
        return;
    }
}
