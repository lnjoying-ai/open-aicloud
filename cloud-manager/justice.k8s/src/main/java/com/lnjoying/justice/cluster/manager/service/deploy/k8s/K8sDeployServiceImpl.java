package com.lnjoying.justice.cluster.manager.service.deploy.k8s;

import com.lnjoying.justice.cluster.manager.common.*;
import com.lnjoying.justice.cluster.manager.db.repo.ClusterRepo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodeInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.cluster.manager.service.deploy.DeployService;
import com.lnjoying.justice.cluster.manager.service.plan.ContainerPlan;
import com.lnjoying.justice.cluster.manager.service.rpc.CombRpcService;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.lnjoying.justice.cluster.manager.util.K8sRedisField;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.lnjoying.justice.cluster.manager.common.PlanAction.*;


@Service("k8sDeployService")
public class K8sDeployServiceImpl implements DeployService
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private ClusterRepo clusterRepo;
    
    @Autowired
    private CombRpcService combRpcService;
    
    final Map<String, Integer> SystemAddonNumMap = new HashMap<String, Integer>(){{put(ClusterType.K8S, 6);put(ClusterType.K3S, 2);}};
    final long ACTION_OVERTIME = 60000;
    final int MaxErrorTime = 15;
    
    K8sDeployServiceImpl()
    {
    }
    
    @Override
    public ErrorCode deploy(ClusterNodePlanInfo clusterNodePlanInfo)  throws WebSystemException
    {
        if (clusterNodePlanInfo == null) return ErrorCode.CLUSTER_DEPLOY_PLAN_NULL;

        if (clusterNodePlanInfo.getNodeStatus().getCode() >= ClusterNodeStatus.DEPLOYED.getCode())
        {
            return ErrorCode.SUCCESS;
        }
        
        ContainerPlan plan = clusterNodePlanInfo.getContainerPlan();
        if (null == plan)
        {
            if (clusterNodePlanInfo.getNodeStatus().equals(ClusterNodeStatus.DEPLOYING))
            {
                LOGGER.info("cluster deployed on {}", clusterNodePlanInfo.getClusterNodeInfo().toString());
                clusterNodePlanInfo.setNodeStatus(ClusterNodeStatus.DEPLOYED);
                if (clusterNodePlanInfo.getClusterNodeInfo().getClusterRoles().contains(K8sRole.ETCD))
                {
                    clusterNodePlanInfo.getClusterNodeInfo().setExistingEtcdCluster(true);
                }
                RedisUtil.lpush(K8sRedisField.MONITOR_CLUSTER_NODE_LIST, clusterNodePlanInfo.getClusterId(), clusterNodePlanInfo.getClusterNodeInfo().getNodeId());
                return ErrorCode.SUCCESS;
            }
            else
            {
                LOGGER.error("no plan for {}", clusterNodePlanInfo.getClusterNodeInfo().getNodeId());
                return ErrorCode.CLUSTER_DEPLOY_PLAN_NULL;
            }
        }
        
        Cls_Func<ClusterNodeInfo, ContainerPlan> deployFunc = null;
        
        LOGGER.info("container {}, cur action:{}, next action:{}",
                plan.getContainerName(), plan.getCurAction(), plan.getNextAction());
        if (clusterNodePlanInfo.getNodeStatus().equals(ClusterNodeStatus.DEPLOY_FAILED))
        {
            return ErrorCode.SUCCESS;
        }
        
        if (clusterNodePlanInfo.getNodeStatus().equals(ClusterNodeStatus.PLANNED))
        {
            clusterNodePlanInfo.setNodeStatus(ClusterNodeStatus.DEPLOYING);
        }
    
        clusterNodePlanInfo.setTipMessage(plan.getTipMessage());

        if (plan.getContainerName().contains(DeployContainerName.NODE_LABEL_DEPLOYER) && plan.getCurAction() == INIT)
        {
            if (! clusterNodePlanInfo.getClusterNodeInfo().getClusterRoles().contains(K8sRole.CONTROLLER))
            {
                String queryRoles = Utils.buildStr("%",K8sRole.CONTROLLER,"%");
                long readyCount = clusterRepo.countClusterNodeInfo(clusterNodePlanInfo.getClusterId(), queryRoles, ClusterNodeStatus.DEPLOYED.getCode());
                if (readyCount < 1)
                {
                    LOGGER.info("no controller node ready for cluster {}", clusterNodePlanInfo.getClusterId());
                    return ErrorCode.SUCCESS;
                }
            }
        }

        switch (plan.getNextAction())
        {
            case REMOVE_BEFORE:
                deployFunc = removeContaierBf();
                break;
            case REMOVE_AFTER:
                deployFunc = removeContaierAf(plan);
                break;
            case CREATE:
                deployFunc = createContainer();
                break;
            case LIST:
                deployFunc = listOperator(plan);
                break;
            case LOG:
                deployFunc = logOperator();
                break;
            case LOG_CHECK:
                deployFunc = checkOperator(plan, clusterNodePlanInfo.getClusterId());
                break;
            case NEW:
                clusterNodePlanInfo.popContainerPlan();
                if (plan.isCoreContainer())
                {
                    plan.setNextAction(CREATE);
                    plan.setCurAction(CREATE);
                    plan.setPreAction(CREATE);
                    plan.setErrorTime(0);
                    clusterNodePlanInfo.addMonitorPlan(plan);
                }
            case ABNORMAL_QUIT:
                clusterNodePlanInfo.setNodeStatus(ClusterNodeStatus.DEPLOY_FAILED);
                clusterNodePlanInfo.setMonitorPlanList(null);
                return ErrorCode.CLUSTER_DEPLOY_FAILED;
            default:
                break;
        }
    
        if (deployFunc != null)
        {
            deployFunc.operator(clusterNodePlanInfo.getClusterNodeInfo(), plan);
        }
        
        if (plan.getNextAction() == PlanAction.NEW)
        {
            clusterNodePlanInfo.popContainerPlan();
            if (plan.isCoreContainer())
            {
                plan.setNextAction(CREATE);
                plan.setCurAction(CREATE);
                plan.setPreAction(CREATE);
                plan.setErrorTime(0);
                clusterNodePlanInfo.addMonitorPlan(plan);
            }
        }
        
        return ErrorCode.SUCCESS;
    }

    @Override
    public ErrorCode undeploy(ClusterNodePlanInfo clusterNodePlanInfo)  throws WebSystemException
    {
        if (clusterNodePlanInfo == null) return ErrorCode.CLUSTER_DEPLOY_PLAN_NULL;

        ContainerPlan plan = clusterNodePlanInfo.getContainerUPlan();
        if (clusterNodePlanInfo.getNodeStatus().equals(ClusterNodeStatus.UDEPLOYING) && plan == null)
        {
            LOGGER.info("cluster undeployed on {}", clusterNodePlanInfo.getClusterNodeInfo().toString());
            clusterNodePlanInfo.setNodeStatus(ClusterNodeStatus.RELEASED);
            clusterNodePlanInfo.setMonitorPlanList(null);

            return ErrorCode.SUCCESS;
        }
    
        Cls_Func<ClusterNodeInfo, ContainerPlan> udeployFunc = null;
    
        LOGGER.info("cluster id {}, node id {}, container {}, cur action:{}, next action:{}",
                clusterNodePlanInfo.getClusterId(), clusterNodePlanInfo.getClusterNodeInfo().getNodeId(), plan.getContainerName(), plan.getCurAction(), plan.getNextAction());
        if (clusterNodePlanInfo.getNodeStatus().equals(ClusterNodeStatus.UDEPLOY_FAILED))
        {
            return ErrorCode.SUCCESS;
        }
    
        if (clusterNodePlanInfo.getNodeStatus().equals(ClusterNodeStatus.UPLANNED))
        {
            clusterNodePlanInfo.setNodeStatus(ClusterNodeStatus.UDEPLOYING);
        }
    
        clusterNodePlanInfo.setTipMessage(plan.getTipMessage());
    
        switch (plan.getNextAction())
        {
            case REMOVE_BEFORE:
                udeployFunc = removeContaierBf();
                break;
            case REMOVE_AFTER:
                udeployFunc = removeContaierAf();
                break;
            case CREATE:
                udeployFunc = createContainer();
                break;
            case LIST:
                udeployFunc = listOperator(plan);
                break;
            case NEW:
                clusterNodePlanInfo.popContainerUPlan();
                break;
            case LOG:
                udeployFunc = logOperator();
                break;
            case REMOVE_NODE_JKE:
                udeployFunc = removeAllJke();
                break;
            case LOG_CHECK:
                udeployFunc = checkOperator(plan, clusterNodePlanInfo.getClusterId());
                break;
            case ABNORMAL_QUIT:
                if (clusterNodePlanInfo.isForceDelete())
                {
                    clusterNodePlanInfo.setNodeStatus(ClusterNodeStatus.RELEASED);
                }
                else
                {
                    clusterNodePlanInfo.setNodeStatus(ClusterNodeStatus.UDEPLOY_FAILED);
                }
                return ErrorCode.CLUSTER_UDEPLOY_FAILED;
            default:
                break;
        }
        
    
        if (udeployFunc != null)
        {
            udeployFunc.operator(clusterNodePlanInfo.getClusterNodeInfo(), plan);
        }
    
        if (plan.getNextAction() == PlanAction.NEW)
        {
            clusterNodePlanInfo.popContainerUPlan();
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
    
    
    Cls_Func<ClusterNodeInfo, ContainerPlan> listOperator(ContainerPlan containerPlan)
    {
        if (containerPlan.getContainerName().contains(DeployContainerName.JKE_SIDEKICK))
        {
            return listOperatorEx();
        }
        
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
                    return ErrorCode.CLUSTER_DEPLOY_FAILED;
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
                case REMOVE_AFTER:
                    if (packetRet.getStatus() == InstanceState.REMOVED.getCode())
                    {
                        plan.setNextAction(PlanAction.NEW);
                    }
                    else
                    {
                        if (interVal > ACTION_OVERTIME)
                        {
                            plan.setErrorTime(plan.getErrorTime() + 1);
                            plan.setNextAction(REMOVE_AFTER);
                        }
                        else
                        {
                            LOGGER.info("try remove after again. {}", plan.getContainerName());
    
                            plan.setCurAction(REMOVE_AFTER);
                            plan.setNextAction(LIST);
                        }
                    }
                    break;
                case CREATE:
                    if (containerPlan.isCoreContainer() && packetRet.getStatus() == InstanceState.RUNNING.getCode())
                    {
                        if (StringUtils.isNotBlank(containerPlan.getLogSucess()) || StringUtils.isNotBlank(containerPlan.getLogFailed()))
                        {
                            plan.setNextAction(LOG);
                        }
                        else
                        {
                            plan.setNextAction(NEW);
                        }
                    }
                    else if (packetRet.getStatus() >= InstanceState.SUCCESS_QUIT.getCode())
                    {
                        //maybe send create cmd was not arrive, list inst can not get the result
                        if (packetRet.getStatus()  == InstanceState.REMOVED.getCode() || packetRet.getStatus()  == InstanceState.OBJECT_NOT_EXIST.getCode())
                        {
                            plan.setNextAction(CREATE);
                        }
                        else if (plan.getContainerName().contains(DeployContainerName.JKE_ADDON_DEPLOYER))
                        {
                            plan.setNextAction(LOG);
                        }
                        else if (StringUtils.isNotBlank(containerPlan.getLogSucess()) || StringUtils.isNotBlank(containerPlan.getLogFailed()))
                        {
                            plan.setNextAction(LOG);
                        }
                        else
                        {
                            plan.setNextAction(NEW);
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
                default:
                    if (packetRet.getStatus() == InstanceState.REMOVED.getCode())
                    {
                        plan.setNextAction(PlanAction.NEW);
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
    
    Cls_Func<ClusterNodeInfo, ContainerPlan>  listOperatorEx()
    {
        Cls_Func<ClusterNodeInfo, ContainerPlan> logFunction = (clusterNodeInfo, plan) ->
        {
            CombRetPacket packetRet = listContainer(clusterNodeInfo, plan.getContainerName());
            if (packetRet.getStatus() > InstanceState.SUCCESS_QUIT.getCode()
                    && packetRet.getStatus() < InstanceState.REMOVED.getCode())
            {
    
                if (plan.getErrorTime() > MaxErrorTime)
                {
                    plan.setNextAction(PlanAction.ABNORMAL_QUIT);
                    return ErrorCode.CLUSTER_DEPLOY_FAILED;
                }
                else
                {
                    plan.setNextAction(PlanAction.ABNORMAL_QUIT);
                    return ErrorCode.CLUSTER_DEPLOY_FAILED;
                }
            }
            
            plan.setPreAction(plan.getCurAction());
            plan.setCurAction(PlanAction.LIST);
    
            long curTime = System.currentTimeMillis();
            long interVal = curTime - plan.getActionTime();
    
            LOGGER.info("list ex inst {} rsp code:{}", plan.getContainerName(), packetRet.getStatus());
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
                            plan.setCurAction(REMOVE_BEFORE);
                            plan.setNextAction(LIST);
                        }
                    }
                    break;
                case CREATE:
                    if (packetRet.getStatus() == InstanceState.CREATED.getCode())
                    {
                        plan.setNextAction(PlanAction.NEW);
                    }
                    else if(packetRet.getStatus() < InstanceState.CREATED.getCode())
                    {
                        if (interVal > ACTION_OVERTIME)
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
                default:
                    LOGGER.error("unknow, {} pre action {}", plan.getContainerName(), plan.getPreAction());
                    plan.setErrorTime(plan.getErrorTime() + 1);
                    plan.setNextAction(REMOVE_BEFORE);
    
                    break;
            }
    
            return ErrorCode.SUCCESS;
        };
        return logFunction;
    }
    
    Cls_Func<ClusterNodeInfo, ContainerPlan>  logOperator()
    {
        Cls_Func<ClusterNodeInfo, ContainerPlan> logFunction = (clusterNodeInfo, plan) ->
        {
            LOGGER.info("action: log for plan {} on {}", plan.getContainerName(), clusterNodeInfo.getNodeId());
    
            plan.setPreAction(plan.getCurAction());
            int code = logContainer(clusterNodeInfo, plan.getContainerName());
            LOGGER.info("log inst {} rsp code:{}", plan.getContainerName(), code);
            if (code == InstanceState.REMOVED.getCode())
            {
                plan.setCurAction(PlanAction.LOG);
                plan.setNextAction(PlanAction.CREATE);
                return ErrorCode.INST_DROPPED;
            }
            plan.setCurAction(PlanAction.LOG);
            plan.setNextAction(PlanAction.LOG_CHECK);
            plan.setActionTime(System.currentTimeMillis());
            return ErrorCode.SUCCESS;
        };
        
        return logFunction;
    }
    
    Cls_Func<ClusterNodeInfo, ContainerPlan>  removeAllJke()
    {
        Cls_Func<ClusterNodeInfo, ContainerPlan> cleanJkeFunction = (clusterNodeInfo, plan) ->
        {
            LOGGER.info("action: clean jke for plan {} on {}", plan.getContainerName(), clusterNodeInfo.getNodeId());
        
            plan.setPreAction(plan.getCurAction());
            int code = cleanJke(clusterNodeInfo, plan.getOrchType());
            LOGGER.info("clean inst {} rsp code:{}", plan.getContainerName(), code);
            plan.setNextAction(NEW);
            return ErrorCode.SUCCESS;
        };
    
        return cleanJkeFunction;
    }
    
    Cls_Func<ClusterNodeInfo, ContainerPlan>  checkOperator(ContainerPlan containerPlan, String clusterId)
    {
        if (containerPlan.getContainerName().contains(DeployContainerName.JKE_ADDON_DEPLOYER))
        {
            return checkAddonLogOperator(containerPlan, clusterId);
        }
        
        Cls_Func<ClusterNodeInfo, ContainerPlan> logFunction = (clusterNodeInfo, plan) ->
        {
            LOGGER.info("action: log check for plan {} on {}", plan.getContainerName(), clusterNodeInfo.getNodeId());
    
            plan.setPreAction(plan.getCurAction());
            String log = getlogContainer(clusterNodeInfo, plan.getContainerName());

            LOGGER.info("container {} log :{}", plan.getContainerName(), log);

            if (CollectionUtils.hasContent(plan.getLogSucess()))
            {
                if (StringUtils.isEmpty(log))
                {
                    plan.setNextAction(LOG);
                    return ErrorCode.SUCCESS;
                }
                
                if (log.contains(plan.getLogSucess()))
                {
//                    plan.setNextAction(REMOVE_AFTER);
                    plan.setNextAction(NEW);
                }
                else
                {
                    if (plan.getErrorTime() > MaxErrorTime)
                    {
                        plan.setNextAction(PlanAction.ABNORMAL_QUIT);
                    }
                    
                    plan.setErrorTime(plan.getErrorTime() + 1);
                    plan.setNextAction(REMOVE_BEFORE);
                }
            }
            else if(CollectionUtils.hasContent(plan.getLogFailed()))
            {
                if (StringUtils.isEmpty(log))
                {
                    plan.setNextAction(LOG);
                    return ErrorCode.SUCCESS;
                }
                
                if (log.contains(plan.getLogFailed()))
                {
                    plan.setErrorTime(plan.getErrorTime() + 1);
                    if (plan.getErrorTime() > MaxErrorTime)
                    {
                        plan.setNextAction(PlanAction.ABNORMAL_QUIT);
                    }
                    else
                    {
                        plan.setNextAction(REMOVE_BEFORE);
                    }
                }
                else
                {
                    plan.setNextAction(PlanAction.NEW);
                }
            }
            else
            {
                plan.setNextAction(PlanAction.NEW);
            }

            return ErrorCode.SUCCESS;
        };
        
        return logFunction;
    }
    
    Cls_Func<ClusterNodeInfo, ContainerPlan>  checkAddonLogOperator(ContainerPlan containerPlan, String clusterId)
    {
        Cls_Func<ClusterNodeInfo, ContainerPlan> logFunction = (clusterNodeInfo, plan) ->
        {
            LOGGER.info("action: addon log check for plan {} on {}", plan.getContainerName(), clusterNodeInfo.getNodeId());

            ClusterInnerInfo clusterInnerInfo = clusterRepo.getClusterInnerInfo(clusterId);
            int systemAddonNum = SystemAddonNumMap.get(clusterInnerInfo.getClusterType());
            LOGGER.info("cluster id: {}, type: {}, SystemAddonNum {}", clusterId, clusterInnerInfo.getClusterType(), systemAddonNum);
            plan.setPreAction(plan.getCurAction());
            String log = getlogContainer(clusterNodeInfo, plan.getContainerName());
            LOGGER.info("log content:{}", log);
            if (StringUtils.isBlank(log))
            {
                plan.setNextAction(PlanAction.LOG_CHECK);
            }
    
            if (ClsUtils.checkLogResult(log, "completed", systemAddonNum))
            {
                plan.setNextAction(PlanAction.NEW);
                return ErrorCode.SUCCESS;
            }
            else
            {
                plan.setNextAction(PlanAction.ABNORMAL_QUIT);
                return ErrorCode.CLUSTER_K8S_LOG_CONTAIN_ERROR;
            }
        };
        
        return logFunction;
    }
    
    int logContainer(ClusterNodeInfo  clusterNodeInfo, String containerName)
    {
        TargetNode targetNode = new TargetNode(clusterNodeInfo.getRegionId(), clusterNodeInfo.getSiteId(), clusterNodeInfo.getNodeId());
        CombRetPacket combRetPacket = combRpcService.getInstService().logContainer(targetNode, containerName);
        return combRetPacket.getStatus();
    }
    
    int cleanJke(ClusterNodeInfo  clusterNodeInfo, String orchType)
    {
        TargetNode targetNode = new TargetNode(clusterNodeInfo.getRegionId(), clusterNodeInfo.getSiteId(), clusterNodeInfo.getNodeId());
        CombRetPacket combRetPacket = combRpcService.getInstService().cleanJkeContainer(targetNode, orchType);
        return combRetPacket.getStatus();
    }
    
    String getlogContainer(ClusterNodeInfo  clusterNodeInfo, String containerName)
    {
        TargetNode targetNode = new TargetNode(clusterNodeInfo.getRegionId(), clusterNodeInfo.getSiteId(), clusterNodeInfo.getNodeId());
        CombRetPacket combRetPacket = combRpcService.getInstService().getLogContainer(targetNode, containerName);
        if (combRetPacket.getStatus() == InstanceState.REMOVED.getCode())
        {
            return "";
        }
        return (String) combRetPacket.getObj();
    }
    
    CombRetPacket listContainer(ClusterNodeInfo  clusterNodeInfo, String containerName)
    {
        TargetNode targetNode = new TargetNode(clusterNodeInfo.getRegionId(), clusterNodeInfo.getSiteId(), clusterNodeInfo.getNodeId());
        CombRetPacket combRetPacket = combRpcService.getInstService().listContainer(targetNode, containerName);
        return combRetPacket;
    }
    
    Cls_Func<ClusterNodeInfo, ContainerPlan> removeContaierAf(ContainerPlan containerPlan)
    {
        if (containerPlan.isCoreContainer())
        {
            containerPlan.setNextAction(PlanAction.NEW);
            return null;
        }
        
        return removeContaierAf();
    }
    
    Cls_Func<ClusterNodeInfo, ContainerPlan> removeContaierAf()
    {
        Cls_Func<ClusterNodeInfo, ContainerPlan> removeFunction = (clusterNodeInfo, plan) ->
        {
            LOGGER.info("action: remove after container for plan {} on {}", plan.getContainerName(), clusterNodeInfo.getNodeId());
        
            plan.setPreAction(plan.getCurAction());
            plan.setCurAction(PlanAction.REMOVE_AFTER);
        
            TargetNode targetNode = new TargetNode(clusterNodeInfo.getRegionId(), clusterNodeInfo.getSiteId(), clusterNodeInfo.getNodeId());
            CombRetPacket combRetPacket = combRpcService.getInstService().removeContainer(targetNode, plan.getContainerName());
        
            if (combRetPacket.getStatus() == InstanceState.REMOVED.getCode())
            {
                plan.setNextAction(PlanAction.NEW);
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
}
