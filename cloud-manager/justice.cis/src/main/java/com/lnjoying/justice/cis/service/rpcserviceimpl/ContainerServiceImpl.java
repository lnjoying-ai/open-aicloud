package com.lnjoying.justice.cis.service.rpcserviceimpl;

import com.lnjoying.justice.cis.common.constant.*;
import com.lnjoying.justice.cis.db.model.TblContainerInstInfo;
import com.lnjoying.justice.cis.db.model.TblContainerInstInfoExample;
import com.lnjoying.justice.cis.db.model.TblContainerSpecInfo;
import com.lnjoying.justice.cis.db.repo.CisRepository;
import com.lnjoying.justice.cis.facade.*;
import com.lnjoying.justice.cis.service.CisManagerService;
import com.lnjoying.justice.cis.webserver.*;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.entity.scheduler.BindResourcesResult;
import com.lnjoying.justice.schema.entity.scheduler.SchedulerResult;
import com.lnjoying.justice.schema.entity.servicemanager.TargetServiceInfo;
import com.lnjoying.justice.schema.entity.stat.GetStatusMetricsRsp;
import com.lnjoying.justice.schema.msg.*;
import com.lnjoying.justice.schema.entity.scheduler.DstNode;
import com.lnjoying.justice.schema.service.cis.ContainerInstService;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RpcSchema(schemaId = "containerService")
public class ContainerServiceImpl implements ContainerInstService
{
	private static Logger LOGGER = LogManager.getLogger(ContainerServiceImpl.class);

	@Autowired
	private CisManagerService cisManagerService;
	
	@Autowired
	CisInstServiceFacade cisInstServiceFacade;
	
	@Autowired
	ClsInstServiceFacade clsInstServiceFacade;

	@Autowired
	CmpInstServiceFacade cmpInstServiceFacade;

	@Autowired
	EcrmInstServiceFacade ecrmInstServiceFacade;
	
	@Autowired
	NetMessageServiceFacade netMessageServiceFacade;

	@Autowired
	private CisRepository cisRepository;

	InstService getInstService(String instId)
	{
		if (instId.startsWith(CisIdPrefix.K8S))
		{
			return clsInstServiceFacade;
		}
		else if (instId.startsWith(CisIdPrefix.CMP))
		{
			return cmpInstServiceFacade;
		}
		else if (instId.startsWith(CisIdPrefix.ECRM))
		{
			return ecrmInstServiceFacade;
		}
		return cisInstServiceFacade;
	}
	

	public void evacuateEdge(@ApiParam(name = "nodeId") String nodeId)
	{
		LOGGER.info("evacuate node: {}", nodeId);
		cisInstServiceFacade.evacuateInst(nodeId);
		clsInstServiceFacade.evacuateInst(nodeId);
		return;
	}

	@Override
	public void setContainer(@ApiParam(name = "instId") String instId,
							 @ApiParam(name = "instName") String instName,
							 @ApiParam(name = "imageName") String imageName,
							 @ApiParam(name = "cmd") String cmd,
							 @ApiParam(name = "refId") String refId,
							 @ApiParam(name = "status") int status,
							 @ApiParam(name = "userId") String userId,
							 @ApiParam(name = "bpId") String bpId,
							 @ApiParam(name = "dstNode") DstNode dstNode)
	{
		cisInstServiceFacade.updateInst(instId, instName, imageName, cmd, refId, status, userId, bpId, dstNode);
	}

	@Override
	public void setContainerStatus(@ApiParam(name = "instId") String instId,
								   @ApiParam(name = "status") int status)
	{
		getInstService(instId).updateInstStatus(instId, status);
	}

