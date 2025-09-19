package com.lnjoying.justice.cluster.manager.service.process.processor;

import com.lnjoying.justice.cluster.manager.common.ClusterMsg;
import com.lnjoying.justice.cluster.manager.db.repo.ClusterRepo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.cluster.manager.service.monitor.MonitorService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class MonitorProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private MonitorService monitorService;
    
    @Autowired
    private ClusterRepo clusterRepo;
    
    @Override
    public void start()
    {
        LOGGER.info("monitor cluster processor started");
    }
    
    @Override
    public void stop()
    {
        LOGGER.info("monitor cluster processor stopped");
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
                process(pack);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                LOGGER.error("MonitorProcessor.run error {}", e.getMessage());
            }
        }
    }
    
    void process(MessagePack pack )
    {
        ClusterNodePlanInfo clusterNodePlanInfo = (ClusterNodePlanInfo)pack.getMessageObj();
        try
        {
            switch (pack.getMsgType())
            {
                case ClusterMsg.MONITOR:
                    clusterNodePlanInfo = getMonitorNodePlanInfo(clusterNodePlanInfo);
                    monitorService.monitor(clusterNodePlanInfo);
                    break;
            }
        }
        catch (WebSystemException e)
        {
            e.printStackTrace();
            LOGGER.error("process error {}", e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("process error {}", e.getMessage());
        }
        finally
        {
            clusterRepo.updateClusterNodePlanInfo(clusterNodePlanInfo);
        }
    }

    private ClusterNodePlanInfo getMonitorNodePlanInfo(ClusterNodePlanInfo clusterNodePlanInfo)
    {
        LOGGER.info("start monitor cluster on {}", clusterNodePlanInfo.getClusterNodeInfo());

        ClusterNodePlanInfo latest = clusterRepo.getMonitorNodePlanInfo(clusterNodePlanInfo.getClusterId(), clusterNodePlanInfo.getClusterNodeInfo().getNodeId());
        if (latest == null)
        {
            LOGGER.error("latest cluster node monitor plan is null, {} monitor on {}"
                    , clusterNodePlanInfo.getClusterId(), clusterNodePlanInfo.getClusterNodeInfo().getNodeId());
            return null;
        }

        if (! latest.equals(clusterNodePlanInfo))
        {
            clusterNodePlanInfo = latest;
        }
        return clusterNodePlanInfo;
    }
}
