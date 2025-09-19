package com.lnjoying.justice.cis.facade;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lnjoying.justice.cis.common.constant.CisRedisField;
import com.lnjoying.justice.cis.common.constant.InstAction;
import com.lnjoying.justice.cis.common.constant.InstanceState;
import com.lnjoying.justice.cis.db.model.TblContainerInstInfo;
import com.lnjoying.justice.cis.db.model.TblContainerInstInfoExample;
import com.lnjoying.justice.cis.db.model.TblContainerRunInfo;
import com.lnjoying.justice.cis.db.model.TblContainerSpecInfo;
import com.lnjoying.justice.cis.db.repo.CisRepository;
import com.lnjoying.justice.cis.domain.model.InspectInfo;
import com.lnjoying.justice.cis.handler.actiondescription.i18n.zh_cn.CisContainerInstActionDescriptionTemplate;
import com.lnjoying.justice.cis.handler.actiondescription.i18n.zh_cn.CisContainerSpecActionDescriptionTemplate;
import com.lnjoying.justice.cis.handler.resourcesupervisor.CisContainerInstResourceSupervisor;
import com.lnjoying.justice.cis.handler.resourcesupervisor.CisContainerSpecResourceSupervisor;
import com.lnjoying.justice.cis.handler.resourcesupervisor.statedict.InstanceStateDesProvider;
import com.lnjoying.justice.cis.service.CombRpcService;
import com.lnjoying.justice.cis.service.rpcserviceimpl.ContainerServiceImpl;
import com.lnjoying.justice.cis.util.CisUtils;
import com.lnjoying.justice.cis.webserver.ContainerLogService;
import com.lnjoying.justice.cis.webserver.WsCommonService;
import com.lnjoying.justice.commonweb.handler.operationevent.model.BizModelStateInfo;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionTemplateFields;
import com.lnjoying.justice.commonweb.util.*;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.schema.entity.dev.FailoverPolicy;
import com.lnjoying.justice.schema.entity.scheduler.BindRelation;
import com.lnjoying.justice.schema.entity.scheduler.BindResourcesResult;
import com.lnjoying.justice.schema.entity.scheduler.DstNode;
import com.lnjoying.justice.schema.entity.scheduler.SchedulerResult;
import com.lnjoying.justice.schema.msg.EeCommonDef;
import com.lnjoying.justice.schema.msg.EeInstDef;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 1/7/22 11:52 AM
 */
@Service
public class CisInstServiceFacade implements InstService
{
    @Autowired
    private CisRepository cisRepository;
    
    @Autowired
    CombRpcService combRpcService;

    @Autowired
    NetMessageServiceFacade netMessageServiceFacade;

    @Autowired
    private WsCommonService wsCommonService;

    @Autowired
    private CisContainerInstResourceSupervisor cisContainerInstResourceSupervisor;

    @Autowired
    private CisContainerSpecResourceSupervisor cisContainerSpecResourceSupervisor;

    private static Logger LOGGER = LogManager.getLogger(ContainerServiceImpl.class);
    
