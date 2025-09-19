package com.lnjoying.justice.cluster.manager.service.rpc;

import com.google.common.collect.Lists;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lnjoying.justice.cluster.manager.common.*;
import com.lnjoying.justice.cluster.manager.config.ClusterManagerConfig;
import com.lnjoying.justice.cluster.manager.config.ClusterServerRootCA;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterInfoExample;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterNodeInfo;
import com.lnjoying.justice.cluster.manager.db.repo.ClusterRepo;
import com.lnjoying.justice.cluster.manager.domain.model.*;
import com.lnjoying.justice.cluster.manager.service.agent.ClusterAgentService;
import com.lnjoying.justice.cluster.manager.service.cert.CertService;
import com.lnjoying.justice.cluster.manager.service.cluster.ClusterService;
import com.lnjoying.justice.cluster.manager.service.process.ClusterMsgProcessStrategy;
import com.lnjoying.justice.cluster.manager.service.process.ClusterProcessStrategy;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.entity.cluster.ClusterAuthParams;
import com.lnjoying.justice.schema.entity.cluster.ClusterEntity;
import com.lnjoying.justice.schema.entity.cluster.ClusterSecretInfo;
import com.lnjoying.justice.schema.entity.scheduler.BindRelation;
import com.lnjoying.justice.schema.entity.scheduler.BindResourcesResult;
import com.lnjoying.justice.schema.entity.scheduler.ClusterBindNode;
import com.lnjoying.justice.schema.entity.scheduler.SchedulerClusterResult;
import com.lnjoying.justice.schema.entity.stat.GetClusterNumMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.GetStatusMetricsRsp;
import com.lnjoying.justice.schema.msg.*;
import com.lnjoying.justice.schema.service.cluster.ClusterManagerService;
import com.micro.core.common.Pair;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.*;

import static com.lnjoying.justice.cluster.manager.common.KeyCertName.CACertName;
import static com.lnjoying.justice.cluster.manager.common.KeyCertName.ClusterServerCACertName;
import static com.lnjoying.justice.schema.common.ErrorCode.AUTH_CLUSTER_FAILED;

/**
 *use to impl cluster manager for rpc
 */
