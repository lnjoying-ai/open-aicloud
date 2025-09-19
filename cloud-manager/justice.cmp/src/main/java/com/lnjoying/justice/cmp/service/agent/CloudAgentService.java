package com.lnjoying.justice.cmp.service.agent;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.lnjoying.justice.cmp.common.*;
import com.lnjoying.justice.cmp.config.CloudManagerConfig;
import com.lnjoying.justice.cmp.db.model.TblCloudAgentInfo;
import com.lnjoying.justice.cmp.db.model.TblCloudAgentInfoExample;
import com.lnjoying.justice.cmp.db.model.TblCloudInfo;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.service.process.CloudMsgProcessStrategy;
import com.lnjoying.justice.cmp.service.rpc.CombRpcService;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.schema.constant.OnlineStatus;
import com.lnjoying.justice.schema.constant.WorkerType;
import com.lnjoying.justice.schema.entity.dev.*;
import com.lnjoying.justice.schema.entity.docker.DockerInstParams;
import com.lnjoying.justice.schema.entity.docker.LogConfig;
import com.lnjoying.justice.schema.entity.docker.RestartPolicy;
import com.lnjoying.justice.schema.entity.edgeif.EdgeWorkerIfState;
import com.lnjoying.justice.schema.entity.edgeif.EdgeWorkerIfStateInfos;
import com.lnjoying.justice.schema.entity.scheduler.AssignEdge2InstReq;
import com.lnjoying.justice.schema.entity.scheduler.BindRelation;
import com.lnjoying.justice.schema.entity.scheduler.BindResourcesResult;
import com.lnjoying.justice.schema.entity.scheduler.SchedulerResult;
import com.lnjoying.justice.schema.msg.EeCtrlMessageDef;
import com.lnjoying.justice.schema.msg.EeNetMessageApi;
import com.lnjoying.justice.schema.msg.EeWorkerDef;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.common.Pair;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class CloudAgentService
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private CloudMsgProcessStrategy cloudMsgProcessStrategy;

    @Autowired
    private CombRpcService combRpcService;

    @Autowired
    private CloudManagerConfig cloudManagerConfig;

    public int addWorker(EeWorkerDef.msg_worker_reg_req_body workerRegReqBody)
    {
        try
        {
            TblCloudAgentInfo tblCloudAgentInfo = cloudRepository.getCloudAgentByPrimaryKey(workerRegReqBody.getToken());
            if (tblCloudAgentInfo != null)
            {

                TblCloudInfo tblCloudInfo = cloudRepository.getCloud(tblCloudAgentInfo.getCloudId());

                if (tblCloudInfo == null || tblCloudInfo.getStatus().equals(CloudStatus.REMOVED.getCode()))
                {
                    return ErrorCode.CLOUD_NOT_EXIST.getCode();
                }

                if (tblCloudAgentInfo.getWorkerId() != null && !tblCloudAgentInfo.getWorkerId().equals(workerRegReqBody.getWorkerId()))
                {
                    return ErrorCode.WORKER_REG_TOKEN_INVALID.getCode();
                }
                else
                {
                    tblCloudAgentInfo.setOnlineStatus(OnlineStatus.ONLINE);
                    tblCloudAgentInfo.setWorkerId(workerRegReqBody.getWorkerId());
                    tblCloudAgentInfo.setUpdateTime(tblCloudAgentInfo.getCreateTime());
                    cloudRepository.updateCloudAgentInfo(tblCloudAgentInfo);


                    if (tblCloudInfo.getStatus() == CloudStatus.MAINTAINING.getCode() || tblCloudInfo.getStatus() == CloudStatus.MAINTAIN_FILED.getCode())
                    {
                        tblCloudInfo.setStatus(CloudStatus.MAINTAINING.getCode());
                    }
                    else
                    {
                        tblCloudInfo.setStatus(CloudStatus.INTERNAL_TEST.getCode());
                    }
                    cloudRepository.updateCloud(tblCloudInfo);

                    RedisUtil.zadd(RedisCache.CLOUD_HEALTH_IDS, tblCloudInfo.getCloudId(), new Date().getTime());
                    RedisUtil.zadd(RedisCache.CLOUD_SYNC_IDS, tblCloudInfo.getCloudId(), new Date().getTime());

                    return ErrorCode.SUCCESS.getCode();
                }
            }
            else
            {
                return ErrorCode.CLOUD_AGENT_DROPPED.getCode();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ErrorCode.PARAM_ERROR.getCode();
        }
    }

    public int workerLogin(EeWorkerDef.msg_worker_login_req_body workerLoginReqBody)
    {
        TblCloudAgentInfo tblCloudAgentInfo = cloudRepository.getCloudAgentByPrimaryKey(workerLoginReqBody.getWorkerId());
        if (tblCloudAgentInfo == null)
        {
            return ErrorCode.WORKER_NOT_EXIST.getCode();
        }

        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(tblCloudAgentInfo.getCloudId());

        if (tblCloudInfo == null || tblCloudInfo.getStatus().equals(CloudStatus.REMOVED.getCode()))
        {
            return ErrorCode.CLOUD_NOT_EXIST.getCode();
        }

        if (StringUtils.isEmpty(tblCloudAgentInfo.getCoreVersion())
                || !workerLoginReqBody.getCoreVersion().equals(tblCloudAgentInfo.getCoreVersion()))
        {
            tblCloudAgentInfo.setCoreVersion(workerLoginReqBody.getCoreVersion());
            tblCloudAgentInfo.setUpdateTime(new Date());
            cloudRepository.updateCloudAgentInfo(tblCloudAgentInfo);
        }

        return ErrorCode.SUCCESS.getCode();
    }

    public void processWorkerIFState(Pair<String, EeNetMessageApi.ee_net_message> ifState)
    {
        String gwNodeId = ifState.getKey();
        EeNetMessageApi.ee_net_message netMessage = ifState.getValue();
        EeCtrlMessageDef.edge_worker_if_state edgeWorkerIfState = netMessage.getCloudCtrlBody().getSyncWorkerIfStateReqBody().getWorkerIfState();

        TblCloudAgentInfo tblCloudAgentInfo = cloudRepository.getCloudAgentByPrimaryKey(edgeWorkerIfState.getWorkerId());
        if (tblCloudAgentInfo == null)
        {
            return;
        }

        EdgeWorkerIfStateInfos devIfStates = (EdgeWorkerIfStateInfos) RedisUtil.oget(RedisCache.WORKER_IF, edgeWorkerIfState.getWorkerId());
        if (devIfStates == null)
        {
            LOGGER.error("worker if state is error");
            return;
        }

        int status = OnlineStatus.OFFLINE;
        for (Map.Entry<String, EdgeWorkerIfState> entry : devIfStates.getEdgeWorkerIfStateMap().entrySet())
        {
            if (entry.getValue().getStatus() != OnlineStatus.ONLINE)
            {
                continue;
            }
            else
            {
                status = OnlineStatus.ONLINE;
            }
        }

        boolean needUpdateAgent = false;
        if (tblCloudAgentInfo.getOnlineStatus() == null || status != tblCloudAgentInfo.getOnlineStatus())
        {
            tblCloudAgentInfo.setOnlineStatus(edgeWorkerIfState.getIfStatus());
            needUpdateAgent = true;
        }

        if (StringUtils.isEmpty(tblCloudAgentInfo.getCoreVersion())
                || !edgeWorkerIfState.getCoreVersion().equals(tblCloudAgentInfo.getCoreVersion()))
        {
            tblCloudAgentInfo.setCoreVersion(edgeWorkerIfState.getCoreVersion());
            needUpdateAgent = true;
        }

        if (needUpdateAgent)
        {
            tblCloudAgentInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
            cloudRepository.updateCloudAgentInfo(tblCloudAgentInfo);
        }

        if (edgeWorkerIfState.getIfStatus() == OnlineStatus.ONLINE)
        {
            RedisUtil.sadd(RedisCache.REGION_GW_WORKER + edgeWorkerIfState.getRegionId() + ":", gwNodeId, edgeWorkerIfState.getWorkerId());
        }
        else
        {
            RedisUtil.srem(RedisCache.REGION_GW_WORKER + edgeWorkerIfState.getRegionId() + ":", gwNodeId, edgeWorkerIfState.getWorkerId());
        }
    }

    public void processImportCloud(String cloudId)
    {
        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
        if (tblCloudInfo.getStatus() != CloudStatus.CREATED.getCode())
        {
            return;
        }
        tblCloudInfo.setStatus(CloudStatus.IMPORTING.getCode());
        tblCloudInfo.setUpdateTime(new Date());
        cloudRepository.updateCloud(tblCloudInfo);

        TblCloudAgentInfo tblCloudAgentInfo = createCloudAgent(tblCloudInfo);

        assignCloudAgent(tblCloudAgentInfo);
    }

    public void processUpdateCloud(String cloudId)
    {
        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
        if (tblCloudInfo.getStatus() == CloudStatus.REMOVED.getCode())
        {
            return;
        }
        tblCloudInfo.setUpdateTime(new Date());
        cloudRepository.updateCloud(tblCloudInfo);

        TblCloudAgentInfoExample example = new TblCloudAgentInfoExample();
        TblCloudAgentInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotIn(InstanceState.removeStatusList);
        criteria.andCloudIdEqualTo(cloudId);
        List<TblCloudAgentInfo> tblCloudAgentInfos = cloudRepository.getCloudAgents(example);

        for (TblCloudAgentInfo tblCloudAgentInfo : tblCloudAgentInfos)
        {
            combRpcService.getInstService().removeContainerByInstId(tblCloudAgentInfo.getAgentId());
        }

        TblCloudAgentInfo tblCloudAgentInfo = createCloudAgent(tblCloudInfo);

        assignCloudAgent(tblCloudAgentInfo);
    }

    public void processDeleteCloud(String cloudId)
    {
        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
        if (tblCloudInfo.getStatus() == CloudStatus.REMOVED.getCode())
        {
            return;
        }

        TblCloudAgentInfoExample example = new TblCloudAgentInfoExample();
        TblCloudAgentInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotIn(InstanceState.removeStatusList);
        criteria.andCloudIdEqualTo(cloudId);
        List<TblCloudAgentInfo> tblCloudAgentInfos = cloudRepository.getCloudAgents(example);

        if (CollectionUtils.isEmpty(tblCloudAgentInfos))
        {
            tblCloudInfo.setStatus(CloudStatus.REMOVED.getCode());
            cloudRepository.updateCloud(tblCloudInfo);
        }

        boolean rpcRemove = false;

        for (TblCloudAgentInfo tblCloudAgentInfo : tblCloudAgentInfos)
        {
            if (tblCloudAgentInfo.getStatus() < InstanceState.FWD.getCode())
            {
                tblCloudAgentInfo.setStatus(InstanceState.FIN_CLOUD_REMOVE.getCode());
                cloudRepository.updateCloudAgentInfo(tblCloudAgentInfo);
                continue;
            }
            combRpcService.getInstService().removeContainerByInstId(tblCloudAgentInfo.getAgentId());
            rpcRemove = true;
        }
        if (!rpcRemove)
        {
            tblCloudInfo.setStatus(CloudStatus.REMOVED.getCode());
            cloudRepository.updateCloud(tblCloudInfo);
        }
    }

    public TblCloudAgentInfo createCloudAgent(TblCloudInfo tblCloudInfo)
    {
        TblCloudAgentInfo tblCloudAgentInfo = new TblCloudAgentInfo();
        CloudProduct cloudProduct = CloudProduct.fromName(tblCloudInfo.getProduct());
        tblCloudAgentInfo.setAgentId(Utils.buildStr("CMP_", cloudProduct.getShortName(), "_",Utils.assignUUId()));
        tblCloudAgentInfo.setCloudId(tblCloudInfo.getCloudId());
        tblCloudAgentInfo.setStatus(InstanceState.MAKED.getCode());
        tblCloudAgentInfo.setOnlineStatus(OnlineStatus.OFFLINE);
        tblCloudAgentInfo.setCreateTime(new Date());
        tblCloudAgentInfo.setUpdateTime(tblCloudAgentInfo.getCreateTime());

        cloudRepository.insertCloudAgent(tblCloudAgentInfo);

        return tblCloudAgentInfo;
    }

    public void assignCloudAgent(TblCloudAgentInfo tblCloudAgentInfo)
    {
        try
        {
            if (tblCloudAgentInfo.getStatus() != InstanceState.MAKED.getCode())
            {
                return;
            }
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(tblCloudAgentInfo.getCloudId());
            if (tblCloudInfo == null || tblCloudInfo.getStatus() == CloudStatus.CREATED.getCode())
            {
                return;
            }

            AssignEdge2InstReq assignEdge2InstReq = new AssignEdge2InstReq();

            assignEdge2InstReq.setName(tblCloudInfo.getName());
            assignEdge2InstReq.setSpecId(Utils.buildStr("CMP_", tblCloudInfo.getCloudId()));

            CpuNeed cpuNeed = new CpuNeed();
            cpuNeed.setCpuNum(0);
            MemNeed memNeed = new MemNeed();
            memNeed.setMemLimit(0);
            DiskNeed diskNeed = new DiskNeed();
            diskNeed.setDiskLimit(0);
            NetworkBandNeed networkBandNeed = new NetworkBandNeed();
            networkBandNeed.setRecvBand(0);
            networkBandNeed.setTransmitBand(0);
            DevNeedInfo devNeedInfo = new DevNeedInfo(cpuNeed, memNeed, diskNeed, networkBandNeed, null);

            assignEdge2InstReq.setDev_need_info(devNeedInfo);
            assignEdge2InstReq.setOn_one_node(0);
            assignEdge2InstReq.setTarget_nodes(Lists.newArrayList(new TargetNode(tblCloudInfo.getRegionId(), tblCloudInfo.getSiteId(), tblCloudInfo.getNodeId())));
            assignEdge2InstReq.setReplica_num(1);
            assignEdge2InstReq.setAuto_run(true);

            assignEdge2InstReq.setBp_id(tblCloudInfo.getBp());
            assignEdge2InstReq.setUser_id(tblCloudInfo.getOwner());
            assignEdge2InstReq.setInst_type("docker");
            assignEdge2InstReq.setWaitAssignIdList(Lists.newArrayList(tblCloudAgentInfo.getAgentId()));

            assignEdge2InstReq.setScheduling_strategy(new SchedulingStrategy());

            Integer allocStatus = combRpcService.getSchedulerService().allocEdge2InstResources(assignEdge2InstReq);
            if (null != allocStatus && allocStatus == SchedulerState.WAITING_SCHEDULING.getCode())
            {
                tblCloudAgentInfo.setStatus(InstanceState.WAIT_ASSIGN.getCode());
            }
            else
            {
                tblCloudAgentInfo.setStatus(InstanceState.CLOUD_SYSTEM_ABNORMAL.getCode());
            }
            cloudRepository.updateCloudAgentInfo(tblCloudAgentInfo);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("assign cloud agent {} error, msg {}", tblCloudAgentInfo.getAgentId(), e.getMessage());
        }
    }

    public BindResourcesResult bindResources(SchedulerResult schedulerResult)
    {
        BindResourcesResult bindResourcesResult = new BindResourcesResult();
        bindResourcesResult.setSuccessBindRelations(new ArrayList<>());
        bindResourcesResult.setFailBindRelations(new ArrayList<>());

        if (null == schedulerResult)
        {
            return null;
        }
        else
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(schedulerResult.getSpecId().replace("CMP_", ""));
            if (tblCloudInfo == null)
            {
                return null;
            }

            Date date = new Date();

            if (schedulerResult.getSchedulerState() == SchedulerState.SUCCESS.getCode()
                    && null != schedulerResult.getBindRelations()
                    && schedulerResult.getBindRelations().size() != 0)
            {
                for (BindRelation bindRelation :schedulerResult.getBindRelations())
                {
                    TblCloudAgentInfo tblCloudAgentInfo = cloudRepository.getCloudAgentByPrimaryKey(bindRelation.getWaitAssignId());
                    if (tblCloudAgentInfo == null || tblCloudAgentInfo.getStatus() != InstanceState.WAIT_ASSIGN.getCode())
                    {
                        bindResourcesResult.getFailBindRelations().add(bindRelation);
                        continue;
                    }
                    tblCloudAgentInfo.setNodeId(bindRelation.getDstNodeId());
                    tblCloudAgentInfo.setSiteId(bindRelation.getDstSiteId());
                    tblCloudAgentInfo.setRegionId(bindRelation.getDstRegionId());

                    tblCloudAgentInfo.setStatus(InstanceState.ASSIGNED.getCode());

                    tblCloudAgentInfo.setUpdateTime(date);
                    cloudRepository.updateCloudAgentInfo(tblCloudAgentInfo);
                    bindResourcesResult.getSuccessBindRelations().add(bindRelation);

                    MessagePack messagePack = new MessagePack();
                    messagePack.setMsgType(CloudMsg.CREATE);
                    messagePack.setMessageObj(tblCloudAgentInfo.getAgentId());
                    cloudMsgProcessStrategy.sendMessage(messagePack);
                }
            }
            else if (SchedulerState.noMatchNodeSchedulerStatusSet.contains(schedulerResult.getSchedulerState()))
            {
                if (tblCloudInfo.getStatus() == CloudStatus.MAINTAINING.getCode())
                {
                    tblCloudInfo.setStatus(CloudStatus.MAINTAIN_FILED.getCode());
                }
                else
                {
                    tblCloudInfo.setStatus(CloudStatus.IMPORT_FILED.getCode());
                }
            }
            else if (schedulerResult.getSchedulerState() >= SchedulerState.UNSUPPORTED_PRODUCT_TYPE.getCode()
                    && schedulerResult.getSchedulerState() <= SchedulerState.SCHEDULER_ERROR.getCode())
            {
                if (tblCloudInfo.getStatus() == CloudStatus.MAINTAINING.getCode())
                {
                    tblCloudInfo.setStatus(CloudStatus.MAINTAIN_FILED.getCode());
                }
                else
                {
                    tblCloudInfo.setStatus(CloudStatus.IMPORT_FILED.getCode());
                }
            }
            else
            {
                if (tblCloudInfo.getStatus() == CloudStatus.MAINTAINING.getCode())
                {
                    tblCloudInfo.setStatus(CloudStatus.MAINTAIN_FILED.getCode());
                }
                else
                {
                    tblCloudInfo.setStatus(CloudStatus.IMPORT_FILED.getCode());
                }
            }
            tblCloudInfo.setUpdateTime(date);
            cloudRepository.updateCloud(tblCloudInfo);
        }

        return bindResourcesResult;
    }

    public void processCreateCloudAgent(String agentId)
    {
        try
        {
            TblCloudAgentInfo tblCloudAgentInfo = cloudRepository.getCloudAgentByPrimaryKey(agentId);
            TargetNode targetNode = new TargetNode(tblCloudAgentInfo.getRegionId(), tblCloudAgentInfo.getSiteId(), tblCloudAgentInfo.getNodeId());
            String gateway = combRpcService.getEdgeResourceService().getOnlineGwByRegion(tblCloudAgentInfo.getRegionId());

            DockerInstParams dockerInstParams = new DockerInstParams();
            dockerInstParams.setImage(cloudManagerConfig.getCloudAgentImage());
            List<String> envs = new ArrayList<>();
            envs.add(String.format("WORKER_TYPE=%s", WorkerType.CLOUD_AGENT));
            dockerInstParams.setEnv(envs);
            dockerInstParams.setCmd(new String[] {"--reg_token", agentId, "--region", tblCloudAgentInfo.getRegionId(), "--gateway", gateway});
            LogConfig logConfig = new LogConfig();
            logConfig.setType("json-file");
            Map<String, String> logMap = new HashMap<>();
            logMap.put("max-size", "100m");
            logMap.put("max-file", "3");
            logConfig.setConfig(logMap);
            dockerInstParams.getHostConfig().setLogConfig(logConfig);
            RestartPolicy restartPolicy = new RestartPolicy();
            restartPolicy.setName("always");
            dockerInstParams.getHostConfig().setRestartPolicy(restartPolicy);
            dockerInstParams.getHostConfig().setBinds(Arrays.asList("/etc/hosts:/etc/hosts"));
            dockerInstParams.getHostConfig().setNetworkMode("host");

            combRpcService.getInstService().createContainerWithInstId(agentId, targetNode, JsonUtils.toJson(dockerInstParams), agentId, null, true,null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("create cloud agent {} error, msg {}", agentId, e.getMessage());
        }
    }
}
