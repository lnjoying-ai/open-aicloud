package com.lnjoying.justice.cmp.service.rpc;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lnjoying.justice.cmp.common.*;
import com.lnjoying.justice.cmp.common.nextstack.NSInstanceStatus;
import com.lnjoying.justice.cmp.common.os.OSInstanceStatus;
import com.lnjoying.justice.cmp.config.CloudManagerConfig;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.*;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.OSServiceEndpoints;
import com.lnjoying.justice.cmp.domain.model.Authorization;
import com.lnjoying.justice.cmp.service.agent.CloudAgentService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.process.CloudMsgProcessStrategy;
import com.lnjoying.justice.cmp.utils.osclient.OSClientUtil;
import com.lnjoying.justice.cmp.utils.osclient.OSClientV3;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.entity.cluster.ClusterAuthParams;
import com.lnjoying.justice.schema.entity.cluster.ClusterEntity;
import com.lnjoying.justice.schema.entity.cmp.*;
import com.lnjoying.justice.schema.entity.scheduler.*;
import com.lnjoying.justice.schema.entity.stat.GetCloudNumMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.GetStatusMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.GetVmInstanceStatusMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.StatusMetrics;
import com.lnjoying.justice.schema.msg.*;
import com.lnjoying.justice.schema.service.cmp.CloudManagerService;
import com.micro.core.common.Pair;
import com.micro.core.persistence.redis.RedisUtil;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;
import static com.lnjoying.justice.schema.common.ErrorCode.AUTH_CLUSTER_FAILED;

/**
 *use to impl cloud manager for rpc
 */
