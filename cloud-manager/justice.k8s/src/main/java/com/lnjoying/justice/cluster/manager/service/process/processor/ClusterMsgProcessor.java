package com.lnjoying.justice.cluster.manager.service.process.processor;

import com.lnjoying.justice.cluster.manager.common.ClusterMsg;
import com.lnjoying.justice.cluster.manager.service.agent.ClusterAgentService;
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
public class ClusterMsgProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    ClusterAgentService clusterAgentService;

    @Override
    public void start()
    {
        LOGGER.info("ecrm message process started");
    }

    @Override
    public void stop()
    {
        LOGGER.info("ecrm message process stopped");
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
                    case ClusterMsg.SYNC_WORKER_IF_STATE:
                        clusterAgentService.processWorkerIFState((Pair<String, EeNetMessageApi.ee_net_message>) pack.getMessageObj());
                         break;
                    default:
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                LOGGER.error("ClusterMsgProcessor.run error {}", e.getMessage());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                LOGGER.error("ClusterMsgProcessor.run error {}", e.getMessage());
            }
        }

    }

    @Override
    public void exceptionThrown(Exception e)
    {
        return;
    }
}
