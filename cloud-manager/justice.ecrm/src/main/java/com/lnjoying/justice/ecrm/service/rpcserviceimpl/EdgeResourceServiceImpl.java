package com.lnjoying.justice.ecrm.service.rpcserviceimpl;

import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.ecrm.common.constant.*;
import com.lnjoying.justice.ecrm.config.LabelProperty;
import com.lnjoying.justice.ecrm.db.model.*;
import com.lnjoying.justice.ecrm.db.repo.EdgeRepository;
import com.lnjoying.justice.ecrm.db.repo.GwRepository;
import com.lnjoying.justice.ecrm.db.repo.ServiceAgentRepository;
import com.lnjoying.justice.ecrm.db.repo.SiteRepository;
import com.lnjoying.justice.ecrm.domain.dto.model.EdgeNodeInfo;
import com.lnjoying.justice.ecrm.domain.dto.model.NetworkInfo;
import com.lnjoying.justice.ecrm.domain.dto.request.AddServiceAgentReq;
import com.lnjoying.justice.ecrm.facade.ServiceAgentService;
import com.lnjoying.justice.ecrm.service.CombRpcService;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import com.lnjoying.justice.schema.entity.edgeif.EdgeGwIfState;
import com.lnjoying.justice.schema.entity.scheduler.BindResourcesResult;
import com.lnjoying.justice.schema.entity.scheduler.ClusterBindNode;
import com.lnjoying.justice.schema.entity.scheduler.SchedulerResult;
import com.lnjoying.justice.schema.entity.servicemanager.NodeInfo;
import com.micro.core.common.Pair;
import com.lnjoying.justice.ecrm.facade.EdgeServiceFacade;
import com.lnjoying.justice.ecrm.facade.GwServiceFacade;
import com.lnjoying.justice.ecrm.facade.RegionServiceFacade;
import com.lnjoying.justice.ecrm.process.service.EcrmMsgProcessStrategy;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.CombRetPbPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.constant.OnlineStatus;
import com.lnjoying.justice.schema.msg.*;
import com.lnjoying.justice.schema.service.ecrm.EdgeResourceService;
//import com.micro.core.persistence.redis.RedisClientUtils;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RpcSchema(schemaId = "edgeResourceService")
public class EdgeResourceServiceImpl implements EdgeResourceService
{
	private static Logger LOGGER = LogManager.getLogger();
	@Autowired
	EdgeServiceFacade edgeServiceFacade;

	@Autowired
	GwServiceFacade gwServiceFacade;

	@Autowired
	RegionServiceFacade regionServiceFacade;

	@Autowired
	EcrmMsgProcessStrategy ecrmMsgProcessStrategy;

	@Autowired
    CombRpcService combRpcService;

	@Autowired
	LabelProperty labelProperty;

	@Autowired
	GwRepository gwRepository;

	@Autowired
	private ServiceAgentService serviceAgentService;

	@Autowired
	private ServiceAgentRepository serviceAgentRepository;

	@Autowired
	private EdgeRepository edgeRepository;

	@Autowired
	private SiteRepository siteRepository;

	Gson gson =  new Gson();

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
				case MessageName.EHOST_OPERATOR:
					processEhostOperator(netMessage);
					break;
				case MessageName.GHOST_OPERATOR:
					processGhostOperator(netMessage);
					break;
				case MessageName.GET_EDGE_REQ:
					processGetEdgeReq(netMessage,edgeMessage.getNodeId());
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

