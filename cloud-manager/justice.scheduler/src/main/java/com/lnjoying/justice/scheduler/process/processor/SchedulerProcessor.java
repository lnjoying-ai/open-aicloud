package com.lnjoying.justice.scheduler.process.processor;

import com.lnjoying.justice.scheduler.common.constant.AffinityState;
import com.lnjoying.justice.scheduler.common.constant.SchedulerMsgType;
import com.lnjoying.justice.scheduler.scheduler.scheduler.ClusterScheduler;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.scheduler.scheduler.Scheduler;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

@Component
public class SchedulerProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    Scheduler scheduler;

    @Autowired
    ClusterScheduler clusterScheduler;

    @Override
    public void start()
    {
        LOGGER.info("stack message process started");
    }

    @Override
    public void stop()
    {
        LOGGER.info("stack message process stopped");
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
                SchedulerBean schedulerBean = new SchedulerBean();
                schedulerBean.setSchedulerState(SchedulerState.SCHEDULING.getCode());
                schedulerBean.setBindRelations(new ArrayList<>());
                schedulerBean.setOriginReq(pack.getMessageObj());
                schedulerBean.setSchedulerMsgType(pack.getMsgType());
                schedulerBean.setAffinityState(AffinityState.DEFAULT.getCode());
                if (schedulerBean.getSchedulerMsgType().equals(SchedulerMsgType.CLUSTER))
                {
                    clusterScheduler.scheduler(schedulerBean);
                }
                else
                {
                    scheduler.scheduler(schedulerBean);
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                LOGGER.error("SchedulerProcessor.run error {}", e.getMessage());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                LOGGER.error("SchedulerProcessor.run error {}", e.getMessage());
            }
        }
    }

    @Override
    public void exceptionThrown(Exception e)
    {
        return;
    }
}
