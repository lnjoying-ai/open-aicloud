package com.lnjoying.justice.cluster.manager.service.process.processor;

import com.lnjoying.justice.cluster.manager.common.*;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterNodeInfo;
import com.lnjoying.justice.cluster.manager.db.repo.ClusterRepo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.cluster.manager.handler.actiondescription.i18n.zh_cn.ClusterInfoActionDescriptionTemplate;
import com.lnjoying.justice.cluster.manager.handler.resourcesupervisor.ClusterInfoResourceSupervisor;
import com.lnjoying.justice.cluster.manager.handler.resourcesupervisor.statedict.ClusterStatusDesProvider;
import com.lnjoying.justice.cluster.manager.service.cert.CertService;
import com.lnjoying.justice.cluster.manager.service.process.ClusterProcessStrategy;
import com.lnjoying.justice.cluster.manager.service.rpc.CombRpcService;
import com.lnjoying.justice.cluster.manager.util.K8sRedisField;
import com.lnjoying.justice.commonweb.handler.operationevent.model.BizModelStateInfo;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionTemplateFields;
import com.lnjoying.justice.commonweb.util.*;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.persistence.redis.RedisUtil;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @description scan cluster data in redis & db to processor
 * @author Regulus
 * @date 11/29/21 4:49 PM
 */