	void processEhostOperator(EeNetMessageApi.ee_net_message netMessage)
	{
		EeHostDef.msg_ehost_operator_body msgEhostOperatorBody = netMessage.getEhostOperatorBody();
		EeHostDef.host_status_desc hostStatusDesc = null;
		int statusCode = 0;
		switch (msgEhostOperatorBody.getOperatorType())
		{
			case HostOperatorType.HOST_ACTIVE_RSP:
			{
				EeHostDef.msg_host_active_rsp_body rspBody     = msgEhostOperatorBody.getHostActiveRspBody();
				statusCode = rspBody.getErrorCode();
				hostStatusDesc =  rspBody.getStatusInfo();
				break;
			}

			case HostOperatorType.HOST_DEACTIVE_RSP:
			{
				EeHostDef.msg_host_deactive_rsp_body rspBody = msgEhostOperatorBody.getHostDeactiveRspBody();
				statusCode = rspBody.getErrorCode();
				hostStatusDesc =  rspBody.getStatusInfo();
				break;
			}

			case HostOperatorType.HOST_REMOVE_RSP:
			{
				EeHostDef.msg_host_remove_rsp_body rspBody     = msgEhostOperatorBody.getHostRemoveRspBody();
				statusCode = rspBody.getErrorCode();
				hostStatusDesc =  rspBody.getStatusInfo();
				break;
			}

			case HostOperatorType.HOST_REBOOT_RSP:
			{
				EeHostDef.msg_host_reboot_rsp_body   rspBody   = msgEhostOperatorBody.getHostRebootRspBody();
				statusCode = rspBody.getErrorCode();
				hostStatusDesc =  rspBody.getStatusInfo();
				break;
			}

			case HostOperatorType.HOST_CONFIG_RSP:
			{
				EeHostDef.msg_host_config_rsp_body   rspBody   = msgEhostOperatorBody.getHostConfigRspBody();
				statusCode = rspBody.getErrorCode();
				edgeServiceFacade.deleteHostConfigSessionCache(netMessage.getHead().getSessionId());
				break;
			}

			case HostOperatorType.HOST_EVACUATE_RSP:
			{
				EeHostDef.msg_host_evacuate_rsp_body rspBody = msgEhostOperatorBody.getHostEvacuateRspBody();
				Object object = RedisUtil.oget(RedisCache.EHOST_OPERATOR_SESSION, netMessage.getHead().getSessionId());
				if (object == null)
				{
					return;
				}

				EeCommonDef.msg_route route = netMessage.getRoute();
				statusCode = rspBody.getErrorCode();
				if (statusCode == ErrorCode.SUCCESS.getCode())
				{
					combRpcService.getContainerInstService().evacuateEdge(route.getONodeId());
					combRpcService.getAosService().evacuateEdge(route.getONodeId());
				}
				break;
			}

			case HostOperatorType.HOST_RPT_REQ:
			{
				EeHostDef.msg_host_rpt_req_body req_body = msgEhostOperatorBody.getHostRptReqBody();
				hostStatusDesc = req_body.getStatusInfo();
				List<EeHostDef.host_image_desc> imageInfoList  = req_body.getImageInfoList();
				List<EeHostDef.host_port_use_desc> portUseList = req_body.getPortInfoList();
				edgeServiceFacade.processHostImage(hostStatusDesc.getNodeId(), imageInfoList);
				edgeServiceFacade.processHostPort(hostStatusDesc.getNodeId(), portUseList);
			}
		}

		if (statusCode != ErrorCode.SUCCESS.getCode())
		{
			LOGGER.debug("{} edge operator failed. ErrorCode: {}", msgEhostOperatorBody.getOperatorType(), statusCode);
		}

		if (hostStatusDesc != null)
		{
			edgeServiceFacade.setEdgeStatus(hostStatusDesc);
		}
	}

	void processGhostOperator(EeNetMessageApi.ee_net_message netMessage)
	{
		EeHostDef.msg_ghost_operator_body msgGhostOperatorBody = netMessage.getGhostOperatorBody();
		EeHostDef.host_status_desc hostStatusDesc = null;
		int statusCode = 0;
		switch (msgGhostOperatorBody.getOperatorType())
		{
			case HostOperatorType.HOST_ACTIVE_RSP:
			{
				EeHostDef.msg_host_active_rsp_body rspBody     = msgGhostOperatorBody.getHostActiveRspBody();
				statusCode = rspBody.getErrorCode();
				hostStatusDesc =  rspBody.getStatusInfo();
				break;
			}

			case HostOperatorType.HOST_DEACTIVE_RSP:
			{
				EeHostDef.msg_host_deactive_rsp_body rspBody = msgGhostOperatorBody.getHostDeactiveRspBody();
				statusCode = rspBody.getErrorCode();
				hostStatusDesc =  rspBody.getStatusInfo();
				break;
			}

			case HostOperatorType.HOST_REMOVE_RSP:
			{
				EeHostDef.msg_host_remove_rsp_body rspBody     = msgGhostOperatorBody.getHostRemoveRspBody();
				statusCode = rspBody.getErrorCode();
				hostStatusDesc =  rspBody.getStatusInfo();
				break;
			}

			case HostOperatorType.HOST_REBOOT_RSP:
			{
				EeHostDef.msg_host_reboot_rsp_body rspBody     = msgGhostOperatorBody.getHostRebootRspBody();
				statusCode = rspBody.getErrorCode();
				hostStatusDesc =  rspBody.getStatusInfo();
				break;
			}
		}

		if (statusCode != ErrorCode.SUCCESS.getCode())
		{
			LOGGER.debug("{} edge operator failed. ErrorCode: {}", msgGhostOperatorBody.getOperatorType(), statusCode);
		}

		if (hostStatusDesc != null)
		{
			gwServiceFacade.setGWStatus(hostStatusDesc);
		}
	}