@RpcSchema(schemaId = "cloudManagerService")
public class CloudManagerServiceImpl implements CloudManagerService
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private CloudAgentService cloudAgentService;

    @Autowired
    private CloudMsgProcessStrategy cloudMsgProcessStrategy;

    @Autowired
    private CombRpcService combRpcService;

    @Autowired
    private CloudManagerConfig cloudManagerConfig;

    @Autowired
    private FlavorRepository flavorRepository;

    @Autowired
    private OSNovaRepository osNovaRepository;

    @Autowired
    private OSCinderRepository osCinderRepository;

    @Autowired
    private VmComputeRepository vmComputeRepository;

    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private NetworkRepository networkRepository;

    @Autowired
    private CloudService cloudService;

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

            return cloudAgentService.addWorker(workerRegReqBody);
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
            return cloudAgentService.workerLogin(workerLoginReqBody);
        }
        catch (InvalidProtocolBufferException e)
        {
            e.printStackTrace();
            return ErrorCode.SystemError.getCode();
        }
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
                messagePack.setMsgType(CloudMsg.SYNC_WORKER_IF_STATE);
                messagePack.setMessageObj(new Pair(nodeId, netMessage));
                cloudMsgProcessStrategy.sendMessage(messagePack);
                break;
            }
        }
    }

    @Override
    public int getCloudNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId, @ApiParam(name = "siteId")String siteId)
    {
        TblCloudInfoExample example = new TblCloudInfoExample();
        TblCloudInfoExample.Criteria criteria = example.createCriteria();

        if (CollectionUtils.hasContent(bpId))
        {
            criteria.andBpEqualTo(bpId);
        }

        if (CollectionUtils.hasContent(userId))
        {
            criteria.andOwnerEqualTo(userId);
        }

        if (CollectionUtils.hasContent(siteId))
        {
            criteria.andSiteIdEqualTo(siteId);
        }
        criteria.andStatusNotEqualTo(CloudStatus.REMOVED.getCode());

        return (int)cloudRepository.countByExample(example);
    }

    @Override
    public BindResourcesResult bindResources(@ApiParam(name = "schedulerResult") SchedulerResult schedulerResult)
    {
        return cloudAgentService.bindResources(schedulerResult);
    }

    @Override
    public void updateCloudAgentStatus(@ApiParam(name = "agentId") String agentId, @ApiParam(name = "status") Integer status)
    {
        try
        {
            TblCloudAgentInfo tblCloudAgentInfo = cloudRepository.getCloudAgentByPrimaryKey(agentId);
            if (tblCloudAgentInfo == null || tblCloudAgentInfo.getStatus() == InstanceState.FIN_CLOUD_REMOVE.getCode())
            {
                LOGGER.info("worker {} is null or removed", agentId);
                return;
            }

            if (status == InstanceState.HARDWARE_ERROR.getCode() || status == InstanceState.NO_IMAGE.getCode() ||
                    status == InstanceState.IMAGE_DOWNLOAD_FAILED.getCode() || status == InstanceState.CREATE_FAILED.getCode())
            {
                tblCloudAgentInfo.setStatus(status);
                combRpcService.getSchedulerService().releaseBindResources(tblCloudAgentInfo.getNodeId(), agentId);
            }
            else if (status == InstanceState.REMOVED.getCode() || status == InstanceState.OBJECT_AUTO_REMOVE.getCode())
            {
                tblCloudAgentInfo.setStatus(InstanceState.FIN_CLOUD_REMOVE.getCode());
                combRpcService.getSchedulerService().releaseBindResources(tblCloudAgentInfo.getNodeId(), agentId);
            }
            else
            {
                tblCloudAgentInfo.setStatus(status);
            }

            tblCloudAgentInfo.setUpdateTime(new Date());

            cloudRepository.updateCloudAgentInfo(tblCloudAgentInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("Error while update cloud agent state, message {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public CombRetPacket authCloud(@ApiParam(name = "cloudAuthParams") ClusterAuthParams cloudAuthParams)
    {
        CombRetPacket combRetPacket = new CombRetPacket();
        try {
            if (Objects.nonNull(cloudAuthParams))
            {
                String cloudId = cloudAuthParams.getClusterId();
                if (StringUtils.isNotBlank(cloudId))
                {
                    TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
                    if (tblCloudInfo == null)
                    {
                        combRetPacket.setStatus(AUTH_CLUSTER_FAILED.getCode());
                        LOGGER.error("cloud {} isn't exist", cloudAuthParams.getClusterId());
                        return combRetPacket;
                    }

                    if (StringUtils.isEmpty(cloudAuthParams.getToken()) || cloudAuthParams.getToken().replace("systemadmin", "").startsWith(cloudManagerConfig.getCloudManagerToken()))
                    {
                        URL cloudUrl = new URL(tblCloudInfo.getUrl());

                        String workId = cloudRepository.selectWorkerIdByCloudId(cloudId);
                        ClusterEntity clusterEntity;

                        if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
                        {
                            Authorization authorization = JsonUtils.fromJson(tblCloudInfo.getAuthorization(), Authorization.class);

                            if (tblCloudInfo.getUrl().toLowerCase(Locale.ROOT).startsWith("https://"))
                            {
                                clusterEntity = ClusterEntity.builder()
                                        .clusterId(cloudId).workderId(workId).clusterIp(cloudUrl.getHost()).clusterPort(cloudUrl.getPort() < 1 ? 443 : cloudUrl.getPort())
                                        .clusterCaCrtPem(tblCloudInfo.getCertificate()).userCrtPem("").userKeyPem("")
                                        .token(JsonUtils.toJson(authorization.getAccessKey())).timestamp(new Date().getTime()).clusterType("clouds")
                                        .mode(tblCloudInfo.getMode()).build();
                            }
                            else
                            {
                                clusterEntity = ClusterEntity.builder()
                                        .clusterId(cloudId).workderId(workId).clusterIp(cloudUrl.getHost()).clusterPort(cloudUrl.getPort() < 1 ? 80 : cloudUrl.getPort())
                                        .userCrtPem("").userKeyPem("")
                                        .token(JsonUtils.toJson(authorization.getAccessKey())).timestamp(new Date().getTime()).clusterType("clouds")
                                        .mode(tblCloudInfo.getMode()).build();
                            }
                        }
                        else if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()) || tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName()))
                        {
                            OSServiceEndpoints.OSServiceResource osServiceResource = OSServiceEndpoints.OSServiceResource.fromName(cloudAuthParams.getResources());
                            if (osServiceResource == null)
                            {
                                if (tblCloudInfo.getUrl().toLowerCase(Locale.ROOT).startsWith("https://"))
                                {
                                    clusterEntity = ClusterEntity.builder()
                                            .clusterId(cloudId).workderId(workId).clusterIp(cloudUrl.getHost()).clusterPort(cloudUrl.getPort() < 1 ? 443 : cloudUrl.getPort())
                                            .clusterCaCrtPem(tblCloudInfo.getCertificate()).userCrtPem("").userKeyPem("")
                                            .token("").timestamp(new Date().getTime()).clusterType("clouds")
                                            .mode(tblCloudInfo.getMode()).build();
                                }
                                else
                                {
                                    clusterEntity = ClusterEntity.builder()
                                            .clusterId(cloudId).workderId(workId).clusterIp(cloudUrl.getHost()).clusterPort(cloudUrl.getPort() < 1 ? 80 : cloudUrl.getPort())
                                            .userCrtPem("").userKeyPem("")
                                            .token("").timestamp(new Date().getTime()).clusterType("clouds")
                                            .mode(tblCloudInfo.getMode()).build();
                                }
                            }
                            else
                            {
                                OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
                                OSClientV3.ServiceEndpoint serviceEndpoint = osClientV3.getServiceEndpoints().get(osServiceResource.getService());
                                if (osServiceResource == OSServiceEndpoints.OSServiceResource.AUTH)
                                {
                                    clusterEntity = ClusterEntity.builder()
                                            .clusterId(cloudId).workderId(workId).clusterIp(serviceEndpoint.getIp()).clusterPort(serviceEndpoint.getPort())
                                            .clusterCaCrtPem(tblCloudInfo.getCertificate()).userCrtPem("").userKeyPem("")
                                            .token("").timestamp(new Date().getTime()).clusterType("clouds").urlPrefix(serviceEndpoint.getUrlPrefix())
                                            .mode(tblCloudInfo.getMode()).build();
                                }
                                else
                                {
                                    Map<String, String> token = new HashMap<>();
                                    token.put("xAuthToken", osClientV3.getToken());
                                    clusterEntity = ClusterEntity.builder()
                                            .clusterId(cloudId).workderId(workId).clusterIp(serviceEndpoint.getIp()).clusterPort(serviceEndpoint.getPort())
                                            .clusterCaCrtPem(tblCloudInfo.getCertificate()).userCrtPem("").userKeyPem("")
                                            .token(JsonUtils.toJson(token)).timestamp(new Date().getTime()).clusterType("clouds").urlPrefix(serviceEndpoint.getUrlPrefix())
                                            .mode(tblCloudInfo.getMode()).build();
                                }
                            }
                        }
                        else
                        {
                            combRetPacket.setStatus(ErrorCode.AUTH_CLUSTER_FAILED.getCode());
                            return combRetPacket;
                        }

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
            LOGGER.error("auth cloud failed:{}", e);
        }
        combRetPacket.setStatus(AUTH_CLUSTER_FAILED.getCode());
        return combRetPacket;
    }

    @Override
    public GetStatusMetricsRsp getCloudStatusMetrics(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId)
    {
        return cloudRepository.getCloudStatusMetrics(userId, bpId);
    }

    @Override
    public GetStatusMetricsRsp getCloudHealthStatusMetrics(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId)
    {
        GetStatusMetricsRsp getStatusMetricsRsp = new GetStatusMetricsRsp();
        List<String> cloudhealths = RedisUtil.keys(RedisCache.CLOUD_HEALTH_STATUS + "*");
        if (cloudhealths == null) return getStatusMetricsRsp;
        getStatusMetricsRsp.setTotalNum(cloudhealths.size());
        getStatusMetricsRsp.setStatusMetrics(new ArrayList<>());

        int health = 0;
        int unhealth = 0;
        int unknown = 0;

        for (String cloudhealth : cloudhealths)
        {
            String status = RedisUtil.get(cloudhealth);
            if (status == null) continue;
            if (status.equals("0"))
            {
                health ++;
            }
            else if (status.equals("1"))
            {
                unhealth ++;
            }
            else if (status.equals("2"))
            {
                unknown ++;
            }
        }
        GetStatusMetricsRsp.StatusMetrics healthMetric =  new GetStatusMetricsRsp.StatusMetrics();
        healthMetric.setCount(health);
        healthMetric.setStatus(HealthStatus.HEALTHY.toStatusCode());
        GetStatusMetricsRsp.StatusMetrics unhealthMetric =  new GetStatusMetricsRsp.StatusMetrics();
        unhealthMetric.setCount(unhealth);
        unhealthMetric.setStatus(HealthStatus.UNHEALTHY.toStatusCode());
        GetStatusMetricsRsp.StatusMetrics unknownMetric =  new GetStatusMetricsRsp.StatusMetrics();
        unknownMetric.setCount(unknown);
        unknownMetric.setStatus(HealthStatus.UNKNOWN.toStatusCode());
        getStatusMetricsRsp.getStatusMetrics().add(healthMetric);
        getStatusMetricsRsp.getStatusMetrics().add(unhealthMetric);
        getStatusMetricsRsp.getStatusMetrics().add(unknownMetric);
        return getStatusMetricsRsp;
    }

    @Override
    public GetVmInstanceStatusMetricsRsp getCloudVmInstanceStatusMetrics(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId)
    {
        GetVmInstanceStatusMetricsRsp getVmInstanceStatusMetricsRsp = new GetVmInstanceStatusMetricsRsp();

        TblCloudInfoExample example = new TblCloudInfoExample();
        TblCloudInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotEqualTo(CloudStatus.REMOVED.getCode());
        List<TblCloudInfo> tblCloudInfos = cloudRepository.getCloudsByExample(example);

        if (tblCloudInfos == null || tblCloudInfos.isEmpty())
        {
            return getVmInstanceStatusMetricsRsp;
        }

        for (TblCloudInfo tblCloudInfo : tblCloudInfos)
        {
            GetVmInstanceStatusMetricsRsp.CloudVmInstanceStatusMetrics cloudVmInstanceStatusMetrics = new GetVmInstanceStatusMetricsRsp.CloudVmInstanceStatusMetrics();
            cloudVmInstanceStatusMetrics.setCloudId(tblCloudInfo.getCloudId());
            cloudVmInstanceStatusMetrics.setCloudName(tblCloudInfo.getName());
            cloudVmInstanceStatusMetrics.setProduct(tblCloudInfo.getProduct());
            cloudVmInstanceStatusMetrics.setStatusMetrics(new ArrayList<>());

            if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
            {
                List<StatusMetrics> nsStatusMetrics = vmComputeRepository.getVmInstanceStatusMetrics(tblCloudInfo.getCloudId(), userId, bpId);
                if (nsStatusMetrics != null)
                {
                    for (StatusMetrics statusMetrics : nsStatusMetrics)
                    {
                        NSInstanceStatus status = NSInstanceStatus.fromCode(statusMetrics.getStatus());
                        GetVmInstanceStatusMetricsRsp.StatusMetrics2 metrics = new GetVmInstanceStatusMetricsRsp.StatusMetrics2();
                        metrics.setCount(statusMetrics.getCount());
                        metrics.setStatus(status.toStatusCode());
                        cloudVmInstanceStatusMetrics.getStatusMetrics().add(metrics);
                    }
                }
            }
            else if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName()) ||
                    tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()))
            {
                List<StatusMetrics> osStatusMetrics = osNovaRepository.getVmInstanceStatusMetrics(tblCloudInfo.getCloudId(), userId, bpId);
                if (osStatusMetrics != null)
                {
                    for (StatusMetrics statusMetrics : osStatusMetrics)
                    {
                        OSInstanceStatus status = OSInstanceStatus.fromEn(statusMetrics.getStatusStr());
                        GetVmInstanceStatusMetricsRsp.StatusMetrics2 metrics = new GetVmInstanceStatusMetricsRsp.StatusMetrics2();
                        metrics.setCount(statusMetrics.getCount());
                        metrics.setStatus(status.toStatusCode());
                        cloudVmInstanceStatusMetrics.getStatusMetrics().add(metrics);
                    }
                }
            }
            getVmInstanceStatusMetricsRsp.getCloudVmInstanceStatusMetrics().add(cloudVmInstanceStatusMetrics);
        }
        return getVmInstanceStatusMetricsRsp;
    }

    @Override
    public GetCloudNumMetricsRsp getCloudNumMetrics(@ApiParam(name = "filter")String filter, @ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId)
    {
        GetCloudNumMetricsRsp getCloudNumMetricsRsp = new GetCloudNumMetricsRsp();

        getCloudNumMetricsRsp.setVmNum(cloudRepository.getVmNum(filter, userId, bpId));
        getCloudNumMetricsRsp.setBaremetalNum(cloudRepository.getBaremetalNum(filter, userId, bpId));

        return getCloudNumMetricsRsp;
    }

    @Override
    public List<RpcFlavorInfo> getCloudFlavor(@ApiParam(name = "cloudId")String cloudId)
    {
        List<RpcFlavorInfo> rpcFlavorInfos = null;
        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
        if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
        {
            TblCmpFlavorExample example = new TblCmpFlavorExample();
            TblCmpFlavorExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            List<TblCmpFlavor> tblCmpFlavors = flavorRepository.getFlavors(example);
            rpcFlavorInfos = tblCmpFlavors.stream().map(tblCmpFlavor ->{
                RpcFlavorInfo rpcFlavorInfo = new RpcFlavorInfo();
                rpcFlavorInfo.setCpu(tblCmpFlavor.getCpu());
                rpcFlavorInfo.setMem(tblCmpFlavor.getMem() * 1024);
                rpcFlavorInfo.setFlavorId(tblCmpFlavor.getFlavorId());
                rpcFlavorInfo.setCloudId(tblCmpFlavor.getCloudId());
                rpcFlavorInfo.setProductId(tblCmpFlavor.getFlavorId());
                rpcFlavorInfo.setName(tblCmpFlavor.getName());
                return rpcFlavorInfo;
            }).collect(Collectors.toList());
        }
        else if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()) || tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName()))
        {
            TblCmpOsFlavorsExample example = new TblCmpOsFlavorsExample();
            TblCmpOsFlavorsExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            List<TblCmpOsFlavors> tblCmpFlavors = osNovaRepository.getFlavors(example);
            rpcFlavorInfos = tblCmpFlavors.stream().map(tblCmpFlavor ->{
                RpcFlavorInfo rpcFlavorInfo = new RpcFlavorInfo();
                rpcFlavorInfo.setCpu(tblCmpFlavor.getVcpus());
                rpcFlavorInfo.setMem(tblCmpFlavor.getMemoryMb());
                rpcFlavorInfo.setFlavorId(tblCmpFlavor.getFlavorid());
                rpcFlavorInfo.setCloudId(tblCmpFlavor.getCloudId());
                rpcFlavorInfo.setProductId(tblCmpFlavor.getFlavorid());
                rpcFlavorInfo.setName(tblCmpFlavor.getName());
                return rpcFlavorInfo;
            }).collect(Collectors.toList());
        }
        return rpcFlavorInfos;
    }

    @Override
    public RpcFlavorInfo getCloudFlavorById(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "flavorId")String flavorId)
    {
        RpcFlavorInfo rpcFlavorInfo = null;
        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
        if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
        {
            TblCmpFlavor tblCmpFlavor = flavorRepository.getFlavorById(cloudId, flavorId);
            if (tblCmpFlavor == null) return rpcFlavorInfo;
            rpcFlavorInfo = new RpcFlavorInfo();
            rpcFlavorInfo.setCpu(tblCmpFlavor.getCpu());
            rpcFlavorInfo.setMem(tblCmpFlavor.getMem() * 1024);
            rpcFlavorInfo.setFlavorId(tblCmpFlavor.getFlavorId());
            rpcFlavorInfo.setCloudId(tblCmpFlavor.getCloudId());
            rpcFlavorInfo.setProductId(tblCmpFlavor.getFlavorId());
            rpcFlavorInfo.setName(tblCmpFlavor.getName());
        }
        else if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()) || tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName()))
        {
            TblCmpOsFlavors tblCmpOsFlavor = osNovaRepository.getFlavorById(cloudId, flavorId);
            if (tblCmpOsFlavor == null) return rpcFlavorInfo;
            rpcFlavorInfo = new RpcFlavorInfo();
            rpcFlavorInfo.setCpu(tblCmpOsFlavor.getVcpus());
            rpcFlavorInfo.setMem(tblCmpOsFlavor.getMemoryMb());
            rpcFlavorInfo.setFlavorId(tblCmpOsFlavor.getFlavorid());
            rpcFlavorInfo.setCloudId(tblCmpOsFlavor.getCloudId());
            rpcFlavorInfo.setProductId(tblCmpOsFlavor.getFlavorid());
            rpcFlavorInfo.setName(tblCmpOsFlavor.getName());
            return rpcFlavorInfo;
        }
        return rpcFlavorInfo;
    }

    @Override
    public List<RpcDiskSpec> getCloudDiskSpec(@ApiParam(name = "cloudId")String cloudId)
    {
        List<RpcDiskSpec> diskSpecs = null;
        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
        if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
        {
            TblCmpStoragePoolExample example = new TblCmpStoragePoolExample();
            TblCmpStoragePoolExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            List<TblCmpStoragePool> tblCmpStoragePools = repoRepository.getStoragePools(example);
            diskSpecs = tblCmpStoragePools.stream().map(tblCmpStoragePool ->{
                RpcDiskSpec rpcDiskSpec = new RpcDiskSpec();
                rpcDiskSpec.setCloudId(cloudId);
                rpcDiskSpec.setDiskSpecId(tblCmpStoragePool.getStoragePoolId());
                rpcDiskSpec.setDiskType(tblCmpStoragePool.getName());
                rpcDiskSpec.setVendor(null);
                rpcDiskSpec.setModel(null);
                rpcDiskSpec.setSize(null);
                rpcDiskSpec.setTransSpeed(null);
                return rpcDiskSpec;
            }).collect(Collectors.toList());
        }
        else if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()) || tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName()))
        {
            TblCmpOsVolumeTypesExample example = new TblCmpOsVolumeTypesExample();
            TblCmpOsVolumeTypesExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            List<TblCmpOsVolumeTypes> tblCmpOsVolumeTypes = osCinderRepository.getVolumeTypes(example);
            diskSpecs = tblCmpOsVolumeTypes.stream().map(tblCmpOsVolumeType ->{
                RpcDiskSpec rpcDiskSpec = new RpcDiskSpec();
                rpcDiskSpec.setCloudId(cloudId);
                rpcDiskSpec.setDiskSpecId(tblCmpOsVolumeType.getId());
                rpcDiskSpec.setDiskType(tblCmpOsVolumeType.getName());
                return rpcDiskSpec;
            }).collect(Collectors.toList());
        }
        return diskSpecs;
    }

    @Override
    public RpcDiskSpec getCloudDiskSpecById(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "diskSpecId")String diskSpecId)
    {
        RpcDiskSpec rpcDiskSpec = null;
        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
        if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
        {
            TblCmpStoragePool tblCmpStoragePool = repoRepository.getStoragePoolById(cloudId, diskSpecId);
            if (tblCmpStoragePool == null) return rpcDiskSpec;
            rpcDiskSpec = new RpcDiskSpec();
            rpcDiskSpec.setCloudId(cloudId);
            rpcDiskSpec.setDiskSpecId(tblCmpStoragePool.getStoragePoolId());
            rpcDiskSpec.setDiskType(tblCmpStoragePool.getName());
            rpcDiskSpec.setVendor(null);
            rpcDiskSpec.setModel(null);
            rpcDiskSpec.setSize(null);
            rpcDiskSpec.setTransSpeed(null);
        }
        else if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()) || tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName()))
        {
            TblCmpOsVolumeTypes tblCmpOsVolumeType = osCinderRepository.getVolumeTypeById(cloudId, diskSpecId);
            if (tblCmpOsVolumeType == null) return rpcDiskSpec;
            rpcDiskSpec = new RpcDiskSpec();
            rpcDiskSpec.setCloudId(cloudId);
            rpcDiskSpec.setDiskSpecId(tblCmpOsVolumeType.getId());
            rpcDiskSpec.setDiskType(tblCmpOsVolumeType.getName());
        }
        return rpcDiskSpec;
    }

    @Override
    public CombRetPacket getCloudAgent(@ApiParam(name = "cloudId")String cloudId)
    {
        CombRetPacket combRetPacket = new CombRetPacket();
        try
        {
            String workId = cloudRepository.selectWorkerIdByCloudId(cloudId);
            combRetPacket.setStatus(ErrorCode.SUCCESS.getCode());
            combRetPacket.setObj(workId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get cloud agent error, {}", e.getMessage());
            combRetPacket.setStatus(ErrorCode.SystemError.getCode());
        }
        return combRetPacket;
    }

    @Override
    public String getCloudName(@ApiParam(name = "cloudId")String cloudId)
    {
        TblCloudInfo cloudInfo = cloudRepository.getCloud(cloudId);
        if (Objects.nonNull(cloudInfo))
        {
            return cloudInfo.getName();
        }
        return "";
    }

    @Override
    public boolean checkOperUserByVmInstanceId(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "vmInstanceId")String vmInstanceId, @ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId)
    {
        if (StringUtils.isEmpty(cloudId) || StringUtils.isEmpty(vmInstanceId))
        {
            return false;
        }
        if (StringUtils.isEmpty(userId) && StringUtils.isEmpty(bpId))
        {
            return false;
        }

        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);

        if (tblCloudInfo == null)
        {
            return false;
        }

        if (cloudService.isOwner(cloudId, userId))
        {
            return true;
        }

        if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
        {
            TblCmpVmInstanceExample example = new TblCmpVmInstanceExample();
            TblCmpVmInstanceExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            if (StringUtils.isNotEmpty(userId))
            {
                criteria.andEeUserEqualTo(userId);
            }
            if (StringUtils.isNotEmpty(bpId))
            {
                criteria.andEeBpEqualTo(bpId);
            }
            criteria.andVmInstanceIdEqualTo(vmInstanceId);

            List<TblCmpVmInstance> tblCmpVmInstances = vmComputeRepository.getVmInstances(example);
            return null != tblCmpVmInstances && tblCmpVmInstances.size() != 0;
        }
        else if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName()) || tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()))
        {
            TblCmpOsInstancesExample example = new TblCmpOsInstancesExample();
            TblCmpOsInstancesExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            if (StringUtils.isNotEmpty(userId))
            {
                criteria.andEeUserEqualTo(userId);
            }
            if (StringUtils.isNotEmpty(bpId))
            {
                criteria.andEeBpEqualTo(bpId);
            }
            criteria.andUuidEqualTo(vmInstanceId);

            List<TblCmpOsInstances> tblCmpOsInstances = osNovaRepository.getInstances(example);
            return null != tblCmpOsInstances && tblCmpOsInstances.size() != 0;
        }
        return false;
    }

    @Override
    public boolean checkOperUserByHypervisorNodeIp(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "hypervisorNodeIp")String hypervisorNodeIp, @ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId)
    {
        if (StringUtils.isEmpty(cloudId) || StringUtils.isEmpty(hypervisorNodeIp))
        {
            return false;
        }
        if (hypervisorNodeIp.contains(":9100"))
        {
            hypervisorNodeIp.replace("9100", "");
        }
        if (StringUtils.isEmpty(userId) && StringUtils.isEmpty(bpId))
        {
            return false;
        }

        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);

        if (tblCloudInfo == null)
        {
            return false;
        }

        if (cloudService.isOwner(cloudId, userId))
        {
            return true;
        }

        if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
        {
            TblCmpHypervisorNodeExample example = new TblCmpHypervisorNodeExample();
            TblCmpHypervisorNodeExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            if (StringUtils.isNotEmpty(userId))
            {
                criteria.andEeUserEqualTo(userId);
            }
            if (StringUtils.isNotEmpty(bpId))
            {
                criteria.andEeBpEqualTo(bpId);
            }
            criteria.andManageIpEqualTo(hypervisorNodeIp);

            List<TblCmpHypervisorNode> tblCmpHypervisorNodes = vmComputeRepository.getHypervisorNodes(example);
            return null != tblCmpHypervisorNodes && tblCmpHypervisorNodes.size() != 0;
        }

        return false;
    }

    @Override
    public String getVmInstanceName(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "vmInstanceId")String vmInstanceId)
    {
        if (StringUtils.isEmpty(cloudId) || StringUtils.isEmpty(vmInstanceId))
        {
            return "";
        }

        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);

        if (tblCloudInfo == null)
        {
            return "";
        }

        if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
        {
            TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, vmInstanceId);

            return null == tblCmpVmInstance ? "" : tblCmpVmInstance.getName();
        }
        else if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName()) || tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()))
        {
            TblCmpOsInstances tblCmpOsInstance = osNovaRepository.getInstanceById(cloudId, vmInstanceId);

            return null == tblCmpOsInstance ? "" : tblCmpOsInstance.getDisplayName();
        }
        return "";
    }

    @Override
    public String getVolumeName(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "volumeId")String volumeId)
    {
        if (StringUtils.isEmpty(cloudId) || StringUtils.isEmpty(volumeId))
        {
            return "";
        }

        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);

        if (tblCloudInfo == null)
        {
            return "";
        }

        if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
        {
            TblCmpVolume tblCmpVolume = repoRepository.getVolumeById(cloudId, volumeId);

            return null == tblCmpVolume ? "" : tblCmpVolume.getName();
        }
        else if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName()) || tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()))
        {
            TblCmpOsVolumes tblCmpOsVolume = osCinderRepository.getVolumeById(cloudId, volumeId);

            return null == tblCmpOsVolume ? "" : tblCmpOsVolume.getDisplayName();
        }
        return "";
    }

    @Override
    public RpcCloudInfos getClouds(@ApiParam(name = "pageNum")Integer pageNum, @ApiParam(name = "pageSize")Integer pageSize)
    {
        try
        {
            if (pageNum == null) pageNum = 1;
            if (pageSize == null) pageSize = 100;

            return cloudRepository.getClouds(pageNum, pageSize);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get rpc clouds error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public int isVmCreateSuccess(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "vmInstanceId")String vmInstanceId)
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);

            if (tblCloudInfo == null)
            {
                return VmCreateStatus.CREATE_FAILED.getCode();
            }

            if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
            {
                TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, vmInstanceId);

                if (tblCmpVmInstance == null || tblCmpVmInstance.getEeStatus() == REMOVED ||
                        tblCmpVmInstance.getPhaseStatus() == NSInstanceStatus.INSTANCE_CREATE_FAILED.getCode() ||
                        tblCmpVmInstance.getPhaseStatus() == NSInstanceStatus.INSTANCE_INJECT_BOOT_FAILED.getCode() ||
                        tblCmpVmInstance.getPhaseStatus() == NSInstanceStatus.INSTANCE_CREATE_FAILED_CLEANING.getCode() ||
                        tblCmpVmInstance.getPhaseStatus() == NSInstanceStatus.INSTANCE_CREATE_FAILED_CLEANED.getCode())
                {
                    return VmCreateStatus.CREATE_FAILED.getCode();
                }
                else if (NSInstanceStatus.CREATING_STATUS.contains(tblCmpVmInstance.getPhaseStatus()))
                {
                    return VmCreateStatus.CREATEING.getCode();
                }
                else
                {
                    return VmCreateStatus.CREATED.getCode();
                }
            }
            else if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName()) || tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()))
            {
                TblCmpOsInstances tblCmpOsInstances = osNovaRepository.getInstanceById(cloudId, vmInstanceId);

                if (tblCmpOsInstances == null || tblCmpOsInstances.getEeStatus() == REMOVED ||
                        tblCmpOsInstances.getStatus().equalsIgnoreCase("ERROR"))
                {
                    return VmCreateStatus.CREATE_FAILED.getCode();
                }
                else if (tblCmpOsInstances.getStatus().equalsIgnoreCase("ACTIVE"))
                {
                    return VmCreateStatus.CREATED.getCode();
                }
                else
                {
                    return VmCreateStatus.CREATEING.getCode();
                }
            }
            else
            {
                return VmCreateStatus.CREATE_FAILED.getCode();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get vm create status error: {}", e.getMessage());
            return VmCreateStatus.CREATE_FAILED.getCode();
        }
    }

    @Override
    public int updateCloudProvider(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "providerId")String providerId)
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            if (tblCloudInfo == null)
            {
                return ErrorCode.CLOUD_NOT_EXIST.getCode();
            }
            tblCloudInfo.setProviderId(providerId);
            cloudRepository.updateCloud(tblCloudInfo);
            return ErrorCode.SUCCESS.getCode();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("update cloud provider error: {}", e.getMessage());
            return ErrorCode.SystemError.getCode();
        }
    }

    @Override
    public List<String> getGpuNames(@ApiParam(name = "cloudIds")List<String> cloudIds, @ApiParam(name = "providerId")String providerId)
    {
        try
        {
            return vmComputeRepository.getGpuNames(cloudIds, providerId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get gpu names error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public RpcGpuResourceMetrics getGpuResourceMetrics(@ApiParam(name = "cloudIds")List<String> cloudIds, @ApiParam(name = "gpuName")String gpuName, @ApiParam(name = "providerId")String providerId, @ApiParam(name = "pageNum")Integer pageNum, @ApiParam(name = "pageSize")Integer pageSize)
    {
        try
        {
            if (pageNum == null) pageNum = 1;
            if (pageSize == null) pageSize = Integer.MAX_VALUE;
            PageHelper.startPage(pageNum, pageSize);
            List<RpcGpuResourceMetric> rpcGpuResourceMetrics = vmComputeRepository.getGpuResourceMetrics(cloudIds, gpuName, providerId);
            PageInfo<RpcGpuResourceMetric> pageInfo = new PageInfo<>(rpcGpuResourceMetrics);

            rpcGpuResourceMetrics.forEach(rpcGpuResourceMetric ->{
                rpcGpuResourceMetric.setIbTotal(vmComputeRepository.getIbTotalByCloudIdAndGpu(rpcGpuResourceMetric.getCloudId(), rpcGpuResourceMetric.getGpuName()));
                rpcGpuResourceMetric.setAvailableIbCount(vmComputeRepository.getAvailableIbCountByCloudIdAndGpu(rpcGpuResourceMetric.getCloudId(), rpcGpuResourceMetric.getGpuName()));
            });

            return RpcGpuResourceMetrics.builder().totalNum(pageInfo.getTotal())
                    .gpuResourceMetrics(rpcGpuResourceMetrics).build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get cloud resource metrics error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public RpcGpuResourceMetric getGpuResourceMetric(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "gpuName")String gpuName)
    {
        try
        {
            List<String> cloudIds = new ArrayList<>();
            cloudIds.add(cloudId);
            List<RpcGpuResourceMetric> rpcGpuResourceMetrics = vmComputeRepository.getGpuResourceMetrics(cloudIds, gpuName, null);

            rpcGpuResourceMetrics.forEach(rpcGpuResourceMetric ->{
                rpcGpuResourceMetric.setIbTotal(vmComputeRepository.getIbTotalByCloudIdAndGpu(rpcGpuResourceMetric.getCloudId(), rpcGpuResourceMetric.getGpuName()));
                rpcGpuResourceMetric.setAvailableIbCount(vmComputeRepository.getAvailableIbCountByCloudIdAndGpu(rpcGpuResourceMetric.getCloudId(), rpcGpuResourceMetric.getGpuName()));
            });

            return rpcGpuResourceMetrics.get(0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get cloud resource metric error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public RpcGpuUsages getGpuUsage(@ApiParam(name = "cloudIds")List<String> cloudIds, @ApiParam(name = "gpuName")String gpuName, @ApiParam(name = "providerId")String providerId)
    {
        try
        {
            RpcGpuUsages rpcGpuUsages = new RpcGpuUsages();
            Map<String, RpcGpuUsage> gpuUsageMap = new HashMap();
            List<RpcGpuResourceMetric> rpcGpuResourceMetrics = vmComputeRepository.getGpuResourceMetrics(cloudIds, gpuName, providerId);

            rpcGpuResourceMetrics.forEach(rpcGpuResourceMetric -> {
                rpcGpuUsages.setGpuTotal(rpcGpuUsages.getGpuTotal() + rpcGpuResourceMetric.getGpuTotal());
                rpcGpuUsages.setAvailableGpuCount(rpcGpuUsages.getAvailableGpuCount() + rpcGpuResourceMetric.getAvailableGpuCount());

                RpcGpuUsage rpcGpuUsage = gpuUsageMap.getOrDefault(rpcGpuResourceMetric.getGpuName(), new RpcGpuUsage(rpcGpuResourceMetric.getGpuName(), 0, 0));
                rpcGpuUsage.setGpuTotal(rpcGpuUsage.getGpuTotal() + rpcGpuResourceMetric.getGpuTotal());
                rpcGpuUsage.setAvailableGpuCount(rpcGpuUsage.getAvailableGpuCount() + rpcGpuResourceMetric.getAvailableGpuCount());
                gpuUsageMap.put(rpcGpuResourceMetric.getGpuName(), rpcGpuUsage);
            });

            rpcGpuUsages.setGpuUsages(gpuUsageMap);
            return rpcGpuUsages;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get gpu usage error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public RpcGpuVmInstances getGpuVmInstances(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "gpuName")String gpuName, @ApiParam(name = "pageNum")Integer pageNum, @ApiParam(name = "pageSize")Integer pageSize)
    {
        try
        {
            if (pageNum == null) pageNum = 1;
            if (pageSize == null) pageSize = Integer.MAX_VALUE;
            PageHelper.startPage(pageNum, pageSize);
            List<RpcGpuVmInstance> rpcGpuVmInstances = vmComputeRepository.getVmInstanceInfoByCloudIdAndGpu(cloudId, gpuName);
            PageInfo<RpcGpuVmInstance> pageInfo = new PageInfo<>(rpcGpuVmInstances);

            rpcGpuVmInstances.forEach(rpcGpuVmInstance -> {
                rpcGpuVmInstance.setBpName(combRpcService.getUmsService().getBpNameByBpId(rpcGpuVmInstance.getBpId()));
                rpcGpuVmInstance.setUserName(combRpcService.getUmsService().getUserNameByUserId(rpcGpuVmInstance.getUserId()));
            });

            return RpcGpuVmInstances.builder().totalNum(pageInfo.getTotal())
                    .vmInstances(rpcGpuVmInstances).build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get vm instance by cloud and gpu error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<RpcEipPool> getCloudEipPool(@ApiParam(name = "cloudId")String cloudId)
    {
        List<RpcEipPool> rpcEipPools = null;
        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
        if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
        {
            TblCmpEipPoolExample example = new TblCmpEipPoolExample();
            TblCmpEipPoolExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            List<TblCmpEipPool> tblCmpEipPools = networkRepository.getEipPools(example);
            rpcEipPools = tblCmpEipPools.stream().map(tblCmpEipPool ->{
                RpcEipPool rpcEipPool = new RpcEipPool();
                rpcEipPool.setCloudId(cloudId);
                rpcEipPool.setPoolId(tblCmpEipPool.getPoolId());
                rpcEipPool.setName(tblCmpEipPool.getName());
                rpcEipPool.setDescription(tblCmpEipPool.getDescription());
                return rpcEipPool;
            }).collect(Collectors.toList());
        }
        else if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()) || tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName()))
        {

        }
        return rpcEipPools;
    }

    @Override
    public RpcEipPool getCloudEipPoolById(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "poolId")String poolId)
    {
        RpcEipPool rpcEipPool = null;
        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
        if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
        {
            TblCmpEipPool tblCmpEipPool = networkRepository.getEipPoolById(cloudId, poolId);
            if (tblCmpEipPool == null) return rpcEipPool;
            rpcEipPool = new RpcEipPool();
            rpcEipPool.setCloudId(cloudId);
            rpcEipPool.setPoolId(tblCmpEipPool.getPoolId());
            rpcEipPool.setName(tblCmpEipPool.getName());
            rpcEipPool.setDescription(tblCmpEipPool.getDescription());
        }
        else if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()) || tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName()))
        {

        }
        return rpcEipPool;
    }

    @Override
    public String getEipAddr(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "eipId")String eipId)
    {
        if (StringUtils.isEmpty(cloudId) || StringUtils.isEmpty(eipId))
        {
            return "";
        }

        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);

        if (tblCloudInfo == null)
        {
            return "";
        }

        if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
        {
            TblCmpEip tblCmpEip = networkRepository.getEipById(cloudId, eipId);

            return null == tblCmpEip ? "" : tblCmpEip.getPublicIp() == null ? tblCmpEip.getIpaddr() : tblCmpEip.getPublicIp();
        }
        else if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName()) || tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()))
        {

        }
        return "";
    }
}
