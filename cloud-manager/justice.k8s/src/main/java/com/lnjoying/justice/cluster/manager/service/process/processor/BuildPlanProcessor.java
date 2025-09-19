package com.lnjoying.justice.cluster.manager.service.process.processor;

import com.lnjoying.justice.cluster.manager.common.*;
import com.lnjoying.justice.cluster.manager.db.repo.ClusterRepo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodeInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.cluster.manager.service.plan.PlanService;
import com.lnjoying.justice.cluster.manager.service.plan.k3s.K3sSystemAddonPlanServiceImpl;
import com.lnjoying.justice.cluster.manager.service.plan.k8s.SystemAddonPlanServiceImpl;
import com.lnjoying.justice.cluster.manager.service.process.ClusterProcessStrategy;
import com.lnjoying.justice.cluster.manager.service.rpc.CombRpcService;
import com.lnjoying.justice.cluster.manager.util.K8sRedisField;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.lnjoying.justice.schema.constant.OnlineStatus;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.common.Pair;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.BlockingQueue;

import static com.lnjoying.justice.cluster.manager.common.ClusterNodeStatus.UDEPLOY_FAILED;
import static com.lnjoying.justice.cluster.manager.service.plan.k3s.AbstractK3sPlanService.KUBE_API_PORT;