	void processCloudCtrl(EeNetMessageApi.ee_net_message netMessage, String nodeId)
	{
		LOGGER.info("process cloud ctrl");
		EeCtrlMessageDef.msg_cloud_ctrl_body msgCloudCtrlBody = netMessage.getCloudCtrlBody();
		switch (msgCloudCtrlBody.getCtrlType())
		{
			case CloudCtrlType.GET_GW_LIST_REQ:
			{
				LOGGER.info("send msg to ecrm processor");
				MessagePack messagePack = new MessagePack();
				messagePack.setMsgType(EcrmMsgType.GET_GW_LIST);
				messagePack.setMessageObj(new Pair(nodeId, netMessage));
				ecrmMsgProcessStrategy.sendMessage(messagePack);
				break;
			}

			case CloudCtrlType.SYNC_EDGE_IF_STATE_REQ:
			{
				MessagePack messagePack = new MessagePack();
				messagePack.setMsgType(EcrmMsgType.SYNC_EDGE_IF_STATE);
				messagePack.setMessageObj(new Pair(nodeId, netMessage));
				ecrmMsgProcessStrategy.sendMessage(messagePack);
				break;
			}

			case CtrlType.SYNC_WORKER_IF_STATE_REQ:
			{
				MessagePack messagePack = new MessagePack();
				messagePack.setMsgType(EcrmMsgType.SYNC_WORKER_IF_STATE);
				messagePack.setMessageObj(new Pair(nodeId, netMessage));
				ecrmMsgProcessStrategy.sendMessage(messagePack);
				break;
			}
		}
	}

	@Override
	public Pair<Integer, Integer> edgeReg(@ApiParam(name = "message")EdgeMessage edgeMessage)
	{
		try
		{
			EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.parseFrom(edgeMessage.getNetMessage());
			EeCommonDef.msg_edge_reg_req_body edgeRegReqBody = netMessage.getEdgeRegReqBody();

			return edgeServiceFacade.addNode(edgeRegReqBody);
		}
		catch (InvalidProtocolBufferException e)
		{
			e.printStackTrace();
			return new Pair<>(ErrorCode.SystemError.getCode(), ActiveStatus.REMOVED);
		}
	}

	@Override
	public int gwReg(@ApiParam(name = "message") EdgeMessage edgeMessage, @ApiParam(name = "remote") String remoteIp)
	{
		try
		{
			EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.parseFrom(edgeMessage.getNetMessage());
			EeCommonDef.msg_gw_reg_req_body gwRegReqBody = netMessage.getGwRegReqBody();

			return gwServiceFacade.addNode(gwRegReqBody);
		}
		catch (InvalidProtocolBufferException e)
		{
			e.printStackTrace();
			return ErrorCode.SystemError.getCode();
		}
	}

	@Override
	public CombRetPbPacket gwLogin(@ApiParam(name = "message")EdgeMessage edgeMessage, @ApiParam(name = "remote")String remoteIp)
	{
		CombRetPbPacket combRetPbPacket = new CombRetPbPacket();

		try
		{
			EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.parseFrom(edgeMessage.getNetMessage());
			EeCommonDef.msg_gw_login_cloud_req_body gwLoginReqBody = netMessage.getGwLoginCloudReqBody();

			int ret =  gwServiceFacade.login(gwLoginReqBody);
			combRetPbPacket.setStatus(ret);
			if (ret == ErrorCode.SUCCESS.getCode())
			{
				List<String> regionIds = regionServiceFacade.getRegionIdsByGwId(gwLoginReqBody.getNodeId());
				EeCommonDef.msg_gw_login_cloud_rsp_body gwLoginCloudRspBody = EeCommonDef.msg_gw_login_cloud_rsp_body.newBuilder().setStatus(ret).addAllRegionIds(regionIds).build();
				combRetPbPacket.setObj(gwLoginCloudRspBody.toByteArray());
			}
		}
		catch (InvalidProtocolBufferException e)
		{
			e.printStackTrace();
			combRetPbPacket.setStatus(ErrorCode.SystemError.getCode());
		}

		return combRetPbPacket;
	}

