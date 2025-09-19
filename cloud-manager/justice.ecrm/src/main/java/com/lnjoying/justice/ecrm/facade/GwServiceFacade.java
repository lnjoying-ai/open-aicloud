package com.lnjoying.justice.ecrm.facade;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.ecrm.common.GoogleCodeTool;
import com.lnjoying.justice.ecrm.common.constant.*;
import com.lnjoying.justice.ecrm.config.EcrmConfig;
import com.lnjoying.justice.ecrm.config.model.GwNodeConfig;
import com.lnjoying.justice.ecrm.db.model.TblEdgeGwInfo;
import com.lnjoying.justice.ecrm.db.model.TblRegionBindInfo;
import com.lnjoying.justice.ecrm.db.repo.GwRepository;
import com.lnjoying.justice.ecrm.db.repo.RegionGwRepository;
import com.lnjoying.justice.ecrm.db.repo.RegionRepository;
import com.lnjoying.justice.ecrm.domain.MemInfo;
import com.lnjoying.justice.ecrm.domain.dto.model.*;
import com.lnjoying.justice.ecrm.domain.dto.request.GwInputReq;
import com.lnjoying.justice.ecrm.domain.dto.response.QueryGwRsp;
import com.lnjoying.justice.schema.common.LJ_Function;
import com.micro.core.common.Pair;
import com.lnjoying.justice.ecrm.domain.model.search.GwSearchCritical;
import com.lnjoying.justice.ecrm.process.service.EcrmMsgProcessStrategy;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.lnjoying.justice.schema.constant.OnlineStatus;
import com.lnjoying.justice.schema.msg.*;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GwServiceFacade
{
	private static Logger LOGGER = LogManager.getLogger();

	@Autowired
	GwRepository gwRepository;

	@Autowired
	EcrmMsgProcessStrategy ecrmMsgProcessStrategy;

	@Autowired
	NetMessageServiceFacade netMessageServiceFacade;

	@Autowired
	RegionRepository regionRepository;

	@Autowired
	RegionGwRepository regionGwRepository;

	@Autowired
	private EcrmConfig ecrmConfig;

	public Map<String, String> genAddCmd(GwInputReq gwInputReq)
	{
		//String installFileName = RedisUtil.get(RedisCache.INSTALL_FILE);
		String installFileName = ecrmConfig.getInstallScriptUrl();
		if (null == installFileName)
		{
			LOGGER.error("install file not set in nacos");
			throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
		}
		GwNodeConfig gwNodeConfig = getGwNodeConfig();
		if (gwNodeConfig == null)
		{
			LOGGER.error("gw template not set in nacos or incomplete");
			throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
		}
//		String cmd = "sudo docker run --rm --privileged";
		String cmd = "bash <(curl -s -S -L " + installFileName + ") ";

		if (gwInputReq == null)
		{
			return null;
		}

		{
			//process port bind

			List<String> portBinds = new ArrayList<>();

//			if (edgeInputReq.getRest_bind() != null)
//			{
//				String restbind = "";
//				if (edgeInputReq.getRest_port() > 0)
//				{
//					restbind = edgeInputReq.getRest_bind() + ":" + edgeInputReq.getRest_port();
////				cmd = cmd + " -p " + edgeInputReq.getRest_bind() + ":" + edgeInputReq.getRest_port();
//				}
//				else if (nodeConfigBean.getAgent().getRest_port() > 0)
//				{
//					restbind = edgeInputReq.getRest_bind() + ":" + nodeConfigBean.getAgent().getRest_port();
////				cmd = cmd + " -p " + edgeInputReq.getRest_bind() + ":" + nodeConfigBean.getAgent().getRest_port();
//				}
//				portBinds.add(restbind);
//			}

			if (! StringUtils.isEmpty(gwInputReq.getRest_bind()))
			{
				String ipBind = "";
				if (gwInputReq.getRest_port() > 0)
				{
					ipBind = gwInputReq.getRest_bind() + ":" + gwInputReq.getRest_port();
//					cmd = cmd + " -p " + gwInputReq.getRest_bind() + ":" + gwInputReq.getRest_port();
				}
				else if (gwNodeConfig.getRestPort() > 0)
				{
					ipBind = gwInputReq.getRest_bind() + ":" + gwNodeConfig.getRestPort();
//					cmd = cmd + " -p " + gwInputReq.getRest_bind() + ":" + gwNodeConfig.getRest_port();
				}
				portBinds.add(ipBind);
			}

			if (! StringUtils.isEmpty(gwInputReq.getService_bind()))
			{
				String ipBind = "";
				if (gwInputReq.getLocal_port() > 0)
				{
					ipBind = gwInputReq.getService_bind() + ":" + gwInputReq.getLocal_port();
//					cmd = cmd + " -p " + gwInputReq.getService_bind() + ":" + gwInputReq.getLocal_port();
				}
				else if (gwNodeConfig.getLocalPort() > 0)
				{
					ipBind = gwInputReq.getService_bind() + ":" + gwNodeConfig.getLocalPort();
//					cmd = cmd + " -p " + gwInputReq.getService_bind() + ":" + gwNodeConfig.getLocal_port();
				}
				portBinds.add(ipBind);
			}

			if (! portBinds.isEmpty())
			{
				String portbindStr = "port_bind=" + StringUtils.join(portBinds, ",");
				cmd = cmd + " -e "  + portbindStr;
			}
		}




		cmd = cmd + " " + gwNodeConfig.getImageName();


		if (! StringUtils.isEmpty(gwInputReq.getAdvertise()))
		{
			cmd = cmd + " --advertise " + gwInputReq.getAdvertise();
		}

		if (! StringUtils.isEmpty(gwInputReq.getNode_name()))
		{
			cmd = cmd + " --node_name " + gwInputReq.getNode_name();
		}

		int rest_port = gwInputReq.getRest_port() > 0 ? gwInputReq.getRest_port():gwNodeConfig.getRestPort();
		if (rest_port > 0)
		{
			cmd = cmd + " --rest " + "0.0.0.0:" + rest_port;
		}

		int local_port = gwInputReq.getLocal_port() > 0 ? gwInputReq.getLocal_port() : gwNodeConfig.getLocalPort();
		if (local_port > 0)
		{
			cmd = cmd + " --local_endpoint " +  "0.0.0.0:" + local_port;
		}

		if (gwInputReq.getLabels() != null && ! gwInputReq.getLabels().isEmpty())
		{
			List<String> labelList = new ArrayList<>();
			for(Map.Entry<String, String> entry : gwInputReq.getLabels().entrySet())
			{
				String label = entry.getKey() + "=" + entry.getValue();
				labelList.add(label);
			}

			String labels = StringUtils.join(labelList, "&");
			cmd = cmd +  " --labels " + labels;
		}



		if (gwInputReq.getRegion_ids() != null)
		{
			for (String region : gwInputReq.getRegion_ids())
			{
				cmd = cmd + " --region " + region;
			}
		}

		if (gwNodeConfig.getCloud() == null || gwNodeConfig.getCloud().isEmpty())
		{
			LOGGER.error("need clod configuration in gw template");
			throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
		}
		else
		{
			cmd = cmd + " --cloud " + gwNodeConfig.getCloud();
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
		Map<String,String> ret = new HashMap<>();
		ret.put("cmd", cmd);
		return ret;

	}

	public int addNode(EeCommonDef.msg_gw_reg_req_body gwRegReqBody)
	{
		DevInfo devInfo = null;
		if (! StringUtils.isEmpty(gwRegReqBody.getDevInfo()))
		{
			devInfo = JsonUtils.fromJson(gwRegReqBody.getDevInfo(), DevInfo.class);
		}

		LJ_Function<TblEdgeGwInfo> lj_function = null;
		AtomicInteger ret = new AtomicInteger();
		TblEdgeGwInfo tblEdgeGwInfo = gwRepository.getGwByNodeId(gwRegReqBody.getNodeId());
		if (tblEdgeGwInfo != null)
		{
			if ( StringUtils.isEmpty(tblEdgeGwInfo.getUuid())
					|| devInfo == null
					|| devInfo.getMachineInfo() == null
					|| ! StringUtils.equals(devInfo.getMachineInfo().getUuid(), tblEdgeGwInfo.getUuid())
					||  tblEdgeGwInfo.getActiveStatus() == ActiveStatus.REMOVED)
			{
				return ErrorCode.DEV_NODEID_OCCUPIED.getCode();
			}

			lj_function = tbl -> {
				ret.set(gwRepository.updateGw(tbl));
			};
		}
		else
		{
			String regToken = gwRegReqBody.getRegToken();

			if (! StringUtils.isEmpty(regToken))
			{
				if (! StringUtils.isNumeric(regToken))
				{
					return ErrorCode.DEV_REG_TOKEN_INVALID.getCode();
				}

				if (! GoogleCodeTool.check_code(Long.parseLong(regToken), System.currentTimeMillis()))
				{
					return ErrorCode.DEV_REG_TOKEN_INVALID.getCode();
				}
			}

			tblEdgeGwInfo = new TblEdgeGwInfo();
			lj_function = tbl -> {
				ret.set(gwRepository.insertGw(tbl));
			};
		}

		tblEdgeGwInfo.setNodeId(gwRegReqBody.getNodeId());
		tblEdgeGwInfo.setNodeName(gwRegReqBody.getNodeName());
		if (! StringUtils.isEmpty(gwRegReqBody.getRegToken()))
		{
			tblEdgeGwInfo.setActiveStatus(ActiveStatus.ACITVE);
		}
		else
		{
			tblEdgeGwInfo.setActiveStatus(ActiveStatus.INACTIVE);
		}

		tblEdgeGwInfo.setOnlineStatus(OnlineStatus.OFFLINE);

		if (devInfo != null)
		{
			List<NetworkInfo> networkInfo = devInfo.getNetworkInfo();
			if (networkInfo != null)
			{
				tblEdgeGwInfo.setNetwork(JsonUtils.toJson(networkInfo));
			}

			CpuInfo cpuInfo = devInfo.getCpuInfo();
			if (cpuInfo != null)
			{
				tblEdgeGwInfo.setCpuFrequency(cpuInfo.getCpuFrequency());
				tblEdgeGwInfo.setCpuLogicNum(cpuInfo.getCpuLogicNum());
				tblEdgeGwInfo.setCpuModel(cpuInfo.getCpuModel());
				tblEdgeGwInfo.setCpuPhysicalNum(cpuInfo.getCpuPhysicalNum());
			}

			MemInfo memInfo =  devInfo.getMemInfo();
			if (memInfo != null)
			{
				tblEdgeGwInfo.setMemTotal(memInfo.getMemTotal());
			}

			DiskInfo diskInfo = devInfo.getDiskInfo();
			if (diskInfo != null)
			{
				tblEdgeGwInfo.setDiskTotal(diskInfo.getDiskTotal());
				tblEdgeGwInfo.setDiskType(diskInfo.getDiskTypes());
				tblEdgeGwInfo.setDiskDetail(JsonUtils.toJson(diskInfo.getDisks()));
			}

			MachineInfo machineInfo = devInfo.getMachineInfo();
			if (machineInfo != null)
			{
				tblEdgeGwInfo.setVender(machineInfo.getVender());
				tblEdgeGwInfo.setProduct(machineInfo.getProduct());
				tblEdgeGwInfo.setUuid(machineInfo.getUuid());
				tblEdgeGwInfo.setSn(machineInfo.getSn());
				tblEdgeGwInfo.setHostName(machineInfo.getHostname());
			}

			if (devInfo.getSoftwareInfo()  != null)
			{
				tblEdgeGwInfo.setSoftwareVersion(JsonUtils.toJson(devInfo.getSoftwareInfo()));
			}
		}

		tblEdgeGwInfo.setCreateTime(new Date());
		tblEdgeGwInfo.setUpdateTime(tblEdgeGwInfo.getCreateTime());
		lj_function.operator(tblEdgeGwInfo);

		if (gwRegReqBody.getRegionIdsList() != null)
		{
			for (String regionId : gwRegReqBody.getRegionIdsList())
			{
				try
				{
					TblRegionBindInfo tblRegionBindInfo = new TblRegionBindInfo();
					tblRegionBindInfo.setNodeId(gwRegReqBody.getNodeId());
					tblRegionBindInfo.setRegionId(regionId);
					tblRegionBindInfo.setCreateTime(Utils.buildDate(System.currentTimeMillis()));
					tblRegionBindInfo.setUpdateTime(tblRegionBindInfo.getCreateTime());
					regionGwRepository.insertRegionBind(tblRegionBindInfo);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					continue;
				}
			}
		}

		if (ret.get() > 0)
		{
			MessagePack messagePack = new MessagePack();
			messagePack.setMsgType(EcrmMsgType.REG_GW);
			messagePack.setMessageObj(gwRegReqBody);
			ecrmMsgProcessStrategy.sendMessage(messagePack);
		}

		return ErrorCode.SUCCESS.getCode();
	}

	public int login(EeCommonDef.msg_gw_login_cloud_req_body gwLoginReqBody)
	{
		TblEdgeGwInfo tblEdgeGwInfo = gwRepository.getGwByNodeId(gwLoginReqBody.getNodeId());
		if (tblEdgeGwInfo == null)
		{
			return ErrorCode.DEV_NOT_EXIST.getCode();
		}

		if (tblEdgeGwInfo.getActiveStatus() == ActiveStatus.REMOVED)
		{
			return ErrorCode.DEV_DROPPED.getCode();
		}

		EeCommonDef.network_address networkAddress = gwLoginReqBody.getAddrMe();

		if (networkAddress != null)
		{
			if ((tblEdgeGwInfo.getHostPublicIp() == null) || ! StringUtils.equals(networkAddress.getIp(), tblEdgeGwInfo.getHostPublicIp()))
			{
				MessagePack messagePack = new MessagePack();
				messagePack.setMsgType(EcrmMsgType.LOGIN_GW);
				messagePack.setMessageObj(gwLoginReqBody);
				ecrmMsgProcessStrategy.sendMessage(messagePack);
			}

			tblEdgeGwInfo.setHostPublicIp(networkAddress.getIp());
			tblEdgeGwInfo.setHostPublicPort(networkAddress.getPort());
		}

		if (StringUtils.isEmpty(tblEdgeGwInfo.getCoreVersion())
				|| !gwLoginReqBody.getCoreVersion().equals(tblEdgeGwInfo.getCoreVersion()))
		{
			tblEdgeGwInfo.setCoreVersion(gwLoginReqBody.getCoreVersion());
		}

		tblEdgeGwInfo.setOnlineStatus(OnlineStatus.ONLINE);
		tblEdgeGwInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));

		gwRepository.updateGw(tblEdgeGwInfo);
		return ErrorCode.SUCCESS.getCode();
	}

	public void updateNode(String nodeId, GwInputReq gwInputReq)
	{
		return;
	}

	public QueryGwRsp getNodes(GwSearchCritical gwSearchCritical)
	{
		return gwRepository.getGws(gwSearchCritical);
	}

	public void deleteNode(String nodeId)
	{
		TblEdgeGwInfo tblEdgeGwInfo = gwRepository.getGwByNodeId(nodeId);
		if (tblEdgeGwInfo == null)
		{
			throw new WebSystemException(ErrorCode.DEV_NOT_EXIST, ErrorLevel.INFO);
		}

		if (tblEdgeGwInfo.getActiveStatus() == ActiveStatus.REMOVED)
		{
			throw new WebSystemException(ErrorCode.DEV_DROPPED, ErrorLevel.INFO);
		}

		gwRepository.deleteGW(nodeId);
		removeNode(tblEdgeGwInfo);
	}

	public void setNodeStatus(String nodeId, int status)
	{
		TblEdgeGwInfo tblEdgeGwInfo = gwRepository.getGwByNodeId(nodeId);
		if (tblEdgeGwInfo == null)
		{
			throw new WebSystemException(ErrorCode.DEV_NOT_EXIST, ErrorLevel.INFO);
		}

		if (tblEdgeGwInfo.getActiveStatus() == ActiveStatus.REMOVED)
		{
			throw new WebSystemException(ErrorCode.DEV_DROPPED, ErrorLevel.INFO);
		}

		tblEdgeGwInfo.setOnlineStatus(status);
		tblEdgeGwInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));

		gwRepository.updateGw(tblEdgeGwInfo);
	}

	public void ctrlNode(String nodeId, String action)
	{
		LOGGER.info("ctrl node {} action: {}", nodeId, action);
		TblEdgeGwInfo tblEdgeGwInfo = gwRepository.getGwByNodeId(nodeId);
		if (tblEdgeGwInfo == null)
		{
			throw new WebSystemException(ErrorCode.DEV_NOT_EXIST, ErrorLevel.INFO);
		}

		if (tblEdgeGwInfo.getActiveStatus() == ActiveStatus.REMOVED)
		{
			throw new WebSystemException(ErrorCode.DEV_DROPPED, ErrorLevel.INFO);
		}

		switch (action)
		{
			case ActionType.ACTIVE:
				activeNode(tblEdgeGwInfo);
				break;
			case ActionType.DEACTIVE:
				deactiveNode(tblEdgeGwInfo);
				break;
			case ActionType.REBOOT:
				sendMessage(tblEdgeGwInfo, EcrmMsgType.REBOOT_GW);
				break;
			case ActionType.REMOVE:
				sendMessage(tblEdgeGwInfo, EcrmMsgType.REMOVE_GW);
				break;
			default:
				throw new WebSystemException(ErrorCode.ACTION_NOT_SUPPORT, ErrorLevel.INFO);
		}
		return;
	}

	private void activeNode(TblEdgeGwInfo tblEdgeGwInfo)
	{
		if (tblEdgeGwInfo.getActiveStatus() == ActiveStatus.ACITVE)
		{
			LOGGER.info("{} has been actived", tblEdgeGwInfo.getNodeId());
			return;
		}

		tblEdgeGwInfo.setActiveStatus(ActiveStatus.ACITVE);
		tblEdgeGwInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));

		gwRepository.updateGw(tblEdgeGwInfo);

		sendMessage(tblEdgeGwInfo, EcrmMsgType.ACTIVE_GW);
	}

	private void deactiveNode(TblEdgeGwInfo tblEdgeGwInfo)
	{
		if (tblEdgeGwInfo.getActiveStatus() == ActiveStatus.INACTIVE)
		{
			LOGGER.info("{} has been deactived", tblEdgeGwInfo.getNodeId());
			return;
		}

		tblEdgeGwInfo.setActiveStatus(ActiveStatus.INACTIVE);
		tblEdgeGwInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));

		gwRepository.updateGw(tblEdgeGwInfo);

		sendMessage(tblEdgeGwInfo, EcrmMsgType.DEACTIVE_GW);
	}

	private void removeNode(TblEdgeGwInfo tblEdgeGwInfo)
	{
		if (tblEdgeGwInfo.getActiveStatus() == ActiveStatus.REMOVED)
		{
			LOGGER.debug("{} has been removed", tblEdgeGwInfo.getNodeId());
		}

		tblEdgeGwInfo.setActiveStatus(ActiveStatus.REMOVED);
		tblEdgeGwInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));

		gwRepository.updateGw(tblEdgeGwInfo);

		sendMessage(tblEdgeGwInfo, EcrmMsgType.REMOVE_GW);
	}

