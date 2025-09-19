package com.lnjoying.justice.ecrm.facade;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.ecrm.common.constant.EcrmMsgType;
import com.lnjoying.justice.ecrm.common.constant.InstanceState;
import com.lnjoying.justice.ecrm.common.constant.RedisCache;
import com.lnjoying.justice.ecrm.config.EcrmConfig;
import com.lnjoying.justice.ecrm.db.model.TblServiceAgentInfo;
import com.lnjoying.justice.ecrm.db.model.TblServiceAgentInfoExample;
import com.lnjoying.justice.ecrm.db.model.TblSiteInfo;
import com.lnjoying.justice.ecrm.db.repo.EdgeRepository;
import com.lnjoying.justice.ecrm.db.repo.RegionRepository;
import com.lnjoying.justice.ecrm.db.repo.ServiceAgentRepository;
import com.lnjoying.justice.ecrm.db.repo.SiteRepository;
import com.lnjoying.justice.ecrm.domain.dto.model.ServiceAgent;
import com.lnjoying.justice.ecrm.domain.dto.request.AddServiceAgentReq;
import com.lnjoying.justice.ecrm.domain.dto.response.GetServiceAgentsRsp;
import com.lnjoying.justice.ecrm.domain.model.search.ServiceAgentSearchCritical;
import com.lnjoying.justice.ecrm.process.service.EcrmMsgProcessStrategy;
import com.lnjoying.justice.ecrm.service.CombRpcService;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.schema.constant.ActiveStatus;
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
import com.lnjoying.justice.schema.service.ecrm.EdgeResourceService;
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
import java.util.stream.Collectors;

