 package com.lnjoying.justice.cis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;
import com.lnjoying.justice.cis.common.constant.*;
import com.lnjoying.justice.cis.config.WSConfig;
import com.lnjoying.justice.cis.controller.dto.request.*;
import com.lnjoying.justice.cis.controller.dto.response.*;
import com.lnjoying.justice.cis.db.model.*;
import com.lnjoying.justice.cis.db.repo.CfgRepository;
import com.lnjoying.justice.cis.db.repo.CisRepository;
import com.lnjoying.justice.cis.domain.dto.req.UpdateContainersCfgReq;
import com.lnjoying.justice.cis.domain.model.ConfigInfo;
import com.lnjoying.justice.cis.domain.model.ContainerConfig;
import com.lnjoying.justice.cis.facade.NetMessageServiceFacade;
import com.lnjoying.justice.cis.handler.actiondescription.i18n.zh_cn.CisContainerInstActionDescriptionTemplate;
import com.lnjoying.justice.cis.handler.actiondescription.i18n.zh_cn.CisContainerSpecActionDescriptionTemplate;
import com.lnjoying.justice.cis.handler.resourcesupervisor.CisContainerInstResourceSupervisor;
import com.lnjoying.justice.cis.handler.resourcesupervisor.CisContainerSpecResourceSupervisor;
import com.lnjoying.justice.cis.handler.resourcesupervisor.statedict.InstanceStateDesProvider;
import com.lnjoying.justice.cis.process.service.SpecMsgProcessStrategy;
import com.lnjoying.justice.cis.webserver.ContainerLogService;
import com.lnjoying.justice.cis.webserver.ContainerShellService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.operationevent.model.BizModelStateInfo;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionTemplateFields;
import com.lnjoying.justice.commonweb.util.DeepCopyUtils;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.commonweb.util.TemplateEngineUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.common.EventType;
import com.lnjoying.justice.schema.common.scheduler.LabelType;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.schema.constant.CfgStatus;
import com.lnjoying.justice.schema.entity.TipMessage;
import com.lnjoying.justice.schema.entity.dev.*;
import com.lnjoying.justice.schema.entity.scheduler.AssignEdge2InstReq;
import com.lnjoying.justice.schema.msg.*;
import com.lnjoying.justice.schema.service.ims.ImsRegistryService;
import com.lnjoying.justice.schema.service.scheduler.SchedulerService;
import com.lnjoying.justice.schema.service.sys.SysService;
import com.micro.core.common.Pair;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cis.common.constant.CisLabels.*;
import static com.lnjoying.justice.cis.common.constant.InstanceState.*;
import static com.lnjoying.justice.cis.controller.dto.response.DockerContainerDeployInfo.DeployStage.*;
import static com.lnjoying.justice.cis.domain.model.ConfigInfo.CONFIG_PREFIX;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;
import static com.lnjoying.justice.schema.common.ErrorCode.*;
import static com.lnjoying.justice.schema.common.ErrorLevel.ERROR;
import static com.lnjoying.justice.schema.common.ErrorLevel.INFO;
import static com.lnjoying.justice.schema.constant.CfgStatus.SYNCING;
import static com.lnjoying.justice.schema.constant.CfgStatus.SYNCING_RESTART;
import static com.lnjoying.justice.schema.constant.ConfigConstants.*;
import static com.lnjoying.justice.schema.msg.MessageName.FILE_CREATE_REQ;
import static com.lnjoying.justice.schema.msg.MessageName.FILE_OPERATOR;

 @Service("CisManagerService")
