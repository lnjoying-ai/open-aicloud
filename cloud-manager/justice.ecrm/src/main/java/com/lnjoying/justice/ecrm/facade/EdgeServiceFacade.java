package com.lnjoying.justice.ecrm.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.operationevent.model.BizModelStateInfo;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionTemplateFields;
import com.lnjoying.justice.commonweb.util.DeepCopyUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.commonweb.util.TemplateEngineUtils;
import com.lnjoying.justice.ecrm.common.GoogleCodeTool;
import com.lnjoying.justice.ecrm.common.constant.*;
import com.lnjoying.justice.ecrm.config.EcrmConfig;
import com.lnjoying.justice.ecrm.config.LabelProperty;
import com.lnjoying.justice.ecrm.config.model.AgentNodeConfig;
import com.lnjoying.justice.ecrm.db.model.*;
import com.lnjoying.justice.ecrm.db.repo.EdgeRepository;
import com.lnjoying.justice.ecrm.db.repo.GwRepository;
import com.lnjoying.justice.ecrm.db.repo.SiteRepository;
import com.lnjoying.justice.ecrm.domain.MemInfo;
import com.lnjoying.justice.ecrm.domain.UpgradePlan;
import com.lnjoying.justice.ecrm.domain.dto.model.*;
import com.lnjoying.justice.ecrm.domain.dto.request.*;
import com.lnjoying.justice.ecrm.domain.dto.response.*;
import com.lnjoying.justice.ecrm.handler.actiondescription.i18n.zh_cn.EdgeActionDescriptionTemplate;
import com.lnjoying.justice.ecrm.handler.resourcesupervisor.EdgeResourceSupervisor;
import com.lnjoying.justice.ecrm.handler.resourcesupervisor.statedict.EdgeResourceStateDesProvider;
import com.lnjoying.justice.ecrm.service.CombRpcService;
import com.lnjoying.justice.ecrm.util.QRCode.QRCodeUtil;
import com.lnjoying.justice.schema.common.*;
import com.lnjoying.justice.schema.entity.docker.DockerInfo;
import com.lnjoying.justice.schema.entity.docker.DockerInstParams;
import com.lnjoying.justice.schema.service.ecrm.EdgeResourceService;
import com.lnjoying.justice.schema.service.ums.UmsService;
import com.micro.core.common.Pair;
import com.lnjoying.justice.ecrm.domain.model.search.EdgeSearchCritical;
import com.lnjoying.justice.ecrm.process.service.EcrmMsgProcessStrategy;
import com.lnjoying.justice.schema.common.ecrm.GpuState;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.lnjoying.justice.schema.constant.EdgeNodeType;
import com.lnjoying.justice.schema.constant.OnlineStatus;
import com.lnjoying.justice.schema.entity.edgeif.EdgeDevIfState;
import com.lnjoying.justice.schema.entity.edgeif.EdgeDevIfStateInfos;
import com.lnjoying.justice.schema.entity.edgeif.EdgeGwIfState;
import com.lnjoying.justice.schema.msg.*;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;


@Service
public class EdgeServiceFacade
{
	private static Logger LOGGER = LogManager.getLogger();

	@Autowired
	EdgeRepository edgeRepository;

	@Autowired
	GwRepository gwRepository;

	@Autowired
	EcrmMsgProcessStrategy ecrmMsgProcessStrategy;
	@Autowired

	NetMessageServiceFacade netMessageServiceFacade;

	@Autowired
	LabelProperty labelProperty;

	@Autowired
	SiteRepository siteRepository;

	@Autowired
    CombRpcService combRpcService;

	@Autowired
	private EcrmConfig ecrmConfig;

	@Autowired
	RegionServiceFacade regionServiceFacade;

	@Autowired
	SiteServiceFacade siteServiceFacade;

	@Autowired
	EdgeResourceService edgeResourceService;

	@Autowired
	private EdgeResourceSupervisor edgeResourceSupervisor;

	public Map<String,String> genAddCmd(EdgeInputReq edgeInputReq)
	{
		String installFileName = ecrmConfig.getInstallScriptUrl();
		if (null == installFileName)
		{
			LOGGER.error("install file not set in nacos");
			throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
		}
		String cmd = "bash <(curl -s -S -L " + installFileName + ") ";
		AgentNodeConfig agentNodeConfig = getAgentNodeConfig();
		if (agentNodeConfig == null)
		{
			LOGGER.error("gw template not set in nacos or incomplete");
			throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
		}

		if (edgeInputReq == null)
		{
			return null;
		}

		{
			//process port bind
			List<String> portBinds = new ArrayList<>();

			if (! StringUtils.isEmpty(edgeInputReq.getRest_bind()))
			{
				String restbind = "";
				if (edgeInputReq.getRest_port() > 0)
				{
					restbind = edgeInputReq.getRest_bind() + ":" + edgeInputReq.getRest_port();
//				cmd = cmd + " -p " + edgeInputReq.getRest_bind() + ":" + edgeInputReq.getRest_port();
				}
				else if (agentNodeConfig.getRestPort() > 0)
				{
					restbind = edgeInputReq.getRest_bind() + ":" + agentNodeConfig.getRestPort();
//				cmd = cmd + " -p " + edgeInputReq.getRest_bind() + ":" + nodeConfigBean.getAgent().getRest_port();
				}
				portBinds.add(restbind);
			}

			if (! portBinds.isEmpty())
			{
				String portbindStr = "port_bind=" + StringUtils.join(portBinds, ",");
				cmd = cmd + " -e "  + portbindStr;
			}
		}

		cmd = cmd + " " + agentNodeConfig.getImageName();

		if (! StringUtils.isEmpty(edgeInputReq.getNode_name()))
		{
			cmd = cmd + " --node_name " + edgeInputReq.getNode_name();
		}

		int rest_port = edgeInputReq.getRest_port() > 0 ? edgeInputReq.getRest_port():agentNodeConfig.getRestPort();
		if (rest_port > 0)
		{
			cmd = cmd + " --rest " + "0.0.0.0:" + rest_port;
		}

		if (! StringUtils.isEmpty(edgeInputReq.getRegion_id()))
		{
			LOGGER.info("test001");
			cmd = cmd + " --region " + edgeInputReq.getRegion_id();
			List<TblEdgeGwInfo> tblEdgeGwInfos = gwRepository.getGWsByRegionId(edgeInputReq.getRegion_id());
			LOGGER.info("test002");
			if (tblEdgeGwInfos != null && !tblEdgeGwInfos.isEmpty())
			{
				LOGGER.info("test003");
				List<EdgeGwIfState> nodes = new ArrayList<>();
				for (TblEdgeGwInfo tblEdgeGwInfo : tblEdgeGwInfos)
				{
					LOGGER.info("test004:" + tblEdgeGwInfo.getNodeId());
					Object object = RedisUtil.oget(RedisCache.GW_IF, tblEdgeGwInfo.getNodeId());
					if (object == null)
					{
						LOGGER.info("test005");
						continue;
					}

					nodes.add((EdgeGwIfState)object);
				}

				if (! nodes.isEmpty())
				{
					EdgeGwIfState edgeGwIfState = nodes.get(Utils.getRandomByRange(0, nodes.size()));
					cmd = cmd + " --gateway " + edgeGwIfState.getAdvertise();
				}
			}
 		}

		if (! StringUtils.isEmpty(edgeInputReq.getSite_id()))
		{
			cmd = cmd + " --site " + edgeInputReq.getSite_id();
		}

		if (null == edgeInputReq.getLabels())
		{
			edgeInputReq.setLabels(new HashMap<>());
		}
		//check orchestration
		checkOrchestration(edgeInputReq.getLabels(), edgeInputReq.getSite_id());
		checkAndSetOwner(edgeInputReq.getLabels());
		//stat switch
		if ("k8s".equals(edgeInputReq.getLabels().get(labelProperty.getNodeOrchestration())))
		{
			cmd = cmd +  " --enable_stat false";
		}

		if (StringUtils.isNotEmpty(edgeInputReq.getVendor()))
		{
			cmd = cmd +  " --vendor " + edgeInputReq.getVendor();
		}

		if (! edgeInputReq.getLabels().isEmpty())
		{
			List<String> labelList = new ArrayList<>();
			for(Map.Entry<String, String> entry : edgeInputReq.getLabels().entrySet())
			{
				if (StringUtils.isEmpty(entry.getValue()))
				{
					continue;
				}
				String label = entry.getKey() + "=" + entry.getValue();
				labelList.add(label);
			}

			String labels = StringUtils.join(labelList, "%");
			cmd = cmd +  " --labels " +  labels;
		}

		if (edgeInputReq.getTaints() != null && ! edgeInputReq.getTaints().isEmpty())
		{
			List<String> taintList = new ArrayList<>();
			for(Map.Entry<String, String> entry : edgeInputReq.getTaints().entrySet())
			{
				if (StringUtils.isEmpty(entry.getValue()))
				{
					continue;
				}
				String taint = entry.getKey() + "=" + entry.getValue();
				taintList.add(taint);
			}

			String taints = StringUtils.join(taintList, "%");
			cmd = cmd +  " --taints " +  taints;
		}

		try
		{
			int reg_token = GoogleCodeTool.generateCode();
			cmd = cmd + " --reg_token " + reg_token;
		}
		catch (InvalidKeyException e)
		{
			e.printStackTrace();
			throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
		}
		Map<String, String> ret  = new HashMap<>();
		ret.put("cmd", cmd);
		return ret;
	}

	public Pair<Integer, Integer> addNode(EeCommonDef.msg_edge_reg_req_body edgeRegReqBody)
	{

		TblEdgeComputeInfo tblEdgeComputeInfo = null;
		try
		{
			if (StringUtils.isEmpty(edgeRegReqBody.getDevInfo()))
			{
				LOGGER.error("dev info is empty");
				return new Pair<>(ErrorCode.DEV_NODEID_OCCUPIED.getCode(), ActiveStatus.REMOVED);
			}

			DevInfo devInfo = JsonUtils.fromJson(edgeRegReqBody.getDevInfo(), DevInfo.class);;


			tblEdgeComputeInfo = edgeRepository.getEdgeNode(edgeRegReqBody.getNodeId());
			if (tblEdgeComputeInfo != null)
			{
				return updateNode(edgeRegReqBody, tblEdgeComputeInfo, devInfo);
			}
			else
			{

				return addNewNode(edgeRegReqBody, devInfo);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new Pair<>(ErrorCode.PARAM_ERROR.getCode(), ActiveStatus.REMOVED);
		}
	}

	public Pair<Integer, Integer> updateNode(EeCommonDef.msg_edge_reg_req_body edgeRegReqBody, TblEdgeComputeInfo tblEdgeComputeInfo, DevInfo devInfo)
	{
		//记录原始状态
		TblEdgeComputeInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblEdgeComputeInfo);
		if (tblEdgeComputeInfo.getActiveStatus() == ActiveStatus.REMOVED)
		{
			return new Pair<>(ErrorCode.DEV_NODEID_OCCUPIED.getCode(), ActiveStatus.REMOVED);
		}

		if (!tblEdgeComputeInfo.getNodeName().equals(edgeRegReqBody.getNodeName())
				|| !tblEdgeComputeInfo.getSiteId().equals(edgeRegReqBody.getSiteId())
				|| !tblEdgeComputeInfo.getRegionId().equals(edgeRegReqBody.getRegionId())
		)
		{
			List<TblEdgeComputeLabelInfo> tblEdgeComputeLabelInfos = edgeRepository.selectLabelByNodeId(tblEdgeComputeInfo.getNodeId());
			Map<String, String> labelMap = new HashMap<>();
			if (null != tblEdgeComputeLabelInfos && tblEdgeComputeLabelInfos.size()>0)
			{
				for (TblEdgeComputeLabelInfo tblEdgeComputeLabelInfo:tblEdgeComputeLabelInfos)
				{
					labelMap.put(tblEdgeComputeLabelInfo.getLabelKey(), tblEdgeComputeLabelInfo.getLabelValue());
				}
			}

			List<TblEdgeComputeTaintInfo> tblEdgeComputeTaintInfos = edgeRepository.selectTaintByNodeId(tblEdgeComputeInfo.getNodeId());
			Map<String, String> taintMap = new HashMap<>();
			if (null != tblEdgeComputeTaintInfos && tblEdgeComputeTaintInfos.size()>0)
			{
				for (TblEdgeComputeTaintInfo tblEdgeComputeTaintInfo:tblEdgeComputeTaintInfos)
				{
					taintMap.put(tblEdgeComputeTaintInfo.getTaintKey(), tblEdgeComputeTaintInfo.getTaintValue());
				}
			}

			ConfigEdgeReq configEdgeReq = new ConfigEdgeReq();
			configEdgeReq.setNodeName(tblEdgeComputeInfo.getNodeName());
			configEdgeReq.setRegionId(tblEdgeComputeInfo.getRegionId());
			configEdgeReq.setSiteId(tblEdgeComputeInfo.getSiteId());
			configEdgeReq.setLabels(labelMap);
			configEdgeReq.setTaints(taintMap);
			configEdgeReq.setResourceLimit(new ResourceLimit(tblEdgeComputeInfo.getMemLimit(), tblEdgeComputeInfo.getCpuLimit(), tblEdgeComputeInfo.getDiskLimit()));


			configEdgeReq.getLabels().put(labelProperty.getNodeName(), configEdgeReq.getNodeName());
			configEdgeReq.getLabels().put(labelProperty.getRegionId(), configEdgeReq.getRegionId());
			String gateway = null;
			if (StringUtils.isNotEmpty(configEdgeReq.getRegionId()))
			{
				tblEdgeComputeInfo.setRegionId(configEdgeReq.getRegionId());
				gateway = edgeResourceService.getOnlineGwByRegion(configEdgeReq.getRegionId());
				if (StringUtils.isEmpty(gateway))
				{
					return new Pair<>(ErrorCode.REGION_NO_ONLINE_GW.getCode(), tblEdgeComputeInfo.getActiveStatus());
				}
			}
			configEdgeReq.getLabels().put(labelProperty.getGw(), gateway);
			configEdgeReq.getLabels().put(labelProperty.getSiteId(), configEdgeReq.getSiteId());

			sendMessag(new Pair<>(tblEdgeComputeInfo.getNodeId(), configEdgeReq), EcrmMsgType.CONFIG_NODE);
		}

		if (devInfo != null)
		{
			List<NetworkInfo> networkInfo = devInfo.getNetworkInfo();
			if (networkInfo != null)
			{
				tblEdgeComputeInfo.setNetwork(JsonUtils.toJson(networkInfo));
			}

			CpuInfo cpuInfo = devInfo.getCpuInfo();
			if (cpuInfo != null)
			{
				tblEdgeComputeInfo.setCpuFrequency(cpuInfo.getCpuFrequency());
				tblEdgeComputeInfo.setCpuLogicNum(cpuInfo.getCpuLogicNum());
				tblEdgeComputeInfo.setCpuModel(cpuInfo.getCpuModel());
				tblEdgeComputeInfo.setCpuPhysicalNum(cpuInfo.getCpuPhysicalNum());
				tblEdgeComputeInfo.setCpuNum(cpuInfo.getCpuNum());

				tblEdgeComputeInfo.setCpuLimit(cpuInfo.getCpuLogicNum());
			}
			else
			{
				tblEdgeComputeInfo.setCpuLimit(0);
			}

			MemInfo memInfo =  devInfo.getMemInfo();
			if (memInfo != null)
			{
				tblEdgeComputeInfo.setMemTotal(memInfo.getMemTotal());

				tblEdgeComputeInfo.setMemLimit(memInfo.getMemTotal());
			}
			else
			{
				tblEdgeComputeInfo.setMemLimit(0L);
			}

			DiskInfo diskInfo = devInfo.getDiskInfo();
			if (diskInfo != null)
			{
				tblEdgeComputeInfo.setDiskTotal(diskInfo.getDiskTotal());
				tblEdgeComputeInfo.setDiskType(diskInfo.getDiskTypes());
				tblEdgeComputeInfo.setDiskDetail(JsonUtils.toJson(diskInfo.getDisks()));

				tblEdgeComputeInfo.setDiskLimit(diskInfo.getDiskTotal());
			}
			else
			{
				tblEdgeComputeInfo.setDiskLimit(0L);
			}

			MachineInfo machineInfo = devInfo.getMachineInfo();
			if (machineInfo != null)
			{
				tblEdgeComputeInfo.setVender(machineInfo.getVender());
				tblEdgeComputeInfo.setProduct(machineInfo.getProduct());
				tblEdgeComputeInfo.setUuid(machineInfo.getUuid());
				tblEdgeComputeInfo.setSn(machineInfo.getSn());
				tblEdgeComputeInfo.setHostName(machineInfo.getHostname());
				tblEdgeComputeInfo.setOs(machineInfo.getOs());
			}

			if (devInfo.getSoftwareInfo() != null)
			{
				tblEdgeComputeInfo.setSoftwareVersion(JsonUtils.toJson(devInfo.getSoftwareInfo()));
			}

			if (devInfo.getSoftwareMap() != null)
			{
				tblEdgeComputeInfo.setSoftwareMap(JsonUtils.toJson(devInfo.getSoftwareMap()));
			}
		}

		tblEdgeComputeInfo.setUpdateTime(new Date());

		edgeRepository.updateEdgeSelective(tblEdgeComputeInfo);
		//记录资源更新事件
		publishEdgeComputeInfoUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, "updateNode");

