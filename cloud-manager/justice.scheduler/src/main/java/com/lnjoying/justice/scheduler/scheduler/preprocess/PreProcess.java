package com.lnjoying.justice.scheduler.scheduler.preprocess;

import com.lnjoying.justice.scheduler.common.constant.OnOneNodeStrategy;
import com.lnjoying.justice.scheduler.common.constant.SchedulerMsgType;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.scheduler.common.constant.Strategy;
import com.lnjoying.justice.scheduler.domain.model.AssignEdgeReq;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.schema.entity.scheduler.AssignEdge2InstReq;
import com.lnjoying.justice.schema.entity.scheduler.AssignEdge2StackReq;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Component
public class PreProcess
{
    private static Logger LOGGER = LogManager.getLogger();

    public void preProcess(SchedulerBean schedulerBean)
    {
        AssignEdgeReq req = new AssignEdgeReq();
        schedulerBean.setReq(req);
        switch(schedulerBean.getSchedulerMsgType())
        {
            case SchedulerMsgType.INST:
                preProcessInstReq(schedulerBean);
                break;
            case SchedulerMsgType.STACK:
                preProcessStackReq(schedulerBean);
                break;
            default:
                LOGGER.info("[step 1 error]unsupported product: {}.", schedulerBean.getSchedulerMsgType());
                schedulerBean.setSchedulerState(SchedulerState.UNSUPPORTED_PRODUCT_TYPE.getCode());
        }
        schedulerBean.setRegions(new HashSet<>());
        schedulerBean.setSites(new HashSet<>());
        schedulerBean.setNodes(new ArrayList<>());
    }

    private void preProcessInstReq(SchedulerBean schedulerBean)
    {
        AssignEdgeReq req = new AssignEdgeReq();
        AssignEdge2InstReq assignEdge2InstReq = (AssignEdge2InstReq)schedulerBean.getOriginReq();
        LOGGER.info("[step 1]pre process: {}, ref id: {}.", schedulerBean.getSchedulerMsgType(), assignEdge2InstReq.getSpecId());
        if (StringUtils.isNotEmpty(assignEdge2InstReq.getScheduling_strategy().getStrategy()))
        {
            req.setStrategy(assignEdge2InstReq.getScheduling_strategy().getStrategy());
        }
        else
        {
            req.setStrategy(Strategy.DEFAULT_STRATEGY.getValue());
        }
        LOGGER.info("scheduling strategy: {}.", req.getStrategy());

        if (null == assignEdge2InstReq.getDev_need_info())
        {
            schedulerBean.setSchedulerState(SchedulerState.DEV_NEED_INFO_ERROR.getCode());
            LOGGER.error("scheduling error，devNeedInfo is null");
            return;
        }
        else
        {
            req.setDevNeedInfo(assignEdge2InstReq.getDev_need_info());
        }

        if (null == assignEdge2InstReq.getOn_one_node())
        {
            schedulerBean.setSchedulerState(SchedulerState.ON_ONE_NODE_ERROR.getCode());
            LOGGER.error("scheduling error，onOneNode is null");
            return;
        }
        else
        {
            req.setOnOneNode(assignEdge2InstReq.getOn_one_node());
        }

        if (null == assignEdge2InstReq.getTarget_nodes())
        {
            schedulerBean.setSchedulerState(SchedulerState.TARGET_NODES_ERROR.getCode());
            LOGGER.error("scheduling error，targetNodes is null");
            return;
        }
        else
        {
            req.setTargetNodes(assignEdge2InstReq.getTarget_nodes());
        }

        if (null == assignEdge2InstReq.getReplica_num() || null == assignEdge2InstReq.getWaitAssignIdList()
                || assignEdge2InstReq.getReplica_num() < 0)
        {
            schedulerBean.setSchedulerState(SchedulerState.REPLICA_OR_REF_ERROR.getCode());
            LOGGER.error("scheduling error，replicaNum < 0 or waitAssignIdList is null");
            return;
        }
        else
        {
            if (assignEdge2InstReq.getReplica_num() == 0 && assignEdge2InstReq.getWaitAssignIdList().size() == 0)
            {
                schedulerBean.setSchedulerState(SchedulerState.SUCCESS.getCode());
                LOGGER.info("scheduling success，replicaNum = 0 and waitAssignIdList is empty");
                return;
            }
            else if (assignEdge2InstReq.getReplica_num() == assignEdge2InstReq.getWaitAssignIdList().size())
            {
                req.setReplicaNum(assignEdge2InstReq.getReplica_num());
                req.setRefIdList(assignEdge2InstReq.getWaitAssignIdList());
            }
            else
            {
                schedulerBean.setSchedulerState(SchedulerState.REPLICA_OR_REF_ERROR.getCode());
                LOGGER.error("scheduling error，replicaNum not equals to waitAssignIdList size");
                return;
            }
        }

        if (null == assignEdge2InstReq.getScheduling_strategy().getLabelSelectorMap())
        {
            req.setLabelSelectorMap(new HashMap<>());
        }
        else
        {
            req.setLabelSelectorMap(assignEdge2InstReq.getScheduling_strategy().getLabelSelectorMap());
        }

        req.setReplicaCompleteStrategy(assignEdge2InstReq.getScheduling_strategy().isReplicaCompleteStrategy());

        req.setWaitAssignId(assignEdge2InstReq.getSpecId());
        req.setUserId(assignEdge2InstReq.getUser_id());
        req.setBpId(assignEdge2InstReq.getBp_id());

        schedulerBean.setReq(req);
    }

