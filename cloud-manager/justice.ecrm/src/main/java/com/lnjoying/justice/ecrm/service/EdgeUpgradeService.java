package com.lnjoying.justice.ecrm.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.operationevent.model.BizModelStateInfo;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionTemplateFields;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.DeepCopyUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.commonweb.util.TemplateEngineUtils;
import com.lnjoying.justice.ecrm.common.Ecrm_Func;
import com.lnjoying.justice.ecrm.common.constant.*;
import com.lnjoying.justice.ecrm.db.model.TblEdgeComputeInfo;
import com.lnjoying.justice.ecrm.db.model.TblEdgeUpgradeInfo;
import com.lnjoying.justice.ecrm.db.repo.EdgeRepository;
import com.lnjoying.justice.ecrm.domain.UpgradePlan;
import com.lnjoying.justice.ecrm.handler.actiondescription.i18n.zh_cn.EdgeActionDescriptionTemplate;
import com.lnjoying.justice.ecrm.handler.resourcesupervisor.EdgeResourceSupervisor;
import com.lnjoying.justice.ecrm.handler.resourcesupervisor.statedict.EdgeResourceStateDesProvider;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import com.lnjoying.justice.schema.service.aos.AosService;
import com.micro.core.common.Pair;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("edgeUpgradeService")
public class EdgeUpgradeService
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CombRpcService combRpcService;

    @Autowired
    private EdgeRepository edgeRepository;

    @Autowired
    private EdgeResourceSupervisor edgeResourceSupervisor;

    final long KEEP_ALIVE_INTERVAL = 10000;
    final long ACTION_OVERTIME = 60000;
    final int INST_MAX_ERROR_TIME = 5;
    final int KEEP_ALIVE_MAX_ERROR_TIME = 30;

    private Set<String> notAutoRemoveContainers = new HashSet<>();

    EdgeUpgradeService()
    {
        notAutoRemoveContainers.add("side-kick-updater");
        notAutoRemoveContainers.add("side-kick-confirmer");
        notAutoRemoveContainers.add("side-kick-rollback");
    }

    public ErrorCode upgrade(TblEdgeUpgradeInfo tblEdgeUpgradeInfo)  throws WebSystemException
    {
        LOGGER.info("start upgrade edge: {}", tblEdgeUpgradeInfo.getNodeId());

        if (tblEdgeUpgradeInfo.getUpgradeStatus() >= UpgradeStatus.UPGRADE_FAILED)
        {
            RedisUtil.srem(RedisCache.UPGRADE_NODEID_SET, "", tblEdgeUpgradeInfo.getNodeId());
            return ErrorCode.SUCCESS;
        }

        List<UpgradePlan> upgradePlans;
        if (StringUtils.isEmpty(tblEdgeUpgradeInfo.getUpgradePlan()))
        {
            tblEdgeUpgradeInfo.setUpgradeStatus(UpgradeStatus.UPGRADE_FAILED);
            LOGGER.error("no plan for {}", tblEdgeUpgradeInfo.getNodeId());
            return ErrorCode.CLUSTER_DEPLOY_PLAN_NULL;
        } else
        {
            upgradePlans = JsonUtils.fromJson(tblEdgeUpgradeInfo.getUpgradePlan(), new TypeToken<List<UpgradePlan>>(){}.getType());
        }

        ErrorCode res = checkWetty(upgradePlans, tblEdgeUpgradeInfo);

        if (res != ErrorCode.SUCCESS)
        {
            return res;
        }

        UpgradePlan plan = getUpgradePlan(upgradePlans);

        if (plan == null)
        {
            tblEdgeUpgradeInfo.setUpgradeStatus(UpgradeStatus.UPGRADE_FAILED);
        }

        LOGGER.info("edge {}, current plan:{}", plan.getNodeId(), plan.getPlanName());



        switch (plan.getPlanName())
        {
            case UpgradePlanType.CREATE_WETTY:
                res = createWetty(plan, tblEdgeUpgradeInfo);
                break;
            case UpgradePlanType.UPGRADE:
            case UpgradePlanType.ROLLBACK:
                res = deployUpgradeContainer(plan, tblEdgeUpgradeInfo);
                break;
            case UpgradePlanType.AUTO_CONFIRM:
                res = autoConfirm(upgradePlans, tblEdgeUpgradeInfo);
                break;
            case UpgradePlanType.MANUAL_CONFIRM:
                res = manualConfirm(plan, tblEdgeUpgradeInfo);
                break;
            case UpgradePlanType.REMOVE_WETTY:
                res = removeWetty(plan, upgradePlans, tblEdgeUpgradeInfo);
                if (res == ErrorCode.SUCCESS)
                {
                    TblEdgeComputeInfo beforeUpdateEntity = edgeRepository.getEdgeNode(tblEdgeUpgradeInfo.getNodeId());
                    TblEdgeComputeInfo tblEdgeComputeInfo = new TblEdgeComputeInfo();
                    tblEdgeComputeInfo.setActiveStatus(ActiveStatus.ACITVE);
                    tblEdgeComputeInfo.setNodeId(tblEdgeUpgradeInfo.getNodeId());
                    edgeRepository.updateEdgeSelective(tblEdgeComputeInfo);
                    //记录资源更新事件
                    TblEdgeComputeInfo afterUpdateEntity = DeepCopyUtils.deepCopy(beforeUpdateEntity);
                    afterUpdateEntity.setActiveStatus(tblEdgeComputeInfo.getActiveStatus());
                    publishEdgeComputeInfoActiveStatusUpdateEvent(afterUpdateEntity, beforeUpdateEntity,"upgrade");
                }
                break;
            default:
                break;
        }
        if (res != ErrorCode.SUCCESS)
        {
            tblEdgeUpgradeInfo.setUpgradeStatus(UpgradeStatus.UPGRADE_FAILED);
        }
        tblEdgeUpgradeInfo.setUpdateTime(new Date());
        tblEdgeUpgradeInfo.setUpgradePlan(JsonUtils.toJson(upgradePlans));
        return res;
    }

    private void publishEdgeComputeInfoActiveStatusUpdateEvent(TblEdgeComputeInfo tblEdgeComputeInfo, TblEdgeComputeInfo beforeUpdateEntity, String action)
    {
        try
        {
            Map<Integer, BizModelStateInfo> activeStatusDesDict = EdgeResourceStateDesProvider.INSTANCE.getStateDesDict().get(EdgeResourceStateDesProvider.ACTIVE_STATUS_FIELD);
            BizModelStateInfo stateInfo = activeStatusDesDict.get(tblEdgeComputeInfo.getActiveStatus());
            edgeResourceSupervisor.publishUpdateEvent("节点激活状态更新", null, true,
                    beforeUpdateEntity, tblEdgeComputeInfo, action,
                    TemplateEngineUtils.render0(EdgeActionDescriptionTemplate.Descriptions.UPDATE_NODE_ACTIVE_STATUS,
                            false,
                            TemplateEngineUtils.newEntry("status", Optional.ofNullable(stateInfo)
                                    .map(x -> x.getCnDescription())
                                    .orElse(Optional.ofNullable(tblEdgeComputeInfo.getActiveStatus())
                                            .map(x -> x.toString())
                                            .orElse(""))),
                            TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
                                    tblEdgeComputeInfo.getNodeName())));
        } catch (Exception e)
        {
            LOGGER.error("发布节点激活状态更新事件失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }

    }

    private ErrorCode createWetty(UpgradePlan plan, TblEdgeUpgradeInfo tblEdgeUpgradeInfo)
    {
        Pair<AosService.TtyStackDeployStatus, String> createWetty = combRpcService.getAosService().addNodeStack(tblEdgeUpgradeInfo.getNodeId(),
                "tty", tblEdgeUpgradeInfo.getOperBp(), tblEdgeUpgradeInfo.getOperUser());

        UpgradePlan.StackPlan stackPlan = new UpgradePlan.StackPlan();
        plan.setStackPlan(stackPlan);

        if (null == createWetty || null == createWetty.getLeft() || createWetty.getLeft() == AosService.TtyStackDeployStatus.ERROR)
        {
            plan.setStatus(UpgradePlanStatus.ERROR);
            return ErrorCode.DEV_UPGRADE_FAILED;
        }
        else if (createWetty.getLeft() == AosService.TtyStackDeployStatus.DEPLOYED)
        {
            plan.setStatus(UpgradePlanStatus.KEEP_ALIVE);
            plan.getStackPlan().setDeployed(true);
            plan.getStackPlan().setStackId(createWetty.getRight());
            plan.getStackPlan().setTimestamp(new Date().getTime());
        }
        else if (createWetty.getLeft() == AosService.TtyStackDeployStatus.NEW)
        {
            plan.setStatus(UpgradePlanStatus.KEEP_ALIVE);
            plan.getStackPlan().setStackId(createWetty.getRight());
            plan.getStackPlan().setTimestamp(new Date().getTime());
        }

        return ErrorCode.SUCCESS;
    }


    private ErrorCode deployUpgradeContainer(UpgradePlan plan, TblEdgeUpgradeInfo tblEdgeUpgradeInfo)
    {
        Ecrm_Func<TblEdgeUpgradeInfo, UpgradePlan> upgradeFunc = null;

        LOGGER.info("container {}, cur action:{}, next action:{}", plan.getContainerPlan().getContainerName(),
                plan.getContainerPlan().getCurAction(), plan.getContainerPlan().getNextAction());

        if (plan.getStatus() == UpgradePlanStatus.WAITING)
        {
            plan.setStatus(UpgradePlanStatus.EXECUTING);
        }
        else if (plan.getStatus() != UpgradePlanStatus.EXECUTING)
        {
            return ErrorCode.SUCCESS;
        }

        switch (plan.getContainerPlan().getNextAction())
        {
            case REMOVE_BEFORE:
                upgradeFunc = removeContaierBf();
                break;
            case REMOVE_AFTER:
                upgradeFunc = removeContaierAf(plan.getContainerPlan());
                break;
            case CREATE:
                upgradeFunc = createContainer();
                break;
            case LIST:
                upgradeFunc = listOperator(plan.getContainerPlan());
                break;
            case LOG:
                upgradeFunc = logOperator();
                break;
            case LOG_CHECK:
                upgradeFunc = checkOperator();
                break;
            case NEW:
                plan.setStatus(UpgradePlanStatus.COMPLETE);
                break;
            case ABNORMAL_QUIT:
                tblEdgeUpgradeInfo.setUpgradeStatus(UpgradeStatus.UPGRADE_FAILED);
                return ErrorCode.DEV_UPGRADE_FAILED;
            default:
                break;
        }

        if (upgradeFunc != null)
        {
            upgradeFunc.operator(tblEdgeUpgradeInfo, plan);
        }

        if (plan.getContainerPlan().getNextAction() == PlanAction.NEW)
        {
            plan.setStatus(UpgradePlanStatus.COMPLETE);
        }

        return ErrorCode.SUCCESS;
    }

    Ecrm_Func<TblEdgeUpgradeInfo, UpgradePlan> removeContaierBf()
    {
        Ecrm_Func<TblEdgeUpgradeInfo, UpgradePlan> removeFunction = (tblEdgeUpgradeInfo, plan) ->
        {
            LOGGER.info("action: remove before for plan {} on {}", plan.getContainerPlan().getContainerName(), tblEdgeUpgradeInfo.getNodeId());
            plan.getContainerPlan().setPreAction(plan.getContainerPlan().getCurAction());
            plan.getContainerPlan().setCurAction(PlanAction.REMOVE_BEFORE);
            
            TargetNode targetNode = new TargetNode(tblEdgeUpgradeInfo.getRegionId(), tblEdgeUpgradeInfo.getSiteId(), tblEdgeUpgradeInfo.getNodeId());
            CombRetPacket combRetPacket = combRpcService.getInstService().removeContainer(targetNode, plan.getContainerPlan().getContainerName());
            
            if (combRetPacket.getStatus() == InstanceState.REMOVED.getCode())
            {
                plan.getContainerPlan().setNextAction(PlanAction.CREATE);
            }
            else
            {
                plan.getContainerPlan().setNextAction(PlanAction.LIST);
            }
            
            plan.getContainerPlan().setActionTime(System.currentTimeMillis());
            
            return ErrorCode.SUCCESS;
        };
        
        return removeFunction;
    }

    Ecrm_Func<TblEdgeUpgradeInfo, UpgradePlan> createContainer()
    {
        Ecrm_Func<TblEdgeUpgradeInfo, UpgradePlan> createFunction = (tblEdgeUpgradeInfo, plan) ->
        {
            LOGGER.info("action: create for plan {} on {}", plan.getContainerPlan().getContainerName(), tblEdgeUpgradeInfo.getNodeId());
    
            plan.getContainerPlan().setPreAction(plan.getContainerPlan().getCurAction());
            plan.getContainerPlan().setCurAction(PlanAction.CREATE);
            
            TargetNode targetNode = new TargetNode(tblEdgeUpgradeInfo.getRegionId(), tblEdgeUpgradeInfo.getSiteId(), tblEdgeUpgradeInfo.getNodeId());
            CombRetPacket combRetPacket = combRpcService.getInstService()
                    .createContainer(targetNode, JsonUtils.toJson(plan.getContainerPlan().getDockerInstParams()), plan.getContainerPlan().getContainerName(),
                            null, plan.getContainerPlan().getAutoRun(), "ecrm");
            
            LOGGER.info("create inst rsp code:{}", combRetPacket.getStatus());
            if (combRetPacket.getStatus() > InstanceState.MAKED.getCode())
            {
                plan.getContainerPlan().setNextAction(PlanAction.LIST);
            }
            
            plan.getContainerPlan().setActionTime(System.currentTimeMillis());
            return ErrorCode.SUCCESS;
        };
        
        return createFunction;
    }


    Ecrm_Func<TblEdgeUpgradeInfo, UpgradePlan> listOperator(UpgradePlan.ContainerPlan containerPlan)
    {
        Ecrm_Func<TblEdgeUpgradeInfo, UpgradePlan> listFunction = (tblEdgeUpgradeInfo, plan) ->
        {
            LOGGER.info("action: list for plan {} on {}", plan.getContainerPlan().getContainerName(), tblEdgeUpgradeInfo.getNodeId());
    
            plan.getContainerPlan().setPreAction(plan.getContainerPlan().getCurAction());
            plan.getContainerPlan().setCurAction(PlanAction.LIST);
            
            CombRetPacket packetRet = listContainer(tblEdgeUpgradeInfo, plan.getContainerPlan().getContainerName());
            if (packetRet.getStatus() > InstanceState.SUCCESS_QUIT.getCode()
                    && packetRet.getStatus() < InstanceState.REMOVED.getCode())
            {
                LOGGER.info("get error for {}, error status:{}", plan.getContainerPlan().getContainerName(), packetRet.getStatus());
                plan.getContainerPlan().setErrorTime(plan.getContainerPlan().getErrorTime() + 1);
                if (plan.getContainerPlan().getErrorTime() > INST_MAX_ERROR_TIME)
                {
                    plan.getContainerPlan().setNextAction(PlanAction.ABNORMAL_QUIT);
                    return ErrorCode.CLUSTER_DEPLOY_FAILED;
                }
                else
                {
                    plan.getContainerPlan().setNextAction(PlanAction.REMOVE_BEFORE);
                    return ErrorCode.SUCCESS;
                }
            }
            
            long curTime = System.currentTimeMillis();
            long interVal = curTime - plan.getContainerPlan().getActionTime();
            LOGGER.info("list {} inst rsp code:{}", plan.getContainerPlan().getContainerName(), packetRet.getStatus());
            switch (plan.getContainerPlan().getPreAction())
            {
                case REMOVE_BEFORE:
                    if (packetRet.getStatus() == InstanceState.REMOVED.getCode())
                    {
                        plan.getContainerPlan().setNextAction(PlanAction.CREATE);
                    }
                    else
                    {
                        if (interVal > ACTION_OVERTIME)
                        {
                            plan.getContainerPlan().setNextAction(PlanAction.REMOVE_BEFORE);
                        }
                        else
                        {
                            LOGGER.info("try remove before again. {}", plan.getContainerPlan().getContainerName());
    
                            plan.getContainerPlan().setCurAction(PlanAction.REMOVE_BEFORE);
                            plan.getContainerPlan().setNextAction(PlanAction.LIST);
                        }
                    }
                    break;
                case REMOVE_AFTER:
                    if (packetRet.getStatus() == InstanceState.REMOVED.getCode())
                    {
                        plan.getContainerPlan().setNextAction(PlanAction.NEW);
                    }
                    else
                    {
                        if (interVal > ACTION_OVERTIME)
                        {
                            plan.getContainerPlan().setNextAction(PlanAction.REMOVE_AFTER);
                        }
                        else
                        {
                            LOGGER.info("try remove after again. {}", plan.getContainerPlan().getContainerName());
    
                            plan.getContainerPlan().setCurAction(PlanAction.REMOVE_AFTER);
                            plan.getContainerPlan().setNextAction(PlanAction.LIST);
                        }
                    }
                    break;
                case CREATE:
                    if (notAutoRemoveContainers.contains(containerPlan.getContainerName()) && packetRet.getStatus() == InstanceState.RUNNING.getCode())
                    {
                        plan.getContainerPlan().setNextAction(PlanAction.LOG);
                    }
                    else if (packetRet.getStatus() >= InstanceState.SUCCESS_QUIT.getCode())
                    {
                        plan.getContainerPlan().setNextAction(PlanAction.LOG);
                    }
                    else if(packetRet.getStatus() >= InstanceState.CREATED.getCode())
                    {
                        plan.getContainerPlan().setCurAction(PlanAction.CREATE);
                        plan.getContainerPlan().setNextAction(PlanAction.LIST);
                    }
                    else
                    {
                        if (interVal > ACTION_OVERTIME)
                        {
                            plan.getContainerPlan().setNextAction(PlanAction.CREATE);
                        }
                        else
                        {
                            plan.getContainerPlan().setCurAction(PlanAction.CREATE);
                            plan.getContainerPlan().setNextAction(PlanAction.LIST);
                        }
                    }
                    break;
                default:
                    if (packetRet.getStatus() == InstanceState.REMOVED.getCode())
                    {
                        plan.getContainerPlan().setNextAction(PlanAction.NEW);
                    }
                    LOGGER.error("unknown status, {} pre action {}", plan.getContainerPlan().getContainerName(), plan.getContainerPlan().getPreAction());
                    break;
            }
            
            return ErrorCode.SUCCESS;
        };
        
        return listFunction;
    }

    Ecrm_Func<TblEdgeUpgradeInfo, UpgradePlan> logOperator()
    {
        Ecrm_Func<TblEdgeUpgradeInfo, UpgradePlan> logFunction = (tblEdgeUpgradeInfo, plan) ->
        {
            LOGGER.info("action: log for plan {} on {}", plan.getContainerPlan().getContainerName(), tblEdgeUpgradeInfo.getNodeId());
    
            plan.getContainerPlan().setPreAction(plan.getContainerPlan().getCurAction());
            int code = logContainer(tblEdgeUpgradeInfo, plan.getContainerPlan().getContainerName());
            LOGGER.info("log inst {} rsp code:{}", plan.getContainerPlan().getContainerName(), code);
            if (code == InstanceState.REMOVED.getCode())
            {
                return ErrorCode.INST_DROPPED;
            }
            plan.getContainerPlan().setCurAction(PlanAction.LOG);
            plan.getContainerPlan().setNextAction(PlanAction.LOG_CHECK);
            plan.getContainerPlan().setActionTime(System.currentTimeMillis());
            return ErrorCode.SUCCESS;
        };
        
        return logFunction;
    }


    Ecrm_Func<TblEdgeUpgradeInfo, UpgradePlan>  checkOperator()
    {
        Ecrm_Func<TblEdgeUpgradeInfo, UpgradePlan> logFunction = (tblEdgeUpgradeInfo, plan) ->
        {
            LOGGER.info("action: log check for plan {} on {}", plan.getContainerPlan().getContainerName(), tblEdgeUpgradeInfo.getNodeId());
    
            plan.getContainerPlan().setPreAction(plan.getContainerPlan().getCurAction());
            String log = getlogContainer(tblEdgeUpgradeInfo, plan.getContainerPlan().getContainerName());

            plan.setLog(log);

            LOGGER.info("container {} log :{}", plan.getContainerPlan().getContainerName(), log);

            if (CollectionUtils.hasContent(plan.getContainerPlan().getLogSucess()))
            {
                if (StringUtils.isEmpty(log))
                {
                    plan.getContainerPlan().setNextAction(PlanAction.LOG);
                    return ErrorCode.SUCCESS;
                }
                
                if (log.contains(plan.getContainerPlan().getLogSucess()))
                {
                    plan.getContainerPlan().setNextAction(PlanAction.REMOVE_AFTER);
                }
            }
            else if(CollectionUtils.hasContent(plan.getContainerPlan().getLogFailed()))
            {
                if (StringUtils.isEmpty(log))
                {
                    plan.getContainerPlan().setNextAction(PlanAction.LOG);
                    return ErrorCode.SUCCESS;
                }
                
                if (log.contains(plan.getContainerPlan().getLogFailed()))
                {
                    plan.getContainerPlan().setErrorTime(plan.getContainerPlan().getErrorTime() + 1);
                    if (plan.getContainerPlan().getErrorTime() > INST_MAX_ERROR_TIME)
                    {
                        plan.getContainerPlan().setNextAction(PlanAction.ABNORMAL_QUIT);
                    }
                    else
                    {
                        plan.getContainerPlan().setNextAction(PlanAction.REMOVE_BEFORE);
                    }
                }
                else
                {
                    plan.getContainerPlan().setNextAction(PlanAction.NEW);
                }
            }
            else
            {
                plan.getContainerPlan().setNextAction(PlanAction.NEW);
            }

            return ErrorCode.SUCCESS;
        };
        
        return logFunction;
    }
    
    int logContainer(TblEdgeUpgradeInfo tblEdgeUpgradeInfo, String containerName)
    {
        TargetNode targetNode = new TargetNode(tblEdgeUpgradeInfo.getRegionId(), tblEdgeUpgradeInfo.getSiteId(), tblEdgeUpgradeInfo.getNodeId());
        CombRetPacket combRetPacket = combRpcService.getInstService().logContainer(targetNode, containerName);
        return combRetPacket.getStatus();
    }
    
    String getlogContainer(TblEdgeUpgradeInfo tblEdgeUpgradeInfo, String containerName)
    {
        TargetNode targetNode = new TargetNode(tblEdgeUpgradeInfo.getRegionId(), tblEdgeUpgradeInfo.getSiteId(), tblEdgeUpgradeInfo.getNodeId());
        CombRetPacket combRetPacket = combRpcService.getInstService().getLogContainer(targetNode, containerName);
        if (combRetPacket.getStatus() == InstanceState.REMOVED.getCode())
        {
            return "";
        }
        return (String) combRetPacket.getObj();
    }
    
    CombRetPacket listContainer(TblEdgeUpgradeInfo tblEdgeUpgradeInfo, String containerName)
    {
        TargetNode targetNode = new TargetNode(tblEdgeUpgradeInfo.getRegionId(), tblEdgeUpgradeInfo.getSiteId(), tblEdgeUpgradeInfo.getNodeId());
        CombRetPacket combRetPacket = combRpcService.getInstService().listContainer(targetNode, containerName);
        return combRetPacket;
    }

    Ecrm_Func<TblEdgeUpgradeInfo, UpgradePlan> removeContaierAf(UpgradePlan.ContainerPlan containerPlan)
    {
        if (notAutoRemoveContainers.contains(containerPlan.getContainerName()))
        {
            containerPlan.setNextAction(PlanAction.NEW);
            return null;
        }
        
        return removeContaierAf();
    }

    Ecrm_Func<TblEdgeUpgradeInfo, UpgradePlan> removeContaierAf()
    {
        Ecrm_Func<TblEdgeUpgradeInfo, UpgradePlan> removeFunction = (tblEdgeUpgradeInfo, plan) ->
        {
            LOGGER.info("action: remove after container for plan {} on {}", plan.getContainerPlan().getContainerName(), tblEdgeUpgradeInfo.getNodeId());
        
            plan.getContainerPlan().setPreAction(plan.getContainerPlan().getCurAction());
            plan.getContainerPlan().setCurAction(PlanAction.REMOVE_AFTER);
        
            TargetNode targetNode = new TargetNode(tblEdgeUpgradeInfo.getRegionId(), tblEdgeUpgradeInfo.getSiteId(), tblEdgeUpgradeInfo.getNodeId());
            CombRetPacket combRetPacket = combRpcService.getInstService().removeContainer(targetNode, plan.getContainerPlan().getContainerName());
        
            if (combRetPacket.getStatus() == InstanceState.REMOVED.getCode())
            {
                plan.getContainerPlan().setNextAction(PlanAction.NEW);
            }
            else
            {
                plan.getContainerPlan().setNextAction(PlanAction.LIST);
            }
            plan.getContainerPlan().setActionTime(System.currentTimeMillis());
        
            return ErrorCode.SUCCESS;
        };
    
        return removeFunction;
    }

    private ErrorCode checkWetty(List<UpgradePlan> upgradePlans, TblEdgeUpgradeInfo tblEdgeUpgradeInfo)
    {
        UpgradePlan plan = getKeepAliveUpgradePlan(upgradePlans);
        if (plan != null)
        {
            LOGGER.info("check wetty: {}", plan.getStackPlan());

            Long timestamp = new Date().getTime();
            if (StringUtils.isEmpty(plan.getStackPlan().getStackId()))
            {
                plan.setStatus(UpgradePlanStatus.ERROR);
                tblEdgeUpgradeInfo.setUpgradeStatus(UpgradeStatus.UPGRADE_FAILED);
                return ErrorCode.DEV_UPGRADE_FAILED;
            }
            else if (plan.getStackPlan().getTimestamp() + KEEP_ALIVE_INTERVAL < timestamp)
            {
                Pair<AosService.TtyStackDeployStatus, String> checkWetty = combRpcService.getAosService().getNodeStack(plan.getStackPlan().getStackId());

                LOGGER.info("check wetty: {} result: {}", plan.getStackPlan(), checkWetty);
                if (checkWetty.getLeft() == AosService.TtyStackDeployStatus.ERROR)
                {
                    plan.setStatus(UpgradePlanStatus.ERROR);
                    tblEdgeUpgradeInfo.setUpgradeStatus(UpgradeStatus.UPGRADE_FAILED);
                    return ErrorCode.DEV_UPGRADE_FAILED;
                }
                else if (checkWetty.getLeft() == AosService.TtyStackDeployStatus.DEPLOYED)
                {
                    plan.getStackPlan().setDeployed(true);
                    plan.getStackPlan().setErrorTime(0);
                }
                else if (!plan.getStackPlan().isDeployed())
                {
                    plan.getStackPlan().setErrorTime(plan.getStackPlan().getErrorTime() + 1);
                }
                else
                {
                    return null;
                }

                plan.getStackPlan().setTimestamp(timestamp);
            }
            else if (!plan.getStackPlan().isDeployed())
            {
                return null;
            }

            if (plan.getStackPlan().getErrorTime() > KEEP_ALIVE_MAX_ERROR_TIME)
            {
                plan.setStatus(UpgradePlanStatus.ERROR);
                tblEdgeUpgradeInfo.setUpgradeStatus(UpgradeStatus.UPGRADE_FAILED);
                return ErrorCode.DEV_UPGRADE_FAILED;
            }
        }
        return ErrorCode.SUCCESS;
    }

    private UpgradePlan getKeepAliveUpgradePlan(List<UpgradePlan> upgradePlans)
    {
        if (CollectionUtils.isEmpty(upgradePlans))
        {
            return null;
        }
        return upgradePlans.stream().filter(upgradePlan -> upgradePlan.getStatus() == UpgradePlanStatus.KEEP_ALIVE).findFirst().orElse(null);
    }

    private UpgradePlan getUpgradePlan(List<UpgradePlan> upgradePlans)
    {
        if (CollectionUtils.isEmpty(upgradePlans))
        {
            return null;
        }
        return upgradePlans.stream().filter(upgradePlan -> upgradePlan.getStatus() == UpgradePlanStatus.EXECUTING || upgradePlan.getStatus() == UpgradePlanStatus.WAITING).findFirst().orElse(null);
    }

    private ErrorCode autoConfirm(List<UpgradePlan> upgradePlans, TblEdgeUpgradeInfo tblEdgeUpgradeInfo)
    {
        upgradePlans.forEach(upgradePlan -> {
            if (upgradePlan.getPlanName().equals(UpgradePlanType.AUTO_CONFIRM))
            {
                upgradePlan.setStatus(UpgradePlanStatus.COMPLETE);
            }
            else if (upgradePlan.getPlanName().equals(UpgradePlanType.ROLLBACK))
            {
                upgradePlan.setStatus(UpgradePlanStatus.SKIP);
            }
        });
        tblEdgeUpgradeInfo.setUpgradeStatus(UpgradeStatus.AUTO_CONFIRM);
        return ErrorCode.SUCCESS;
    }

    private ErrorCode manualConfirm(UpgradePlan plan, TblEdgeUpgradeInfo tblEdgeUpgradeInfo)
    {
        plan.setStatus(UpgradePlanStatus.EXECUTING);
        tblEdgeUpgradeInfo.setUpgradeStatus(UpgradeStatus.MANUAL_CONFIRM);
        return ErrorCode.SUCCESS;
    }

    private ErrorCode removeWetty(UpgradePlan plan, List<UpgradePlan> upgradePlans ,TblEdgeUpgradeInfo tblEdgeUpgradeInfo)
    {
        UpgradePlan keepAlivePlan = getKeepAliveUpgradePlan(upgradePlans);
        if (keepAlivePlan != null)
        {
            combRpcService.getAosService().deleteNodeStack(keepAlivePlan.getStackPlan().getStackId());
        }

        plan.setStatus(UpgradePlanStatus.COMPLETE);
        tblEdgeUpgradeInfo.setUpgradeStatus(UpgradeStatus.UPGRADE_COMPLETED);
        return ErrorCode.SUCCESS;
    }
}
