package com.lnjoying.justice.cluster.manager.service.process.processor;

import com.lnjoying.justice.cluster.manager.common.ClusterMsg;
import com.lnjoying.justice.cluster.manager.common.ClusterNodeStatus;
import com.lnjoying.justice.cluster.manager.db.repo.ClusterRepo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.cluster.manager.service.deploy.DeployService;
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
public class DeployProcessor  extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private DeployService k8sDeployService;
    
    @Autowired
    private ClusterRepo clusterRepo;
    
    @Override
    public void start()
    {
        LOGGER.info("deploy cluster processor started");
    }
    
    @Override
    public void stop()
    {
        LOGGER.info("deploy cluster processor stopped");
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
                LOGGER.error("DeployProcessor.run error {}", e.getMessage());
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
                case ClusterMsg.DEPLOY:
                    clusterNodePlanInfo = getLatestDeployNodePlanInfo(clusterNodePlanInfo);
                    k8sDeployService.deploy(clusterNodePlanInfo);
                    if (clusterNodePlanInfo.getNodeStatus().equals(ClusterNodeStatus.DEPLOY_FAILED))
                    {
//                        clusterRepo.updateClusterStatus(clusterNodePlanInfo.getClusterId(), ClusterStatus.DEPLOY_FAILED.getCode());
                    }
                    break;
                case ClusterMsg.UNDEPLOY:
                    clusterNodePlanInfo = getLatestUndeployNodePlanInfo(clusterNodePlanInfo);
                    k8sDeployService.undeploy(clusterNodePlanInfo);
                    if (clusterNodePlanInfo.isForceDelete() && !clusterNodePlanInfo.getNodeStatus().equals(ClusterNodeStatus.RELEASED))
                    {
                        clusterNodePlanInfo.setNodeStatus(ClusterNodeStatus.RELEASED);
                        clusterNodePlanInfo.setMonitorPlanList(null);
                    }
                    break;
            }
        }
        catch (WebSystemException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            clusterRepo.updateClusterNodePlanInfo(clusterNodePlanInfo);
        }
    }


    private ClusterNodePlanInfo getLatestDeployNodePlanInfo(ClusterNodePlanInfo clusterNodePlanInfo)
    {
        LOGGER.info("start deploy cluster on {}", clusterNodePlanInfo.getClusterNodeInfo());

        ClusterNodePlanInfo latest = clusterRepo.getDeployNodePlanInfo(clusterNodePlanInfo.getClusterId(), clusterNodePlanInfo.getClusterNodeInfo().getNodeId());
        if (latest == null)
        {
            LOGGER.error("latest cluster node plan is null, {} deploy on {}"
                    , clusterNodePlanInfo.getClusterId(), clusterNodePlanInfo.getClusterNodeInfo().getNodeId());
            return null;
        }

        if (! latest.equals(clusterNodePlanInfo))
        {
            clusterNodePlanInfo = latest;
        }
        return clusterNodePlanInfo;
    }


    @Nullable
    private ClusterNodePlanInfo getLatestUndeployNodePlanInfo(ClusterNodePlanInfo clusterNodePlanInfo)
    {
        LOGGER.info("start undeploy cluster on {}", clusterNodePlanInfo.getClusterNodeInfo());

        ClusterNodePlanInfo latest = clusterRepo.getUnDeployNodePlanInfo(clusterNodePlanInfo.getClusterId(), clusterNodePlanInfo.getClusterNodeInfo().getNodeId());
        if (latest == null)
        {
            LOGGER.error("latest cluster node undeploy plan is null, {} undeploy on {}"
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
