package com.lnjoying.justice.scheduler.scheduler.framework.bindrelations;

import com.lnjoying.justice.scheduler.common.constant.OnOneNodeStrategy;
import com.lnjoying.justice.scheduler.common.constant.SchedulerElement;
import com.lnjoying.justice.scheduler.scheduler.framework.BaseElement;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.scheduler.db.mapper.TblEdgeComputeInfoMapper;
import com.lnjoying.justice.scheduler.domain.model.NodeResourcesInfo;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.scheduler.scheduler.framework.noderesources.ResourcesUtil;
import com.lnjoying.justice.schema.entity.scheduler.BindRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

@Component
public class BindRelations extends BaseElement
{
    BindRelations()
    {
        schedulerElement = SchedulerElement.BIND_RELATIONS;
    }

    @Autowired
    TblEdgeComputeInfoMapper tblEdgeComputeInfoMapper;

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

        bindRelations(schedulerBean);
    }

    private void bindRelations(SchedulerBean schedulerBean)
    {
        OnOneNodeStrategy onOneNodeStrategy = OnOneNodeStrategy.fromCode(schedulerBean.getReq().getOnOneNode());
        if (null == onOneNodeStrategy)
        {
            schedulerBean.setSchedulerState(SchedulerState.ON_ONE_NODE_ERROR.getCode());
            return;
        }
        switch (onOneNodeStrategy) {
            case ON_ONE_NODE:
                schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_NODE.getCode());
                if (schedulerBean.getNodes().size() > 0)
                {
                    for (String nodeId:schedulerBean.getNodes())
                    {
                        NodeResourcesInfo nodeRemainingResources = resourcesUtil.getResources(nodeId);
                        for (String refId:schedulerBean.getReq().getRefIdList())
                        {
                            if (resourcesUtil.requireResouceMatchable(nodeRemainingResources, schedulerBean.getReq().getDevNeedInfo(), schedulerBean.getSchedulerMsgType(), refId, false))
                            {
                                BindRelation bindRelation = new BindRelation();
                                bindRelation.setWaitAssignId(refId);
                                bindRelation.setDstNodeId(nodeRemainingResources.getNodeId());
                                bindRelation.setDstSiteId(nodeRemainingResources.getSiteId());
                                bindRelation.setDstRegionId(nodeRemainingResources.getRegionId());
                                schedulerBean.getBindRelations().add(bindRelation);
                            }
                            else
                            {
                                LOGGER.info("[BindRelations-{}]request resources error, node: {}.", onOneNodeStrategy.getName(), nodeId);
                                resourcesUtil.releaseResources(nodeId, schedulerBean.getReq().getRefIdList());
                                schedulerBean.getBindRelations().clear();
                                break;
                            }
                        }
                        if (schedulerBean.getBindRelations().size() == schedulerBean.getReq().getRefIdList().size())
                        {
                            LOGGER.info("[BindRelations-{}]bind success, relations: {}.", onOneNodeStrategy.getName(), schedulerBean.getBindRelations());
                            schedulerBean.setSchedulerState(SchedulerState.SUCCESS.getCode());
                            break;
                        }
                        else
                        {
                            LOGGER.info("[BindRelations-{}]bind filed, all raplica: {}, satisfied {}.", onOneNodeStrategy.getName(), schedulerBean.getReq().getReplicaNum(), schedulerBean.getBindRelations().size());
                        }
                    }
                }
                else
                {
                    schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_NODE.getCode());
                    LOGGER.info("[BindRelations-{}]bind filed, reason: no matched node.", onOneNodeStrategy.getName());
                }
                break;
            case DEFAULT:
                if (schedulerBean.getNodes().size() == 0)
                {
                    schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_NODE.getCode());
                    LOGGER.info("[BindRelations-{}]bind filed, reason: no matched node.", onOneNodeStrategy.getName());
                }
                else
                {
                    Queue<String> queue = new LinkedList<String>(schedulerBean.getNodes());
                    int replicaNum = schedulerBean.getReq().getReplicaNum();
                    while (!queue.isEmpty())
                    {
                        String nodeId = queue.poll();
                        NodeResourcesInfo nodeRemainingResources = resourcesUtil.getResources(nodeId);
                        String refId = schedulerBean.getReq().getRefIdList().get(schedulerBean.getReq().getReplicaNum() - replicaNum);
                        if (resourcesUtil.requireResouceMatchable(nodeRemainingResources, schedulerBean.getReq().getDevNeedInfo(), schedulerBean.getSchedulerMsgType(), refId, false))
                        {
                            BindRelation bindRelation = new BindRelation();
                            bindRelation.setWaitAssignId(refId);
                            bindRelation.setDstNodeId(nodeRemainingResources.getNodeId());
                            bindRelation.setDstSiteId(nodeRemainingResources.getSiteId());
                            bindRelation.setDstRegionId(nodeRemainingResources.getRegionId());
                            schedulerBean.getBindRelations().add(bindRelation);

                            replicaNum--;

                            queue.offer(nodeId);
                        }
                        else
                        {
                            LOGGER.info("[BindRelations-{}]request resources error, node: {}.", onOneNodeStrategy.getName(), nodeId);
                        }
                        if (replicaNum == 0)
                        {
                            break;
                        }
                    }
                    if (replicaNum == 0)
                    {
                        schedulerBean.setSchedulerState(SchedulerState.SUCCESS.getCode());
                        LOGGER.info("[BindRelations-{}]bind success, relations: {}.", onOneNodeStrategy.getName(), schedulerBean.getBindRelations());
                    }
                    else
                    {
                        schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_NODE.getCode());

                        for (BindRelation bindRelation :schedulerBean.getBindRelations())
                        {
                            resourcesUtil.releaseResources(bindRelation.getDstNodeId(), bindRelation.getWaitAssignId());
                        }
                        LOGGER.info("[BindRelations-{}]bind filed, release relations: {}.", onOneNodeStrategy.getName(), schedulerBean.getBindRelations());
                    }
                }
                break;
            case DISPERSE:
                if (schedulerBean.getNodes().size() >= schedulerBean.getReq().getReplicaNum())
                {
                    int replicaNum = schedulerBean.getReq().getReplicaNum();
                    for (String nodeId:schedulerBean.getNodes())
                    {
                        NodeResourcesInfo nodeRemainingResources = resourcesUtil.getResources(nodeId);
                        String refId = schedulerBean.getReq().getRefIdList().get(schedulerBean.getReq().getReplicaNum() - replicaNum);
                        if (resourcesUtil.requireResouceMatchable(nodeRemainingResources, schedulerBean.getReq().getDevNeedInfo(), schedulerBean.getSchedulerMsgType(), refId, false))
                        {
                            BindRelation bindRelation = new BindRelation();
                            bindRelation.setWaitAssignId(refId);
                            bindRelation.setDstNodeId(nodeRemainingResources.getNodeId());
                            bindRelation.setDstSiteId(nodeRemainingResources.getSiteId());
                            bindRelation.setDstRegionId(nodeRemainingResources.getRegionId());
                            schedulerBean.getBindRelations().add(bindRelation);

                            replicaNum--;
                        }
                        else
                        {
                            LOGGER.info("[BindRelations-{}]request resources error, node: {}.", onOneNodeStrategy.getName(), nodeId);
                        }
                        if (replicaNum == 0)
                        {
                            break;
                        }
                    }
                    if (replicaNum == 0)
                    {
                        schedulerBean.setSchedulerState(SchedulerState.SUCCESS.getCode());
                        LOGGER.info("[BindRelations-{}]bind success, relations: {}.", onOneNodeStrategy.getName(), schedulerBean.getBindRelations());
                    }
                    else
                    {
                        schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_NODE.getCode());

                        for (BindRelation bindRelation :schedulerBean.getBindRelations())
                        {
                            resourcesUtil.releaseResources(bindRelation.getDstNodeId(), bindRelation.getWaitAssignId());
                        }
                        LOGGER.info("[BindRelations-{}]bind filed, release relations: {}.", onOneNodeStrategy.getName(), schedulerBean.getBindRelations());
                    }
                }
                else
                {
                    schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_NODE.getCode());
                    LOGGER.info("[BindRelations-{}]bind filed, reason: no matched node.", onOneNodeStrategy.getName());
                }
                break;
            case MONOPOLY:
                if (schedulerBean.getNodes().size() >= schedulerBean.getReq().getReplicaNum())
                {
                    int replicaNum = schedulerBean.getReq().getReplicaNum();
                    for (String nodeId:schedulerBean.getNodes())
                    {
                        NodeResourcesInfo nodeRemainingResources = resourcesUtil.getResources(nodeId);
                        String refId = schedulerBean.getReq().getRefIdList().get(schedulerBean.getReq().getReplicaNum() - replicaNum);
                        if (resourcesUtil.requireResouceMatchable(nodeRemainingResources, schedulerBean.getReq().getDevNeedInfo(), schedulerBean.getSchedulerMsgType(), refId, true))
                        {
                            BindRelation bindRelation = new BindRelation();
                            bindRelation.setWaitAssignId(refId);
                            bindRelation.setDstNodeId(nodeRemainingResources.getNodeId());
                            bindRelation.setDstSiteId(nodeRemainingResources.getSiteId());
                            bindRelation.setDstRegionId(nodeRemainingResources.getRegionId());
                            schedulerBean.getBindRelations().add(bindRelation);

                            replicaNum--;
                        }
                        else
                        {
                            LOGGER.info("[BindRelations-{}]request resources error, node: {}.", onOneNodeStrategy.getName(), nodeId);
                        }
                        if (replicaNum == 0)
                        {
                            break;
                        }
                    }
                    if (replicaNum == 0)
                    {
                        schedulerBean.setSchedulerState(SchedulerState.SUCCESS.getCode());
                        LOGGER.info("[BindRelations-{}]bind success, relations: {}.", onOneNodeStrategy.getName(), schedulerBean.getBindRelations());
                    }
                    else
                    {
                        schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_NODE.getCode());

                        for (BindRelation bindRelation :schedulerBean.getBindRelations())
                        {
                            resourcesUtil.releaseResources(bindRelation.getDstNodeId(), bindRelation.getWaitAssignId());
                        }
                        LOGGER.info("[BindRelations-{}]bind filed, release relations: {}.", onOneNodeStrategy.getName(), schedulerBean.getBindRelations());
                    }
                }
                else
                {
                    schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_NODE.getCode());
                    LOGGER.info("[BindRelations-{}]bind filed, reason: no matched node.", onOneNodeStrategy.getName());
                }
                break;
            default:
        }
    }
}