	@Override
	public CombRetPacket deliver(@ApiParam(name = "message") EdgeMessage edgeMessage)
	{
		CombRetPacket ret = new CombRetPacket();

		try
		{
			EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.parseFrom(edgeMessage.getNetMessage());
			switch (netMessage.getHead().getMsgName())
			{
				case MessageName.INST_OPERATOR:
					onInstOperatorMessage(edgeMessage.getNodeId(), netMessage);
					break;
				case MessageName.FILE_OPERATOR:
					onFileOperatorMessage(edgeMessage.getNodeId(), netMessage);
					break;
				default:
					break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
 		    ret.setStatus(ErrorCode.SystemError.getCode());
			return ret;
		}

		return ret;
	}

	private void onInstOperatorMessage(String nodeId, EeNetMessageApi.ee_net_message netMessage)
	{
		EeInstDef.msg_inst_operator_body inst_operator_body_body = netMessage.getInstOperatorBody();
		if (null == inst_operator_body_body)
		{
			LOGGER.error("inst operator body null");
			return;
		}

		String opertorType = inst_operator_body_body.getOperatorType();
		switch (opertorType)
		{
			case OperatorType.CREATE_INST_RSP:
				onCreateContainerInstRsp(nodeId, netMessage);
				break;
			case OperatorType.LIFE_MNG_INST_RSP:
				onLifeMngInstRsp(nodeId, netMessage);
				break;
			case OperatorType.START_SHELL_RSP:
				onStartShellRsp(nodeId, netMessage);
				break;
			case OperatorType.RPT_INST_REQ:
				onRptInstReq(nodeId, netMessage);
				break;
			case OperatorType.OUTPUT_SHELL_REQ:
				onOutputShellReq(nodeId, netMessage);
				break;
			case OperatorType.BATCH_RPT_INST_REQ:
				onBatchRptInst(netMessage);
				break;

			/////////////////////////////log////////////////////////////////////////
			case OperatorType.LOG_INST_RSP:
				onLogInstRsp(nodeId, netMessage);
				break;
			case OperatorType.LOG_FOLLOW_INST_RSP:
				onLogFollowInstRsp(nodeId, netMessage);
				break;
			case OperatorType.OUTPUT_LOG_REQ:
				onOutputLogReq(nodeId, netMessage);
				break;
			case OperatorType.STOP_LOG_RSP:
				break;
			//////////////////////////////////////////////////////////////////////////
			case OperatorType.RPT_INST_RSP:
				break;
			case OperatorType.RPT_INST_STAT_RSP:
				break;
			case OperatorType.LIST_INST_RSP:
				onListInstRsp(netMessage);
				break;
			default:
				break;
		}
		return;
	}

	private void onCreateContainerInstRsp(String nodeId, EeNetMessageApi.ee_net_message netMessage)
	{
		try
		{
			EeInstDef.msg_create_inst_rsp_body body = netMessage.getInstOperatorBody().getCreateInstRspBody();
			int errorCode = body.getErrorCode();
			EeInstDef.inst_status_desc statusInfo = body.getStatusInfo();
		

			//if error then log
			if (0 != errorCode)
			{
				LOGGER.error("create container instance received error rsp, instId: {} error code: {}", statusInfo.getInstId(), errorCode);
			}
			
			getInstService(statusInfo.getInstId()).updateInst4CreateEvent(statusInfo);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("create container instance rsp exception: {}", e.getMessage());
		}
	}

	private void onLifeMngInstRsp(String nodeId, EeNetMessageApi.ee_net_message netMessage)
	{
		try
		{
			EeInstDef.msg_life_mng_inst_rsp_body body = netMessage.getInstOperatorBody().getLifeMngInstRspBody();
			int errorCode = body.getErrorCode();
			EeInstDef.inst_status_desc statusInfo = body.getStatusInfo();

			//if error then log
			if (0 != errorCode)
			{
				LOGGER.error("life mng inst received error rsp, instId: {} error code: {}", statusInfo.getInstId(), errorCode);
			}

			removeLifeMngEvent(statusInfo.getInstId(), netMessage.getHead().getSessionId());
			
			getInstService(statusInfo.getInstId()).updateInstInfo(statusInfo);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("life mng inst rsp exception: {}", e.getMessage());
		}
	}

	private void onStartShellRsp(String nodeId, EeNetMessageApi.ee_net_message netMessage)
	{
		//get execId and set to web sessions
		EeInstDef.msg_start_shell_rsp_body body = netMessage.getInstOperatorBody().getStartShellRspBody();
		int errorCode = body.getErrorCode();
		String execId = body.getExecId();

		//remote exec error
		if (0  != errorCode)
		{
			cisInstServiceFacade.dealStartShellRsp(errorCode, execId);
			//log
			LOGGER.error("on start shell rsp received error code: {} node id: {} token: {}", errorCode, nodeId, execId);
		}
	}

	private void onOutputShellReq(String nodeId, EeNetMessageApi.ee_net_message netMessage)
	{
		EeInstDef.msg_output_shell_req_body body = netMessage.getInstOperatorBody().getOutputShellReqBody();
		ContainerShellService.onEdgeOutputShellReq(body);
	}

	private void onBatchRptInst(EeNetMessageApi.ee_net_message netMessage)
	{
		EeInstDef.msg_batch_rpt_inst_req_body batchRptInstReqBody = netMessage.getInstOperatorBody().getBatchRptInstReqBody();

		//send rsp
		EeInstDef.msg_batch_rpt_inst_rsp_body.Builder batchRptInstRspBody = EeInstDef.msg_batch_rpt_inst_rsp_body.newBuilder();
		batchRptInstRspBody.setErrorCode(ErrorCode.SUCCESS.getCode());

		EeNetMessageApi.ee_net_message.Builder netMessageRsp = EeNetMessageApi.ee_net_message.newBuilder();
		EeCommonDef.msg_header.Builder rspHeadBuild = EeCommonDef.msg_header.newBuilder();
		rspHeadBuild.setNonce(Utils.assignUUId());
		rspHeadBuild.setSessionId(netMessage.getHead().getSessionId());
		rspHeadBuild.setMsgName(MessageName.INST_OPERATOR);

		EeInstDef.msg_inst_operator_body.Builder instRspOperator = EeInstDef.msg_inst_operator_body.newBuilder();
		instRspOperator.setBatchRptInstRspBody(batchRptInstRspBody);
		netMessageRsp.setHead(rspHeadBuild).setInstOperatorBody(instRspOperator);
		EdgeMessage edgeRspMessage = new EdgeMessage();
		edgeRspMessage.setNetMessage(netMessageRsp.build().toByteArray());
		edgeRspMessage.setNodeId(netMessage.getRoute().getONodeId());
		
		netMessageServiceFacade.submitMessage(netMessage.getRoute().getONodeId(), netMessageRsp.build());
		//update inst rpt
		for (EeInstDef.batch_rpt_inst rptInst : batchRptInstReqBody.getRptInfosList())
		{
			if (rptInst.getInstType().equals("docker"))
			{
				for (EeInstDef.inst_status_desc statusDesc : rptInst.getInstStatusInfosList())
				{
					getInstService(statusDesc.getInstId()).dealInstRpt(statusDesc, netMessage.getRoute());
				}
			}
		}
	}

	private void onListInstRsp(EeNetMessageApi.ee_net_message netMessage)
	{
		EeInstDef.msg_list_inst_rsp_body listInstRspBody = netMessage.getInstOperatorBody().getListInstRspBody();

		//update inst rpt
		for (EeInstDef.inst_status_desc statusDesc : listInstRspBody.getInstStatusInfoList())
		{
			getInstService(statusDesc.getInstId()).dealInstRpt(statusDesc, netMessage.getRoute());
		}
	}

	private void onRptInstReq(String nodeId, EeNetMessageApi.ee_net_message netMessage)
	{
		try
		{
			EeInstDef.msg_rpt_inst_req_body body = netMessage.getInstOperatorBody().getRptInstReqBody();
			EeInstDef.inst_status_desc statusInfo = body.getStatusInfo();

			getInstService(statusInfo.getInstId()).dealInstRpt(statusInfo, netMessage.getRoute());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("remove container instance rsp exception: {}", e.getMessage());
		}
	}

	private void onLogInstRsp(String nodeId, EeNetMessageApi.ee_net_message netMessage)
	{
		//get sessionId and set to web sessions
		EeInstDef.msg_log_inst_rsp_body body = netMessage.getInstOperatorBody().getLogInstRspBody();
		EeCommonDef.msg_header header = netMessage.getHead();
		int errorCode = body.getErrorCode();
		String logContent = body.getLogContent();
		String logId = header.getSessionId();
		
		if (errorCode != 0)
		{
			LOGGER.error("on log inst rsp received error code: {} node id: {} token: {}", errorCode, nodeId, logId);
		}
		
		getInstService(logId).dealInstLogRsp(errorCode, logId, logContent);
	}

	private void onLogFollowInstRsp(String nodeId, EeNetMessageApi.ee_net_message netMessage)
	{
		//get logId and set to web sessions
		EeInstDef.msg_log_follow_inst_rsp_body body = netMessage.getInstOperatorBody().getLogFollowInstRspBody();
		int errorCode = body.getErrorCode();
		String logId = body.getLogId();

		//remote exec error
		if (0  != errorCode)
		{
			//log
			LOGGER.error("on log follow rsp received error code: {} node id: {} token: {}", errorCode, nodeId, logId);
			cisInstServiceFacade.dealInstFollowLogRsp(errorCode, logId);
		}
	}

	private void onOutputLogReq(String nodeId, EeNetMessageApi.ee_net_message netMessage)
	{
		EeInstDef.msg_output_log_req_body body = netMessage.getInstOperatorBody().getOutputLogReqBody();
		ContainerLogService.onOutputLogReq(body);
	}

	@Override
	public BindResourcesResult bindResources(@ApiParam(name = "schedulerResult") SchedulerResult schedulerResult)
	{
		return cisInstServiceFacade.bindResources(schedulerResult);
	}

	@Override
	public boolean isOwnerOfInst(@ApiParam(name = "instId") String instId,
					@ApiParam(name = "userId") String userId,
					@ApiParam(name = "bpId") String bpId)
	{
		return cisManagerService.isOwnerOfInst(instId, null, userId) || cisManagerService.isOwnerOfInst(instId, bpId, userId);
	}

	private void removeLifeMngEvent(String instId, String sessionId)
	{
		if (!instId.startsWith(CisIdPrefix.K8S))
		{
			RedisUtil.delete(CisRedisField.CIS_SPAWN_LIFE_EVENT + sessionId);
			RedisUtil.zrem(CisRedisField.CIS_SPAWN_LIFE_EVENT_SET, sessionId);
		}
	}

	@Override
	public int getContainerNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId)
	{
		return cisInstServiceFacade.getContainerNum(userId, bpId);
	}

