package com.lnjoying.justice.aos.service.rpcserviceimpl;

import com.lnjoying.justice.aos.common.ExposePort;
import com.lnjoying.justice.aos.common.ExposePortInfo;
import com.lnjoying.justice.aos.common.SimpleStackStatusEnum;
import com.lnjoying.justice.aos.common.StackSearchCritical;
import com.lnjoying.justice.aos.db.model.TblStackInfo;
import com.lnjoying.justice.aos.db.repo.StackRepository;
import com.lnjoying.justice.aos.domain.dto.rsp.GetStackListRsp;
import com.lnjoying.justice.aos.domain.model.StackDeployInfo;
import com.lnjoying.justice.aos.domain.model.StackInfo;
import com.lnjoying.justice.aos.facade.StackServiceFacade;
import com.lnjoying.justice.aos.handler.actiondescription.i18n.zh_cn.StackInfoActionDescriptionTemplate;
import com.lnjoying.justice.aos.handler.resourcesupervisor.StackInfoResourceSupervisor;
import com.lnjoying.justice.aos.handler.resourcesupervisor.statedict.StackStateDesProvider;
import com.lnjoying.justice.aos.service.CombRpcService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.operationevent.model.BizModelStateInfo;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionTemplateFields;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.DeepCopyUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.commonweb.util.TemplateEngineUtils;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.entity.aos.AddStackReq;
import com.lnjoying.justice.schema.entity.aos.StackDeploySimpleInfo;
import com.lnjoying.justice.schema.entity.omc.MonitorEndpointInfo;
import com.lnjoying.justice.schema.entity.scheduler.DstNode;
import com.lnjoying.justice.schema.entity.servicemanager.RpcPortMappingResult;
import com.lnjoying.justice.schema.service.aos.AosStackService;
import com.lnjoying.justice.schema.service.servicemanager.ServicePortCallback;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.queryBpId;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.queryUserId;
import static com.lnjoying.justice.schema.common.ErrorCode.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/4 16:32
 */

@Slf4j
@RpcSchema(schemaId = "aosStackService")
public class AosStackServiceImpl implements AosStackService, ServicePortCallback
{
    private static Logger LOGGER = LogManager.getLogger(AosStackServiceImpl.class);

    @Autowired
    StackServiceFacade stackFacade;
    
    @Autowired
    StackRepository stackRepo;

    @Autowired
    private CombRpcService combRpcService;

    @Autowired
    private StackInfoResourceSupervisor stackInfoResourceSupervisor;

    @Override
    public boolean isStackRunningStatus(@ApiParam(name = "stackId") String stackId)
    {
        try
        {
            StackInfo stack = stackFacade.getStack(stackId, null, true);
            if (Objects.nonNull(stack))
            {
                if (SimpleStackStatusEnum.RUNNING.equals(SimpleStackStatusEnum.getSimpleStatus(stack.getStatus().getCode())))
                {
                    return true;
                }

                return false;
            }
        }
        catch (Exception e)
        {
            log.error("get stack status failed");
        }

        return false;
    }

    @Override
    public boolean deleteStack(@ApiParam(name = "stackId") String stackId)
    {
        try
        {
            stackFacade.deleteStack(stackId, null);
            return true;
        }
        catch (Exception e)
        {
            ErrorCode code = ((WebSystemException) e).getCode();
            if (code.equals(STACK_NOT_EXIST) || code.equals(STACK_DROPPED))
            {
                return true;
            }
            log.error("delete stack by omc failed:{}", e);
            return false;
        }
    }
    