@RpcSchema(schemaId = "clusterManagerService")
public class ClusterManagerServiceImpl implements ClusterManagerService
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private  ClusterRepo clusterRepo;
    
    @Autowired
    private CertService certService;
    
    @Autowired
    private  ClusterProcessStrategy clusterProcessStrategy;

    @Autowired
    private ClusterMsgProcessStrategy clusterMsgProcessStrategy;
    
    @Autowired
    private ClusterManagerConfig clusterManagerConfig;

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private CombRpcService combRpcService;

    @Autowired
    private ClusterAgentService clusterAgentService;

    @Autowired
    @Lazy
    private ClusterServerRootCA clusterServerRootCA;

    @Override
    public int deliver(@ApiParam(name = "message") EdgeMessage edgeMessage)
    {
        try
        {
            EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.parseFrom(edgeMessage.getNetMessage());
            LOGGER.info("deliver msg: {}", netMessage);

            switch (netMessage.getHead().getMsgName())
            {
                case MessageName.CLOUD_CTRL:
                    processCloudCtrl(netMessage, edgeMessage.getNodeId());
                    break;
            }
        }
        catch (InvalidProtocolBufferException e)
        {
            e.printStackTrace();
            return ErrorCode.SystemError.getCode();
        }

        return ErrorCode.SUCCESS.getCode();
    }

    void sendProcessor(Object msg, String msgName, String processor)
    {
        MessagePack messagePack = new MessagePack();
        messagePack.setMessageObj(msg);
        messagePack.setMsgType(msgName);
        clusterProcessStrategy.sendMessage(messagePack, processor);
    }

    /**
     *bind edge node to cluster
     * @param schedulerResult
     * @return
     */
    @Override
    public BindResourcesResult bindResources(@ApiParam(name = "schedulerResult") SchedulerClusterResult schedulerResult)
    {
        BindResourcesResult bindResourcesResult = new BindResourcesResult();
        bindResourcesResult.setSuccessBindRelations(new ArrayList<>());
        bindResourcesResult.setFailBindRelations(new ArrayList<>());
    
        ClusterInnerInfo clusterInnerInfo = clusterRepo.getClusterInnerInfo(schedulerResult.getClusterId());
        if (clusterInnerInfo == null)
        {
            return bindResourcesResult;
        }

        try
        {
            if (clusterInnerInfo.getStatus().getCode() >= ClusterStatus.ASSIGNED.getCode())
            {
                LOGGER.error("{} cluster status have been change to {}", clusterInnerInfo.getName(), clusterInnerInfo.getStatus());
                return bindResourcesResult;
            }

    
            if (schedulerResult.getSchedulerState() > SchedulerState.SUCCESS.getCode())
            {
                clusterInnerInfo.setStatus(ClusterStatus.ASSIGNED_FAILED);
                sendProcessor(clusterInnerInfo, ClusterMsg.UPDATE_CLUSTER, ProcessorName.CLUSTER);
                return bindResourcesResult;
            }

            List<ClusterBindNode> clusterBindNodes = combRpcService.getEdgeResourceService().fillClusterBindNodeField(schedulerResult.getBindNodes());
            
            for (ClusterBindNode bindNode : clusterBindNodes)
            {
                try
                {
                    if (bindNode.getExternalAddress() == null)
                    {
                        bindNode.setExternalAddress("");
                    }
                    
                    if (StringUtils.isBlank(bindNode.getInternalAddress()))
                    {
                        bindNode.setInternalAddress(bindNode.getExternalAddress());
                    }
                    
                    if (StringUtils.isBlank(bindNode.getInternalAddress()))
                    {
                        continue;
                    }
                    
                    TblClusterNodeInfo tblClusterNodeInfo = new TblClusterNodeInfo();
                    BeanUtils.copyProperties(bindNode, tblClusterNodeInfo);
                    tblClusterNodeInfo.setClusterId(schedulerResult.getClusterId());
                    tblClusterNodeInfo.setCreateTime(new Date());
                    tblClusterNodeInfo.setUpdateTime(tblClusterNodeInfo.getCreateTime());
                    tblClusterNodeInfo.setStatus(ClusterNodeStatus.BINDED.getCode());
                    tblClusterNodeInfo.setNodeName(bindNode.getNodeName());
                    tblClusterNodeInfo.setExternalAddress(bindNode.getExternalAddress());
                    tblClusterNodeInfo.setInternalAddress(bindNode.getInternalAddress());
                    if (! CollectionUtils.isEmpty(bindNode.getLabels()))
                    {
                        tblClusterNodeInfo.setLabels(JsonUtils.toJson(bindNode.getLabels()));
                    }
    
                    if (! CollectionUtils.isEmpty(bindNode.getAnnotations()))
                    {
                        tblClusterNodeInfo.setAnnotations(JsonUtils.toJson(bindNode.getAnnotations()));
                    }
    
                    if (! CollectionUtils.isEmpty(bindNode.getTaints()))
                    {
                        tblClusterNodeInfo.setTaints(JsonUtils.toJson(bindNode.getTaints()));
                    }

                    if (! CollectionUtils.isEmpty(bindNode.getSoftWareInfos()))
                    {
                        tblClusterNodeInfo.setDockerInfo(bindNode.getSoftWareInfos().getOrDefault("docker", null));
                    }

                    clusterRepo.insertClusterNode(tblClusterNodeInfo);
                    ClusterNodePlanInfo clusterNodePlanInfo = new ClusterNodePlanInfo();
                    clusterNodePlanInfo.setClusterId(clusterInnerInfo.getClusterId());
                    clusterNodePlanInfo.setClusterNodeInfo(new ClusterNodeInfo());
                    BeanUtils.copyProperties(bindNode, clusterNodePlanInfo.getClusterNodeInfo());
                    clusterNodePlanInfo.setLabels();
                    clusterNodePlanInfo.setNodeStatus(ClusterNodeStatus.BINDED);
                    clusterInnerInfo.addNodePlan(clusterNodePlanInfo, bindNode.getClusterRoles());

                    bindResourcesResult.getSuccessBindRelations().add(convertClusterBindNodeToBindRelation(bindNode));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    bindResourcesResult.getFailBindRelations().add(convertClusterBindNodeToBindRelation(bindNode));
                    continue;
                }
            }
            
            if (clusterInnerInfo.nodePlanSize() < 1)
            {
                clusterInnerInfo.setStatus(ClusterStatus.ASSIGNING);
            }
            else
            {
                clusterInnerInfo.setStatus(ClusterStatus.ASSIGNED);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("bind cluster error. {}", e.toString());
            schedulerResult.getBindNodes().forEach(bindNode -> {bindResourcesResult.getFailBindRelations().add(convertClusterBindNodeToBindRelation(bindNode));});
            clusterInnerInfo.setStatus(ClusterStatus.ASSIGNED_FAILED);
        }
        finally
        {
            sendProcessor(clusterInnerInfo, ClusterMsg.UPDATE_CLUSTER, ProcessorName.CLUSTER);
        }

        if (clusterInnerInfo.getStatus() == ClusterStatus.ASSIGNED)
        {
            MessagePack messagePack = new MessagePack();
            messagePack.setMsgType(ClusterMsg.PREPARE_BUILD);
            messagePack.setMessageObj(clusterInnerInfo);
            clusterProcessStrategy.sendMessage(messagePack, ProcessorName.CLUSTER);
        }
       

        return bindResourcesResult;
    }

    private BindRelation convertClusterBindNodeToBindRelation(ClusterBindNode bindNode)
    {
        BindRelation bindRelation = new BindRelation();
        bindRelation.setWaitAssignId(bindNode.getClusterRoles());
        bindRelation.setDstRegionId(bindNode.getRegionId());
        bindRelation.setDstSiteId(bindNode.getSiteId());
        bindRelation.setDstNodeId(bindNode.getNodeId());
        return bindRelation;
    }

    @Override
    public ClusterSecretInfo getClusterServerCert()
    {
        X509CertificateInfo x509CertificateInfo = certService.genClusterServerCerts(ClusterServerCACertName);
        if (Objects.nonNull(x509CertificateInfo))
        {
            return ClusterSecretInfo.builder().crtPem(x509CertificateInfo.getCertificatePem())
                    .keyPem(x509CertificateInfo.getKeyPem()).rootPem(clusterServerRootCA.getSvrRootCrt()).build();
        }
        return null;
    }

    @Override
    public CombRetPacket authCluster(@ApiParam(name = "clusterAuthParams") ClusterAuthParams clusterAuthParams)
    {
        CombRetPacket combRetPacket = new CombRetPacket();
        try {
            if (Objects.nonNull(clusterAuthParams))
            {
                String clusterId = clusterAuthParams.getClusterId();
                if (StringUtils.isNotBlank(clusterId))
                {
                    ClusterInnerInfo clusterInnerInfo = clusterRepo.getClusterInnerInfo(clusterId);
                    if (clusterInnerInfo == null)
                    {
                        combRetPacket.setStatus(AUTH_CLUSTER_FAILED.getCode());
                        LOGGER.error("cluster {} isn't exist", clusterAuthParams.getClusterId());
                        return combRetPacket;
                    }

                    String token = clusterRepo.selectSecretTokenByClusterId(clusterId);
                    if (checkClusterIdAndToken(clusterAuthParams, clusterInnerInfo, token))
                    {
                        String endpointNodeIp = clusterInnerInfo.getEndpointNodeIp();

                        Map<String, X509CertificateInfo> certificateInfo = clusterInnerInfo.getCertMap();
                        if (certificateInfo == null)
                        {
                            certificateInfo = clusterRepo.getCertficateInfo(clusterId);
                        }
                        String workId = clusterRepo.selectAgentIdByClusterId(clusterId);
                        ClusterEntity clusterEntity = ClusterEntity.builder()
                                .clusterId(clusterId).workderId(workId).clusterIp(endpointNodeIp).clusterPort(6443)
                                .clusterCaCrtPem(certificateInfo.get(CACertName).getCertificatePem())
                                .userCrtPem("").userKeyPem("")
                                .token(token).timestamp(new Date().getTime())
                                .build();
                        LOGGER.info("auth cluster success, return cluster entity:{}", clusterEntity);
                        combRetPacket.setObj(clusterEntity);
                        combRetPacket.setStatus(ErrorCode.SUCCESS.getCode());
                        return combRetPacket;
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("auth cluster failed:{}", e);
        }
        combRetPacket.setStatus(AUTH_CLUSTER_FAILED.getCode());
        return combRetPacket;
    }

    @Override
    public int workerReg(@ApiParam(name = "message") EdgeMessage edgeMessage)
    {
        try
        {
            EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.parseFrom(edgeMessage.getNetMessage());
            EeWorkerDef.msg_worker_reg_req_body workerRegReqBody = netMessage.getWorkerRegReqBody();
            String token = workerRegReqBody.getToken();

            if (StringUtils.isEmpty(token))
            {
                return ErrorCode.WORKER_REG_TOKEN_INVALID.getCode();
            }

            return clusterAgentService.addWorker(workerRegReqBody);
        }
        catch (InvalidProtocolBufferException e)
        {
            e.printStackTrace();
            return ErrorCode.SystemError.getCode();
        }
    }

    @Override
    public int workerLogin(@ApiParam(name = "message") EdgeMessage edgeMessage)
    {
        try
        {
            EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.parseFrom(edgeMessage.getNetMessage());
            EeWorkerDef.msg_worker_login_req_body workerLoginReqBody = netMessage.getWorkerLoginReqBody();
            return clusterAgentService.workerLogin(workerLoginReqBody);
        }
        catch (InvalidProtocolBufferException e)
        {
            e.printStackTrace();
            return ErrorCode.SystemError.getCode();
        }
    }

    @Override
    public CombRetPacket authClusterUser(@ApiParam(name = "clusterAuthParams") ClusterAuthParams clusterAuthParams)
    {
        CombRetPacket combRetPacket = new CombRetPacket();
        try
        {
            if (Objects.nonNull(clusterAuthParams))
            {
                String clusterId = clusterAuthParams.getClusterId();
                if (StringUtils.isBlank(clusterId))
                {
                    combRetPacket.setStatus(AUTH_CLUSTER_FAILED.getCode());
                    return combRetPacket;
                }

                ClusterInnerInfo clusterInnerInfo = clusterRepo.getClusterInnerInfo(clusterId);
                if (clusterInnerInfo == null)
                {
                    LOGGER.error("cluster {} isn't exist", clusterAuthParams.getClusterId());
                    combRetPacket.setStatus(AUTH_CLUSTER_FAILED.getCode());
                    return combRetPacket;
                }

                UserBasicInfo userBasicInfo = UserBasicInfo.builder().userId(clusterAuthParams.getUserId()).bpId(clusterAuthParams.getBpId()).userKind(clusterAuthParams.getUserKind()).build();


                ClsUtils.checkUserGrantForCluster(userBasicInfo, clusterInnerInfo);
                String token = clusterRepo.selectSecretTokenByClusterId(clusterId);
                if (StringUtils.isBlank(token))
                {
                    LOGGER.error("cluster {} token is blank", clusterAuthParams.getClusterId());
                    combRetPacket.setStatus(AUTH_CLUSTER_FAILED.getCode());
                    return combRetPacket;
                }
    
                String clusterServerInner = clusterManagerConfig.getClusterServerInner();
                if (StringUtils.isBlank(clusterServerInner))
                {
                    clusterServerInner = "127.0.0.1:16443";
                }
    
    
                String clsvrUrl = String.format("http://%s/k8s/clusters/%s", clusterServerInner, clusterId);
    
    
                ClusterEntity clusterEntity = ClusterEntity.builder().token(token).clsvrUrl(clsvrUrl).build();
                LOGGER.info("auth cluster success, return cluster entity:{}", clusterEntity);
                combRetPacket.setObj(clusterEntity);
                combRetPacket.setStatus(ErrorCode.SUCCESS.getCode());
                return combRetPacket;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("auth cluster failed:{}", e);
        }
        combRetPacket.setStatus(AUTH_CLUSTER_FAILED.getCode());
        return combRetPacket;
    }

    void processCloudCtrl(EeNetMessageApi.ee_net_message netMessage, String nodeId)
    {
        LOGGER.info("process cloud ctrl");
        EeCtrlMessageDef.msg_cloud_ctrl_body msgCloudCtrlBody = netMessage.getCloudCtrlBody();
        switch (msgCloudCtrlBody.getCtrlType())
        {
            case CtrlType.SYNC_WORKER_IF_STATE_REQ:
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(ClusterMsg.SYNC_WORKER_IF_STATE);
                messagePack.setMessageObj(new Pair(nodeId, netMessage));
                clusterMsgProcessStrategy.sendMessage(messagePack);
                break;
            }
        }
    }

    private boolean checkClusterIdAndToken(ClusterAuthParams clusterAuthParams, ClusterInnerInfo cluster, String token)
    {
        if (Objects.nonNull(cluster))
        {
            int status = cluster.getServiceStatus();
            if (ClusterActiveStatus.ACTIVE != status)
            {
                return false;
            }

            String tokenParams = clusterAuthParams.getToken();
            if (StringUtils.isNotBlank(tokenParams) && tokenParams.endsWith(token))
            {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public int getClusterNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId)
    {
        TblClusterInfoExample example = new TblClusterInfoExample();
        TblClusterInfoExample.Criteria criteria = example.createCriteria();

        if (CollectionUtils.hasContent(bpId))
        {
            criteria.andBpEqualTo(bpId);
        }

        if (CollectionUtils.hasContent(userId))
        {
            criteria.andMembersLike("%" + userId + "%");
        }
        criteria.andStatusNotEqualTo(ClusterStatus.REMOVED.getCode());

        return (int)clusterRepo.countByExample(example);
    }

    @Override
    public String getKubeConfig(@ApiParam(name = "clusterId")String clusterId, @ApiParam(name = "operUserInfo")Pair<String, String> operUserInfo)
    {
        String kubeConfig = clusterService.getKubeConfig(clusterId, operUserInfo);
        return kubeConfig;
    }

    @Override
    public GetStatusMetricsRsp getClusterStatusMetrics(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId)
    {
        return clusterRepo.getClusterStatusMetrics(userId, bpId);
    }

    @Override
    public GetClusterNumMetricsRsp getClusterNumMetrics(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId)
    {
        GetClusterNumMetricsRsp getClusterNumMetricsRsp = new GetClusterNumMetricsRsp();
        TblClusterInfoExample k8sExample = new TblClusterInfoExample();
        TblClusterInfoExample k3sExample = new TblClusterInfoExample();
        TblClusterInfoExample.Criteria k8sCriteria = k8sExample.createCriteria();
        TblClusterInfoExample.Criteria k3sCriteria = k3sExample.createCriteria();
        k8sCriteria.andStatusNotEqualTo(ClusterStatus.REMOVED.getCode());
        k3sCriteria.andStatusNotEqualTo(ClusterStatus.REMOVED.getCode());
        k8sCriteria.andCreateTypeEqualTo(ClusterCreateType.CUSTOM);
        k3sCriteria.andCreateTypeEqualTo(ClusterCreateType.CUSTOM);
        if (StringUtils.isNotEmpty(userId))
        {
            k8sCriteria.andOwnerEqualTo(userId);
            k3sCriteria.andOwnerEqualTo(userId);
        }
        if (StringUtils.isNotEmpty(bpId))
        {
            k8sCriteria.andBpEqualTo(userId);
            k3sCriteria.andBpEqualTo(userId);
        }
        k8sCriteria.andClusterTypeEqualTo(ClusterType.K8S);
        k3sCriteria.andClusterTypeEqualTo(ClusterType.K3S);
        getClusterNumMetricsRsp.setK3sNum((int) clusterRepo.countByExample(k3sExample));
        getClusterNumMetricsRsp.setK8sNum((int) clusterRepo.countByExample(k8sExample));
        return getClusterNumMetricsRsp;
    }

    @Override
    public CombRetPacket getClusterAgent(@ApiParam(name = "clusterId")String clusterId)
    {
        CombRetPacket combRetPacket = new CombRetPacket();
        try
        {
            String workId = clusterRepo.selectAgentIdByClusterId(clusterId);
            combRetPacket.setStatus(ErrorCode.SUCCESS.getCode());
            combRetPacket.setObj(workId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get cluster agent error, {}", e.getMessage());
            combRetPacket.setStatus(ErrorCode.SystemError.getCode());
        }
        return combRetPacket;
    }

    @Override
    public String getClusterName(@ApiParam(name = "clusterId")String clusterId)
    {
        TblClusterInfo clusterInfo = clusterRepo.getCluster(clusterId);
        if (Objects.nonNull(clusterInfo))
        {
            return clusterInfo.getName();
        }
        return "";
    }
}