    @Override
    public void updateInst4CreateEvent(EeInstDef.inst_status_desc statusInfo)
    {
        try
        {
            //update db
            TblContainerInstInfo record = new TblContainerInstInfo();
            record.setInstId(statusInfo.getInstId());
            record.setStatus(statusInfo.getInstStatus());
            String refId = statusInfo.getRefId();
            Date date = new Date();
            record.setUpdateTime(date);
            record.setReportTime(date);
            if (null != refId)
            {
                record.setRefId(refId);
            }

            TblContainerInstInfo  tblContainerInstInfo= cisRepository.selectByPrimaryKey(statusInfo.getInstId());
            cisRepository.updateByPrimaryKeySelective(record);
            //记录资源更新事件
            TblContainerInstInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblContainerInstInfo);
            BeanUtils.copyNonNullProperties(record, tblContainerInstInfo);
            publishContainerInstanceStatusUpdateEvent(null, tblContainerInstInfo, beforeUpdateEntity, "updateInst4CreateEvent");
        
            //insert new run info
            if (statusInfo.getInstStatus() == InstanceState.RUNNING.getCode())
            {
                TblContainerRunInfo runInfo = cisRepository.getInstLatestRunInfo(statusInfo.getInstId());
                if (null == runInfo || null != runInfo.getStopTime())
                {
                    TblContainerRunInfo tblContainerRunInfo = new TblContainerRunInfo();
                    tblContainerRunInfo.setRunId(Utils.assignUUId());
                    tblContainerRunInfo.setInstId(statusInfo.getInstId());
                    tblContainerRunInfo.setStartTime(record.getUpdateTime());
                    tblContainerRunInfo.setState(record.getStatus());
                    cisRepository.insertRunInfo(tblContainerRunInfo);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LOGGER.error("create container instance rsp exception: {}", e.getMessage());
        }
    }
    
    @Override
    public void updateInstInfo(EeInstDef.inst_status_desc statusInfo)
    {
        //update db
        TblContainerInstInfo record = cisRepository.selectByPrimaryKey(statusInfo.getInstId());
        if (record == null) return;
        Date date = new Date();
        record.setUpdateTime(date);
    
        //update inst state according to rsp status. insert or update running info when needed.
        updateInstState(statusInfo, record);
    }
    
    private void addRptInstReq(EeInstDef.inst_status_desc statusInfo, EeCommonDef.msg_route route)
    {
        TblContainerInstInfo record = new TblContainerInstInfo();
        record.setInstId(statusInfo.getInstId());
        record.setStatus(statusInfo.getInstStatus());
        record.setRefId(statusInfo.getRefId());
        Date date = new Date();
        record.setCreateTime(date);
        record.setUpdateTime(date);
        record.setNodeId(route.getONodeId());
        record.setRegionId(route.getORegionId());
        if (null != statusInfo.getExternInfo())
        {
            record.setInspetContainerParams(statusInfo.getExternInfo());
        }
        
        cisRepository.insertSelective(record);
        //记录资源创建事件
        publishContainerInstCreateEvent(record, "addRptInstReq");

        if (statusInfo.getInstStatus() == InstanceState.RUNNING.getCode())
        {
            TblContainerRunInfo runInfo = cisRepository.getInstLatestRunInfo(record.getInstId());
            if (null != runInfo)
            {
                if (null != statusInfo.getExternInfo())
                {
                    runInfo.setInstDetailInfo(statusInfo.getExternInfo());
                    cisRepository.updateRunInfoByPrimaryKey(runInfo);
                }
            }
            else
            {
                runInfo = new TblContainerRunInfo();
                runInfo.setRunId(Utils.assignUUId());
                runInfo.setInstId(statusInfo.getInstId());
                runInfo.setStartTime(record.getUpdateTime());
                runInfo.setState(record.getStatus());
                if (null != statusInfo.getExternInfo())
                {
                    runInfo.setInstDetailInfo(statusInfo.getExternInfo());
                }
                cisRepository.insertRunInfo(runInfo);
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
    
    @Override
    public void dealInstRpt(EeInstDef.inst_status_desc statusInfo, EeCommonDef.msg_route route)
    {
        TblContainerInstInfo record = cisRepository.selectByPrimaryKey(statusInfo.getInstId());
        if (record != null)
        {
            updateRptInstReq(record, statusInfo);
        }
        else
        {
            addRptInstReq(statusInfo, route);
        }
    }
    
    @Override
    public void evacuateInst(String nodeId)
    {
        TblContainerInstInfo tblContainerInstInfo = new TblContainerInstInfo();
        tblContainerInstInfo.setStatus(InstanceState.FIN_CLOUD_REMOVE.getCode());
        tblContainerInstInfo.setRemoveTime(new Date());
        tblContainerInstInfo.setUpdateTime(new Date());

        TblContainerInstInfoExample example = cisRepository.buildEvacuateInstByNodeIdExample(nodeId);
        List<TblContainerInstInfo> containerInstInfoList = cisRepository.getContainerInstInfosByExample(example);
        cisRepository.updateByExampleSelective(tblContainerInstInfo, example);

        //记录资源更新事件
        if (!CollectionUtils.isEmpty(containerInstInfoList))
        {
            for (TblContainerInstInfo beforeUpdateEntity : containerInstInfoList)
            {
                TblContainerInstInfo containerInstInfo = DeepCopyUtils.deepCopy(beforeUpdateEntity);
                containerInstInfo.setStatus(tblContainerInstInfo.getStatus());
                containerInstInfo.setRemoveTime(tblContainerInstInfo.getRemoveTime());
                containerInstInfo.setUpdateTime(tblContainerInstInfo.getUpdateTime());
                publishContainerInstanceStatusUpdateEvent(null, containerInstInfo, beforeUpdateEntity,
                        "evacuateInst");
            }
        }
    }
    
    public void updateInst(String instId, String instName, String imageName, String cmd, String refId, int status, String userId, String bpId, DstNode dstNode)
    {
        List cmdList = new ArrayList();
        if (! StringUtils.isEmpty(cmd))
        {
            cmdList.add(cmd);
        }
        TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(instId);
        if (tblContainerInstInfo == null)
        {
            tblContainerInstInfo = new TblContainerInstInfo();
            tblContainerInstInfo.setInstId(instId);
            tblContainerInstInfo.setContainerName(instName);
            tblContainerInstInfo.setCreateTime(new Date());
            tblContainerInstInfo.setUpdateTime(tblContainerInstInfo.getCreateTime());
            tblContainerInstInfo.setImageName(imageName);
            tblContainerInstInfo.setCmd(JsonUtils.toJson(cmdList));
            tblContainerInstInfo.setStatus(status);
            tblContainerInstInfo.setRefId(refId);
            if (! StringUtils.isEmpty(userId)) tblContainerInstInfo.setUserId(userId);
            if (! StringUtils.isEmpty(bpId)) tblContainerInstInfo.setBpId(bpId);
            if (dstNode != null)
            {
                tblContainerInstInfo.setNodeId(dstNode.getDstNodeId());
                tblContainerInstInfo.setRegionId(dstNode.getDstRegionId());
                tblContainerInstInfo.setSiteId(dstNode.getDstSiteId());
            }
        
            cisRepository.insertSelective(tblContainerInstInfo);
            //记录资源创建事件
            publishContainerInstCreateEvent(tblContainerInstInfo, "updateInst");
        }
        else
        {
            TblContainerInstInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblContainerInstInfo);

            tblContainerInstInfo.setContainerName(instName);
            tblContainerInstInfo.setImageName(imageName);
            tblContainerInstInfo.setCmd(JsonUtils.toJson(cmdList));
            tblContainerInstInfo.setUpdateTime(new Date());
            tblContainerInstInfo.setStatus(status);
            tblContainerInstInfo.setRefId(refId);
            if (! StringUtils.isEmpty(userId)) tblContainerInstInfo.setUserId(userId);
            if (! StringUtils.isEmpty(bpId)) tblContainerInstInfo.setBpId(bpId);
            if (dstNode != null)
            {
                tblContainerInstInfo.setNodeId(dstNode.getDstNodeId());
                tblContainerInstInfo.setRegionId(dstNode.getDstRegionId());
                tblContainerInstInfo.setSiteId(dstNode.getDstSiteId());
            }

            cisRepository.updateByPrimaryKeySelective(tblContainerInstInfo);
            //记录资源更新事件
            publishContainerInstanceStatusUpdateEvent(null, tblContainerInstInfo, beforeUpdateEntity,
                    "updateInst");
        }
    }
    
    @Override
    public void updateInstStatus(String instId, int status)
    {
        TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(instId);
        if (tblContainerInstInfo == null)
        {
            return;
        }
        TblContainerInstInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblContainerInstInfo);
        tblContainerInstInfo.setStatus(status);
        tblContainerInstInfo.setUpdateTime(new Date());
        cisRepository.updateByPrimaryKey(tblContainerInstInfo);
        //记录资源更新事件
        publishContainerInstanceStatusUpdateEvent(null, tblContainerInstInfo, beforeUpdateEntity,
                "updateInstStatus");
    }
    
    @Override
    public void dealInstLogRsp(int errorCode, String logId, String logContent)
    {
        //log inst error
        if (0  != errorCode)
        {
            //log
            wsCommonService.sendMessageByToken(logId, "log inst error: " + errorCode);
        
            WsCommonService.removeWsTokens(logId);
            WsCommonService.removeSession(logId);
            return;
        }
    
        //send
        ContainerLogService.onLogInstRsp(logId, logContent);
    }
    
    public void dealInstFollowLogRsp(int errorCode, String logId)
    {
        //remote exec error
        if (0  != errorCode)
        {
            //log
            wsCommonService.sendMessageByToken(logId, "log follow error: " + errorCode);
        
            WsCommonService.removeWsTokens(logId);
            WsCommonService.removeSession(logId);
        }
    }
    
    private void updateRptInstReq(TblContainerInstInfo record, EeInstDef.inst_status_desc statusInfo)
    {
        if (StringUtils.isEmpty(record.getRefId()))
        {
            record.setRefId(statusInfo.getRefId());
        }
        Date date = new Date();
        record.setUpdateTime(date);
//        record.setReportTime(date);
        if (StringUtils.isNotBlank(statusInfo.getExternInfo()))
        {
            record.setInspetContainerParams(statusInfo.getExternInfo());
    
            InstanceState state = CisUtils.getDockerInstState(statusInfo.getExternInfo(), statusInfo.getInstStatus());
            statusInfo = statusInfo.toBuilder().setInstStatus(state.getCode()).build();
        }
        
        //update inst state according to rsp status. insert or update running info when needed.
        updateInstState(statusInfo, record);
    }
    
    private void updateInstState(EeInstDef.inst_status_desc statusInfo, TblContainerInstInfo record)
    {
        TblContainerInstInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(record);
        if (StringUtils.isNotEmpty(record.getRestartPolicy()) && record.getRestartPolicy().equals("always"))
        {
            TblContainerSpecInfo tblContainerSpecInfo = cisRepository.getSpec(record.getSpecId());
            FailoverPolicy failoverPolicy = JsonUtils.fromJson(tblContainerSpecInfo.getFailoverPolicy(), FailoverPolicy.class);
            if (failoverPolicy.getNeedFailover() && statusInfo.getInstStatus() == InstanceState.RUNNING.getCode())
            {
                if ((record.getReportTime().getTime() + failoverPolicy.getDelays()) < new Date().getTime())
                {
                    netMessageServiceFacade.lifeMngInstReq(record.getInstId(), record.getRefId(), InstAction.REMOVE.getName(), record.getNodeId());
                    return;
                }
                else
                {
                    RedisUtil.zadd(CisRedisField.CIS_KEEPALIVE_SPEC_INSTIDS + record.getSpecId(), record.getInstId(), (double)record.getUpdateTime().getTime());
                }
            }
        }

        record.setReportTime(record.getUpdateTime());

        try
        {
            record.setStatus(statusInfo.getInstStatus());

            InstanceState stateInMsg = InstanceState.fromCode(statusInfo.getInstStatus());
            //check state in msg
            switch (Objects.requireNonNull(stateInMsg))
            {
                case USER_STOP_QUIT:
                    //User stop quit, tranform state and update last running info.
                    if (record.getStatus() == InstanceState.SPAWN_SYSTEM_STOP.getCode())
                    {
                        record.setStatus(InstanceState.FIN_SYSTEM_STOP.getCode());
                    }
                    else if (record.getStatus() == InstanceState.SPAWN_USER_STOP_QUIT.getCode())
                    {
                        record.setStatus(InstanceState.FIN_USER_STOP_QUIT.getCode());
                    }
                    else if (record.getStatus() == InstanceState.SPAWN_OVERDUE_QUIT.getCode())
                    {
                        record.setStatus(InstanceState.FIN_OVERDUE_QUIT.getCode());
                    }
                    break;
                case SUCCESS_QUIT:
                    //success quit, update last running info.
                case ABNORMAL_QUIT:
                    //abnormal quit, update last running info.
                case SYSTEM_STOP:
                    //User stop quit, keep restart always inst alive
                    if (StringUtils.isNotEmpty(record.getRestartPolicy()) && record.getRestartPolicy().equals("always"))
                    {
                        netMessageServiceFacade.lifeMngInstReq(record.getInstId(), record.getRefId(), InstAction.START.getName(), record.getNodeId());
                    }
                    break;
                case HARDWARE_ERROR:
                    //create fail due to hardware error, release resources
                case NO_IMAGE:
                    //create fail due to no image, release resources
                case IMAGE_DOWNLOAD_FAILED:
                    //create fail due to image download failed, release resources
                case CREATE_FAILED:
                    //create fail, release resources

                    //relesae resources(remove gpu bind and update edge monopoly status: remove)
                    combRpcService.getSchedulerService().releaseBindResources(record.getNodeId(), statusInfo.getInstId());
                    break;
                case REMOVED:
                    //remove, update last running info and remove time.
                case OBJECT_AUTO_REMOVE:
                    record.setStatus(InstanceState.FIN_CLOUD_REMOVE.getCode());
                    record.setRemoveTime(record.getUpdateTime());
                    //relesae resources(remove gpu bind and update edge monopoly status: remove)
                    combRpcService.getSchedulerService().releaseBindResources(record.getNodeId(), statusInfo.getInstId());
                    break;
                case OBJECT_NOT_EXIST:
                    //object not exist, update last running info and remove time.
                    break;
                case KEEP_ON:
                    //keep on, do nothing.
                    return;
                default:
                    break;
            }

            insertOrUpdateRunInfo(record, statusInfo.getExternInfo());
        }
        catch (Exception e)
        {
            LOGGER.error("Error while update inst state, message {}", e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            cisRepository.updateByPrimaryKeySelective(record);
            //记录资源更新事件
            publishContainerInstanceStatusUpdateEvent(null, record, beforeUpdateEntity, "updateInstState");
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
                            TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME, beforeUpdateContainerInstEntity.getContainerName()),
                            TemplateEngineUtils.newEntry("spec_name", Optional.ofNullable(tblContainerSpecInfo)
                                    .map(x -> x.getSpecName())
                                    .orElse("")),
                            TemplateEngineUtils.newEntry("spec_id", beforeUpdateContainerInstEntity.getSpecId())));
        } catch (Exception e)
        {
            LOGGER.error("发布容器状态更新事件失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    private void insertOrUpdateRunInfo(TblContainerInstInfo tblContainerInstInfo, String instDetailInfo)
    {
        InspectInfo inspectInfo = CisUtils.getDockerInstInspectInfo(instDetailInfo);
        TblContainerRunInfo runInfo = cisRepository.getInstLatestRunInfo(tblContainerInstInfo.getInstId());
        if (null == runInfo)
        {
            runInfo = new TblContainerRunInfo();
            runInfo.setRunId(Utils.assignUUId());
            runInfo.setInstId(tblContainerInstInfo.getInstId());
            if (inspectInfo == null || inspectInfo.getState() == null)
            {
                return;
            }
            if (inspectInfo.getState().getStartedAt().getTime() > 0)
            {
                runInfo.setStartTime(inspectInfo.getState().getStartedAt());
            }
            else
            {
                return;
            }
            if (inspectInfo.getState().getFinishedAt().getTime() > 0)
            {
                runInfo.setStopTime(inspectInfo.getState().getFinishedAt());
            }
            runInfo.setState(tblContainerInstInfo.getStatus());
            runInfo.setInstDetailInfo(instDetailInfo);
            cisRepository.insertRunInfo(runInfo);
        }
        else
        {
            if (null == runInfo.getStopTime())
            {
                if (inspectInfo == null || inspectInfo.getState() == null)
                {
                    runInfo.setStopTime(new Date());
                    runInfo.setState(tblContainerInstInfo.getStatus());
                    cisRepository.updateRunInfoByPrimaryKey(runInfo);
                    return;
                }
                if (inspectInfo.getState().getFinishedAt().getTime() > runInfo.getStartTime().getTime())
                {
                    runInfo.setStopTime(inspectInfo.getState().getFinishedAt());
                    runInfo.setState(tblContainerInstInfo.getStatus());
                    runInfo.setInstDetailInfo(instDetailInfo);
                    cisRepository.updateRunInfoByPrimaryKey(runInfo);
                }
            }
            else
            {
                if (inspectInfo == null || inspectInfo.getState() == null)
                {
                    return;
                }
                if (inspectInfo.getState().getStartedAt().getTime() > runInfo.getStartTime().getTime())
                {
                    runInfo = new TblContainerRunInfo();
                    runInfo.setRunId(Utils.assignUUId());
                    runInfo.setInstId(tblContainerInstInfo.getInstId());
                    runInfo.setStartTime(inspectInfo.getState().getStartedAt());
                    if (inspectInfo.getState().getFinishedAt().getTime() > inspectInfo.getState().getStartedAt().getTime())
                    {
                        runInfo.setStopTime(inspectInfo.getState().getFinishedAt());
                    }
                    runInfo.setState(tblContainerInstInfo.getStatus());
                    runInfo.setInstDetailInfo(instDetailInfo);
                    cisRepository.insertRunInfo(runInfo);
                }
                else if (inspectInfo.getState().getStartedAt().getTime() == runInfo.getStartTime().getTime() &&
                        inspectInfo.getState().getFinishedAt().getTime() >= runInfo.getStopTime().getTime())
                {
                    runInfo.setStopTime(inspectInfo.getState().getFinishedAt());
                    runInfo.setState(tblContainerInstInfo.getStatus());
                    runInfo.setInstDetailInfo(instDetailInfo);
                    cisRepository.updateRunInfoByPrimaryKey(runInfo);
                }
            }
        }

    }
    
    private static final Set<Integer> noMatchNodeSchedulerStatusSet = Sets.newHashSet(SchedulerState.NO_MATCHED_REGION.getCode(),
            SchedulerState.NO_MATCHED_SITE.getCode(), SchedulerState.NO_MATCHED_NODE.getCode());

    private static final List<Integer> cloudRemoveStatusList = Lists.newArrayList(InstanceState.SPAWNED_CLOUD_REMOVE.getCode(), InstanceState.FIN_CLOUD_REMOVE.getCode());
    
    public BindResourcesResult bindResources(@ApiParam(name = "schedulerResult") SchedulerResult schedulerResult)
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
            TblContainerSpecInfo tblContainerSpecInfo = cisRepository.getSpec(schedulerResult.getSpecId());
            if (tblContainerSpecInfo == null)
            {
                return null;
            }

            TblContainerSpecInfo beforeUpdateContainerSpecEntity = DeepCopyUtils.deepCopy(tblContainerSpecInfo);
            Date date = new Date();

            if (schedulerResult.getSchedulerState() == SchedulerState.SUCCESS.getCode()
                    && null != schedulerResult.getBindRelations()
                    && schedulerResult.getBindRelations().size() != 0)
            {
                for (BindRelation bindRelation :schedulerResult.getBindRelations())
                {
                    TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(bindRelation.getWaitAssignId());
                    if (tblContainerInstInfo == null || tblContainerInstInfo.getStatus() != InstanceState.WAIT_ASSIGN.getCode())
                    {
                        //1 container has been delete
                        //2 container not wait assign
                        bindResourcesResult.getFailBindRelations().add(bindRelation);
                        continue;
                    }

                    TblContainerInstInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblContainerInstInfo);

                    tblContainerInstInfo.setNodeId(bindRelation.getDstNodeId());
                    tblContainerInstInfo.setSiteId(bindRelation.getDstSiteId());
                    tblContainerInstInfo.setRegionId(bindRelation.getDstRegionId());
                
                    tblContainerInstInfo.setStatus(InstanceState.ASSIGNED.getCode());
                
                    tblContainerInstInfo.setUpdateTime(date);
                    cisRepository.updateByPrimaryKeySelective(tblContainerInstInfo);
                    //记录资源更新事件
                    publishContainerInstanceStatusUpdateEvent(tblContainerSpecInfo, tblContainerInstInfo, beforeUpdateEntity, "bindResources");
                    bindResourcesResult.getSuccessBindRelations().add(bindRelation);
                }
                tblContainerSpecInfo.setStatus(InstanceState.ASSIGNED.getCode());
            }
            else if (noMatchNodeSchedulerStatusSet.contains(schedulerResult.getSchedulerState()))
            {
                //set spec status: no match node
                tblContainerSpecInfo.setStatus(InstanceState.NO_MATCH_NODE.getCode());
            
                //update inst status
                cisRepository.updateInstsStatus(schedulerResult.getWaitAssignIdList(), tblContainerSpecInfo.getStatus());
                if (!CollectionUtils.isEmpty(schedulerResult.getWaitAssignIdList()))
                {
                    TblContainerInstInfoExample example = cisRepository.buildUpdateContainerInstInfoByInstIdListExample(schedulerResult.getWaitAssignIdList());
                    List<TblContainerInstInfo> waitAssignContainerInstList = cisRepository.selectByExample(example);
                    //记录资源更新事件
                    publishContainerInstanceStatusUpdateEvent(tblContainerSpecInfo, waitAssignContainerInstList, "bindResources");
                }
            }
            else if (schedulerResult.getSchedulerState() >= SchedulerState.UNSUPPORTED_PRODUCT_TYPE.getCode()
                    && schedulerResult.getSchedulerState() <= SchedulerState.SCHEDULER_ERROR.getCode())
            {
                //set spec status: sched param check error
                tblContainerSpecInfo.setStatus(InstanceState.CLOUD_CRETAE_FAILED_PARAMS.getCode());
            
                //update inst status
                if (!CollectionUtils.isEmpty(schedulerResult.getWaitAssignIdList()))
                {
                    TblContainerInstInfoExample example = cisRepository.buildUpdateContainerInstInfoByInstIdListExample(schedulerResult.getWaitAssignIdList());
                    List<TblContainerInstInfo> waitAssignContainerInstList = cisRepository.selectByExample(example);
                    //记录资源更新事件
                    publishContainerInstanceStatusUpdateEvent(tblContainerSpecInfo, waitAssignContainerInstList, "bindResources");
                }
            }
            else
            {
                //set spec status: cloud system abnormal
                tblContainerSpecInfo.setStatus(InstanceState.CLOUD_SYSTEM_ABNORMAL.getCode());
            
                //update inst status
                if (!CollectionUtils.isEmpty(schedulerResult.getWaitAssignIdList()))
                {
                    TblContainerInstInfoExample example = cisRepository.buildUpdateContainerInstInfoByInstIdListExample(schedulerResult.getWaitAssignIdList());
                    List<TblContainerInstInfo> waitAssignContainerInstList = cisRepository.selectByExample(example);
                    //记录资源更新事件
                    publishContainerInstanceStatusUpdateEvent(tblContainerSpecInfo, waitAssignContainerInstList, "bindResources");
                }
            }
            tblContainerSpecInfo.setUpdateTime(date);
            cisRepository.updateSpec(tblContainerSpecInfo);
            //记录资源更新事件
            publishContainerSpecStatusUpdateEvent(tblContainerSpecInfo, beforeUpdateContainerSpecEntity, "bindResources");
        }
        return bindResourcesResult;
    }

    private void publishContainerSpecStatusUpdateEvent(TblContainerSpecInfo tblContainerSpecInfo, TblContainerSpecInfo beforeUpdateEntity, String action)
    {
        try
        {
            if (Objects.equals(tblContainerSpecInfo.getStatus(), beforeUpdateEntity.getStatus()))
            {
                LOGGER.debug("容器部署 {} 状态未生变化，忽略", beforeUpdateEntity.getSpecName());
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
    
    public void dealStartShellRsp(int errorCode, String execId)
    {
        wsCommonService.sendMessageByToken(execId, "start shell error: " + errorCode);
    
        WsCommonService.removeWsTokens(execId);
        WsCommonService.removeSession(execId);
    }

    public int getContainerNum(String userId, String bpId)
    {
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
        criteria.andSpecIdIsNotNull();
        criteria.andStatusNotIn(cloudRemoveStatusList);
        return (int)cisRepository.countByExample(example);
    }
}