		//insert gpu
		if (null != devInfo && null != devInfo.getGpuInfo() && devInfo.getGpuInfo().size() > 0)
		{
			insertOrUpdateEdgeGpuInfo(devInfo.getGpuInfo(), tblEdgeComputeInfo.getNodeId());
		}
		else
		{
			insertOrUpdateEdgeGpuInfo(new ArrayList<>(), tblEdgeComputeInfo.getNodeId());
		}

		return new Pair<>(ErrorCode.SUCCESS.getCode(), tblEdgeComputeInfo.getActiveStatus());
	}

	private void publishEdgeComputeInfoUpdateEvent(TblEdgeComputeInfo tblEdgeComputeInfo, TblEdgeComputeInfo beforeUpdateEntity, String action)
	{
		if (tblEdgeComputeInfo == null || beforeUpdateEntity == null)
		{
			LOGGER.debug("tblEdgeComputeInfo或beforeUpdateEntity为空, 跳过此次发布节点更新事件!");
			return;
		}

		if (!Objects.equals(tblEdgeComputeInfo.getActiveStatus(), beforeUpdateEntity.getActiveStatus()))
		{
			publishEdgeComputeInfoActiveStatusUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, action);
			return;
		} else if (!Objects.equals(tblEdgeComputeInfo.getOnlineStatus(), beforeUpdateEntity.getOnlineStatus()))
		{
			publishEdgeComputeInfoOnlineStatusUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, action);
			return;
		}

		try
		{
			edgeResourceSupervisor.publishUpdateEvent("节点更新", null, false,
					beforeUpdateEntity, tblEdgeComputeInfo, action,
					TemplateEngineUtils.render0(EdgeActionDescriptionTemplate.Descriptions.UPDATE_NODE,
							false,
							TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
									beforeUpdateEntity.getNodeName())));
		} catch (Exception e)
		{
			LOGGER.error("发布节点更新事件失败! stackTrace:{}, errorMessage:{}",
					ExceptionUtils.getStackTrace(e), e.getMessage());
		}
	}

	public Pair<Integer, Integer> addNewNode(EeCommonDef.msg_edge_reg_req_body edgeRegReqBody, DevInfo devInfo)
	{
		List<TblEdgeComputeInfo> tblEdgeComputeInfos = getEdgeComputeInfoByUuid(devInfo.getMachineInfo().getUuid());
		if (! CollectionUtils.isEmpty(tblEdgeComputeInfos))
		{
			return new Pair<>(ErrorCode.DEV_UUID_OCCUPIED.getCode(), ActiveStatus.REMOVED);
		}

		String regToken = edgeRegReqBody.getRegToken();

		if (! StringUtils.isEmpty(regToken))
		{
			if (! StringUtils.isNumeric(regToken))
			{
				return new Pair<>(ErrorCode.DEV_REG_TOKEN_INVALID.getCode(), ActiveStatus.REMOVED);
			}

			if (! GoogleCodeTool.check_code(Long.parseLong(regToken), System.currentTimeMillis()))
			{
				return new Pair<>(ErrorCode.DEV_REG_TOKEN_INVALID.getCode(), ActiveStatus.REMOVED);
			}
		}

		TblEdgeComputeInfo tblEdgeComputeInfo = new TblEdgeComputeInfo();

		tblEdgeComputeInfo.setNodeName(edgeRegReqBody.getNodeName());
		tblEdgeComputeInfo.setNodeId(edgeRegReqBody.getNodeId());
		if (StringUtils.isEmpty(edgeRegReqBody.getSiteId()))
		{
			tblEdgeComputeInfo.setSiteId("default");
		}
		else
		{
			tblEdgeComputeInfo.setSiteId(edgeRegReqBody.getSiteId());
		}

		if (StringUtils.isEmpty(edgeRegReqBody.getRegionId()))
		{
			tblEdgeComputeInfo.setRegionId("default");
		}
		else
		{
			tblEdgeComputeInfo.setRegionId(edgeRegReqBody.getRegionId());
		}

		if (edgeRegReqBody.getRegionId().equals("default"))
		{
			regionServiceFacade.checkDefaultRegion();
		}

		if (edgeRegReqBody.getSiteId().equals("default"))
		{
			siteServiceFacade.checkDefaultSite();
		}

		if (! StringUtils.isEmpty(edgeRegReqBody.getRegToken()))
		{
			tblEdgeComputeInfo.setActiveStatus(ActiveStatus.ACITVE);
		}
		else if (tblEdgeComputeInfo.getActiveStatus() == null)
		{
			tblEdgeComputeInfo.setActiveStatus(ActiveStatus.INACTIVE);
		}

		tblEdgeComputeInfo.setOnlineStatus(OnlineStatus.OFFLINE);

		Map<String, String> labelMap = new HashMap<>(edgeRegReqBody.getLabelsMap());

		if (devInfo != null)
		{
			List<NetworkInfo> networkInfo = devInfo.getNetworkInfo();
			if (networkInfo != null)
			{
				tblEdgeComputeInfo.setNetwork(JsonUtils.toJson(networkInfo));
			}

			CpuInfo cpuInfo = devInfo.getCpuInfo();
			if (cpuInfo != null)
			{
				tblEdgeComputeInfo.setCpuFrequency(cpuInfo.getCpuFrequency());
				tblEdgeComputeInfo.setCpuLogicNum(cpuInfo.getCpuLogicNum());
				tblEdgeComputeInfo.setCpuModel(cpuInfo.getCpuModel());
				tblEdgeComputeInfo.setCpuPhysicalNum(cpuInfo.getCpuPhysicalNum());
				tblEdgeComputeInfo.setCpuNum(cpuInfo.getCpuNum());

				tblEdgeComputeInfo.setCpuLimit(cpuInfo.getCpuLogicNum());
			}
			else
			{
				tblEdgeComputeInfo.setCpuLimit(0);
			}

			MemInfo memInfo =  devInfo.getMemInfo();
			if (memInfo != null)
			{
				tblEdgeComputeInfo.setMemTotal(memInfo.getMemTotal());

				tblEdgeComputeInfo.setMemLimit(memInfo.getMemTotal());
			}
			else
			{
				tblEdgeComputeInfo.setMemLimit(0L);
			}

			DiskInfo diskInfo = devInfo.getDiskInfo();
			if (diskInfo != null)
			{
				tblEdgeComputeInfo.setDiskTotal(diskInfo.getDiskTotal());
				tblEdgeComputeInfo.setDiskType(diskInfo.getDiskTypes());
				tblEdgeComputeInfo.setDiskDetail(JsonUtils.toJson(diskInfo.getDisks()));

				tblEdgeComputeInfo.setDiskLimit(diskInfo.getDiskTotal());
			}
			else
			{
				tblEdgeComputeInfo.setDiskLimit(0L);
			}

			MachineInfo machineInfo = devInfo.getMachineInfo();
			if (machineInfo != null)
			{
				tblEdgeComputeInfo.setVender(machineInfo.getVender());
				tblEdgeComputeInfo.setProduct(machineInfo.getProduct());
				tblEdgeComputeInfo.setUuid(machineInfo.getUuid());
				tblEdgeComputeInfo.setSn(machineInfo.getSn());
				tblEdgeComputeInfo.setHostName(machineInfo.getHostname());
				tblEdgeComputeInfo.setOs(machineInfo.getOs());

				//generate machine labels
				checkEdgeOsLabel(labelMap, machineInfo.getOs());
				checkEdgeCoreVersionLabel(labelMap, machineInfo.getCoreVersion());
			}

			if (devInfo.getSoftwareInfo() != null)
			{
				tblEdgeComputeInfo.setSoftwareVersion(JsonUtils.toJson(devInfo.getSoftwareInfo()));
			}

			if (devInfo.getSoftwareMap() != null)
			{
				tblEdgeComputeInfo.setSoftwareMap(JsonUtils.toJson(devInfo.getSoftwareMap()));

				//generate machine labels
				checkEdgeDockerVersionLabel(labelMap, devInfo.getSoftwareMap());
			}
		}

		tblEdgeComputeInfo.setCreateTime(new Date());
		tblEdgeComputeInfo.setUpdateTime(tblEdgeComputeInfo.getCreateTime());

		// get user info from labelMap
		checkAndGetOwner(tblEdgeComputeInfo, labelMap);

		edgeRepository.insertEdge(tblEdgeComputeInfo);
		//发布资源创建事件
		edgeResourceSupervisor.publishCreateEvent(tblEdgeComputeInfo,
				TemplateEngineUtils.render0(EdgeActionDescriptionTemplate.Descriptions.ADD_NODE,
						false,
						TemplateEngineUtils.newEntry("node_name", tblEdgeComputeInfo.getNodeName())), null, "addNewNode");

		//insert gpu
		if (null != devInfo && null != devInfo.getGpuInfo() && devInfo.getGpuInfo().size() > 0)
		{
			insertOrUpdateEdgeGpuInfo(devInfo.getGpuInfo(), tblEdgeComputeInfo.getNodeId());

			//check gpu label
			checkEdgeGpuLabel(labelMap, devInfo);
		}
		else
		{
			insertOrUpdateEdgeGpuInfo(new ArrayList<>(), tblEdgeComputeInfo.getNodeId());
		}

		//remove edge label
		removeEdgeLabels(tblEdgeComputeInfo.getNodeId());

		//check orchestration
		checkOrchestration(labelMap, edgeRegReqBody.getSiteId());

		//insert edge labels to the database and cache.
		insertEdgeLabels(labelMap, tblEdgeComputeInfo.getNodeId());

		return new Pair<>(ErrorCode.SUCCESS.getCode(), tblEdgeComputeInfo.getActiveStatus());
	}

	public void updateNode(String nodeId, ConfigEdgeReq configEdgeReq)
	{
		TblEdgeComputeInfo tblEdgeComputeInfo = edgeRepository.getEdgeNode(nodeId);
		//记录原始状态
		TblEdgeComputeInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblEdgeComputeInfo);
		if (tblEdgeComputeInfo == null)
		{
			throw new WebSystemException(ErrorCode.DEV_NOT_EXIST, ErrorLevel.INFO);
		}

		if (configEdgeReq.getResourceLimit() != null)
		{
			tblEdgeComputeInfo.setCpuLimit(configEdgeReq.getResourceLimit().getCpuLimit());
			tblEdgeComputeInfo.setMemLimit(configEdgeReq.getResourceLimit().getMemLimit());
			tblEdgeComputeInfo.setDiskLimit(configEdgeReq.getResourceLimit().getDiskLimit());
		}
		if (StringUtils.isNotEmpty(configEdgeReq.getNodeName()))
		{
			tblEdgeComputeInfo.setNodeName(configEdgeReq.getNodeName());
		}

		String gateway = null;
		if (StringUtils.isNotEmpty(configEdgeReq.getRegionId()))
		{
			tblEdgeComputeInfo.setRegionId(configEdgeReq.getRegionId());
			gateway = edgeResourceService.getOnlineGwByRegion(configEdgeReq.getRegionId());
			if (StringUtils.isEmpty(gateway))
			{
				throw new WebSystemException(ErrorCode.REGION_NO_ONLINE_GW, ErrorLevel.ERROR);
			}
		}
		if (StringUtils.isNotEmpty(configEdgeReq.getSiteId()))
		{
			tblEdgeComputeInfo.setSiteId(configEdgeReq.getSiteId());
		}
		tblEdgeComputeInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));

		edgeRepository.updateEdge(tblEdgeComputeInfo);
		//记录资源更新事件
		publishEdgeComputeInfoUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, "updateNode");

		//remove old label and cache
		removeEdgeLabels(nodeId);

		if (null == configEdgeReq.getLabels())
		{
			configEdgeReq.setLabels(new HashMap<>());
		}

		//check gpu label
		if (!configEdgeReq.getLabels().containsKey(labelProperty.getNodeGpu()))
		{
			List<TblEdgeComputeGpuInfo> tblEdgeComputeGpuInfos = edgeRepository.selectGpuByNodeId(nodeId);
			if (null != tblEdgeComputeGpuInfos && tblEdgeComputeGpuInfos.size() > 0)
			{
				configEdgeReq.getLabels().put(labelProperty.getNodeGpu(), tblEdgeComputeGpuInfos.get(0).getGpuType());
			}
		}

		//check orchestration
		checkOrchestration(configEdgeReq.getLabels(), tblEdgeComputeInfo.getSiteId());

		//insert edge labels to the database and cache.
		insertEdgeLabels(configEdgeReq.getLabels(), nodeId);

		edgeRepository.deleteTaints(nodeId);
		if (configEdgeReq.getTaints() != null)
		{
			for (Map.Entry<String, String> entry : configEdgeReq.getTaints().entrySet())
			{
				TblEdgeComputeTaintInfo tblEdgeComputeTaintInfo = new TblEdgeComputeTaintInfo();
				tblEdgeComputeTaintInfo.setNodeId(nodeId);
				tblEdgeComputeTaintInfo.setTaintKey(entry.getKey());
				tblEdgeComputeTaintInfo.setTaintValue(entry.getValue());
				edgeRepository.insertTaint(tblEdgeComputeTaintInfo);
			}
		}

		if (StringUtils.isNotEmpty(configEdgeReq.getNodeName()))
		{
			configEdgeReq.getLabels().put(labelProperty.getNodeName(), configEdgeReq.getNodeName());
		}

		if (StringUtils.isNotEmpty(configEdgeReq.getRegionId()))
		{
			configEdgeReq.getLabels().put(labelProperty.getRegionId(), configEdgeReq.getRegionId());
			configEdgeReq.getLabels().put(labelProperty.getGw(), gateway);
		}

		if (StringUtils.isNotEmpty(configEdgeReq.getSiteId()))
		{
			configEdgeReq.getLabels().put(labelProperty.getSiteId(), configEdgeReq.getSiteId());
		}

		if (tblEdgeComputeInfo.getOnlineStatus() == OnlineStatus.OFFLINE)
		{
			offlineNodeHostConfig(nodeId, configEdgeReq);
		}
		else
		{
			sendMessag(new Pair<>(nodeId, configEdgeReq), EcrmMsgType.CONFIG_NODE);
		}
	}

	public EdgeNodeInfo getNodeById(String nodeId)
	{
		TblEdgeComputeInfo tblEdgeComputeInfo =  edgeRepository.getEdgeNode(nodeId);
		if (tblEdgeComputeInfo == null)
		{
			return null;
		}
		EdgeNodeInfo edgeNodeInfo = new EdgeNodeInfo();
		edgeRepository.setEdgeNodeInfo(edgeNodeInfo, tblEdgeComputeInfo);
		edgeRepository.setRegionNameAndSiteName(tblEdgeComputeInfo, edgeNodeInfo);
		setUserInfoFromLables(edgeNodeInfo);
		return edgeNodeInfo;
	}

	public QueryEdgeRsp getNodes(EdgeSearchCritical edgeSearchCritical)
	{
		QueryEdgeRsp edges = edgeRepository.getEdges(edgeSearchCritical);
		if (Objects.nonNull(edges))
		{
			List<EdgeNodeInfo> nodes = edges.getNodes();
			if (!CollectionUtils.isEmpty(nodes))
			{
				nodes.stream().forEach(node -> {
					setUserInfoFromLables(node);
					setUpgradeState(node);
				});
			}
		}

		return edges;
	}

	public void deleteNode(String nodeId)
	{
		TblEdgeComputeInfo tblEdgeComputeInfo = edgeRepository.getEdgeNode(nodeId);
		//记录原始状态
		TblEdgeComputeInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblEdgeComputeInfo);
		if (tblEdgeComputeInfo == null)
		{
			throw new WebSystemException(ErrorCode.DEV_NOT_EXIST, ErrorLevel.INFO);
		}

		if (tblEdgeComputeInfo.getActiveStatus() == ActiveStatus.REMOVED)
		{
			LOGGER.debug("{} has been removed", tblEdgeComputeInfo.getNodeId());
		}
		else if (tblEdgeComputeInfo.getActiveStatus() == ActiveStatus.UPGRADE)
		{
			TblEdgeUpgradeInfo tblEdgeUpgradeInfo = edgeRepository.getEdgeUpgradeInfo(nodeId);
			if (tblEdgeUpgradeInfo != null && tblEdgeUpgradeInfo.getUpgradeStatus() < UpgradeStatus.UPGRADE_FAILED)
			{
				throw new WebSystemException(ErrorCode.DEV_UPGRADING, ErrorLevel.INFO);
			}
		}

		tblEdgeComputeInfo.setActiveStatus(ActiveStatus.REMOVED);
		tblEdgeComputeInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));

		edgeRepository.updateEdge(tblEdgeComputeInfo);
		//记录资源更新事件
		publishEdgeComputeInfoUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, "deleteNode");

		//remove old label and cache
		removeEdgeLabels(nodeId);

		//remove region cache
		RedisUtil.srem(RedisCache.ALL_ONLINE_EDGE, "", nodeId);
		RedisUtil.srem(RedisCache.REGION_ONLINE_EDGE, tblEdgeComputeInfo.getRegionId(), nodeId);
		RedisUtil.srem(RedisCache.SITE_ONLINE_EDGE, tblEdgeComputeInfo.getSiteId(), nodeId);

		sendMessag(tblEdgeComputeInfo, EcrmMsgType.REMOVE_EDGE);
	}

	private void publishEdgeComputeInfoActiveStatusUpdateEvent(TblEdgeComputeInfo tblEdgeComputeInfo, TblEdgeComputeInfo beforeUpdateEntity, String action)
	{
		try
		{
			Map<Integer, BizModelStateInfo> activeStatusDesDict = EdgeResourceStateDesProvider.INSTANCE.getStateDesDict().get(EdgeResourceStateDesProvider.ACTIVE_STATUS_FIELD);
			BizModelStateInfo stateInfo = activeStatusDesDict.get(tblEdgeComputeInfo.getActiveStatus());
			edgeResourceSupervisor.publishUpdateEvent("节点激活状态更新", null, true,
					beforeUpdateEntity, tblEdgeComputeInfo, action,
					TemplateEngineUtils.render0(EdgeActionDescriptionTemplate.Descriptions.UPDATE_NODE_ACTIVE_STATUS,
							false,
							TemplateEngineUtils.newEntry("status", Optional.ofNullable(stateInfo)
									.map(x -> x.getCnDescription())
									.orElse(Optional.ofNullable(tblEdgeComputeInfo.getActiveStatus())
											.map(x -> x.toString())
											.orElse(""))),
							TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
									tblEdgeComputeInfo.getNodeName())));
		} catch (Exception e)
		{
			LOGGER.error("发布节点激活状态更新事件失败! stackTrace:{}, errorMessage:{}",
					ExceptionUtils.getStackTrace(e), e.getMessage());
		}

	}

	public void ctrlNode(String nodeId, String action)
	{
		LOGGER.info("{} {}", action, nodeId);
		TblEdgeComputeInfo tblEdgeComputeInfo = edgeRepository.getEdgeNode(nodeId);
		if (tblEdgeComputeInfo == null)
		{
			throw new WebSystemException(ErrorCode.DEV_NOT_EXIST, ErrorLevel.INFO);
		}

		if (tblEdgeComputeInfo.getActiveStatus() == ActiveStatus.REMOVED)
		{
			throw new WebSystemException(ErrorCode.DEV_DROPPED, ErrorLevel.INFO);
		}

		if (tblEdgeComputeInfo.getActiveStatus() == ActiveStatus.UPGRADE)
		{
			throw new WebSystemException(ErrorCode.DEV_UPGRADING, ErrorLevel.INFO);
		}

		switch (action)
		{
			case ActionType.ACTIVE:
				activeNode(tblEdgeComputeInfo);
				break;
			case ActionType.DEACTIVE:
				deactiveNode(tblEdgeComputeInfo);
				break;
			case ActionType.EVACUATE:
				sendMessag(tblEdgeComputeInfo, EcrmMsgType.EVACUATE_EDGE);
				break;
			case ActionType.REBOOT:
				sendMessag(tblEdgeComputeInfo, EcrmMsgType.REBOOT_EDGE);
				publishEdgeComputeInfoControlEvent(action, tblEdgeComputeInfo, "ctrlNode");
				break;
			default:
				throw new WebSystemException(ErrorCode.ACTION_NOT_SUPPORT, ErrorLevel.INFO);
		}
		return;
	}

	private void publishEdgeComputeInfoControlEvent(String controlInstruction, TblEdgeComputeInfo tblEdgeComputeInfo, String action)
	{
		try
		{
			EdgeActionDescriptionTemplate actionDescriptionTemplate = new EdgeActionDescriptionTemplate(controlInstruction);
			if (actionDescriptionTemplate.getDescriptionTemplate().isPresent())
			{
				edgeResourceSupervisor.publishUpdateEvent("节点控制", null, true,
						tblEdgeComputeInfo, null, action,
						TemplateEngineUtils.render0(actionDescriptionTemplate.getDescriptionTemplate().get(),
								false,
								TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
										tblEdgeComputeInfo.getNodeName())));
			}
		} catch (Exception e)
		{
			LOGGER.error("发布节点控制事件失败! stackTrace:{}, errorMessage:{}",
					ExceptionUtils.getStackTrace(e), e.getMessage());
		}
	}

	private void activeNode(TblEdgeComputeInfo tblEdgeComputeInfo)
	{
		//记录原始状态
		TblEdgeComputeInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblEdgeComputeInfo);

		if (tblEdgeComputeInfo.getActiveStatus() == ActiveStatus.ACITVE)
		{
			LOGGER.debug("{} has been actived", tblEdgeComputeInfo.getNodeId());
			return;
		}

		tblEdgeComputeInfo.setActiveStatus(ActiveStatus.ACITVE);
		tblEdgeComputeInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));

		edgeRepository.updateEdge(tblEdgeComputeInfo);
		//记录资源更新事件
		publishEdgeComputeInfoUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, "activeNode");

		sendMessag(tblEdgeComputeInfo, EcrmMsgType.ACTIVE_EDGE);
	}

	private void deactiveNode(TblEdgeComputeInfo tblEdgeComputeInfo)
	{
		//记录原始状态
		TblEdgeComputeInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblEdgeComputeInfo);
		if (tblEdgeComputeInfo.getActiveStatus() == ActiveStatus.INACTIVE)
		{
			LOGGER.debug("{} has been deactived", tblEdgeComputeInfo.getNodeId());
			return;
		}

		tblEdgeComputeInfo.setActiveStatus(ActiveStatus.INACTIVE);
		tblEdgeComputeInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));

		edgeRepository.updateEdge(tblEdgeComputeInfo);
		//记录资源更新事件
		publishEdgeComputeInfoUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, "deactiveNode");
		sendMessag(tblEdgeComputeInfo, EcrmMsgType.DEACTIVE_EDGE);
	}

	private void sendMessag(Object o, String oper)
	{
		MessagePack msgPack = new MessagePack();
		msgPack.setMsgType(oper);
		msgPack.setMessageObj(o);
		ecrmMsgProcessStrategy.sendMessage(msgPack);
	}

	EeHostDef.msg_ehost_operator_body.Builder makeEhostBuilder(String operType)
	{
		return  EeHostDef.msg_ehost_operator_body.newBuilder().setOperatorType(operType);
	}

	public void processActiveEdge(TblEdgeComputeInfo tblEdgeComputeInfo)
	{
		LOGGER.info("process active edge {}", tblEdgeComputeInfo.getNodeId());
		EeHostDef.msg_host_active_req_body.Builder hostOperyBody = EeHostDef.msg_host_active_req_body.newBuilder();
		hostOperyBody.setNodeId(tblEdgeComputeInfo.getNodeId());
		EeHostDef.msg_ehost_operator_body.Builder msgEhostOperatorBodyBuilder = makeEhostBuilder(HostOperatorType.HOST_ACTIVE_REQ).setHostActiveReqBody(hostOperyBody);


		EeNetMessageApi.ee_net_message netMessage = makeEhostNetMsg(msgEhostOperatorBodyBuilder);
		netMessageServiceFacade.submitMessage(tblEdgeComputeInfo.getNodeId(), netMessage);
	}



	public void processDeactiveEdge(TblEdgeComputeInfo tblEdgeComputeInfo)
	{
		LOGGER.info("process deactive edge {}", tblEdgeComputeInfo.getNodeId());
		EeHostDef.msg_host_deactive_req_body.Builder hostOperyBody = EeHostDef.msg_host_deactive_req_body.newBuilder();
		hostOperyBody.setNodeId(tblEdgeComputeInfo.getNodeId());
		EeHostDef.msg_ehost_operator_body.Builder msgEhostOperatorBodyBuilder = makeEhostBuilder(HostOperatorType.HOST_DEACTIVE_REQ).setHostDeactiveReqBody(hostOperyBody);


		EeNetMessageApi.ee_net_message netMessage = makeEhostNetMsg(msgEhostOperatorBodyBuilder);
		netMessageServiceFacade.submitMessage(tblEdgeComputeInfo.getNodeId(), netMessage);
	}

	public void processEvacuateEdge(TblEdgeComputeInfo tblEdgeComputeInfo)
	{
		LOGGER.info("process evacuate edge {}", tblEdgeComputeInfo.getNodeId());
		EeHostDef.msg_host_evacuate_req_body.Builder hostOperyBody = EeHostDef.msg_host_evacuate_req_body.newBuilder();
		hostOperyBody.setNodeId(tblEdgeComputeInfo.getNodeId());
		EeHostDef.msg_ehost_operator_body.Builder msgEhostOperatorBodyBuilder = makeEhostBuilder(HostOperatorType.HOST_EVACUATE_REQ).setHostEvacuateReqBody(hostOperyBody);


		EeNetMessageApi.ee_net_message netMessage = makeEhostNetMsg(msgEhostOperatorBodyBuilder);
		netMessageServiceFacade.submitMessage(tblEdgeComputeInfo.getNodeId(), netMessage);
		RedisUtil.oset(RedisCache.EHOST_OPERATOR_SESSION, netMessage.getHead().getSessionId(), EcrmMsgType.EVACUATE_EDGE,RedisCache.EHOST_OPERATOR_SESSION_DURATION);
	}

	public void processRemoveEdge(TblEdgeComputeInfo tblEdgeComputeInfo)
	{
		LOGGER.info("process remove edge {}", tblEdgeComputeInfo.getNodeId());
		EeHostDef.msg_host_remove_req_body.Builder hostOperyBody = EeHostDef.msg_host_remove_req_body.newBuilder();
		hostOperyBody.setNodeId(tblEdgeComputeInfo.getNodeId());
		EeHostDef.msg_ehost_operator_body.Builder msgEhostOperatorBodyBuilder = makeEhostBuilder(HostOperatorType.HOST_REMOVE_REQ).setHostRemoveReqBody(hostOperyBody);

		EeNetMessageApi.ee_net_message netMessage = makeEhostNetMsg(msgEhostOperatorBodyBuilder);
		netMessageServiceFacade.submitMessage(tblEdgeComputeInfo.getNodeId(), netMessage);
	}

	public void processRebootEdge(TblEdgeComputeInfo tblEdgeComputeInfo)
	{
		LOGGER.info("process reboot edge {}", tblEdgeComputeInfo.getNodeId());
		EeHostDef.msg_host_reboot_req_body.Builder hostOperyBody = EeHostDef.msg_host_reboot_req_body.newBuilder();
		hostOperyBody.setNodeId(tblEdgeComputeInfo.getNodeId());
		EeHostDef.msg_ehost_operator_body.Builder msgEhostOperatorBodyBuilder = makeEhostBuilder(HostOperatorType.HOST_REBOOT_REQ).setHostRebootReqBody(hostOperyBody);

		EeNetMessageApi.ee_net_message netMessage = makeEhostNetMsg(msgEhostOperatorBodyBuilder);
		netMessageServiceFacade.submitMessage(tblEdgeComputeInfo.getNodeId(), netMessage);
	}

	EeNetMessageApi.ee_net_message makeEhostNetMsg(EeHostDef.msg_ehost_operator_body.Builder msgEhostOperatorBodyBuilder )
	{
		return EeNetMessageApi.ee_net_message.newBuilder().setHead(netMessageServiceFacade.makeReqMsgHeader(MessageName.EHOST_OPERATOR)).setEhostOperatorBody(msgEhostOperatorBodyBuilder).build();
	}

	public void processConfigEdge(Pair<String, ConfigEdgeReq> configEdgeReqPair)
	{

		String nodeId = configEdgeReqPair.getKey();
		ConfigEdgeReq configEdgeReq= configEdgeReqPair.getValue();
		LOGGER.info("process config edge {}", nodeId);
		EeHostDef.msg_host_config_req_body.Builder msgHostConfigReqpBodyOrBuilder = EeHostDef.msg_host_config_req_body.newBuilder();
		msgHostConfigReqpBodyOrBuilder.setNodeId(nodeId);
		if (configEdgeReq.getLabels() != null)
		{
			configEdgeReq.getLabels().forEach((key, value)->{if (StringUtils.isNotEmpty(value)) msgHostConfigReqpBodyOrBuilder.putLabels(key, value);});
		}

		if (configEdgeReq.getResourceLimit() != null)
		{
			ResourceLimit resourceLimit = configEdgeReq.getResourceLimit();
			EeHostDef.msg_resource_limit msgResourceLimit = EeHostDef.msg_resource_limit.newBuilder()
					.setCpuLimit(resourceLimit.getCpuLimit())
					.setDiskLimit(resourceLimit.getDiskLimit())
					.setMemLimit(resourceLimit.getMemLimit()).build();
			msgHostConfigReqpBodyOrBuilder.setResourceLimit(msgResourceLimit);
		}

		EeHostDef.msg_ehost_operator_body.Builder msgEhostOperatorBodyBuilder = makeEhostBuilder(HostOperatorType.HOST_CONFIG_REQ).setHostConfigReqBody(msgHostConfigReqpBodyOrBuilder);

		EeNetMessageApi.ee_net_message netMessage = makeEhostNetMsg(msgEhostOperatorBodyBuilder);

		RedisUtil.set(RedisCache.HOST_CONFIG_SESSION + netMessage.getHead().getSessionId(), JsonUtils.toJson(configEdgeReqPair), 10*60);
		RedisUtil.set(RedisCache.HOST_CONFIG_NODE_SESSION + nodeId, netMessage.getHead().getSessionId());
		RedisUtil.zadd(RedisCache.HOST_CONFIG_SET, netMessage.getHead().getSessionId(), netMessage.getHead().getTimestamp() + 30*1000);

		netMessageServiceFacade.submitMessage(nodeId, netMessage);
	}

	public void processEdgeIFState(Pair<String, EeNetMessageApi.ee_net_message> ifState)
	{
		String gwNodeId = ifState.getKey();
		EeNetMessageApi.ee_net_message netMessage = ifState.getValue();
		EeCtrlMessageDef.edge_dev_if_state edgeDevIfState = netMessage.getCloudCtrlBody().getSyncEdgeIfStateReqBody().getDevIfState();
		TblEdgeComputeInfo tblEdgeComputeInfo = edgeRepository.getEdgeNode(edgeDevIfState.getNodeId());
		if (tblEdgeComputeInfo == null)
		{
			return;
		}
		//记录原始状态
		TblEdgeComputeInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblEdgeComputeInfo);
		String eventName = null;

		EdgeDevIfStateInfos devIfStates = (EdgeDevIfStateInfos) RedisUtil.oget(RedisCache.EDGE_IF, edgeDevIfState.getNodeId());
		if (devIfStates == null)
		{
			LOGGER.error("dev if state is error");
			return;
		}

		int status = OnlineStatus.OFFLINE;
		for (Map.Entry<String, EdgeDevIfState> entry : devIfStates.getEdgeDevIfStateMap().entrySet())
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
		boolean needUpdateEdge = false;
		if (tblEdgeComputeInfo.getOnlineStatus() == null || status != tblEdgeComputeInfo.getOnlineStatus())
		{
			tblEdgeComputeInfo.setOnlineStatus(edgeDevIfState.getIfStatus());
			needUpdateEdge = true;
		}

		if (StringUtils.isEmpty(tblEdgeComputeInfo.getCoreVersion())
				|| !edgeDevIfState.getCoreVersion().equals(tblEdgeComputeInfo.getCoreVersion()))
		{
			tblEdgeComputeInfo.setCoreVersion(edgeDevIfState.getCoreVersion());
			needUpdateEdge = true;
		}

		if (needUpdateEdge)
		{
			tblEdgeComputeInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
			edgeRepository.updateEdge(tblEdgeComputeInfo);

			//记录资源更新
            publishEdgeComputeInfoUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, "processEdgeIFState");
		}


		if (edgeDevIfState.getIfStatus() == OnlineStatus.ONLINE)
		{
			//site, gw and edge relation, use for edge state check.
			RedisUtil.sadd(RedisCache.SITE_GW_EDGE + tblEdgeComputeInfo.getRegionId() + ":" + tblEdgeComputeInfo.getSiteId()
					+ ":", gwNodeId, tblEdgeComputeInfo.getNodeId());

			//online edge set, use for scheduling
			RedisUtil.sadd(RedisCache.ALL_ONLINE_EDGE, "", tblEdgeComputeInfo.getNodeId());
			RedisUtil.sadd(RedisCache.REGION_ONLINE_EDGE, tblEdgeComputeInfo.getRegionId(), tblEdgeComputeInfo.getNodeId());
			RedisUtil.sadd(RedisCache.SITE_ONLINE_EDGE, tblEdgeComputeInfo.getSiteId(), tblEdgeComputeInfo.getNodeId());

			getOfflineNodeHostConfig(edgeDevIfState.getNodeId());
		}
		else
		{
			//site, gw and edge relation, use for edge state check.
			RedisUtil.srem(RedisCache.SITE_GW_EDGE + tblEdgeComputeInfo.getRegionId() + ":" + tblEdgeComputeInfo.getSiteId()
					+ ":", gwNodeId, tblEdgeComputeInfo.getNodeId());

			//online edge set, use for scheduling
			RedisUtil.srem(RedisCache.ALL_ONLINE_EDGE, "", tblEdgeComputeInfo.getNodeId());
			RedisUtil.srem(RedisCache.REGION_ONLINE_EDGE, tblEdgeComputeInfo.getRegionId(), tblEdgeComputeInfo.getNodeId());
			RedisUtil.srem(RedisCache.SITE_ONLINE_EDGE, tblEdgeComputeInfo.getSiteId(), tblEdgeComputeInfo.getNodeId());
		}
	}

	private void publishEdgeComputeInfoOnlineStatusUpdateEvent(TblEdgeComputeInfo tblEdgeComputeInfo, TblEdgeComputeInfo beforeUpdateEntity, String action)
	{
		try
		{
			Map<Integer, BizModelStateInfo> onlineStatusDesDict = EdgeResourceStateDesProvider.INSTANCE.getStateDesDict().get(EdgeResourceStateDesProvider.ONLINE_STATUS_FIELD);
			BizModelStateInfo stateInfo = onlineStatusDesDict.get(tblEdgeComputeInfo.getOnlineStatus());
			edgeResourceSupervisor.publishUpdateEvent("节点在线状态更新", beforeUpdateEntity.getNodeName(), false,
					beforeUpdateEntity, tblEdgeComputeInfo, action,
					TemplateEngineUtils.render0(EdgeActionDescriptionTemplate.Descriptions.UPDATE_NODE_ONLINE_STATUS,
							false,
							TemplateEngineUtils.newEntry("status", Optional.ofNullable(stateInfo)
									.map(x -> x.getCnDescription())
									.orElse(Optional.ofNullable(tblEdgeComputeInfo.getOnlineStatus())
											.map(x -> x.toString())
											.orElse("")))));
		} catch (Exception e)
		{
			LOGGER.error("发布节点在线状态更新事件失败! stackTrace:{}, errorMessage:{}",
					ExceptionUtils.getStackTrace(e), e.getMessage());
		}
	}

	public void setEdgeStatus(EeHostDef.host_status_desc hostStatusDesc)
	{
		TblEdgeComputeInfo tblEdgeComputeInfo = edgeRepository.getEdgeNode(hostStatusDesc.getNodeId());
		if (tblEdgeComputeInfo == null)
		{
			return;
		}

		if (tblEdgeComputeInfo.getActiveStatus() == ActiveStatus.REMOVED)
		{
			return;
		}

		TblEdgeComputeInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblEdgeComputeInfo);
		if (tblEdgeComputeInfo.getActiveStatus() != hostStatusDesc.getHostStatus())
		{
			if (tblEdgeComputeInfo.getActiveStatus() == ActiveStatus.ACITVE)
			{
				sendMessag(tblEdgeComputeInfo, EcrmMsgType.ACTIVE_EDGE);
			}
			if (tblEdgeComputeInfo.getActiveStatus() == ActiveStatus.INACTIVE)
			{
				sendMessag(tblEdgeComputeInfo, EcrmMsgType.DEACTIVE_EDGE);
			}
		}
		tblEdgeComputeInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
		edgeRepository.updateEdge(tblEdgeComputeInfo);
		//记录资源更新事件
		publishEdgeComputeInfoUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, "setEdgeStatus");
	}

	public void addEdgeStat(EeStatDef.msg_host_stat_req_body reqBody)
	{

	}

	public Pair<Integer, EeCommonDef.edge_reg_info> edgeLogin(EeCommonDef.msg_edge_login_gw_req_body edgeLoginGwReqBody)
	{
		TblEdgeComputeInfo tblEdgeComputeInfo = edgeRepository.getEdgeNode(edgeLoginGwReqBody.getNodeId());
		//记录原始状态
		TblEdgeComputeInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblEdgeComputeInfo);
		if (tblEdgeComputeInfo == null)
		{
			LOGGER.info("edge login dev not exist {}", edgeLoginGwReqBody.getNodeId());
			return new Pair<>(ErrorCode.DEV_NOT_EXIST.getCode(), null);
		}

		if (tblEdgeComputeInfo.getActiveStatus() == null || tblEdgeComputeInfo.getActiveStatus() == ActiveStatus.REMOVED)
		{
			LOGGER.info("edge login dev dropped, node id {}", edgeLoginGwReqBody.getNodeId());
			
			return new Pair<>(ErrorCode.DEV_DROPPED.getCode(), null);
		}

		if (StringUtils.isEmpty(tblEdgeComputeInfo.getCoreVersion())
				|| !edgeLoginGwReqBody.getCoreVersion().equals(tblEdgeComputeInfo.getCoreVersion()))
		{
			tblEdgeComputeInfo.setCoreVersion(edgeLoginGwReqBody.getCoreVersion());
			tblEdgeComputeInfo.setUpdateTime(new Date());
			edgeRepository.updateEdge(tblEdgeComputeInfo);
			//记录资源更新事件
			publishEdgeComputeInfoUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, "edgeLogin");
		}

		EeCommonDef.edge_reg_info.Builder edgeRegInfo = assembleEdgeRegInfo(tblEdgeComputeInfo);
		return new Pair<>(ErrorCode.SUCCESS.getCode(), edgeRegInfo.build());
	}

	EeCommonDef.edge_reg_info.Builder assembleEdgeRegInfo(TblEdgeComputeInfo tblEdgeComputeInfo)
	{
		EeCommonDef.edge_reg_info.Builder edgeRegInfo = EeCommonDef.edge_reg_info.newBuilder();
		edgeRegInfo.setNodeId(tblEdgeComputeInfo.getNodeId());
		edgeRegInfo.setRegionId(tblEdgeComputeInfo.getRegionId());
		edgeRegInfo.setSiteId(tblEdgeComputeInfo.getSiteId());
		edgeRegInfo.setNodeName(tblEdgeComputeInfo.getNodeName());
		edgeRegInfo.setNodeType(EdgeNodeType.EDGE_COMPUTE);
		if (tblEdgeComputeInfo.getActiveStatus() == ActiveStatus.ACITVE)
		{
			edgeRegInfo.setDevState(EeCommonDef.DEV_STATE.CHECKPASS);
		}

		if (tblEdgeComputeInfo.getActiveStatus() == ActiveStatus.INACTIVE)
		{
			edgeRegInfo.setDevState(EeCommonDef.DEV_STATE.NEEDCHECK);
		}
		return edgeRegInfo;
	}

	public void processGetEdgeReq(Pair<String, EeNetMessageApi.ee_net_message> msgPair)
	{
		EeNetMessageApi.ee_net_message netMessage = msgPair.getValue();
		EeNetMessageApi.msg_get_edge_req_body getEdgeReqBody = netMessage.getGetEdgeReqBody();

		TblEdgeComputeInfoExample example = new TblEdgeComputeInfoExample();
		TblEdgeComputeInfoExample.Criteria criteria= example.createCriteria();
		criteria.andRegionIdIn(getEdgeReqBody.getRegionIdsList());
		Long edgeSum = edgeRepository.countEdge(example);
		int pkTotal = edgeSum.intValue() / 1000;
		if (pkTotal * 1000 <  edgeSum)
		{
			pkTotal = pkTotal + 1;
		}

		for (int pkNum = 1; pkNum <= pkTotal; pkNum++)
		{
			example.setStartRow((pkNum-1)*1000);
			example.setPageSize(1000);
			List<TblEdgeComputeInfo> tblEdgeComputeInfoList = edgeRepository.getEdges(example);

			EeNetMessageApi.msg_get_edge_rsp_body.Builder getEdgeRspBody = EeNetMessageApi.msg_get_edge_rsp_body.newBuilder();
			getEdgeRspBody.setPkNum(pkNum);
			getEdgeRspBody.setPkTotal(pkTotal);
			for (TblEdgeComputeInfo tblEdgeComputeInfo : tblEdgeComputeInfoList)
			{
				EeCommonDef.edge_reg_info.Builder edgeRegInfo = assembleEdgeRegInfo(tblEdgeComputeInfo);
				getEdgeRspBody.addEdgeNodes(edgeRegInfo);
			}

			EeCommonDef.msg_header.Builder msgHeader = netMessageServiceFacade.makeRspMsgHeader(MessageName.GET_EDGE_RSP, netMessage.getHead());
			EeNetMessageApi.ee_net_message.Builder netRspMsg = EeNetMessageApi.ee_net_message.newBuilder();
			netRspMsg.setHead(msgHeader).setGetEdgeRspBody(getEdgeRspBody);
			netMessageServiceFacade.submitMessage(msgPair.getKey(), netRspMsg.build());
		}
	}

	public void processHostImage(String nodeId, List<EeHostDef.host_image_desc> imageInfoList)
	{
		if (imageInfoList == null || imageInfoList.isEmpty())
		{
			return;
		}
		TblEdgeImageInfo tblEdgeImageInfo = edgeRepository.getHostImage(nodeId);
		List<ImageInfo> imageInfos = new ArrayList<>();
		for (EeHostDef.host_image_desc hostImageDesc : imageInfoList)
		{
			ImageInfo imageInfo = new ImageInfo();
			imageInfo.setCreate_time(hostImageDesc.getCreateTime());
			imageInfo.setImage_id(hostImageDesc.getImageId());
			imageInfo.setImage_name(hostImageDesc.getImageName());
			imageInfo.setSize(hostImageDesc.getSize());
			imageInfos.add(imageInfo);
		}

		if (tblEdgeImageInfo != null)
		{
			tblEdgeImageInfo.setImages(JsonUtils.toJson(imageInfos));
			tblEdgeImageInfo.setUpdateTime(new Date());
			edgeRepository.updateHostImage(tblEdgeImageInfo);
		}
		else
		{
			tblEdgeImageInfo = new TblEdgeImageInfo();
			tblEdgeImageInfo.setNodeId(nodeId);
			tblEdgeImageInfo.setImages(JsonUtils.toJson(imageInfos));
			tblEdgeImageInfo.setCreateTime(new Date());
			tblEdgeImageInfo.setUpdateTime(tblEdgeImageInfo.getCreateTime());
			edgeRepository.insertHostImage(tblEdgeImageInfo);
		}
	}

	public void processHostPort(String nodeId, List<EeHostDef.host_port_use_desc> portUseList)
	{
		if (portUseList == null || portUseList.isEmpty())
		{
			return;
		}

		TblEdgePortInfo tblEdgePortInfo = edgeRepository.getHostPortInfo(nodeId);
		List<PortInfo> portInfos = new ArrayList<>();
		for (EeHostDef.host_port_use_desc portUseDesc : portUseList)
		{
			PortInfo portInfo = new PortInfo();
			portInfo.setIp(portUseDesc.getIp());
			portInfo.setPort(portUseDesc.getPort());
			portInfo.setProc_name(portUseDesc.getProcName());
			portInfo.setProto(portUseDesc.getProto());
			portInfos.add(portInfo);
		}

		if (tblEdgePortInfo != null)
		{
			tblEdgePortInfo.setPorts(JsonUtils.toJson(portInfos));
			tblEdgePortInfo.setUpdateTime(new Date());
			edgeRepository.updateHostPort(tblEdgePortInfo);
		}
		else
		{
			tblEdgePortInfo = new TblEdgePortInfo();
			tblEdgePortInfo.setNodeId(nodeId);
			tblEdgePortInfo.setPorts(JsonUtils.toJson(portInfos));
			tblEdgePortInfo.setCreateTime(new Date());
			tblEdgePortInfo.setUpdateTime(tblEdgePortInfo.getCreateTime());
			edgeRepository.insertHostPort(tblEdgePortInfo);
		}
	}

	public List<ImageInfo> getNodeImages(String nodeId)
	{
		TblEdgeImageInfo tblEdgeImageInfo = edgeRepository.getHostImage(nodeId);
		if (tblEdgeImageInfo == null)
		{
			return null;
		}


		List<ImageInfo> imageInfos  = JsonUtils.fromJson(tblEdgeImageInfo.getImages(),  new com.google.gson.reflect.TypeToken<List<ImageInfo>>(){}.getType());

		if (imageInfos == null || imageInfos.isEmpty())
		{
			return null;
		}

		return imageInfos;
	}

	private AgentNodeConfig getAgentNodeConfig()
	{
		AgentNodeConfig agentNodeConfig = null;
		String imageName = ecrmConfig.getAgentImage();
		String restPortStr = ecrmConfig.getAgentRestPort();
		if (null != imageName && StringUtils.isNumeric(restPortStr))
		{
			agentNodeConfig = new AgentNodeConfig(imageName, Integer.parseInt(restPortStr));
		}
		return agentNodeConfig;
	}

	private void insertOrUpdateEdgeGpuInfo(List<GpuInfo> gpuInfos, String nodeId)
	{
		LJ_Function<TblEdgeComputeGpuInfo> lj_function = null;
		Map<String, TblEdgeComputeGpuInfo> tblEdgeComputeGpuInfoMap = edgeRepository.selectGpuByNodeId(nodeId).stream()
				.collect(Collectors.toMap(TblEdgeComputeGpuInfo::getGpuId, tblEdgeComputeGpuInfo -> tblEdgeComputeGpuInfo));
		for (GpuInfo gpuInfo:gpuInfos)
		{
			TblEdgeComputeGpuInfo tblEdgeComputeGpuInfo = edgeRepository.selectEdgeGpuByPrimaryKey(gpuInfo.getGpuId());
			if (null == tblEdgeComputeGpuInfo)
			{
				tblEdgeComputeGpuInfo = new TblEdgeComputeGpuInfo();
				tblEdgeComputeGpuInfo.setGpuId(gpuInfo.getGpuId());
				tblEdgeComputeGpuInfo.setStatus(GpuState.FREE.code);
				tblEdgeComputeGpuInfo.setRefId(null);
				tblEdgeComputeGpuInfo.setCreateTime(new Date());

				lj_function = tbl -> {
					edgeRepository.insertEdgeGpu(tbl);
				};
			}
			else
			{
				tblEdgeComputeGpuInfo.setUpdateTime(new Date());

				lj_function = tbl -> {
					edgeRepository.updateEdgeGpu(tbl);
				};
			}

			tblEdgeComputeGpuInfo.setGpuType(gpuInfo.getGpuType());
			tblEdgeComputeGpuInfo.setGpuModel(gpuInfo.getGpuModel());
			tblEdgeComputeGpuInfo.setDriverVersion(gpuInfo.getDriverVersion());
			tblEdgeComputeGpuInfo.setCudaVersion(gpuInfo.getCudaVersion());
			tblEdgeComputeGpuInfo.setCudnnVersion(gpuInfo.getCudnnVersion());
			tblEdgeComputeGpuInfo.setNodeId(nodeId);
			tblEdgeComputeGpuInfo.setIndex(gpuInfo.getIndex());

			lj_function.operator(tblEdgeComputeGpuInfo);

			tblEdgeComputeGpuInfoMap.remove(gpuInfo.getGpuId());
		}

		tblEdgeComputeGpuInfoMap.values().forEach(tblEdgeComputeGpuInfo -> {
			tblEdgeComputeGpuInfo.setStatus(GpuState.REMOVE.code);
			tblEdgeComputeGpuInfo.setUpdateTime(new Date());
			edgeRepository.updateEdgeGpu(tblEdgeComputeGpuInfo);
		});
	}

	private void checkEdgeGpuLabel(Map<String, String> labelMap, DevInfo devInfo)
	{
		if (!labelMap.containsKey(labelProperty.getNodeGpu()))
		{
			labelMap.put(labelProperty.getNodeGpu(), devInfo.getGpuInfo().get(0).getGpuType());
		}
	}

	private void checkEdgeOsLabel(Map<String, String> labelMap, String version)
	{
		if (!labelMap.containsKey(labelProperty.getNodeOs()) && StringUtils.isNotEmpty(version))
		{
			labelMap.put(labelProperty.getNodeOs(), version);
		}
	}

	private void checkEdgeCoreVersionLabel(Map<String, String> labelMap, String coreVersion)
	{
		if (!labelMap.containsKey(labelProperty.getNodeCoreVersion()) && StringUtils.isNotEmpty(coreVersion))
		{
			labelMap.put(labelProperty.getNodeCoreVersion(), coreVersion);
		}
	}

	private void checkEdgeDockerVersionLabel(Map<String, String> labelMap, Map<String, String> softwareMap)
	{
		String dockerInfoStr;
		if (!labelMap.containsKey(labelProperty.getNodeDockerVersion()) && StringUtils.isNotEmpty(dockerInfoStr = softwareMap.get("docker")))
		{
			ObjectMapper mapper = new ObjectMapper();
			try
			{
				ObjectNode dockerInfo = mapper.readValue(dockerInfoStr, ObjectNode.class);
				String dockerVersion;
				if (StringUtils.isNotEmpty(dockerVersion = dockerInfo.get("ServerVersion").asText()))
				{
					labelMap.put(labelProperty.getNodeDockerVersion(), dockerVersion);
				}
			}
			catch (JsonProcessingException e)
			{
				e.printStackTrace();
				LOGGER.error("Parsing docker info error. {}", dockerInfoStr);
			}
		}
	}

	private void insertEdgeLabels(Map<String, String> labelMap, String nodeId)
	{
		for (Map.Entry<String, String> entry : labelMap.entrySet())
		{
			if (null == entry.getValue() || entry.getValue().isEmpty())
			{
				continue;
			}
			//save to db
			TblEdgeComputeLabelInfo tblEdgeComputeLabelInfo = new TblEdgeComputeLabelInfo();
			tblEdgeComputeLabelInfo.setNodeId(nodeId);
			tblEdgeComputeLabelInfo.setLabelKey(entry.getKey());
			tblEdgeComputeLabelInfo.setLabelValue(entry.getValue());
			edgeRepository.insertLabel(tblEdgeComputeLabelInfo);
			//set cache
			String[] values = entry.getValue().split(",");
			for (String value:values)
			{
				RedisUtil.sadd(RedisCache.LABEL_EDGE, entry.getKey() + ":" + value, nodeId);
			}
			RedisUtil.sadd(RedisCache.LABEL_EDGE, entry.getKey(), nodeId);
			RedisUtil.sadd(RedisCache.LABEL_EDGE_SET, "", entry.getKey());
		}
	}

	private void removeEdgeLabels(String nodeId)
	{
		List<TblEdgeComputeLabelInfo> tblEdgeComputeLabelInfos = edgeRepository.selectLabelByNodeId(nodeId);
		if (null != tblEdgeComputeLabelInfos && !tblEdgeComputeLabelInfos.isEmpty())
		{
			edgeRepository.deleteLabels(nodeId);
			for (TblEdgeComputeLabelInfo tblEdgeComputeLabelInfo:tblEdgeComputeLabelInfos)
			{
				if (null == tblEdgeComputeLabelInfo.getLabelValue() || tblEdgeComputeLabelInfo.getLabelValue().isEmpty())
				{
					continue;
				}
				String[] values = tblEdgeComputeLabelInfo.getLabelValue().split(",");
				for (String value:values)
				{
					RedisUtil.srem(RedisCache.LABEL_EDGE, tblEdgeComputeLabelInfo.getLabelKey() + ":" + value, nodeId);
					Long keyValueMembers =  RedisUtil.scard(RedisCache.LABEL_EDGE + tblEdgeComputeLabelInfo.getLabelKey() + ":" + value);
					if (null != keyValueMembers && keyValueMembers <= 0)
					{
						RedisUtil.delete(RedisCache.LABEL_EDGE + tblEdgeComputeLabelInfo.getLabelKey() + ":" + value);
					}
				}
				RedisUtil.srem(RedisCache.LABEL_EDGE, tblEdgeComputeLabelInfo.getLabelKey(), nodeId);
				Long keyMembers =  RedisUtil.scard(RedisCache.LABEL_EDGE + tblEdgeComputeLabelInfo.getLabelKey());
				if (null != keyMembers && keyMembers <= 0)
				{
					RedisUtil.delete(RedisCache.LABEL_EDGE + tblEdgeComputeLabelInfo.getLabelKey());
					RedisUtil.srem(RedisCache.LABEL_EDGE_SET, "", tblEdgeComputeLabelInfo.getLabelKey());
				}
			}
		}
	}

	private void checkOrchestration(Map<String, String> labels, String siteId)
	{
		if (!labels.containsKey(labelProperty.getNodeOrchestration()))
		{
			TblSiteInfo tblSiteInfo = siteRepository.getSite(siteId);
			if (tblSiteInfo != null)
			{
				Map<String, String> siteLabels = JsonUtils.fromJson(tblSiteInfo.getLabels(), new TypeToken<Map<String, String>>(){}.getType());
				String value = "docker";
				if (siteLabels.containsKey(labelProperty.getSiteOrchestration()))
				{
					value = siteLabels.get(labelProperty.getSiteOrchestration());
				}
				labels.put(labelProperty.getNodeOrchestration(), value);
			}
		}
	}

	/**
	 * owner label is not set set to current user, set bp id default
	 * @param labels
	 */
	private void checkAndSetOwner(Map<String, String> labels)
	{
		if (!isAdmin())
		{
			boolean ownerLabelNotExist = StringUtils.isBlank(labels.get(labelProperty.getNodeOwner()));
			boolean bpLabelNotExist = StringUtils.isBlank(labels.get(labelProperty.getNodeBpId()));
			if (ownerLabelNotExist && bpLabelNotExist)
			{
				// Set current user information
				labels.put(labelProperty.getNodeOwner(), getUserId());
				labels.put(labelProperty.getNodeBpId(), getBpId());
				return;
			}
			else if (bpLabelNotExist)
			{
				labels.put(labelProperty.getNodeBpId(), getBpId());
			}
			else
			{
				// set specified owner information
			}
		}
	}

	private void checkAndGetOwner(TblEdgeComputeInfo edgeComputeInfo, Map<String, String> labels)
	{
		String userId = labelProperty.getNodeOwner();
		String bpId = labelProperty.getNodeBpId();
		if (labels.containsKey(userId))
		{
			edgeComputeInfo.setUserId(labels.get(userId));

			// assemble bp info
			if (!labels.containsValue(bpId))
			{
				try
				{
					UmsService.UserInfo userInfo = combRpcService.getUmsService().getUserInfoByUseId(labels.get(userId));
					if (Objects.nonNull(userInfo))
					{
						edgeComputeInfo.setBpId(userInfo.getBpId());
						labels.put(bpId, userInfo.getBpId());
					}
				}
				catch (Exception e)
				{
					LOGGER.error("error adding node label bp, userId:{}, node:{}", userId, edgeComputeInfo.getNodeId());
				}

			}
		}


		if (labels.containsValue(bpId))
		{
			edgeComputeInfo.setBpId(labels.get(bpId));
		}
	}

	private void setUserInfoFromLables(EdgeNodeInfo edgeNodeInfo)
	{
		Map<String, String> labels = edgeNodeInfo.getLabels();
		if (!CollectionUtils.isEmpty(labels))
		{
			labels.entrySet().stream().forEach(entry -> {
				try
				{
					if (labelProperty.getNodeBpId().equals(entry.getKey()))
					{
						String bpId = entry.getValue();
						if (StringUtils.isNotBlank(bpId))
						{
							String bpName = combRpcService.getUmsService().getBpNameByBpId(bpId);
							edgeNodeInfo.setBp_id(bpId);
							edgeNodeInfo.setBp_name(bpName);
						}
					}
					else if (labelProperty.getNodeUserId().equals(entry.getKey()))
					{
						String userId = entry.getValue();
						if (StringUtils.isNotBlank(userId))
						{
							String userName = combRpcService.getUmsService().getUserNameByUserId(userId);
							edgeNodeInfo.setUser_id(userId);
							edgeNodeInfo.setUser_name(userName);
						}
					}
				}
				catch (Exception e)
				{
					LOGGER.error("user labels error");
				}
			});
		}
	}

	private void setUpgradeState(EdgeNodeInfo edgeNodeInfo)
	{
		if (edgeNodeInfo.getActive_status() == ActiveStatus.UPGRADE)
		{
			TblEdgeUpgradeInfo tblEdgeUpgradeInfo = edgeRepository.getEdgeUpgradeInfo(edgeNodeInfo.getNode_id());
			edgeNodeInfo.setUpgrade_status(tblEdgeUpgradeInfo == null || tblEdgeUpgradeInfo.getUpgradeStatus() == null ?
					0 : tblEdgeUpgradeInfo.getUpgradeStatus());
		}
	}

	public List<VersionInfo> getVersions()
	{
		List<VersionInfo> versionInfos;
		String agentVersions = ecrmConfig.getAgentVersions();
		versionInfos = JsonUtils.fromJson(agentVersions, new TypeToken<List<VersionInfo>>(){}.getType());
		return versionInfos;
	}

	private VersionInfo getVersion(String version)
	{
		List<VersionInfo> versionInfos = getVersions();

		return versionInfos.stream().filter(item -> version.equals(item.getVersion())).findAny().orElse(null);
	}

	public void agentUpgrade(String nodeId, String newVersion, boolean needConfirm)
	{
		VersionInfo newVersionInfo = getVersion(newVersion);
		if (null == newVersionInfo)
		{
			throw new WebSystemException(ErrorCode.VERSION_INFO_EMPTY, ErrorLevel.INFO);
		}

		TblEdgeComputeInfo tblEdgeComputeInfo = edgeRepository.getEdgeNode(nodeId);
		//记录原始状态
		TblEdgeComputeInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblEdgeComputeInfo);
		if (tblEdgeComputeInfo == null)
		{
			throw new WebSystemException(ErrorCode.DEV_NOT_EXIST, ErrorLevel.INFO);
		}

		if (tblEdgeComputeInfo.getActiveStatus() != ActiveStatus.ACITVE
				|| tblEdgeComputeInfo.getOnlineStatus() != OnlineStatus.ONLINE)
		{
			LOGGER.info("{} node cannot upgrade", nodeId);
			throw new WebSystemException(ErrorCode.DEV_CANNOT_UPGRADE, ErrorLevel.INFO);
		}

		Map<String, String> softwareMap = JsonUtils.fromJson(tblEdgeComputeInfo.getSoftwareMap(), new TypeToken<Map<String, String>>(){}.getType());
		String arch;
		if (softwareMap != null)
		{
			DockerInfo dockerInfo = JsonUtils.fromJson(softwareMap.get("docker"), DockerInfo.class);
			if (dockerInfo != null)
			{
				switch (dockerInfo.getArchitecture())
				{
					case "x86_64":
						arch = "linux/amd64";
						break;
					case "aarch64":
						arch = "linux/arm64";
						break;
					default:
						LOGGER.info("node {} arch is: {}, cannot upgrade", nodeId, dockerInfo.getArchitecture());
						throw new WebSystemException(ErrorCode.DEV_CANNOT_UPGRADE, ErrorLevel.INFO);
				}

				if (! newVersionInfo.getImage_hash().containsKey(arch))
				{
					LOGGER.info("miss version info of arch: {}, node {} cannot upgrade", dockerInfo.getArchitecture(), nodeId);
					throw new WebSystemException(ErrorCode.MISS_VERSION_INFO, ErrorLevel.INFO);
				}
			}
			else
			{
				LOGGER.info("node {} docker info empty, cannot upgrade", nodeId);
				throw new WebSystemException(ErrorCode.DEV_CANNOT_UPGRADE, ErrorLevel.INFO);
			}
		}
		else
		{
			LOGGER.info("node {} software info empty, cannot upgrade", nodeId);
			throw new WebSystemException(ErrorCode.DEV_CANNOT_UPGRADE, ErrorLevel.INFO);
		}

		tblEdgeComputeInfo.setActiveStatus(ActiveStatus.UPGRADE);
		edgeRepository.updateEdge(tblEdgeComputeInfo);
		//记录资源更新事件
		publishEdgeComputeInfoUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, "agentUpgrade");

		insertUpgradePlan(nodeId, tblEdgeComputeInfo.getCoreVersion(), newVersionInfo, needConfirm, tblEdgeComputeInfo, arch);

		RedisUtil.sadd(RedisCache.UPGRADE_NODEID_SET, "", nodeId);
	}

	private void insertUpgradePlan(String nodeId, String oldVersion, VersionInfo newVersionInfo, boolean needConfirm, TblEdgeComputeInfo tblEdgeComputeInfo, String arch)
	{
		TblEdgeUpgradeInfo tblEdgeUpgradeInfo = edgeRepository.getEdgeUpgradeInfo(nodeId);
		boolean update = true;
		if (tblEdgeUpgradeInfo == null)
		{
			tblEdgeUpgradeInfo = new TblEdgeUpgradeInfo();
			tblEdgeUpgradeInfo.setNodeId(nodeId);
			update = false;
		}

		tblEdgeUpgradeInfo.setOldVersion(oldVersion);
		tblEdgeUpgradeInfo.setNewVersion(newVersionInfo.getVersion());
		List<UpgradePlan> upgradePlans = buildUpgradePlans(nodeId, oldVersion, newVersionInfo, needConfirm, arch);
		tblEdgeUpgradeInfo.setUpgradePlan(JsonUtils.toJson(upgradePlans));
		tblEdgeUpgradeInfo.setUpgradeStatus(UpgradeStatus.PLANED);
		tblEdgeUpgradeInfo.setCreateTime(new Date());
		tblEdgeUpgradeInfo.setUpdateTime(tblEdgeUpgradeInfo.getCreateTime());
		tblEdgeUpgradeInfo.setOperBp(getBpId());
		tblEdgeUpgradeInfo.setOperUser(getUserId());
		tblEdgeUpgradeInfo.setRegionId(tblEdgeComputeInfo.getRegionId());
		tblEdgeUpgradeInfo.setSiteId(tblEdgeComputeInfo.getSiteId());

		if (update)
		{
			edgeRepository.updateEdgeUpgradeInfo(tblEdgeUpgradeInfo);
		}
		else
		{
			edgeRepository.insertEdgeUpgradeInfo(tblEdgeUpgradeInfo);
		}
	}

	private List<UpgradePlan> buildUpgradePlans(String nodeId, String oldVersion, VersionInfo newVersionInfo, boolean needConfirm, String arch)
	{
		List<UpgradePlan> upgradePlans = new ArrayList<>();

		upgradePlans.add(buildCreateWettyPlan(nodeId));
		upgradePlans.add(buildUpgradePlan(nodeId, oldVersion, newVersionInfo, arch));
		if (needConfirm)
		{
			upgradePlans.add(new UpgradePlan(nodeId, UpgradePlanType.MANUAL_CONFIRM));
		}
		else
		{
			upgradePlans.add(buildConfirmPlan(nodeId, oldVersion, newVersionInfo));
		}
		upgradePlans.add(buildRollbackPlan(nodeId, oldVersion, newVersionInfo));
		upgradePlans.add(buildRemoveWettyPlan(nodeId));
		return upgradePlans;
	}

	private UpgradePlan buildCreateWettyPlan(String nodeId)
	{
		UpgradePlan createWettyPlan = new UpgradePlan(nodeId, UpgradePlanType.CREATE_WETTY);
		return createWettyPlan;
	}

	private UpgradePlan buildUpgradePlan(String nodeId, String oldVersion, VersionInfo newVersionInfo, String arch)
	{
		UpgradePlan upgradePlan = new UpgradePlan(nodeId, UpgradePlanType.UPGRADE, "side-kick-updater");
		DockerInstParams dockerInstParams = upgradePlan.getContainerPlan().getDockerInstParams();
		dockerInstParams.getHostConfig().setPrivileged(true);
		dockerInstParams.setEntrypoint(new String[]{"./upgrade.sh"});
		dockerInstParams.setImage("lnjoying/side-kick-upgrade:v0.1.0");
		List<String> envs = new ArrayList<>();
		envs.add(String.format("IMAGE_NAME=%s", newVersionInfo.getImage_name()));
		envs.add(String.format("IMAGE_HASH=%s", newVersionInfo.getImage_hash().get(arch)));
		dockerInstParams.setEnv(envs);
		dockerInstParams.setCmd(new String[] {"--vnew", newVersionInfo.getVersion(), "--vold", oldVersion});
		dockerInstParams.getHostConfig().setBinds(Arrays.asList("/var/run/docker.sock:/var/run/docker.sock", "/var/lnjoying:/var/lnjoying"));
		upgradePlan.getContainerPlan().setAutoRun(true);
//		upgradePlan.getContainerPlan().setLogSucess("upgrade completed");
		upgradePlan.getContainerPlan().setLogFailed("upgrade failed");
		return upgradePlan;
	}

	private UpgradePlan buildConfirmPlan(String nodeId, String oldVersion, VersionInfo newVersionInfo)
	{
		UpgradePlan confirmPlan = new UpgradePlan(nodeId, UpgradePlanType.AUTO_CONFIRM, "side-kick-confirmer");
		DockerInstParams dockerInstParams = confirmPlan.getContainerPlan().getDockerInstParams();
		dockerInstParams.getHostConfig().setPrivileged(true);
		dockerInstParams.setEntrypoint(new String[]{"./confirm.sh"});
		dockerInstParams.setImage("lnjoying/side-kick-upgrade:v0.1.0");
		dockerInstParams.setCmd(new String[] {"--vnew", newVersionInfo.getVersion(), "--vold", oldVersion});
		dockerInstParams.getHostConfig().setBinds(Arrays.asList("/var/run/docker.sock:/var/run/docker.sock", "/var/lnjoying:/var/lnjoying"));
		confirmPlan.getContainerPlan().setAutoRun(true);
		return confirmPlan;
	}

	private UpgradePlan buildRollbackPlan(String nodeId, String oldVersion, VersionInfo newVersionInfo)
	{
		UpgradePlan rollbackPlan = new UpgradePlan(nodeId, UpgradePlanType.ROLLBACK, "side-kick-rollback");
		DockerInstParams dockerInstParams = rollbackPlan.getContainerPlan().getDockerInstParams();
		dockerInstParams.getHostConfig().setPrivileged(true);
//		dockerInstParams.getHostConfig().setAutoRemove(true);
		dockerInstParams.setEntrypoint(new String[]{"./rollback.sh"});
		dockerInstParams.setImage("lnjoying/side-kick-upgrade:v0.1.0");
		dockerInstParams.setCmd(new String[] {"--vnew", newVersionInfo.getVersion(), "--vold", oldVersion});
		dockerInstParams.getHostConfig().setBinds(Arrays.asList("/var/run/docker.sock:/var/run/docker.sock", "/var/lnjoying:/var/lnjoying"));
		rollbackPlan.getContainerPlan().setAutoRun(true);
		rollbackPlan.getContainerPlan().setLogSucess("rollback completed");
		rollbackPlan.getContainerPlan().setLogFailed("rollback failed");
		return rollbackPlan;
	}

	private UpgradePlan buildRemoveWettyPlan(String nodeId)
	{
		UpgradePlan removeWettyPlan = new UpgradePlan(nodeId, UpgradePlanType.REMOVE_WETTY);
		return removeWettyPlan;
	}

	public UpgradeLog getUpgradeLog(String nodeId)
	{
		TblEdgeUpgradeInfo tblEdgeUpgradeInfo = edgeRepository.getEdgeUpgradeInfo(nodeId);
		return UpgradeLog.of(tblEdgeUpgradeInfo);
	}

	public void confirm(String nodeId, String action)
	{
		TblEdgeUpgradeInfo tblEdgeUpgradeInfo = edgeRepository.getEdgeUpgradeInfo(nodeId);
		if (tblEdgeUpgradeInfo == null || tblEdgeUpgradeInfo.getUpgradeStatus() != UpgradeStatus.MANUAL_CONFIRM)
		{
			throw new WebSystemException(ErrorCode.DEV_UPGRADE_CANNOT_CONFIRM, ErrorLevel.INFO);
		}

		List<UpgradePlan> upgradePlans = JsonUtils.fromJson(tblEdgeUpgradeInfo.getUpgradePlan(), new TypeToken<List<UpgradePlan>>(){}.getType());

		switch (action)
		{
			case "confirm":
			{
				upgradePlans.forEach(upgradePlan -> {
					if (upgradePlan.getPlanName().equals(UpgradePlanType.MANUAL_CONFIRM))
					{
						upgradePlan.setStatus(UpgradePlanStatus.COMPLETE);
					}
					else if (upgradePlan.getPlanName().equals(UpgradePlanType.ROLLBACK))
					{
						upgradePlan.setStatus(UpgradePlanStatus.SKIP);
					}
				});
				tblEdgeUpgradeInfo.setUpgradePlan(JsonUtils.toJson(upgradePlans));
				tblEdgeUpgradeInfo.setUpgradeStatus(UpgradeStatus.CLEAN);
				tblEdgeUpgradeInfo.setUpdateTime(new Date());
				edgeRepository.updateEdgeUpgradeInfo(tblEdgeUpgradeInfo);
				TblEdgeComputeInfo tblEdgeComputeInfo = edgeRepository.getEdgeNode(nodeId);
				//记录原始状态
				TblEdgeComputeInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblEdgeComputeInfo);
				tblEdgeComputeInfo.setActiveStatus(ActiveStatus.ACITVE);
				tblEdgeComputeInfo.setUpdateTime(new Date());
				edgeRepository.updateEdge(tblEdgeComputeInfo);
				//记录资源更新事件
				publishEdgeComputeInfoUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, "confirm");
				break;
			}
			case "rollback":
			{
				upgradePlans.forEach(upgradePlan -> {
					if (upgradePlan.getPlanName().equals(UpgradePlanType.MANUAL_CONFIRM))
					{
						upgradePlan.setStatus(UpgradePlanStatus.COMPLETE);
					}
				});
				tblEdgeUpgradeInfo.setUpgradePlan(JsonUtils.toJson(upgradePlans));
				tblEdgeUpgradeInfo.setUpgradeStatus(UpgradeStatus.ROLLBACK);
				tblEdgeUpgradeInfo.setUpdateTime(new Date());
				edgeRepository.updateEdgeUpgradeInfo(tblEdgeUpgradeInfo);
				//记录事件
				TblEdgeComputeInfo tblEdgeComputeInfo = edgeRepository.getEdgeNode(nodeId);
				edgeResourceSupervisor.publishUpdateEvent("节点升级回滚", null, true,
						tblEdgeComputeInfo, null, "confirm",
						TemplateEngineUtils.render0(EdgeActionDescriptionTemplate.Descriptions.CONFIRM_UPGRADE_NODE_ROLLBACK,
								false,
								TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
										tblEdgeComputeInfo.getNodeName())));
				break;
			}
			default:
				break;
		}
	}

	public GetQRCodeRsp getQrcodes(String nodeId)
	{
		TblEdgeComputeInfo tblEdgeComputeInfo = edgeRepository.getEdgeNode(nodeId);
		if (tblEdgeComputeInfo == null)
		{
			throw new WebSystemException(ErrorCode.DEV_NOT_EXIST, ErrorLevel.INFO);
		}

		List<NetworkInfo> networkInfos = JsonUtils.fromJson(tblEdgeComputeInfo.getNetwork(), new com.google.gson.reflect.TypeToken<List<NetworkInfo>>(){}.getType());

		if (StringUtils.isEmpty(tblEdgeComputeInfo.getVender()))
		{
			tblEdgeComputeInfo.setVender("unknown");
		}

		QrcodeContent content = QrcodeContent.builder().vendor(tblEdgeComputeInfo.getVender()).
				uuid(tblEdgeComputeInfo.getUuid()).mac(StringUtils.join(networkInfos.stream().map(NetworkInfo::getMac).collect(Collectors.toList()), ',')).build();

		return new GetQRCodeRsp(QRCodeUtil.getBase64QRCode(JsonUtils.toJson(content)));
	}

	public EdgeBindsRsp bindNodes(EdgeBindsReq edgeBindsReq)
	{
		List<BindAck> acks = new ArrayList<>();
		for (BindRawNodeInfo bindRawNodeInfo : edgeBindsReq.getBindInfos())
		{
			List<TblEdgeComputeInfo> tblEdgeComputeInfos = getEdgeComputeInfoByUuid(bindRawNodeInfo.getUuid());
			if (CollectionUtils.isEmpty(tblEdgeComputeInfos))
			{
				acks.add(new BindAck(bindRawNodeInfo.getUuid(), null, HttpStatus.NOT_FOUND.value(), "节点不存在"));
			}
			else if (tblEdgeComputeInfos.size() == 1)
			{
				TblEdgeComputeInfo tblEdgeComputeInfo = tblEdgeComputeInfos.get(0);
				//记录原始状态
				TblEdgeComputeInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblEdgeComputeInfo);
				Map<String, String> labels = new HashMap<>();
				List<TblEdgeComputeLabelInfo> edgeComputeLabelInfos = edgeRepository.selectLabelByNodeId(tblEdgeComputeInfo.getNodeId());
				if (! CollectionUtils.isEmpty(edgeComputeLabelInfos))
				{
					edgeComputeLabelInfos.forEach(edgeComputeLabelInfo -> {
						labels.put(edgeComputeLabelInfo.getLabelKey(), edgeComputeLabelInfo.getLabelValue());
					});
				}

				String gateway = edgeResourceService.getOnlineGwByRegion(bindRawNodeInfo.getRegionId());
				if (StringUtils.isEmpty(gateway))
				{
					acks.add(new BindAck(bindRawNodeInfo.getUuid(), null, HttpStatus.CONFLICT.value(), "所选区域无可用网关"));
					continue;
				}

				String edgeBp = labels.getOrDefault(labelProperty.getNodeBpId(), null);
				String edgeOwner = labels.getOrDefault(labelProperty.getNodeOwner(), null);

				if (isAdmin() || isPersonal())
				{
					String owner = getUserId();
					if (StringUtils.isNotEmpty(edgeBp) || (StringUtils.isNotEmpty(edgeOwner) && !edgeOwner.equals(owner)))
					{
//						acks.add(new BindAck(bindRawNodeInfo.getUuid(), null, HttpStatus.CONFLICT.value(), "节点已被其他用户绑定"));
						acks.add(new BindAck(bindRawNodeInfo.getUuid(), null, HttpStatus.CONFLICT.value(), "节点已被绑定"));
					}
					else if (StringUtils.isEmpty(edgeOwner))
					{
						tblEdgeComputeInfo.setNodeName(bindRawNodeInfo.getNodeName());
						tblEdgeComputeInfo.setRegionId(bindRawNodeInfo.getRegionId());
						tblEdgeComputeInfo.setSiteId(bindRawNodeInfo.getSiteId());
						labels.put(labelProperty.getNodeOwner(), owner);
						tblEdgeComputeInfo.setActiveStatus(ActiveStatus.ACITVE);
						tblEdgeComputeInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
						edgeRepository.updateEdge(tblEdgeComputeInfo);
						//记录资源更新事件
						publishEdgeComputeInfoUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, "bindNodes");

						//remove edge label
						removeEdgeLabels(tblEdgeComputeInfo.getNodeId());

						//insert edge labels to the database and cache.
						insertEdgeLabels(labels, tblEdgeComputeInfo.getNodeId());

						ConfigEdgeReq configEdgeReq = new ConfigEdgeReq(null, labels, null, null, null, null);

						if (tblEdgeComputeInfo.getOnlineStatus() == OnlineStatus.ONLINE)
						{
							sendMessag(tblEdgeComputeInfo, EcrmMsgType.ACTIVE_EDGE);

							labels.put(labelProperty.getRegionId(), bindRawNodeInfo.getRegionId());
							labels.put(labelProperty.getGw(), gateway);
							labels.put(labelProperty.getSiteId(), bindRawNodeInfo.getSiteId());
							labels.put(labelProperty.getNodeName(), bindRawNodeInfo.getNodeName());

							sendMessag(new Pair<>(tblEdgeComputeInfo.getNodeId(), configEdgeReq), EcrmMsgType.CONFIG_NODE);
						}
						else
						{
							offlineNodeHostConfig(tblEdgeComputeInfo.getNodeId(), configEdgeReq);
						}

						acks.add(new BindAck(bindRawNodeInfo.getUuid(), null, HttpStatus.OK.value(), "绑定成功"));
					}
					else
					{
						acks.add(new BindAck(bindRawNodeInfo.getUuid(), null, HttpStatus.CONFLICT.value(), "节点已被绑定"));
					}
				}
				else
				{
					String bp = getBpId();
					String owner = getUserId();
					if ((StringUtils.isNotEmpty(edgeOwner) && !edgeOwner.equals(owner)) || (StringUtils.isNotEmpty(edgeBp) && !edgeBp.equals(bp)))
					{
//						acks.add(new BindAck(bindRawNodeInfo.getUuid(), null, HttpStatus.CONFLICT.value(), "节点已被其他用户绑定"));
						acks.add(new BindAck(bindRawNodeInfo.getUuid(), null, HttpStatus.CONFLICT.value(), "节点已被绑定"));
					}
					else if (StringUtils.isEmpty(edgeOwner) && StringUtils.isEmpty(edgeBp))
					{
						tblEdgeComputeInfo.setNodeName(bindRawNodeInfo.getNodeName());
						tblEdgeComputeInfo.setRegionId(bindRawNodeInfo.getRegionId());
						tblEdgeComputeInfo.setSiteId(bindRawNodeInfo.getSiteId());
						labels.put(labelProperty.getNodeOwner(), owner);
						labels.put(labelProperty.getNodeBpId(), bp);
						tblEdgeComputeInfo.setActiveStatus(ActiveStatus.ACITVE);
						tblEdgeComputeInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
						edgeRepository.updateEdge(tblEdgeComputeInfo);
						//记录资源更新事件
						publishEdgeComputeInfoUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, "bindNodes");

						//remove edge label
						removeEdgeLabels(tblEdgeComputeInfo.getNodeId());

						//insert edge labels to the database and cache.
						insertEdgeLabels(labels, tblEdgeComputeInfo.getNodeId());

						ConfigEdgeReq configEdgeReq = new ConfigEdgeReq(null, labels, null, null, null, null);

						if (tblEdgeComputeInfo.getOnlineStatus() == OnlineStatus.ONLINE)
						{
							sendMessag(tblEdgeComputeInfo, EcrmMsgType.ACTIVE_EDGE);

							labels.put(labelProperty.getRegionId(), bindRawNodeInfo.getRegionId());
							labels.put(labelProperty.getGw(), gateway);
							labels.put(labelProperty.getSiteId(), bindRawNodeInfo.getSiteId());
							labels.put(labelProperty.getNodeName(), bindRawNodeInfo.getNodeName());

							sendMessag(new Pair<>(tblEdgeComputeInfo.getNodeId(), configEdgeReq), EcrmMsgType.CONFIG_NODE);
						}
						else
						{
							offlineNodeHostConfig(tblEdgeComputeInfo.getNodeId(), configEdgeReq);
						}

						acks.add(new BindAck(bindRawNodeInfo.getUuid(), null, HttpStatus.OK.value(), "绑定成功"));
					}
					else
					{
						acks.add(new BindAck(bindRawNodeInfo.getUuid(), null, HttpStatus.CONFLICT.value(), "节点已被绑定"));
					}
				}
			}
			else
			{
				acks.add(new BindAck(bindRawNodeInfo.getUuid(), null, HttpStatus.CONFLICT.value(), "UUID冲突"));
			}
		}
		return new EdgeBindsRsp(acks);
	}

	public List<TblEdgeComputeInfo> getEdgeComputeInfoByUuid(String uuid)
	{
		TblEdgeComputeInfoExample example = new TblEdgeComputeInfoExample();
		TblEdgeComputeInfoExample.Criteria criteria= example.createCriteria();
		criteria.andActiveStatusNotEqualTo(ActiveStatus.REMOVED);
		criteria.andUuidEqualTo(uuid);
		return edgeRepository.getEdges(example);
	}

	public void offlineNodeHostConfig(String nodeId, ConfigEdgeReq configEdgeReq)
	{
		RedisUtil.sadd(RedisCache.HOST_CONFIG_OFFLINE, "", nodeId);
		RedisUtil.set(RedisCache.HOST_CONFIG_NODE + nodeId, JsonUtils.toJson(new Pair<>(nodeId, configEdgeReq)));
	}

	public void getOfflineNodeHostConfig(String nodeId)
	{
		if (RedisUtil.sismember(RedisCache.HOST_CONFIG_OFFLINE, "", nodeId))
		{
			String pairJson = RedisUtil.get(RedisCache.HOST_CONFIG_NODE + nodeId);
			if (!StringUtils.isEmpty(pairJson))
			{
				Pair<String, ConfigEdgeReq> configEdgeReqPair = JsonUtils.fromJson(pairJson, new com.google.gson.reflect.TypeToken<Pair<String,ConfigEdgeReq>>(){}.getType());
				if (configEdgeReqPair != null)
				{
					MessagePack messagePack = new MessagePack();
					messagePack.setMsgType(EcrmMsgType.CONFIG_NODE);
					messagePack.setMessageObj(configEdgeReqPair);
					ecrmMsgProcessStrategy.sendMessage(messagePack);
				}
			}
			RedisUtil.srem(RedisCache.HOST_CONFIG_OFFLINE, "", nodeId);
			RedisUtil.delete(RedisCache.HOST_CONFIG_NODE + nodeId);
		}
	}

	public void deleteHostConfigSessionCache(String sessionId)
	{
		RedisUtil.zrem(RedisCache.HOST_CONFIG_SET, sessionId);
		RedisUtil.delete(RedisCache.HOST_CONFIG_SESSION + sessionId);
	}

	public EdgeUnbindsRsp unbindNodes(EdgeUnbindsReq edgeUnbindsReq)
	{
		List<UnBindAck> acks = new ArrayList<>();
		for (String nodeId : edgeUnbindsReq.getNodeIds())
		{
			TblEdgeComputeInfo tblEdgeComputeInfo = edgeRepository.getEdgeNode(nodeId);
			//记录原始状态
			TblEdgeComputeInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblEdgeComputeInfo);
			if (tblEdgeComputeInfo == null)
			{
				acks.add(new UnBindAck(null, nodeId, HttpStatus.NOT_FOUND.value(), "节点不存在"));
			}
			else
			{
				Map<String, String> labels = new HashMap<>();
				List<TblEdgeComputeLabelInfo> edgeComputeLabelInfos = edgeRepository.selectLabelByNodeId(nodeId);
				if (! CollectionUtils.isEmpty(edgeComputeLabelInfos))
				{
					edgeComputeLabelInfos.forEach(edgeComputeLabelInfo -> {
						labels.put(edgeComputeLabelInfo.getLabelKey(), edgeComputeLabelInfo.getLabelValue());
					});
				}

				String edgeBp = labels.getOrDefault(labelProperty.getNodeBpId(), null);
				String edgeOwner = labels.getOrDefault(labelProperty.getNodeOwner(), null);

				if (isAdmin())
				{
					if (edgeBp == null)
					{
						if (edgeOwner == null)
						{
							acks.add(new UnBindAck(tblEdgeComputeInfo.getUuid(), nodeId, HttpStatus.CONFLICT.value(), "节点未绑定"));
							continue;
						}
						else
						{
							labels.remove(labelProperty.getNodeBpId());
						}
					}
					else
					{
						labels.remove(labelProperty.getNodeBpId());
						if (edgeOwner != null) labels.remove(labelProperty.getNodeOwner());
					}

					tblEdgeComputeInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
					edgeRepository.updateEdge(tblEdgeComputeInfo);
					//记录资源更新事件
					edgeResourceSupervisor.publishUpdateEvent("节点解绑", null, true,
							beforeUpdateEntity, tblEdgeComputeInfo, "unbindNodes",
							TemplateEngineUtils.render0(EdgeActionDescriptionTemplate.Descriptions.UNBIND_SINGLE_NODE,
									false,
									TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
											tblEdgeComputeInfo.getNodeName())));

					//remove edge label
					removeEdgeLabels(tblEdgeComputeInfo.getNodeId());

					//insert edge labels to the database and cache.
					insertEdgeLabels(labels, tblEdgeComputeInfo.getNodeId());

					ConfigEdgeReq configEdgeReq = new ConfigEdgeReq(null, labels, null, null, null, null);

					if (tblEdgeComputeInfo.getOnlineStatus() == OnlineStatus.ONLINE)
					{
						sendMessag(tblEdgeComputeInfo, EcrmMsgType.ACTIVE_EDGE);

						sendMessag(new Pair<>(tblEdgeComputeInfo.getNodeId(), configEdgeReq), EcrmMsgType.CONFIG_NODE);
					}
					else
					{
						offlineNodeHostConfig(tblEdgeComputeInfo.getNodeId(), configEdgeReq);
					}

					acks.add(new UnBindAck(tblEdgeComputeInfo.getUuid(), nodeId, HttpStatus.OK.value(), "解绑成功"));
				}
				else if (isPersonal() || isBpUser())
				{
					String owner = getUserId();
					if (StringUtils.isNotEmpty(edgeOwner) && edgeOwner.equals(owner))
					{
						labels.remove(labelProperty.getNodeOwner());
						if (edgeBp != null) labels.remove(labelProperty.getNodeBpId());

						tblEdgeComputeInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
						edgeRepository.updateEdge(tblEdgeComputeInfo);
						//记录资源更新事件
						edgeResourceSupervisor.publishUpdateEvent("节点解绑", null, true,
								beforeUpdateEntity, tblEdgeComputeInfo, "unbindNodes",
								TemplateEngineUtils.render0(EdgeActionDescriptionTemplate.Descriptions.UNBIND_SINGLE_NODE,
										false,
										TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
												tblEdgeComputeInfo.getNodeName())));
						//remove edge label
						removeEdgeLabels(tblEdgeComputeInfo.getNodeId());

						//insert edge labels to the database and cache.
						insertEdgeLabels(labels, tblEdgeComputeInfo.getNodeId());

						ConfigEdgeReq configEdgeReq = new ConfigEdgeReq(null, labels, null, null, null, null);

						if (tblEdgeComputeInfo.getOnlineStatus() == OnlineStatus.ONLINE)
						{
							sendMessag(tblEdgeComputeInfo, EcrmMsgType.ACTIVE_EDGE);

							sendMessag(new Pair<>(tblEdgeComputeInfo.getNodeId(), configEdgeReq), EcrmMsgType.CONFIG_NODE);
						}
						else
						{
							offlineNodeHostConfig(tblEdgeComputeInfo.getNodeId(), configEdgeReq);
						}

						acks.add(new UnBindAck(tblEdgeComputeInfo.getUuid(), nodeId, HttpStatus.OK.value(), "解绑成功"));
					}
					else
					{
						acks.add(new UnBindAck(tblEdgeComputeInfo.getUuid(), nodeId, HttpStatus.CONFLICT.value(), "节点解绑冲突"));
					}
				}
				else
				{
					String bp = getBpId();
					String owner = getUserId();
					if (StringUtils.isNotEmpty(edgeBp) && edgeBp.equals(bp))
					{
						labels.remove(labelProperty.getNodeBpId());
						if (edgeOwner != null) labels.remove(labelProperty.getNodeOwner());
						tblEdgeComputeInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
						edgeRepository.updateEdge(tblEdgeComputeInfo);
						//记录资源事件
						edgeResourceSupervisor.publishUpdateEvent("节点解绑", null, true,
								beforeUpdateEntity, tblEdgeComputeInfo, "unbindNodes",
								TemplateEngineUtils.render0(EdgeActionDescriptionTemplate.Descriptions.UNBIND_SINGLE_NODE,
										false,
										TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
												tblEdgeComputeInfo.getNodeName())));

						//remove edge label
						removeEdgeLabels(tblEdgeComputeInfo.getNodeId());

						//insert edge labels to the database and cache.
						insertEdgeLabels(labels, tblEdgeComputeInfo.getNodeId());

						ConfigEdgeReq configEdgeReq = new ConfigEdgeReq(null, labels, null, null, null, null);

						if (tblEdgeComputeInfo.getOnlineStatus() == OnlineStatus.ONLINE)
						{
							sendMessag(tblEdgeComputeInfo, EcrmMsgType.ACTIVE_EDGE);

							sendMessag(new Pair<>(tblEdgeComputeInfo.getNodeId(), configEdgeReq), EcrmMsgType.CONFIG_NODE);
						}
						else
						{
							offlineNodeHostConfig(tblEdgeComputeInfo.getNodeId(), configEdgeReq);
						}

						acks.add(new UnBindAck(tblEdgeComputeInfo.getUuid(), nodeId, HttpStatus.OK.value(), "绑定成功"));
					}
					else
					{
						acks.add(new UnBindAck(tblEdgeComputeInfo.getUuid(), nodeId, HttpStatus.CONFLICT.value(), "节点解绑冲突"));
					}
				}
			}
		}
		return new EdgeUnbindsRsp(acks);
	}

	public void patchNode(String nodeId, PatchEdgeReq patchEdgeReq)
	{

		Map<String, String> newLabels = patchEdgeReq.getLabels();
		if (CollectionUtils.isEmpty(newLabels))
		{
			return;
		}

		Map<String, String> existingLabelMap  = getAllLabelsForNode(nodeId);

		for (Map.Entry<String, String> entry : newLabels.entrySet())
		{
			String labelKey = entry.getKey();
			String frontendLabelValue = entry.getValue();
			String existingLabelValue = existingLabelMap.get(labelKey);
			if (StringUtils.isBlank(existingLabelValue))
			{
				TblEdgeComputeLabelInfo tblEdgeComputeLabelInfo = new TblEdgeComputeLabelInfo();
				tblEdgeComputeLabelInfo.setNodeId(nodeId);
				tblEdgeComputeLabelInfo.setLabelKey(entry.getKey());
				tblEdgeComputeLabelInfo.setLabelValue(entry.getValue());
				edgeRepository.insertEdgeComputeLabelSelective(tblEdgeComputeLabelInfo);
			}
			else
			{
				if (!Objects.equals(existingLabelValue, frontendLabelValue)) {
					TblEdgeComputeLabelInfo tblEdgeComputeLabelInfo = new TblEdgeComputeLabelInfo();
					tblEdgeComputeLabelInfo.setNodeId(nodeId);
					tblEdgeComputeLabelInfo.setLabelKey(entry.getKey());
					tblEdgeComputeLabelInfo.setLabelValue(entry.getValue());
					edgeRepository.updateEdgeComputeLabelByPrimaryKeySelective(tblEdgeComputeLabelInfo);
				}
			}

		}
	}

	public Map<String, String> getAllLabelsForNode(String nodeId) {
		List<TblEdgeComputeLabelInfo> labels   = edgeRepository.selectLabelByNodeId(nodeId);

		Map<String, String> labelMap = new HashMap<>();
		for (TblEdgeComputeLabelInfo label : labels) {
			labelMap.put(label.getLabelKey(), label.getLabelValue());
		}

		return labelMap;
	}
}
