package com.lnjoying.justice.aos.facade;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.lnjoying.justice.aos.common.*;
import com.lnjoying.justice.aos.db.model.*;
import com.lnjoying.justice.aos.db.repo.StackRepository;
import com.lnjoying.justice.aos.db.repo.TemplateRepository;
import com.lnjoying.justice.aos.domain.CFG;
import com.lnjoying.justice.aos.domain.dto.req.AddServiceReq;
import com.lnjoying.justice.aos.domain.dto.req.UpdateStackCfgReq;
import com.lnjoying.justice.aos.domain.dto.req.UpdateStackReq;
import com.lnjoying.justice.aos.domain.dto.rsp.*;
import com.lnjoying.justice.aos.domain.model.*;
import com.lnjoying.justice.aos.handler.actiondescription.i18n.zh_cn.StackInfoActionDescriptionTemplate;
import com.lnjoying.justice.aos.handler.actiondescription.i18n.zh_cn.StackSpecActionDescriptionTemplate;
import com.lnjoying.justice.aos.handler.actiondescription.i18n.zh_cn.StackTemplateActionDescriptionTemplate;
import com.lnjoying.justice.aos.handler.resourcesupervisor.StackInfoResourceSupervisor;
import com.lnjoying.justice.aos.handler.resourcesupervisor.StackSpecResourceSupervisor;
import com.lnjoying.justice.aos.handler.resourcesupervisor.TemplateResourceSupervisor;
import com.lnjoying.justice.aos.handler.resourcesupervisor.statedict.RecordStatusDesProvider;
import com.lnjoying.justice.aos.handler.resourcesupervisor.statedict.StackStateDesProvider;
import com.lnjoying.justice.aos.process.processor.ProcessorName;
import com.lnjoying.justice.aos.process.service.StackMsgProcessStrategy;
import com.lnjoying.justice.aos.service.CombRpcService;
import com.lnjoying.justice.aos.util.AosTemplateUtil;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.operationevent.IBizModelStateDesProvider;
import com.lnjoying.justice.commonweb.handler.operationevent.model.BizModelStateInfo;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionTemplateFields;
import com.lnjoying.justice.commonweb.util.*;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.common.EventType;
import com.lnjoying.justice.schema.common.scheduler.LabelType;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.schema.constant.CfgStatus;
import com.lnjoying.justice.schema.constant.ConfigConstants;
import com.lnjoying.justice.schema.entity.TipMessage;
import com.lnjoying.justice.schema.entity.aos.AddStackReq;
import com.lnjoying.justice.schema.entity.dev.*;
import com.lnjoying.justice.schema.entity.scheduler.AssignEdge2StackReq;
import com.lnjoying.justice.schema.entity.scheduler.DstNode;
import com.lnjoying.justice.schema.entity.servicemanager.RpcAddServicePortReq;
import com.lnjoying.justice.schema.msg.*;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import com.lnjoying.justice.schema.service.ims.ImsRegistryService;
import com.lnjoying.justice.schema.service.sys.SysService;
import com.lnjoying.justice.schema.service.ums.UmsService;
import com.micro.core.common.Pair;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

import static com.lnjoying.justice.aos.common.AosLabels.*;
import static com.lnjoying.justice.aos.common.StackState.*;
import static com.lnjoying.justice.aos.domain.model.StackDeployInfo.DeployStage.*;
import static com.lnjoying.justice.schema.common.ErrorCode.*;
import static com.lnjoying.justice.schema.common.ErrorLevel.ERROR;
import static com.lnjoying.justice.schema.common.ErrorLevel.INFO;

/**
 * stack facade, provide function for stack, service, inst.
 */
@Service
public class StackServiceFacade
{
	private static Logger LOGGER = LogManager.getLogger();

	JsonParser jsonParser = new JsonParser();
	Yaml yaml = new Yaml();
	private static final Yaml customYaml = new Yaml(new DockerComposeYaml.DockerComposeRepresenter());

	@Autowired
	StackRepository stackRepo;

	@Autowired
	TemplateRepository templateRepo;

	@Autowired
	StackMsgProcessStrategy stackMsgProcessStrategy;

	@Autowired
    CombRpcService combRpcService;

	@Autowired
	NetMessageServiceFacade netMessageServiceFacade;

	@Autowired
	LabelProperty labelProperty;

	@Autowired
	StackSpecResourceSupervisor stackSpecResourceSupervisor;

	@Autowired
	StackInfoResourceSupervisor stackInfoResourceSupervisor;

	@Autowired
	TemplateResourceSupervisor templateResourceSupervisor;
	
	private static final Set<Integer> finRemoveStatusSet = Sets.newHashSet(StackState.FIN_CLOUD_REMOVE);
	private static final Set<Integer> cloudRemoveStatusSet = Sets.newHashSet(StackState.SPAWNED_CLOUD_REMOVE, StackState.FIN_CLOUD_REMOVE);
	private static final Set<Integer> edgeRemoveStatusSet = Sets.newHashSet(StackState.REMOVED, StackState.OBJECT_NOT_EXIST, StackState.OBJECT_AUTO_REMOVE);
	private static final List<Integer> cloudRemoveStatusList = Lists.newArrayList(StackState.SPAWNED_CLOUD_REMOVE, StackState.FIN_CLOUD_REMOVE);