public class CisManagerService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CisManagerService.class);

    @Autowired
    private CisRepository cisRepository;

    @RpcReference(microserviceName = "ecss", schemaId = "schedulerService")
    private SchedulerService schedulerService;

    @RpcReference(microserviceName = "ims", schemaId = "imsRegistryService")
    private ImsRegistryService imsService;

    @Autowired
    private WSConfig wsConfig;

    @Autowired
    NetMessageServiceFacade netMessageServiceFacade;

    @Autowired
    LabelProperty labelProperty;

    @Autowired
    private CombRpcService combRpcService;

    @Autowired
    private CfgRepository cfgRepository;

    @Autowired
    SpecMsgProcessStrategy specMsgProcessStrategy;

    @Autowired
    private CisContainerSpecResourceSupervisor cisContainerSpecResourceSupervisor;

    @Autowired
    private CisContainerInstResourceSupervisor cisContainerInstResourceSupervisor;

    private static final Set<Integer> cloudRemoveStatusSet = Sets.newHashSet(InstanceState.SPAWNED_CLOUD_REMOVE.getCode(), InstanceState.FIN_CLOUD_REMOVE.getCode());
    private static final Set<Integer> edgeRemoveStatusSet = Sets.newHashSet(InstanceState.REMOVED.getCode(), InstanceState.OBJECT_NOT_EXIST.getCode(),
            InstanceState.OBJECT_AUTO_REMOVE.getCode());
    private static final Set<Integer> createFailStatusSet = Sets.newHashSet(InstanceState.EDGE_UNREACHABLE.getCode(), InstanceState.NO_MATCH_NODE.getCode(),
            InstanceState.CLOUD_CRETAE_FAILED_PARAMS.getCode(), InstanceState.CLOUD_CRETAE_FAILED_OVERDUE.getCode(), CREATE_FAILED.getCode(),
            InstanceState.NO_IMAGE.getCode());
    private static final List<Integer> cloudRemoveStatusList = Lists.newArrayList(InstanceState.SPAWNED_CLOUD_REMOVE.getCode(), InstanceState.FIN_CLOUD_REMOVE.getCode());

    public Map<String, String> createContainerInst(CreateContainerInstReq req, String bpId, String userId)
    {
        try
        {
            boolean configCorrectly = checkBindConfig(req.getInst_params());
            if (!configCorrectly)
            {
                throw new WebSystemException(CONFIG_PARAM_ERROR, ERROR);
            }

            //check request params
            if (null == req.getInst_params() || null == req.getInst_params().get("Image"))
            {
                throw new WebSystemException(ErrorCode.IMAGE_EMPTY, ErrorLevel.INFO);
            }

            ObjectNode instParams =req.getInst_params();
            String imageName = instParams.get("Image").asText();
            String cmd = null != instParams.get("Cmd")?instParams.get("Cmd").toString():null;

            DevNeedInfo devNeedInfo = req.getDev_need_info();

            TblContainerSpecInfo tblContainerSpecInfo = new TblContainerSpecInfo();
            tblContainerSpecInfo.setSpecId(Utils.buildStr("JST_DCS_",Utils.assignUUId()));
            tblContainerSpecInfo.setSpecName(req.getName());
            tblContainerSpecInfo.setImageName(imageName);
            tblContainerSpecInfo.setRegistryId(req.getRegistry_id());
            tblContainerSpecInfo.setCmd(cmd);
            SchedulingStrategy schedulingStrategy = new SchedulingStrategy();
            if (null != req.getScheduling_strategy())
            {
                schedulingStrategy.setLabelSelectorMap(req.getScheduling_strategy().getLabelSelectorMap());
                schedulingStrategy.setStrategy(req.getScheduling_strategy().getStrategy());
            }
            else
            {
                schedulingStrategy.setLabelSelectorMap(new HashMap<>());
            }
            List<LabelSelector> nodeLabelSelector = schedulingStrategy.getLabelSelectorMap().getOrDefault(LabelType.NODE.getValue(), new ArrayList<>());
            nodeLabelSelector.add(new LabelSelector("Must", "In", labelProperty.getNodeOrchestration(), "docker"));
            schedulingStrategy.getLabelSelectorMap().put(LabelType.NODE.getValue(), nodeLabelSelector);
            schedulingStrategy.setReplicaCompleteStrategy(true);
            schedulingStrategy.setOnOneNode(req.getOn_one_node());
            schedulingStrategy.setTargetNodes(req.getTarget_nodes());
            tblContainerSpecInfo.setSchedulingStrategy(JsonUtils.toJson(schedulingStrategy));
            tblContainerSpecInfo.setCpuNum(req.getDev_need_info().getCpu().getCpuNum());
            tblContainerSpecInfo.setMemLimit(req.getDev_need_info().getMem().getMemLimit().longValue());
            tblContainerSpecInfo.setDiskLimit(req.getDev_need_info().getDisk().getDiskLimit().longValue());
            if (null != req.getDev_need_info().getNetworkBandNeed())
            {
                tblContainerSpecInfo.setTransmitBandLimit(req.getDev_need_info().getNetworkBandNeed().getTransmitBand());
                tblContainerSpecInfo.setRecvBandLimit(req.getDev_need_info().getNetworkBandNeed().getRecvBand());
            }
            if (null!=devNeedInfo && null!=devNeedInfo.getGpu() && null!=devNeedInfo.getGpu().getGpuNum())
            {
                tblContainerSpecInfo.setGpuNum(req.getDev_need_info().getGpu().getGpuNum());
            }
            tblContainerSpecInfo.setDevNeedInfo(JsonUtils.toJson(req.getDev_need_info()));
            tblContainerSpecInfo.setContainerParams(req.getInst_params().toString());
            tblContainerSpecInfo.setAutoRun(req.isAuto_run());
            tblContainerSpecInfo.setReplicaNum(req.getReplica_num());
            if (! StringUtils.isEmpty(req.getBp_id()))
            {
                tblContainerSpecInfo.setBpId(req.getBp_id());
            }
            else if (! StringUtils.isEmpty(bpId))
            {
                tblContainerSpecInfo.setBpId(bpId);
            }
            if (! StringUtils.isEmpty(req.getUser_id()))
            {
                tblContainerSpecInfo.setUserId(req.getUser_id());
            }
            else
            {
                tblContainerSpecInfo.setUserId(userId);
            }
            if (schedulingStrategy.getTargetNodes() != null && ! schedulingStrategy.getTargetNodes().isEmpty()  && req.getReplica_num() > 0)
            {
                tblContainerSpecInfo.setStatus(InstanceState.SPAWNED.getCode());
            }
            else
            {
                tblContainerSpecInfo.setStatus(InstanceState.MAKED.getCode());
            }
            tblContainerSpecInfo.setRestartPolicy(req.getRestart_policy());
            tblContainerSpecInfo.setFailoverPolicy(JsonUtils.toJson(req.getFailover_policy()));
            tblContainerSpecInfo.setCreateTime(new Date());
            tblContainerSpecInfo.setUpdateTime(tblContainerSpecInfo.getCreateTime());

            if (!CollectionUtils.isEmpty(req.getExtra_params()))
            {
                tblContainerSpecInfo.setExtraParams(JsonUtils.toJson(req.getExtra_params()));
            }
            cisRepository.insertSpec(tblContainerSpecInfo);

            Map<String, String> ret = new HashMap<>();
            ret.put("id", tblContainerSpecInfo.getSpecId());
            //发布资源创建事件
            publishContainerSpecInfoCreateEvent(tblContainerSpecInfo, "createContainerInst");
            return ret;
        }
        catch (DuplicateKeyException e)
        {
            throw new WebSystemException(ErrorCode.INST_DUP, ErrorLevel.INFO);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    private void publishContainerSpecInfoCreateEvent(TblContainerSpecInfo tblContainerSpecInfo, String action)
    {
        try
        {
            cisContainerSpecResourceSupervisor.publishCreateEvent(tblContainerSpecInfo,
                    TemplateEngineUtils.render0(CisContainerSpecActionDescriptionTemplate.Descriptions.ADD_CONTAINER_DEPLOY,
                            false,
                            TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME, tblContainerSpecInfo.getSpecName()),
                            TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE, tblContainerSpecInfo)), null,
                    action);
        } catch (Exception e)
        {
            LOGGER.error("发布添加容器事件失败! stackTrace:{}, errorMessage:{}", e.getMessage());
        }
    }

    public GetContainerInstInfosRsp getContainerInstInfos(ContainerSearchCritical critical)
    {
        try
        {
            GetContainerInstInfosRsp rsp = new GetContainerInstInfosRsp();

            TblContainerInstInfoExample example = new TblContainerInstInfoExample();
            TblContainerInstInfoExample.Criteria criteria = example.createCriteria();

            if (null == critical.getStatus())
            {
                criteria.andStatusNotIn(cloudRemoveStatusList);
            }
            else if (critical.getStatus() < 1100)
            {
                criteria.andStatusEqualTo(critical.getStatus());
            }
            else
            {
                criteria.andStatusIn(InstanceState.simpleMap.get(critical.getStatus()));
            }

            if (critical.getNodeId() != null) criteria.andNodeIdEqualTo(critical.getNodeId());
            if (critical.getSite() != null) criteria.andSiteIdEqualTo(critical.getSite());
            if (critical.getRegion() != null) criteria.andRegionIdEqualTo(critical.getRegion());
            if (critical.getSpecId() != null) criteria.andSpecIdEqualTo(critical.getSpecId());

            String bpId = queryBpId();
            String userId = queryUserId();
            if (StringUtils.isNotBlank(bpId))
            {
                criteria.andBpIdEqualTo(bpId);
            }

            if (StringUtils.isNotBlank(userId))
            {
                criteria.andUserIdEqualTo(userId);
            }

            criteria.andSpecIdIsNotNull();

            //count container instances
            long total_num = cisRepository.countByExample(example);
            if (total_num < 1)
            {
                LOGGER.info("get container instance infos empty: {}", critical);
                rsp.setContainers(Collections.EMPTY_LIST);
                return rsp;
            }
            rsp.setTotal_num((int)total_num);

            //get container instances
            int begin = 0;
            int pageSize = 10;
            if (critical.getPageSize() != null && critical.getPageNum() != null && critical.getPageNum() >= 1 )
            {
                begin = (critical.getPageNum() - 1) * critical.getPageSize();
                example.setStartRow(begin);

                pageSize = critical.getPageSize();
                example.setPageSize(pageSize);
            }

            example.setOrderByClause("create_time desc");

            List<TblContainerInstInfo> records = cisRepository.getContainerInstInfosByExample(example);
            if (records == null || records.isEmpty())
            {
                LOGGER.info("get container instance infos empty: {}", critical);
                return rsp;
            }

            List<DockerContainerInfo> containers = new ArrayList<>();
            for (TblContainerInstInfo record : records)
            {
                DockerContainerInfo container = getDockerContainerInfo(record);

                container.setRestartPolicy(record.getRestartPolicy());

                containers.add(container);
            }

            rsp.setContainers(containers);
            return rsp;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    private DockerContainerInfo getDockerContainerInfo(TblContainerInstInfo record)
    {
        DockerContainerInfo container = new DockerContainerInfo();

        container.setName(record.getContainerName());
        container.setId(record.getInstId());
        container.setContainerId("");
        container.setImageName(record.getImageName());
        container.setStatus(InstanceState.fromCode(record.getStatus()).toStatusCode());

        //container.setIp("");
        if (null != record.getCmd() && !record.getCmd().isEmpty())
        {
            List<String> cmdList = new ArrayList<>();
            container.setCmd(JsonUtils.fromJson(record.getCmd(), cmdList.getClass()));
        }

        assembleUserInfo(record, container);
        assembleDstNode(record, container);

        container.setCreateTime(Utils.formatDate(record.getCreateTime()));
        if (null != record.getRemoveTime())
        {
            container.setStopTime(Utils.formatDate(record.getRemoveTime()));
        }

        generateInstTipMessage(container, record.getEventType(), record.getReportTime());

        TblContainerSpecInfo spec = cisRepository.getSpec(record.getSpecId());
        String registryUrl = getRegistryUrlBySpec(spec);
        if (StringUtils.isNotBlank(registryUrl))
        {
            container.setImageName(registryUrl + "/" + container.getImageName());
        }
        container.setRefId(record.getRefId());
        return container;
    }

    public long containerLifeMng(String instanceId, String action)
    {
        try
        {
            //check params
            TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(instanceId);
            if (null == tblContainerInstInfo)
            {
                LOGGER.error("container life mng get container instance info error: {}", action);
                return 0;
            }
            //记录原始状态
            TblContainerInstInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblContainerInstInfo);
            if (action.equals(InstAction.REMOVE.getName()))
            {
                Integer x = doRemove(tblContainerInstInfo);
                if (x != null) return x;
            }
            else if (action.equals(InstAction.STOP.getName()))
            {
                //need not to stop
                if (tblContainerInstInfo.getStatus() < InstanceState.SPAWNED.getCode()
                        || cloudRemoveStatusSet.contains(tblContainerInstInfo.getStatus())
                        || edgeRemoveStatusSet.contains(tblContainerInstInfo.getStatus())
                        || createFailStatusSet.contains(tblContainerInstInfo.getStatus()))
                {
                    return 0;
                }

                tblContainerInstInfo.setStatus(InstanceState.SPAWN_USER_STOP_QUIT.getCode());
            }

            tblContainerInstInfo.setUpdateTime(new Date());
            cisRepository.updateByPrimaryKey(tblContainerInstInfo);
            //发布资源更新事件
            CisContainerInstActionDescriptionTemplate actionDescriptionTemplate = new CisContainerInstActionDescriptionTemplate(action);
            if (actionDescriptionTemplate.getDescriptionTemplate().isPresent())
            {
                cisContainerInstResourceSupervisor.publishUpdateEvent("容器控制",
                        null, true,
                        beforeUpdateEntity, tblContainerInstInfo, "containerLifeMng",
                        TemplateEngineUtils.render0(actionDescriptionTemplate.getDescriptionTemplate().get(),
                                false,
                                TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
                                        tblContainerInstInfo.getContainerName())));
            }

            return netMessageServiceFacade.lifeMngInstReq(instanceId, tblContainerInstInfo.getRefId(), action, tblContainerInstInfo.getNodeId());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    public DockerContainerInfo getContainerInstInfo(String instanceId)
    {
        if (StringUtils.isNotBlank(instanceId))
        {
            TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByInstIdOrRefId(instanceId);

            if (Objects.nonNull(tblContainerInstInfo))
            {
                DockerContainerInfo dockerContainerInfo = getDockerContainerInfo(tblContainerInstInfo);
                DockerContainerInfoDetail dockerContainerInfoDetail = new DockerContainerInfoDetail();
                BeanUtils.copyProperties(dockerContainerInfo, dockerContainerInfoDetail);

                TblContainerSpecInfo spec = cisRepository.getSpec(tblContainerInstInfo.getSpecId());
                try
                {
                    Pair<List<String>, List<ContainerConfig>> pair = convert(spec.getContainerParams());
                    dockerContainerInfoDetail.setContainerConfigs(pair.getRight());
                    dockerContainerInfoDetail.setContainerVolumes(pair.getLeft());
                }
                catch (Exception e)
                {
                    LOGGER.error("parse container config error:{}", e);
                }

                return dockerContainerInfoDetail;
            }
        }

        return null;
    }

    public Object getContainerInstStats(String instanceId)
    {
        return null;
    }

    public RemoteExecCmdRsp remoteExecute(RemoteExecCmdReq req)
    {
        try
        {
            TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(req.getInstId());
            if (null == tblContainerInstInfo)
            {
                throw new WebSystemException(ErrorCode.INST_DROPPED, ErrorLevel.ERROR);
            }

            if (!tblContainerInstInfo.getStatus().equals(InstanceState.RUNNING.getCode()))
            {
                throw new WebSystemException(ErrorCode.INST_CANNOT_REMOTE_EXECUTE, ErrorLevel.ERROR);
            }

            RemoteExecCmdRsp rsp = new RemoteExecCmdRsp();

            //check params

            //generate execid
            String execId = Utils.assignUUId();

            //add web socket token
            ContainerShellService.addWsTokens(execId, req);

            //RemoteExecCmdRsp
            rsp.setToken(execId);
            String startUrl = wsConfig.getUrlPrefix() + "exec/" +  execId;
            rsp.setUrl(startUrl);

            return rsp;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    public LogContainerInstRsp logFollowInst(String instanceId, boolean flow, int lines)
    {
        try
        {
            LogContainerInstReq logContainerInstReq = new LogContainerInstReq();
            logContainerInstReq.setInstId(instanceId);
            logContainerInstReq.setFlow(flow);
            logContainerInstReq.setHead_or_tail("tail");
            logContainerInstReq.setLines(lines);

            LogContainerInstRsp rsp = new LogContainerInstRsp();

            //generate logid
            String execId = Utils.assignUUId();

            //add web socket token
            ContainerLogService.addWsTokens(execId, logContainerInstReq);

            //LogContainerInstRsp
            rsp.setToken(execId);
            String startUrl = wsConfig.getUrlPrefix() + "logs/" +  execId;
            rsp.setUrl(startUrl);

            return rsp;
        }
        catch(Exception e)
        {
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    /**
     * schedule spec inst
     * @param specId
     */
    public void processScheduleInst(String specId)
    {
        TblContainerSpecInfo tblContainerSpecInfo = cisRepository.getSpec(specId);
        List<String> waitAssignIdList = Collections.emptyList();
        List<TblContainerInstInfo> waitAssignInstList = Collections.emptyList();
        if (tblContainerSpecInfo == null)
        {
            return;
        }

        if (tblContainerSpecInfo.getStatus() != InstanceState.SPAWNED.getCode())
        {
            return;
        }

        //记录原始状态
        TblContainerSpecInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblContainerSpecInfo);

        try
        {
            AssignEdge2InstReq assignEdge2InstReq = new AssignEdge2InstReq();
            assignEdge2InstReq.setName(tblContainerSpecInfo.getSpecName());
            assignEdge2InstReq.setDev_need_info(JsonUtils.fromJson(tblContainerSpecInfo.getDevNeedInfo(), DevNeedInfo.class));
            SchedulingStrategy schedulingStrategy = JsonUtils.fromJson(tblContainerSpecInfo.getSchedulingStrategy(), SchedulingStrategy.class);
            assignEdge2InstReq.setOn_one_node(schedulingStrategy.getOnOneNode());
            assignEdge2InstReq.setTarget_nodes(schedulingStrategy.getTargetNodes());
            assignEdge2InstReq.setScheduling_strategy(schedulingStrategy);
            assignEdge2InstReq.setReplica_num(tblContainerSpecInfo.getReplicaNum());
            assignEdge2InstReq.setInst_params(tblContainerSpecInfo.getContainerParams());
            assignEdge2InstReq.setAuto_run(tblContainerSpecInfo.getAutoRun());
            assignEdge2InstReq.setBp_id(tblContainerSpecInfo.getBpId());
            assignEdge2InstReq.setUser_id(tblContainerSpecInfo.getUserId());
            assignEdge2InstReq.setInst_type("docker");

            //gengrate container inst
            waitAssignInstList = genContainerInst(tblContainerSpecInfo);
            waitAssignIdList = waitAssignInstList.stream().map(x -> x.getInstId()).collect(Collectors.toList());

            assignEdge2InstReq.setWaitAssignIdList(waitAssignIdList);
            assignEdge2InstReq.setSpecId(specId);

            //alloc resources, send task to scheduler
            Integer allocStatus = schedulerService.allocEdge2InstResources(assignEdge2InstReq);
            if (null != allocStatus && allocStatus == SchedulerState.WAITING_SCHEDULING.getCode())
            {
                tblContainerSpecInfo.setStatus(InstanceState.WAIT_ASSIGN.getCode());
            }
            else
            {
                tblContainerSpecInfo.setStatus(InstanceState.CLOUD_SYSTEM_ABNORMAL.getCode());
                cisRepository.updateInstsStatus(waitAssignIdList, tblContainerSpecInfo.getStatus());
                publishContainerInstanceStatusUpdateEvent(tblContainerSpecInfo, waitAssignInstList, "processScheduleInst");
            }

        }
        catch (JsonSyntaxException e)
        {
            LOGGER.info("spec: {} params error", specId);
            tblContainerSpecInfo.setStatus(InstanceState.CLOUD_CRETAE_FAILED_PARAMS.getCode());
            cisRepository.updateInstsStatus(waitAssignIdList, tblContainerSpecInfo.getStatus());
            publishContainerInstanceStatusUpdateEvent(tblContainerSpecInfo, waitAssignInstList, "processScheduleInst");
            e.printStackTrace();
        }
        catch (IllegalStateException e)
        {
            LOGGER.error("instance of {} has been generated but microservice error, waiting for resend schedule request", specId);
            tblContainerSpecInfo.setStatus(InstanceState.WAIT_ASSIGN.getCode());
            e.printStackTrace();
        }
        finally
        {
            tblContainerSpecInfo.setUpdateTime(new Date());
            cisRepository.updateSpec(tblContainerSpecInfo);
            //记录资源更新事件
            publishContainerSpecStatusUpdateEvent(tblContainerSpecInfo, beforeUpdateEntity, "processScheduleInst");
        }
    }

    private void publishContainerSpecStatusUpdateEvent(TblContainerSpecInfo tblContainerSpecInfo, TblContainerSpecInfo beforeUpdateEntity, String action)
    {
        try
        {
            if (Objects.equals(tblContainerSpecInfo.getStatus(), beforeUpdateEntity.getStatus()))
            {
                LOGGER.debug("容器部署 {} 状态未发生变化", beforeUpdateEntity.getSpecName());
                return;
            }

            Map<Integer, BizModelStateInfo> instanceStateDesDict = InstanceStateDesProvider.INSTANCE.getStateDesDict().get(InstanceStateDesProvider.STATUS_FIELD);
            BizModelStateInfo stateInfo = instanceStateDesDict.get(tblContainerSpecInfo.getStatus());
            cisContainerSpecResourceSupervisor.publishUpdateEvent("容器部署状态更新",
                    tblContainerSpecInfo.getSpecName(), true,
                    beforeUpdateEntity, tblContainerSpecInfo, action,
                    TemplateEngineUtils.render0(CisContainerSpecActionDescriptionTemplate.Descriptions.UPDATE_SPEC_STATUS,
                            false,
                            TemplateEngineUtils.newEntry("status", Optional.ofNullable(stateInfo)
                                    .map(x -> x.getCnDescription())
                                    .orElse(Optional.ofNullable(tblContainerSpecInfo.getStatus())
                                            .map(x->x.toString())
                                            .orElse(""))),
                            TemplateEngineUtils.newEntry("spec_name", tblContainerSpecInfo.getSpecName()),
                            TemplateEngineUtils.newEntry("spec_id", tblContainerSpecInfo.getSpecId())));
        } catch (Exception e)
        {
            LOGGER.error("发布容器部署状态更新事件失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    private void publishContainerInstanceStatusUpdateEvent(TblContainerSpecInfo tblContainerSpecInfo, List<TblContainerInstInfo> waitAssignInstList, String action)
    {
        try
        {
            for (TblContainerInstInfo beforeUpdateContainerInstEntity : waitAssignInstList)
            {
                if (Objects.equals(tblContainerSpecInfo.getStatus(), beforeUpdateContainerInstEntity.getStatus()))
                {
                    LOGGER.debug("容器 {} 状态未生变化，忽略", beforeUpdateContainerInstEntity.getContainerName());
                    continue;
                }
                TblContainerInstInfo tblContainerInstInfo = DeepCopyUtils.deepCopy(beforeUpdateContainerInstEntity);
                tblContainerInstInfo.setStatus(tblContainerSpecInfo.getStatus());

                publishContainerInstanceStatusUpdateEvent(tblContainerSpecInfo, tblContainerInstInfo, beforeUpdateContainerInstEntity, action);
            }
        } catch (Exception e)
        {
            LOGGER.error("发布容器状态更新事件失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }


    /**
     * retry schedule inst
     * @param instId
     */
    public void processReScheduleInst(String instId)
    {
        TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(instId);
        if (tblContainerInstInfo == null)
        {
            return;
        }

        if (tblContainerInstInfo.getStatus() != InstanceState.WAIT_ASSIGN.getCode())
        {
            return;
        }

        TblContainerInstInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblContainerInstInfo);
        TblContainerSpecInfo tblContainerSpecInfo = cisRepository.getSpec(tblContainerInstInfo.getSpecId());
        if (tblContainerSpecInfo == null)
        {
            // can not find spec
            tblContainerInstInfo.setStatus(InstanceState.CLOUD_CRETAE_FAILED_PARAMS.getCode());
            tblContainerInstInfo.setUpdateTime(new Date());
            cisRepository.updateByPrimaryKey(tblContainerInstInfo);

            //记录资源更新事件
            publishContainerInstanceStatusUpdateEvent(tblContainerSpecInfo, tblContainerInstInfo, beforeUpdateEntity, "processReScheduleInst");
        }
        else
        {
            try
            {
                AssignEdge2InstReq assignEdge2InstReq = new AssignEdge2InstReq();
                assignEdge2InstReq.setName(tblContainerSpecInfo.getSpecName());
                assignEdge2InstReq.setDev_need_info(JsonUtils.fromJson(tblContainerSpecInfo.getDevNeedInfo(), DevNeedInfo.class));
                SchedulingStrategy schedulingStrategy = JsonUtils.fromJson(tblContainerSpecInfo.getSchedulingStrategy(), SchedulingStrategy.class);
                assignEdge2InstReq.setOn_one_node(schedulingStrategy.getOnOneNode());
                assignEdge2InstReq.setTarget_nodes(schedulingStrategy.getTargetNodes());
                assignEdge2InstReq.setScheduling_strategy(schedulingStrategy);
                assignEdge2InstReq.setReplica_num(tblContainerSpecInfo.getReplicaNum());
                assignEdge2InstReq.setInst_params(tblContainerSpecInfo.getContainerParams());
                assignEdge2InstReq.setAuto_run(tblContainerSpecInfo.getAutoRun());
                assignEdge2InstReq.setBp_id(tblContainerSpecInfo.getBpId());
                assignEdge2InstReq.setUser_id(tblContainerSpecInfo.getUserId());
                assignEdge2InstReq.setInst_type("docker");
                List<String> waitAssignIdList = Lists.newArrayList(instId);
                assignEdge2InstReq.setWaitAssignIdList(waitAssignIdList);
                assignEdge2InstReq.setSpecId(tblContainerInstInfo.getSpecId());

                //alloc resources, send task to scheduler
                Integer allocStatus = schedulerService.allocEdge2InstResources(assignEdge2InstReq);
                if (null != allocStatus && allocStatus == SchedulerState.WAITING_SCHEDULING.getCode())
                {
                    tblContainerInstInfo.setStatus(InstanceState.WAIT_ASSIGN.getCode());
                }
                else
                {
                    tblContainerInstInfo.setStatus(InstanceState.CLOUD_SYSTEM_ABNORMAL.getCode());
                }

            }
            catch (JsonSyntaxException e)
            {
                LOGGER.info("inst: {} params error", instId);
                tblContainerInstInfo.setStatus(InstanceState.CLOUD_CRETAE_FAILED_PARAMS.getCode());
                e.printStackTrace();
            }
            catch (IllegalStateException e)
            {
                LOGGER.error("microservice error, waiting for resend schedule request");
                tblContainerInstInfo.setStatus(InstanceState.WAIT_ASSIGN.getCode());
                e.printStackTrace();
            }
            finally
            {
                tblContainerInstInfo.setUpdateTime(new Date());
                cisRepository.updateByPrimaryKey(tblContainerInstInfo);
                //记录资源更新事件
                publishContainerInstanceStatusUpdateEvent(tblContainerSpecInfo, tblContainerInstInfo, beforeUpdateEntity, "processReScheduleInst");
            }
        }
    }

    private void publishContainerInstanceStatusUpdateEvent(TblContainerSpecInfo tblContainerSpecInfo, TblContainerInstInfo tblContainerInstInfo, TblContainerInstInfo beforeUpdateContainerInstEntity, String action)
    {
        try
        {
            if (Objects.equals(tblContainerInstInfo.getStatus(), beforeUpdateContainerInstEntity.getStatus()))
            {
                LOGGER.debug("容器 {} 状态未生变化，忽略", beforeUpdateContainerInstEntity.getContainerName());
                return;
            }
            Map<Integer, BizModelStateInfo> instanceStateDesDict = InstanceStateDesProvider.INSTANCE.getStateDesDict().get(InstanceStateDesProvider.STATUS_FIELD);
            BizModelStateInfo stateInfo = instanceStateDesDict.get(tblContainerInstInfo.getStatus());
            cisContainerInstResourceSupervisor.publishUpdateEvent("容器状态更新", null,
                    false, beforeUpdateContainerInstEntity, tblContainerInstInfo, action,
                    TemplateEngineUtils.render0(CisContainerInstActionDescriptionTemplate.Descriptions.CONTAINER_STATUS_UPDATE,
                            false,
                            TemplateEngineUtils.newEntry("status",
                                    Optional.ofNullable(stateInfo)
                                            .map(x -> x.getCnDescription())
                                            .orElse(Optional.ofNullable(tblContainerInstInfo.getStatus())
                                                    .map(x -> x.toString())
                                                    .orElse(""))),
                            TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME, tblContainerInstInfo.getContainerName()),
                            TemplateEngineUtils.newEntry("spec_name", Optional.ofNullable(tblContainerSpecInfo)
                                    .map(x -> x.getSpecName())
                                    .orElse("")),
                            TemplateEngineUtils.newEntry("spec_id", tblContainerInstInfo.getSpecId())));
        } catch (Exception e)
        {
            LOGGER.error("发布容器状态更新事件失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    /**
     * inst failover
     * @param failoverPair
     */
    public void processInstFailover(Pair<String, String> failoverPair)
    {
        String oldInstId = failoverPair.getLeft();
        String failoverRange = failoverPair.getRight();
        TblContainerInstInfo oldTblContainerInstInfo = cisRepository.selectByPrimaryKey(oldInstId);
        if (oldTblContainerInstInfo == null || StringUtils.isEmpty(oldTblContainerInstInfo.getSpecId()))
        {
            return;
        }

        TblContainerSpecInfo tblContainerSpecInfo = cisRepository.getSpec(oldTblContainerInstInfo.getSpecId());
        if (tblContainerSpecInfo == null)
        {
            // can not find spec
            return;
        }
        else
        {
            TblContainerInstInfo tblContainerInstInfo = new TblContainerInstInfo();
            BeanUtils.copyProperties(oldTblContainerInstInfo, tblContainerInstInfo);
            tblContainerInstInfo.setInstId(Utils.buildStr("JST_DC_",Utils.assignUUId()));
            tblContainerInstInfo.setContainerName(tblContainerInstInfo.getContainerName() + 'F');
            tblContainerInstInfo.setStatus(InstanceState.SPAWNED.getCode());
            tblContainerInstInfo.setNodeId(null);
            tblContainerInstInfo.setSiteId(null);
            tblContainerInstInfo.setRegionId(null);
            tblContainerInstInfo.setRefId(null);
            tblContainerInstInfo.setInspetContainerParams(null);
            tblContainerInstInfo.setCreateTime(new Date());
            tblContainerInstInfo.setUpdateTime(tblContainerInstInfo.getCreateTime());
            tblContainerInstInfo.setReportTime(tblContainerInstInfo.getCreateTime());
            tblContainerInstInfo.setEventType(0);

            try
            {
                AssignEdge2InstReq assignEdge2InstReq = new AssignEdge2InstReq();
                assignEdge2InstReq.setName(tblContainerSpecInfo.getSpecName());
                assignEdge2InstReq.setDev_need_info(JsonUtils.fromJson(tblContainerSpecInfo.getDevNeedInfo(), DevNeedInfo.class));
                SchedulingStrategy schedulingStrategy = JsonUtils.fromJson(tblContainerSpecInfo.getSchedulingStrategy(), SchedulingStrategy.class);
                assignEdge2InstReq.setOn_one_node(schedulingStrategy.getOnOneNode());

                switch (failoverRange)
                {
                    case "region":
                        schedulingStrategy.setTargetNodes(Arrays.asList(new TargetNode(oldTblContainerInstInfo.getRegionId(), null, null)));
                        break;
                    case "site":
                        schedulingStrategy.setTargetNodes(Arrays.asList(new TargetNode(oldTblContainerInstInfo.getRegionId(), oldTblContainerInstInfo.getSiteId(), null)));
                        break;
                    case "default":
                        schedulingStrategy.setTargetNodes(Arrays.asList(new TargetNode(oldTblContainerInstInfo.getRegionId(), null, null)));
                        schedulingStrategy.addRegionPerferLabelSelector(oldTblContainerInstInfo.getRegionId());
                        schedulingStrategy.addSitePerferLabelSelector(oldTblContainerInstInfo.getSiteId());
                    default:
                        break;
                }

                assignEdge2InstReq.setTarget_nodes(schedulingStrategy.getTargetNodes());
                assignEdge2InstReq.setScheduling_strategy(schedulingStrategy);
                assignEdge2InstReq.setReplica_num(1);
                assignEdge2InstReq.setInst_params(tblContainerSpecInfo.getContainerParams());
                assignEdge2InstReq.setAuto_run(tblContainerSpecInfo.getAutoRun());
                assignEdge2InstReq.setBp_id(tblContainerSpecInfo.getBpId());
                assignEdge2InstReq.setUser_id(tblContainerSpecInfo.getUserId());
                assignEdge2InstReq.setInst_type("docker");
                List<String> waitAssignIdList = Lists.newArrayList(tblContainerInstInfo.getInstId());
                assignEdge2InstReq.setWaitAssignIdList(waitAssignIdList);
                assignEdge2InstReq.setSpecId(tblContainerInstInfo.getSpecId());

                //alloc resources, send task to scheduler
                Integer allocStatus = schedulerService.allocEdge2InstResources(assignEdge2InstReq);
                if (null != allocStatus && allocStatus == SchedulerState.WAITING_SCHEDULING.getCode())
                {
                    tblContainerInstInfo.setStatus(InstanceState.WAIT_ASSIGN.getCode());
                }
                else
                {
                    tblContainerInstInfo.setStatus(InstanceState.CLOUD_SYSTEM_ABNORMAL.getCode());
                }

            }
            catch (JsonSyntaxException e)
            {
                LOGGER.info("inst: {} params error", tblContainerInstInfo.getInstId());
                tblContainerInstInfo.setStatus(InstanceState.CLOUD_CRETAE_FAILED_PARAMS.getCode());
                e.printStackTrace();
            }
            catch (IllegalStateException e)
            {
                LOGGER.error("microservice error, waiting for resend schedule request");
                tblContainerInstInfo.setStatus(InstanceState.WAIT_ASSIGN.getCode());
                e.printStackTrace();
            }
            finally
            {
                cisRepository.insertInst(tblContainerInstInfo);
                //记录资源创建事件
                publishContainerInstCreateEvent(tblContainerInstInfo, "processInstFailover");
            }
        }
    }

    private void publishContainerInstCreateEvent(TblContainerInstInfo tblContainerInstInfo, String action)
    {
        try
        {
            Map<Integer, BizModelStateInfo> instanceStateDesDict = InstanceStateDesProvider.INSTANCE.getStateDesDict().get(InstanceStateDesProvider.STATUS_FIELD);
            BizModelStateInfo stateInfo = instanceStateDesDict.get(tblContainerInstInfo.getStatus());
            cisContainerInstResourceSupervisor.publishCreateEvent(tblContainerInstInfo,
                    TemplateEngineUtils.render0(CisContainerInstActionDescriptionTemplate.Descriptions.CREATE_CONTAINER,
                            false,
                            TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
                                    tblContainerInstInfo.getContainerName()),
                            TemplateEngineUtils.newEntry("spec_id", tblContainerInstInfo.getSpecId()),
                            TemplateEngineUtils.newEntry("status", Optional.ofNullable(stateInfo)
                                    .map(x -> x.getCnDescription())
                                    .orElse(Optional.ofNullable(tblContainerInstInfo.getStatus())
                                            .map(x -> x.toString())
                                            .orElse("")))),
                    null,
                    action);
        } catch (Exception e)
        {
            LOGGER.error("发布容器实例创建事件失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    /**
     * assign inst to dst node
     * @param instId
     */
    public void processAssignInst(String instId)
    {
        TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(instId);
        if (tblContainerInstInfo == null)
        {
            return;
        }

        if (tblContainerInstInfo.getStatus() != InstanceState.ASSIGNED.getCode())
        {
            return;
        }

        TblContainerInstInfo beforeUpdateContainerInstInfoEntity = DeepCopyUtils.deepCopy(tblContainerInstInfo);

        try
        {
            //get spec
            TblContainerSpecInfo tblContainerSpecInfo = cisRepository.getSpec(tblContainerInstInfo.getSpecId());

            //send create msg
            String instParamsStr = tblContainerSpecInfo.getContainerParams();
            String devNeedInfoStr = tblContainerSpecInfo.getDevNeedInfo();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode instParams = mapper.readValue(instParamsStr, ObjectNode.class);
            DevNeedInfo devNeedInfo = JsonUtils.fromJson(devNeedInfoStr, DevNeedInfo.class);
            //params error
            if (null == devNeedInfo || null == instParams)
            {
                tblContainerSpecInfo.setStatus(InstanceState.CLOUD_CRETAE_FAILED_PARAMS.getCode());

                //relesae resources(remove gpu bind and update edge monopoly status: remove)
                schedulerService.releaseBindResources(tblContainerInstInfo.getNodeId(), instId);
                return;
            }
    
            Registry registryInfo = null;
    
            //request ims for registry
//            EeCommonDef.registry_info.Builder registryInfo = EeCommonDef.registry_info.newBuilder();
            if (null != tblContainerSpecInfo.getRegistryId() && !tblContainerSpecInfo.getRegistryId().isEmpty())
            {
                registryInfo = new Registry();
                ImsRegistryService.Registry registry = imsService.getRegistry(tblContainerSpecInfo.getRegistryId(), tblContainerSpecInfo.getBpId(), tblContainerSpecInfo.getUserId());
                if (null == registry)
                {
                    tblContainerSpecInfo.setStatus(InstanceState.CLOUD_CRETAE_FAILED_PARAMS.getCode());

                    //relesae resources(remove gpu bind and update edge monopoly status: remove)
                    schedulerService.releaseBindResources(tblContainerInstInfo.getNodeId(), instId);
                    return;
                }
    
                registryInfo.setServer(registry.getRegistryUrl());
                registryInfo.setUsername(registry.getRegistryUserName());
                registryInfo.setPassword(registry.getRegistryPassword());
//                createInstReqBuilder.setRegistry(registryInfo);

                String imageName = instParams.get("Image").asText();
                if(!imageName.startsWith(registry.getRegistryUrl()))
                {
                    instParams.put("Image", registry.getRegistryUrl() + "/" + imageName);
                }
            }
    
            String instparmaStr = "";
            // add default labels
            generateDefaultLables(tblContainerInstInfo, tblContainerSpecInfo, instParams);
            // set restart policy
            setDockerRestartPolicy(tblContainerSpecInfo.getRestartPolicy(), instParams);
            // Modify the binding path of the container if there are additional config files.
            modifyContainerConfig(instParams, instId);

            if (null != tblContainerSpecInfo.getGpuNum())
            {
                //generate gpu params
                List<TblEdgeComputeGpuInfo> tblEdgeComputeGpuInfos = null;

                //get bind gpu
                if (tblContainerSpecInfo.getGpuNum() > 0)
                {
                   tblEdgeComputeGpuInfos = cisRepository.selectBindGpuByInstId(instId);
                }
                else if (tblContainerSpecInfo.getGpuNum() == 0)
                {
                    tblEdgeComputeGpuInfos = cisRepository.selectGpuByNodeId(instId);
                }
    
                instparmaStr = generateGpuParams(instParams, devNeedInfo, tblEdgeComputeGpuInfos);
            }
            else
            {
                instparmaStr = instParams.toString();
            }
            netMessageServiceFacade.createInst(registryInfo, instparmaStr, devNeedInfoStr, tblContainerSpecInfo.getAutoRun(), 1,
                    tblContainerSpecInfo.getBpId(), tblContainerSpecInfo.getUserId(), tblContainerInstInfo.getContainerName(),
                    tblContainerInstInfo.getInstId(), tblContainerInstInfo.getNodeId(), tblContainerSpecInfo.getExtraParams());

            tblContainerInstInfo.setStatus(InstanceState.FWD.getCode());
        }
        catch (Exception e)
        {
            LOGGER.info("{} params error", instId);
            tblContainerInstInfo.setStatus(InstanceState.CLOUD_CRETAE_FAILED_PARAMS.getCode());
            e.printStackTrace();

            //relesae resources(remove gpu bind and update edge monopoly status: remove)
            schedulerService.releaseBindResources(tblContainerInstInfo.getNodeId(), instId);
        }
        finally
        {
            tblContainerInstInfo.setUpdateTime(new Date());
            cisRepository.updateByPrimaryKeySelective(tblContainerInstInfo);
            //记录资源更新事件
            publishContainerInstanceStatusUpdateEvent(null, tblContainerInstInfo, beforeUpdateContainerInstInfoEntity,
                    "processAssignInst");
        }
    }

    /**
     * resend msg to dst node
     * @param instId
     */
    public void processResendInst(String instId)
    {
        TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(instId);
        if (tblContainerInstInfo == null)
        {
            return;
        }

        if (tblContainerInstInfo.getStatus() != InstanceState.FWD.getCode())
        {
            return;
        }

        TblContainerInstInfo beforeUpdateContainerInstInfoEntity = DeepCopyUtils.deepCopy(tblContainerInstInfo);

        try
        {
            //update failNum
            tblContainerInstInfo.setFailNum(tblContainerInstInfo.getFailNum()+1);

            //get spec
            TblContainerSpecInfo tblContainerSpecInfo = cisRepository.getSpec(tblContainerInstInfo.getSpecId());

            //send create msg
            EeInstDef.msg_create_inst_req_body.Builder createInstReqBuilder = EeInstDef.msg_create_inst_req_body.newBuilder();

            createInstReqBuilder.setDevNeedInfo(tblContainerSpecInfo.getDevNeedInfo());//Maybe useless
            // set extra_params
            createInstReqBuilder.setExtraParams(tblContainerSpecInfo.getExtraParams());
            String instParamsStr = tblContainerSpecInfo.getContainerParams();
            String devNeedInfoStr = tblContainerSpecInfo.getDevNeedInfo();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode instParams = mapper.readValue(instParamsStr, ObjectNode.class);
            DevNeedInfo devNeedInfo = JsonUtils.fromJson(devNeedInfoStr, DevNeedInfo.class);
            //params error
            if (null == devNeedInfo || null == instParams)
            {
                tblContainerSpecInfo.setStatus(InstanceState.CLOUD_CRETAE_FAILED_PARAMS.getCode());

                //relesae resources(remove gpu bind and update edge monopoly status: remove)
                schedulerService.releaseBindResources(tblContainerInstInfo.getNodeId(), instId);
                return;
            }

            //request ims for registry
            if (null != tblContainerSpecInfo.getRegistryId() && !tblContainerSpecInfo.getRegistryId().isEmpty())
            {
                ImsRegistryService.Registry registry = imsService.getRegistry(tblContainerSpecInfo.getRegistryId(), tblContainerSpecInfo.getBpId(), tblContainerSpecInfo.getUserId());
                if (null == registry)
                {
                    tblContainerSpecInfo.setStatus(InstanceState.CLOUD_CRETAE_FAILED_PARAMS.getCode());

                    //relesae resources(remove gpu bind and update edge monopoly status: remove)
                    schedulerService.releaseBindResources(tblContainerInstInfo.getNodeId(), instId);
                    return;
                }
                EeCommonDef.registry_info.Builder registryInfo = EeCommonDef.registry_info.newBuilder();
                registryInfo.setServer(registry.getRegistryUrl());
                registryInfo.setUsername(registry.getRegistryUserName());
                registryInfo.setPassword(registry.getRegistryPassword());
                createInstReqBuilder.setRegistry(registryInfo);

                String imageName = instParams.get("Image").asText();
                if(!imageName.startsWith(registry.getRegistryUrl()))
                {
                    instParams.put("Image", registry.getRegistryUrl() + "/" + imageName);
                }
            }
            else
            {
                EeCommonDef.registry_info.Builder registryInfo = EeCommonDef.registry_info.newBuilder();
                createInstReqBuilder.setRegistry(registryInfo);
            }

            // add default labels
            generateDefaultLables(tblContainerInstInfo, tblContainerSpecInfo, instParams);
            // set restart policy
            setDockerRestartPolicy(tblContainerSpecInfo.getRestartPolicy(), instParams);
            if (null != tblContainerSpecInfo.getGpuNum())
            {
                //generate gpu params
                List<TblEdgeComputeGpuInfo> tblEdgeComputeGpuInfos = null;

                //get bind gpu
                if (tblContainerSpecInfo.getGpuNum() > 0)
                {
                    tblEdgeComputeGpuInfos = cisRepository.selectBindGpuByInstId(instId);
                }
                else if (tblContainerSpecInfo.getGpuNum() == 0)
                {
                    tblEdgeComputeGpuInfos = cisRepository.selectGpuByNodeId(instId);
                }

                createInstReqBuilder.setInstParams(generateGpuParams(instParams, devNeedInfo, tblEdgeComputeGpuInfos));
            }
            else
            {
                createInstReqBuilder.setInstParams(instParams.toString());
            }
            createInstReqBuilder.setAutoRun(tblContainerSpecInfo.getAutoRun());
            createInstReqBuilder.setReplicaNum(tblContainerSpecInfo.getReplicaNum());
            if (tblContainerSpecInfo.getBpId() != null) createInstReqBuilder.setBpId(tblContainerSpecInfo.getBpId());
            createInstReqBuilder.setUserId(tblContainerSpecInfo.getUserId());

            createInstReqBuilder.setInstName(tblContainerInstInfo.getContainerName());
            createInstReqBuilder.setInstId(tblContainerInstInfo.getInstId());

            EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.INST_OPERATOR);
            EeInstDef.msg_inst_operator_body.Builder instOperator = EeInstDef.msg_inst_operator_body.newBuilder();
            instOperator.setOperatorType(OperatorType.CREATE_INST_REQ);
            instOperator.setInstType("docker");
            instOperator.setCreateInstReqBody(createInstReqBuilder);

            EeNetMessageApi.ee_net_message netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setInstOperatorBody(instOperator).build();

            netMessageServiceFacade.submitMessage(tblContainerInstInfo.getNodeId(), netMessgae);

            tblContainerInstInfo.setStatus(InstanceState.FWD.getCode());
        }
        catch (Exception e)
        {
            LOGGER.info("{} params error", instId);
            tblContainerInstInfo.setStatus(InstanceState.CLOUD_CRETAE_FAILED_PARAMS.getCode());
            e.printStackTrace();

            //relesae resources(remove gpu bind and update edge monopoly status: remove)
            schedulerService.releaseBindResources(tblContainerInstInfo.getNodeId(), instId);
        }
        finally
        {

            tblContainerInstInfo.setUpdateTime(new Date());
            cisRepository.updateByPrimaryKeySelective(tblContainerInstInfo);
            //记录资源修改事件
            publishContainerInstanceStatusUpdateEvent(null, tblContainerInstInfo, beforeUpdateContainerInstInfoEntity,
                    "processResendInst");
        }
    }


    public void processCreateNoRsp(String instId)
    {
        TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(instId);
        if (tblContainerInstInfo == null)
        {
            return;
        }

        if (tblContainerInstInfo.getStatus() != InstanceState.FWD.getCode())
        {
            return;
        }

        TblContainerInstInfo beforeUpdateContainerInstInfoEntity = DeepCopyUtils.deepCopy(tblContainerInstInfo);

        try
        {
            tblContainerInstInfo.setStatus(InstanceState.EDGE_UNREACHABLE.getCode());
            tblContainerInstInfo.setFailNum(tblContainerInstInfo.getFailNum()+1);

        }
        catch (Exception e)
        {
            LOGGER.info("{} params error", instId);
            tblContainerInstInfo.setStatus(InstanceState.CLOUD_CRETAE_FAILED_PARAMS.getCode());
            e.printStackTrace();

            //relesae resources(remove gpu bind and update edge monopoly status: remove)
            schedulerService.releaseBindResources(tblContainerInstInfo.getNodeId(), instId);
        }
        finally
        {

            tblContainerInstInfo.setUpdateTime(new Date());
            cisRepository.updateByPrimaryKeySelective(tblContainerInstInfo);
            //记录资源更新事件
            publishContainerInstanceStatusUpdateEvent(null, tblContainerInstInfo, beforeUpdateContainerInstInfoEntity,
                    "processCreateNoRsp");
        }
    }

    /**
     * generate docker info by record in db
     * @param tblContainerSpecInfo
     */
    private List<TblContainerInstInfo> genContainerInst(TblContainerSpecInfo tblContainerSpecInfo)
    {
        List<TblContainerInstInfo> instList = new ArrayList<>();
        for (int i = 0; i < tblContainerSpecInfo.getReplicaNum(); i++)
        {
            TblContainerInstInfo tblContainerInstInfo = new TblContainerInstInfo();
            tblContainerInstInfo.setInstId(Utils.buildStr("JST_DC_",Utils.assignUUId()));
            tblContainerInstInfo.setRefId("");
            tblContainerInstInfo.setNodeId("");
            tblContainerInstInfo.setRegionId("");
            tblContainerInstInfo.setSiteId("");
            tblContainerInstInfo.setSpecId(tblContainerSpecInfo.getSpecId());
            tblContainerInstInfo.setContainerName(Utils.buildStr(tblContainerSpecInfo.getSpecName(), "_", tblContainerSpecInfo.getSpecId().substring(tblContainerSpecInfo.getSpecId().length()-8), "_", String.valueOf(i)));
            tblContainerInstInfo.setStatus(InstanceState.WAIT_ASSIGN.getCode());
            tblContainerInstInfo.setInspetContainerParams("");
            tblContainerInstInfo.setIp("");
            tblContainerInstInfo.setSendCreateNum(0);
            tblContainerInstInfo.setStartNum(0);
            tblContainerInstInfo.setFailNum(0);
            tblContainerInstInfo.setCreateTime(new Date());
            tblContainerInstInfo.setUpdateTime(tblContainerInstInfo.getCreateTime());
            tblContainerInstInfo.setRemoveTime(tblContainerInstInfo.getCreateTime());
            tblContainerInstInfo.setImageName(tblContainerSpecInfo.getImageName());
            tblContainerInstInfo.setCmd(tblContainerSpecInfo.getCmd());
            tblContainerInstInfo.setUserId(tblContainerSpecInfo.getUserId());
            tblContainerInstInfo.setBpId(tblContainerSpecInfo.getBpId());
            tblContainerInstInfo.setRestartPolicy(tblContainerSpecInfo.getRestartPolicy());
            cisRepository.insertSelective(tblContainerInstInfo);
            //记录资源创建事件
            publishContainerInstCreateEvent(tblContainerInstInfo, "genContainerInst");

            instList.add(tblContainerInstInfo);
        }
        return instList;
    }

    public String generateGpuParams(ObjectNode instParams, DevNeedInfo devNeedInfo, List<TblEdgeComputeGpuInfo> tblEdgeComputeGpuInfos)
    {
        //if hostconfig not exists, set default hostconfig
        if (null == instParams.get("HostConfig"))
        {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            instParams.set("HostConfig",objectNode);
        }
        //set gpu runtime
        ((ObjectNode)instParams.get("HostConfig")).put("Runtime","nvidia");

        //if env not exists, set default env
        if (null == instParams.get("Env"))
        {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            instParams.set("Env",objectNode);
        }

        //set nvidia visible devices
        if (null != tblEdgeComputeGpuInfos && tblEdgeComputeGpuInfos.size()>0)
        {
            StringBuilder visibleDevices = new StringBuilder();
            for (TblEdgeComputeGpuInfo tblEdgeComputeGpuInfo:tblEdgeComputeGpuInfos)
            {
                visibleDevices.append(tblEdgeComputeGpuInfo.getIndex());
                visibleDevices.append(',');
            }
            ((ObjectNode)instParams.get("Env")).put("NVIDIA_VISIBLE_DEVICES", visibleDevices.toString().substring(0,visibleDevices.toString().length()-1));
        }

        //set nvidia driver capabilities
        if (null != devNeedInfo.getGpu() && null != devNeedInfo.getGpu().getDriverCapabilities() && !devNeedInfo.getGpu().getDriverCapabilities().isEmpty())
        {
            ((ObjectNode)instParams.get("Env")).put("NVIDIA_DRIVER_CAPABILITIES", devNeedInfo.getGpu().getDriverCapabilities());
        }

        //if env is null
        if (null != instParams.get("Env") && instParams.get("Env").isEmpty())
        {
            instParams.remove("Env");
        }

        return instParams.toString();
    }

    public boolean isOwnerOfInst(String instId, String bpId, String userId)
    {
        if (StringUtils.isEmpty(instId))
        {
            return false;
        }
        if (StringUtils.isEmpty(userId) && StringUtils.isEmpty(bpId))
        {
            return false;
        }

        TblContainerInstInfoExample example = new TblContainerInstInfoExample();
        TblContainerInstInfoExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(userId))
        {
            criteria.andUserIdEqualTo(userId);
        }
        if (StringUtils.isNotEmpty(bpId))
        {
            criteria.andBpIdEqualTo(bpId);
        }
        criteria.andInstIdEqualTo(instId);

        List<TblContainerInstInfo> tblContainerInstInfos = cisRepository.selectByExample(example);
        return null != tblContainerInstInfos && tblContainerInstInfos.size() != 0;
    }

    public GetContainerDeployInfosRsp getContainerDeployInfos(ContainerSearchCritical critical, String ownerUserId)
    {
        GetContainerDeployInfosRsp rsp = GetContainerDeployInfosRsp.builder().build();
        List<Integer> cloudRemoveStatusList = CisManagerService.cloudRemoveStatusList;
        cloudRemoveStatusList.add(RecordStatus.DELETED.value());

        String bpId = queryBpId();
        String userId = queryUserId();
        if ((isAdmin() || isBpAdmin()) && StringUtils.isNotBlank(ownerUserId))
        {
          userId = ownerUserId;
        }
        int totalNum = cisRepository.countAllDeployments(critical.getStatus(), cloudRemoveStatusList,
                getFullStatus(PROGRESSING_STAGE), getFullStatus(AVAILABLE_STAGE), getFullStatus(READY_STAGE), getFullStatus(FAILED_STAGE),
                critical.getName(), critical.getRegion(), critical.getSite(), critical.getNodeId(),
                userId, bpId);
        if (totalNum < 1)
        {
            LOGGER.info("get container deploy infos empty: {}", critical);
            return rsp;
        }

        rsp.setTotalNum(totalNum);
        //get container deploy infos
        int startRow = 0;
        int pageSize = critical.getPageSize();
        if (critical.getPageSize() != null && critical.getPageNum() != null && critical.getPageNum() >= 1 )
        {
            startRow = (critical.getPageNum() - 1) * critical.getPageSize();
        }

        List<DockerContainerDeployInfo> records = cisRepository.selectAllDeployments(critical.getStatus(), cloudRemoveStatusList,
                getFullStatus(PROGRESSING_STAGE), getFullStatus(AVAILABLE_STAGE), getFullStatus(READY_STAGE), getFullStatus(FAILED_STAGE),
                critical.getName(), critical.getRegion(), critical.getSite(), critical.getNodeId(),
                userId, bpId,  startRow, pageSize);
        if (records == null || records.isEmpty())
        {
            LOGGER.info("get container deploy infos empty: {}", critical);
            return rsp;
        }

        // assemble userName bpName
        records = assembleDockerContainerDeployInfos(records);

        rsp.setDeployments(records);
        return rsp;
    }


    public DockerContainerDeployInfo getContainerDeployInfo(String specId)
    {
        DockerContainerDeployInfo dockerContainerDeployInfo = cisRepository.selectDeploymentBySpecId(specId, cloudRemoveStatusList,  getFullStatus(PROGRESSING_STAGE), getFullStatus(AVAILABLE_STAGE), getFullStatus(READY_STAGE), getFullStatus(FAILED_STAGE));
        if (Objects.nonNull(dockerContainerDeployInfo))
        {
            dockerContainerDeployInfo = assembleDockerContainerDeployInfo(dockerContainerDeployInfo);
            return dockerContainerDeployInfo;
        }
        return new DockerContainerDeployInfo();
    }

    public GetContainerInstInfosRsp getContainerDeployInstancesInfo(ContainerSearchCritical critical)
    {
        GetContainerInstInfosRsp containerInstInfos = getContainerInstInfos(critical);
        return containerInstInfos;
    }

    public void deleteContainerDeployInfo(String specId)
    {

        TblContainerSpecInfo containerSpecInfo = getContainerSpecInfo(specId,queryBpId(), queryUserId());

        deleteAssociatedResources(containerSpecInfo);

        String name = containerSpecInfo.getSpecId() + containerSpecInfo.getSpecName();
        if (name.length() > 63)
        {
            name = name.substring(0, 63);
        }
        TblContainerSpecInfo tblContainerSpecInfo = new TblContainerSpecInfo();
        tblContainerSpecInfo.setSpecId(specId);
        tblContainerSpecInfo.setSpecName(name);
        tblContainerSpecInfo.setUpdateTime(new Date());
        tblContainerSpecInfo.setStatus(RecordStatus.DELETED.value());

        TblContainerSpecInfo afterUpdateEntity = DeepCopyUtils.deepCopy(containerSpecInfo);
        cisRepository.updateSpec(tblContainerSpecInfo);
        //记录资源更新事件
        afterUpdateEntity.setSpecName(tblContainerSpecInfo.getSpecName());
        afterUpdateEntity.setStatus(tblContainerSpecInfo.getStatus());
        afterUpdateEntity.setUpdateTime(tblContainerSpecInfo.getUpdateTime());
        publishContainerSpecStatusUpdateEvent(afterUpdateEntity, containerSpecInfo,
                "deleteContainerDeployInfo");

        RedisUtil.delete(CisRedisField.CIS_KEEPALIVE_SPEC_INSTIDS + specId);
        RedisUtil.zrem(CisRedisField.CIS_KEEPALIVE_SPECIDS, specId);
    }

    public TblContainerSpecInfo getContainerSpecInfo(String specId, String bpId, String userId)
    {
        TblContainerSpecInfo specInfo = cisRepository.getSpec(specId);
        if (Objects.isNull(specInfo))
        {
            throw new WebSystemException(INST_SPEC_NOT_EXIST, ErrorLevel.ERROR);
        }

        if (RecordStatus.DELETED.value() == specInfo.getStatus().intValue())
        {
            throw new WebSystemException(INST_SPEC_DROPPED, ERROR);
        }

        if (Objects.nonNull(bpId))
        {
            if (!bpId.equals(specInfo.getBpId()))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, INFO);
            }
        }

        if (StringUtils.isNotBlank(userId))
        {
            if (!userId.equals(specInfo.getUserId()))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, INFO);
            }
        }
        
        return specInfo;
    }

    public List<DockerContainerInfo> getContainerDeployInstancesInfo(String specId)
    {

        List<DockerContainerInfo> containers = new ArrayList<>();

        TblContainerInstInfoExample example = new TblContainerInstInfoExample();
        TblContainerInstInfoExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(specId);
        example.setOrderByClause("create_time desc");
        List<TblContainerInstInfo> records = cisRepository.getContainerInstInfosByExample(example);
        if (records == null || records.isEmpty())
        {
            LOGGER.info("get container spec instance infos empty: {}", specId);
            return containers;
        }

        for (TblContainerInstInfo record : records)
        {
            DockerContainerInfo container = new DockerContainerInfo();

            container.setName(record.getContainerName());
            container.setId(record.getInstId());
            container.setContainerId("");
            container.setImageName(record.getImageName());
            container.setStatus(InstanceState.fromCode(record.getStatus()).toStatusCode());

            //container.setIp("");
            if (null != record.getCmd() && !record.getCmd().isEmpty())
            {
                List<String> cmdList = new ArrayList<>();
                container.setCmd(JsonUtils.fromJson(record.getCmd(), cmdList.getClass()));
            }
            container.setBpId(record.getBpId());

            container.setUserId(record.getUserId());
            container.setNodeId(record.getNodeId());

            container.setCreateTime(Utils.formatDate(record.getCreateTime()));
            if (null != record.getRemoveTime())
            {
                container.setStopTime(Utils.formatDate(record.getRemoveTime()));
            }

            generateInstTipMessage(container, record.getEventType(), record.getReportTime());

            containers.add(container);
        }

        return containers;
    }

    /**
     * add default labels
     * @param tblContainerInstInfo
     * @param tblContainerSpecInfo
     * @param instParams
     */
    private void generateDefaultLables(TblContainerInstInfo tblContainerInstInfo, TblContainerSpecInfo tblContainerSpecInfo, ObjectNode instParams) {
        ObjectNode labels = (ObjectNode) instParams.get("Labels");
        if (Objects.isNull(labels)) {
            labels = instParams.putObject("Labels");
        }
        String instId = tblContainerInstInfo.getInstId();
        if (StringUtils.isNotBlank(instId))
        {
            labels.put(JDC_UUID, instId);
        }

        String userId = tblContainerInstInfo.getUserId();
        if (StringUtils.isNotBlank(userId))
        {
            labels.put(JDC_UID, userId);
        }

        String bpId = tblContainerInstInfo.getBpId();
        if (StringUtils.isNotBlank(bpId))
        {
            labels.put(JDC_BPID, bpId);
        }

        labels.put(INST_TYPE, INST_TYPE_VALUE);

        String specName = tblContainerSpecInfo.getSpecName();
        if (StringUtils.isNotBlank(specName))
        {
            labels.put(INST_NAME, specName);
        }

    }

    private void setDockerRestartPolicy(String restartPolicy, ObjectNode instParams)
    {
        if (StringUtils.isNotEmpty(restartPolicy))
        {
            //if hostconfig not exists, set default hostconfig
            if (null == instParams.get("HostConfig"))
            {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                instParams.set("HostConfig", objectNode);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            ((ObjectNode)instParams.get("HostConfig")).set("RestartPolicy", objectNode);
            if (restartPolicy.startsWith("on-failure") && !restartPolicy.equals("on-failure"))
            {
                int maximumRetryCount = Integer.parseInt(restartPolicy.split(":")[1]);
                ((ObjectNode)instParams.get("HostConfig").get("RestartPolicy")).put("Name","on-failure").put("MaximumRetryCount", maximumRetryCount);
            }
            else
            {
                ((ObjectNode)instParams.get("HostConfig").get("RestartPolicy")).put("Name", restartPolicy);
            }
        }
    }

    public void processLostStatusGT10MAndLT2H(String instId)
    {
        TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(instId);
        if (tblContainerInstInfo == null)
        {
            return;
        }

        if (tblContainerInstInfo.getEventType() == EventType.LOST_STATUS_GT_10M_LT_2H.getCode())
        {
            //Less than 10 min
            if (tblContainerInstInfo.getUpdateTime().after(new Date(new Date().getTime() - 10 * 60 * 1000)))
            {
                return;
            }
        }
        else if (tblContainerInstInfo.getEventType() != EventType.DEFAULT.getCode())
        {
            return;
        }

        TblContainerInstInfo beforeUpdateContainerInstInfoEntity = DeepCopyUtils.deepCopy(tblContainerInstInfo);

        //update event type
        tblContainerInstInfo.setEventType(EventType.LOST_STATUS_GT_10M_LT_2H.getCode());
        tblContainerInstInfo.setStatus(null);
        tblContainerInstInfo.setUpdateTime(new Date());
        cisRepository.updateByPrimaryKeySelective(tblContainerInstInfo);
        //记录资源更新事件
        publishContainerInstanceStatusUpdateEvent(null, tblContainerInstInfo, beforeUpdateContainerInstInfoEntity,
                "processLostStatusGT10MAndLT2H");
        netMessageServiceFacade.listInstReq(instId, tblContainerInstInfo.getNodeId());
    }

    public void processLostStatusGT2H(String instId)
    {
        TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(instId);
        if (tblContainerInstInfo == null)
        {
            return;
        }

        if (tblContainerInstInfo.getEventType() != EventType.LOST_STATUS_GT_10M_LT_2H.getCode())
        {
            return;
        }

        TblContainerInstInfo beforeUpdateContainerInstInfoEntity = DeepCopyUtils.deepCopy(tblContainerInstInfo);

        //update event type
        tblContainerInstInfo.setEventType(EventType.LOST_STATUS_GT_2H.getCode());
        tblContainerInstInfo.setStatus(null);
        tblContainerInstInfo.setUpdateTime(new Date());
        cisRepository.updateByPrimaryKeySelective(tblContainerInstInfo);
        //记录资源更新事件
        publishContainerInstanceStatusUpdateEvent(null, tblContainerInstInfo, beforeUpdateContainerInstInfoEntity,
                "processLostStatusGT2H");
    }

    private void generateInstTipMessage(DockerContainerInfo dockerContainerInfo, Integer eventTypeCode, Date reportTime)
    {
        TipMessage tipMessage = null;
        EventType eventType = EventType.fromCode(eventTypeCode);
        switch (Objects.requireNonNull(eventType))
        {
            case DEFAULT:
                break;
            case LOST_STATUS_GT_10M_LT_2H:
                tipMessage = new TipMessage();
                tipMessage.setLevel(TipMessage.TipMessageLevel.INFO.getLevel());
                if (null == reportTime) break;
                tipMessage.setMessage(String.format(eventType.getDescription(), Utils.diffDate(reportTime, new Date())));
                break;
            case LOST_STATUS_GT_2H:
                tipMessage = new TipMessage();
                tipMessage.setLevel(TipMessage.TipMessageLevel.WARNING.getLevel());
                if (null == reportTime) break;
                tipMessage.setMessage(String.format(eventType.getDescription(), Utils.diffDate(reportTime, new Date())));
                break;
            default:
        }
        dockerContainerInfo.setTip_message(tipMessage);
    }


    private List<DockerContainerDeployInfo> assembleDockerContainerDeployInfos(List<DockerContainerDeployInfo> records)
    {
        if (CollectionUtils.isEmpty(records))
        {
            return records;
        }

        List<DockerContainerDeployInfo> dockerContainerDeployInfos = records.stream().map(dockerContainerDeployInfo ->
        {
            return assembleDockerContainerDeployInfo(dockerContainerDeployInfo);
        }).collect(Collectors.toList());

        return dockerContainerDeployInfos;
    }

    private DockerContainerDeployInfo assembleDockerContainerDeployInfo(DockerContainerDeployInfo dockerContainerDeployInfo)
    {
        String userId = dockerContainerDeployInfo.getUserId();
        if (StringUtils.isNotBlank(userId))
        {
            String userName = combRpcService.getUmsService().getUserNameByUserId(userId);
            dockerContainerDeployInfo.setUserName(userName);
        }

        String bpId = dockerContainerDeployInfo.getBpId();
        if (StringUtils.isNotBlank(bpId))
        {
            String bpName = combRpcService.getUmsService().getBpNameByBpId(bpId);
            dockerContainerDeployInfo.setBpName(bpName);
        }

        TblContainerSpecInfo spec = cisRepository.getSpec(dockerContainerDeployInfo.getId());
        String registryUrl = getRegistryUrlBySpec(spec);
        if (StringUtils.isNotBlank(registryUrl))
        {
            dockerContainerDeployInfo.setImageName(registryUrl + "/" + dockerContainerDeployInfo.getImageName());
        }

        return dockerContainerDeployInfo;
    }

    private String getRegistryUrlBySpec(TblContainerSpecInfo spec)
    {
        if (Objects.nonNull(spec))
        {
            String registryId = spec.getRegistryId();
            if (StringUtils.isNotBlank(registryId))
            {
                try
                {
                    String registryUrl = imsService.getRegistryUrl(registryId);
                    if (StringUtils.isNotBlank(registryUrl))
                    {
                        return registryUrl;
                    }
                }
                catch (Exception e)
                {
                    LOGGER.error("get registry url error:{}", e);

                }
            }
        }

        return "";
    }

    private void deleteAssociatedResources(TblContainerSpecInfo containerSpecInfo)
    {
        if (Objects.nonNull(containerSpecInfo))
        {
            // Verify that there are associated inst
            TblContainerInstInfoExample example = new TblContainerInstInfoExample();
            TblContainerInstInfoExample.Criteria criteria = example.createCriteria();
            criteria.andStatusNotIn(cloudRemoveStatusList);
            criteria.andSpecIdEqualTo(containerSpecInfo.getSpecId());

            List<TblContainerInstInfo> containerInstInfos = cisRepository.getContainerInstInfosByExample(example);
            if (!CollectionUtils.isEmpty(containerInstInfos))
            {
                List<CompletableFuture<Integer>> futureList = containerInstInfos.stream().map(instInfo ->
                {
                    CompletableFuture<Integer> remove = CompletableFuture.supplyAsync(() ->
                    {
                        Integer x = doRemove(instInfo);
                        if (x != null)
                        {
                            return 0;
                        }
                        netMessageServiceFacade.lifeMngInstReq(instInfo.getInstId(), instInfo.getRefId(), "remove", instInfo.getNodeId());
                        return 0;
                    }).exceptionally(e -> 1);
                    return remove;
                }).collect(Collectors.toList());
                long total = futureList.stream().map(CompletableFuture::join).mapToInt(Integer::intValue).sum();
               // total > 0 , indicates that an exception has occurred
                if (total > 0)
                {
                    throw new WebSystemException(INST_SPEC_DELETING_ERROR, ERROR);
                }
            }
        }
    }


    private Integer doRemove(TblContainerInstInfo tblContainerInstInfo)
    {
        //记录原始状态
        TblContainerInstInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblContainerInstInfo);

        //if stack have not been scheduled, drop it directly
        if (tblContainerInstInfo.getStatus() < InstanceState.SPAWNED.getCode())
        {
            cisRepository.deleteByPrimaryKey(tblContainerInstInfo.getInstId());
            //记录资源删除事件
            publishContainerInstInfoDeleteEvent(tblContainerInstInfo, beforeUpdateEntity, "doRemove");
            return 0;
        }

        if (InstanceState.FIN_CLOUD_REMOVE.getCode() == tblContainerInstInfo.getStatus())
        {
            //inst already dropd
            return 0;
        }
        else if (edgeRemoveStatusSet.contains(tblContainerInstInfo.getStatus())
                || createFailStatusSet.contains(tblContainerInstInfo.getStatus())
                || StringUtils.isEmpty(tblContainerInstInfo.getRefId()))
        {
            //1 inst already remove in edge
            //2 inst have not create in edge or create filed
            tblContainerInstInfo.setStatus(InstanceState.FIN_CLOUD_REMOVE.getCode());
            tblContainerInstInfo.setUpdateTime(new Date());
            cisRepository.updateByPrimaryKey(tblContainerInstInfo);

            //记录资源更新事件
            TblContainerSpecInfo tblContainerSpecInfo = cisRepository.getSpec(tblContainerInstInfo.getSpecId());
            publishContainerInstanceStatusUpdateEvent(tblContainerSpecInfo, tblContainerInstInfo,
                    beforeUpdateEntity, "doRemove");

            //relesae resources(remove gpu bind and update edge monopoly status: remove)
            schedulerService.releaseBindResources(tblContainerInstInfo.getNodeId(), tblContainerInstInfo.getInstId());
            return 0;
        }

        //update inst status
        tblContainerInstInfo.setStatus(InstanceState.SPAWNED_CLOUD_REMOVE.getCode());
        return null;
    }

    private void publishContainerInstInfoDeleteEvent(TblContainerInstInfo tblContainerInstInfo, TblContainerInstInfo beforeUpdateEntity, String action)
    {
        cisContainerInstResourceSupervisor.publishDeleteEvent(beforeUpdateEntity, null, null, action,
                TemplateEngineUtils.render0(CisContainerInstActionDescriptionTemplate.Descriptions.REMOVE,
                        false,
                        TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
                                tblContainerInstInfo.getContainerName())));
    }

    private void assembleDstNode(TblContainerInstInfo record, DockerContainerInfo container)
    {
        String nodeId = record.getNodeId();
        CompletableFuture<String> nodeCompletableFuture = CompletableFuture.supplyAsync(() ->
        {
            if (StringUtils.isNotBlank(nodeId))
            {
                String nodeName = combRpcService.getRegionResourceService().getNodeNameById(nodeId);
                return nodeName;
            }
            return  "";
        });

        String siteId = record.getSiteId();
        CompletableFuture<String> siteCompletableFuture = CompletableFuture.supplyAsync(() ->
        {
            if (StringUtils.isNotBlank(siteId))
            {
                String siteName = combRpcService.getRegionResourceService().getSiteNameById(siteId);
                return siteName;
            }
            return "";
        });

        String regionId = record.getRegionId();
        CompletableFuture<String> regionCompletableFuture = CompletableFuture.supplyAsync(() ->
        {
            if (StringUtils.isNotBlank(regionId))
            {
                String regionName = combRpcService.getRegionResourceService().getRegionNameById(regionId);
                return regionName;
            }
            return "";
        });

        CompletableFuture.allOf(nodeCompletableFuture, siteCompletableFuture, regionCompletableFuture);
        container.setNodeId(nodeId);
        container.setSiteId(siteId);
        container.setRegionId(regionId);
        try
        {
            container.setNodeName(nodeCompletableFuture.get());
            container.setRegionName(regionCompletableFuture .get());
            container.setSiteName(siteCompletableFuture.get());
        }
        catch (InterruptedException |ExecutionException e)
        {
            LOGGER.error("rpc getRegionResource error:{}", e);
        }
    }

    public List<ContainerOperatorLog> getContainerInstOperLogs(String instanceId)
    {
        return cisRepository.getContainerInstOperLogs(instanceId);
    }

    public List<ContainerRunLog> getContainerInstRunLogs(String instanceId)
    {
        return cisRepository.getContainerInstRunLogs(instanceId);
    }

    public void insertOperatorInfo(TblContainerOperatorInfo tblContainerOperatorInfo)
    {
        try
        {
            if (null != tblContainerOperatorInfo)
            {
                tblContainerOperatorInfo.setOperId(Utils.assignUUId());
                tblContainerOperatorInfo.setOperTime(new Date());
                cisRepository.insertOperatorInfo(tblContainerOperatorInfo);
            }
        }
        catch (Exception e)
        {
            //insert operator log error
            LOGGER.debug("insert operator log error, inst : {}.", tblContainerOperatorInfo.getInstId());
        }
    }

    private void assembleUserInfo(TblContainerInstInfo record, DockerContainerInfo container)
    {
        String recordBpId = record.getBpId();
        String recordBpName = "";
        if (StringUtils.isNotBlank(recordBpId))
        {
            try
            {
                recordBpName = combRpcService.getUmsService().getBpNameByBpId(recordBpId);
            }
            catch (Exception e)
            {
                LOGGER.error("get bp name failed");
            }
        }
        container.setBpId(recordBpId);
        container.setBpName(recordBpName);

        String recordUserId = record.getUserId();
        String recordUserName = "";
        if (StringUtils.isNotBlank(recordUserId))
        {
            try
            {
                recordUserName = combRpcService.getUmsService().getUserNameByUserId(recordUserId);
            }
            catch (Exception e)
            {
                LOGGER.error("get user name failed");
            }
        }
        container.setUserId(recordUserId);
        container.setUserName(recordUserName);
    }

    public List<DockerContainerInfo> getContainersByCfg(String userId, String group, String dataId)
    {
        List<DockerContainerInfo> containers = new ArrayList<>();
        List<Integer> cloudRemoveStatusList = CisManagerService.cloudRemoveStatusList;
        cloudRemoveStatusList.add(RecordStatus.DELETED.value());
        List<TblContainerInstInfo> records = cfgRepository.getContainersByCfg(userId, group, dataId, cloudRemoveStatusList);
        if (CollectionUtils.isEmpty(records))
        {
            return containers;
        }

        for (TblContainerInstInfo record : records)
        {
            DockerContainerInfo container = getDockerContainerInfo(record);

            container.setRestartPolicy(record.getRestartPolicy());

            containers.add(container);
        }

        return containers;
    }

    public void updateContainersCfg(ConfigInfo configInfo, UpdateContainersCfgReq updateContainersCfgReq)
    {
        String userId = configInfo.getUserId();
        String group = configInfo.getGroup();
        String dataId = configInfo.getDataId();
        List<TblCfgdataContainerInfo> tblCfgdataContainerInfos = cfgRepository.selectAll(userId, group, dataId, updateContainersCfgReq.getContainerIds());
        if (!CollectionUtils.isEmpty(tblCfgdataContainerInfos))
        {
            SysService.ConfigInfoBase config = null;
            try
            {
                config = combRpcService.getSysService().getConfig(userId, group, dataId);
            }
            catch(Exception e)
            {
                LOGGER.error("get config:{} error: {}", configInfo, e);
            }

            if (Objects.nonNull(config))
            {
                String md5 = config.getMd5();
                SysService.ConfigInfoBase finalConfig = config;
                tblCfgdataContainerInfos.stream().forEach(tblCfgdataContainerInfo -> {
                    if (!StringUtils.equals(md5, tblCfgdataContainerInfo.getDataHash()))
                    {
                        tblCfgdataContainerInfo.setDataHash(md5);
                        tblCfgdataContainerInfo.setUpdateTime(new Date());
                        if (updateContainersCfgReq.isRestart())
                        {
                            tblCfgdataContainerInfo.setSyncState(SYNCING_RESTART);
                        }
                        else
                        {
                            tblCfgdataContainerInfo.setSyncState(SYNCING);
                        }
                        cfgRepository.updateByPrimaryKeySelective(tblCfgdataContainerInfo);
                        sendConfigToEdge(new Pair(tblCfgdataContainerInfo, finalConfig));
                    }
                });
            }

        }
    }

    public void checkConfig(List<String> instIds)
    {
        instIds.removeIf(instId -> {
            TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(instId);
            if (Objects.nonNull(tblContainerInstInfo))
            {
                String specId = tblContainerInstInfo.getSpecId();
                TblContainerSpecInfo spec = cisRepository.getSpec(specId);
                if (Objects.nonNull(spec))
                {
                    String containerParams = spec.getContainerParams();
                    if (StringUtils.isNotBlank(containerParams))
                    {
                        ObjectNode instParams = null;
                        try
                        {
                            instParams = (ObjectNode) JacksonUtils.objectMapper.readTree(containerParams);
                            LOGGER.info("check bind config:{}", instParams.toPrettyString());
                        }
                        catch (JsonProcessingException e)
                        {
                            LOGGER.error("parse container params error:{}", e);
                            return false;
                        }

                        List<ConfigInfo> configInfos = convert(instParams);
                        if (!CollectionUtils.isEmpty(configInfos))
                        {
                            List<TblCfgdataContainerInfo> tblCfgdataContainerInfos = cfgRepository.selectByContainerId(instId);
                            if (!CollectionUtils.isEmpty(tblCfgdataContainerInfos))
                            {
                                if (tblCfgdataContainerInfos.size() == configInfos.size())
                                {
                                    return false;
                                }
                                else
                                {
                                    // remove the already created configuration
                                    tblCfgdataContainerInfos.forEach(tblCfgdataContainerInfo -> {
                                        configInfos.removeIf(configInfo -> {
                                            return configInfo.getDataId().equals(tblCfgdataContainerInfo.getDataId())
                                                    && configInfo.getGroup().equals(tblCfgdataContainerInfo.getDataGroup())
                                                    && configInfo.getUserId().equals(tblCfgdataContainerInfo.getUserId());
                                        });
                                    });
                                }
                            }
                            createCfgdataContainerInfo(configInfos, tblContainerInstInfo);
                            return true;
                        }

                    }

                }
            }

            return false;
        });
    }

    public void processConfigFileCreateReq(Object messageObj)
    {
        Pair<TblCfgdataContainerInfo, SysService.ConfigInfoBase> msg = (Pair<TblCfgdataContainerInfo, SysService.ConfigInfoBase>) messageObj;
        TblCfgdataContainerInfo containerInfo = msg.getLeft();
        SysService.ConfigInfoBase configInfoBase = msg.getRight();
        LOGGER.info("submit config file create msg:{} to node:{}", configInfoBase.getUserId() + "" + "" + "", containerInfo.getNodeId());

        EeFileDef.msg_file_create_req_body.Builder fileCreateReqBody = EeFileDef.msg_file_create_req_body.newBuilder();
        // /var/lnjoying/volume/{instid}/{userId}/{group}/{dataId}
        fileCreateReqBody.setFilePath(EDGE_CONFIG_FILE_PREFIX + containerInfo.getContainerId()
            + "/" + configInfoBase.getUserId() + "/" + configInfoBase.getGroup() + "/" + configInfoBase.getDataId());
        fileCreateReqBody.setContent(configInfoBase.getContent());
        fileCreateReqBody.setHash(configInfoBase.getMd5());

        EeFileDef.msg_file_operator_body.Builder operatorBody = EeFileDef.msg_file_operator_body.newBuilder();
        operatorBody.setOperatorType(FILE_CREATE_REQ);
        operatorBody.setAlgorithm(FILE_CREATE_ALGORITHM);
        operatorBody.setFileCreateReqBody(fileCreateReqBody);

        // cis.config.sss.xxx
        String sessionId = REQ_MSG_HEADER_NAME_CIS_PREFIX + containerInfo.getCfgVolumeId() + "." + Utils.assignUUId().substring(0, 10);
        EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(FILE_OPERATOR, sessionId);
        EeNetMessageApi.ee_net_message.Builder netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setFileOperatorBody(operatorBody);
        netMessageServiceFacade.submitMessage(containerInfo.getNodeId(), netMessage.build());
    }

    public int processFileCreateRsp(EeFileDef.msg_file_create_rsp_body fileCreateRspBody, EeCommonDef.msg_header head)
    {
        String sessionId = head.getSessionId();

        int status = fileCreateRspBody.getStatus();
        if (CfgStatus.checkStatus(status))
        {
            // cis.config.cfgVolumeId.xxx
            String[] split = StringUtils.split(sessionId, ".", 4);
            if (split.length >= 3)
            {
                String cfgVolumeId = split[2];
                TblCfgdataContainerInfo cfgdataContainerInfo = cfgRepository.selectByPrimaryKey(cfgVolumeId);
                if (Objects.isNull(cfgdataContainerInfo))
                {
                    return ErrorCode.SUCCESS.getCode();
                }

                TblContainerInstInfo containerInstInfo = cisRepository.selectByPrimaryKey(cfgdataContainerInfo.getContainerId());
                int containerStatus = containerInstInfo.getStatus();
                if (Objects.isNull(containerInstInfo))
                {
                    return ErrorCode.SUCCESS.getCode();
                }
                //记录原始状态
                TblContainerInstInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(containerInstInfo);
                LOGGER.info("process file create rsp success, session:{}, status:{} ", sessionId, fileCreateRspBody.getStatus());
                switch(status)
                {
                    case CfgStatus.SUCCESS:
                        if (SYNCING_RESTART == cfgdataContainerInfo.getSyncState())
                        {
                            processRestartContainer(cfgdataContainerInfo);
                        }
                        break;
                    case CfgStatus.NO_PERMISSION:
                    case CfgStatus.NO_SPACE:
                        if (StringUtils.isNotEmpty(containerInstInfo.getRestartPolicy()) && containerInstInfo.getRestartPolicy().equals("always"))
                        {
                            TblContainerSpecInfo tblContainerSpecInfo = cisRepository.getSpec(containerInstInfo.getSpecId());
                            FailoverPolicy failoverPolicy = JsonUtils.fromJson(tblContainerSpecInfo.getFailoverPolicy(), FailoverPolicy.class);
                            if (failoverPolicy.getNeedFailover())
                            {
                                String instId = containerInstInfo.getInstId();
                                createNewInst(instId, failoverPolicy.getFailoverRange());
                                containerLifeMng(instId, InstAction.REMOVE.getName());
                                cfgRepository.deleteByContainerId(instId);
                            }
                            else if (ASSIGNED.getCode() == containerStatus)
                            {
                                containerInstInfo.setStatus(CREATE_FAILED.getCode());
                                cisRepository.updateByPrimaryKeySelective(containerInstInfo);
                                //记录资源更新事件
                                publishContainerInstanceStatusUpdateEvent(tblContainerSpecInfo, containerInstInfo,
                                        beforeUpdateEntity, "processFileCreateRsp");
                            }
                        }
                        else if (ASSIGNED.getCode() == containerStatus)
                        {
                            containerInstInfo.setStatus(CREATE_FAILED.getCode());
                            cisRepository.updateByPrimaryKeySelective(containerInstInfo);
                            //记录资源更新事件
//                            TblContainerSpecInfo tblContainerSpecInfo = cisRepository.getSpec(containerInstInfo.getSpecId());
                            publishContainerInstanceStatusUpdateEvent(null, containerInstInfo,
                                    beforeUpdateEntity, "processFileCreateRsp");
                        }
                        break;
                    case CfgStatus.HASH_CHECK_FAILED:
                    case CfgStatus.OTHER_ERROR:
                        containerInstInfo.setStatus(CREATE_FAILED.getCode());
                        cisRepository.updateByPrimaryKeySelective(containerInstInfo);
                        //记录资源更新事件
//                        TblContainerSpecInfo tblContainerSpecInfo = cisRepository.getSpec(containerInstInfo.getSpecId());
                        publishContainerInstanceStatusUpdateEvent(null, containerInstInfo,
                                beforeUpdateEntity, "processFileCreateRsp");
                        break;
                    default:
                }

                cfgdataContainerInfo.setSyncState(status);
                cfgdataContainerInfo.setCfgVolumeId(cfgVolumeId);
                cfgdataContainerInfo.setUpdateTime(new Date());
                cfgRepository.updateByPrimaryKeySelective(cfgdataContainerInfo);
            }
            return ErrorCode.SUCCESS.getCode();
        }

        LOGGER.error("process file create rsp error, session:{}, status:{} ", sessionId, fileCreateRspBody.getStatus());
        return ErrorCode.CONFIG_CREATE_ERROR.getCode();
    }

    private void processRestartContainer(TblCfgdataContainerInfo cfgdataContainerInfo)
    {
        containerLifeMng(cfgdataContainerInfo.getContainerId(), InstAction.RESTART.getName());
    }

    private boolean checkBindConfig(ObjectNode instParams)
    {
        List<ConfigInfo> configInfos = convert(instParams);

        List<ConfigInfo> verifyList = verify(configInfos);
        return verifyList.size() == configInfos.size();

    }

    private List<ConfigInfo> convert(ObjectNode instParams)
    {
        List<ConfigInfo> res = new ArrayList<>();
        LOGGER.info("check bind config:{}", instParams.toPrettyString());
        if (Objects.nonNull(instParams))
        {
            JsonNode jsonNode = instParams.get("HostConfig").get("Binds");
            if (Objects.nonNull(jsonNode))
            {
                if (jsonNode.isArray())
                {
                    ArrayNode array = (ArrayNode) jsonNode;

                    array.forEach(value -> {
                        String bind = value.asText();

                        if (StringUtils.isNotBlank(bind) && StringUtils.startsWithIgnoreCase(bind, CONFIG_PREFIX))
                        {
                            //host-src:container-dest:ro
                            String[] split = StringUtils.split(bind.substring(CONFIG_PREFIX.length()), ":");
                            String hostSrc = split[0];
                            ConfigInfo configInfo = ConfigInfo.parseCfg(hostSrc);
                            res.add(configInfo);
                        }
                    });
                }
            }
        }

        return res;
    }

    public  Pair<List<String>, List<ContainerConfig>> convert(String containerParams)
    {
        Pair<List<String>, List<ContainerConfig>> pair = new Pair<>();
        List<ContainerConfig> containerConfigs = new ArrayList<>();
        List<String> containerVolumes = new ArrayList<>();
        if (StringUtils.isNotBlank(containerParams))
        {
            ObjectNode instParams = null;
            try
            {
                instParams = (ObjectNode) JacksonUtils.objectMapper.readTree(containerParams);
                LOGGER.info("check bind config:{}", instParams.toPrettyString());
            }
            catch (JsonProcessingException e)
            {
                LOGGER.error("parse container params error:{}", e);
                return pair;
            }

            if (Objects.nonNull(instParams))
            {
                JsonNode jsonNode = instParams.get("HostConfig").get("Binds");
                if (Objects.nonNull(jsonNode))
                {
                    if (jsonNode.isArray())
                    {
                        ArrayNode array = (ArrayNode) jsonNode;

                        array.forEach(value ->
                        {
                            String bind = value.asText();

                            if (StringUtils.isNotBlank(bind))
                            {
                                if (StringUtils.startsWithIgnoreCase(bind, CONFIG_PREFIX))
                                {
                                    //host-src:container-dest:ro
                                    String[] split = StringUtils.split(bind.substring(CONFIG_PREFIX.length()), ":");
                                    String hostSrc = split[0];
                                    ConfigInfo configInfo = ConfigInfo.parseCfg(hostSrc);
                                    ContainerConfig containerConfig = new ContainerConfig();
                                    containerConfig.setUserId(configInfo.getUserId());
                                    containerConfig.setGroup(configInfo.getGroup());
                                    containerConfig.setDataId(configInfo.getDataId());
                                    containerConfig.setContainerDest(split.length > 1 ? split[1] : "");
                                    containerConfig.setReadOnly(split.length > 2 ? StringUtils.isNotBlank(split[2]) && StringUtils.equalsIgnoreCase(split[2], "ro") : false);
                                    containerConfigs.add(containerConfig);
                                }
                                else
                                {
                                    containerVolumes.add(bind);
                                }

                            }
                        });
                    }
                }
            }

        }

        pair.setKey(containerVolumes);
        pair.setValue(containerConfigs);
        return pair;
    }

    private List<ConfigInfo> verify(List<ConfigInfo> configInfos)
    {
        // Verify that there is a corresponding config in config center
        if (!CollectionUtils.isEmpty(configInfos))
        {
            List<CompletableFuture<ConfigInfo>> futureList = configInfos.stream().map(configInfo ->
                    CompletableFuture.supplyAsync(() ->
                    {

                        if (Objects.nonNull(configInfo))
                        {
                            Boolean exists = combRpcService.getSysService().exists(configInfo.getUserId(), configInfo.getGroup(), configInfo.getDataId());
                            return exists ? configInfo : null;
                        }

                        return null;
                    }).exceptionally(er ->
                    {
                        LOGGER.error("verify config info:{} error: {}", configInfo, er);
                        return null;
                    })
            ).collect(Collectors.toList());
            return futureList.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    private void createCfgdataContainerInfo(List<ConfigInfo> configInfos, TblContainerInstInfo tblContainerInstInfo)
    {
        configInfos.stream().forEach(configInfo -> {
            Pair<TblCfgdataContainerInfo, SysService.ConfigInfoBase> configPair = doCreateCfgdataContainerInfo(configInfo, tblContainerInstInfo);
            if (Objects.nonNull(configPair))
            {
                sendConfigToEdge(configPair);
            }

        });
    }

    private Pair<TblCfgdataContainerInfo, SysService.ConfigInfoBase> doCreateCfgdataContainerInfo(ConfigInfo configInfo, TblContainerInstInfo tblContainerInstInfo)
    {

        try
        {
            Pair pair = CompletableFuture.supplyAsync(() ->
            {
                if (Objects.nonNull(configInfo))
                {
                    SysService.ConfigInfoBase config = null;
                    try
                    {
                        config = combRpcService.getSysService().getConfig(configInfo.getUserId(), configInfo.getGroup(), configInfo.getDataId());
                    }
                    catch (Exception e)
                    {
                        LOGGER.error("get config:{} error: {}", configInfo, e);
                    }
                    if (Objects.nonNull(config))
                    {
                        TblCfgdataContainerInfo tblCfgdataContainerInfo = new TblCfgdataContainerInfo();
                        String cfgVolumeId = Utils.assignUUId();
                        tblCfgdataContainerInfo.setCfgVolumeId(cfgVolumeId);
                        tblCfgdataContainerInfo.setDataId(config.getDataId());
                        tblCfgdataContainerInfo.setDataGroup(config.getGroup());
                        tblCfgdataContainerInfo.setDataHash(config.getMd5());
                        tblCfgdataContainerInfo.setUserId(config.getUserId());
                        tblCfgdataContainerInfo.setNodeId(tblContainerInstInfo.getNodeId());
                        tblCfgdataContainerInfo.setContainerId(tblContainerInstInfo.getInstId());
                        tblCfgdataContainerInfo.setCreateTime(new Date());
                        tblCfgdataContainerInfo.setSyncState(SYNCING);
                        cfgRepository.insertSelective(tblCfgdataContainerInfo);
                        return new Pair(tblCfgdataContainerInfo, config);
                    }
                }
                return null;
            }).exceptionally(er ->
            {
                LOGGER.error("verify config info:{} error: {}", configInfo, er);
                return null;
            }).get();
            return pair;
        }
        catch (InterruptedException | ExecutionException e)
        {
           LOGGER.error("get config info base error:{}", e);
           return null;
        }
    }


    private void sendConfigToEdge(Pair<TblCfgdataContainerInfo, SysService.ConfigInfoBase> configInfo)
    {
        MessagePack msgPack = new MessagePack();
        msgPack.setMsgType(CisMsgType.CONFIG_FILE_CREATE);
        msgPack.setMessageObj(configInfo);
        specMsgProcessStrategy.sendMessage(msgPack);
    }

    private void createNewInst(String instId, String failoverRange)
    {
        Pair<String, String> failoverPair = new Pair<>(instId, failoverRange);
        MessagePack messagePack = new MessagePack();
        messagePack.setMsgType(CisMsgType.INST_FAILOVER);
        messagePack.setMessageObj(failoverPair);
        specMsgProcessStrategy.sendMessage(messagePack);
    }


    private void modifyContainerConfig(ObjectNode instParams, String instId)
    {
        JsonNode jsonNode = instParams.get("HostConfig").get("Binds");
        if (Objects.nonNull(jsonNode))
        {
            if (jsonNode.isArray())
            {
                ArrayNode newBinds = ((ObjectNode) instParams.get("HostConfig")).putArray("Binds");
                ArrayNode array = (ArrayNode) jsonNode;

                array.forEach(value -> {
                    String bind = value.asText();

                    if (StringUtils.isNotBlank(bind))
                    {
                        if (StringUtils.startsWithIgnoreCase(bind, CONFIG_PREFIX))
                        {
                            String replaceBind = StringUtils.replace(bind, CONFIG_PREFIX, EDGE_CONFIG_FILE_PREFIX + instId + "/");
                            newBinds.add(replaceBind);
                        }
                        else
                        {
                            newBinds.add(bind);
                        }
                    }
                });
            }
        }

    }

}