    private void preProcessStackReq(SchedulerBean schedulerBean)
    {
        AssignEdgeReq req = new AssignEdgeReq();
        AssignEdge2StackReq assignEdge2StackReq = (AssignEdge2StackReq)schedulerBean.getOriginReq();
        LOGGER.info("[step 1]pre process: {}, ref id: {}.", schedulerBean.getSchedulerMsgType(), assignEdge2StackReq.getSpecId());
        if (StringUtils.isNotEmpty(assignEdge2StackReq.getScheduling_strategy().getStrategy()))
        {
            req.setStrategy(assignEdge2StackReq.getScheduling_strategy().getStrategy());
        }
        else
        {
            req.setStrategy(Strategy.DEFAULT_STRATEGY.getValue());
        }
        LOGGER.info("scheduling strategy: {}.", req.getStrategy());

        if (null == assignEdge2StackReq.getDev_need_info())
        {
            schedulerBean.setSchedulerState(SchedulerState.DEV_NEED_INFO_ERROR.getCode());
            LOGGER.error("scheduling error，devNeedInfo is null");
            return;
        }
        else
        {
            req.setDevNeedInfo(assignEdge2StackReq.getDev_need_info());
        }

        req.setOnOneNode(OnOneNodeStrategy.DISPERSE.getCode());
        LOGGER.info("default onOneNode strategy");

        if (null == assignEdge2StackReq.getTarget_nodes())
        {
            schedulerBean.setSchedulerState(SchedulerState.TARGET_NODES_ERROR.getCode());
            LOGGER.error("scheduling error，targetNodes is null");
            return;
        }
        else
        {
            req.setTargetNodes(assignEdge2StackReq.getTarget_nodes());
        }

        if (null == assignEdge2StackReq.getReplica_num() || null == assignEdge2StackReq.getWaitAssignIdList()
                || assignEdge2StackReq.getReplica_num() < 0)
        {
            schedulerBean.setSchedulerState(SchedulerState.REPLICA_OR_REF_ERROR.getCode());
            LOGGER.error("scheduling error，replicaNum < 0 or waitAssignIdList is null");
            return;
        }
        else
        {
            if (assignEdge2StackReq.getReplica_num() == 0 && assignEdge2StackReq.getWaitAssignIdList().size() == 0)
            {
                schedulerBean.setSchedulerState(SchedulerState.SUCCESS.getCode());
                LOGGER.info("scheduling success，replicaNum = 0 and waitAssignIdList is empty");
                return;
            }
            else if (assignEdge2StackReq.getReplica_num() == assignEdge2StackReq.getWaitAssignIdList().size())
            {
                req.setReplicaNum(assignEdge2StackReq.getReplica_num());
                req.setRefIdList(assignEdge2StackReq.getWaitAssignIdList());
            }
            else
            {
                schedulerBean.setSchedulerState(SchedulerState.REPLICA_OR_REF_ERROR.getCode());
                LOGGER.error("scheduling error，replicaNum not equals to waitAssignIdList size");
                return;
            }
        }

        if (null == assignEdge2StackReq.getScheduling_strategy().getLabelSelectorMap())
        {
            req.setLabelSelectorMap(new HashMap<>());
        }
        else
        {
            req.setLabelSelectorMap(assignEdge2StackReq.getScheduling_strategy().getLabelSelectorMap());
        }

        req.setReplicaCompleteStrategy(assignEdge2StackReq.getScheduling_strategy().isReplicaCompleteStrategy());

        req.setWaitAssignId(assignEdge2StackReq.getSpecId());
        req.setUserId(assignEdge2StackReq.getUser_id());
        req.setBpId(assignEdge2StackReq.getBp_id());

        schedulerBean.setReq(req);
    }
}