//	private void rebootNode(TblEdgeGwInfo tblEdgeGwInfo)
//	{
//		MessagePack msgPack = new MessagePack();
//		msgPack.setMsgType(EcrmMsgType.REBOOT_GW);
//		msgPack.setMessageObj(tblEdgeGwInfo);
//		ecrmMsgProcessStrategy.sendMessage(msgPack);
//	}

	private void sendMessage(TblEdgeGwInfo tblEdgeGwInfo, String oper)
	{
		MessagePack msgPack = new MessagePack();
		msgPack.setMsgType(oper);
		msgPack.setMessageObj(tblEdgeGwInfo);
		ecrmMsgProcessStrategy.sendMessage(msgPack);
	}


	EeHostDef.msg_ghost_operator_body.Builder makeGhostBuilder(String operType)
	{
		return  EeHostDef.msg_ghost_operator_body.newBuilder().setOperatorType(operType);
	}

	public void processActiveGw(TblEdgeGwInfo tblEdgeGwInfo)
	{
		LOGGER.info("prepare active {}", tblEdgeGwInfo.getNodeId());

		EeHostDef.msg_host_active_req_body.Builder hostOperyBody = EeHostDef.msg_host_active_req_body.newBuilder();
		hostOperyBody.setNodeId(tblEdgeGwInfo.getNodeId());
		EeHostDef.msg_ghost_operator_body.Builder msgGhostOperatorBodyBuilder = makeGhostBuilder(HostOperatorType.HOST_ACTIVE_REQ).setHostActiveReqBody(hostOperyBody);


		EeNetMessageApi.ee_net_message ee_net_message = makeGhostNetMsg(msgGhostOperatorBodyBuilder);
		netMessageServiceFacade.submitMessage(tblEdgeGwInfo.getNodeId(), ee_net_message);
	}



	public void processDeactiveGw(TblEdgeGwInfo tblEdgeGwInfo)
	{
		LOGGER.info("prepare deactive {}", tblEdgeGwInfo.getNodeId());
		EeHostDef.msg_host_deactive_req_body.Builder hostOperyBody = EeHostDef.msg_host_deactive_req_body.newBuilder();
		hostOperyBody.setNodeId(tblEdgeGwInfo.getNodeId());
		EeHostDef.msg_ghost_operator_body.Builder msgGhostOperatorBodyBuilder = makeGhostBuilder(HostOperatorType.HOST_DEACTIVE_REQ).setHostDeactiveReqBody(hostOperyBody);

		EeNetMessageApi.ee_net_message ee_net_message = makeGhostNetMsg(msgGhostOperatorBodyBuilder);
		netMessageServiceFacade.submitMessage(tblEdgeGwInfo.getNodeId(), ee_net_message);
	}

	public void processRebootGw(TblEdgeGwInfo tblEdgeGwInfo)
	{
		LOGGER.info("prepare reboot {}", tblEdgeGwInfo.getNodeId());

		EeHostDef.msg_host_reboot_req_body.Builder hostOperyBody = EeHostDef.msg_host_reboot_req_body.newBuilder();
		hostOperyBody.setNodeId(tblEdgeGwInfo.getNodeId());
		EeHostDef.msg_ghost_operator_body.Builder msgGhostOperatorBodyBuilder = makeGhostBuilder(HostOperatorType.HOST_REBOOT_REQ).setHostRebootReqBody(hostOperyBody);

		EeNetMessageApi.ee_net_message ee_net_message = makeGhostNetMsg(msgGhostOperatorBodyBuilder);
		netMessageServiceFacade.submitMessage(tblEdgeGwInfo.getNodeId(), ee_net_message);
	}

	public void processRemoveGw(TblEdgeGwInfo tblEdgeGwInfo)
	{
		LOGGER.info("prepare remove {}", tblEdgeGwInfo.getNodeId());

		EeHostDef.msg_host_remove_req_body.Builder hostOperyBody = EeHostDef.msg_host_remove_req_body.newBuilder();
		hostOperyBody.setNodeId(tblEdgeGwInfo.getNodeId());
		EeHostDef.msg_ghost_operator_body.Builder msgGhostOperatorBodyBuilder = makeGhostBuilder(HostOperatorType.HOST_REMOVE_REQ).setHostRemoveReqBody(hostOperyBody);

		EeNetMessageApi.ee_net_message ee_net_message = makeGhostNetMsg(msgGhostOperatorBodyBuilder);
		netMessageServiceFacade.submitMessage(tblEdgeGwInfo.getNodeId(), ee_net_message);
	}

	EeNetMessageApi.ee_net_message makeGhostNetMsg(EeHostDef.msg_ghost_operator_body.Builder msgGhostOperatorBodyBuilder )
	{
		return EeNetMessageApi.ee_net_message.newBuilder().setHead(netMessageServiceFacade.makeReqMsgHeader(MessageName.GHOST_OPERATOR)).setGhostOperatorBody(msgGhostOperatorBodyBuilder).build();
	}

	public void processRegGw(EeCommonDef.msg_gw_reg_req_body gwRegReqBody)
	{
		List<TblEdgeGwInfo> gwInfoList = gwRepository.getBrotherGws(gwRegReqBody.getNodeId());
		if (gwInfoList == null)
		{
			return;
		}

		EeCtrlMessageDef.msg_sync_gw_req.Builder gwBuilder = EeCtrlMessageDef.msg_sync_gw_req.newBuilder();
		EeCtrlMessageDef.gw_net_info.Builder gwNetInfo = EeCtrlMessageDef.gw_net_info.newBuilder().setNodeId(gwRegReqBody.getNodeId());

		if (gwRegReqBody.getRegionIdsList() != null && ! gwRegReqBody.getRegionIdsList().isEmpty())
		{
			gwNetInfo.addAllRegionIds(gwRegReqBody.getRegionIdsList());
		}

		gwBuilder.setGwInfo(gwNetInfo);

		for (TblEdgeGwInfo tblEdgeGwInfo : gwInfoList)
		{
			EeCtrlMessageDef.msg_cloud_ctrl_body.Builder ctrlBody =  EeCtrlMessageDef.msg_cloud_ctrl_body.newBuilder().setCtrlType(CloudCtrlType.SYNC_GW_REQ).setSyncGwReqBody(gwBuilder);
			EeCommonDef.msg_header.Builder header = netMessageServiceFacade.makeReqMsgHeader(MessageName.CLOUD_CTRL);
			EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setCloudCtrlBody(ctrlBody).build();
			netMessageServiceFacade.submitMessage(tblEdgeGwInfo.getNodeId(), netMessage);
		}
	}

	public void processLoginGw(EeCommonDef.msg_gw_login_cloud_req_body gwLoginCloudReqBody)
	{
		List<TblEdgeGwInfo> gwInfoList = gwRepository.getBrotherGws(gwLoginCloudReqBody.getNodeId());
		if (gwInfoList == null)
		{
			return;
		}

		EeCtrlMessageDef.msg_sync_gw_req.Builder gwBuilder = EeCtrlMessageDef.msg_sync_gw_req.newBuilder();
		EeCtrlMessageDef.gw_net_info.Builder gwNetInfo = EeCtrlMessageDef.gw_net_info.newBuilder().setNodeId(gwLoginCloudReqBody.getNodeId());

		List<String> regionds = regionGwRepository.getRegionIdsByGwNodeId(gwLoginCloudReqBody.getNodeId());

		if (regionds != null && ! regionds.isEmpty())
		{
			gwNetInfo.addAllRegionIds(regionds);
		}

		gwNetInfo.setGwAddress(gwLoginCloudReqBody.getAddrMe());

		gwBuilder.setGwInfo(gwNetInfo);

		for (TblEdgeGwInfo tblEdgeGwInfo : gwInfoList)
		{
			EeCtrlMessageDef.msg_cloud_ctrl_body.Builder ctrlBody =  EeCtrlMessageDef.msg_cloud_ctrl_body.newBuilder().setCtrlType(CloudCtrlType.SYNC_GW_REQ).setSyncGwReqBody(gwBuilder);
			EeCommonDef.msg_header.Builder header = netMessageServiceFacade.makeReqMsgHeader(MessageName.CLOUD_CTRL);
			EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setCloudCtrlBody(ctrlBody).build();
			netMessageServiceFacade.submitMessage(tblEdgeGwInfo.getNodeId(), netMessage);
		}
	}

	public void processGetGwList(Pair<String, EeNetMessageApi.ee_net_message> getGwListReq)
	{
		try
		{
			LOGGER.info("process get gw list");
			List<TblEdgeGwInfo> tblEdgeGwInfoList = gwRepository.getAllActiveGws();
			if (tblEdgeGwInfoList == null)
			{
				LOGGER.info("no active gw");
				return;
			}

			EeCtrlMessageDef.msg_get_gw_list_rsp.Builder getGwListRsp = EeCtrlMessageDef.msg_get_gw_list_rsp.newBuilder();
			for (TblEdgeGwInfo tblEdgeGwInfo : tblEdgeGwInfoList)
			{
				List<String> regionIds = regionGwRepository.getRegionIdsByGwNodeId(tblEdgeGwInfo.getNodeId());
				if (regionIds == null)
				{
					continue;
				}

				EeCommonDef.network_address.Builder networAddress = EeCommonDef.network_address.newBuilder();
				networAddress.setIp(tblEdgeGwInfo.getHostPublicIp());
				networAddress.setPort(tblEdgeGwInfo.getHostPublicPort());

				EeCtrlMessageDef.gw_net_info.Builder gwNet = EeCtrlMessageDef.gw_net_info.newBuilder();
				gwNet.setNodeId(tblEdgeGwInfo.getNodeId());
				gwNet.addAllRegionIds(regionIds);
				gwNet.setGwAddress(networAddress);
				getGwListRsp.addGws(gwNet);
			}

			EeCtrlMessageDef.msg_cloud_ctrl_body.Builder ctrlBody =  EeCtrlMessageDef.msg_cloud_ctrl_body.newBuilder().setCtrlType(CloudCtrlType.GET_GW_LIST_RSP).setGetGwListRspBody(getGwListRsp);
			EeCommonDef.msg_header.Builder header = netMessageServiceFacade.makeRspMsgHeader(MessageName.CLOUD_CTRL, getGwListReq.getValue().getHead());
			EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setCloudCtrlBody(ctrlBody).build();
			netMessageServiceFacade.submitMessage(getGwListReq.getKey(), netMessage);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get error {}", e.getMessage());
		}

	}

	public void setGWStatus(EeHostDef.host_status_desc hostStatusDesc)
	{
		TblEdgeGwInfo tblEdgeGwInfo = gwRepository.getGwByNodeId(hostStatusDesc.getNodeId());
		if (tblEdgeGwInfo == null)
		{
			return;
		}
		if (tblEdgeGwInfo.getActiveStatus() == ActiveStatus.REMOVED)
		{
			return;
		}
//		tblEdgeGwInfo.setActiveStatus(hostStatusDesc.getHostStatus());
		tblEdgeGwInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
		gwRepository.updateGw(tblEdgeGwInfo);
	}

	public GwNodeConfig getGwNodeConfig()
	{
		GwNodeConfig gwNodeConfig = null;
/*		String imageName = RedisUtil.get(RedisCache.GW_IMAGE);
		String restPortStr = RedisUtil.get(RedisCache.GW_REST_PORT);
		String localPortStr = RedisUtil.get(RedisCache.GW_LOCAL_PORT);*/
		String imageName = ecrmConfig.getGwImage();
		String restPortStr = ecrmConfig.getGwRestPort();
		String localPortStr = ecrmConfig.getGwLocalPort();
		//String cloud = RedisUtil.get(RedisCache.CLOUD_URL);
		String cloud = ecrmConfig.getCloudAddr();
		if (null != imageName && StringUtils.isNumeric(restPortStr) && StringUtils.isNumeric(localPortStr) && null != cloud)
		{
			gwNodeConfig = new  GwNodeConfig(imageName, Integer.parseInt(restPortStr), Integer.parseInt(localPortStr), cloud);
		}
		return gwNodeConfig;
	}
}
