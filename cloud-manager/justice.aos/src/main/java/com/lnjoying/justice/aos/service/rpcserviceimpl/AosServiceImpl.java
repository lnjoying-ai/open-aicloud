package com.lnjoying.justice.aos.service.rpcserviceimpl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lnjoying.justice.aos.common.StackOperatorType;
import com.lnjoying.justice.aos.common.StackState;
import com.lnjoying.justice.aos.db.model.TblStackInfo;
import com.lnjoying.justice.aos.db.model.TblStackInfoExample;
import com.lnjoying.justice.aos.db.model.TblStackSpecInfo;
import com.lnjoying.justice.aos.db.repo.StackRepository;
import com.lnjoying.justice.aos.facade.HelmFacade;
import com.lnjoying.justice.aos.facade.NodeStackFacade;
import com.lnjoying.justice.aos.facade.StackServiceFacade;
import com.lnjoying.justice.aos.handler.actiondescription.i18n.zh_cn.StackInfoActionDescriptionTemplate;
import com.lnjoying.justice.aos.handler.actiondescription.i18n.zh_cn.StackSpecActionDescriptionTemplate;
import com.lnjoying.justice.aos.handler.resourcesupervisor.StackInfoResourceSupervisor;
import com.lnjoying.justice.aos.handler.resourcesupervisor.StackSpecResourceSupervisor;
import com.lnjoying.justice.aos.handler.resourcesupervisor.statedict.StackStateDesProvider;
import com.lnjoying.justice.commonweb.handler.operationevent.model.BizModelStateInfo;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionTemplateFields;
import com.lnjoying.justice.commonweb.util.*;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.schema.entity.scheduler.BindRelation;
import com.lnjoying.justice.schema.entity.scheduler.BindResourcesResult;
import com.lnjoying.justice.schema.entity.scheduler.DstNode;
import com.lnjoying.justice.schema.entity.scheduler.SchedulerResult;
import com.lnjoying.justice.schema.entity.servicemanager.TargetServiceInfo;
import com.lnjoying.justice.schema.msg.*;
import com.lnjoying.justice.schema.service.aos.AosService;
import com.micro.core.common.Pair;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@RpcSchema(schemaId = "aosService")
public class AosServiceImpl implements AosService
{
	private static Logger LOGGER = LogManager.getLogger();

	@Autowired
	StackServiceFacade stackServiceFacade;

	@Autowired
	NodeStackFacade nodeStackFacade;

	@Autowired
	StackRepository stackRepo;

	@Autowired
	StackSpecResourceSupervisor stackSpecResourceSupervisor;

	@Autowired
	StackInfoResourceSupervisor stackInfoResourceSupervisor;

	@Autowired
	private HelmFacade helmFacade;

	private static final Set<Integer> noMatchNodeSchedulerStatusSet = Sets.newHashSet(SchedulerState.NO_MATCHED_REGION.getCode(),
			SchedulerState.NO_MATCHED_SITE.getCode(), SchedulerState.NO_MATCHED_NODE.getCode());

	private static final List<Integer> cloudRemoveStatusList = Lists.newArrayList(StackState.SPAWNED_CLOUD_REMOVE, StackState.FIN_CLOUD_REMOVE);