@Component
public class ScanDeployProcessor  extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    
    public ScanDeployProcessor()
    {
        LOGGER.info("init edge dev state check processor");
    }
    
    @Autowired
    private ClusterRepo clusterRepo;
    
    @Autowired
    private ClusterProcessStrategy clusterProcessStrategy;

    @Autowired
    private ClusterInfoResourceSupervisor clusterInfoResourceSupervisor;
    
    @Override
    public void start()
    {
        LOGGER.info("start scan deploy job");
    }
    
    @Override
    public void stop()
    {
        LOGGER.info("stop render task process");
    }
    
    @Autowired
    CertService certService;

    @Autowired
    private CombRpcService combRpcService;
    
    @Override
    public void run()
    {
        loadClusterToBuildPlan();
        loadNodePlanToDeploy();
        checkClusterReleased();
        checkClusterDeployed();
        loadOldUndeployCluster();
        loadNodeUnPlanToUDeploy();
        loadUnAssignedCluster();
        loadNodeMonitorPlanToMonitor();
    }
    
    /**
     * @description load cluster to build deploy plan on the assigned node
     * @author Regulus
     * @date 11/29/21 5:01 PM
     * @param  
     * @return void
     */
    void loadClusterToBuildPlan()
    {
        LOGGER.info("load cluster to build plan");
        try
        {
            if (clusterProcessStrategy.getTaskQueueLength(ProcessorName.PLAN) > 0)
            {
                LOGGER.info("msg queue is not empty for processor plan");
                return;
            }
    
            loadNeedPlanFromDB();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadClusterToBuildPlan error:{}", e.getMessage());
        }
        
    }
    /**
     * @description load node plan to deploy processor.
     * @author Regulus
     * @date 11/29/21 3:11 PM
     * @param
     * @return void
     */
    void loadNodePlanToDeploy()
    {
        LOGGER.info("load node plan to deploy");
        try
        {
            if (clusterProcessStrategy.getTaskQueueLength(ProcessorName.DEPLOY) > 0)
            {
                LOGGER.info("msg queue is not empty for processor deploy");
                return;
            }
            
            Set<String> deployingClusterIds = RedisUtil.smembers(K8sRedisField.DEPLOY_CLUSTERID_SET, "");
            if (CollectionUtils.isEmpty(deployingClusterIds))
            {
                 loadNodePlanFromDB();
                return;
            }
    
            for (String clusterId : deployingClusterIds)
            {
                ClusterInnerInfo clusterInnerInfo = clusterRepo.getClusterInnerInfo(clusterId);
                if (clusterInnerInfo.getStatus().equals(ClusterStatus.PLANNED))
                {
                    LOGGER.info("update cluster {} status from PLANNED to DEPLOYING", clusterId);
                    clusterInnerInfo.setStatus(ClusterStatus.DEPLOYING);
                    updateClusterInfo(clusterInnerInfo);
                }

                List<String> deployNodeListIds = RedisUtil.lrange(K8sRedisField.DEPLOY_CLUSTER_NODE_LIST, clusterId, 0, 100);
                if (CollectionUtils.isEmpty(deployNodeListIds))
                {
                    RedisUtil.srem(K8sRedisField.DEPLOY_CLUSTERID_SET, "", clusterId);
                    continue;
                }
        
                for (String nodeId : deployNodeListIds)
                {
    
                    ClusterNodePlanInfo clusterNodePlanInfo  = clusterRepo.getDeployNodePlanInfo(clusterId, nodeId);
                    if (clusterNodePlanInfo.getNodeStatus().getCode() > ClusterNodeStatus.DEPLOYING.getCode())
                    {
                        RedisUtil.lrem(K8sRedisField.DEPLOY_CLUSTER_NODE_LIST, clusterId, nodeId);
                        continue;
                    }
            
                    sendMessageToDeployProcessor(clusterNodePlanInfo, ClusterMsg.DEPLOY);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadNodePlanToDeploy error {}", e.getMessage());
        }
    }
    
    void loadNodeUnPlanToUDeploy()
    {
        LOGGER.info("load node undeploy plan to udeploy");
        try
        {
            if (clusterProcessStrategy.getTaskQueueLength(ProcessorName.DEPLOY) > 0)
            {
                LOGGER.info("msg queue is not empty for processor udeploy");
                return;
            }
            Set<String> undeployingClusterIds = RedisUtil.smembers(K8sRedisField.UNDEPLOY_CLUSTERID_SET, "");
            if (CollectionUtils.isEmpty(undeployingClusterIds))
            {
                loadNodeUnPlanFromDB();
                return;
            }
            
            for (String clusterId : undeployingClusterIds)
            {
                List<String> undeployNodeListIds = RedisUtil.lrange(K8sRedisField.UNDEPLOY_CLUSTER_NODE_LIST, clusterId, 0, 100);
                if (CollectionUtils.isEmpty(undeployNodeListIds))
                {
                    RedisUtil.srem(K8sRedisField.UNDEPLOY_CLUSTERID_SET, "", clusterId);
                    continue;
                }
                
                for (String nodeId : undeployNodeListIds)
                {
                    String key = String.format(K8sRedisField.UNDEPLOY_CLUSTER_NODE, clusterId, nodeId);
                    Object planObj = RedisUtil.oget(key, "");
                    if (planObj == null)
                    {
                        RedisUtil.lrem(K8sRedisField.UNDEPLOY_CLUSTER_NODE_LIST, clusterId, nodeId);
                        continue;
                    }
                    
                    ClusterNodePlanInfo clusterNodePlanInfo = (ClusterNodePlanInfo) planObj;
                    if (clusterNodePlanInfo.getNodeStatus().getCode() > ClusterNodeStatus.UDEPLOYING.getCode())
                    {
                        RedisUtil.lrem(K8sRedisField.UNDEPLOY_CLUSTER_NODE_LIST, clusterId, nodeId);
                        continue;
                    }
                    
                    sendMessageToDeployProcessor(clusterNodePlanInfo, ClusterMsg.UNDEPLOY);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadNodePlanToDeploy error {}", e.getMessage());
        }
    }
    /**
     * @description send message to deploy processor
     * @author Regulus
     * @date 11/29/21 3:08 PM
     * @param clusterNodePlanInfo 
     * @return void
     */
    void sendMessageToDeployProcessor(ClusterNodePlanInfo clusterNodePlanInfo, String type)
    {
        MessagePack messagePack = new MessagePack();
        messagePack.setMessageObj(clusterNodePlanInfo);
        messagePack.setMsgType(type);
        clusterProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOY);
    }
    
    void sendMessageToPlanProcessor(ClusterInnerInfo clusterInnerInfo, String msgType)
    {
        MessagePack messagePack = new MessagePack();
        messagePack.setMessageObj(clusterInnerInfo);
        messagePack.setMsgType(msgType);
        clusterProcessStrategy.sendMessage(messagePack, ProcessorName.PLAN);
    }
    
    void sendMessageToClusterProcessor(Object cluster, String type)
    {
        MessagePack messagePack = new MessagePack();
        messagePack.setMessageObj(cluster);
        messagePack.setMsgType(type);
        clusterProcessStrategy.sendMessage(messagePack, ProcessorName.CLUSTER);
    }
    
    /**
     * @description load deploying node plan or planned node plan to deploying.
     * @author Regulus
     * @date 11/29/21 4:59 PM
     * @param
     * @return void
     */
    void loadNodePlanFromDB()
    {
        List<ClusterNodePlanInfo> clusterNodePlanInfos = clusterRepo.getDeployingClusterNodeInfo();
        if (CollectionUtils.isEmpty(clusterNodePlanInfos))
        {
            clusterNodePlanInfos = clusterRepo.getPlannedClusterNodeInfo();
            if (CollectionUtils.isEmpty(clusterNodePlanInfos))
            {
                return;
            }
        }
        
        for (ClusterNodePlanInfo clusterNodePlanInfo : clusterNodePlanInfos)
        {
            sendMessageToDeployProcessor(clusterNodePlanInfo, ClusterMsg.DEPLOY);
        }
    }
    
    void loadNodeUnPlanFromDB()
    {
        List<ClusterNodePlanInfo> clusterNodeUPlanInfos = clusterRepo.getUnDeployingClusterNodeInfo();
        if (CollectionUtils.isEmpty(clusterNodeUPlanInfos))
        {
            clusterNodeUPlanInfos = clusterRepo.getUnPlannedClusterNodeInfo();
            if (CollectionUtils.isEmpty(clusterNodeUPlanInfos))
            {
                return;
            }
        }
        
        for (ClusterNodePlanInfo clusterNodePlanInfo : clusterNodeUPlanInfos)
        {
            sendMessageToDeployProcessor(clusterNodePlanInfo, ClusterMsg.UNDEPLOY);
        }
    }
    
    /**
     * @description load assigned & planing cluster to build deploy plan from db
     * @author Regulus
     * @date 11/29/21 4:59 PM
     * @param
     * @return void
     */
    void loadNeedPlanFromDB()
    {
        Data_Func<Integer, ErrorCode> dataFunc = status ->
        {
            List<TblClusterInfo> clusterInfos = clusterRepo.getClusterInfosToPlan(status);
            if (! CollectionUtils.isEmpty(clusterInfos))
            {
                for (TblClusterInfo tblClusterInfo : clusterInfos)
                {
                    TblClusterInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblClusterInfo);
                    ClusterInnerInfo clusterInnerInfo = clusterRepo.getClusterInnerInfo(tblClusterInfo.getId());
                    if (! CollectionUtils.isEmpty(clusterInnerInfo.getClusterNodePlanMap()))
                    {
                        if (CollectionUtils.isEmpty(clusterInnerInfo.getCertMap()))
                        {
                            certService.genClusterCerts(clusterInnerInfo, false);
                        }
                        
                        if (status == ClusterStatus.ASSIGNED.getCode())
                        {
                            LOGGER.info("send cluster {} to prebuild before plan", clusterInnerInfo.getClusterId());
                            sendMessageToClusterProcessor(clusterInnerInfo, ClusterMsg.PREPARE_BUILD);
                        }
                        else
                        {
                            LOGGER.info("send cluster {} to build plan", clusterInnerInfo.getClusterId());
                            sendMessageToPlanProcessor(clusterInnerInfo, ClusterMsg.BUILD_DEPLOY_PLAN);
                        }
                    }
                    else
                    {
                        tblClusterInfo.setStatus(ClusterStatus.ASSIGNING.getCode());
                        clusterRepo.updateCluster(tblClusterInfo);
                        //记录资源更新事件
                        publishClusterInfoUpdateEvent(tblClusterInfo, beforeUpdateEntity, "loadNeedPlanFromDB");
                    }
                }
            }
            return ErrorCode.SUCCESS;
        };
        
        dataFunc.operator(ClusterStatus.ASSIGNED.getCode());
        dataFunc.operator(ClusterStatus.PLANNING.getCode());
    }

    public void updateClusterInfo(ClusterInnerInfo clusterInnerInfo)
    {
        TblClusterInfo tblClusterInfoSelective = new TblClusterInfo();
        tblClusterInfoSelective.setId(clusterInnerInfo.getClusterId());
        tblClusterInfoSelective.setStatus(clusterInnerInfo.getStatus().getCode());
        tblClusterInfoSelective.setServiceState(clusterInnerInfo.getServiceStatus());
        tblClusterInfoSelective.setUpdateTime(new Date());
        if (clusterInnerInfo.getClusterType().equals(ClusterType.K8S))
        {
            tblClusterInfoSelective.setClusterEngineConfig(JsonUtils.toJson(clusterInnerInfo.getJkeConfig()));
        }
        else
        {
            tblClusterInfoSelective.setClusterEngineConfig(JsonUtils.toJson(clusterInnerInfo.getK3sConfig()));
        }

        TblClusterInfo tblClusterInfo = clusterRepo.getCluster(clusterInnerInfo.getClusterId());
        TblClusterInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblClusterInfo);
        clusterRepo.updateCluster(tblClusterInfoSelective);
        BeanUtils.copyNonNullProperties(tblClusterInfoSelective, tblClusterInfo);
        //记录资源更新事件
        publishClusterInfoUpdateEvent(tblClusterInfo, beforeUpdateEntity, "updateClusterInfo");

        if (clusterInnerInfo.getStatus().getCode() == ClusterStatus.REMOVED.getCode())
        {
            RedisUtil.odel(K8sRedisField.K8S_CLUSTER_INNER_INFO, clusterInnerInfo.getClusterId());
        }
        else
        {
            RedisUtil.oset(K8sRedisField.K8S_CLUSTER_INNER_INFO, clusterInnerInfo.getClusterId(), clusterInnerInfo);
        }
        return;
    }

    private void publishClusterInfoUpdateEvent(TblClusterInfo tblClusterInfo, TblClusterInfo beforeUpdateEntity, String action)
    {
        try
        {
            if (tblClusterInfo == null || beforeUpdateEntity == null)
            {
                LOGGER.debug("tblClusterInfo或beforeUpdateEntity为空，跳过发布集群信息更新事件!");
                return;
            }

            if (!Objects.equals(tblClusterInfo.getStatus(), beforeUpdateEntity.getStatus()))
            {
                //集群状态更新
                publishClusterInfoStatusUpdateEvent(tblClusterInfo, beforeUpdateEntity, action);
                return;
            }

            if (!Objects.equals(tblClusterInfo.getServiceState(), beforeUpdateEntity.getServiceState()))
            {
                //集群服务状态更新
                publishClusterInfoServiceStatusUpdateEvent(tblClusterInfo, beforeUpdateEntity, action);
                return;
            }

            clusterInfoResourceSupervisor.publishUpdateEvent("集群信息更新", beforeUpdateEntity.getName(),
                    false, beforeUpdateEntity, tblClusterInfo, action,

                    TemplateEngineUtils.render0(ClusterInfoActionDescriptionTemplate.Descriptions.UPDATE_CLUSTER,
                            false,
                            TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
                                    beforeUpdateEntity.getName())));
        } catch (Exception e)
        {
            LOGGER.error("发布集群信息更新事件失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    private void publishClusterInfoStatusUpdateEvent(TblClusterInfo tblClusterInfo, TblClusterInfo beforeUpdateEntity, String action)
    {
        try
        {
            Map<Integer, BizModelStateInfo> clusterInfoStatusDesDict = ClusterStatusDesProvider.INSTANCE.getStateDesDict().get(ClusterStatusDesProvider.STATUS_FIELD);
            BizModelStateInfo stateInfo = clusterInfoStatusDesDict.get(tblClusterInfo.getStatus());
            clusterInfoResourceSupervisor.publishUpdateEvent("集群状态更新",
                    tblClusterInfo.getName(), false,
                    beforeUpdateEntity, tblClusterInfo, action,
                    TemplateEngineUtils.render0(ClusterInfoActionDescriptionTemplate.Descriptions.UPDATE_CLUSTER_STATUS,
                            false,
                            TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME, tblClusterInfo.getName()),
                            TemplateEngineUtils.newEntry("status", Optional.ofNullable(stateInfo)
                                    .map(x -> x.getCnDescription())
                                    .orElse(Optional.ofNullable(tblClusterInfo.getStatus())
                                            .map(x -> x.toString())
                                            .orElse("")))
                    ));
        } catch (Exception e)
        {
            LOGGER.error("发布集群状态更新事件失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    private void publishClusterInfoServiceStatusUpdateEvent(TblClusterInfo tblClusterInfo, TblClusterInfo beforeUpdateEntity, String action)
    {
        try
        {
            Map<Integer, BizModelStateInfo> clusterInfoServiceStateDesDict = ClusterStatusDesProvider.INSTANCE.getStateDesDict().get(ClusterStatusDesProvider.SERVICE_STATE_FIELD);
            BizModelStateInfo stateInfo = clusterInfoServiceStateDesDict.get(tblClusterInfo.getStatus());
            clusterInfoResourceSupervisor.publishUpdateEvent("集群服务状态更新",
                    tblClusterInfo.getName(), false,
                    beforeUpdateEntity, tblClusterInfo, action,
                    TemplateEngineUtils.render0(ClusterInfoActionDescriptionTemplate.Descriptions.UPDATE_CLUSTER_SERVICE_STATUS,
                            false,
                            TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME, tblClusterInfo.getName()),
                            TemplateEngineUtils.newEntry("status", Optional.ofNullable(stateInfo)
                                    .map(x -> x.getCnDescription())
                                    .orElse(Optional.ofNullable(tblClusterInfo.getStatus())
                                            .map(x -> x.toString())
                                            .orElse("")))
                    ));
        } catch (Exception e)
        {
            LOGGER.error("发布集群服务状态更新事件失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }
    
    void checkClusterReleased()
    {
        LOGGER.info("check cluster which will be released");
        try
        {
            Set<String> clusterIds = RedisUtil.smembers(K8sRedisField.UNDEPLOY_CLUSTERID_SET, "");
    
            clusterIds = clusterRepo.getClusterIdsByCondition(ClusterStatus.REMOVING.getCode(), clusterIds);
            if (CollectionUtils.isEmpty(clusterIds))
            {
                return;
            }
    
            clusterIds.forEach(clsId -> {
                long count = clusterRepo.countUnDeployingClusterNode(clsId);
                if (count < 1)
                {
                    TblClusterInfo tblClusterInfoSelective = new TblClusterInfo();
                    tblClusterInfoSelective.setId(clsId);
    
                    long failedNum = clusterRepo.countUnDeployFailedClusterNode(clsId);
                    if (failedNum < 1)
                    {
                        tblClusterInfoSelective.setStatus(ClusterStatus.REMOVED.getCode());
                        
                    }
                    else
                    {
                        tblClusterInfoSelective.setStatus(ClusterStatus.REMOVE_FAILED.getCode());
                    }
    
                    RedisUtil.srem(K8sRedisField.UNDEPLOY_CLUSTERID_SET, "", clsId);
                    RedisUtil.odel(K8sRedisField.K8S_CLUSTER_INNER_INFO, clsId);
                    // remove reources
                    List<TblClusterNodeInfo> clusterNodeInfoList = clusterRepo.getClusterNodeInfoList(clsId);
                    if (!CollectionUtils.isEmpty(clusterNodeInfoList))
                    {
                        clusterNodeInfoList.stream().forEach(tblClusterNodeInfo -> {
                            RedisUtil.lrem(K8sRedisField.UNDEPLOY_CLUSTER_NODE_LIST, clsId, tblClusterNodeInfo.getNodeId());
                            combRpcService.getSchedulerService().releaseBindResources(tblClusterNodeInfo.getNodeId(), clsId);
                        });
                    }
                    tblClusterInfoSelective.setUpdateTime(new Date());
                    tblClusterInfoSelective.setName(clsId + clusterRepo.getClusterInnerInfo(clsId).getClusterName());

                    TblClusterInfo tblClusterInfo = clusterRepo.getCluster(tblClusterInfoSelective.getId());
                    TblClusterInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblClusterInfo);
                    clusterRepo.updateCluster(tblClusterInfoSelective);
                    BeanUtils.copyNonNullProperties(tblClusterInfoSelective, tblClusterInfo);
                    //记录新源更新事件
                    publishClusterInfoUpdateEvent(tblClusterInfo, beforeUpdateEntity, "checkClusterReleased");
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("checkClusterReleased error {}", e.getMessage());
        }
    }
    
    void checkClusterDeployed()
    {
        LOGGER.info("check cluster which will be deployed");
        try
        {
            Set<String> clusterIds = RedisUtil.smembers(K8sRedisField.DEPLOY_CLUSTERID_SET, "");
            
            clusterIds = clusterRepo.getClusterIdsByCondition(ClusterStatus.DEPLOYING.getCode(), clusterIds);
            if (CollectionUtils.isEmpty(clusterIds))
            {
                return;
            }
            
            clusterIds.forEach(clsId -> {
                long count = clusterRepo.countDeployingClusterNode(clsId);
                if (count < 1)
                {
                    ClusterInnerInfo clusterInfo = clusterRepo.getClusterInnerInfo(clsId);
                    if (clusterInfo.getStatus().getCode() >= ClusterStatus.DEPLOYED.getCode())
                    {
                        RedisUtil.srem(K8sRedisField.DEPLOY_CLUSTERID_SET, "", clsId);
                    }
                    else
                    {
                        
                        long countFailed = clusterRepo.countDeployFailedClusterNode(clsId);
                        if (countFailed >= 1)
                        {
                            if (clusterRepo.countDeployPartialSuccess(clsId))
                            {
                                clusterInfo.setStatus(ClusterStatus.DEPLOY_PARTIAL_SUCCESS);
                                RedisUtil.sadd(K8sRedisField.MONITOR_CLUSTERID_SET, "", clsId);
                            }
                            else
                            {
                                clusterInfo.setStatus(ClusterStatus.DEPLOY_FAILED);
                            }
                        }
                        else
                        {
                            if (clusterRepo.countClusterNode(clsId) > 0)
                            {
                                clusterInfo.setStatus(ClusterStatus.DEPLOYED);
                                RedisUtil.sadd(K8sRedisField.MONITOR_CLUSTERID_SET, "", clsId);
                            }
                            else
                            {
                                clusterInfo.setStatus(ClusterStatus.DEPLOY_FAILED);
                            }
                        }
                        
                        MessagePack messagePack = new MessagePack();
                        messagePack.setMessageObj(clusterInfo);
                        messagePack.setMsgType(ClusterMsg.UPDATE_CLUSTER);
                        clusterProcessStrategy.sendMessage(messagePack, ProcessorName.CLUSTER);
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("checkClusterDeployed error {}", e.getMessage());
        }
    }
    
    void loadOldUndeployCluster()
    {
        LOGGER.info("load old undeploy cluster to undeploy");
    
        try
        {
            List<String> clusterIds = clusterRepo.getClusterIdsByElapse("5 min", ClusterStatus.REMOVING.getCode());
            if (CollectionUtils.isEmpty(clusterIds))
            {
                return;
            }
    
            clusterIds.forEach(clusterId -> {
                ClusterInnerInfo clusterInnerInfo = clusterRepo.getClusterInnerInfo(clusterId);
                sendMessageToPlanProcessor(clusterInnerInfo, ClusterMsg.BUILD_UNDEPLOY_PLAN);
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadOldUndeployCluster error {}", e.getMessage());
        }
    }
    
    void loadUnAssignedCluster()
    {
        LOGGER.info("load unassigned cluster");
        try
        {
            if (clusterProcessStrategy.getTaskQueueLength(ProcessorName.CLUSTER) > 0)
            {
                LOGGER.info("msg queue is not empty for processor plan");
                return;
            }
    
            Data_Func<Integer, ErrorCode> dataFunc = status ->
            {
                List<TblClusterInfo> clusterInfos = clusterRepo.getClusterInfos(status, 3);
                if (! CollectionUtils.isEmpty(clusterInfos))
                {
                    for (TblClusterInfo tblClusterInfo : clusterInfos)
                    {
                        sendMessageToClusterProcessor(tblClusterInfo, ClusterMsg.ASSIGN);
                    }
                }
                return ErrorCode.SUCCESS;
            };
    
            dataFunc.operator(ClusterStatus.SPAWN.getCode());
            dataFunc.operator(ClusterStatus.ASSIGNING.getCode());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadUnAssignedCluster error {}", e.getMessage());
        }
    }

    void loadNodeMonitorPlanToMonitor()
    {
        LOGGER.info("load node monitor plan to monitor");
        try
        {
            if (clusterProcessStrategy.getTaskQueueLength(ProcessorName.MONITOR) > 0)
            {
                LOGGER.info("msg queue is not empty for processor monitor");
                return;
            }
            Set<String> monitorClusterIds = RedisUtil.smembers(K8sRedisField.MONITOR_CLUSTERID_SET, "");
            if (CollectionUtils.isEmpty(monitorClusterIds))
            {
                loadNeedMonitorClusterFromDB();
                return;
            }

            for (String clusterId : monitorClusterIds)
            {
                List<String> monitorNodeListIds = RedisUtil.lrange(K8sRedisField.MONITOR_CLUSTER_NODE_LIST, clusterId, 0, 100);
                if (CollectionUtils.isEmpty(monitorNodeListIds))
                {
                    RedisUtil.srem(K8sRedisField.MONITOR_CLUSTERID_SET, "", clusterId);
                    continue;
                }

                ClusterInnerInfo clusterInnerInfo = clusterRepo.getClusterInnerInfo(clusterId);
                if (clusterInnerInfo == null || (clusterInnerInfo.getStatus() != ClusterStatus.DEPLOYED && clusterInnerInfo.getStatus() != ClusterStatus.DEPLOY_PARTIAL_SUCCESS))
                {
                    RedisUtil.srem(K8sRedisField.MONITOR_CLUSTERID_SET, "", clusterId);
                    continue;
                }

                for (String nodeId : monitorNodeListIds)
                {
                    String key = String.format(K8sRedisField.MONITOR_CLUSTER_NODE, clusterId, nodeId);
                    Object planObj = RedisUtil.oget(key, "");
                    if (planObj == null)
                    {
                        RedisUtil.lrem(K8sRedisField.MONITOR_CLUSTER_NODE_LIST, clusterId, nodeId);
                        continue;
                    }

                    ClusterNodePlanInfo clusterNodePlanInfo = (ClusterNodePlanInfo) planObj;
                    if (clusterNodePlanInfo.getNodeStatus().getCode() > ClusterNodeStatus.UPLANNING.getCode())
                    {
                        RedisUtil.lrem(K8sRedisField.MONITOR_CLUSTER_NODE_LIST, clusterId, nodeId);
                        continue;
                    }

                    sendMessageToMonitorProcessor(clusterNodePlanInfo, ClusterMsg.MONITOR);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadNodePlanToDeploy error {}", e.getMessage());
        }
    }

    void sendMessageToMonitorProcessor(ClusterNodePlanInfo clusterNodePlanInfo, String type)
    {
        MessagePack messagePack = new MessagePack();
        messagePack.setMessageObj(clusterNodePlanInfo);
        messagePack.setMsgType(type);
        clusterProcessStrategy.sendMessage(messagePack, ProcessorName.MONITOR);
    }

    void loadNeedMonitorClusterFromDB()
    {
        LOGGER.info("load need monitor cluster from db");

        try
        {
            Set<String> clusterIds = clusterRepo.getClusterIdsByStatus(ClusterStatus.DEPLOYED.getCode());
            if (!CollectionUtils.isEmpty(clusterIds))
            {
                clusterIds.forEach(clusterId -> {
                    RedisUtil.sadd(K8sRedisField.MONITOR_CLUSTERID_SET, "", clusterId);
                });
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadNeedMonitorClusterFromDB error {}", e.getMessage());
        }
    }
}
