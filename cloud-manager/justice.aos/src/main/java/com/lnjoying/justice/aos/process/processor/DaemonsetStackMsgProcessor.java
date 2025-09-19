package com.lnjoying.justice.aos.process.processor;

import com.lnjoying.justice.aos.common.AosMsgType;
import com.lnjoying.justice.aos.facade.StackServiceFacade;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class DaemonsetStackMsgProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    StackServiceFacade stackFacade;

    @Override
    public void start()
    {
        LOGGER.info("daemonset stack message process started");
    }

    @Override
    public void stop()
    {
        LOGGER.info("daemonset stack message process stopped");
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
                    case AosMsgType.SCHEDULE_DAEMONSET_STACK:
                        stackFacade.processDaemonsetStack((String) pack.getMessageObj());
                    default:
                }
            }
            catch (InterruptedException e)
            {
                LOGGER.error("daemonset stack message process InterruptedException", e);
            }
            catch (Exception e)
            {
                LOGGER.error("daemonset stack message process error", e);
            }
        }
    }

    @Override
    public void exceptionThrown(Exception e)
    {
        return;
    }
}