	@Override
	public int deliver(@ApiParam(name = "message")EdgeMessage edgeMessage)
	{

		try
		{
			EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.parseFrom(edgeMessage.getNetMessage());
			LOGGER.info("deliver msg: {}", netMessage);

			switch (netMessage.getHead().getMsgName())
			{
				case MessageName.STACK_OPERATOR:
					processStackOperator(netMessage);
					break;
				case MessageName.FILE_OPERATOR:
					processFileOperator(netMessage);
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

	void processStackOperator(EeNetMessageApi.ee_net_message netMessage)
	{
		EeStackDef.msg_stack_operator_body stackOperatorBody = netMessage.getStackOperatorBody();
		String msgName = stackOperatorBody.getOperatorType();
		switch (msgName)
		{
			case StackOperatorType.RPT_STACK_REQ:
				EeStackDef.msg_rpt_stack_req_body stackRptBody = stackOperatorBody.getRptStackReqBody();
				EeCommonDef.msg_route route                    = netMessage.getRoute();
				stackServiceFacade.processStackRpt(stackRptBody, route);
				break;

			case StackOperatorType.CRETATE_STACK_RSP:
				EeStackDef.msg_create_stack_rsp_body stackCreatRspBody = stackOperatorBody.getCreateStackRspBody();
				stackServiceFacade.processCreateRsp(stackCreatRspBody, netMessage.getRoute());
				break;

			case StackOperatorType.LIFE_MNG_STACK_RSP:
				EeStackDef.msg_life_mng_stack_rsp_body stackLifeMngRspBody = stackOperatorBody.getLifeMngStackRspBody();
				stackServiceFacade.processLifeMngRsp(stackLifeMngRspBody, netMessage.getRoute(), netMessage.getHead().getSessionId());
				break;
			case StackOperatorType.BATCH_RPT_STACK_REQ:
				EeStackDef.msg_batch_rpt_stack_req_body batchRptStackReqBody = stackOperatorBody.getBatchRptStackReqBody();
				stackServiceFacade.processBatchRptStackReq(batchRptStackReqBody, netMessage.getRoute(), netMessage.getHead());
				break;

			case StackOperatorType.LIST_STACK_RSP:
				EeStackDef.msg_list_stack_rsp_body listStackRspBody = stackOperatorBody.getListStackRspBody();
				stackServiceFacade.processListStackRsp(listStackRspBody, netMessage.getRoute());
				break;
		}
	}

	void processFileOperator(EeNetMessageApi.ee_net_message netMessage)
	{
		EeFileDef.msg_file_operator_body fileOperatorBody = netMessage.getFileOperatorBody();
		String msgName = fileOperatorBody.getOperatorType();
		switch (msgName)
		{
			case MessageName.FILE_CREATE_RSP:
				EeFileDef.msg_file_create_rsp_body fileCreateRspBody = fileOperatorBody.getFileCreateRspBody();
				stackServiceFacade.processFileCreateRsp(fileCreateRspBody, netMessage.getHead().getSessionId());
				break;
		}
	}

	@Override
	public void evacuateEdge(@ApiParam(name = "nodeId")String nodeId)
	{
		LOGGER.info("evacuate node: {}", nodeId);
		stackServiceFacade.evacuateStack(nodeId);
	}

	@Override
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
			TblStackSpecInfo tblStackSpecInfo = stackRepo.selectStackSpecInfoByPrimaryKey(schedulerResult.getSpecId());

			if (tblStackSpecInfo == null)
			{
				return null;
			}

			//记录原始状态
			TblStackSpecInfo beforeUpdateStackSpecEntity = DeepCopyUtils.deepCopy(tblStackSpecInfo);

			//查出需要修改状态的记录列表
			List<TblStackInfo> updateStackInfoList = Collections.emptyList();

			Date date = new Date();

			if (schedulerResult.getSchedulerState() == SchedulerState.SUCCESS.getCode()
					&& null != schedulerResult.getBindRelations()
					&& schedulerResult.getBindRelations().size() != 0)
			{
				for (BindRelation bindRelation :schedulerResult.getBindRelations())
				{
					TblStackInfo tblStackInfo = stackRepo.getStack(bindRelation.getWaitAssignId());
					//记录原始状态
					TblStackInfo beforeUpdateStackInfoEntity = DeepCopyUtils.deepCopy(tblStackInfo);
					if (tblStackInfo == null || tblStackInfo.getStatus() != StackState.WAIT_ASSIGN)
					{
						//1 container has been delete
						//2 container not wait assign
						bindResourcesResult.getFailBindRelations().add(bindRelation);
						continue;
					}
					DstNode dstNode = new DstNode();
					dstNode.setDstNodeId(bindRelation.getDstNodeId());
					dstNode.setDstSiteId(bindRelation.getDstSiteId());
					dstNode.setDstRegionId(bindRelation.getDstRegionId());
					tblStackInfo.setDstNode(JsonUtils.toJson(dstNode));
					tblStackInfo.setUpdateTime(date);
					tblStackInfo.setStatus(StackState.ASSIGNED);

					stackRepo.updateStack(tblStackInfo);
					//记录资源更新事件
					publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateStackInfoEntity, "bindResources");
					bindResourcesResult.getSuccessBindRelations().add(bindRelation);
				}

				tblStackSpecInfo.setStatus(StackState.ASSIGNED);
			}
			else if (noMatchNodeSchedulerStatusSet.contains(schedulerResult.getSchedulerState()))
			{
				//set spec status: no match node
				tblStackSpecInfo.setStatus(StackState.NO_MATCH_NODE);

				//记录资源更新事件－数据源准备
				if (!CollectionUtils.isEmpty(schedulerResult.getWaitAssignIdList()))
				{
					TblStackInfoExample stackInfoExample = new TblStackInfoExample();
					stackInfoExample.createCriteria().andStackIdIn(schedulerResult.getWaitAssignIdList());
					updateStackInfoList = stackRepo.getStackList(stackInfoExample);
				}

				//update stack status
				stackRepo.updateStacksStatus(schedulerResult.getWaitAssignIdList(), StackState.NO_MATCH_NODE);

				//记录资源更新事件
				batchPublishStackInfoStatusUpdateEvent(tblStackSpecInfo.getStatus(), updateStackInfoList, "bindResources");
			}
			else if(schedulerResult.getSchedulerState() >= SchedulerState.UNSUPPORTED_PRODUCT_TYPE.getCode()
					&& schedulerResult.getSchedulerState() <= SchedulerState.SCHEDULER_ERROR.getCode())
			{
				//set spec status: sched param check error
				tblStackSpecInfo.setStatus(StackState.CLOUD_CRETAE_FAILED_PARAMS);

				//记录资源更新事件－数据源准备
				if (!CollectionUtils.isEmpty(schedulerResult.getWaitAssignIdList()))
				{
					TblStackInfoExample stackInfoExample = new TblStackInfoExample();
					stackInfoExample.createCriteria().andStackIdIn(schedulerResult.getWaitAssignIdList());
					updateStackInfoList = stackRepo.getStackList(stackInfoExample);
				}

				//update stack status
				stackRepo.updateStacksStatus(schedulerResult.getWaitAssignIdList(), StackState.CLOUD_CRETAE_FAILED_PARAMS);
				//记录资源更新事件
				batchPublishStackInfoStatusUpdateEvent(tblStackSpecInfo.getStatus(), updateStackInfoList, "bindResources");
			}
			else
			{
				//set spec status: cloud system abnormal
				tblStackSpecInfo.setStatus(StackState.CLOUD_SYSTEM_ABNORMAL);

				//记录资源更新事件－数据源准备
				if (!CollectionUtils.isEmpty(schedulerResult.getWaitAssignIdList()))
				{
					TblStackInfoExample stackInfoExample = new TblStackInfoExample();
					stackInfoExample.createCriteria().andStackIdIn(schedulerResult.getWaitAssignIdList());
					updateStackInfoList = stackRepo.getStackList(stackInfoExample);
				}

				//update stack status
				stackRepo.updateStacksStatus(schedulerResult.getWaitAssignIdList(), StackState.CLOUD_SYSTEM_ABNORMAL);
				//记录资源更新事件
				batchPublishStackInfoStatusUpdateEvent(tblStackSpecInfo.getStatus(), updateStackInfoList, "bindResources");
			}
			tblStackSpecInfo.setUpdateTime(date);
			stackRepo.updateSpec(tblStackSpecInfo);
			//记录资源更新事件
			publishStackSpecStatusUpdateEvent(tblStackSpecInfo, beforeUpdateStackSpecEntity);
		}
		return bindResourcesResult;
	}