    /**
     * 需要根据service manager的接口做变更，对方需要返回port id
     * 根据接收的分配的端口信息进行设置端口的映射信息
     * @param portMappingResult
     * @return
     */
    @Override
    public CombRetPacket addServicePortCallback(@ApiParam(name = "portMappingResult") RpcPortMappingResult portMappingResult)
    {
        String reqId = portMappingResult.getReqId();
        String portId = portMappingResult.getServicePortId();
        String stackId = reqId;
        
        log.info("receive port mapping result,req id:{}, port id: {}", reqId, portId);
        
        CombRetPacket combRetPacket = new CombRetPacket();
        if (CollectionUtils.isEmpty(portMappingResult.getTargetServices()))
        {
            return combRetPacket;
        }
        
        TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
        if (tblStackInfo == null)
        {
            combRetPacket.setStatus(STACK_NOT_EXIST.getCode());
            return combRetPacket;
        }

        TblStackInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblStackInfo);
        ExposePortInfo exposePortInfo = JsonUtils.fromJson((String)tblStackInfo.getExposePorts(), ExposePortInfo.class);
        for (RpcPortMappingResult.TargetService targetService : portMappingResult.getTargetServices())
        {
            log.info("receive port mapping result, endpoint {}, target port:{}, service port id:{}",
                    targetService.getEndpoint(), targetService.getTargetPort(), targetService.getTargetServiceId());
            ExposePort exposePort = exposePortInfo.getExposePortMap().get(targetService.getTargetPort());
            if (exposePort == null)
            {
                continue;
            }
            exposePort.setPortId(targetService.getTargetServiceId());
            exposePort.setCloudEP(targetService.getEndpoint());

            if (exposePortInfo.getPurpose().equals("inner_monitor"))
            {
                sendEndpointInfoToOmc(tblStackInfo, targetService, exposePort);
            }


        }
    