	public GetStatusMetricsRsp getContainerInstanceStatusMetrics(@ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId)
	{
		GetStatusMetricsRsp containerInstanceStatusMetrics = cisRepository.getContainerInstanceStatusMetrics(bpId, userId);
		return containerInstanceStatusMetrics;
	}


	private int onFileOperatorMessage(String nodeId, EeNetMessageApi.ee_net_message netMessage)
	{
		EeCommonDef.msg_header head = netMessage.getHead();
		EeFileDef.msg_file_operator_body fileOperatorBody = netMessage.getFileOperatorBody();

		try
		{
			if (fileOperatorBody == null || head == null)
			{
				return ErrorCode.PARAM_ERROR.getCode();
			}

			String msgName = fileOperatorBody.getOperatorType();
			switch (msgName)
			{
				case MessageName.FILE_CREATE_RSP:
					EeFileDef.msg_file_create_rsp_body fileCreateRspBody = fileOperatorBody.getFileCreateRspBody();
					cisManagerService.processFileCreateRsp(fileCreateRspBody, head);
					break;
				default:
			}
		}
		catch (Exception e)
		{
			LOGGER.error("parse file operator msg fail", e);
		}

		return ErrorCode.SUCCESS.getCode();
	}

	@Override
	public List<TargetServiceInfo> getContainer(@ApiParam(name = "specId")String specId, @ApiParam(name = "instId")String instId)
	{
		List<TargetServiceInfo> targetServiceInfos = new ArrayList<>();
		if (instId != null)
		{
			TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(instId);
			targetServiceInfos.add(new TargetServiceInfo(instId, tblContainerInstInfo.getRegionId(),
					tblContainerInstInfo.getSiteId(), tblContainerInstInfo.getNodeId(), 0));
		}
		else
		{
			TblContainerInstInfoExample example = new TblContainerInstInfoExample();
			TblContainerInstInfoExample.Criteria criteria = example.createCriteria();
			criteria.andSpecIdEqualTo(specId);
			criteria.andStatusEqualTo(InstanceState.RUNNING.getCode());

			List<TblContainerInstInfo> records = cisRepository.getContainerInstInfosByExample(example);

			records.forEach(tblContainerInstInfo -> {
				targetServiceInfos.add(new TargetServiceInfo(tblContainerInstInfo.getInstId(), tblContainerInstInfo.getRegionId(),
						tblContainerInstInfo.getSiteId(), tblContainerInstInfo.getNodeId(), 0));
			});
		}
		return targetServiceInfos;
	}

	@Override
	public String getSpecName(@ApiParam(name = "specId")String specId)
	{
		TblContainerSpecInfo tblStackSpecInfo = cisRepository.getSpec(specId);
		if (Objects.nonNull(tblStackSpecInfo))
		{
			return tblStackSpecInfo.getSpecName();
		}
		return "";
	}

	@Override
	public String getInstName(@ApiParam(name = "instId")String instId)
	{
		TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(instId);
		if (Objects.nonNull(tblContainerInstInfo))
		{
			return tblContainerInstInfo.getContainerName();
		}
		return "";
	}
}
