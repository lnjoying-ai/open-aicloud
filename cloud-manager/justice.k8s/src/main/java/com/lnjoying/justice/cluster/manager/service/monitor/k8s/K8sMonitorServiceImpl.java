package com.lnjoying.justice.cluster.manager.service.monitor.k8s;

import com.lnjoying.justice.cluster.manager.common.Cls_Func;
import com.lnjoying.justice.cluster.manager.common.ClusterNodeStatus;
import com.lnjoying.justice.cluster.manager.common.InstanceState;
import com.lnjoying.justice.cluster.manager.common.PlanAction;
import com.lnjoying.justice.cluster.manager.db.repo.ClusterRepo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodeInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.cluster.manager.service.monitor.MonitorService;
import com.lnjoying.justice.cluster.manager.service.plan.ContainerPlan;
import com.lnjoying.justice.cluster.manager.service.rpc.CombRpcService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.lnjoying.justice.cluster.manager.common.PlanAction.*;


@Service("k8sMonitorService")
public class K8sMonitorServiceImpl implements MonitorService
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ClusterRepo clusterRepo;

    @Autowired
    private CombRpcService combRpcService;

    final long ACTION_OVERTIME = 60000;
    final long SLEEP_TIME = 60000;
    final int MaxErrorTime = 1000;

    @Override
    public ErrorCode monitor(ClusterNodePlanInfo clusterNodePlanInfo)  throws WebSystemException
    {
        if (clusterNodePlanInfo == null) return ErrorCode.SUCCESS;

        if (clusterNodePlanInfo.getNodeStatus() != ClusterNodeStatus.DEPLOYED && clusterNodePlanInfo.getNodeStatus() != ClusterNodeStatus.ACTIVE)
        {
            return ErrorCode.SUCCESS;
        }

        for (ContainerPlan plan : clusterNodePlanInfo.getMonitorPlanList())
        {
            Cls_Func<ClusterNodeInfo, ContainerPlan> monitorFunc = null;

            LOGGER.info("cluster id {}, node id {}, container {}, cur action:{}, next action:{}",
                    clusterNodePlanInfo.getClusterId(), clusterNodePlanInfo.getClusterNodeInfo().getNodeId(), plan.getContainerName(), plan.getCurAction(), plan.getNextAction());

            clusterNodePlanInfo.setTipMessage(plan.getTipMessage());

            switch (plan.getNextAction())
            {
                case REMOVE_BEFORE:
                    monitorFunc = removeContaierBf();
                    break;
                case CREATE:
                    if ((System.currentTimeMillis() - plan.getActionTime()) < SLEEP_TIME)
                    {
                        break;
                    }
                    monitorFunc = createContainer();
                    break;
                case LIST:
                    monitorFunc = listOperator();
                    break;
                case RESTART:
                    monitorFunc = restartOperator();
                    break;
                default:
                    break;
            }

            if (monitorFunc != null)
            {
                PlanAction preAction = plan.getPreAction();
                PlanAction curAction = plan.getCurAction();
                PlanAction nestAction = plan.getNextAction();
                try
                {
                    monitorFunc.operator(clusterNodePlanInfo.getClusterNodeInfo(), plan);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    plan.setPreAction(preAction);
                    plan.setCurAction(curAction);
                    plan.setNextAction(nestAction);
                }
            }
        }

        return ErrorCode.SUCCESS;
    }


    Cls_Func<ClusterNodeInfo, ContainerPlan> removeContaierBf()
    {
        Cls_Func<ClusterNodeInfo, ContainerPlan> removeFunction = (clusterNodeInfo, plan) ->
        {
            LOGGER.info("action: remove before for plan {} on {}", plan.getContainerName(), clusterNodeInfo.getNodeId());
            plan.setPreAction(plan.getCurAction());
            plan.setCurAction(REMOVE_BEFORE);

            TargetNode targetNode = new TargetNode(clusterNodeInfo.getRegionId(), clusterNodeInfo.getSiteId(), clusterNodeInfo.getNodeId());
            CombRetPacket combRetPacket = combRpcService.getInstService().removeContainer(targetNode, plan.getContainerName());

            if (combRetPacket.getStatus() == InstanceState.REMOVED.getCode())
            {
                plan.setNextAction(PlanAction.CREATE);
            }
            else if (combRetPacket.getStatus() == InstanceState.KEEP_ON.getCode())
            {
                plan.setNextAction(CREATE);
            }
            else
            {
                plan.setNextAction(PlanAction.LIST);
            }

            plan.setActionTime(System.currentTimeMillis());

            return ErrorCode.SUCCESS;
        };

        return removeFunction;
    }
    
    Cls_Func<ClusterNodeInfo, ContainerPlan> createContainer()
    {
        Cls_Func<ClusterNodeInfo, ContainerPlan> createFunction = (clusterNodeInfo, plan) ->
        {
            LOGGER.info("action: create for plan {} on {}", plan.getContainerName(), clusterNodeInfo.getNodeId());
    
            plan.setPreAction(plan.getCurAction());
            plan.setCurAction(PlanAction.CREATE);
            
            TargetNode targetNode = new TargetNode(clusterNodeInfo.getRegionId(), clusterNodeInfo.getSiteId(), clusterNodeInfo.getNodeId());
            CombRetPacket combRetPacket = combRpcService.getInstService()
                    .createContainer(targetNode, JsonUtils.toJson(plan.getDockerInstParams()), plan.getContainerName(), plan.getRegistryInfo(), plan.getAutoRun(), "k8s");
            
            LOGGER.info("create inst rsp code:{}", combRetPacket.getStatus());
            if (combRetPacket.getStatus() > InstanceState.MAKED.getCode())
            {
                plan.setNextAction(PlanAction.LIST);
            }
            
            plan.setActionTime(System.currentTimeMillis());
            return ErrorCode.SUCCESS;
        };
        
        return createFunction;
    }
    
    
    Cls_Func<ClusterNodeInfo, ContainerPlan> listOperator()
    {
        Cls_Func<ClusterNodeInfo, ContainerPlan> listFunction = (clusterNodeInfo, plan) ->
        {
            LOGGER.info("action: list for plan {} on {}", plan.getContainerName(), clusterNodeInfo.getNodeId());
    
            plan.setPreAction(plan.getCurAction());
            plan.setCurAction(PlanAction.LIST);
            
            CombRetPacket packetRet = listContainer(clusterNodeInfo, plan.getContainerName());
            if ((packetRet.getStatus() > InstanceState.SUCCESS_QUIT.getCode()
                    && packetRet.getStatus() < InstanceState.REMOVED.getCode()) ||
                    packetRet.getStatus() == InstanceState.EDGE_SYSTEM_ABNORMAL.getCode())
            {
                LOGGER.info("get error for {}, error status:{}", plan.getContainerName(), packetRet.getStatus());
                
                if (plan.getErrorTime() > MaxErrorTime)
                {
                    plan.setNextAction(PlanAction.ABNORMAL_QUIT);
                    return ErrorCode.CLUSTER_MONITOR_FAILED;
                }
                else
                {
                    plan.setErrorTime(plan.getErrorTime() + 1);
                    plan.setNextAction(REMOVE_BEFORE);
                    return ErrorCode.SUCCESS;
                }
            }
            
            long curTime = System.currentTimeMillis();
            long interVal = curTime - plan.getActionTime();
            LOGGER.info("list {} inst rsp code:{}", plan.getContainerName(), packetRet.getStatus());
            switch (plan.getPreAction())
            {
                case REMOVE_BEFORE:
                    if (packetRet.getStatus() == InstanceState.REMOVED.getCode())
                    {
                        plan.setNextAction(PlanAction.CREATE);
                    }
                    else
                    {
                        if (interVal > ACTION_OVERTIME)
                        {
                            plan.setErrorTime(plan.getErrorTime() + 1);
                            plan.setNextAction(REMOVE_BEFORE);
                        }
                        else
                        {
                            LOGGER.info("try remove_bf plan again. {}", plan.getContainerName());
                            plan.setCurAction(REMOVE_BEFORE);
                            plan.setNextAction(LIST);
                        }
                    }
                    break;
                case CREATE:
                    if (packetRet.getStatus() == InstanceState.RUNNING.getCode())
                    {
                        plan.setActionTime(curTime);
                        plan.setNextAction(CREATE);
                    }
                    else if (packetRet.getStatus() >= InstanceState.SUCCESS_QUIT.getCode())
                    {
                        //maybe send create cmd was not arrive, list inst can not get the result
                        if (packetRet.getStatus()  == InstanceState.REMOVED.getCode() || packetRet.getStatus()  == InstanceState.OBJECT_NOT_EXIST.getCode())
                        {
                            plan.setNextAction(CREATE);
                        }
                        else if (packetRet.getStatus() <= InstanceState.ABNORMAL_QUIT.getCode())
                        {
                            plan.setNextAction(RESTART);
                        }
                        else
                        {
                            plan.setNextAction(REMOVE_BEFORE);
                        }
                    }
                    else if(packetRet.getStatus() < InstanceState.CREATED.getCode())
                    {
                        if (interVal > ACTION_OVERTIME *5)
                        {
                            plan.setErrorTime(plan.getErrorTime() + 1);
                            plan.setNextAction(REMOVE_BEFORE);
                        }
                        else
                        {
                            plan.setCurAction(CREATE);
                            plan.setNextAction(LIST);
                        }
                    }
                    else
                    {
                        if (interVal > ACTION_OVERTIME * 5)
                        {
                            plan.setErrorTime(plan.getErrorTime() + 1);
                            plan.setNextAction(REMOVE_BEFORE);
                        }
                        else
                        {
                            plan.setCurAction(CREATE);
                            plan.setNextAction(LIST);
                        }
                    }
                    break;
                case RESTART:
                    if (packetRet.getStatus() == InstanceState.RUNNING.getCode())
                    {
                        plan.setActionTime(curTime);
                        plan.setNextAction(CREATE);
                    }
                    else
                    {
                        plan.setNextAction(REMOVE_BEFORE);
                    }
                default:
                    if (packetRet.getStatus() == InstanceState.REMOVED.getCode() || packetRet.getStatus()  == InstanceState.OBJECT_NOT_EXIST.getCode())
                    {
                        plan.setNextAction(PlanAction.CREATE);
                    }
                    else
                    {
                        LOGGER.error("unknown status, {} pre action {}", plan.getContainerName(), plan.getPreAction());
                        plan.setNextAction(REMOVE_BEFORE);
                    }
                    break;
            }
            
            return ErrorCode.SUCCESS;
        };
        
        return listFunction;
    }
    
    CombRetPacket listContainer(ClusterNodeInfo  clusterNodeInfo, String containerName)
    {
        TargetNode targetNode = new TargetNode(clusterNodeInfo.getRegionId(), clusterNodeInfo.getSiteId(), clusterNodeInfo.getNodeId());
        CombRetPacket combRetPacket = combRpcService.getInstService().listContainer(targetNode, containerName);
        return combRetPacket;
    }

    CombRetPacket actionContainer(ClusterNodeInfo  clusterNodeInfo, String containerName, String action)
    {
        TargetNode targetNode = new TargetNode(clusterNodeInfo.getRegionId(), clusterNodeInfo.getSiteId(), clusterNodeInfo.getNodeId());
        CombRetPacket combRetPacket = combRpcService.getInstService().actionContainer(targetNode, containerName, action);
        return combRetPacket;
    }

    Cls_Func<ClusterNodeInfo, ContainerPlan> restartOperator()
    {
        Cls_Func<ClusterNodeInfo, ContainerPlan> restartFunction = (clusterNodeInfo, plan) ->
        {
            LOGGER.info("action: restart for plan {} on {}", plan.getContainerName(), clusterNodeInfo.getNodeId());

            plan.setPreAction(plan.getCurAction());
            plan.setCurAction(PlanAction.RESTART);

            actionContainer(clusterNodeInfo, plan.getContainerName(), "restart");

            plan.setNextAction(PlanAction.LIST);

            return ErrorCode.SUCCESS;
        };

        return restartFunction;
    }
}