	/**
	 * add stack to db
	 * @param req
	 * @param bpId
	 * @param userId
	 * @return
	 * @throws WebSystemException
	 */
	@Transactional(rollbackFor = Exception.class)
	public String addStack(AddStackReq req, String bpId, String userId)
	{
		try
		{
			//replica num can only be 1
			if (req.getReplica_num() == 0)
			{
				throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
			}

			 String bpIdReq = req.getBp_id();
			 String userIdReq = req.getUser_id();
			 if (StringUtils.isNotBlank(bpIdReq) && StringUtils.isBlank(userIdReq))
			 {
			 	//assemble master user id for stack service query
				 UmsService.UserInfo masterUser = combRpcService.getUmsService().getMasterUserInfoByBpId(bpIdReq);
				 if (Objects.nonNull(masterUser))
				 {
				 	userId = masterUser.getUserId();
				 }
				 bpId = bpIdReq;
			 }

			// insert stack spec info
			TblStackSpecInfo tblStackSpecInfo = new TblStackSpecInfo();
			tblStackSpecInfo.setSpecId(Utils.assignUUId());
			tblStackSpecInfo.setSpecName(req.getName());
			tblStackSpecInfo.setCreateUserId(userId);
			tblStackSpecInfo.setBpId(bpId);
			tblStackSpecInfo.setUserId(userId);
			tblStackSpecInfo.setCreateTime(new Date());
			tblStackSpecInfo.setStackType(req.getStack_type());

			tblStackSpecInfo.setDeployStrategy(req.getDeploy_strategy());
			tblStackSpecInfo.setUseServicePenetration(req.getUse_service_penetration());
			tblStackSpecInfo.setUpdateTime(tblStackSpecInfo.getCreateTime());
			String aosType                  = req.getAos_type();
			Map<String, String> inputParams = req.getInput_params();
			String contentType              = req.getContent_type();
			String stackCompose             = req.getStack_compose();
			String justiceCompose           = req.getJustice_compose();
			if (! StringUtils.isEmpty(req.getTemplate_id()))
			{
				TblStackTemplateInfo templateInfo = templateRepo.getTemplate(req.getTemplate_id());
				//记录原始状态
				TblStackTemplateInfo beforeUpdateTemplateEntity = DeepCopyUtils.deepCopy(templateInfo);
				if (templateInfo == null)
				{
					throw new WebSystemException(ErrorCode.TEMPLATE_NOT_EXIST, ErrorLevel.INFO);
				}
				TblStackTemplateBaseInfo tblStackTemplateBaseInfo = templateRepo.selectBasicInfoByPrimaryKey(templateInfo.getRootId());
				if (tblStackTemplateBaseInfo == null)
				{
					throw new WebSystemException(ErrorCode.TEMPLATE_NOT_EXIST, ErrorLevel.INFO);
				}

				if (templateInfo.getIsUsed() == null || templateInfo.getIsUsed() == true)
				{
					templateInfo.setIsUsed(true);
					templateRepo.updateTemplate(templateInfo);
					//记录资源更新事件
					String resourceInstanceName = String.format("%s:%s", tblStackTemplateBaseInfo.getName(),
							beforeUpdateTemplateEntity.getVersion());
					templateResourceSupervisor.publishUpdateEvent("应用模板更新", resourceInstanceName, false,
							beforeUpdateTemplateEntity, templateInfo, "addStack",
							TemplateEngineUtils.render0(StackTemplateActionDescriptionTemplate.Description.UPDATE_TEMPLATE,
									false,
									TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME, resourceInstanceName)));
				}
				aosType = templateInfo.getAosType();
				contentType = templateInfo.getContentType();
				tblStackSpecInfo.setTemplateId(req.getTemplate_id());
				tblStackSpecInfo.setTemplateName(tblStackTemplateBaseInfo.getName());
				tblStackSpecInfo.setTemplateVersion(templateInfo.getVersion());
				if (StringUtils.isEmpty(stackCompose))
				{
					stackCompose = templateInfo.getStackCompose();
				}

				if (StringUtils.isEmpty(justiceCompose))
				{
					justiceCompose = templateInfo.getJusticeCompose();
				}

				toSetDeployStrategy(tblStackSpecInfo, templateInfo);

			}
			if (! CollectionUtils.isEmpty(inputParams))
			{
				Set<CFG> cfgs = getCfgs(justiceCompose, inputParams);
				if (cfgs != null)
				{
					tblStackSpecInfo.setCfgs(JsonUtils.toJson(cfgs));
				}
				
				if (req.getUse_service_penetration())
				{
					ExposePortInfo exposePortInfo = assembleExposePortInfo(req);
					tblStackSpecInfo.setExposePorts(JsonUtils.toJson(exposePortInfo));
				}

				stackCompose = AosTemplateUtil.format(tblStackSpecInfo.getSpecId(), stackCompose, inputParams);
			}

			// add default labels
			stackCompose = addDefaultLabels(tblStackSpecInfo, stackCompose);

			tblStackSpecInfo.setAosType(aosType);
			tblStackSpecInfo.setContentType(contentType);
			tblStackSpecInfo.setStackCompose(stackCompose);
			tblStackSpecInfo.setJusticeCompose(justiceCompose);

			tblStackSpecInfo.setDescription(req.getDescription());
			tblStackSpecInfo.setTemplateId(req.getTemplate_id());
			// todo set status
			if (req.getTarget_nodes() != null && ! req.getTarget_nodes().isEmpty())
			{
				LOGGER.info("target nodes: {}", req.getTarget_nodes());
				tblStackSpecInfo.setTargetNodes(JsonUtils.toJson(req.getTarget_nodes()));
			}
			tblStackSpecInfo.setReplicaNum(req.getReplica_num());
			tblStackSpecInfo.setDevNeedInfo(JsonUtils.toJson(req.getDev_need_info()));
			tblStackSpecInfo.setAutoRun(req.getAuto_run());
			tblStackSpecInfo.setRegistryId(req.getRegistry_id());

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
			schedulingStrategy.setOnOneNode(2);
			schedulingStrategy.setTargetNodes(req.getTarget_nodes());


			tblStackSpecInfo.setSchedulingStrategy(JsonUtils.toJson(schedulingStrategy));
			tblStackSpecInfo.setExtraParams(JsonUtils.toJson(req. getExtra_params()));
			List<String> stackComposeImages = getStackComposeImages(stackCompose);
			tblStackSpecInfo.setImageNames(JsonUtils.toJson(stackComposeImages));

			if (req.getStack_type().equals(AppType.DAEMONSET))
			{
				tblStackSpecInfo.setStatus(ASSIGNED);
			}
			else if (req.getStack_type().equals(AppType.DEPLOYMENT))
			{
				if (tblStackSpecInfo.getTargetNodes() != null && ! tblStackSpecInfo.getTargetNodes().isEmpty() && tblStackSpecInfo.getReplicaNum() > 0)
				{
					tblStackSpecInfo.setStatus(StackState.SPAWNED);
				}
				else
				{
					tblStackSpecInfo.setStatus(StackState.MAKED);
				}
			}
			
			tblStackSpecInfo.setAlwaysOnline(req.getAlways_online());
			tblStackSpecInfo.setFailoverPolicy(req.getFailover_policy());

			stackRepo.insertStackSpecInfo(tblStackSpecInfo);
			//记录资源创建事件
			publishStackSpecCreateEvent(tblStackSpecInfo, "addStack");

			return tblStackSpecInfo.getSpecId();
		}
		catch (DuplicateKeyException e)
		{
			throw new WebSystemException(ErrorCode.STACK_DUP, ErrorLevel.INFO);
		}
	}

	private void publishStackSpecCreateEvent(TblStackSpecInfo tblStackSpecInfo, String action)
	{
		stackSpecResourceSupervisor.publishCreateEvent(tblStackSpecInfo,
				TemplateEngineUtils.render0(
						StackSpecActionDescriptionTemplate.Descriptions.ADD_SPEC,
						false,
						TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
								tblStackSpecInfo.getSpecName()),
						TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_ID,
								tblStackSpecInfo.getSpecId())),null,action);
	}

	ExposePortInfo assembleExposePortInfo(AddStackReq req)
	{
		Map<String, String> inputParams = req.getInput_params();
		List<ExposePort> ports = new ArrayList<>();
		String exposePrefix="";
		Map<Integer, ExposePort> exposePortMap = new HashMap<>();
		Map<Integer, String> portCertMap= new HashMap<>();
		for(Map.Entry<String, String> entry : inputParams.entrySet())
		{
			int port = 0;
			if (entry.getKey().startsWith(AosConst.EXPOSE_PORT_PREFIX))
			{
				exposePrefix = AosConst.EXPOSE_PORT_PREFIX;
				port = Integer.parseInt(entry.getValue());
			}
			else if (entry.getKey().startsWith(AosConst.EXPOSE_ADDR_PREFIX))
			{
				String addr = entry.getValue();
				String []addrs = addr.split(":");
				if (addrs.length != 2)
				{
					continue;
				}
				exposePrefix = AosConst.EXPOSE_ADDR_PREFIX;
				port = Integer.parseInt(addrs[1]);
			}
			else if (entry.getKey().startsWith(AosConst.EXPOSE_CERT_PREFIX))
			{
				String cert = entry.getValue();
				String port_ = cert.replace(AosConst.EXPOSE_CERT_PREFIX, "");
				Integer tmpPort = Integer.parseInt(port_);
				portCertMap.put(tmpPort, entry.getValue());
			}
			
			if (port != 0)
			{
				ExposePort exposePort = new ExposePort();
				exposePort.setEdgePort(port);
				String exposeInfo = entry.getKey().replace(exposePrefix, "");
				String [] exposeArray = exposeInfo.split("_");
				String protocol = exposeArray[0];
				final List<String> protocolSupports = Arrays.asList("tcp", "http", "https", "udp");
				if (StringUtils.isNotEmpty(protocol))
				{
					if (protocolSupports.contains(protocol.toLowerCase()))
					{
						exposePort.setProtocol(protocol);
					}
				}
				else
				{
					// default http
					exposePort.setProtocol("http");
				}

				if (exposeArray.length > 1)
				{
					StringBuilder typeBuilder = new StringBuilder();
					for (int i = 1; i < exposeArray.length; i++)
					{
						typeBuilder.append(exposeArray[i]);
						if (i < exposeArray.length - 1)
						{
							typeBuilder.append("_");
						}
					}
					String type =typeBuilder.toString();
					if (StringUtils.isNotBlank(type))
					{
						exposePort.setType(type);
					}
				}
				
				ports.add(exposePort);
				exposePortMap.put(port, exposePort);
			}
		}
		
		if (! CollectionUtils.isEmpty(ports))
		{
			if (!CollectionUtils.isEmpty(portCertMap))
			{
				for (ExposePort exposePort : ports)
				{
					exposePort.setCert(portCertMap.get(exposePort.getEdgePort()));
				}
			}
			
			ExposePortInfo exposePortInfo = new ExposePortInfo();
			exposePortInfo.setExposePortMap(exposePortMap);
			if (! CollectionUtils.isEmpty(req.getExtra_params()))
			{
				String purpose = req.getExtra_params().get("penetration_purpose");
				exposePortInfo.setPurpose(purpose);
			}
			return exposePortInfo;
		}
		return null;
	}

	public void updateStack(String stackId, UpdateStackReq req)
	{
		return;
	}

	/**
	 * remove stack
	 * @param stackId
	 * @param userId
	 * @throws WebSystemException
	 */
	public void deleteStack(String stackId, String userId)
	{
		TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
		//记录原始状态
		TblStackInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblStackInfo);
		if (tblStackInfo == null)
		{
			throw new WebSystemException(ErrorCode.STACK_NOT_EXIST, ErrorLevel.INFO);
		}

		if (userId != null)
		{
			if (! tblStackInfo.getUserId().equals(userId) && ! tblStackInfo.getCreateUserId().equals(userId))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
		}

		String name = tblStackInfo.getStackId() + tblStackInfo.getName();
		if (name.length() > 63)
		{
			name = name.substring(0, 63);
		}

		//if stack have not been scheduled, drop it directly
		if (tblStackInfo.getStatus() < StackState.SPAWNED)
		{
			stackRepo.deleteStack(tblStackInfo.getStackId());
			//记录资源删除事件
			stackInfoResourceSupervisor.publishDeleteEvent(tblStackInfo, null, null,
					"deleteStack",
					TemplateEngineUtils.render0(StackInfoActionDescriptionTemplate.Descriptions.DELETE_STACK,
							false,
							TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
									tblStackInfo.getName())));
			stackRepo.deleteCfgdataStackInfo(tblStackInfo.getStackId());
			return;
		}

		if (cloudRemoveStatusSet.contains(tblStackInfo.getStatus()))
		{
			//stack already dropd
			throw new WebSystemException(ErrorCode.STACK_DROPPED, ErrorLevel.INFO);
		}
		else if (edgeRemoveStatusSet.contains(tblStackInfo.getStatus())
				||tblStackInfo.getStatus() == StackState.EDGE_UNREACHABLE
				|| tblStackInfo.getStatus() == StackState.NO_MATCH_NODE
				|| tblStackInfo.getStatus() == StackState.CLOUD_CRETAE_FAILED_PARAMS
				|| tblStackInfo.getStatus() == StackState.CLOUD_CRETAE_FAILED_OVERDUE
				|| tblStackInfo.getStatus() == StackState.CREATE_FAILED
				|| tblStackInfo.getStatus() == StackState.NO_IMAGE)
		{
			//1 stack already remove in edge
			//2 stack have not create in edge or create filed
			tblStackInfo.setName(name);
			tblStackInfo.setStatus(StackState.FIN_CLOUD_REMOVE);
			tblStackInfo.setUpdateTime(new Date());
			stackRepo.updateStack(tblStackInfo);
			//记录资源更新事件
			publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "deleteStack");

			//relesae resources(remove gpu bind and update edge monopoly status: remove)
			releseBindResources(tblStackInfo);
			return;
		}

		//flag stack have been dropped
		tblStackInfo.setName(name);
		tblStackInfo.setStatus(StackState.SPAWNED_CLOUD_REMOVE);
		tblStackInfo.setUpdateTime(new Date());
		stackRepo.updateStack(tblStackInfo);
		//记录资源更新事件
		publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "deleteStack");

		MessagePack messagePack = new MessagePack();
		messagePack.setMsgType(AosMsgType.DELETE_STACK);
		messagePack.setMessageObj(tblStackInfo);
		stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
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
			LOGGER.error("发布StackInfo状态更新事件时报错! stackTrace:{}, errorMessage:{}",
					ExceptionUtils.getStackTrace(e), e.getMessage());
		}
	}


	/**
	 * assemble StackInfo by record in db
	 * @param tblStackInfo
	 * @return
	 */
	private StackInfo assembleStackInfo(TblStackInfo tblStackInfo)
	{

		String specId = tblStackInfo.getSpecId();
		TblStackSpecInfo tblStackSpecInfo = getStackSpecInfoBySpecId(specId);

		StackInfo stackInfo = new StackInfo();
		stackInfo.setId(tblStackInfo.getStackId());
		stackInfo.setName(tblStackInfo.getName());
		stackInfo.setDescription(tblStackSpecInfo.getDescription());
		StackTemplateBrief templateBrief = new StackTemplateBrief();
		templateBrief.setId(tblStackSpecInfo.getTemplateId());
		templateBrief.setName(tblStackSpecInfo.getTemplateName());
		templateBrief.setVersion(tblStackSpecInfo.getTemplateVersion());
		stackInfo.setTemplate(templateBrief);
		stackInfo.setBp_id(tblStackInfo.getBpId());
		stackInfo.setUser_id(tblStackInfo.getUserId());
		stackInfo.setCreate_user_id(tblStackInfo.getCreateUserId());

		setStackStatus(tblStackInfo, stackInfo);
		List<TargetNode> targetNodeList = JsonUtils.fromJson(tblStackSpecInfo.getTargetNodes(), new com.google.gson.reflect.TypeToken<List<TargetNode>>(){}.getType());
		stackInfo.setTarget_nodes(CollectionUtils.isEmpty(targetNodeList) ? Collections.emptyList() : targetNodeList);
		DstNode dstNode = JsonUtils.fromJson((String)tblStackInfo.getDstNode(), new com.google.gson.reflect.TypeToken<DstNode>(){}.getType());
		stackInfo.setDst_node(assembleDstNode(dstNode));
		stackInfo.setService_ids(stackRepo.getServiceIds(tblStackInfo.getStackId()));
		List<String> labels = JsonUtils.fromJson(tblStackInfo.getLabels(), new com.google.gson.reflect.TypeToken<List<String>>(){}.getType());
		stackInfo.setLabels(labels);
		stackInfo.setReplica_num(tblStackSpecInfo.getReplicaNum());
		stackInfo.setDev_need_info(JsonUtils.fromJson(tblStackInfo.getDevNeedInfo(), DevNeedInfo.class));
		stackInfo.setCreate_time(Utils.formatDate(tblStackInfo.getCreateTime()));
		stackInfo.setUpdate_time(Utils.formatDate(tblStackInfo.getUpdateTime()));
		if (CollectionUtils.hasContent((String)tblStackInfo.getExposePorts()))
		{
			List<ExposePort> portMaps = new ArrayList<>();
			
			ExposePortInfo exposePortInfo = JsonUtils.fromJson((String)tblStackInfo.getExposePorts(), ExposePortInfo.class);
			Map<Integer, ExposePort> exposePortMap = exposePortInfo.getExposePortMap();
			if (!CollectionUtils.isEmpty(exposePortMap))
			{
				exposePortMap.forEach((port, exposePort) -> {
					if (CollectionUtils.hasContent(exposePort.getPortId()))
					{
						portMaps.add(exposePort);
					}
				});
				stackInfo.setPort_maps(portMaps);
			}

		}

		generateStackTipMessage(stackInfo, tblStackInfo.getEventType(), tblStackInfo.getReportTime());
		assembleUserInfo(stackInfo);
		return stackInfo;
	}

	private void assembleUserInfo(StackInfo stackInfo)
	{
		String bpId = stackInfo.getBp_id();
		String bpName = "";
		if (StringUtils.isNotBlank(bpId))
		{
			try
			{
				bpName = combRpcService.getUmsService().getBpNameByBpId(bpId);
			}
			catch (Exception e)
			{
				LOGGER.error("get bp name failed");
			}
		}
		stackInfo.setBp_name(bpName);

		String userId = stackInfo.getUser_id();
		String userName = "";
		if (StringUtils.isNotBlank(userId))
		{
			try
			{
				userName = combRpcService.getUmsService().getUserNameByUserId(userId);
			}
			catch (Exception e)
			{
				LOGGER.error("get user name failed");
			}
		}
		stackInfo.setUser_name(userName);
	}

	/**
	 * get stack info by stackId
	 * @param stackId
	 * @param operUserId
	 * @return
	 * @throws WebSystemException
	 */
	public StackInfo getStack(String stackId, String operUserId, boolean finRemove)
	{
		TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
		if (tblStackInfo == null)
		{
			throw new WebSystemException(ErrorCode.STACK_NOT_EXIST, ErrorLevel.INFO);
		}

		if (finRemove)
		{
			if (finRemoveStatusSet.contains(tblStackInfo.getStatus()))
			{
				throw new WebSystemException(STACK_DROPPED, ErrorLevel.INFO);
			}
		}
		else
		{
			if (cloudRemoveStatusSet.contains(tblStackInfo.getStatus()))
			{
				throw new WebSystemException(STACK_DROPPED, ErrorLevel.INFO);
			}
		}


		if (operUserId != null)
		{
			if (! tblStackInfo.getUserId().equals(operUserId) && ! tblStackInfo.getCreateUserId().equals(operUserId))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
		}

		return assembleStackInfo(tblStackInfo);
	}

	/**
	 * get stack compose file by stackId
	 * @param stackId
	 * @param operUserId
	 * @return
	 * @throws IOException
	 */
	public ByteArrayOutputStream getStackArchives(String stackId, String operUserId) throws IOException
	{
		StackComposeInfo stackComposeInfo = getStackCompose(stackId, operUserId);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		try
		{
			if (! StringUtils.isEmpty(stackComposeInfo.getStack_compose()))
			{
				Utils.zipFile(stackComposeInfo.getStack_compose(), zos, "docker-compose.yml");
			}

			if (! StringUtils.isEmpty(stackComposeInfo.getJustice_compse()))
			{
				Utils.zipFile(stackComposeInfo.getJustice_compse(), zos, "justice-compose.yml");
			}
		}
		finally
		{
			zos.flush();
			baos.flush();
			zos.close();
			baos.close();
		}
		return baos;
	}

	/**
	 * get stack compose info
	 * @param stackId
	 * @param operUserId
	 * @return
	 * @throws WebSystemException
	 */
	public StackComposeInfo getStackCompose(String stackId, String operUserId)
	{
		TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
		if (tblStackInfo == null)
		{
			throw new WebSystemException(ErrorCode.STACK_NOT_EXIST, ErrorLevel.INFO);
		}

		if (cloudRemoveStatusSet.contains(tblStackInfo.getStatus()))
		{
			throw new WebSystemException(ErrorCode.STACK_DROPPED, ErrorLevel.INFO);
		}

		if (! StringUtils.isEmpty(operUserId))
		{
			if (! tblStackInfo.getUserId().equals(operUserId) && ! tblStackInfo.getCreateUserId().equals(operUserId))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
		}

		TblStackSpecInfo tblStackSpecInfo = getStackSpecInfoBySpecId(tblStackInfo.getSpecId());
		StackComposeInfo stackComposeInfo = new StackComposeInfo();
		stackComposeInfo.setJustice_compse(tblStackSpecInfo.getJusticeCompose());
		stackComposeInfo.setStack_compose(tblStackSpecInfo.getStackCompose());

		return stackComposeInfo;
	}

	/**
	 * get stack list by condition
	 * @param critical
	 * @return
	 */
	public GetStackListRsp getStackList(StackSearchCritical critical)
	{
		TblStackInfoExample example = new TblStackInfoExample();

		GetStackListRsp getStackListRsp = new GetStackListRsp();

		List<Integer> statusCollection = Objects.nonNull(critical.getStatus()) ?  Lists.newArrayList(SimpleStackStatusEnum.getFullStatus(critical.getStatus())) : new ArrayList<>();
		int count = stackRepo.countAll(critical.getName(),critical.getBpId(), critical.getUserId(), statusCollection,
				critical.getRegionId(), critical.getSiteId(), critical.getNodeId(), cloudRemoveStatusList, critical.getStackType());
		getStackListRsp.setTotal_num(count);
		if (count < 1)
		{
			getStackListRsp.setStacks(Collections.EMPTY_LIST);
			return getStackListRsp;
		}

		example.setStartRow((critical.getPageNum() - 1) * critical.getPageSize());
		example.setPageSize(critical.getPageSize());

		List<StackInfo> stacks = new ArrayList<>();

		List<TblStackInfo> stackInfoList = stackRepo.selectAll(null, critical.getName(), critical.getBpId(), critical.getUserId(), statusCollection,
				critical.getRegionId(), critical.getSiteId(), critical.getNodeId(), example.getPageSize(), example.getStartRow(),
				cloudRemoveStatusList, critical.getStackType());
		for (TblStackInfo tblStackInfo : stackInfoList)
		{
			StackInfo stackInfo = assembleStackInfo(tblStackInfo);
			stacks.add(stackInfo);
		}

		getStackListRsp.setStacks(stacks);

		return getStackListRsp;
	}

	/**
	 * action stack
	 * @param stackId
	 * @param action
	 * @param operUserId
	 * @throws WebSystemException
	 */
	public void actionStack(String stackId, String action, String operUserId)
	{
		TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
		if (tblStackInfo == null)
		{
			throw new WebSystemException(ErrorCode.STACK_NOT_EXIST, ErrorLevel.INFO);
		}

		if (cloudRemoveStatusSet.contains(tblStackInfo.getStatus()))
		{
			throw new WebSystemException(ErrorCode.STACK_DROPPED, ErrorLevel.INFO);
		}

		if (operUserId != null)
		{
			if (! tblStackInfo.getUserId().equals(operUserId) && ! tblStackInfo.getCreateUserId().equals(operUserId))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
		}

		String oper = "";
		switch (action)
		{
			case ActionType.START:
				oper = AosMsgType.START_STACK;
				break;
			case ActionType.STOP:
				oper = AosMsgType.STOP_STACK;
				break;
			case ActionType.RESTART:
				oper = AosMsgType.RESTART_STACK;
				break;
			default:
			    throw new WebSystemException(ErrorCode.ACTION_NOT_SUPPORT, ErrorLevel.INFO);
		}

		MessagePack messagePack = new MessagePack();
		messagePack.setMsgType(oper);
		messagePack.setMessageObj(tblStackInfo);
		stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
	}

	public Map<String,String> addService(AddServiceReq req)
	{
		return null;
	}


	/**
	 *assemble ServiceInfo by record in db
	 * @param tblStackServiceInfo
	 * @return
	 */
	public ServiceInfo assembleService(TblStackServiceInfo tblStackServiceInfo)
	{
		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setName(tblStackServiceInfo.getName());
		serviceInfo.setId(tblStackServiceInfo.getServiceId());
		serviceInfo.setDescription(tblStackServiceInfo.getDescription());
		if (tblStackServiceInfo.getAutoRun() != null) serviceInfo.setAuto_run(tblStackServiceInfo.getAutoRun());
		serviceInfo.setStack_id(tblStackServiceInfo.getStackId());
		serviceInfo.setStack_name(tblStackServiceInfo.getStackName());
		serviceInfo.setImage_name(tblStackServiceInfo.getImageName());
		setServiceStatus(tblStackServiceInfo, serviceInfo);
		DstNode dstNode = JsonUtils.fromJson(tblStackServiceInfo.getDstNode(), new com.google.gson.reflect.TypeToken<DstNode>(){}.getType());
		serviceInfo.setDst_node(dstNode);
		serviceInfo.setCreate_time(Utils.formatDate(tblStackServiceInfo.getCreateTime()));
		serviceInfo.setUpdate_time(Utils.formatDate(tblStackServiceInfo.getUpdateTime()));
		serviceInfo.setInst_ids(stackRepo.getInstIds(tblStackServiceInfo.getServiceId()));
		int instNum = (serviceInfo.getInst_ids() == null) ? 0: serviceInfo.getInst_ids().size();
		serviceInfo.setInst_num(instNum);

		return serviceInfo;
	}

	/**
	 * get service by serviceId
	 * @param serviceId
	 * @param operUserId
	 * @return
	 * @throws WebSystemException
	 */
	public ServiceInfo getService(String serviceId, String operUserId)
	{
		TblStackServiceInfo tblStackServiceInfo = stackRepo.getService(serviceId);
		if (tblStackServiceInfo == null)
		{
			throw  new WebSystemException(ErrorCode.STACK_SERVICE_NOT_EXIST, ErrorLevel.INFO);
		}

		if (cloudRemoveStatusSet.contains(tblStackServiceInfo.getStatus()))
		{
			throw  new WebSystemException(ErrorCode.STACK_SERVICE_DROPPED, ErrorLevel.INFO);
		}

		if (! StringUtils.isEmpty(operUserId) && ! tblStackServiceInfo.getUserId().equals(operUserId))
		{
			throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
		}

		return assembleService(tblStackServiceInfo);
	}

	public GetServiceListRsp getServiceList(ServiceSearchCritical critical)
	{
		TblStackServiceInfoExample example = new TblStackServiceInfoExample();

		List<Integer> statusCollection = Objects.nonNull(critical.getStatus()) ?  Lists.newArrayList(SimpleStackStatusEnum.getFullStatus(critical.getStatus())) : new ArrayList<>();
		long count = stackRepo.countAllService(critical.getStackName(), critical.getServiceName(), critical.getUserId(), statusCollection,
				critical.getRegionId(), critical.getSiteId(), critical.getNodeId(), cloudRemoveStatusList);

		GetServiceListRsp getServiceListRsp = new GetServiceListRsp();
		getServiceListRsp.setTotal_num(count);
		if (count < 1)
		{
			return getServiceListRsp;
		}

		//to do: set page size and page num
		example.setStartRow((critical.getPageNum()-1)*critical.getPageSize());
		example.setPageSize(critical.getPageSize());
		List<TblStackServiceInfo> stackServiceInfoList = stackRepo.selectAllService(critical.getStackName(), critical.getServiceName(), critical.getUserId(), statusCollection,
				critical.getRegionId(), critical.getSiteId(), critical.getNodeId(), example.getPageSize(), example.getStartRow(), cloudRemoveStatusList);
		if (stackServiceInfoList == null || stackServiceInfoList.isEmpty())
		{
			return getServiceListRsp;
		}

		List<ServiceInfo> serviceInfos = new ArrayList<>();
		for (TblStackServiceInfo tblStackServiceInfo : stackServiceInfoList)
		{
			ServiceInfo serviceInfo =  assembleService(tblStackServiceInfo);
			serviceInfos.add(serviceInfo);
		}

		getServiceListRsp.setServices(serviceInfos);

		return getServiceListRsp;
	}

	/**
	 * delete service
	 * @param serviceId
	 * @param operUserId
	 * @throws WebSystemException
	 */
	public void deleteService(String serviceId, String operUserId)
	{
		TblStackServiceInfo tblStackServiceInfo = stackRepo.getService(serviceId);

		if (tblStackServiceInfo == null)
		{
			throw  new WebSystemException(ErrorCode.STACK_SERVICE_NOT_EXIST, ErrorLevel.INFO);
		}

		if (cloudRemoveStatusSet.contains(tblStackServiceInfo.getStatus()))
		{
			throw  new WebSystemException(ErrorCode.STACK_SERVICE_DROPPED, ErrorLevel.INFO);
		}

		if (! StringUtils.isEmpty(operUserId) && ! tblStackServiceInfo.getUserId().equals(operUserId))
		{
			throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
		}

		//...send to processor for delete service: containers delete inst and send msg to edge
		MessagePack messagePack = new MessagePack();
		messagePack.setMsgType(AosMsgType.DELETE_SERVICE);
		messagePack.setMessageObj(tblStackServiceInfo);
		stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);

		return;
	}

	/**
	 * action service by serviceId
	 * @param serviceId
	 * @param action
	 * @param operUserId
	 * @throws WebSystemException
	 */
	public void actionService(String serviceId, String action, String operUserId)
	{
		TblStackServiceInfo tblStackServiceInfo = stackRepo.getService(serviceId);
		if (tblStackServiceInfo == null)
		{
			throw new WebSystemException(ErrorCode.STACK_NOT_EXIST, ErrorLevel.INFO);
		}

		if (operUserId != null)
		{
			if (! tblStackServiceInfo.getUserId().equals(operUserId))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
		}

		String oper = "";
		switch (action)
		{
			case ActionType.START:
				oper = AosMsgType.START_SERVICE;
				break;
			case ActionType.STOP:
				oper = AosMsgType.STOP_SERVICE;
				break;
			case ActionType.RESTART:
				oper = AosMsgType.RESTART_SERVICE;
				break;
			default:
				throw new WebSystemException(ErrorCode.ACTION_NOT_SUPPORT, ErrorLevel.INFO);
		}

		MessagePack messagePack = new MessagePack();
		messagePack.setMsgType(oper);
		messagePack.setMessageObj(tblStackServiceInfo);
		stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
	}

	/**
	 * assemble stack inst by record in db
	 * @param tblStackInstInfo
	 * @return
	 */
	private ContainerInstInfo assembleStackInst(TblStackInstInfo tblStackInstInfo)
	{
		ContainerInstInfo serviceInstanceInfo = new ContainerInstInfo();

		serviceInstanceInfo.setContainer_id(tblStackInstInfo.getRefId());
		serviceInstanceInfo.setInst_id(tblStackInstInfo.getInstId());
		serviceInstanceInfo.setImage_name(tblStackInstInfo.getImageName());
		setStackInstStatus(serviceInstanceInfo, tblStackInstInfo);
		serviceInstanceInfo.setName(tblStackInstInfo.getInstName());
		String nodeId = tblStackInstInfo.getNodeId();
		serviceInstanceInfo.setNode_id(nodeId);
		if (StringUtils.isNotBlank(nodeId))
		{
			serviceInstanceInfo.setNode_name(combRpcService.getRegionResourceService().getNodeNameById(nodeId));
		}
		String extenInfo = tblStackInstInfo.getExtenInfo();
		if (! StringUtils.isEmpty(extenInfo))
		{
			JsonObject jsonObject = JsonParser.parseString(extenInfo).getAsJsonObject();
			if (jsonObject.get("command") != null)
			{
				String command = jsonObject.get("command").getAsString();
				serviceInstanceInfo.setCommand(command);
			}
			if (jsonObject.get("ports") != null)
			{
				String ports   = jsonObject.get("ports").getAsString();
				serviceInstanceInfo.setPorts(ports);
			}
		}

		return serviceInstanceInfo;
	}

	/**
	 * get stack inst list by serviceId
	 *
	 * @param serviceId
	 * @param userId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public GetContainerListRsp getStackInst(String serviceId, String userId, Integer pageNum, Integer pageSize)
	{
		TblStackInstInfoExample example = new TblStackInstInfoExample();
		TblStackInstInfoExample.Criteria criteria = example.createCriteria();

		criteria.andServiceIdEqualTo(serviceId);
		if (! StringUtils.isEmpty(userId))
		{
			criteria.andUserIdEqualTo(userId);
		}

		criteria.andStatusNotIn(cloudRemoveStatusList);
		PageHelper.startPage(pageNum, pageSize);
		List<TblStackInstInfo> stackInstInfoList = stackRepo.getInstList(example);
		PageInfo<TblStackInstInfo> pageInfo = new PageInfo<>(stackInstInfoList);
		if (stackInstInfoList == null || stackInstInfoList.isEmpty())
		{
			return GetContainerListRsp.builder().containers(Collections.EMPTY_LIST).total_num(0).build();
		}

		List<ContainerInstInfo> serviceInstanceInfos = new ArrayList<>();
		for (TblStackInstInfo tblStackInstInfo : stackInstInfoList)
		{
			ContainerInstInfo serviceInstanceInfo = assembleStackInst(tblStackInstInfo);
			serviceInstanceInfos.add(serviceInstanceInfo);
		}

		return GetContainerListRsp.builder().containers(serviceInstanceInfos).total_num(pageInfo.getTotal()).build();
	}

	/**
	 * generate docker compose info by record in db
	 * @param tblStackInfo
	 */
	private void genDockerComposeStackSerice(TblStackInfo tblStackInfo, String dockerCompose)
	{
		DockerComposeYaml composeInfo = yaml.loadAs(dockerCompose, DockerComposeYaml.class);
		Map<String, Object> services = composeInfo.getServices();
		for (Map.Entry<String, Object> entry : services.entrySet())
		{
			String serviceName = entry.getKey();
			Map<String,Object> serviceMap = (Map<String,Object> ) entry.getValue();
			String image = (String) serviceMap.get("image");
			TblStackServiceInfo tblStackServiceInfo = new TblStackServiceInfo();
			tblStackServiceInfo.setStatus(tblStackInfo.getStatus());
			tblStackServiceInfo.setName(serviceName);
			tblStackServiceInfo.setStackId(tblStackInfo.getStackId());
			tblStackServiceInfo.setStackName(tblStackInfo.getName());
			tblStackServiceInfo.setAutoRun(tblStackInfo.getAutoRun());
			tblStackServiceInfo.setCreateTime(new Date());
			tblStackServiceInfo.setUpdateTime(tblStackServiceInfo.getCreateTime());
			tblStackServiceInfo.setDescription(tblStackServiceInfo.getDescription());
			tblStackServiceInfo.setServiceId(Utils.assignUUId());
			tblStackServiceInfo.setUserId(tblStackInfo.getUserId());
			tblStackServiceInfo.setAutoRun(tblStackInfo.getAutoRun());
			tblStackServiceInfo.setImageName(image);
			try
			{
				stackRepo.insertService(tblStackServiceInfo);
			}
			catch (DuplicateKeyException e)
			{
				continue;
			}
		}
	}

	/**
	 * generate stack info by record in db
	 * @param tblStackSpecInfo
	 */
	public List<TblStackInfo> genStack(TblStackSpecInfo tblStackSpecInfo, boolean setASSIGNED)
	{
		TblStackInfoExample example = new TblStackInfoExample();
		TblStackInfoExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(tblStackSpecInfo.getSpecId());
		criteria.andStatusEqualTo(StackState.WAIT_ASSIGN);
		List<TblStackInfo> stackList = stackRepo.getStack(example);
		if (stackList.size() > 0)
		{
			return stackList;
		}

		List<TblStackInfo> instList = new ArrayList<>();
		for (int i = 0; i < tblStackSpecInfo.getReplicaNum(); i++)
		{
			TblStackInfo tblStackInfo = new TblStackInfo();
			tblStackInfo.setSpecId(tblStackSpecInfo.getSpecId());
			tblStackInfo.setStackId(Utils.buildStr(AosPrefix.DEPLOYMENT, Utils.assignUUId()));
			tblStackInfo.setName(Utils.buildStr(tblStackSpecInfo.getSpecName(), "_", tblStackSpecInfo.getSpecId().substring(tblStackSpecInfo.getSpecId().length() - 8), "_", String.valueOf(i)));
			tblStackInfo.setUserId(tblStackSpecInfo.getUserId());
			tblStackInfo.setBpId(tblStackSpecInfo.getBpId());
			tblStackInfo.setDevNeedInfo(tblStackSpecInfo.getDevNeedInfo());
			tblStackInfo.setAutoRun(tblStackSpecInfo.getAutoRun());
			tblStackInfo.setAlwaysOnline(tblStackSpecInfo.getAlwaysOnline());

			tblStackInfo.setCreateTime(new Date());
			tblStackInfo.setCreateUserId(tblStackSpecInfo.getCreateUserId());
			if (null != tblStackSpecInfo.getLabels())
			{
				tblStackInfo.setLabels(tblStackSpecInfo.getLabels());
			}

//			tblStackInfo.setUpdateTime(null);
			if (setASSIGNED)
			{
				tblStackInfo.setStatus(ASSIGNED);
				String targetNodes = tblStackSpecInfo.getTargetNodes();
				List<TargetNode> dstNodeList = JsonUtils.fromJson(targetNodes,new com.google.gson.reflect.TypeToken<List<TargetNode>>(){}.getType());
				TargetNode targetNode = dstNodeList.get(0);
				DstNode dstNode = new DstNode();
				dstNode.setDstNodeId(targetNode.getDstNodeId());
				dstNode.setDstSiteId(targetNode.getDstSiteId());
				dstNode.setDstRegionId(targetNode.getDstRegionId());
				tblStackInfo.setDstNode(JsonUtils.toJson(dstNode));
			}
			else
			{
				tblStackInfo.setStatus(tblStackSpecInfo.getStatus());
			}

			tblStackInfo.setSendCreateNum(0);
			tblStackInfo.setStartNum(0);
			tblStackInfo.setFailNum(0);

			stackRepo.insertStack(tblStackInfo);
			//记录资源创建事件
			publishStackInfoCreateEvent(tblStackInfo, "genStack");

			if (tblStackSpecInfo.getAosType().equals("docker-compose"))
			{
				genDockerComposeStackSerice(tblStackInfo, tblStackSpecInfo.getStackCompose());
			}

			instList.add(tblStackInfo);
		}
		return instList;
	}

	private void publishStackInfoCreateEvent(TblStackInfo tblStackInfo, String action)
	{
		try
		{
			stackInfoResourceSupervisor.publishCreateEvent(tblStackInfo,
					TemplateEngineUtils.render0(StackInfoActionDescriptionTemplate.Descriptions.CREATE_STACK,
							false,
							TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME, tblStackInfo.getName())),
					tblStackInfo.getName(),
					action);
		} catch (Exception e)
		{
			LOGGER.error("发布应用信息创建事件失败! stackTrace:{}, errorMessage:{}",
					ExceptionUtils.getStackTrace(e), e.getMessage());
		}
	}

	/**
	 * assign satck to dst node
	 * @param specId
	 */
	public void processScheduleStack(String specId)
	{
		TblStackSpecInfo tblStackSpecInfo = getStackSpecInfoBySpecId(specId);
		//记录原始状态
		TblStackSpecInfo beforeUpdateStackSpecEntity = DeepCopyUtils.deepCopy(tblStackSpecInfo);
		List<TblStackInfo> waitAssignStackInfoList = Collections.emptyList();
		List<String> waitAssignIdList = Collections.emptyList();

		if (tblStackSpecInfo.getStatus() != StackState.SPAWNED)
		{
			return;
		}

		boolean submitScheduleTaskSuccess = false;

		try
		{
			AssignEdge2StackReq assignEdge2StackReq = new AssignEdge2StackReq();
			assignEdge2StackReq.setName(tblStackSpecInfo.getSpecName());
			assignEdge2StackReq.setAos_type(tblStackSpecInfo.getAosType());
			assignEdge2StackReq.setBp_id(tblStackSpecInfo.getBpId());
			assignEdge2StackReq.setUser_id(tblStackSpecInfo.getUserId());
			assignEdge2StackReq.setStack_compose(tblStackSpecInfo.getStackCompose());
			assignEdge2StackReq.setJustice_compose(tblStackSpecInfo.getJusticeCompose());
			assignEdge2StackReq.setReplica_num(tblStackSpecInfo.getReplicaNum());
			assignEdge2StackReq.setDev_need_info(JsonUtils.fromJson(tblStackSpecInfo.getDevNeedInfo(), DevNeedInfo.class));
			assignEdge2StackReq.setTarget_nodes(JsonUtils.fromJson(tblStackSpecInfo.getTargetNodes(), new com.google.gson.reflect.TypeToken<List<TargetNode>>(){}.getType()));

			SchedulingStrategy schedulingStrategy = JsonUtils.fromJson(tblStackSpecInfo.getSchedulingStrategy(), SchedulingStrategy.class);
			assignEdge2StackReq.setScheduling_strategy(schedulingStrategy);

			//generate stack
			waitAssignStackInfoList = genStack(tblStackSpecInfo, false);
			waitAssignIdList = waitAssignStackInfoList.stream()
					.map(x->x.getStackId()).collect(Collectors.toList());

			assignEdge2StackReq.setWaitAssignIdList(waitAssignIdList);
			assignEdge2StackReq.setSpecId(specId);

			// save ahead the status to db
			tblStackSpecInfo.setStatus(StackState.WAIT_ASSIGN);
			tblStackSpecInfo.setUpdateTime(new Date());
			stackRepo.updateSpec(tblStackSpecInfo);

			//记录资源更新事件
			//－－－－－－－－发布应用部署状态更新事件
			publishStackSpecStatusUpdateEvent(tblStackSpecInfo, beforeUpdateStackSpecEntity,
					"processScheduleStack", StackStateDesProvider.INSTANCE, StackStateDesProvider.STATUS_FIELD);
			//－－－－－－－－发布应用状态更新事件
			if (!CollectionUtils.isEmpty(waitAssignStackInfoList))
			{
				stackRepo.updateStacksStatus(waitAssignIdList, tblStackSpecInfo.getStatus());
				for (TblStackInfo beforeUpdateStackInfoEntity : waitAssignStackInfoList)
				{
					TblStackInfo tblStackInfo = DeepCopyUtils.deepCopy(beforeUpdateStackInfoEntity);
					tblStackInfo.setStatus(tblStackSpecInfo.getStatus());
					//记录资源更新事件
					publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateStackInfoEntity, "processScheduleStack");
				}
			}

			//alloc resources, send task to scheduler
			Integer allocStatus = combRpcService.getSchedulerService().allocEdge2StackResources(assignEdge2StackReq);
			if (null != allocStatus && allocStatus == SchedulerState.WAITING_SCHEDULING.getCode())
			{
				submitScheduleTaskSuccess = true;
				// submit scheduling task success, should do nothing
				// pass
			} else
			{
				submitScheduleTaskSuccess = false;
				tblStackSpecInfo.setStatus(StackState.CLOUD_SYSTEM_ABNORMAL);
			}
		}
		catch (JsonSyntaxException e)
		{
			submitScheduleTaskSuccess = false;
			LOGGER.info("{} params error", specId);
			tblStackSpecInfo.setStatus(StackState.CLOUD_CRETAE_FAILED_PARAMS);
			e.printStackTrace();
		}
		catch (IllegalStateException e)
		{
			submitScheduleTaskSuccess = false;
			LOGGER.error("stack of {} has been generated but microservice error, waiting for resend ", specId);
			tblStackSpecInfo.setStatus(StackState.WAIT_ASSIGN);
			e.printStackTrace();
		} catch (Exception e)
		{
			submitScheduleTaskSuccess = false;
			tblStackSpecInfo.setStatus(StackState.CLOUD_SYSTEM_ABNORMAL);
			e.printStackTrace();
			LOGGER.error("StackServiceFacede.processScheduleStack error {}", e.getMessage());
		}
		finally
		{
			if (submitScheduleTaskSuccess == false)
			{
				Integer errorStackStatus = tblStackSpecInfo.getStatus();
				tblStackSpecInfo = getStackSpecInfoBySpecId(specId);
				if (Objects.equals(tblStackSpecInfo.getStatus(), StackState.ASSIGNED))
				{
					// updated successfully by scheduler service
					// pass
				} else
				{
					// may something error, update status back to an abnormal value
					tblStackSpecInfo.setUpdateTime(new Date());
					tblStackSpecInfo.setStatus(errorStackStatus);
					//记录资源更新事件
					//－－－－－－－－发布应用部署状态更新事件
					stackRepo.updateSpec(tblStackSpecInfo);
                    publishStackSpecStatusUpdateEvent(tblStackSpecInfo, beforeUpdateStackSpecEntity, "processScheduleStack",
                            StackStateDesProvider.INSTANCE, StackStateDesProvider.STATUS_FIELD);
					//－－－－－－－－发布应用状态更新事件
					if (!CollectionUtils.isEmpty(waitAssignStackInfoList))
					{
						stackRepo.updateStacksStatus(waitAssignIdList, tblStackSpecInfo.getStatus());
						for (TblStackInfo beforeUpdateStackInfoEntity : waitAssignStackInfoList)
						{
							TblStackInfo tblStackInfo = DeepCopyUtils.deepCopy(beforeUpdateStackInfoEntity);
							tblStackInfo.setStatus(tblStackSpecInfo.getStatus());
							//记录资源更新事件
							publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateStackInfoEntity, "processScheduleStack");
						}
					}
				}
			}
		}
	}

	private void publishStackSpecStatusUpdateEvent(TblStackSpecInfo tblStackSpecInfo, TblStackSpecInfo beforeUpdateStackSpecEntity, String action, IBizModelStateDesProvider modelStateDesProvider, String stateDesField)
	{
		try
		{
			Map<Integer, BizModelStateInfo> stackStateDict = modelStateDesProvider.getStateDesDict().get(stateDesField);
			BizModelStateInfo stateInfo = stackStateDict.get(tblStackSpecInfo.getStatus());
			stackSpecResourceSupervisor.publishUpdateEvent("应用部署状态更新", null,
					false, beforeUpdateStackSpecEntity, tblStackSpecInfo,
					action,
					TemplateEngineUtils.render0(StackSpecActionDescriptionTemplate.Descriptions.UPDATE_SPEC_AND_STACKS_STATUS,
							false,
							TemplateEngineUtils.newEntry("status",
									Optional.ofNullable(stateInfo)
											.map(x->x.getCnDescription())
											.orElse(Optional.ofNullable(tblStackSpecInfo.getStatus())
													.map(x->x.toString())
													.orElse(""))),
							TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME, tblStackSpecInfo.getSpecName()),
							TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_ID, tblStackSpecInfo.getSpecId())));
		} catch (Exception e)
		{
			LOGGER.error("发布应用部署状态更新事件失败! stackTrace:{}, errorMessage:{}",
					ExceptionUtils.getStackTrace(e), e.getMessage());
		}
	}

	/**
	 * retry assign satck to dst node
	 * @param stackId
	 */
	public void processReScheduleStack(String stackId)
	{
		TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
		//记录原始状态
		TblStackInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblStackInfo);
		if (tblStackInfo == null)
		{
			return;
		}

		if (tblStackInfo.getStatus() != StackState.WAIT_ASSIGN)
		{
			return;
		}

		String specId = tblStackInfo.getSpecId();
		TblStackSpecInfo tblStackSpecInfo = stackRepo.selectStackSpecInfoByPrimaryKey(specId);
		if (tblStackSpecInfo == null)
		{
			// can not find spec
			tblStackInfo.setStatus(StackState.CLOUD_CRETAE_FAILED_PARAMS);
			tblStackInfo.setUpdateTime(new Date());
			stackRepo.updateStack(tblStackInfo);
			//记录状态更新事件
			publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "processReScheduleStack");
		}
		else
		{
			try
			{
				AssignEdge2StackReq assignEdge2StackReq = new AssignEdge2StackReq();
				assignEdge2StackReq.setName(tblStackSpecInfo.getSpecName());
				assignEdge2StackReq.setAos_type(tblStackSpecInfo.getAosType());
				assignEdge2StackReq.setBp_id(tblStackSpecInfo.getBpId());
				assignEdge2StackReq.setUser_id(tblStackSpecInfo.getUserId());
				assignEdge2StackReq.setStack_compose(tblStackSpecInfo.getStackCompose());
				assignEdge2StackReq.setJustice_compose(tblStackSpecInfo.getJusticeCompose());
				assignEdge2StackReq.setReplica_num(tblStackSpecInfo.getReplicaNum());
				assignEdge2StackReq.setDev_need_info(JsonUtils.fromJson(tblStackSpecInfo.getDevNeedInfo(), DevNeedInfo.class));
				assignEdge2StackReq.setTarget_nodes(JsonUtils.fromJson(tblStackSpecInfo.getTargetNodes(), new com.google.gson.reflect.TypeToken<List<TargetNode>>(){}.getType()));

				SchedulingStrategy schedulingStrategy = JsonUtils.fromJson(tblStackSpecInfo.getSchedulingStrategy(), SchedulingStrategy.class);
				assignEdge2StackReq.setScheduling_strategy(schedulingStrategy);

				List<String> waitAssignIdList = Lists.newArrayList(stackId);
				assignEdge2StackReq.setWaitAssignIdList(waitAssignIdList);
				assignEdge2StackReq.setSpecId(stackId);

				//alloc resources, send task to scheduler
				Integer allocStatus = combRpcService.getSchedulerService().allocEdge2StackResources(assignEdge2StackReq);
				if (null != allocStatus && allocStatus == SchedulerState.WAITING_SCHEDULING.getCode())
				{
					tblStackInfo.setStatus(StackState.WAIT_ASSIGN);
				}
				else
				{
					tblStackInfo.setStatus(StackState.CLOUD_SYSTEM_ABNORMAL);
				}
			}
			catch (JsonSyntaxException e)
			{
				LOGGER.info("{} params error", stackId);
				tblStackInfo.setStatus(StackState.CLOUD_CRETAE_FAILED_PARAMS);
				e.printStackTrace();
			}
			catch (IllegalStateException e)
			{
				LOGGER.error("microservice error, waiting for resend ");
				tblStackInfo.setStatus(StackState.WAIT_ASSIGN);
				e.printStackTrace();
			}
			finally
			{
				tblStackInfo.setUpdateTime(new Date());
				stackRepo.updateStack(tblStackInfo);
				//记录资源更新事件
				publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "processReScheduleStack");
			}
		}
	}

	/**
	 * satck failover
	 * @param failoverPair
	 */
	public void processStackFailover(Pair<String, String> failoverPair)
	{
		String oldStackId = failoverPair.getLeft();
		String failoverRange = failoverPair.getRight();
		TblStackInfo oldTblStackInfo = stackRepo.getStack(oldStackId);
		if (oldTblStackInfo == null || StringUtils.isEmpty(oldTblStackInfo.getSpecId()))
		{
			return;
		}

		TblStackSpecInfo tblStackSpecInfo = stackRepo.selectStackSpecInfoByPrimaryKey(oldTblStackInfo.getSpecId());
		if (tblStackSpecInfo == null)
		{
			// can not find spec
			return;
		}
		else
		{
			TblStackInfo tblStackInfo = new TblStackInfo();
			BeanUtils.copyProperties(oldTblStackInfo, tblStackInfo);
			tblStackInfo.setStackId(Utils.buildStr(AosPrefix.DEPLOYMENT,Utils.assignUUId()));
			tblStackInfo.setName(tblStackInfo.getName() + 'F');
			tblStackInfo.setCreateTime(new Date());
			tblStackInfo.setUpdateTime(tblStackInfo.getCreateTime());
			tblStackInfo.setReportTime(tblStackInfo.getCreateTime());
			tblStackInfo.setDstNode(null);
			tblStackInfo.setEventType(0);
			tblStackInfo.setStatus(StackState.SPAWNED);
			tblStackInfo.setAlwaysOnline(tblStackSpecInfo.getAlwaysOnline());

			try
			{
				AssignEdge2StackReq assignEdge2StackReq = new AssignEdge2StackReq();
				assignEdge2StackReq.setName(tblStackSpecInfo.getSpecName());
				assignEdge2StackReq.setAos_type(tblStackSpecInfo.getAosType());
				assignEdge2StackReq.setBp_id(tblStackSpecInfo.getBpId());
				assignEdge2StackReq.setUser_id(tblStackSpecInfo.getUserId());
				assignEdge2StackReq.setStack_compose(tblStackSpecInfo.getStackCompose());
				assignEdge2StackReq.setJustice_compose(tblStackSpecInfo.getJusticeCompose());
				assignEdge2StackReq.setReplica_num(1);
				assignEdge2StackReq.setDev_need_info(JsonUtils.fromJson(tblStackSpecInfo.getDevNeedInfo(), DevNeedInfo.class));

				SchedulingStrategy schedulingStrategy = JsonUtils.fromJson(tblStackSpecInfo.getSchedulingStrategy(), SchedulingStrategy.class);

				DstNode dstNode = JsonUtils.fromJson((String)oldTblStackInfo.getDstNode(), DstNode.class);
				if (dstNode == null)
				{
					return;
				}
				TargetNode targetNode = new TargetNode();

				switch (failoverRange)
				{
					case "region":
						targetNode.setDstRegionId(dstNode.getDstRegionId());
						break;
					case "site":
						targetNode.setDstRegionId(dstNode.getDstRegionId());
						targetNode.setDstSiteId(dstNode.getDstSiteId());
						break;
					case "default":
						schedulingStrategy.addRegionPerferLabelSelector(targetNode.getDstRegionId());
						schedulingStrategy.addSitePerferLabelSelector(targetNode.getDstSiteId());
						targetNode.setDstRegionId(dstNode.getDstRegionId());
						targetNode.setDstSiteId(null);
						targetNode.setDstNodeId(null);
					default:
						break;
				}

				tblStackInfo.setDstNode(JsonUtils.toJson(targetNode));

				schedulingStrategy.setTargetNodes(Arrays.asList(targetNode));
				assignEdge2StackReq.setScheduling_strategy(schedulingStrategy);

				assignEdge2StackReq.setTarget_nodes(schedulingStrategy.getTargetNodes());

				List<String> waitAssignIdList = Lists.newArrayList(tblStackInfo.getStackId());
				assignEdge2StackReq.setWaitAssignIdList(waitAssignIdList);
				assignEdge2StackReq.setSpecId(tblStackInfo.getSpecId());

				//alloc resources, send task to scheduler
				Integer allocStatus = combRpcService.getSchedulerService().allocEdge2StackResources(assignEdge2StackReq);
				if (null != allocStatus && allocStatus == SchedulerState.WAITING_SCHEDULING.getCode())
				{
					tblStackInfo.setStatus(StackState.WAIT_ASSIGN);
				}
				else
				{
					tblStackInfo.setStatus(StackState.CLOUD_SYSTEM_ABNORMAL);
				}
			}
			catch (JsonSyntaxException e)
			{
				LOGGER.info("{} params error", tblStackInfo.getStackId());
				tblStackInfo.setStatus(StackState.CLOUD_CRETAE_FAILED_PARAMS);
				e.printStackTrace();
			}
			catch (IllegalStateException e)
			{
				LOGGER.error("microservice error, waiting for resend ");
				tblStackInfo.setStatus(StackState.WAIT_ASSIGN);
				e.printStackTrace();
			}
			finally
			{
				stackRepo.insertStack(tblStackInfo);
				//记录资源创建事件
				publishStackInfoCreateEvent(tblStackInfo, "processStackFailover");
			}
		}
	}

	public void processSyncCfg(String stackId)
	{
		TblStackInfo tblStackInfo = stackRepo.getStack(stackId);

		if (tblStackInfo == null)
		{
			return;
		}

		if (tblStackInfo.getStatus() != ASSIGNED)
		{
			return;
		}

		//记录原始状态
		TblStackInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblStackInfo);
		String specId = tblStackInfo.getSpecId();
		TblStackSpecInfo tblStackSpecInfo = getStackSpecInfoBySpecId(specId);

		try
		{
			if (StringUtils.isEmpty(tblStackSpecInfo.getCfgs()))
			{
				tblStackInfo.setStatus(CFG_SYNCED);
			}
			else
			{
				List<CFG> cfgs = JsonUtils.fromJson(tblStackSpecInfo.getCfgs(), new com.google.gson.reflect.TypeToken<List<CFG>>(){}.getType());
				if (CollectionUtils.isEmpty(cfgs))
				{
					tblStackInfo.setStatus(CFG_SYNCED);
				}
				else
				{
					tblStackInfo.setStatus(SYNC_CFG);
					tblStackInfo.setUpdateTime(new Date());
					stackRepo.updateStack(tblStackInfo);
					//记录资源更新事件
					publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "processSyncCfg");

					DstNode dstNode = JsonUtils.fromJson((String)tblStackInfo.getDstNode(), DstNode.class);

					for (CFG cfg : cfgs)
					{
						SysService.ConfigInfoBase configInfoBase = combRpcService.getSysService().getConfig(cfg.getUserId(), cfg.getGroup(), cfg.getDataId());
						TblCfgdataStackInfo tblCfgdataStackInfo = new TblCfgdataStackInfo(Utils.assignUUId(), configInfoBase.getDataId(),
								configInfoBase.getGroup(), configInfoBase.getMd5(), configInfoBase.getUserId(), dstNode.getDstNodeId(),
								stackId, CfgStatus.SYNCING, new Date(), new Date());
						stackRepo.insertCfgdataStackInfo(tblCfgdataStackInfo);

						sendFileCreateReq(configInfoBase, stackId, dstNode.getDstNodeId(), tblCfgdataStackInfo.getCfgVolumeId());
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.info("{} params error", stackId);
			tblStackInfo.setStatus(StackState.CLOUD_CRETAE_FAILED_PARAMS);
			e.printStackTrace();

			//relesae resources(remove gpu bind and update edge monopoly status: remove)
			releseBindResources(tblStackInfo);
		}
		finally
		{
			tblStackInfo.setUpdateTime(new Date());
			stackRepo.updateStack(tblStackInfo);
			//记录资源更新事件
			publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "processSyncCfg");
		}
	}

	/**
	 * assign satck to dst node
	 * @param stackId
	 */
	public void processAssignStack(String stackId)
	{
		TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
		//记录原始状态
		TblStackInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblStackInfo);
		if (tblStackInfo == null)
		{
			return;
		}

		if (tblStackInfo.getStatus() != CFG_SYNCED)
		{
			return;
		}
		String specId = tblStackInfo.getSpecId();
		TblStackSpecInfo tblStackSpecInfo = getStackSpecInfoBySpecId(specId);

		try
		{
			EeStackDef.msg_create_stack_req_body.Builder stackReqBody = EeStackDef.msg_create_stack_req_body.newBuilder();
			stackReqBody.setAutoRun(tblStackInfo.getAutoRun());
			if (tblStackInfo.getBpId() != null) stackReqBody.setBpId(tblStackInfo.getBpId());
			stackReqBody.setUserId(tblStackInfo.getUserId());
			stackReqBody.setStackCompose(tblStackSpecInfo.getStackCompose());

			if (StringUtils.isNotEmpty(tblStackSpecInfo.getCfgs()))
			{
				List<CFG> cfgs = JsonUtils.fromJson(tblStackSpecInfo.getCfgs(), new com.google.gson.reflect.TypeToken<List<CFG>>(){}.getType());
				if (!CollectionUtils.isEmpty(cfgs))
				{
					Map<String, String> inputParams = new HashMap<>();
					inputParams.put("justice_stack_id", stackId);
					stackReqBody.setStackCompose(AosTemplateUtil.format(tblStackSpecInfo.getSpecId(), stackReqBody.getStackCompose(), inputParams));
				}
			}

			if (! StringUtils.isEmpty(tblStackSpecInfo.getJusticeCompose()))
			{
				stackReqBody.setJusticeCompose(tblStackSpecInfo.getJusticeCompose());
			}

			stackReqBody.setType(tblStackSpecInfo.getAosType());

			stackReqBody.setStackName(tblStackInfo.getName());
			stackReqBody.setStackId(tblStackInfo.getStackId());

			tblStackSpecInfo.assembleImagePullPolicy();
			stackReqBody.setExtraParams((String) tblStackSpecInfo.getExtraParams());
			EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.STACK_OPERATOR);
			EeStackDef.msg_stack_operator_body.Builder stackOperator = EeStackDef.msg_stack_operator_body.newBuilder();
			stackOperator.setOperatorType(StackOperatorType.CRETATE_STACK_REQ);

			//request ims for registry
			if (null != tblStackSpecInfo.getRegistryId() && !tblStackSpecInfo.getRegistryId().isEmpty())
			{
				ImsRegistryService.Registry registry = combRpcService.getImsRegistryService().getRegistry(tblStackSpecInfo.getRegistryId(), tblStackInfo.getBpId(), tblStackInfo.getUserId());
				if (null == registry)
				{
					tblStackInfo.setStatus(StackState.CLOUD_CRETAE_FAILED_PARAMS);

					//relesae resources(remove gpu bind and update edge monopoly status: remove)
					releseBindResources(tblStackInfo);
					return;
				}
				EeCommonDef.registry_info.Builder registryInfo = EeCommonDef.registry_info.newBuilder();
				registryInfo.setServer(registry.getRegistryUrl());
				registryInfo.setUsername(registry.getRegistryUserName());
				registryInfo.setPassword(registry.getRegistryPassword());
				stackReqBody.setRegistry(registryInfo);
			}
			else
			{
				EeCommonDef.registry_info.Builder registryInfo = EeCommonDef.registry_info.newBuilder();
				stackReqBody.setRegistry(registryInfo);
			}

			stackOperator.setCreateStackReqBody(stackReqBody);

			DstNode dstNode = JsonUtils.fromJson((String)tblStackInfo.getDstNode(), DstNode.class);
			EeNetMessageApi.ee_net_message.Builder netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setStackOperatorBody(stackOperator);

			byte [] message = netMessgae.build().toByteArray();
			netMessageServiceFacade.submitMessage(dstNode.getDstNodeId(), message);

			tblStackInfo.setStatus(StackState.FWD);
		}
		catch (Exception e)
		{
			LOGGER.info("{} params error", stackId);
			tblStackInfo.setStatus(StackState.CLOUD_CRETAE_FAILED_PARAMS);
			e.printStackTrace();

			//relesae resources(remove gpu bind and update edge monopoly status: remove)
			releseBindResources(tblStackInfo);
		}
		finally
		{

			tblStackInfo.setUpdateTime(new Date());
			stackRepo.updateStack(tblStackInfo);
			//记录资源更新事件
			publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "processAssignStack");
		}
	}

	/**
	 * resend stack to dst node
	 * @param stackId
	 */
	public void processResendStack(String stackId)
	{
		LOGGER.info("process resend");
		TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
		//记录原始状态
		TblStackInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblStackInfo);
		if (tblStackInfo == null)
		{
			LOGGER.info("stack is null. for {}", stackId);
			return;
		}

		if (tblStackInfo.getStatus() != StackState.FWD)
		{
			LOGGER.info("stack state have been changed for {}", stackId);
			return;
		}
		String specId = tblStackInfo.getSpecId();
		TblStackSpecInfo tblStackSpecInfo = getStackSpecInfoBySpecId(specId);

		//update failNum
		tblStackInfo.setFailNum(tblStackInfo.getFailNum()+1);

		EeStackDef.msg_create_stack_req_body.Builder stackReqBody = EeStackDef.msg_create_stack_req_body.newBuilder();
		stackReqBody.setAutoRun(tblStackInfo.getAutoRun());
		if (tblStackInfo.getBpId() != null) stackReqBody.setBpId(tblStackInfo.getBpId());
		stackReqBody.setUserId(tblStackInfo.getUserId());
		stackReqBody.setStackId(tblStackInfo.getStackId());
		stackReqBody.setStackCompose(tblStackSpecInfo.getStackCompose());

		if (StringUtils.isNotEmpty(tblStackSpecInfo.getCfgs()))
		{
			List<CFG> cfgs = JsonUtils.fromJson(tblStackSpecInfo.getCfgs(), new com.google.gson.reflect.TypeToken<List<CFG>>(){}.getType());
			if (!CollectionUtils.isEmpty(cfgs))
			{
				Map<String, String> inputParams = new HashMap<>();
				inputParams.put("justice_stack_id", stackId);
				stackReqBody.setStackCompose(AosTemplateUtil.format(tblStackSpecInfo.getSpecId(), stackReqBody.getStackCompose(), inputParams));
			}
		}

		if (! StringUtils.isEmpty(tblStackSpecInfo.getJusticeCompose()))
		{
			stackReqBody.setJusticeCompose(tblStackSpecInfo.getJusticeCompose());
		}

		stackReqBody.setType(tblStackSpecInfo.getAosType());
		tblStackSpecInfo.assembleImagePullPolicy();
		stackReqBody.setExtraParams((String)tblStackSpecInfo.getExtraParams());
		stackReqBody.setStackName(tblStackInfo.getName());

		EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.STACK_OPERATOR);
		EeStackDef.msg_stack_operator_body.Builder stackOperator = EeStackDef.msg_stack_operator_body.newBuilder();
		stackOperator.setOperatorType(StackOperatorType.CRETATE_STACK_REQ);

		//request ims for registry
		if (null != tblStackSpecInfo.getRegistryId() && !tblStackSpecInfo.getRegistryId().isEmpty())
		{
			ImsRegistryService.Registry registry = combRpcService.getImsRegistryService().getRegistry(tblStackSpecInfo.getRegistryId(), tblStackInfo.getBpId(), tblStackInfo.getUserId());
			if (null == registry)
			{
				tblStackInfo.setStatus(StackState.CLOUD_CRETAE_FAILED_PARAMS);

				//relesae resources(remove gpu bind and update edge monopoly status: remove)
				releseBindResources(tblStackInfo);
				return;
			}
			EeCommonDef.registry_info.Builder registryInfo = EeCommonDef.registry_info.newBuilder();
			registryInfo.setServer(registry.getRegistryUrl());
			registryInfo.setUsername(registry.getRegistryUserName());
			registryInfo.setPassword(registry.getRegistryPassword());
			stackReqBody.setRegistry(registryInfo);
		}
		else
		{
			EeCommonDef.registry_info.Builder registryInfo = EeCommonDef.registry_info.newBuilder();
			stackReqBody.setRegistry(registryInfo);
		}

		stackOperator.setCreateStackReqBody(stackReqBody);

		LOGGER.info("prepare send msg to dst node. dst node: {}", tblStackInfo.getDstNode());
		DstNode dstNode = JsonUtils.fromJson((String)tblStackInfo.getDstNode(), DstNode.class);
		EeNetMessageApi.ee_net_message.Builder netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setStackOperatorBody(stackOperator);
		byte [] message = netMessgae.build().toByteArray();
		LOGGER.info("prepare send msg to dst node");
		LOGGER.info("send msg to dst node");
		netMessageServiceFacade.submitMessage(dstNode.getDstNodeId(), message);

		tblStackInfo.setStatus(StackState.FWD);
		tblStackInfo.setUpdateTime(new Date());
		stackRepo.updateStack(tblStackInfo);
		//记录资源更新事件
		publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "processResendStack");
	}

	/**
	 * resend stack to dst node
	 * @param stackId
	 */
	public void processNoRsp(String stackId)
	{
		LOGGER.info("process resend");
		TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
		//记录原始状态
		TblStackInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblStackInfo);
		if (tblStackInfo == null)
		{
			LOGGER.info("stack is null. for {}", stackId);
			return;
		}

		if (tblStackInfo.getStatus() != StackState.FWD)
		{
			LOGGER.info("stack state have been changed for {}", stackId);
			return;
		}

		tblStackInfo.setStatus(StackState.EDGE_UNREACHABLE);
		tblStackInfo.setFailNum(tblStackInfo.getFailNum()+1);
		tblStackInfo.setUpdateTime(new Date());
		stackRepo.updateStack(tblStackInfo);
		//记录资源更新事件
		publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "processNoRsp");
	}

	/**
	 * process stack action for stack
	 * @param tblStackInfo
	 */
	public void processStartStack(TblStackInfo tblStackInfo)
	{
		EeStackDef.msg_life_mng_stack_req_body.Builder stackReqBody = EeStackDef.msg_life_mng_stack_req_body.newBuilder();
		stackReqBody.setAction(ActionType.START);
		stackReqBody.setStackId(tblStackInfo.getStackId());

		EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.STACK_OPERATOR);
		EeStackDef.msg_stack_operator_body.Builder stackOperator = EeStackDef.msg_stack_operator_body.newBuilder();
		stackOperator.setOperatorType(StackOperatorType.LIFE_MNG_STACK_REQ);
		stackOperator.setLifeMngStackReqBody(stackReqBody);

		EeNetMessageApi.ee_net_message.Builder netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setStackOperatorBody(stackOperator);
		if (tblStackInfo.getDstNode() == null || ((String)tblStackInfo.getDstNode()).isEmpty())
		{
			return;
		}

		DstNode dstNode = JsonUtils.fromJson((String)tblStackInfo.getDstNode(), new com.google.gson.reflect.TypeToken<DstNode>(){}.getType());

		byte [] messageByte = netMessgae.build().toByteArray();

		netMessageServiceFacade.submitMessage(dstNode.getDstNodeId(), messageByte);

		addLifeMngEvent(reqHeader.getSessionId(), stackReqBody.getAction(), tblStackInfo.getStackId(), reqHeader.getTimestamp());
	}

	/**
	 * process stop action for stack
	 * @param tblStackInfo
	 */
	public void processStopStack(TblStackInfo tblStackInfo)
	{
		//记录原始状态
		TblStackInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblStackInfo);
		LOGGER.info("process stop stack");
		EeStackDef.msg_life_mng_stack_req_body.Builder stackReqBody = EeStackDef.msg_life_mng_stack_req_body.newBuilder();
		stackReqBody.setAction(ActionType.STOP);
		stackReqBody.setStackId(tblStackInfo.getStackId());


		EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.STACK_OPERATOR);
		EeStackDef.msg_stack_operator_body.Builder stackOperator = EeStackDef.msg_stack_operator_body.newBuilder();
		stackOperator.setOperatorType(StackOperatorType.LIFE_MNG_STACK_REQ);
		stackOperator.setLifeMngStackReqBody(stackReqBody);

		if (tblStackInfo.getDstNode() == null || ((String)tblStackInfo.getDstNode()).isEmpty())
		{
			return;
		}

		EeNetMessageApi.ee_net_message.Builder netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setStackOperatorBody(stackOperator);
		DstNode dstNode = JsonUtils.fromJson((String)tblStackInfo.getDstNode(), new com.google.gson.reflect.TypeToken<DstNode>(){}.getType());
		byte [] messageByte = netMessgae.build().toByteArray();

		netMessageServiceFacade.submitMessage(dstNode.getDstNodeId(), messageByte);

		addLifeMngEvent(reqHeader.getSessionId(), stackReqBody.getAction(), tblStackInfo.getStackId(), reqHeader.getTimestamp());

		tblStackInfo.setStatus(StackState.SPAWN_USER_STOP_QUIT);
		tblStackInfo.setUpdateTime(new Date());
		stackRepo.updateStack(tblStackInfo);
		//记录资源更新事件
		publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "processStopStack");
	}

	/**
	 *
	 * @param tblStackInfo
	 */
	public void processRestartStack(TblStackInfo tblStackInfo)
	{
		LOGGER.info("process stop stack");
		EeStackDef.msg_life_mng_stack_req_body.Builder stackReqBody = EeStackDef.msg_life_mng_stack_req_body.newBuilder();
		stackReqBody.setAction(ActionType.RESTART);
		stackReqBody.setStackId(tblStackInfo.getStackId());

		EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.STACK_OPERATOR);
		EeStackDef.msg_stack_operator_body.Builder stackOperator = EeStackDef.msg_stack_operator_body.newBuilder();
		stackOperator.setOperatorType(StackOperatorType.LIFE_MNG_STACK_REQ);
		stackOperator.setLifeMngStackReqBody(stackReqBody);

		if (tblStackInfo.getDstNode() == null || ((String)tblStackInfo.getDstNode()).isEmpty())
		{
			return;
		}

		EeNetMessageApi.ee_net_message.Builder netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setStackOperatorBody(stackOperator);
		DstNode dstNode = JsonUtils.fromJson((String)tblStackInfo.getDstNode(), new com.google.gson.reflect.TypeToken<DstNode>(){}.getType());

		byte [] messageByte = netMessgae.build().toByteArray();
		netMessageServiceFacade.submitMessage(dstNode.getDstNodeId(), messageByte);

		addLifeMngEvent(reqHeader.getSessionId(), stackReqBody.getAction(), tblStackInfo.getStackId(), reqHeader.getTimestamp());
	}


	/**
	 * process remove action for stack
	 * @param tblStackInfo
	 */
	public void processRemoveStack(TblStackInfo tblStackInfo)
	{
		//记录原始状态
		TblStackInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblStackInfo);
		EeStackDef.msg_life_mng_stack_req_body.Builder stackReqBody = EeStackDef.msg_life_mng_stack_req_body.newBuilder();
		stackReqBody.setAction(ActionType.REMOVE);
		stackReqBody.setStackId(tblStackInfo.getStackId());


		EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.STACK_OPERATOR);
		EeStackDef.msg_stack_operator_body.Builder stackOperator = EeStackDef.msg_stack_operator_body.newBuilder();
		stackOperator.setOperatorType(StackOperatorType.LIFE_MNG_STACK_REQ);
		stackOperator.setLifeMngStackReqBody(stackReqBody);

		if (tblStackInfo.getDstNode() == null || ((String)tblStackInfo.getDstNode()).isEmpty())
		{
			processStackServiceStatusByStackStatus(tblStackInfo.getStackId(), StackState.SPAWNED_CLOUD_REMOVE);
			return;
		}

		EeNetMessageApi.ee_net_message.Builder netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setStackOperatorBody(stackOperator);
		DstNode dstNode = JsonUtils.fromJson((String)tblStackInfo.getDstNode(), new com.google.gson.reflect.TypeToken<DstNode>(){}.getType());
		byte [] messageByte = netMessgae.build().toByteArray();

		netMessageServiceFacade.submitMessage(dstNode.getDstNodeId(), messageByte);

			//relesae resources(remove gpu bind and update edge monopoly status: remove)
		combRpcService.getSchedulerService().releaseBindResources(dstNode.getDstNodeId(), tblStackInfo.getStackId());


		tblStackInfo.setStatus(StackState.SPAWNED_CLOUD_REMOVE);
		tblStackInfo.setUpdateTime(new Date());
		stackRepo.updateStack(tblStackInfo);
		//记录资源更新事件
		publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "processRemoveStack");

		processStackServiceStatusByStackStatus(tblStackInfo.getStackId(), StackState.SPAWNED_CLOUD_REMOVE);

		// release port and more
		releaseUserServicePenetration(tblStackInfo);
	}

	/**
	 * process start action for service
	 * @param tblStackServiceInfo
	 */
	public void processStartService(TblStackServiceInfo tblStackServiceInfo)
	{
		EeStackDef.msg_life_mng_stack_req_body.Builder stackReqBody = EeStackDef.msg_life_mng_stack_req_body.newBuilder();
		stackReqBody.setAction(ActionType.START);
		stackReqBody.setStackId(tblStackServiceInfo.getStackId());
		stackReqBody.setServiceName(tblStackServiceInfo.getName());


		EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.STACK_OPERATOR);
		EeStackDef.msg_stack_operator_body.Builder stackOperator = EeStackDef.msg_stack_operator_body.newBuilder();
		stackOperator.setOperatorType(StackOperatorType.LIFE_MNG_STACK_REQ);
		stackOperator.setLifeMngStackReqBody(stackReqBody);

		EeNetMessageApi.ee_net_message.Builder netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setStackOperatorBody(stackOperator);

		if (tblStackServiceInfo.getDstNode() == null || tblStackServiceInfo.getDstNode().isEmpty())
		{
			return;
		}

		DstNode dstNode = JsonUtils.fromJson(tblStackServiceInfo.getDstNode(), new com.google.gson.reflect.TypeToken<DstNode>(){}.getType());
		byte [] messageByte = netMessgae.build().toByteArray();
		netMessageServiceFacade.submitMessage(dstNode.getDstNodeId(), messageByte);

	}

	/**
	 * process stop action for service
	 * @param tblStackServiceInfo
	 */
	public void processStopService(TblStackServiceInfo tblStackServiceInfo)
	{
		LOGGER.info("process stop stack");
		EeStackDef.msg_life_mng_stack_req_body.Builder stackReqBody = EeStackDef.msg_life_mng_stack_req_body.newBuilder();
		stackReqBody.setAction(ActionType.STOP);
		stackReqBody.setStackId(tblStackServiceInfo.getStackId());
		stackReqBody.setServiceName(tblStackServiceInfo.getName());


		EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.STACK_OPERATOR);
		EeStackDef.msg_stack_operator_body.Builder stackOperator = EeStackDef.msg_stack_operator_body.newBuilder();
		stackOperator.setOperatorType(StackOperatorType.LIFE_MNG_STACK_REQ);
		stackOperator.setLifeMngStackReqBody(stackReqBody);

		EeNetMessageApi.ee_net_message.Builder netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setStackOperatorBody(stackOperator);
		DstNode dstNode = JsonUtils.fromJson(tblStackServiceInfo.getDstNode(), new com.google.gson.reflect.TypeToken<DstNode>(){}.getType());

		if (tblStackServiceInfo.getDstNode() == null || tblStackServiceInfo.getDstNode().isEmpty())
		{
			return;
		}

		byte [] messageByte = netMessgae.build().toByteArray();
		netMessageServiceFacade.submitMessage(dstNode.getDstNodeId(), messageByte);


		tblStackServiceInfo.setStatus(StackState.SPAWN_USER_STOP_QUIT);
		tblStackServiceInfo.setUpdateTime(new Date());
		stackRepo.updateService(tblStackServiceInfo);
	}

	/**
	 * process restart service
	 * @param tblStackServiceInfo
	 */
	public void processRestartService(TblStackServiceInfo tblStackServiceInfo)
	{
		LOGGER.info("process restart service");
		EeStackDef.msg_life_mng_stack_req_body.Builder stackReqBody = EeStackDef.msg_life_mng_stack_req_body.newBuilder();
		stackReqBody.setAction(ActionType.RESTART);
		stackReqBody.setStackId(tblStackServiceInfo.getStackId());
		stackReqBody.setServiceName(tblStackServiceInfo.getName());

		EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.STACK_OPERATOR);
		EeStackDef.msg_stack_operator_body.Builder stackOperator = EeStackDef.msg_stack_operator_body.newBuilder();
		stackOperator.setOperatorType(StackOperatorType.LIFE_MNG_STACK_REQ);
		stackOperator.setLifeMngStackReqBody(stackReqBody);

		EeNetMessageApi.ee_net_message.Builder netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setStackOperatorBody(stackOperator);
		DstNode dstNode = JsonUtils.fromJson(tblStackServiceInfo.getDstNode(), new com.google.gson.reflect.TypeToken<DstNode>(){}.getType());

		if (tblStackServiceInfo.getDstNode() == null || tblStackServiceInfo.getDstNode().isEmpty())
		{
			return;
		}

		byte [] messageByte = netMessgae.build().toByteArray();
		netMessageServiceFacade.submitMessage(dstNode.getDstNodeId(), messageByte);

	}

	/**
	 * process remove action for service
	 * @param tblStackServiceInfo
	 */
	public void processRemoveService(TblStackServiceInfo tblStackServiceInfo)
	{
		try
		{
			EeStackDef.msg_life_mng_stack_req_body.Builder stackReqBody = EeStackDef.msg_life_mng_stack_req_body.newBuilder();
			stackReqBody.setAction(ActionType.REMOVE);
			stackReqBody.setStackId(tblStackServiceInfo.getStackId());
			stackReqBody.setServiceName(tblStackServiceInfo.getName());

			EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.STACK_OPERATOR);
			EeStackDef.msg_stack_operator_body.Builder stackOperator = EeStackDef.msg_stack_operator_body.newBuilder();
			stackOperator.setOperatorType(StackOperatorType.LIFE_MNG_STACK_REQ);
			stackOperator.setLifeMngStackReqBody(stackReqBody);

			EeNetMessageApi.ee_net_message.Builder netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setStackOperatorBody(stackOperator);

			if (tblStackServiceInfo.getDstNode() == null || tblStackServiceInfo.getDstNode().isEmpty())
			{
				return;
			}

			DstNode dstNode = JsonUtils.fromJson(tblStackServiceInfo.getDstNode(), new com.google.gson.reflect.TypeToken<DstNode>(){}.getType());

			byte [] messageByte = netMessgae.build().toByteArray();
			netMessageServiceFacade.submitMessage(dstNode.getDstNodeId(), messageByte);

			processStackInstStatusByServiceStatus(tblStackServiceInfo.getServiceId(), StackState.SPAWNED_CLOUD_REMOVE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("process del service:{}", tblStackServiceInfo.getServiceId());
		}
		finally
		{
			tblStackServiceInfo.setStatus(StackState.SPAWNED_CLOUD_REMOVE);
			tblStackServiceInfo.setUpdateTime(new Date());
			stackRepo.updateService(tblStackServiceInfo);
		}

	}

	/**
	 * process status error for detail
	 * @param stackId
	 * @param stackStatus
	 */
	void processStackServiceStatusByStackStatus(String stackId, int stackStatus)
	{
		List<TblStackServiceInfo> tblStackServiceInfoList = stackRepo.getServiceListByStackId(stackId);
		if (tblStackServiceInfoList == null || tblStackServiceInfoList.isEmpty())
		{
			return;
		}

		for (TblStackServiceInfo tblStackServiceInfo : tblStackServiceInfoList)
		{
			processStackInstStatusByServiceStatus(tblStackServiceInfo.getServiceId(), stackStatus);

			tblStackServiceInfo.setStatus(stackStatus);
			tblStackServiceInfo.setUpdateTime(new Date());
			stackRepo.updateService(tblStackServiceInfo);
		}
	}

	void processStackInstStatusByServiceStatus(String serviceId, int status)
	{
		TblStackInstInfoExample example = new TblStackInstInfoExample();
		TblStackInstInfoExample.Criteria criteria = example.createCriteria();
		criteria.andServiceIdEqualTo(serviceId);
		criteria.andStatusNotIn(cloudRemoveStatusList);

		List<TblStackInstInfo> tblStackInstInfos = stackRepo.getInstList(example);
		if (tblStackInstInfos == null || tblStackInstInfos.isEmpty())
		{
			return;
		}

		for (TblStackInstInfo inst : tblStackInstInfos)
		{
			combRpcService.getContainerInstService().setContainerStatus(inst.getInstId(), status);
		}

		TblStackInstInfo tblStackInstInfo = new TblStackInstInfo();
		tblStackInstInfo.setStatus(status);
		tblStackInstInfo.setUpdateTime(new Date());
		stackRepo.updateInst(tblStackInstInfo, example);
	}


	/**
	 * process stack status by rpt info
	 * @param stackId
	 * @param statckStatus
	 * @param serviceDescList
	 * @param route
	 * @return
	 */
	private int processStackStatusInfo(String stackId, int statckStatus, List<EeStackDef.stack_service_desc> serviceDescList, EeCommonDef.msg_route route)
	{
		TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
		//记录原始状态
		TblStackInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblStackInfo);
		if (tblStackInfo == null)
		{
			return ErrorCode.STACK_NOT_EXIST.getCode();
		}

		Date date = new Date();

		if (tblStackInfo.getAlwaysOnline() != null && tblStackInfo.getAlwaysOnline())
		{
			TblStackSpecInfo tblStackSpecInfo = getStackSpecInfoBySpecId(tblStackInfo.getSpecId());
			FailoverPolicy failoverPolicy = JsonUtils.fromJson(tblStackSpecInfo.getFailoverPolicy(), FailoverPolicy.class);
			if (Objects.nonNull(failoverPolicy))
			{
				if (failoverPolicy.getNeedFailover() && statckStatus == StackState.RUNNING)
				{
					if ((tblStackInfo.getReportTime().getTime() + failoverPolicy.getDelays()) < new Date().getTime())
					{
						MessagePack messagePack = new MessagePack();
						messagePack.setMsgType(AosMsgType.DELETE_STACK);
						messagePack.setMessageObj(tblStackInfo);
						stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
						return ErrorCode.SUCCESS.getCode();
					}
					else
					{
						RedisUtil.zadd(RedisCache.AOS_KEEPALIVE_SPEC_STACKIDS + tblStackInfo.getSpecId(), stackId, date.getTime());
					}
				}
			}

		}
		
		if (statckStatus == StackState.RUNNING)
		{
			try
			{
				if (CollectionUtils.hasContent((String)tblStackInfo.getExposePorts()))
				{
					ExposePortInfo exposeInfo = JsonUtils.fromJson((String)tblStackInfo.getExposePorts(), ExposePortInfo.class);
					Map<Integer, ExposePort> exposePortMap = exposeInfo.getExposePortMap();
					RpcAddServicePortReq addServicePortReq = new RpcAddServicePortReq();
					addServicePortReq.setMicroServiceName("aos");
					addServicePortReq.setReqId(tblStackInfo.getStackId());
					addServicePortReq.setTags(JsonUtils.fromJson(tblStackInfo.getLabels(), new com.google.gson.reflect.TypeToken<List<String>>(){}.getType()));
					addServicePortReq.setDeployment(tblStackInfo.getSpecId());
					addServicePortReq.setPurpose(exposeInfo.getPurpose());
					addServicePortReq.setTargetType("compose");
					List<RpcAddServicePortReq.TargetService> targetServices = new ArrayList<>();
					for (Map.Entry<Integer, ExposePort> entry : exposePortMap.entrySet())
					{
						ExposePort exposePort = entry.getValue();
						if (! CollectionUtils.hasContent(exposePort.getPortId()))
						{
							RpcAddServicePortReq.TargetService targetService = new RpcAddServicePortReq.TargetService(exposePort.getEdgePort());
							targetService.setProtocol(exposePort.getProtocol());
							targetService.setService(stackId);
							targetService.setCert(exposePort.getCert());
							targetServices.add(targetService);
						}
					}

					if (! CollectionUtils.isEmpty(targetServices))
					{
						addServicePortReq.setTargetServices(targetServices);
						combRpcService.getServiceManagerService().addServicePort(addServicePortReq);
					}
				}
			}
			catch (Exception e)
			{
				LOGGER.error("process add service port failed:{}, error:{}", tblStackInfo.getStackId(), e);
			}
		}

		switch (statckStatus)
		{
			case StackState.USER_STOP_QUIT:
				//User stop quit, tranform state.
				if (tblStackInfo.getStatus() == StackState.SPAWN_SYSTEM_STOP)
				{
					statckStatus = StackState.FIN_SYSTEM_STOP;
				}
				else if (tblStackInfo.getStatus() == StackState.SPAWN_USER_STOP_QUIT)
				{
					statckStatus = StackState.FIN_USER_STOP_QUIT;
				}
				else if (tblStackInfo.getStatus() == StackState.SPAWN_OVERDUE_QUIT)
				{
					statckStatus = StackState.FIN_OVERDUE_QUIT;
				}
				break;
			case StackState.SUCCESS_QUIT:
				//success quit.
			case StackState.ABNORMAL_QUIT:
				//abnormal quit.
			case StackState.SYSTEM_STOP:
				//User stop quit.
				if (tblStackInfo.getAlwaysOnline() != null && tblStackInfo.getAlwaysOnline())
				{
					MessagePack messagePack = new MessagePack();
					messagePack.setMsgType(AosMsgType.START_STACK);
					messagePack.setMessageObj(tblStackInfo);
					stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
				}
				break;
			case StackState.HARDWARE_ERROR:
				//create fail due to hardware error, release resources
			case StackState.NO_IMAGE:
				//create fail due to no image, release resources
			case StackState.IMAGE_DOWNLOAD_FAILED:
				//create fail due to image download failed, release resources
			case StackState.CREATE_FAILED:
				//create fail, release resources

				//relesae resources(remove gpu bind and update edge monopoly status: remove)
				combRpcService.getSchedulerService().releaseBindResources(route.getONodeId(), tblStackInfo.getStackId());
				break;
			case StackState.REMOVED:
				//remove, update last running info and remove time.
			case StackState.OBJECT_AUTO_REMOVE:
				//object auto remove, update last running info and remove time.
				statckStatus = StackState.FIN_CLOUD_REMOVE;

				stackRepo.deleteCfgdataStackInfo(stackId);

				//relesae resources(remove gpu bind and update edge monopoly status: remove)
				combRpcService.getSchedulerService().releaseBindResources(route.getONodeId(), tblStackInfo.getStackId());
				break;
			case StackState.OBJECT_NOT_EXIST:
				//object not exist, update last running info and remove time.
				break;
			case StackState.KEEP_ON:
				//keep on, do nothing.
				return ErrorCode.SUCCESS.getCode();
			default:
				break;
		}

		tblStackInfo.setStatus(statckStatus);
		tblStackInfo.setUpdateTime(date);
		tblStackInfo.setReportTime(date);
		try
		{
			if (serviceDescList == null || serviceDescList.isEmpty())
			{
				processStackServiceStatusByStackStatus(stackId, statckStatus);
				return ErrorCode.SUCCESS.getCode();
			}

			for (EeStackDef.stack_service_desc stackServiceDesc : serviceDescList)
			{
				TblStackServiceInfo tblStackServiceInfo = stackRepo.getService(stackServiceDesc.getServiceName(), stackId);
				if (tblStackServiceInfo == null)
				{
					continue;
				}

				tblStackServiceInfo.setStatus(stackServiceDesc.getServiceStatus());
				tblStackServiceInfo.setImageName(stackServiceDesc.getImageName());
				tblStackServiceInfo.setImageId(stackServiceDesc.getImageId());
				tblStackServiceInfo.setUpdateTime(date);
				stackRepo.updateService(tblStackServiceInfo);
				List<EeStackDef.stack_inst_desc> stackInstList = stackServiceDesc.getStackInstList();
				if (stackInstList == null || stackInstList.isEmpty())
				{
					continue;
				}
				for (EeStackDef.stack_inst_desc stackInst : stackInstList)
				{
					TblStackInstInfo tblStackInstInfo = stackRepo.getInst(stackInst.getInstId());
					if (tblStackInstInfo == null)
					{
						tblStackInstInfo = new TblStackInstInfo();
						tblStackInstInfo.setInstName(stackInst.getInstName());
						tblStackInstInfo.setCreateTime(date);
						tblStackInstInfo.setUpdateTime(date);
						tblStackInstInfo.setInstId(stackInst.getInstId());
						tblStackInstInfo.setServiceId(tblStackServiceInfo.getServiceId());
						tblStackInstInfo.setRefId(stackInst.getRefId());
						tblStackInstInfo.setImageName(stackInst.getImageName());
						tblStackInstInfo.setExtenInfo(stackInst.getExtenInfo());
						tblStackInstInfo.setImageId("");
						tblStackInstInfo.setStatus(stackInst.getStatus());
						tblStackInstInfo.setNodeId(route.getONodeId());
						stackRepo.insertInst(tblStackInstInfo);
					}
					else
					{
						tblStackInstInfo.setStatus(stackInst.getStatus());
						tblStackInstInfo.setUpdateTime(date);
						stackRepo.updateInst(tblStackInstInfo);
					}

					DstNode finalDstNode = JsonUtils.fromJson((String)tblStackInfo.getDstNode(), new com.google.gson.reflect.TypeToken<DstNode>(){}.getType());
					DstNode dstNode = null;

					if (finalDstNode.getDstNodeId().equals(route.getONodeId()))
					{
						dstNode = finalDstNode;
					}


					if (dstNode == null)
					{
						dstNode = new DstNode();
						dstNode.setDstNodeId(route.getONodeId());
						dstNode.setDstRegionId(route.getORegionId());
					}

					String extenInfo = tblStackInstInfo.getExtenInfo();
					String cmd = "";
					if (! StringUtils.isEmpty(extenInfo))
					{
						JsonObject jsonObject = JsonParser.parseString(extenInfo).getAsJsonObject();
						if (jsonObject.get("command") != null)
						{
							cmd = jsonObject.get("command").getAsString();
						}
					}

					combRpcService.getContainerInstService().setContainer(stackInst.getInstId(), stackInst.getInstName(),stackInst.getImageName(),cmd,
							stackInst.getRefId(), stackInst.getStatus(), tblStackInfo.getUserId(), tblStackInfo.getBpId(),
							dstNode);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			stackRepo.updateStack(tblStackInfo);
			//记录资源更新事件
			publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "processStackStatusInfo");
		}

		return ErrorCode.SUCCESS.getCode();
	}

	/**
	 * process stack rpt
	 * @param rptStackReqBody
	 * @param route
	 * @return
	 */
	public int processStackRpt(EeStackDef.msg_rpt_stack_req_body rptStackReqBody, EeCommonDef.msg_route route)
	{
		int status = processStackStatusInfo(rptStackReqBody.getStackId(), rptStackReqBody.getStackStatus(), rptStackReqBody.getServicesInfoList(), route);
		EeStackDef.msg_rpt_stack_rsp_body.Builder rptStackRsp = EeStackDef.msg_rpt_stack_rsp_body.newBuilder();
		rptStackRsp.setErrorCode(status);
		EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.STACK_OPERATOR);
		EeStackDef.msg_stack_operator_body.Builder stackOperator = EeStackDef.msg_stack_operator_body.newBuilder();
		stackOperator.setOperatorType(StackOperatorType.RPT_STACK_RSP);
		stackOperator.setRptStackRspBody(rptStackRsp);

		EeNetMessageApi.ee_net_message netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setStackOperatorBody(stackOperator).build();
		netMessageServiceFacade.submitMessage(route.getONodeId(), netMessgae.toByteArray());

		return ErrorCode.SUCCESS.getCode();
	}

	/**
	 * process life mng rsp
	 * @param lifeMngStackRspBody
	 * @param route
	 * @return
	 */
	public int processLifeMngRsp(EeStackDef.msg_life_mng_stack_rsp_body lifeMngStackRspBody, EeCommonDef.msg_route route, String sessionId)
	{
		removeLifeMngEvent(sessionId);

		return processStackStatusInfo(lifeMngStackRspBody.getStackId(), lifeMngStackRspBody.getStackStatus(), lifeMngStackRspBody.getServicesInfoList(), route);
	}

	public int processBatchRptStackReq(EeStackDef.msg_batch_rpt_stack_req_body batchRptStackReqBody, EeCommonDef.msg_route route, EeCommonDef.msg_header head)
	{
		int code = ErrorCode.SUCCESS.getCode();
		// process status
		try
		{
			List<EeStackDef.stack_status_desc> rptInfosList = batchRptStackReqBody.getRptInfosList();
			if (!CollectionUtils.isEmpty(rptInfosList))
			{
				rptInfosList.stream().forEach(stack_status_desc -> {
					processStackStatusInfo(stack_status_desc.getStackId(), stack_status_desc.getStackStatus(), stack_status_desc.getServicesInfoList(), route);
				});
			}
		}
		catch(Exception e)
		{
			LOGGER.error("process batch rpt stack error:{}, data:{}", e, batchRptStackReqBody);
			code = ErrorCode.PROCESS_BATCH_RPT_STACK_ERROR.getCode();
		}

		// response
		EeStackDef.msg_batch_rpt_stack_rsp_body.Builder rptStackRsp = EeStackDef.msg_batch_rpt_stack_rsp_body.newBuilder();
		rptStackRsp.setErrorCode(code);
		EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeRspMsgHeader(MessageName.STACK_OPERATOR, head);
		EeStackDef.msg_stack_operator_body.Builder stackOperator = EeStackDef.msg_stack_operator_body.newBuilder();
		stackOperator.setOperatorType(StackOperatorType.BATCH_RPT_STACK_RSP);
		stackOperator.setBatchRptStackRspBody(rptStackRsp);

		EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setStackOperatorBody(stackOperator).build();
		netMessageServiceFacade.submitMessage(route.getONodeId(), netMessage.toByteArray());

		return ErrorCode.SUCCESS.getCode();
	}

	public void evacuateStack(String nodeId)
	{
		TblStackInstInfoExample example = new TblStackInstInfoExample();
		TblStackInstInfoExample.Criteria criteria = example.createCriteria();
		criteria.andNodeIdEqualTo(nodeId);
		criteria.andStatusNotIn(cloudRemoveStatusList);
		List<TblStackInstInfo> tblStackInfoList = stackRepo.getInstList(example);
		if (tblStackInfoList == null || tblStackInfoList.isEmpty())
		{
			return;
		}

		for (TblStackInstInfo instInfo : tblStackInfoList)
		{
			instInfo.setStatus(StackState.SPAWNED_CLOUD_REMOVE);
			instInfo.setUpdateTime(new Date());
			stackRepo.updateInst(instInfo);
			combRpcService.getContainerInstService().setContainerStatus(instInfo.getInstId(), StackState.SPAWNED_CLOUD_REMOVE);
		}

		String nodeLike = Utils.buildStr("%",nodeId, "%");
		
		RegionResourceService.NodeInfo nodeInfo = combRpcService.getRegionResourceService().getNodeInfoByNodeId(nodeId);
		DstNode dstNode = new DstNode();
		dstNode.setDstRegionId(nodeInfo.getRegionId());
		dstNode.setDstNodeId(nodeId);
		dstNode.setDstSiteId(nodeInfo.getSiteId());

		TblStackInfoExample stackInfoExample = new TblStackInfoExample();
		TblStackInfoExample.Criteria criteriaStack = stackInfoExample.createCriteria();
		criteriaStack.andDstNodeEqualTo(dstNode);
		criteriaStack.andStatusNotEqualTo(AosStatus.REMOVE);
		List<TblStackInfo> stackList = stackRepo.getStackList(stackInfoExample);
		if (!CollectionUtils.isEmpty(stackList))
		{
			stackRepo.updateStacksStatus(stackList.stream().map(x -> x.getStackId()).collect(Collectors.toList()),
					AosStatus.REMOVE);
			for (TblStackInfo tblStackInfo : stackList)
			{
				TblStackInfo beforeUpdateStackInfoEntity = DeepCopyUtils.deepCopy(tblStackInfo);
				tblStackInfo.setStatus(AosStatus.REMOVE);
				publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateStackInfoEntity, "evacuateStack");
			}
		}

		TblStackServiceInfoExample serviceExample = new TblStackServiceInfoExample();
		TblStackServiceInfoExample.Criteria criteriaService = serviceExample.createCriteria();
		criteriaService.andDstNodesLike(nodeLike);
		criteriaService.andStatusNotEqualTo(AosStatus.REMOVE);
		TblStackServiceInfo tblStackServiceInfo = new TblStackServiceInfo();
		tblStackServiceInfo.setStatus(AosStatus.REMOVE);
		tblStackServiceInfo.setUpdateTime(new Date());
		stackRepo.updateStackService(tblStackServiceInfo, serviceExample);
	}

	public void processListStackRsp(EeStackDef.msg_list_stack_rsp_body listStackRspBody, EeCommonDef.msg_route route)
	{
		for (EeStackDef.stack_status_desc stackStatusDesc : listStackRspBody.getStackStatusInfoList())
		{
			processStackStatusInfo(stackStatusDesc.getStackId(), stackStatusDesc.getStackStatus(), stackStatusDesc.getServicesInfoList(), route);
		}
	}

	/**
	 * process create stack rsp
	 * @param createStackRspBody
	 * @param route
	 * @return
	 */
	public int processCreateRsp(EeStackDef.msg_create_stack_rsp_body createStackRspBody, EeCommonDef.msg_route route)
	{
		return processStackStatusInfo(createStackRspBody.getStackId(), createStackRspBody.getStackStatus(), createStackRspBody.getServicesInfoList(), route);
	}

	/**
	 * get service list by stack id
	 *
	 * @param stackId
	 * @param operUserId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public GetServiceListRsp getServiceByStackId(String stackId, String operUserId, Integer pageNum, Integer pageSize)
	{
		TblStackServiceInfoExample example = new TblStackServiceInfoExample();
		TblStackServiceInfoExample.Criteria criteria = example.createCriteria();
		criteria.andStackIdEqualTo(stackId);
		if (! StringUtils.isEmpty(operUserId))
		{
			criteria.andUserIdEqualTo(operUserId);
		}

		criteria.andStatusNotIn(cloudRemoveStatusList);

		PageHelper.startPage(pageNum, pageSize);
		List<TblStackServiceInfo> tblStackServiceInfoList = stackRepo.getServiceList(example);
		PageInfo<TblStackServiceInfo> pageInfo = new PageInfo<>(tblStackServiceInfoList);
		if (tblStackServiceInfoList == null || tblStackServiceInfoList.isEmpty())
		{
			return GetServiceListRsp.builder().services(Collections.EMPTY_LIST).total_num(0).build();
		}

		List<ServiceInfo> serverInfoList = new ArrayList<>();
		for (TblStackServiceInfo tblStackServiceInfo : tblStackServiceInfoList)
		{
			ServiceInfo serviceInfo = assembleService(tblStackServiceInfo);
			serverInfoList.add(serviceInfo);
		}

		return GetServiceListRsp.builder().services(serverInfoList).total_num(pageInfo.getTotal()).build();
	}

	public boolean isOwnerOfStack(String stackId, String bpId, String userId)
	{
		if (StringUtils.isEmpty(stackId))
		{
			return false;
		}
		if (StringUtils.isEmpty(userId) && StringUtils.isEmpty(bpId))
		{
			return false;
		}

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
		criteria.andStackIdEqualTo(stackId);

		List<TblStackInfo> tblStackInfos = stackRepo.getStack(example);

		return null != tblStackInfos && tblStackInfos.size() != 0;
	}

	public boolean isOwnerOfService(String serviceId, String bpId, String userId)
	{
		if (StringUtils.isEmpty(serviceId))
		{
			return false;
		}
		if (StringUtils.isEmpty(userId) && StringUtils.isEmpty(bpId))
		{
			return false;
		}

		TblStackServiceInfo tblStackServiceInfo = stackRepo.getService(serviceId);
		if (null == tblStackServiceInfo || StringUtils.isEmpty(tblStackServiceInfo.getStackId()))
		{
			return false;
		}

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
		criteria.andStackIdEqualTo(tblStackServiceInfo.getStackId());

		List<TblStackInfo> tblStackInfos = stackRepo.getStack(example);

		return null != tblStackInfos && tblStackInfos.size() != 0;
	}

	public GetStackDeployListRsp getStackDeployInfos(StackSearchCritical critical)
	{
		GetStackDeployListRsp rsp = GetStackDeployListRsp.builder().build();
		List<Integer> cloudRemoveStatusList = StackServiceFacade.cloudRemoveStatusList;
		cloudRemoveStatusList.add(RecordStatus.DELETED.value());
		int totalNum = stackRepo.countAllDeployments(critical.getStatus(), cloudRemoveStatusList,
				getFullStatus(PROGRESSING_STAGE), getFullStatus(AVAILABLE_STAGE), getFullStatus(READY_STAGE), getFullStatus(FAILED_STAGE),
				critical.getName(), critical.getRegionId(), critical.getSiteId(), critical.getNodeId(),
				critical.getBpId(), critical.getUserId(), critical.getStackType());
		if (totalNum < 1)
		{
			LOGGER.info("get stack deploy infos empty: {}", critical);
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

		List<StackDeployInfo> records = stackRepo.selectAllDeployments(critical.getStatus(), cloudRemoveStatusList,
				getFullStatus(PROGRESSING_STAGE), getFullStatus(AVAILABLE_STAGE), getFullStatus(READY_STAGE), getFullStatus(FAILED_STAGE),
				critical.getName(), critical.getRegionId(), critical.getSiteId(), critical.getNodeId(),
				critical.getBpId(), critical.getUserId(), startRow, pageSize, critical.getStackType());
		if (records == null || records.isEmpty())
		{
			LOGGER.info("get stack deploy infos empty: {}", critical);
			return rsp;
		}

		// assemble userName bpName
		records = assembleStackDeployInfos(records);

		rsp.setDeployments(records);
		return rsp;
	}

	public StackDeployInfo getStackDeployInfo(String specId)
	{
		return stackRepo.selectDeploymentBySpecId(specId, cloudRemoveStatusList,  getFullStatus(PROGRESSING_STAGE), getFullStatus(AVAILABLE_STAGE), getFullStatus(READY_STAGE), getFullStatus(FAILED_STAGE));
	}

	public GetStackListRsp getStackDeployInstancesInfo(StackSearchCritical critical)
	{
		List<StackInfo> stacks = new ArrayList<>();
		List<Integer> statusCollection = Objects.nonNull(critical.getStatus()) ?  Lists.newArrayList(SimpleStackStatusEnum.getFullStatus(critical.getStatus())) : new ArrayList<>();
		PageHelper.startPage(critical.getPageNum(), critical.getPageSize());
		List<TblStackInfo> stackInfoList = stackRepo.selectAll(critical.getSpecId(), critical.getName(), critical.getBpId(), critical.getUserId(), statusCollection,
				critical.getRegionId(), critical.getSiteId(), critical.getNodeId(), 0, 0,
				cloudRemoveStatusList, critical.getStackType());
		PageInfo<TblStackInfo> pageInfo = new PageInfo<>(stackInfoList);
		for (TblStackInfo tblStackInfo : stackInfoList)
		{
			StackInfo stackInfo = assembleStackInfo(tblStackInfo);
			stacks.add(stackInfo);
		}

		return GetStackListRsp.builder().stacks(stacks).total_num(pageInfo.getTotal()).build();
	}

	public void deleteStackDeployInfo(String specId, String userId)
	{
		TblStackSpecInfo stackSpecInfo = getStackSpecInfoBySpecId(specId, userId);
		//记录原始状态
		TblStackSpecInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(stackSpecInfo);

		deleteAssociatedResources(stackSpecInfo);

		String name = stackSpecInfo.getSpecId() + stackSpecInfo.getSpecName();
		if (name.length() > 63)
		{
			name = name.substring(0, 63);
		}
		TblStackSpecInfo tblStackSpecInfo = new TblStackSpecInfo();
		tblStackSpecInfo.setSpecId(specId);
		tblStackSpecInfo.setSpecName(name);
		tblStackSpecInfo.setUpdateTime(new Date());
		tblStackSpecInfo.setStatus(RecordStatus.DELETED.value());

		stackRepo.updateSpec(tblStackSpecInfo);
		//记录资源更新事件
		publishStackSpecStatusUpdateEvent(tblStackSpecInfo, beforeUpdateEntity, "deleteStackDeployInfo",
				RecordStatusDesProvider.INSTANCE, RecordStatusDesProvider.STATUS_FIELD);
	}

	private void setStackStatus(TblStackInfo tblStackInfo, StackInfo stackInfo)
	{
		StackInfo.StatusCode statusCode = new StackInfo.StatusCode();
		Integer status = tblStackInfo.getStatus();
		statusCode.setCode(status);
		Map<String, String> desc = new HashMap<>(2);
		SimpleStackStatusEnum simpleStatus = SimpleStackStatusEnum.getSimpleStatus(status);
		if (Objects.nonNull(simpleStatus)) {
			desc.put("en", simpleStatus.getEnName());
			desc.put("cn", simpleStatus.getCnName());
		}
		statusCode.setDesc(desc);
		stackInfo.setStatus(statusCode);
	}

	private void setServiceStatus(TblStackServiceInfo tblStackInfo, ServiceInfo serviceInfo)
	{
		ServiceInfo.StatusCode statusCode = new ServiceInfo.StatusCode();
		Integer status = tblStackInfo.getStatus();
		statusCode.setCode(status);
		Map<String, String> desc = new HashMap<>(2);
		SimpleStackStatusEnum simpleStatus = SimpleStackStatusEnum.getSimpleStatus(status);
		if (Objects.nonNull(simpleStatus)) {
			desc.put("en", simpleStatus.getEnName());
			desc.put("cn", simpleStatus.getCnName());
		}
		statusCode.setDesc(desc);
		serviceInfo.setStatus(statusCode);
	}

	private void setStackInstStatus(ContainerInstInfo serviceInstanceInfo, TblStackInstInfo tblStackInstInfo)
	{
		ContainerInstInfo.StatusCode statusCode = new ContainerInstInfo.StatusCode();
		Integer status = tblStackInstInfo.getStatus();
		statusCode.setCode(status);
		Map<String, String> desc = new HashMap<>(2);
		SimpleStackStatusEnum simpleStatus = SimpleStackStatusEnum.getSimpleStatus(status);
		if (Objects.nonNull(simpleStatus)) {
			desc.put("en", simpleStatus.getEnName().replace("stack", "inst"));
			desc.put("cn", simpleStatus.getCnName().replace("应用", "容器"));
		}
		statusCode.setDesc(desc);
		serviceInstanceInfo.setStatus(statusCode);
	}

	private void releseBindResources(TblStackInfo tblStackInfo)
	{
		DstNode dstNode = JsonUtils.fromJson((String)tblStackInfo.getDstNode(), DstNode.class);
		if (null == dstNode)
		{
			return;
		}
		//relesae resources(remove gpu bind and update edge monopoly status: remove)
		combRpcService.getSchedulerService().releaseBindResources(dstNode.getDstNodeId(), tblStackInfo.getStackId());
	}

	public String addDefaultLabels(TblStackSpecInfo tblStackSpecInfo, String stackCompose)
	{
		Map<String, Object> labels = new LinkedHashMap<>();
		String specId = tblStackSpecInfo.getSpecId();
		if (StringUtils.isNotBlank(specId))
		{
			labels.put(JDS_UUID, specId);
		}

		String userId = tblStackSpecInfo.getUserId();
		if (StringUtils.isNotBlank(userId))
		{
			labels.put(JDS_UID, userId);
		}

		String bpId = tblStackSpecInfo.getBpId();
		if (StringUtils.isNotBlank(bpId))
		{
			labels.put(JDS_BPID, bpId);
		}

		labels.put(INST_TYPE, INST_TYPE_VALUE);

		String specName = tblStackSpecInfo.getSpecName();
		if (StringUtils.isNotBlank(specName))
		{
			labels.put(JDS_NAME, specName);
		}

		try
		{
			DockerComposeYaml composeInfo = customYaml.loadAs(stackCompose, DockerComposeYaml.class);
			composeInfo.setLabels(labels);
			stackCompose = customYaml.dumpAsMap(composeInfo);
			return stackCompose;
		}
		catch (Exception e)
		{
			LOGGER.error("parse docker compose error: {}", e);
			throw new WebSystemException(STACK_COMPOSE_PARSE_FAILED, ERROR);
		}

	}

	/**
	 * get all images in docker compose
	 * @return
	 */
	private List<String> getStackComposeImages(String stackCompose) {
		DockerComposeYaml composeInfo = yaml.loadAs(stackCompose, DockerComposeYaml.class);
		return composeInfo.getImages();
	}

	private Set<CFG> getCfgs(String justiceComposeStr, Map<String, String> inputParams)
	{
		Set<CFG> cfgSet = null;
		JusticeCompose justiceCompose  = yaml.loadAs(justiceComposeStr, JusticeCompose.class);
		for (TemplateInputParams templateInputParams : justiceCompose.getBasic().getInput_params())
		{
			if (templateInputParams.getType().equalsIgnoreCase("CfgOption"))
			{
				String cfgStr = inputParams.getOrDefault(templateInputParams.getVariable(), null);
				if (StringUtils.isEmpty(cfgStr))
				{
					throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
				}
				else
				{
					String[] cfgParam = cfgStr.split("/");

					if (cfgParam.length != 5)
					{
						throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
					}

					if (cfgSet == null)
					{
						cfgSet = new HashSet<>();
					}

					CFG cfg = new CFG(cfgParam[2], cfgParam[3], cfgParam[4]);

					if (!combRpcService.getSysService().exists(cfg.getUserId(), cfg.getGroup(), cfg.getDataId()))
					{
						throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
					}

					cfgSet.add(cfg);

					inputParams.put(templateInputParams.getVariable(), String.format("/var/lnjoying/volume/${justice_stack_id}/%s/%s/%s", cfg.getUserId(), cfg.getGroup(), cfg.getDataId()));
				}
			}
		}
		return cfgSet;
	}

	private TblStackSpecInfo getStackSpecInfoBySpecId(String specId, String userId)
	{
		TblStackSpecInfo tblStackSpecInfo = stackRepo.selectStackSpecInfoByPrimaryKey(specId);
		if (tblStackSpecInfo == null)
		{
			throw new WebSystemException(ErrorCode.STACK_NOT_EXIST, ErrorLevel.INFO);
		}

		if (RecordStatus.DELETED.value() == tblStackSpecInfo.getStatus().intValue())
		{
			throw new WebSystemException(STACK_DROPPED, ErrorLevel.ERROR);
		}

		if (StringUtils.isNotBlank(userId))
		{
			if (!userId.equals(tblStackSpecInfo.getUserId()))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, INFO);
			}
		}
		return tblStackSpecInfo;
	}

	private TblStackSpecInfo getStackSpecInfoBySpecId(String specId)
	{
		TblStackSpecInfo tblStackSpecInfo = stackRepo.selectStackSpecInfoByPrimaryKey(specId);
		if (tblStackSpecInfo == null)
		{
			throw new WebSystemException(ErrorCode.STACK_NOT_EXIST, ErrorLevel.INFO);
		}

		return tblStackSpecInfo;
	}

	public void processLostStatusGT10MAndLT2H(String stackId)
	{
		TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
		//记录原始状态
		TblStackInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblStackInfo);
		if (tblStackInfo == null)
		{
			LOGGER.info("stack is null. for {}", stackId);
			return;
		}

		if (tblStackInfo.getEventType() == EventType.LOST_STATUS_GT_10M_LT_2H.getCode())
		{
			//Less than 10 min
			if (tblStackInfo.getUpdateTime().after(new Date(new Date().getTime() - 10 * 60 * 1000)))
			{
				return;
			}
		}
		else if (tblStackInfo.getEventType() != EventType.DEFAULT.getCode())
		{
			return;
		}
		tblStackInfo.setEventType(EventType.LOST_STATUS_GT_10M_LT_2H.getCode());
		tblStackInfo.setStatus(null);
		tblStackInfo.setUpdateTime(new Date());
		stackRepo.updateStack(tblStackInfo);
		//记录资源更新事件
		publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "processLostStatusGT10MAndLT2H");

		EeStackDef.msg_list_stack_req_body.Builder listStackReqBody = EeStackDef.msg_list_stack_req_body.newBuilder();
		listStackReqBody.addStackId(stackId);

		EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.STACK_OPERATOR);
		EeStackDef.msg_stack_operator_body.Builder stackOperator = EeStackDef.msg_stack_operator_body.newBuilder();
		stackOperator.setOperatorType(StackOperatorType.LIST_STACK_REQ);
		stackOperator.setListStackReqBody(listStackReqBody);

		DstNode dstNode = JsonUtils.fromJson((String)tblStackInfo.getDstNode(), DstNode.class);
		EeNetMessageApi.ee_net_message.Builder netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setStackOperatorBody(stackOperator);
		byte [] message = netMessgae.build().toByteArray();
		netMessageServiceFacade.submitMessage(dstNode.getDstNodeId(), message);
	}

	public void processLostStatusGT2H(String stackId)
	{
		TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
		//记录原始状态
		TblStackInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblStackInfo);
		if (tblStackInfo == null)
		{
			LOGGER.info("stack is null. for {}", stackId);
			return;
		}

		if (tblStackInfo.getEventType() != EventType.LOST_STATUS_GT_10M_LT_2H.getCode())
		{
			return;
		}
		tblStackInfo.setEventType(EventType.LOST_STATUS_GT_2H.getCode());
		tblStackInfo.setStatus(null);
		tblStackInfo.setUpdateTime(new Date());
		stackRepo.updateStack(tblStackInfo);
		//记录资源更新事件
		publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "processLostStatusGT2H");
	}

	private void generateStackTipMessage(StackInfo stackInfo, Integer eventTypeCode, Date reportTime)
	{
		TipMessage tipMessage = null;

		switch (stackInfo.getStatus().getCode())
		{
			case StackState.EDGE_UNREACHABLE:
				tipMessage = new TipMessage(TipMessage.TipMessageLevel.INFO.getLevel(), "节点" + stackInfo.getDst_node().getDstNodeName() + "无法连接，请选择其他节点重新部署");
				break;
			case StackState.NO_MATCH_NODE:
				tipMessage = new TipMessage(TipMessage.TipMessageLevel.INFO.getLevel(), "选定的区域/站点中暂无可用节点，请修改区域/站点或添加新的节点后再尝试部署应用");
				break;
			case StackState.CLOUD_CRETAE_FAILED_PARAMS:
				tipMessage = new TipMessage(TipMessage.TipMessageLevel.INFO.getLevel(), "参数校验失败，请检查输入的参数");
				break;
			case StackState.HARDWARE_ERROR:
				tipMessage = new TipMessage(TipMessage.TipMessageLevel.INFO.getLevel(), "节点" + stackInfo.getDst_node().getDstNodeName() + "硬件故障，请选择其他节点重新部署");
				break;
			case StackState.IMAGE_DOWNLOAD_FAILED:
				tipMessage = new TipMessage(TipMessage.TipMessageLevel.INFO.getLevel(), "镜像下载失败，请检查镜像是否存在，建议预下载镜像到节点后再尝试部署");
				break;
			case StackState.CREATE_FAILED:
				tipMessage = new TipMessage(TipMessage.TipMessageLevel.INFO.getLevel(), "在节点" + stackInfo.getDst_node().getDstNodeName() + "上创建应用失败");
				break;
		}

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
		stackInfo.setTip_message(tipMessage);
	}

	private void deleteAssociatedResources(TblStackSpecInfo specInfo)
	{
		if (Objects.nonNull(specInfo))
		{
			// Verify that there are associated inst
			TblStackInfoExample example = new TblStackInfoExample();
			TblStackInfoExample.Criteria criteria = example.createCriteria();
			criteria.andStatusNotIn(cloudRemoveStatusList);
			criteria.andSpecIdEqualTo(specInfo.getSpecId());

			List<TblStackInfo> tblStackInfos = stackRepo.getStack(example);
			if (!CollectionUtils.isEmpty(tblStackInfos))
			{
				List<CompletableFuture<Integer>> futureList = tblStackInfos.stream().map(stackInfo ->
				{
					CompletableFuture<Integer> remove = CompletableFuture.supplyAsync(() ->
					{
						deleteStack(stackInfo.getStackId(), stackInfo.getUserId());
						//删除端口
						try
						{
							combRpcService.getServiceManagerService().deleteServicePort(stackInfo.getStackId());
						}
						catch (Exception e)
						{
							LOGGER.error("delete service port error", e);
							throw e;
						}
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

	private List<StackDeployInfo> assembleStackDeployInfos(List<StackDeployInfo> records)
	{
		if (CollectionUtils.isEmpty(records))
		{
			return records;
		}

		List<StackDeployInfo> dockerContainerDeployInfos = records.stream().map(dockerContainerDeployInfo ->
		{
			return assembleStackDeployInfo(dockerContainerDeployInfo);
		}).collect(Collectors.toList());

		return dockerContainerDeployInfos;
	}

	private StackDeployInfo assembleStackDeployInfo(StackDeployInfo dockerContainerDeployInfo)
	{
		String userId = dockerContainerDeployInfo.getUserId();
		String userName = "";

		String bpId = dockerContainerDeployInfo.getBpId();
		String bpName = "";
		try {
			if (StringUtils.isNotBlank(userId))
			{
				userName = combRpcService.getUmsService().getUserNameByUserId(userId);

			}
			if (StringUtils.isNotBlank(bpId))
			{
				bpName = combRpcService.getUmsService().getBpNameByBpId(bpId);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("rpc get bpName userName error:{}", e);
		}

		dockerContainerDeployInfo.setUserId(userId);
		dockerContainerDeployInfo.setUserName(userName);
		dockerContainerDeployInfo.setBpId(bpId);
		dockerContainerDeployInfo.setBpName(bpName);
		return dockerContainerDeployInfo;
	}

	private StackInfo.FullDstNode  assembleDstNode(DstNode dstNode)
	{
		StackInfo.FullDstNode fullDstNode = new StackInfo.FullDstNode();
		if (Objects.nonNull(dstNode))
		{
			String dstNodeId = dstNode.getDstNodeId();
			CompletableFuture<String> nodeCompletableFuture = CompletableFuture.supplyAsync(() ->
			{

				if (StringUtils.isNotBlank(dstNodeId))
				{
					String nodeName = combRpcService.getRegionResourceService().getNodeNameById(dstNodeId);
					return nodeName;
				}
				return "";
			});

			String dstSiteId = dstNode.getDstSiteId();
			CompletableFuture<String> siteCompletableFuture = CompletableFuture.supplyAsync(() ->
			{
				if (StringUtils.isNotBlank(dstSiteId))
				{
					String siteName = combRpcService.getRegionResourceService().getSiteNameById(dstSiteId);
					return siteName;
				}
				return "";
			});

			String dstRegionId = dstNode.getDstRegionId();
			CompletableFuture<String> regionCompletableFuture = CompletableFuture.supplyAsync(() ->
			{
				if (StringUtils.isNotBlank(dstRegionId))
				{
					String regionName = combRpcService.getRegionResourceService().getRegionNameById(dstRegionId);
					return regionName;
				}
				return "";
			});

			CompletableFuture.allOf(nodeCompletableFuture, siteCompletableFuture, regionCompletableFuture);

			fullDstNode.setDstNodeId(dstNodeId);
			fullDstNode.setDstSiteId(dstSiteId);
			fullDstNode.setDstRegionId(dstRegionId);
			try
			{
				fullDstNode.setDstNodeName(nodeCompletableFuture.get());
				fullDstNode.setDstSiteName(siteCompletableFuture.get());
				fullDstNode.setDstRegionName(regionCompletableFuture.get());
			}
			catch (InterruptedException | ExecutionException e)
			{
				LOGGER.error("rpc getRegionResource error:{}", e);
			}
		}

		return fullDstNode;
	}

	private void sendDeleteStackMessageToEdge(String stackId)
	{
		TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
		MessagePack messagePack = new MessagePack();
		messagePack.setMsgType(AosMsgType.DELETE_STACK);
		messagePack.setMessageObj(tblStackInfo);
		stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
	}

	private void addLifeMngEvent(String sessionId, String action, String stackId, long timeStamp)
	{
		Pair<String, String> pair = new Pair<>(stackId, action);
		switch (action)
		{
			case ActionType.STOP:
			case ActionType.START:
			case ActionType.REMOVE:
			case ActionType.RESTART:
				RedisUtil.set(RedisCache.AOS_SPAWN_LIFE_EVENT + sessionId, JsonUtils.toJson(pair), 10*60);
				RedisUtil.zadd(RedisCache.AOS_SPAWN_LIFE_EVENT_SET, sessionId, timeStamp + 60*1000);
				break;
		}
	}

	private void removeLifeMngEvent(String sessionId)
	{
		RedisUtil.delete(RedisCache.AOS_SPAWN_LIFE_EVENT + sessionId);
		RedisUtil.zrem(RedisCache.AOS_SPAWN_LIFE_EVENT_SET, sessionId);
	}

	/**
	 * process file create rsp
	 * @param fileCreateRspBody
	 * @param sessionId
	 * @return
	 */
	public int processFileCreateRsp(EeFileDef.msg_file_create_rsp_body fileCreateRspBody, String sessionId)
	{
		int status = fileCreateRspBody.getStatus();
		String cfgVolumeId = sessionId.split("\\.")[2];

		TblCfgdataStackInfo tblCfgdataStackInfo = stackRepo.selectCfgdataStackInfo(cfgVolumeId);

		TblStackInfo tblStackInfo = stackRepo.getStack(tblCfgdataStackInfo.getStackId());
		//记录原始状态
		TblStackInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblStackInfo);

		switch (status)
		{
			case CfgStatus.SUCCESS:
				if (tblStackInfo.getStatus() == SYNC_CFG)
				{
					tblStackInfo.setStatus(CFG_SYNCED);
					stackRepo.updateStack(tblStackInfo);
					//记录资源修改事件
					publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "processFileCreateRsp");
				}
				if (tblCfgdataStackInfo.getSyncState() == CfgStatus.SYNCING_RESTART)
				{
					processRestartStack(tblStackInfo);
				}
				break;
			case CfgStatus.NO_PERMISSION:
			case CfgStatus.NO_SPACE:
				if (tblStackInfo.getAlwaysOnline() != null && tblStackInfo.getAlwaysOnline())
				{
					TblStackSpecInfo tblStackSpecInfo = getStackSpecInfoBySpecId(tblStackInfo.getSpecId());
					FailoverPolicy failoverPolicy = JsonUtils.fromJson(tblStackSpecInfo.getFailoverPolicy(), FailoverPolicy.class);
					if (failoverPolicy.getNeedFailover())
					{
						createNewStack(tblCfgdataStackInfo.getStackId(), failoverPolicy.getFailoverRange());
						deleteStack(tblCfgdataStackInfo.getStackId(), null);
					}
					else if (tblStackInfo.getStatus() == SYNC_CFG)
					{
						tblStackInfo.setStatus(CREATE_FAILED);
						stackRepo.updateStack(tblStackInfo);
						//记录资源更新事件
						publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "processFileCreateRsp");
					}
				}
				else if (tblStackInfo.getStatus() == SYNC_CFG)
				{
					tblStackInfo.setStatus(CREATE_FAILED);
					stackRepo.updateStack(tblStackInfo);
					//记录资源更新事件
					publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "processFileCreateRsp");
				}
				break;
			case CfgStatus.HASH_CHECK_FAILED:
			case CfgStatus.OTHER_ERROR:
				if (tblStackInfo.getStatus() == SYNC_CFG)
				{
					tblStackInfo.setStatus(CREATE_FAILED);
					stackRepo.updateStack(tblStackInfo);
					//记录资源更新事件
					publishStackInfoUpdateEvent(tblStackInfo, beforeUpdateEntity, "processFileCreateRsp");
				}
				break;
		}

		tblCfgdataStackInfo.setSyncState(status);
		tblCfgdataStackInfo.setUpdateTime(new Date());
		stackRepo.updateCfgdataStackInfo(tblCfgdataStackInfo);

		return ErrorCode.SUCCESS.getCode();
	}

	public GetCfgStackListRsp getCfgStacks(String operUserId, String dataId, String group)
	{
		GetCfgStackListRsp getCfgStackListRsp = new GetCfgStackListRsp();
		List<StackInfo> stacks = new ArrayList<>();

		List<TblStackInfo> tblStackInfos = stackRepo.getCfgStacks(operUserId, dataId, group, cloudRemoveStatusList);

		for (TblStackInfo tblStackInfo : tblStackInfos)
		{
			StackInfo stackInfo = assembleStackInfo(tblStackInfo);
			stacks.add(stackInfo);
		}

		getCfgStackListRsp.setStacks(stacks);
		return getCfgStackListRsp;
	}

	public void updateStackCfg(UpdateStackCfgReq updateStackCfgReq)
	{
		String[] cfgParam = updateStackCfgReq.getCfg().split("/");

		if (cfgParam.length != 5)
		{
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}

		TblCfgdataStackInfoExample example = new TblCfgdataStackInfoExample();
		TblCfgdataStackInfoExample.Criteria criteria = example.createCriteria();
		criteria.andStackIdIn(updateStackCfgReq.getStackIds());
		criteria.andUserIdEqualTo(cfgParam[2]);
		criteria.andDataGroupEqualTo(cfgParam[3]);
		criteria.andDataIdEqualTo(cfgParam[4]);

		List<TblCfgdataStackInfo> tblCfgdataStackInfos = stackRepo.selectCfgdataStackInfo(example);

		SysService.ConfigInfoBase configInfoBase = combRpcService.getSysService().getConfig(cfgParam[2], cfgParam[3], cfgParam[4]);

		for (TblCfgdataStackInfo tblCfgdataStackInfo : tblCfgdataStackInfos)
		{
			if (tblCfgdataStackInfo.getDataHash().equals(configInfoBase.getMd5()))
			{
				continue;
			}
			if (updateStackCfgReq.isRestart())
			{
				tblCfgdataStackInfo.setSyncState(CfgStatus.SYNCING_RESTART);
			}
			else
			{
				tblCfgdataStackInfo.setSyncState(CfgStatus.SYNCING);
			}
			tblCfgdataStackInfo.setDataHash(configInfoBase.getMd5());
			tblCfgdataStackInfo.setUpdateTime(new Date());

			stackRepo.updateCfgdataStackInfo(tblCfgdataStackInfo);

			sendFileCreateReq(configInfoBase, tblCfgdataStackInfo.getStackId(), tblCfgdataStackInfo.getNodeId(), tblCfgdataStackInfo.getCfgVolumeId());
		}
	}

	private void sendFileCreateReq(SysService.ConfigInfoBase configInfoBase, String stackId, String nodeId, String cfgVolumeId)
	{
		EeFileDef.msg_file_create_req_body.Builder fileCreateBody = EeFileDef.msg_file_create_req_body.newBuilder();
		fileCreateBody.setContent(configInfoBase.getContent());
		fileCreateBody.setFilePath(String.format("/var/lnjoying/volume/%s/%s/%s/%s", stackId, configInfoBase.getUserId(), configInfoBase.getGroup(), configInfoBase.getDataId()));
		fileCreateBody.setHash(configInfoBase.getMd5());

		EeFileDef.msg_file_operator_body.Builder fileOperatorBody = EeFileDef.msg_file_operator_body.newBuilder();
		fileOperatorBody.setAlgorithm(ConfigConstants.FILE_CREATE_ALGORITHM);
		fileOperatorBody.setOperatorType(MessageName.FILE_CREATE_REQ);
		fileOperatorBody.setFileCreateReqBody(fileCreateBody);

		EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.FILE_OPERATOR, ConfigConstants.REQ_MSG_HEADER_NAME_AOS_PREFIX + cfgVolumeId + "." + Utils.getRandomStr(10));

		EeNetMessageApi.ee_net_message.Builder netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setFileOperatorBody(fileOperatorBody);

		byte [] message = netMessgae.build().toByteArray();
		netMessageServiceFacade.submitMessage(nodeId, message);
	}

	private void createNewStack(String stackId, String failoverRange)
	{
		Pair<String, String> failoverPair = new Pair<>(stackId, failoverRange);
		MessagePack messagePack = new MessagePack();
		messagePack.setMsgType(AosMsgType.STACK_FAILOVER);
		messagePack.setMessageObj(failoverPair);
		stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
	}
	
	/**
	 *deploy a daemonset app to a node
	 * @param regionId
	 * @param siteId
	 * @param nodeId
	 * @param tblStackSpecInfo
	 */
	public void deployDaemonset(String regionId, String siteId, String nodeId, TblStackSpecInfo tblStackSpecInfo)
	{
		try
		{
			TblStackInfo tblStackInfo = new TblStackInfo();
			tblStackInfo.setSpecId(tblStackSpecInfo.getSpecId());
			tblStackInfo.setStackId(Utils.buildStr(AosPrefix.DAEMONSET, Utils.assignUUId()));
			String name = Utils.buildStr(tblStackSpecInfo.getSpecName(), "_", tblStackSpecInfo.getSpecId().substring(tblStackSpecInfo.getSpecId().length() - 8), "_", nodeId.substring(0, 4));
			if (StringUtils.isNotBlank(name))
			{
				if (name.length() > 63)
				{
					name = name.substring(0, 63);
				}
			}
			tblStackInfo.setName(name);
			tblStackInfo.setUserId(tblStackSpecInfo.getUserId());
			tblStackInfo.setBpId(tblStackSpecInfo.getBpId());
			tblStackInfo.setDevNeedInfo(tblStackSpecInfo.getDevNeedInfo());
			tblStackInfo.setAutoRun(tblStackSpecInfo.getAutoRun());
			tblStackInfo.setAlwaysOnline(true);

			tblStackInfo.setCreateTime(new Date());
			tblStackInfo.setUpdateTime(tblStackInfo.getCreateTime());
			tblStackInfo.setCreateUserId(tblStackSpecInfo.getCreateUserId());
			if (null != tblStackSpecInfo.getLabels())
			{
				tblStackInfo.setLabels(tblStackSpecInfo.getLabels());
			}
			tblStackInfo.setStatus(ASSIGNED);
			DstNode dstNode = new DstNode();
			dstNode.setDstNodeId(nodeId);
			dstNode.setDstSiteId(siteId);
			dstNode.setDstRegionId(regionId);
			tblStackInfo.setDstNode(JsonUtils.toJson(dstNode));
			tblStackInfo.setSendCreateNum(0);
			tblStackInfo.setStartNum(0);
			tblStackInfo.setFailNum(0);
			tblStackInfo.setEventType(0);
			tblStackInfo.setStackType(tblStackSpecInfo.getStackType());
			tblStackInfo.setUseServicePenetration(tblStackSpecInfo.getUseServicePenetration());

			if (tblStackSpecInfo.getUseServicePenetration() && CollectionUtils.hasContent((String)tblStackSpecInfo.getExposePorts()))
			{
				tblStackInfo.setExposePorts(tblStackSpecInfo.getExposePorts());
			}

			stackRepo.insertStack(tblStackInfo);
			//记录资源创建事件
			publishStackInfoCreateEvent(tblStackInfo, "deployDaemonset");

			if (tblStackSpecInfo.getAosType().equals("docker-compose"))
			{
				genDockerComposeStackSerice(tblStackInfo, tblStackSpecInfo.getStackCompose());
			}
		}
		catch (Exception e)
		{
			LOGGER.error("deployDaemonset failed, specId:{}, error:{}", tblStackSpecInfo.getSpecId(), e);
		}

	}
	
	public void releaseDaemonset(TblStackSpecInfo tblStackSpecInfo, String resourceId, String resLevel)
	{
		try
		{
			List<Integer> fullStatus = SimpleStackStatusEnum.getFullStatus(SimpleStackStatusEnum.STACK_NOT_EXIST.getCode());
			fullStatus.add(StackState.SPAWNED_CLOUD_REMOVE);
			fullStatus.add( StackState.FIN_CLOUD_REMOVE);
			List<String> stacks = stackRepo.getStack(tblStackSpecInfo.getSpecId(), resLevel, resourceId, fullStatus);

			if (CollectionUtils.isEmpty(stacks))
			{
				return;
			}

			for (String stackId : stacks)
			{
				try
				{
					deleteStack(stackId, null);
				}
				catch (Exception e)
				{
					LOGGER.error("delte Daemonset stack failed, stackId:{}, error:{}", stackId, e);
				}

			}
		}
		catch (Exception e)
		{
			LOGGER.error("releaseDaemonset failed, specId:{}, error:{}", tblStackSpecInfo.getSpecId(), e);
		}
        
//        stackRepo.deleteDaemonsetStackOnNode(stacks); 直接清理可能会有问题
	}



	/**做的主要工作
	 * 1、根据传入的id，取出当前已部署的所有的站点.
	 * 2、若是要求获得所有的站的数据，那么从ecrm去检索当前有哪些站点是新增的，哪些是陈旧的。若是指明了要部署的站点，那么比对当前已部署的站点，哪些是要去掉的
	 * 3、遍历有效的站点：调用ecrm的接口，获得有哪些节点是要部署stack实例，有哪些节点是要清理实例
	 * 4、清理陈旧的站点下所有的实例，清理陈旧的节点的数据
	 * @param specId
	 */
	public void processDaemonsetStack(String specId)
	{
		TblStackSpecInfo tblStackSpecInfo = getStackSpecInfoBySpecId(specId);
		
		if (tblStackSpecInfo.getStatus() != ASSIGNED)
		{
			return;
		}
		
		SchedulingStrategy schedulingStrategy = JsonUtils.fromJson(tblStackSpecInfo.getSchedulingStrategy(), SchedulingStrategy.class);
		
		Set<String> siteIds = stackRepo.getDesireDaemonsetSiteIdByspecId(tblStackSpecInfo);
		// do not use old Site Ids because the newly added nodes cannot be automatically deployed
		//Set<String> oldSiteIds = stackRepo.getDaemonsetSiteIdByspecId(tblStackSpecInfo.getSpecId());
		Set<String> oldSiteIds = new HashSet<>();
		//oldSiteIds留下的是要删除的
        if (! CollectionUtils.isEmpty(siteIds) && ! CollectionUtils.isEmpty(oldSiteIds))
        {
            for (String siteid : siteIds)
            {
                oldSiteIds.remove(siteid);
            }
        }
		
		
		if (siteIds == null)
		{
			List<LabelSelector> siteLabelSelector = CollectionUtils.isEmpty(schedulingStrategy.getLabelSelectorMap()) ? null : schedulingStrategy.getLabelSelectorMap().get(LabelType.SITE.getValue());
			Pair<Set<String>, Set<String>> sitePair = combRpcService.getRegionResourceService().getSites(siteLabelSelector, oldSiteIds);
			if (CollectionUtils.isEmpty(sitePair.getLeft()))
			{
				return;
			}
			
			siteIds = sitePair.getLeft();
			oldSiteIds = sitePair.getRight();
		}
  
		//释放不需要部署的站点的资源
        if (! CollectionUtils.isEmpty(oldSiteIds))
        {
            oldSiteIds.forEach(siteid -> releaseDaemonset(tblStackSpecInfo, siteid, DstResLevel.SITE));
        }
		
		if (siteIds.isEmpty())
		{
			return;
		}
		
		for(String siteId : siteIds)
		{
			List<String> nodeIds = stackRepo.getDaemonsetNodeIdByspecId(specId, siteId);
			List<LabelSelector> nodeLabelSelector = CollectionUtils.isEmpty(schedulingStrategy.getLabelSelectorMap()) ? null : schedulingStrategy.getLabelSelectorMap().get(LabelType.NODE.getValue());
			
			//一个站点下仅部署一个实例
			if (tblStackSpecInfo.getDeployStrategy().equals(DaemonSetDeployType.ONE_IN_SITE))
			{
				String node = CollectionUtils.isEmpty(nodeIds) ? null:nodeIds.get(0);
				RegionResourceService.SelectNode deployNode = combRpcService.getRegionResourceService().getSingleNodeInSite(siteId, nodeLabelSelector, node);
				if (deployNode == null)
				{
					continue;
				}
				
				Pair<List<String> ,List<String>> dpNodes = deployNode.getNodes();
				List<String> newNodes = dpNodes.getLeft();
                List<String> oldNodes = dpNodes.getRight();
				if (! CollectionUtils.isEmpty(newNodes))
                {
                    String newNode = newNodes.get(0);
                    //若新旧节点一样，忽略
                    if (node != null && newNode.equals(node))
                    {
                        continue;
                    }
                    //部署新节点
                    deployDaemonset(deployNode.getRegionId(), deployNode.getSiteId(), newNode, tblStackSpecInfo);
                    if (node != null)
                    {
                        //卸载旧的节点
                        releaseDaemonset(tblStackSpecInfo, node, DstResLevel.NODE);
                    }
                }
				else
                {
                    //卸载的其实是自身，如果条件不满足部署的情况下要卸载的，即使在没有其它满足的情况下
                    if (! CollectionUtils.isEmpty(oldNodes))
                    {
                        releaseDaemonset(tblStackSpecInfo, oldNodes.get(0), DstResLevel.NODE);
                    }
                }
                
				
			}
			else if (tblStackSpecInfo.getDeployStrategy().equals(DaemonSetDeployType.ANY_IN_SITE))
			{
			    //每个站点下的每个资源实例均部署
				RegionResourceService.SelectNode deployNode = combRpcService.getRegionResourceService().getMultiNodeInSite(siteId, nodeLabelSelector, nodeIds);
				if (deployNode == null)
				{
					continue;
				}
				
				List<String> newNodes = deployNode.getNodes().getLeft();
				List<String> oldNodes = deployNode.getNodes().getRight();
				//部署新的
				if (! CollectionUtils.isEmpty(newNodes))
				{
					for(String newNode : newNodes)
					{
						deployDaemonset(deployNode.getRegionId(), deployNode.getSiteId(), newNode, tblStackSpecInfo);
					}
				}
				//卸载不满足部署要求的
				if (! CollectionUtils.isEmpty(oldNodes))
				{
					for(String oldNode : oldNodes)
					{
						releaseDaemonset(tblStackSpecInfo, oldNode, DstResLevel.NODE);
					}
				}
			}
		}
	}


	private static void toSetDeployStrategy(TblStackSpecInfo tblStackSpecInfo, TblStackTemplateInfo templateInfo)
	{
		String labels = templateInfo.getLabels();
		if (StringUtils.isNotBlank(labels))
		{
			List<String> labelList= JsonUtils.fromJson(labels, new com.google.gson.reflect.TypeToken<List<String>>(){}.getType());
			if (!CollectionUtils.isEmpty(labelList))
			{
				// 0:site daemonset 1:node daemonset
				if (labelList.contains("one_in_site") || labels.contains("ONE_IN_SITE"))
				{
					tblStackSpecInfo.setDeployStrategy(0);
				}
				else if (labelList.contains("any_in_site") || labels.contains("ANY_IN_SITE"))
				{
					tblStackSpecInfo.setDeployStrategy(1);
				}
			}
		}
	}

	private void releaseUserServicePenetration(TblStackInfo tblStackInfo)
	{
		if (tblStackInfo.getUseServicePenetration() && tblStackInfo.getExposePorts() != null)
		{
			try
			{
				combRpcService.getServiceManagerService().deleteServicePort(tblStackInfo.getStackId());
			}
			catch (Exception e)
			{
				LOGGER.error("deleteServicePort error:{}", e);
			}

			ExposePortInfo exposePortInfo = JsonUtils.fromJson((String) tblStackInfo.getExposePorts(), ExposePortInfo.class);
			if (Objects.nonNull(exposePortInfo))
			{
				exposePortInfo.getExposePortMap().forEach((k, v) -> {
					try
					{
						if (StringUtils.isBlank(v.getPortId()))
						{
							LOGGER.warn("deleteMonitorEndpoint error, portId is blank, specId:{}", tblStackInfo.getSpecId());
						}
						else
						{
							combRpcService.getOmcService().deleteMonitorEndpoint(v.getType(), v.getPortId());
						}
					}
					catch (Exception e)
					{
						LOGGER.error("deleteMonitorEndpoint error:{}", e);
					}

				});
			}
		}
	}
}