	@Override
	public int gwDisConnect(@ApiParam(name = "gwNodeId")String nodeId)
	{
		try
		{
			gwServiceFacade.setNodeStatus(nodeId, OnlineStatus.OFFLINE);
		}
		catch (WebSystemException e)
		{
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public int edgeDisConnect(@ApiParam(name = "edgeNodeId")String nodeId) {
		return 0;
	}

	@Override
	public CombRetPacket edgeRemoteLogin(@ApiParam(name = "message") EdgeMessage edgeMessage)
	{
		CombRetPacket combRetPacket = new CombRetPacket();

		try
		{
			EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.parseFrom(edgeMessage.getNetMessage());
			EeCommonDef.msg_edge_login_gw_req_body edgeLoginGwReqBody = netMessage.getEdgeLoginGwReqBody();
			Pair<Integer, EeCommonDef.edge_reg_info> ret = edgeServiceFacade.edgeLogin(edgeLoginGwReqBody);

			EeCommonDef.edge_reg_info edgeRegInfo = ret.getValue();
			if (ret.getKey().equals(ErrorCode.SUCCESS.getCode()) && edgeRegInfo != null)
			{
				byte [] src = edgeRegInfo.toByteArray();
				String extern = Base64.getEncoder().encodeToString(src);
				combRetPacket.setObj(extern);
			}
			combRetPacket.setStatus(ret.getKey());
		}
		catch (InvalidProtocolBufferException e)
		{
			e.printStackTrace();
			combRetPacket.setStatus(ErrorCode.SystemError.getCode());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return combRetPacket;
	}

	void processGetEdgeReq(EeNetMessageApi.ee_net_message netMessage, String nodeId)
	{
		LOGGER.info("send msg to ecrm processor");
		MessagePack messagePack = new MessagePack();
		messagePack.setMsgType(EcrmMsgType.GET_EDGE_REQ);
		messagePack.setMessageObj(new Pair<>(nodeId, netMessage));
		ecrmMsgProcessStrategy.sendMessage(messagePack);
	}

	@Override
	public List<ClusterBindNode> fillClusterBindNodeField(@ApiParam(name = "clusterBindNodes") List<ClusterBindNode> clusterBindNodes)
	{
		if (!CollectionUtils.isEmpty(clusterBindNodes))
		{
			for (ClusterBindNode clusterBindNode : clusterBindNodes)
			{
				EdgeNodeInfo edgeNodeInfo = edgeServiceFacade.getNodeById(clusterBindNode.getNodeId());
				if (edgeNodeInfo != null)
				{
					clusterBindNode.setNodeName(edgeNodeInfo.getNode_name());
					clusterBindNode.setInternalAddress(edgeNodeInfo.getLabels().containsKey(labelProperty.getInternalAddress())?edgeNodeInfo.getLabels().get(labelProperty.getInternalAddress()):getNodeInternalIP(edgeNodeInfo.getDev_info().getNetworkInfo()));
					clusterBindNode.setExternalAddress(edgeNodeInfo.getLabels().containsKey(labelProperty.getExternalAddress())?edgeNodeInfo.getLabels().get(labelProperty.getExternalAddress()):getNodeExternalIP(edgeNodeInfo.getDev_info().getNetworkInfo()));
					clusterBindNode.setLabels(edgeNodeInfo.getLabels());
					clusterBindNode.setTaints(edgeNodeInfo.getTaints());
					clusterBindNode.setSoftWareInfos(edgeNodeInfo.getDev_info().getSoftwareMap());
					clusterBindNode.setAnnotations(null);
				}
			}
		}
		return clusterBindNodes;
	}

	private String getNodeInternalIP(List<NetworkInfo> networkInfos)
	{
		if (!CollectionUtils.isEmpty(networkInfos))
		{
			for (NetworkInfo networkInfo : networkInfos)
			{
				if (StringUtils.isNotEmpty(networkInfo.getIpv4()))
				{
					String ip = networkInfo.getIpv4().split("/")[0];
					if (innerIP(ip)) return ip;
				}
			}
		}
		return null;
	}

	private String getNodeExternalIP(List<NetworkInfo> networkInfos)
	{
		if (!CollectionUtils.isEmpty(networkInfos))
		{
			for (NetworkInfo networkInfo : networkInfos)
			{
				if (StringUtils.isNotEmpty(networkInfo.getIpv4()))
				{
					String ip = networkInfo.getIpv4().split("/")[0];
					if (!innerIP(ip)) return ip;
				}
			}
		}
		return null;
	}

	private boolean innerIP(String ip) {

		Pattern reg = Pattern.compile("^(127\\.0\\.0\\.1)|(localhost)|(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})|(192\\.168\\.\\d{1,3}\\.\\d{1,3})$");
		Matcher match = reg.matcher(ip);

		return match.find();
	}

	@Override
	public String getOnlineGwByRegion(@ApiParam(name = "regionId") String regionId)
	{
		if (! StringUtils.isEmpty(regionId))
		{
			List<TblEdgeGwInfo> tblEdgeGwInfos = gwRepository.getGWsByRegionId(regionId);
			if (tblEdgeGwInfos != null && !tblEdgeGwInfos.isEmpty())
			{
				List<EdgeGwIfState> nodes = new ArrayList<>();
				for (TblEdgeGwInfo tblEdgeGwInfo : tblEdgeGwInfos)
				{
					Object object = RedisUtil.oget(RedisCache.GW_IF, tblEdgeGwInfo.getNodeId());
					if (object == null)
					{
						continue;
					}

					nodes.add((EdgeGwIfState)object);
				}

				if (! nodes.isEmpty())
				{
					EdgeGwIfState edgeGwIfState = nodes.get(Utils.getRandomByRange(0, nodes.size()));
					return edgeGwIfState.getAdvertise();
				}
			}
		}
		return null;
	}

	@Override
	public Pair<Integer, Integer> getNodeStatus(@ApiParam(name = "nodeId") String nodeId){
		EdgeNodeInfo nodeInfo = edgeServiceFacade.getNodeById(nodeId);
		if (nodeInfo == null)
		{
			return null;
		}
		else
		{
			return new Pair<Integer, Integer>(nodeInfo.getActive_status(), nodeInfo.getOnline_status());
		}
	}

	@Override
	public BindResourcesResult bindResources(@ApiParam(name = "schedulerResult") SchedulerResult schedulerResult)
	{
		return serviceAgentService.bindResources(schedulerResult);
	}

	@Override
	public void updateServiceAgentStatus(@ApiParam(name = "agentId") String agentId, @ApiParam(name = "status") Integer status)
	{
		try
		{
			TblServiceAgentInfo tblServiceAgentInfo = serviceAgentRepository.getServiceAgent(agentId);
			if (tblServiceAgentInfo == null || tblServiceAgentInfo.getStatus() == InstanceState.FIN_CLOUD_REMOVE.getCode())
			{
				LOGGER.info("worker {} is null or removed", agentId);
				return;
			}

			if (status == InstanceState.HARDWARE_ERROR.getCode() || status == InstanceState.NO_IMAGE.getCode() ||
					status == InstanceState.IMAGE_DOWNLOAD_FAILED.getCode() || status == InstanceState.CREATE_FAILED.getCode())
			{
				tblServiceAgentInfo.setStatus(status);
				combRpcService.getSchedulerService().releaseBindResources(tblServiceAgentInfo.getNodeId(), agentId);
			}
			else if (status == InstanceState.REMOVED.getCode() || status == InstanceState.OBJECT_AUTO_REMOVE.getCode())
			{
				tblServiceAgentInfo.setStatus(InstanceState.FIN_CLOUD_REMOVE.getCode());
				//relesae resources(remove gpu bind and update edge monopoly status: remove)
				combRpcService.getSchedulerService().releaseBindResources(tblServiceAgentInfo.getNodeId(), agentId);
			}
			else
			{
				tblServiceAgentInfo.setStatus(status);
			}

			tblServiceAgentInfo.setUpdateTime(new Date());

			serviceAgentRepository.updateServiceAgentSelective(tblServiceAgentInfo);
		}
		catch (Exception e)
		{
			LOGGER.error("Error while update service agent state, message {}", e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public CombRetPacket getServiceAgent(@ApiParam(name = "siteId")String siteId)
	{
		CombRetPacket combRetPacket = new CombRetPacket();
		try
		{
			TblSiteInfo tblSiteInfo = siteRepository.getSite(siteId);
			if (tblSiteInfo == null || tblSiteInfo.getStatus() == ActiveStatus.REMOVED)
			{
				combRetPacket.setStatus(ErrorCode.SITE_NOT_EXIST.getCode());
				return combRetPacket;
			}

			TblServiceAgentInfoExample example = new TblServiceAgentInfoExample();
			TblServiceAgentInfoExample.Criteria criteria = example.createCriteria();
			criteria.andSiteIdEqualTo(siteId);
			criteria.andStatusNotIn(InstanceState.removeStatusList);
			example.setOrderByClause("online_status desc");
			List<TblServiceAgentInfo> tblServiceAgentInfos = serviceAgentRepository.getServiceAgentsByExample(example);

			for (TblServiceAgentInfo tblServiceAgentInfo : tblServiceAgentInfos)
			{
				if (tblServiceAgentInfo.getOnlineStatus() == OnlineStatus.ONLINE)
				{
					String workId = tblServiceAgentInfo.getWorkerId();
					combRetPacket.setStatus(ErrorCode.SUCCESS.getCode());
					combRetPacket.setObj(workId);
					return combRetPacket;
				}
				else if ((tblServiceAgentInfo.getStatus() >= InstanceState.MAKED.getCode() && tblServiceAgentInfo.getStatus() <= InstanceState.FWD.getCode())
						|| (tblServiceAgentInfo.getStatus() >= InstanceState.QUEUEING.getCode() && tblServiceAgentInfo.getStatus() <= InstanceState.RESTARTING.getCode()))
				{
					combRetPacket.setStatus(ErrorCode.SUCCESS.getCode());
					return combRetPacket;
				}
			}



			AddServiceAgentReq addServiceAgentReq = new AddServiceAgentReq();
			addServiceAgentReq.setTargetNodes(new ArrayList<>());
			addServiceAgentReq.setDescription("service-agent(auto deploy)");
			addServiceAgentReq.getTargetNodes().add(new TargetNode(tblSiteInfo.getRegionId(), tblSiteInfo.getSiteId(), null));
			serviceAgentService.addServiceAgent(addServiceAgentReq);
			combRetPacket.setStatus(ErrorCode.SUCCESS.getCode());
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
	public NodeInfo getNode(@ApiParam(name = "nodeId")String nodeId)
	{
		TblEdgeComputeInfo tblEdgeComputeInfo =  edgeRepository.getEdgeNode(nodeId);
		if (tblEdgeComputeInfo == null)
		{
			return null;
		}
		NodeInfo nodeInfo = new NodeInfo();
		nodeInfo.setRegionId(tblEdgeComputeInfo.getRegionId());
		nodeInfo.setSiteId(tblEdgeComputeInfo.getSiteId());
		nodeInfo.setNodeId(tblEdgeComputeInfo.getNodeId());
		List<NetworkInfo> networkInfos = gson.fromJson(tblEdgeComputeInfo.getNetwork(), new com.google.gson.reflect.TypeToken<List<NetworkInfo>>(){}.getType());
		if (networkInfos == null)
		{
			return nodeInfo;
		}
		String ip = getNodeInternalIP(networkInfos);
		if (StringUtils.isEmpty(ip)) ip = getNodeExternalIP(networkInfos);
		nodeInfo.setIp(ip);
		return nodeInfo;
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

			return serviceAgentService.addWorker(workerRegReqBody);
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
			return serviceAgentService.workerLogin(workerLoginReqBody);
		}
		catch (InvalidProtocolBufferException e)
		{
			e.printStackTrace();
			return ErrorCode.SystemError.getCode();
		}
	}
}
