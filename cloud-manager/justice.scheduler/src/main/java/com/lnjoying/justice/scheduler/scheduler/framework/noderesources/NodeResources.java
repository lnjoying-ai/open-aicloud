package com.lnjoying.justice.scheduler.scheduler.framework.noderesources;

import com.lnjoying.justice.scheduler.common.constant.OnOneNodeStrategy;
import com.lnjoying.justice.scheduler.common.constant.SchedulerElement;
import com.lnjoying.justice.scheduler.scheduler.framework.BaseElement;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.scheduler.scheduler.framework.ononenode.OnOneNode;
import com.lnjoying.justice.scheduler.domain.model.NodeResourcesInfo;
import com.lnjoying.justice.schema.entity.dev.DevNeedInfo;
import com.micro.core.persistence.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class NodeResources extends BaseElement
{
    NodeResources()
    {
        schedulerElement = SchedulerElement.NODE_RESOURCES;
    }

    @Autowired
    OnOneNode onOneNode;

    @Autowired
    ResourcesUtil resourcesUtil;

    @Override
    public void doScheduler(SchedulerBean schedulerBean)
    {
        if (!isScheduling(schedulerBean.getSchedulerState()) || isSkipElement(schedulerBean.getStrategyMask()))
        {
            return;
        }

        LOGGER.info("[element]name: {} mask: {}.", getElementName(), Integer.toBinaryString(getElementMask()));

        allocateNodeResources(schedulerBean);
    }

    private void allocateNodeResources(SchedulerBean schedulerBean)
    {
        int replicaNum = schedulerBean.getReq().getReplicaNum();
        Long nodeNum = RedisUtil.scard(schedulerBean.getNodeRedisKey());
        if (null == nodeNum || nodeNum == 0)
        {
            LOGGER.info("[NodeResources]no available node.");
            schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_NODE.getCode());
            return;
        }
        LOGGER.info("[NodeResources]available node num: {}.", nodeNum);
        Set<String> nodes = RedisUtil.smembers(schedulerBean.getNodeRedisKey());
        if (null == nodes)
        {
            LOGGER.info("[NodeResources]get nodes from redis error.");
            schedulerBean.setSchedulerState(SchedulerState.REDIS_ERROR.getCode());
            return;
        }
        OnOneNodeStrategy onOneNodeStrategy = OnOneNodeStrategy.fromCode(schedulerBean.getReq().getOnOneNode());
        if (null == onOneNodeStrategy)
        {
            schedulerBean.setSchedulerState(SchedulerState.ON_ONE_NODE_ERROR.getCode());
            return;
        }
        switch(onOneNodeStrategy)
        {
            case ON_ONE_NODE:
                LOGGER.info("[NodeResources-onOneNode]nodes: {}. replicaNum: {}", nodes, replicaNum);
                DevNeedInfo devNeedInfo = onOneNode.onOneNode(schedulerBean);
                for (String nodeId:nodes)
                {
                    NodeResourcesInfo nodeRemainingResources = resourcesUtil.getResources(nodeId);
                    if(null == nodeRemainingResources || nodeRemainingResources.isMonopoly())
                    {
                        LOGGER.info("[NodeResources-onOneNode]node: {} is null or in use or monopoly.", nodeId);
                        continue;
                    }
                    if (resourcesUtil.isResourcesSatisfied(nodeRemainingResources, devNeedInfo))
                    {
                        schedulerBean.getNodes().add(nodeId);
                        LOGGER.info("[NodeResources-onOneNode]node: {} satisfied.", nodeId);

                        if (isNotNeedGetAllNode(schedulerBean.getStrategyMask()))
                        {
                            break;
                        }
                    }
                }
                if (schedulerBean.getNodes().isEmpty())
                {
                    schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_NODE.getCode());
                }
                break;
            case DEFAULT:
                LOGGER.info("[NodeResources-default]nodes: {}. replicaNum: {}", nodes, replicaNum);
                for (String nodeId:nodes)
                {
                    NodeResourcesInfo nodeRemainingResources = resourcesUtil.getResources(nodeId);
                    if (null == nodeRemainingResources || nodeRemainingResources.isMonopoly())
                    {
                        LOGGER.info("[NodeResources-default]node: {} is null or in use or monopoly.", nodeId);
                        continue;
                    }
                    if (resourcesUtil.isResourcesSatisfied(nodeRemainingResources, schedulerBean.getReq().getDevNeedInfo()))
                    {
                        schedulerBean.getNodes().add(nodeId);
                        replicaNum --;
                        LOGGER.info("[NodeResources-default]node: {} satisfied.", nodeId);
                    }
                    else
                    {
                        continue;
                    }
                    if (replicaNum == 0 && isNotNeedGetAllNode(schedulerBean.getStrategyMask()))
                    {
                        LOGGER.info("[NodeResources-default]all replicaNum satisfied. satisfied nodes: {}", schedulerBean.getNodes());
                        break;
                    }
                }
                if (replicaNum == schedulerBean.getReq().getReplicaNum())
                {
                    LOGGER.info("[NodeResources-default]not satisfied. left replicaNum: {}", replicaNum);
                    schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_NODE.getCode());
                    return;
                }
                break;
            case DISPERSE:
                LOGGER.info("[NodeResources-disperse]nodes: {}. replicaNum: {}", nodes, replicaNum);
                for (String nodeId:nodes)
                {
                    NodeResourcesInfo nodeRemainingResources = resourcesUtil.getResources(nodeId);
                    if (null == nodeRemainingResources || nodeRemainingResources.isMonopoly())
                    {
                        LOGGER.info("[NodeResources-disperse]node: {} is null or in use or monopoly.", nodeId);
                        continue;
                    }
                    if (resourcesUtil.isResourcesSatisfied(nodeRemainingResources, schedulerBean.getReq().getDevNeedInfo()))
                    {
                        schedulerBean.getNodes().add(nodeId);
                        replicaNum --;
                        LOGGER.info("[NodeResources-disperse]node: {} satisfied.", nodeId);
                    }
                    else
                    {
                        continue;
                    }
                    if (replicaNum == 0 && isNotNeedGetAllNode(schedulerBean.getStrategyMask()))
                    {
                        LOGGER.info("[NodeResources-disperse]all replicaNum satisfied. satisfied nodes: {}", schedulerBean.getNodes());
                        break;
                    }
                }
                if (replicaNum > 0)
                {
                    LOGGER.info("[NodeResources-disperse]not satisfied. left replicaNum: {}", replicaNum);
                    schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_NODE.getCode());
                    return;
                }
                break;
            case MONOPOLY:
                LOGGER.info("[NodeResources-momopoly]nodes: {}. replicaNum: {}", nodes, replicaNum);
                for (String nodeId:nodes)
                {
                    NodeResourcesInfo nodeRemainingResources = resourcesUtil.getResources(nodeId);
                    if (null == nodeRemainingResources || nodeRemainingResources.isInUse() || nodeRemainingResources.isMonopoly())
                    {
                        LOGGER.info("[NodeResources-momopoly]node: {} is null or in use or monopoly.", nodeId);
                        continue;
                    }
                    if (resourcesUtil.isResourcesSatisfied(nodeRemainingResources, schedulerBean.getReq().getDevNeedInfo()))
                    {
                        schedulerBean.getNodes().add(nodeId);
                        replicaNum --;
                        LOGGER.info("[NodeResources-momopoly]node: {} satisfied.", nodeId);
                    }
                    else
                    {
                        continue;
                    }
                    if (replicaNum == 0 && isNotNeedGetAllNode(schedulerBean.getStrategyMask()))
                    {
                        LOGGER.info("[NodeResources-momopoly]all replicaNum satisfied. satisfied nodes: {}", schedulerBean.getNodes());
                        break;
                    }
                }
                if (replicaNum > 0)
                {
                    LOGGER.info("[NodeResources-momopoly]not satisfied. left replicaNum: {}", replicaNum);
                    schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_NODE.getCode());
                    return;
                }
                break;
            default:
        }
    }

    private boolean isNotNeedGetAllNode(Integer strategyMask)
    {
        return (strategyMask & SchedulerElement.BIND_LEAST.getMask()) == 0;
    }
}
