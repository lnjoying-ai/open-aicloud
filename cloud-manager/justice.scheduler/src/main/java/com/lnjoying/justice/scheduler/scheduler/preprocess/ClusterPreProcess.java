package com.lnjoying.justice.scheduler.scheduler.preprocess;

import com.lnjoying.justice.scheduler.common.constant.AffinityState;
import com.lnjoying.justice.scheduler.common.constant.OnOneNodeStrategy;
import com.lnjoying.justice.scheduler.common.constant.Strategy;
import com.lnjoying.justice.scheduler.domain.model.AssignEdgeReq;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.schema.entity.dev.K8sDevNeed;
import com.lnjoying.justice.schema.msg.scheduler.AssignEdge2ClusterReq;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Component
public class ClusterPreProcess
{
    private static Logger LOGGER = LogManager.getLogger();

    public List<SchedulerBean> preProcess(SchedulerBean schedulerBean)
    {
        List<SchedulerBean> schedulerBeans = new ArrayList<>();
        AssignEdge2ClusterReq assignEdge2ClusterReq = (AssignEdge2ClusterReq)schedulerBean.getOriginReq();
        for (K8sDevNeed k8sDevNeed : assignEdge2ClusterReq.getDevNeeds())
        {
            SchedulerBean newSchedulerBean = new SchedulerBean();
            newSchedulerBean.setSchedulerMsgType(schedulerBean.getSchedulerMsgType());
            newSchedulerBean.setSchedulerState(schedulerBean.getSchedulerState());
            newSchedulerBean.setBindRelations(new ArrayList<>());
            newSchedulerBean.setAffinityState(AffinityState.DEFAULT.getCode());

            AssignEdgeReq req = new AssignEdgeReq();
            newSchedulerBean.setReq(req);

            preProcessClusterReq(newSchedulerBean, assignEdge2ClusterReq, k8sDevNeed);

            newSchedulerBean.setRegions(new HashSet<>());
            newSchedulerBean.setSites(new HashSet<>());
            newSchedulerBean.setNodes(new ArrayList<>());

            schedulerBeans.add(newSchedulerBean);
        }

        AssignEdgeReq req = new AssignEdgeReq();
        schedulerBean.setReq(req);
        req.setWaitAssignId(assignEdge2ClusterReq.getSpecId());
        return schedulerBeans;
    }

    private void preProcessClusterReq(SchedulerBean schedulerBean, AssignEdge2ClusterReq assignEdge2ClusterReq, K8sDevNeed k8sDevNeed)
    {
        AssignEdgeReq req = new AssignEdgeReq();
        LOGGER.info("[step 1]pre process: {}, ref id: {}.", schedulerBean.getSchedulerMsgType(), assignEdge2ClusterReq.getSpecId());
        if (StringUtils.isNotEmpty(assignEdge2ClusterReq.getSchedulingStrategy().getStrategy()))
        {
            req.setStrategy(assignEdge2ClusterReq.getSchedulingStrategy().getStrategy());
        }
        else
        {
            req.setStrategy(Strategy.DEFAULT_STRATEGY.getValue());
        }

        if (null == k8sDevNeed)
        {
            schedulerBean.setSchedulerState(SchedulerState.DEV_NEED_INFO_ERROR.getCode());
            return;
        }
        else
        {
            req.setDevNeedInfo(k8sDevNeed.getSpec());
        }

        req.setOnOneNode(OnOneNodeStrategy.MONOPOLY.getCode());


        if (null == assignEdge2ClusterReq.getTargetNodes())
        {
            schedulerBean.setSchedulerState(SchedulerState.TARGET_NODES_ERROR.getCode());
            return;
        }
        else
        {
            req.setTargetNodes(assignEdge2ClusterReq.getTargetNodes());
        }

        if (null == assignEdge2ClusterReq.getReplicaNum() || null == assignEdge2ClusterReq.getWaitAssignIdList()
                || assignEdge2ClusterReq.getReplicaNum() < 0 || k8sDevNeed.getNum() < 0)
        {
            schedulerBean.setSchedulerState(SchedulerState.REPLICA_OR_REF_ERROR.getCode());
            return;
        }
        else
        {
            if ((assignEdge2ClusterReq.getReplicaNum() == 0 && assignEdge2ClusterReq.getWaitAssignIdList().size() == 0)
                    || k8sDevNeed.getNum() == 0)
            {
                schedulerBean.setSchedulerState(SchedulerState.SUCCESS.getCode());
                return;
            }
            else if (assignEdge2ClusterReq.getReplicaNum() == assignEdge2ClusterReq.getWaitAssignIdList().size())
            {
                req.setReplicaNum(assignEdge2ClusterReq.getReplicaNum() * k8sDevNeed.getNum());
                List<String> waitAssignIdList = new ArrayList<>();
                for (int i = 0; i < k8sDevNeed.getNum(); i++)
                {
                    waitAssignIdList.addAll(assignEdge2ClusterReq.getWaitAssignIdList());
                }
                req.setRefIdList(waitAssignIdList);
            }
            else
            {
                schedulerBean.setSchedulerState(SchedulerState.REPLICA_OR_REF_ERROR.getCode());
                return;
            }
        }

        if (null == assignEdge2ClusterReq.getSchedulingStrategy().getLabelSelectorMap())
        {
            req.setLabelSelectorMap(new HashMap<>());
        }
        else
        {
            req.setLabelSelectorMap(assignEdge2ClusterReq.getSchedulingStrategy().getLabelSelectorMap());
        }

        req.setReplicaCompleteStrategy(assignEdge2ClusterReq.getSchedulingStrategy().isReplicaCompleteStrategy());

        req.setWaitAssignId(k8sDevNeed.getRoles());
        req.setUserId(assignEdge2ClusterReq.getUserId());
        req.setBpId(assignEdge2ClusterReq.getBpId());

        schedulerBean.setReq(req);
    }
}