@Service
public class ServiceAgentService
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ServiceAgentRepository serviceAgentRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private EdgeRepository edgeRepository;

    @Autowired
    private CombRpcService combRpcService;

    @Autowired
    private EdgeResourceService edgeResourceService;

    @Autowired
    private EcrmConfig ecrmConfig;

    @Autowired
    private EcrmMsgProcessStrategy ecrmMsgProcessStrategy;

    public int addServiceAgent(AddServiceAgentReq addServiceAgentReq)
    {
        if (CollectionUtils.isEmpty(addServiceAgentReq.getTargetNodes()))
        {
            return ErrorCode.PARAM_ERROR.getCode();
        }

        TargetNode targetNode = addServiceAgentReq.getTargetNodes().get(0);

        TblServiceAgentInfo tblServiceAgentInfo = new TblServiceAgentInfo();
        tblServiceAgentInfo.setAgentId(Utils.buildStr("ECRM_SERCIVEAGENT_",Utils.assignUUId()));
        tblServiceAgentInfo.setTargetNodes(JsonUtils.toJson(addServiceAgentReq.getTargetNodes()));
        tblServiceAgentInfo.setType(WorkerType.SERVICE_AGENT);
        tblServiceAgentInfo.setDescription(addServiceAgentReq.getDescription());
        tblServiceAgentInfo.setOnlineStatus(OnlineStatus.OFFLINE);
        tblServiceAgentInfo.setCoreVersion(null);
        tblServiceAgentInfo.setRegionId(targetNode.getDstRegionId());
        tblServiceAgentInfo.setSiteId(targetNode.getDstSiteId());
        tblServiceAgentInfo.setNodeId(targetNode.getDstNodeId());
        tblServiceAgentInfo.setStatus(InstanceState.MAKED.getCode());
        tblServiceAgentInfo.setCreateTime(new Date());
        tblServiceAgentInfo.setImage(addServiceAgentReq.getImage());
        tblServiceAgentInfo.setUpdateTime(tblServiceAgentInfo.getCreateTime());
        serviceAgentRepository.insertServiceAgent(tblServiceAgentInfo);

        assignServiceAgent(tblServiceAgentInfo);

        return ErrorCode.SUCCESS.getCode();
    }

    public int deleteServiceAgent(String agentId)
    {
        TblServiceAgentInfo tblServiceAgentInfo = serviceAgentRepository.getServiceAgent(agentId);
        if (tblServiceAgentInfo == null || tblServiceAgentInfo.getStatus() == InstanceState.FIN_CLOUD_REMOVE.getCode())
        {
            return ErrorCode.WORKER_NOT_EXIST.getCode();
        }
        CombRetPacket combRetPacket = combRpcService.getInstService().removeContainerByInstId(tblServiceAgentInfo.getAgentId());
        if (combRetPacket.getStatus() == InstanceState.REMOVED.getCode())
        {
            tblServiceAgentInfo.setStatus(InstanceState.FIN_CLOUD_REMOVE.getCode());
            tblServiceAgentInfo.setUpdateTime(new Date());
            serviceAgentRepository.updateServiceAgentSelective(tblServiceAgentInfo);
        }

        return ErrorCode.SUCCESS.getCode();
    }

    public GetServiceAgentsRsp getServiceAgents(ServiceAgentSearchCritical agentSearchCritical)
    {
        GetServiceAgentsRsp getServiceAgentsRsp = new GetServiceAgentsRsp();
        TblServiceAgentInfoExample example = new TblServiceAgentInfoExample();
        TblServiceAgentInfoExample.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotEmpty(agentSearchCritical.getRegionId())) criteria.andRegionIdEqualTo(agentSearchCritical.getRegionId());
        if(StringUtils.isNotEmpty(agentSearchCritical.getSiteId())) criteria.andSiteIdEqualTo(agentSearchCritical.getSiteId());
        if(StringUtils.isNotEmpty(agentSearchCritical.getNodeId())) criteria.andNodeIdEqualTo(agentSearchCritical.getNodeId());
        if(StringUtils.isNotEmpty(agentSearchCritical.getAgentId())) criteria.andAgentIdEqualTo(agentSearchCritical.getAgentId());
        criteria.andStatusNotEqualTo(InstanceState.FIN_CLOUD_REMOVE.getCode());

        PageHelper.startPage(agentSearchCritical.getPageNum(), agentSearchCritical.getPageSize());
        List<TblServiceAgentInfo> serviceAgentInfos = serviceAgentRepository.getServiceAgentsByExample(example);
        PageInfo<TblServiceAgentInfo> pageInfo = new PageInfo<>(serviceAgentInfos);

        List<ServiceAgent> serviceAgents = serviceAgentInfos.stream().map(tblServiceAgentInfo -> {
            ServiceAgent serviceAgent = ServiceAgent.of(tblServiceAgentInfo);
            serviceAgent.setName(regionRepository.getRegion(serviceAgent.getRegionId()), siteRepository.getSite(serviceAgent.getSiteId()), edgeRepository.getEdgeNode(serviceAgent.getNodeId()));
            return serviceAgent;
        }).collect(Collectors.toList());

        getServiceAgentsRsp.setServiceAgents(serviceAgents);
        getServiceAgentsRsp.setTotalNum(pageInfo.getTotal());

        return getServiceAgentsRsp;
    }

    private void assignServiceAgent(TblServiceAgentInfo tblServiceAgentInfo)
    {
        try
        {
            if (tblServiceAgentInfo.getStatus() != InstanceState.MAKED.getCode())
            {
                return;
            }
            TblSiteInfo tblSiteInfo = siteRepository.getSite(tblServiceAgentInfo.getSiteId());
            if (tblSiteInfo == null || tblSiteInfo.getStatus() == ActiveStatus.REMOVED)
            {
                return;
            }

            AssignEdge2InstReq assignEdge2InstReq = new AssignEdge2InstReq();

            assignEdge2InstReq.setName(tblSiteInfo.getSiteName());
            assignEdge2InstReq.setSpecId(Utils.buildStr("ECRM_", tblSiteInfo.getSiteId()));

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
            assignEdge2InstReq.setTarget_nodes(Lists.newArrayList(new TargetNode(tblServiceAgentInfo.getRegionId(), tblServiceAgentInfo.getSiteId(), tblServiceAgentInfo.getNodeId())));
            assignEdge2InstReq.setReplica_num(1);
            assignEdge2InstReq.setAuto_run(true);

            assignEdge2InstReq.setInst_type("docker");
            assignEdge2InstReq.setWaitAssignIdList(Lists.newArrayList(tblServiceAgentInfo.getAgentId()));

            assignEdge2InstReq.setScheduling_strategy(new SchedulingStrategy());

            Integer allocStatus = combRpcService.getSchedulerService().allocEdge2InstResources(assignEdge2InstReq);
            if (null != allocStatus && allocStatus == SchedulerState.WAITING_SCHEDULING.getCode())
            {
                tblServiceAgentInfo.setStatus(InstanceState.WAIT_ASSIGN.getCode());
            }
            else
            {
                tblServiceAgentInfo.setStatus(InstanceState.CLOUD_SYSTEM_ABNORMAL.getCode());
            }
            serviceAgentRepository.updateServiceAgentSelective(tblServiceAgentInfo);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("assign service agent {} error, msg {}", tblServiceAgentInfo.getAgentId(), e.getMessage());
        }
    }

    public int addWorker(EeWorkerDef.msg_worker_reg_req_body workerRegReqBody)
    {
        try
        {
            TblServiceAgentInfo tblServiceAgentInfo = serviceAgentRepository.getServiceAgent(workerRegReqBody.getToken());
            if (tblServiceAgentInfo != null)
            {

                TblSiteInfo tblSiteInfo = siteRepository.getSite(tblServiceAgentInfo.getSiteId());

                if (tblSiteInfo == null || tblSiteInfo.getStatus().equals(ActiveStatus.REMOVED))
                {
                    return ErrorCode.SITE_REMOVED.getCode();
                }

                if (tblServiceAgentInfo.getWorkerId() != null && !tblServiceAgentInfo.getWorkerId().equals(workerRegReqBody.getWorkerId()))
                {
                    return ErrorCode.WORKER_REG_TOKEN_INVALID.getCode();
                }
                else
                {
                    tblServiceAgentInfo.setOnlineStatus(OnlineStatus.ONLINE);
                    tblServiceAgentInfo.setWorkerId(workerRegReqBody.getWorkerId());
                    tblServiceAgentInfo.setUpdateTime(new Date());
                    serviceAgentRepository.updateServiceAgentSelective(tblServiceAgentInfo);

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
        TblServiceAgentInfo tblServiceAgentInfo = serviceAgentRepository.getServiceAgent(workerLoginReqBody.getWorkerId());
        if (tblServiceAgentInfo == null)
        {
            return ErrorCode.WORKER_NOT_EXIST.getCode();
        }

        TblSiteInfo tblSiteInfo = siteRepository.getSite(tblServiceAgentInfo.getSiteId());

        if (tblSiteInfo == null || tblSiteInfo.getStatus().equals(ActiveStatus.REMOVED))
        {
            return ErrorCode.SITE_REMOVED.getCode();
        }

        if (StringUtils.isEmpty(tblServiceAgentInfo.getCoreVersion())
                || !workerLoginReqBody.getCoreVersion().equals(tblServiceAgentInfo.getCoreVersion()))
        {
            tblServiceAgentInfo.setCoreVersion(workerLoginReqBody.getCoreVersion());
            tblServiceAgentInfo.setUpdateTime(new Date());
            serviceAgentRepository.updateServiceAgent(tblServiceAgentInfo);
        }

        return ErrorCode.SUCCESS.getCode();
    }

    public void processWorkerIFState(Pair<String, EeNetMessageApi.ee_net_message> ifState)
    {
        String gwNodeId = ifState.getKey();
        EeNetMessageApi.ee_net_message netMessage = ifState.getValue();
        EeCtrlMessageDef.edge_worker_if_state edgeWorkerIfState = netMessage.getCloudCtrlBody().getSyncWorkerIfStateReqBody().getWorkerIfState();

        TblServiceAgentInfo tblServiceAgentInfo = serviceAgentRepository.getServiceAgent(edgeWorkerIfState.getWorkerId());
        if (tblServiceAgentInfo == null)
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
        if (tblServiceAgentInfo.getOnlineStatus() == null || status != tblServiceAgentInfo.getOnlineStatus())
        {
            tblServiceAgentInfo.setOnlineStatus(edgeWorkerIfState.getIfStatus());
            needUpdateAgent = true;
        }

        if (StringUtils.isEmpty(tblServiceAgentInfo.getCoreVersion())
                || !edgeWorkerIfState.getCoreVersion().equals(tblServiceAgentInfo.getCoreVersion()))
        {
            tblServiceAgentInfo.setCoreVersion(edgeWorkerIfState.getCoreVersion());
            needUpdateAgent = true;
        }

        if (needUpdateAgent)
        {
            tblServiceAgentInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
            serviceAgentRepository.updateServiceAgentSelective(tblServiceAgentInfo);
        }

        if (edgeWorkerIfState.getIfStatus() == OnlineStatus.ONLINE)
        {
            //gw and worker relation, use for worker state check.
            RedisUtil.sadd(RedisCache.REGION_GW_WORKER + edgeWorkerIfState.getRegionId() + ":", gwNodeId, edgeWorkerIfState.getWorkerId());
        }
        else
        {
            //gw and worker relation, use for worker state check.
            RedisUtil.srem(RedisCache.REGION_GW_WORKER + edgeWorkerIfState.getRegionId() + ":", gwNodeId, edgeWorkerIfState.getWorkerId());
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
            TblSiteInfo tblSiteInfo = siteRepository.getSite(schedulerResult.getSpecId().replace("ECRM_", ""));
            if (tblSiteInfo == null)
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
                    TblServiceAgentInfo tblServiceAgentInfo = serviceAgentRepository.getServiceAgent(bindRelation.getWaitAssignId());
                    if (tblServiceAgentInfo == null || tblServiceAgentInfo.getStatus() != InstanceState.WAIT_ASSIGN.getCode())
                    {
                        //1 container has been delete
                        //2 container not wait assign
                        bindResourcesResult.getFailBindRelations().add(bindRelation);
                        continue;
                    }
                    tblServiceAgentInfo.setNodeId(bindRelation.getDstNodeId());
                    tblServiceAgentInfo.setSiteId(bindRelation.getDstSiteId());
                    tblServiceAgentInfo.setRegionId(bindRelation.getDstRegionId());

                    tblServiceAgentInfo.setStatus(InstanceState.ASSIGNED.getCode());

                    tblServiceAgentInfo.setUpdateTime(date);
                    serviceAgentRepository.updateServiceAgent(tblServiceAgentInfo);
                    bindResourcesResult.getSuccessBindRelations().add(bindRelation);

                    MessagePack messagePack = new MessagePack();
                    messagePack.setMsgType(EcrmMsgType.CREATE_SERVICE_AGENT);
                    messagePack.setMessageObj(tblServiceAgentInfo.getAgentId());
                    ecrmMsgProcessStrategy.sendMessage(messagePack);
                }
            }
            else if (SchedulerState.noMatchNodeSchedulerStatusSet.contains(schedulerResult.getSchedulerState()))
            {
                //set spec status: no match node
                schedulerResult.getBindRelations().forEach(bindRelation -> {
                    TblServiceAgentInfo tblServiceAgentInfo = serviceAgentRepository.getServiceAgent(bindRelation.getWaitAssignId());
                    tblServiceAgentInfo.setStatus(InstanceState.NO_MATCH_NODE.getCode());
                    tblServiceAgentInfo.setUpdateTime(new Date());
                    serviceAgentRepository.updateServiceAgent(tblServiceAgentInfo);
                });
            }
            else if (schedulerResult.getSchedulerState() >= SchedulerState.UNSUPPORTED_PRODUCT_TYPE.getCode()
                    && schedulerResult.getSchedulerState() <= SchedulerState.SCHEDULER_ERROR.getCode())
            {
                //set spec status: sched param check error
                schedulerResult.getBindRelations().forEach(bindRelation -> {
                    TblServiceAgentInfo tblServiceAgentInfo = serviceAgentRepository.getServiceAgent(bindRelation.getWaitAssignId());
                    tblServiceAgentInfo.setStatus(InstanceState.CLOUD_CRETAE_FAILED_PARAMS.getCode());
                    tblServiceAgentInfo.setUpdateTime(new Date());
                    serviceAgentRepository.updateServiceAgent(tblServiceAgentInfo);
                });
            }
            else
            {
                //set spec status: cloud system abnormal
                schedulerResult.getBindRelations().forEach(bindRelation -> {
                    TblServiceAgentInfo tblServiceAgentInfo = serviceAgentRepository.getServiceAgent(bindRelation.getWaitAssignId());
                    tblServiceAgentInfo.setStatus(InstanceState.CLOUD_SYSTEM_ABNORMAL.getCode());
                    tblServiceAgentInfo.setUpdateTime(new Date());
                    serviceAgentRepository.updateServiceAgent(tblServiceAgentInfo);
                });
            }
        }

        return bindResourcesResult;
    }

    public void processCreateServiceAgent(String agentId)
    {
        try
        {
            TblServiceAgentInfo tblServiceAgentInfo = serviceAgentRepository.getServiceAgent(agentId);
            TargetNode targetNode = new TargetNode(tblServiceAgentInfo.getRegionId(), tblServiceAgentInfo.getSiteId(), tblServiceAgentInfo.getNodeId());
            String gateway = edgeResourceService.getOnlineGwByRegion(tblServiceAgentInfo.getRegionId());

            DockerInstParams dockerInstParams = new DockerInstParams();
            dockerInstParams.setImage(StringUtils.isEmpty(tblServiceAgentInfo.getImage()) ? ecrmConfig.getServiceAgentImage() : tblServiceAgentInfo.getImage());
            List<String> envs = new ArrayList<>();
            envs.add(String.format("WORKER_TYPE=%s", WorkerType.SERVICE_AGENT));
            dockerInstParams.setEnv(envs);
            dockerInstParams.setCmd(new String[] {"--reg_token", agentId, "--region", tblServiceAgentInfo.getRegionId(), "--gateway", gateway});
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
//            dockerInstParams.setEntrypoint(new String[] {"java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5004", "-jar", "com.justice.agent.jar"});
//            dockerInstParams.getHostConfig().setNetworkMode("host");

            combRpcService.getInstService().createContainerWithInstId(agentId, targetNode, JsonUtils.toJson(dockerInstParams), agentId, null, true,null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("create service agent {} error, msg {}", agentId, e.getMessage());
        }
    }
}