        tblStackInfo.setExposePorts(exposePortInfo);
        stackRepo.updateStack(tblStackInfo);
        //记录资源更新事件
        publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "addServicePortCallback");
        return combRetPacket;
    }

    private void publishStackInfoUpdateEvent(TblStackInfo tblStackInfo, TblStackInfo beforeUpdateEntity, String action)
    {
        try
        {
            if (tblStackInfo == null || beforeUpdateEntity == null)
            {
                LOGGER.debug("tblStackInfo或beforeUpdateEntity为null，跳过发布应用信息更新事件");
                return;
            }

            if (!Objects.equals(tblStackInfo.getStatus(), beforeUpdateEntity.getStatus()))
            {
                publishStackInfoStatusUpdateEvent(tblStackInfo, beforeUpdateEntity, action);
                return;
            }

            stackInfoResourceSupervisor.publishUpdateEvent("应用信息更新",
                    beforeUpdateEntity.getName(), false, beforeUpdateEntity,
                    tblStackInfo, action,
                    TemplateEngineUtils.render0(StackInfoActionDescriptionTemplate.Descriptions.UPDATE_STACK,
                            false,
                            TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME, tblStackInfo.getName()))
            );
        } catch (Exception e)
        {
            LOGGER.error("发布应用信息更新事件失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    private void publishStackInfoStatusUpdateEvent(TblStackInfo tblStackInfo, TblStackInfo beforeUpdateEntity, String action)
    {
        try
        {
            if (Objects.equals(tblStackInfo.getStatus(), beforeUpdateEntity.getStatus()))
            {
                LOGGER.debug("应用 {} 状态未发生变化, 忽略", beforeUpdateEntity.getName());
                return;
            }
            Map<Integer, BizModelStateInfo> stackStateDesDict = StackStateDesProvider.INSTANCE.getStateDesDict().get(StackStateDesProvider.STATUS_FIELD);
            BizModelStateInfo statusInfo = stackStateDesDict.get(tblStackInfo.getStatus());
            stackInfoResourceSupervisor.publishUpdateEvent("应用状态更新", null,
                    false, beforeUpdateEntity, tblStackInfo, action,
                    TemplateEngineUtils.render0(StackInfoActionDescriptionTemplate.Descriptions.STACK_STATUS_UPDATE,
                            false,
                            TemplateEngineUtils.newEntry("status",
                                    Optional.ofNullable(statusInfo)
                                            .map(x -> x.getCnDescription())
                                            .orElse(Optional.ofNullable(tblStackInfo.getStatus())
                                                    .map(x -> x.toString())
                                                    .orElse(""))),
                            TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
                                    tblStackInfo.getName())));
        } catch (Exception e)
        {
            LOGGER.error("发布应用状态更新事件失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }


    /**
     * 创建一个应用的部署要求，返回部署的spec id
     * @param addStackReq
     * @return
     */
    @Override
    public String createStackSpec(@ApiParam(name = "addStackReq") AddStackReq addStackReq)
    {
        return stackFacade.addStack(addStackReq, addStackReq.getBp_id(), addStackReq.getUser_id());
    }
    
    /**
     * 删除stack
     * @param specId
     * @return
     */
    @Override
    public int removeStackSpec(@ApiParam(name = "specId") String specId)
    {
        try
        {
            stackFacade.deleteStackDeployInfo(specId, null);
        }
        catch (WebSystemException exception)
        {
            ErrorCode code = ((WebSystemException) exception).getCode();
            if (code.equals(STACK_NOT_EXIST) || code.equals(STACK_DROPPED))
            {
                return 0;
            }
            return exception.getCode().getCode();
        }
        catch (Exception e)
        {
            log.error("delete stack deploy info failed");
            return SystemError.getCode();
        }
        
        return 0;
        
    }

    @Override
    public StackDeploySimpleInfo getStackDeployInfo(@ApiParam(name = "specId")String specId)
    {
        StackDeployInfo stackDeployInfo = stackFacade.getStackDeployInfo(specId);
        if (Objects.nonNull(stackDeployInfo))
        {
            StackDeploySimpleInfo stackDeploySimpleInfo = StackDeploySimpleInfo.builder()
                    .name(stackDeployInfo.getName())
                    .id(stackDeployInfo.getId())
                    .replicaNum(stackDeployInfo.getReplicaNum())
                    .readyNum(stackDeployInfo.getReadyNum())
                    .processingNum(stackDeployInfo.getProcessingNum())
                    .failedNum(stackDeployInfo.getFailedNum())
                    .availableNum(stackDeployInfo.getAvailableNum()).build();
            return stackDeploySimpleInfo;
        }
        return null;
    }

    @Override
    public List<MonitorEndpointInfo> getMonitorEndpointList(@ApiParam(name = "specId") String specId)
    {

        List<MonitorEndpointInfo> result = new ArrayList<>();
        try
        {
            StackSearchCritical critical = new StackSearchCritical();
            critical.setSpecId(specId);
            GetStackListRsp stackDeployInstancesInfo = stackFacade.getStackDeployInstancesInfo(critical);
            if (Objects.nonNull(stackDeployInstancesInfo))
            {
                stackDeployInstancesInfo.getStacks().forEach(stackInfo -> {

                    List<MonitorEndpointInfo> monitorEndpointInfoList = stackInfo.getPort_maps().stream().map(exposePort ->
                    {
                        MonitorEndpointInfo monitorEndpointInfo = new MonitorEndpointInfo();
                        String cloudEP = exposePort.getCloudEP();
                        monitorEndpointInfo.setUniqueId(exposePort.getPortId());
                        monitorEndpointInfo.setBpId(stackInfo.getBp_id());
                        monitorEndpointInfo.setUserId(stackInfo.getUser_id());
                        StackInfo.FullDstNode dstNode = stackInfo.getDst_node();
                        if (Objects.nonNull(dstNode))
                        {
                            monitorEndpointInfo.setNodeId(dstNode.getDstNodeId());
                            monitorEndpointInfo.setSiteId(dstNode.getDstSiteId());
                        }

                        monitorEndpointInfo.setType(exposePort.getType());
                        monitorEndpointInfo.setEndpoint(cloudEP);
                        return monitorEndpointInfo;
                    }).collect(Collectors.toList());
                    result.addAll(monitorEndpointInfoList);
                });
            }

        }
        catch (Exception e)
        {
            log.error("send endpoint info to omc failed:{}", e);
        }

        return result;
    }

    private void sendEndpointInfoToOmc(TblStackInfo tblStackInfo, RpcPortMappingResult.TargetService targetService, ExposePort exposePort)
    {
        try
        {
            MonitorEndpointInfo monitorEndpointInfo = new MonitorEndpointInfo();
            monitorEndpointInfo.setUniqueId(exposePort.getPortId());
            monitorEndpointInfo.setBpId(tblStackInfo.getBpId());
            monitorEndpointInfo.setUserId(tblStackInfo.getUserId());
            DstNode dstNode = JsonUtils.fromJson((String) tblStackInfo.getDstNode(), DstNode.class);
            monitorEndpointInfo.setNodeId(dstNode.getDstNodeId());
            monitorEndpointInfo.setSiteId(dstNode.getDstSiteId());
            monitorEndpointInfo.setType(exposePort.getType());
            monitorEndpointInfo.setEndpoint(targetService.getEndpoint());
            combRpcService.getOmcService().addMonitorEndpoint(monitorEndpointInfo);
        }
        catch (Exception e)
        {
            log.error("send endpoint info to omc failed:{}", e);
        }

    }
}