	private void publishStackSpecStatusUpdateEvent(TblStackSpecInfo tblStackSpecInfo, TblStackSpecInfo beforeUpdateStackSpecEntity)
	{
		try
		{
			Map<Integer, BizModelStateInfo> stackStateDict = StackStateDesProvider.INSTANCE.getStateDesDict().get(StackStateDesProvider.STATUS_FIELD);
			BizModelStateInfo stateInfo = stackStateDict.get(tblStackSpecInfo.getStatus());
			stackSpecResourceSupervisor.publishUpdateEvent("应用规格状态更新", null,
					false, beforeUpdateStackSpecEntity, tblStackSpecInfo,
					"bindResources",
					TemplateEngineUtils.render0(StackSpecActionDescriptionTemplate.Descriptions.UPDATE_SPEC_STATUS,
							false,
							TemplateEngineUtils.newEntry("status", Optional.ofNullable(stateInfo).map(x -> x.getCnDescription())
									.orElse(Optional.ofNullable(tblStackSpecInfo.getStatus())
											.map(x -> x.toString())
											.orElse(""))),
							TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME, tblStackSpecInfo.getSpecName()),
							TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_ID, tblStackSpecInfo.getSpecId())));
		} catch (Exception e)
		{
			LOGGER.error("发布应用规格状态更新事件失败! stackTrace:{}, errorMessage:{}",
					ExceptionUtils.getStackTrace(e), e.getMessage());
		}
	}

	/**
	 * 批量发布StackInfo状态更新事件
	 *
	 * @param stackSpecState
	 * @param unchangedStackInfoList
	 */
	private void batchPublishStackInfoStatusUpdateEvent(Integer stackSpecState, List<TblStackInfo> unchangedStackInfoList, String action)
	{
		if (!CollectionUtils.isEmpty(unchangedStackInfoList))
		{
			for (TblStackInfo tblStackInfo : unchangedStackInfoList)
			{
				TblStackInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblStackInfo);
				tblStackInfo.setStatus(stackSpecState);
				publishStackInfoStatusUpdateEvent(tblStackInfo, beforeUpdateEntity, action);
			}
		}
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

	/**
	 * 发布单个StackInfo状态更新事件
	 *
	 * @param tblStackInfo
	 * @param beforeUpdateEntity
	 * @param action
	 */
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

	@Override
	public boolean isOwner(@ApiParam(name = "stackId") String stackId,
					@ApiParam(name = "bpId") String bpId,
					@ApiParam(name = "userId") String userId)
	{
		return stackServiceFacade.isOwnerOfStack(stackId, null, userId) || stackServiceFacade.isOwnerOfStack(stackId, bpId, userId);
	}

	@Override
	public Pair<TtyStackDeployStatus, String> addNodeStack(@ApiParam(name = "nodeId") String nodeId,
														   @ApiParam(name = "type") String type,
														   @ApiParam(name = "bpId") String bpId,
														   @ApiParam(name = "userId") String userId)
	{
		Pair<TtyStackDeployStatus, String> res = null;

		switch (type)
		{
			case "tty":
				res = nodeStackFacade.addNodeTtyStack(nodeId, type, bpId, userId);
				break;
			default:
				break;
		}
		return  res;
	}

	@Override
	public Pair<TtyStackDeployStatus, String> getNodeStack(@ApiParam(name = "stackId") String stackId)
	{
		return nodeStackFacade.getNodeTtyStack(stackId);
	}

	@Override
	public void deleteNodeStack(@ApiParam(name = "stackId") String stackId)
	{
		nodeStackFacade.toBeDeleted(stackId);
	}

	@Override
	public int getLiteAppNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId)
	{
		TblStackInfoExample example = new TblStackInfoExample();
		TblStackInfoExample.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(userId))
		{
			criteria.andUserIdEqualTo(userId);
		}
		if (StringUtils.isNotEmpty(bpId))
		{
			criteria.andBpIdEqualTo(bpId);
		}
		criteria.andStatusNotIn(cloudRemoveStatusList);
		return (int)stackRepo.countStack(example);
	}

	@Override
	public int getHelmAppNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId)
	{
		return helmFacade.countHelmStack(bpId, userId);
	}

	@Override
	public List<TargetServiceInfo> getStack(@ApiParam(name = "specId")String specId, @ApiParam(name = "stackId")String stackId)
	{
		List<TargetServiceInfo> targetServiceInfos = new ArrayList<>();
		if (stackId != null)
		{
			TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
			DstNode dstNode = JsonUtils.fromJson((String)tblStackInfo.getDstNode(), new com.google.gson.reflect.TypeToken<DstNode>(){}.getType());
			targetServiceInfos.add(new TargetServiceInfo(stackId, dstNode.getDstRegionId(),
					dstNode.getDstSiteId(), dstNode.getDstNodeId(), 0));
		}
		else
		{
			TblStackInfoExample example = new TblStackInfoExample();
			TblStackInfoExample.Criteria criteria = example.createCriteria();
			criteria.andSpecIdEqualTo(specId);
			criteria.andStatusEqualTo(StackState.RUNNING);
			List<TblStackInfo> tblStackInfos = stackRepo.getStack(example);

			tblStackInfos.forEach(tblStackInfo -> {
				DstNode dstNode = JsonUtils.fromJson((String)tblStackInfo.getDstNode(), new com.google.gson.reflect.TypeToken<DstNode>(){}.getType());
				targetServiceInfos.add(new TargetServiceInfo(tblStackInfo.getStackId(), dstNode.getDstRegionId(),
						dstNode.getDstSiteId(), dstNode.getDstNodeId(), 0));
			});
		}
		return targetServiceInfos;
	}

	@Override
	public String getSpecName(@ApiParam(name = "specId")String specId)
	{
		TblStackSpecInfo tblStackSpecInfo = stackRepo.selectStackSpecInfoByPrimaryKey(specId);
		if (Objects.nonNull(tblStackSpecInfo))
		{
			return tblStackSpecInfo.getSpecName();
		}
		return "";
	}

	@Override
	public String getStackName(@ApiParam(name = "stackId")String stackId)
	{
		TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
		if (Objects.nonNull(tblStackInfo))
		{
			return tblStackInfo.getName();
		}
		return "";
	}
	

}
