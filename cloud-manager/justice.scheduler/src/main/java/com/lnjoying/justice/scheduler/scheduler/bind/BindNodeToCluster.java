package com.lnjoying.justice.scheduler.scheduler.bind;

import com.lnjoying.justice.scheduler.common.constant.EdgeMonopolyState;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.schema.entity.scheduler.*;
import com.lnjoying.justice.schema.service.cluster.ClusterManagerService;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class BindNodeToCluster extends BindNode
{
    @RpcReference(microserviceName = "cls", schemaId = "clusterManagerService")
    protected ClusterManagerService clusterManagerService;

    public void bindNode(SchedulerBean schedulerBean, List<SchedulerBean> successSchedulerBeans)
    {
        LOGGER.info("[step 3]bind node, ref id: {}, scheduler state: {}.", schedulerBean.getReq().getWaitAssignId(), schedulerBean.getSchedulerState());
        bindNodeToCluster(schedulerBean, successSchedulerBeans);
    }

    private void bindNodeToCluster(SchedulerBean schedulerBean, List<SchedulerBean> successSchedulerBeans)
    {
        SchedulerClusterResult schedulerClusterResult = new SchedulerClusterResult();
        schedulerClusterResult.setSchedulerState(schedulerBean.getSchedulerState());
        schedulerClusterResult.setClusterId(schedulerBean.getReq().getWaitAssignId());
        if (schedulerClusterResult.getSchedulerState() == SchedulerState.SUCCESS.getCode())
        {
            List<ClusterBindNode> clusterBindNodes = new ArrayList<>();
            successSchedulerBeans.forEach(successSchedulerBean -> {
                successSchedulerBean.getBindRelations().forEach(bindRelation -> {
                    ClusterBindNode clusterBindNode = new ClusterBindNode();
                    clusterBindNode.setNodeId(bindRelation.getDstNodeId());
                    clusterBindNode.setSiteId(bindRelation.getDstSiteId());
                    clusterBindNode.setRegionId(bindRelation.getDstRegionId());
                    clusterBindNode.setClusterRoles(successSchedulerBean.getReq().getWaitAssignId());
                    clusterBindNodes.add(clusterBindNode);
                });
            });

            schedulerClusterResult.setBindNodes(clusterBindNodes);
        }

        BindResourcesResult bindResourcesResult = null;
        try
        {
            bindResourcesResult = clusterManagerService.bindResources(schedulerClusterResult);
        }
        catch (IllegalStateException e)
        {
            LOGGER.error("microservice error, waiting for resend schedule request");
        }

        LOGGER.info("[step 3.1]bind node, scheduler result:{} , bind result: {}.", schedulerClusterResult, bindResourcesResult);

        successSchedulerBeans.forEach(this::delResourcesCache);

        if (null != bindResourcesResult)
        {
            if (! CollectionUtils.isEmpty(bindResourcesResult.getSuccessBindRelations()))
            {
                bindResourcesResult.getSuccessBindRelations().forEach(bindRelation -> {bindRelation.setWaitAssignId(schedulerClusterResult.getClusterId());});
                bindEdgeMonopoly(bindResourcesResult.getSuccessBindRelations(), EdgeMonopolyState.CLUSTER_MONOPOLY);
            }

            if (! CollectionUtils.isEmpty(bindResourcesResult.getFailBindRelations()))
            {
                releaseResourceWhileScheduleFail(bindResourcesResult.getFailBindRelations());
            }
        }
        else
        {
            successSchedulerBeans.forEach(successSchedulerBean -> releaseResourceWhileScheduleFail(successSchedulerBean.getBindRelations()));
        }
    }
}