@Component
public class BuildPlanProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private PlanService etcdPlanService;
    
    @Autowired
    private PlanService workerPlanService;
    
    @Autowired
    private PlanService controllerPlanService;
    
    @Autowired
    private ClusterProcessStrategy clusterProcessStrategy;

    @Autowired
    CombRpcService combRpcService;
    
    @Autowired
    private SystemAddonPlanServiceImpl systemAddonPlanService;

    @Autowired
    private K3sSystemAddonPlanServiceImpl k3sSystemAddonPlanService;

    @Autowired
    private ClusterRepo clusterRepo;

    @Autowired
    private PlanService k3sControllerPlanServiceImpl;

    @Autowired
    private PlanService k3sWorkerPlanServiceImpl;
    
    PlanService getPlanService(String roles)
    {
        if (roles.contains(K8sRole.CONTROLLER))
        {
            return controllerPlanService;
        }
        
        if (roles.contains(K8sRole.ETCD))
        {
            return etcdPlanService;
        }
        
        if (roles.equals(K8sRole.WORKER))
        {
            return workerPlanService;
        }
        
        return null;
    }

    public PlanService getK3sPlanService(String roles)
    {
        if (roles.contains(K3sRole.CONTROLLER))
        {
            return k3sControllerPlanServiceImpl;
        }

        if (roles.contains(K3sRole.WORKER))
        {
            return k3sWorkerPlanServiceImpl;
        }

        return null;
    }
    
    @Override
    public void start()
    {
        LOGGER.info("build plan message processor started");
    }
    
    @Override
    public void stop()
    {
        LOGGER.info("build plan message processor stopped");
    }
    
    @Override
    public void run()
    {
        BlockingQueue<Object> queue = getBlockQueue();
        while (true)
        {
            try
            {
                MessagePack pack = (MessagePack) queue.take();
                ClusterInnerInfo clusterInnerInfo = (ClusterInnerInfo) pack.getMessageObj();
                String clusterType = clusterInnerInfo.getClusterType();
                switch (pack.getMsgType())
                {
                    case ClusterMsg.BUILD_DEPLOY_PLAN:
                        if (ClusterType.K8S.equals(clusterType))
                        {
                            buildNodeDeployPlan(clusterInnerInfo);
                        }
                        else if (ClusterType.K3S.equals(clusterType))
                        {
                            buildK3sNodeDeployPlan(clusterInnerInfo);
                        }

                        break;
                    case ClusterMsg.BUILD_UNDEPLOY_PLAN:
                        if (ClusterType.K8S.equals(clusterType))
                        {
                            buildNodeUnDeployPlan(clusterInnerInfo);
                        }
                        else if (ClusterType.K3S.equals(clusterType))
                        {
                            buildK3sNodeUnDeployPlan(clusterInnerInfo);
                        }
                        break;
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                LOGGER.error("BuildPlanProcessor.run error {}", e.getMessage());
            }
            catch (WebSystemException e)
            {
                e.printStackTrace();
                LOGGER.error("BuildPlanProcessor.run error {}", e.getMessage());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                LOGGER.error("BuildPlanProcessor.run error {}", e.getMessage());
            }
        }
    }

    /**
     * use planservice to build deploy job, according to cluster roles.
     * @param clusterInnerInfo
     */
    void buildNodeDeployPlan(ClusterInnerInfo clusterInnerInfo) throws Exception
    {
        List<String> roleList = Arrays.asList(K8sRole.ETCD, K8sRole.CONTROLLER, K8sRole.WORKER);
        
        Boolean haveBuildSystemAddon = false;
        clusterInnerInfo.setStatus(ClusterStatus.PLANNING);
        DeploySituationHolder deploySituationHolder = recordTheNumberOfEachRole(clusterInnerInfo, roleList);
        ClusterStatus clsStatus = ClusterStatus.PLANNED;

        stopDeploy:
        for (String role : roleList)
        {
            List<ClusterNodePlanInfo> clusterNodePlanInfos = clusterInnerInfo.getClusterNodePlanInfos(role);
            if (CollectionUtils.isEmpty(clusterNodePlanInfos))
            {
                continue;
            }
            
            for (ClusterNodePlanInfo nodePlanInfo : clusterNodePlanInfos)
            {
                ClusterNodePlanInfo latestNode = clusterRepo.getDeployNodePlanInfo(nodePlanInfo.getClusterId(), nodePlanInfo.getClusterNodeInfo().getNodeId());
                if (latestNode.getNodeStatus().getCode() > nodePlanInfo.getNodeStatus().getCode())
                {
                    LOGGER.info("node {} plan has been build for cls {}", nodePlanInfo.getClusterNodeInfo().getNodeId(), nodePlanInfo.getClusterId());
                    continue;
                }
                
                if (nodePlanInfo.getNodeStatus().getCode() >= ClusterNodeStatus.PLANNED.getCode())
                {
                    if (nodePlanInfo.getNodeStatus().getCode() == ClusterNodeStatus.PLAN_FAILED.getCode())
                    {
                        deploySituationHolder.numberOfRoles.get(role).increaseFailNum();
                        if (!deploySituationHolder.checkContinueDeploy(role))
                        {
                            clsStatus = ClusterStatus.PLAN_FAILED;
                            LOGGER.error("deploy failed, deploySituation:{}", deploySituationHolder);
                            break stopDeploy;
                        }
                    }
                    else
                    {
                        deploySituationHolder.numberOfRoles.get(role).increaseSuccessNum();
                    }

                    continue;
                }
                
                nodePlanInfo.setNodeStatus(ClusterNodeStatus.PLANNING);
                clusterRepo.updateClusterNodePlanInfo(nodePlanInfo);

                String clusterRoles = nodePlanInfo.getClusterNodeInfo().getClusterRoles();
                PlanService planService = getPlanService(clusterRoles);
                if (planService == null)
                {
                    continue;
                }
                
                try
                {
                    int errorCode = planService.buildNodeDeployPlan(clusterInnerInfo, nodePlanInfo);
                    if (errorCode != ErrorCode.SUCCESS.getCode())
                    {
                        nodePlanInfo.setNodeStatus(ClusterNodeStatus.PLAN_FAILED);
                        clsStatus = ClusterStatus.PLAN_FAILED;
                        deploySituationHolder.increaseFailNum(clusterRoles);
                        LOGGER.error("deploy failed, deploySituation:{}", deploySituationHolder);
                        break;
                    }
    
                    if (nodePlanInfo.getClusterNodeInfo().getClusterRoles().contains(K8sRole.CONTROLLER))
                    {
                        //build system addon plan
                        if (haveBuildSystemAddon == false)
                        {
                            errorCode = systemAddonPlanService.buildNodeDeployPlan(clusterInnerInfo, nodePlanInfo);
                            if (errorCode != ErrorCode.SUCCESS.getCode())
                            {
                                nodePlanInfo.setNodeStatus(ClusterNodeStatus.PLAN_FAILED);
                                clsStatus = ClusterStatus.PLAN_FAILED;
                                deploySituationHolder.increaseFailNum(clusterRoles);
                                LOGGER.error("deploy failed, deploySituation:{}", deploySituationHolder);
                                break;
                            }

                            haveBuildSystemAddon = true;
                        }
                    }
                    
                    nodePlanInfo.setNodeStatus(ClusterNodeStatus.PLANNED);
                    deploySituationHolder.increaseSuccessNum(clusterRoles);
                }
                finally
                {
                    clusterRepo.updateClusterNodePlanInfo(nodePlanInfo);
                    if (!deploySituationHolder.checkContinueDeploy(role))
                    {
                        clsStatus = ClusterStatus.PLAN_FAILED;
                        LOGGER.error("deploy failed, deploySituation:{}", deploySituationHolder);
                        break stopDeploy;
                    }
                    sendMessageToDeployProcessor(clusterInnerInfo, nodePlanInfo);
                }
            }
        }

        if (ClusterStatus.PLANNED.equals(clsStatus))
        {
            RedisUtil.sadd(K8sRedisField.DEPLOY_CLUSTERID_SET, "", clusterInnerInfo.getClusterId());
        }

        clusterInnerInfo.setStatus(clsStatus);
        sendProcessor(clusterInnerInfo, ClusterMsg.UPDATE_CLUSTER, ProcessorName.CLUSTER);
//        clusterRepo.updateClusterInfo(clusterInnerInfo);
    }
    
    void sendProcessor(Object msg, String msgName, String processor)
    {
        MessagePack messagePack = new MessagePack();
        messagePack.setMessageObj(msg);
        messagePack.setMsgType(msgName);
        clusterProcessStrategy.sendMessage(messagePack, processor);
    }

    void buildNodeUnDeployPlan(ClusterInnerInfo clusterInnerInfo) throws Exception
    {
        LOGGER.info("begin build undeploy cluster plan. {}", clusterInnerInfo.getName());
        List<String> roleList = Arrays.asList(K8sRole.ETCD, K8sRole.CONTROLLER, K8sRole.WORKER);
        for (String role : roleList)
        {
            List<ClusterNodePlanInfo> clusterNodePlanInfos = clusterInnerInfo.getClusterNodePlanInfos(role);
            if (CollectionUtils.isEmpty(clusterNodePlanInfos))
            {
                continue;
            }
    
            for (ClusterNodePlanInfo nodePlanInfo : clusterNodePlanInfos)
            {
                LOGGER.info("cluster node {} status is:{}", nodePlanInfo.getClusterNodeInfo().getNodeId(), nodePlanInfo.getNodeStatus());

                Pair<Integer, Integer> nodeStatusPair = combRpcService.getEdgeResourceService().getNodeStatus(nodePlanInfo.getClusterNodeInfo().getNodeId());

                if (nodePlanInfo.isForceDelete() &&
                        (nodeStatusPair == null || nodeStatusPair.getLeft() == ActiveStatus.REMOVED || nodeStatusPair.getRight() == OnlineStatus.OFFLINE))
                {
                    nodePlanInfo.setNodeStatus(ClusterNodeStatus.RELEASED);
                    nodePlanInfo.setMonitorPlanList(null);
                    clusterRepo.updateClusterNodePlanInfo(nodePlanInfo);
                    continue;
                }

                //if undeploy failed can redo it
                if (nodePlanInfo.getNodeStatus().getCode() >= ClusterNodeStatus.UPLANNED.getCode()
                        && nodePlanInfo.getNodeStatus().getCode() != UDEPLOY_FAILED.getCode())
                {
                    continue;
                }

                if (nodePlanInfo.getNodeStatus().getCode() == UDEPLOY_FAILED.getCode())
                {
                    nodePlanInfo.getContainerUPlanList().clear();
                    nodePlanInfo.getContainerPlanList().clear();
                    nodePlanInfo.setNodeStatus(ClusterNodeStatus.UPLANNING);
                    clusterRepo.updateClusterNodeReUnDeployPlanInfo(nodePlanInfo);
                }
                else
                {
                    nodePlanInfo.setNodeStatus(ClusterNodeStatus.UPLANNING);
                    clusterRepo.updateClusterNodePlanInfo(nodePlanInfo);
                }

                PlanService planService = getPlanService(nodePlanInfo.getClusterNodeInfo().getClusterRoles());
                if (planService == null)
                {
                    continue;
                }

                try
                {
                    planService.buildNodeUnDeployPlan(clusterInnerInfo, nodePlanInfo);
                }
                catch (Exception e)
                {
                    LOGGER.error("build clusterId:{} node:{} node unDeploy plan error:{}", clusterInnerInfo.getClusterId(), nodePlanInfo.getClusterNodeInfo().getNodeId(), e);
                    nodePlanInfo.setNodeStatus(UDEPLOY_FAILED);
                    clusterRepo.updateClusterNodePlanInfo(nodePlanInfo);
                    break;
                }

                nodePlanInfo.setNodeStatus(ClusterNodeStatus.UPLANNED);
    
                clusterRepo.updateClusterNodePlanInfo(nodePlanInfo);
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(ClusterMsg.UNDEPLOY);
                messagePack.setMessageObj(nodePlanInfo);
                clusterProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOY);
                RedisUtil.lpush(K8sRedisField.UNDEPLOY_CLUSTER_NODE_LIST, clusterInnerInfo.getClusterId(), nodePlanInfo.getClusterNodeInfo().getNodeId());
            }
            RedisUtil.sadd(K8sRedisField.UNDEPLOY_CLUSTERID_SET, "", clusterInnerInfo.getClusterId());
//            clusterRepo.updateClusterInfo(clusterInnerInfo);
            sendProcessor(clusterInnerInfo, ClusterMsg.UPDATE_CLUSTER, ProcessorName.CLUSTER);
        }
    }


    private void buildK3sNodeDeployPlan(ClusterInnerInfo clusterInnerInfo)
    {
        String clusterName = clusterInnerInfo.getClusterName();
        clusterInnerInfo.setStatus(ClusterStatus.PLANNING);
        ClusterStatus clsStatus = ClusterStatus.PLANNED;
        List<ClusterNodePlanInfo> clusterControllerNodePlanInfos = clusterInnerInfo.getClusterNodePlanInfos(K3sRole.CONTROLLER);
        if (CollectionUtils.isEmpty(clusterControllerNodePlanInfos))
        {
            clsStatus = ClusterStatus.PLAN_FAILED;
        }

        List<ClusterNodePlanInfo> clusterWorkerNodePlanInfos = clusterInnerInfo.getClusterNodePlanInfos(K3sRole.WORKER);
        int controllerSize = clusterControllerNodePlanInfos.size();
        int workSize = clusterWorkerNodePlanInfos.size();

        if (controllerSize == 2)
        {
            LOGGER.info("You're creating 2 server nodes: Please consider creating at least 3 to achieve etcd quorum & fault tolerance");
        }

        boolean stopBuildDeployPlan = false;
        String connectUrl = "";
        String token = Utils.assignUUId();
        for (int i = 0; i < controllerSize; i++)
        {
            ClusterNodePlanInfo clusterNodePlanInfo = clusterControllerNodePlanInfos.get(i);
            ClusterNodePlanInfo.K3sContext k3sContext = clusterNodePlanInfo.getK3sContext();
            ClusterNodeInfo clusterNodeInfo = clusterNodePlanInfo.getClusterNodeInfo();
            String clusterRoles = clusterNodeInfo.getClusterRoles();
            // Multiple controller nodes, the first one is set to init node

            if (i == 0)
            {
                connectUrl = "https://" + clusterNodeInfo.getInternalAddress() + ":" + KUBE_API_PORT;
                k3sContext.setInit(true);

                if (clusterInnerInfo.getK3sConfig().getDataStoreConfig() != null)
                {
                    if (clusterInnerInfo.getK3sConfig().getDataStoreConfig().isEnableEtcd())
                    {
                        k3sContext.setDatastoreEndpoint("etcd");
                    }
                    else if (StringUtils.isNotEmpty(clusterInnerInfo.getK3sConfig().getDataStoreConfig().getDatastoreEndpoint()))
                    {
                        k3sContext.setDatastoreEndpoint(clusterInnerInfo.getK3sConfig().getDataStoreConfig().getDatastoreEndpoint());
                    }
                }
                if (clusterInnerInfo.getK3sConfig().getNetworkConfig() != null)
                {
                    if (StringUtils.isNotEmpty(clusterInnerInfo.getK3sConfig().getNetworkConfig().getClusterCidr()))
                    {
                        k3sContext.setClusterCidr(clusterInnerInfo.getK3sConfig().getNetworkConfig().getClusterCidr());
                    }
                    if (StringUtils.isNotEmpty(clusterInnerInfo.getK3sConfig().getNetworkConfig().getServiceCidr()))
                    {
                        k3sContext.setServiceCidr(clusterInnerInfo.getK3sConfig().getNetworkConfig().getServiceCidr());
                    }
                    if (StringUtils.isNotEmpty(clusterInnerInfo.getK3sConfig().getNetworkConfig().getServiceNodePortRange()))
                    {
                        k3sContext.setServiceNodePortRange(clusterInnerInfo.getK3sConfig().getNetworkConfig().getServiceNodePortRange());
                    }
                    if (StringUtils.isNotEmpty(clusterInnerInfo.getK3sConfig().getNetworkConfig().getClusterDns()))
                    {
                        k3sContext.setClusterDns(clusterInnerInfo.getK3sConfig().getNetworkConfig().getClusterDns());
                    }
                    if (StringUtils.isNotEmpty(clusterInnerInfo.getK3sConfig().getNetworkConfig().getClusterDomain()))
                    {
                        k3sContext.setClusterDomain(clusterInnerInfo.getK3sConfig().getNetworkConfig().getClusterDomain());
                    }
                }
            }
            else
            {
                k3sContext.setK3sUrl(connectUrl);
            }
            k3sContext.setRole(K3sRole.CONTROLLER);
            k3sContext.setNodeName(clusterName + "-" + K3sRole.CONTROLLER + "-" + String.valueOf(i));
            k3sContext.setK3sToken(token);
            if (!clusterRoles.contains(K3sRole.WORKER))
            {
                k3sContext.setDisableAgent(true);
            }

            // update node status
            clusterNodePlanInfo.setNodeStatus(ClusterNodeStatus.PLANNING);
            clusterRepo.updateClusterNodePlanInfo(clusterNodePlanInfo);

            // build deploy plan
            try
            {
                int errorCode = k3sControllerPlanServiceImpl.buildNodeDeployPlan(clusterInnerInfo, clusterNodePlanInfo);
                if (errorCode != ErrorCode.SUCCESS.getCode())
                {
                    clusterNodePlanInfo.setNodeStatus(ClusterNodeStatus.PLAN_FAILED);
                    clsStatus = ClusterStatus.PLAN_FAILED;
                    LOGGER.error("build k3s controller deploy plan failed, errorCode:{}", errorCode);
                    stopBuildDeployPlan = true;
                    break;
                }

                if (i == 0)
                {
                    errorCode = k3sSystemAddonPlanService.buildNodeDeployPlan(clusterInnerInfo, clusterNodePlanInfo);
                    if (errorCode != ErrorCode.SUCCESS.getCode())
                    {
                        clusterNodePlanInfo.setNodeStatus(ClusterNodeStatus.PLAN_FAILED);
                        clsStatus = ClusterStatus.PLAN_FAILED;
                        LOGGER.error("build k3s controller deploy plan failed, errorCode:{}", errorCode);
                        stopBuildDeployPlan = true;
                        break;
                    }
                }

                clusterNodePlanInfo.setNodeStatus(ClusterNodeStatus.PLANNED);
            }
            catch (Exception e)
            {
                stopBuildDeployPlan = true;
                LOGGER.error("build k3s deploy plan failed :{}", e);
            }
            finally
            {
                clusterRepo.updateClusterNodePlanInfo(clusterNodePlanInfo);
                sendMessageToDeployProcessor(clusterInnerInfo, clusterNodePlanInfo);
            }
        }

        if (stopBuildDeployPlan)
        {
            return;
        }

        for (int i = 0; i < workSize; i++)
        {
            ClusterNodePlanInfo clusterNodePlanInfo = clusterWorkerNodePlanInfos.get(i);
            ClusterNodeInfo clusterNodeInfo = clusterNodePlanInfo.getClusterNodeInfo();
            if (clusterNodeInfo.getClusterRoles().contains(K3sRole.CONTROLLER))
            {
                continue;
            }
            ClusterNodePlanInfo.K3sContext k3sContext = clusterNodePlanInfo.getK3sContext();
            k3sContext.setRole(K3sRole.WORKER);
            k3sContext.setNodeName(clusterName + "-" + K3sRole.WORKER + "-" + String.valueOf(i));
            k3sContext.setK3sUrl(connectUrl);
            k3sContext.setK3sToken(token);
            // build deploy plan
            try
            {
                int errorCode = k3sWorkerPlanServiceImpl.buildNodeDeployPlan(clusterInnerInfo, clusterNodePlanInfo);
                if (errorCode != ErrorCode.SUCCESS.getCode())
                {
                    clusterNodePlanInfo.setNodeStatus(ClusterNodeStatus.PLAN_FAILED);
                    LOGGER.error("build k3s worker deploy plan failed, errorCode:{}", errorCode);
                    break;
                }

                clusterNodePlanInfo.setNodeStatus(ClusterNodeStatus.PLANNED);
            }
            catch (Exception e)
            {
                LOGGER.error("build k3s deploy plan failed :{}", e);
            }
            finally
            {
                clusterRepo.updateClusterNodePlanInfo(clusterNodePlanInfo);
                sendMessageToDeployProcessor(clusterInnerInfo, clusterNodePlanInfo);
            }

        }

        if (ClusterStatus.PLANNED.equals(clsStatus))
        {
            RedisUtil.sadd(K8sRedisField.DEPLOY_CLUSTERID_SET, "", clusterInnerInfo.getClusterId());
        }

        clusterInnerInfo.setStatus(clsStatus);
        sendProcessor(clusterInnerInfo, ClusterMsg.UPDATE_CLUSTER, ProcessorName.CLUSTER);

    }

    private void sendMessageToDeployProcessor(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo)
    {
        if (clusterNodePlanInfo.getNodeStatus().equals(ClusterNodeStatus.PLANNED))
        {
            MessagePack messagePack = new MessagePack();
            messagePack.setMsgType(ClusterMsg.DEPLOY);
            messagePack.setMessageObj(clusterNodePlanInfo);
            clusterProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOY);
            RedisUtil.lpush(K8sRedisField.DEPLOY_CLUSTER_NODE_LIST, clusterInnerInfo.getClusterId(), clusterNodePlanInfo.getClusterNodeInfo().getNodeId());
        }
    }


    private void buildK3sNodeUnDeployPlan(ClusterInnerInfo clusterInnerInfo)
    {
        LOGGER.info("begin build undeploy cluster plan. {}", clusterInnerInfo.getName());
        List<String> roleList = Arrays.asList(K3sRole.CONTROLLER, K3sRole.WORKER);
        for (String role : roleList)
        {
            List<ClusterNodePlanInfo> clusterNodePlanInfos = clusterInnerInfo.getClusterNodePlanInfos(role);
            if (CollectionUtils.isEmpty(clusterNodePlanInfos))
            {
                continue;
            }

            for (ClusterNodePlanInfo nodePlanInfo : clusterNodePlanInfos)
            {
                LOGGER.info("cluster node {} status is:{}", nodePlanInfo.getClusterNodeInfo().getNodeId(), nodePlanInfo.getNodeStatus());

                Pair<Integer, Integer> nodeStatusPair = combRpcService.getEdgeResourceService().getNodeStatus(nodePlanInfo.getClusterNodeInfo().getNodeId());

                if (nodePlanInfo.isForceDelete() &&
                        (nodeStatusPair == null || nodeStatusPair.getLeft() == ActiveStatus.REMOVED || nodeStatusPair.getRight() == OnlineStatus.OFFLINE))
                {
                    nodePlanInfo.setNodeStatus(ClusterNodeStatus.RELEASED);
                    nodePlanInfo.setMonitorPlanList(null);
                    clusterRepo.updateClusterNodePlanInfo(nodePlanInfo);
                    continue;
                }

                //if undeploy failed can redo it
                if (nodePlanInfo.getNodeStatus().getCode() >= ClusterNodeStatus.UPLANNED.getCode()
                        && nodePlanInfo.getNodeStatus().getCode() != UDEPLOY_FAILED.getCode())
                {
                    continue;
                }

                if (nodePlanInfo.getNodeStatus().getCode() == UDEPLOY_FAILED.getCode())
                {
                    nodePlanInfo.getContainerUPlanList().clear();
                    nodePlanInfo.getContainerPlanList().clear();
                    nodePlanInfo.setNodeStatus(ClusterNodeStatus.UPLANNING);
                    clusterRepo.updateClusterNodeReUnDeployPlanInfo(nodePlanInfo);
                }
                else
                {
                    nodePlanInfo.setNodeStatus(ClusterNodeStatus.UPLANNING);
                    clusterRepo.updateClusterNodePlanInfo(nodePlanInfo);
                }

                PlanService planService = getK3sPlanService(nodePlanInfo.getClusterNodeInfo().getClusterRoles());
                if (planService == null)
                {
                    continue;
                }

                try
                {
                    planService.buildNodeUnDeployPlan(clusterInnerInfo, nodePlanInfo);
                }
                catch (Exception e)
                {
                    LOGGER.error("build clusterId:{} node:{} node unDeploy plan error:{}", clusterInnerInfo.getClusterId(), nodePlanInfo.getClusterNodeInfo().getNodeId(), e);
                    nodePlanInfo.setNodeStatus(UDEPLOY_FAILED);
                    clusterRepo.updateClusterNodePlanInfo(nodePlanInfo);
                    break;
                }

                nodePlanInfo.setNodeStatus(ClusterNodeStatus.UPLANNED);

                clusterRepo.updateClusterNodePlanInfo(nodePlanInfo);
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(ClusterMsg.UNDEPLOY);
                messagePack.setMessageObj(nodePlanInfo);
                clusterProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOY);
                RedisUtil.lpush(K8sRedisField.UNDEPLOY_CLUSTER_NODE_LIST, clusterInnerInfo.getClusterId(), nodePlanInfo.getClusterNodeInfo().getNodeId());
            }
            RedisUtil.sadd(K8sRedisField.UNDEPLOY_CLUSTERID_SET, "", clusterInnerInfo.getClusterId());
//            clusterRepo.updateClusterInfo(clusterInnerInfo);
            sendProcessor(clusterInnerInfo, ClusterMsg.UPDATE_CLUSTER, ProcessorName.CLUSTER);
        }
    }


    private DeploySituationHolder recordTheNumberOfEachRole(ClusterInnerInfo clusterInnerInfo, List<String> roleList)
    {
        DeploySituationHolder deploySituationHolder = new DeploySituationHolder();
        for (String role : roleList)
        {
            List<ClusterNodePlanInfo> clusterNodePlanInfos = clusterInnerInfo.getClusterNodePlanInfos(role);
            if (CollectionUtils.isEmpty(clusterNodePlanInfos))
            {
                continue;
            }
            deploySituationHolder.getNumberOfRoles().put(role,
                    new DeploySituation(role, clusterNodePlanInfos.size(), 0, 0));
        }
        return deploySituationHolder;
    }

    private static class DeploySituationHolder
    {
        private Map<String, DeploySituation> numberOfRoles = new HashMap<>();

        public Map<String, DeploySituation> getNumberOfRoles()
        {
            return numberOfRoles;
        }

        public void increaseSuccessNum(String clusterRoles)
        {
            String[] roles = clusterRoles.split(",");
            if (roles.length > 0)
            {
                for (String role : roles)
                {
                    DeploySituation deploySituation = numberOfRoles.get(role);
                    if (Objects.nonNull(deploySituation))
                    {
                        deploySituation.increaseSuccessNum();
                    }
                }
            }

        }

        public void increaseFailNum(String clusterRoles)
        {
            String[] roles = clusterRoles.split(",");
            if (roles.length > 0)
            {
                for (String role : roles)
                {
                    DeploySituation deploySituation = numberOfRoles.get(role);
                    if (Objects.nonNull(deploySituation))
                    {
                        deploySituation.increaseFailNum();
                    }
                }
            }

        }

        /**
         * false means the deploy needs to be stopped, true means continue the deploy
         * @param role
         * @return
         */
        public boolean checkContinueDeploy(String role)
        {
            if ((K8sRole.ETCD.equals(role)) || (K8sRole.CONTROLLER.equals(role)))
            {
                DeploySituation deploySituation = numberOfRoles.get(role);
                if (deploySituation.num > 0)
                {
                    if (deploySituation.successNum > 0)
                    {
                        return true;
                    }

                    if (deploySituation.failNum < deploySituation.num)
                    {
                        return true;
                    }
                    else
                    {
                      return false;
                    }
                }
            }
            else
            {
               return true;
            }
            return false;
        }

        @Override
        public String toString()
        {
            return "DeploySituationHolder{" +
                    "numberOfRoles=" + numberOfRoles +
                    '}';
        }
    }

    private static class DeploySituation
    {
        private String role;

        private int num;

        private int successNum;

        private int failNum;

        public DeploySituation(String role, int num, int successNum, int failNum)
        {
            this.role = role;
            this.num = num;
            this.successNum = successNum;
            this.failNum = failNum;
        }

        public void increaseSuccessNum()
        {
            if (this.num == this.successNum){
                return;
            }
            this.successNum = this.successNum + 1;
        }

        public void increaseFailNum()
        {
            if (this.num == this.failNum){
                return;
            }
            this.failNum = this.failNum + 1;
        }

        public String getRole()
        {
            return role;
        }

        public void setRole(String role)
        {
            this.role = role;
        }

        public int getNum()
        {
            return num;
        }

        public void setNum(int num)
        {
            this.num = num;
        }

        public int getSuccessNum()
        {
            return successNum;
        }

        public void setSuccessNum(int successNum)
        {
            this.successNum = successNum;
        }

        public int getFailNum()
        {
            return failNum;
        }

        public void setFailNum(int failNum)
        {
            this.failNum = failNum;
        }

        @Override
        public String toString()
        {
            return "DeploySituation{" +
                    "role='" + role + '\'' +
                    ", num=" + num +
                    ", successNum=" + successNum +
                    ", failNum=" + failNum +
                    '}';
        }
    }
}
