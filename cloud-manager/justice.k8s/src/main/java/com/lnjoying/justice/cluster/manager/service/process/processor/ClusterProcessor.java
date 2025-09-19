package com.lnjoying.justice.cluster.manager.service.process.processor;

import com.lnjoying.justice.cluster.manager.common.ClusterMsg;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.service.cluster.ClusterService;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class ClusterProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private ClusterService k8sClusterService;
    
    @Override
    public void start()
    {
        LOGGER.info("cluster message processor started");
    }
    
    @Override
    public void stop()
    {
        LOGGER.info("cluster message processor stopped");
    }
    
    @Override
    public void run()
    {
        LOGGER.info("cluster message processor running ");
        BlockingQueue<Object> queue = getBlockQueue();
        while (true)
        {
            try
            {
                MessagePack pack = (MessagePack) queue.take();
                
                switch (pack.getMsgType())
                {
                    case ClusterMsg.ASSIGN:
                    {
                        TblClusterInfo tblClusterInfo = (TblClusterInfo) pack.getMessageObj();
                        k8sClusterService.assignDev(tblClusterInfo);
                        break;
                    }
                    case ClusterMsg.RELEASE:
                    {
                        ClusterInnerInfo clusterInnerInfo = (ClusterInnerInfo) pack.getMessageObj();
                        k8sClusterService.releaseDev(clusterInnerInfo);
                        break;
                    }
                    
                    case ClusterMsg.UPDATE_CLUSTER:
                    {
                        ClusterInnerInfo clusterInnerInfo = (ClusterInnerInfo) pack.getMessageObj();
                        k8sClusterService.updateCluster(clusterInnerInfo);
                        break;
                    }
                    
                    case ClusterMsg.UPDATE_NODE:
                    {
                    
                    }
                    
                    case ClusterMsg.PREPARE_BUILD:
                    {
                        ClusterInnerInfo clusterInnerInfo = (ClusterInnerInfo) pack.getMessageObj();
                        k8sClusterService.prepareBuildCluster(clusterInnerInfo);
                        break;
                    }
                    
                    default:
                    {
                        LOGGER.error("unknown action {}", pack.getMsgType());
                    }
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                LOGGER.error("ClusterProcessor.run error {}", e.getMessage());
            }
            catch (IllegalStateException e)
            {
                LOGGER.error("service comb error.{}", e.getMessage());
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                LOGGER.error("ClusterProcessor.run error {}", e.getMessage());
            }
        }
    }
}
