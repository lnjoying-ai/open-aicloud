package com.lnjoying.justice.cis.webserver;

import com.lnjoying.justice.cis.common.constant.OperatorType;
import com.lnjoying.justice.cis.controller.dto.request.RemoteExecCmdReq;
import com.lnjoying.justice.cis.db.model.TblContainerInstInfo;
import com.lnjoying.justice.cis.db.repo.CisRepository;
import com.lnjoying.justice.cis.facade.NetMessageServiceFacade;
import com.lnjoying.justice.schema.msg.EeCommonDef;
import com.lnjoying.justice.schema.msg.EeInstDef;
import com.lnjoying.justice.schema.msg.EeNetMessageApi;
import com.lnjoying.justice.schema.msg.MessageName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContainerShellService
{
	private static Logger LOGGER = LogManager.getLogger();

	private static CisRepository cisRepository;

	private static NetMessageServiceFacade netMessageServiceFacade;

	@Autowired
	void setCisRepository(CisRepository cisRepository)
	{
		this.cisRepository = cisRepository;
	}

	@Autowired
	void setNetMessageServiceFacade(NetMessageServiceFacade netMessageServiceFacade) {this.netMessageServiceFacade = netMessageServiceFacade;}

	public static void addWsTokens(String token, RemoteExecCmdReq req)
	{
		GlobalWsInfo.PREALLOCATED_WS_TOKENS.put(token, req);
	}

	public static void onEdgeOutputShellReq(EeInstDef.msg_output_shell_req_body body)
	{
		LOGGER.debug("recv out put shell req {}", body);
		String token = body.getExecId();
		Integer state = body.getState();
		Integer receivedSeq = body.getOutSeq();
		String messageText = body.getExecInfo();

		//look up web session
		WsServerEndPoint wsr = GlobalWsInfo.WS_SESSIONS.get(token);
		if (null == wsr)
		{
			LOGGER.error("edge output shell get web socket server error, token: {}", token);
			return;
		}

		//state: 0 exit
		if (0 == state)
		{
			LOGGER.info("edge pipe have been release");
			wsr.release();
			return;
		}

		//update timestamp
		wsr.setUpdateTime(System.currentTimeMillis());

		synchronized (wsr.outputCaches)
		{
			if (wsr.outputCaches.isEmpty() )
			{
				if ((wsr.latestSendSeq.get() + 1) == receivedSeq)
				{
					wsr.sendText(messageText);
					wsr.latestSendSeq.incrementAndGet();
				}
				else
				{
					wsr.outputCaches.add(body);
				}
			}
			//not empty
			else
			{
				if ((wsr.latestSendSeq.get() + 1) == receivedSeq)
				{
					wsr.sendText(messageText);
					wsr.latestSendSeq.incrementAndGet();

					//judge if send left
					while (!wsr.outputCaches.isEmpty() && ((wsr.latestSendSeq.get() + 1) == wsr.outputCaches.get(0).getOutSeq()))
					{
						EeInstDef.msg_output_shell_req_body head = wsr.outputCaches.remove(0);
						wsr.sendText(head.getExecInfo());
						wsr.latestSendSeq.incrementAndGet();
					}
				}
				else
				{
					wsr.addOutputShellReq(body);

					//too many accumulated and clear output caches
					if (wsr.outputCaches.size() > GlobalWsInfo.MAX_OUTPUT_CACHES_SIZE)
					{
						while (!wsr.outputCaches.isEmpty())
						{
							EeInstDef.msg_output_shell_req_body head = wsr.outputCaches.remove(0);
							wsr.sendText(head.getExecInfo());
							wsr.latestSendSeq.set(head.getOutSeq());
						}
					}
				}
			}
		}
	}

	public static int sendStartShellReq(String execId, RemoteExecCmdReq req)
	{
		//header
		EeCommonDef.msg_header.Builder header = netMessageServiceFacade.makeReqMsgHeader(MessageName.INST_OPERATOR);
		//get node id from db
		TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(req.getInstId());
		String refId = tblContainerInstInfo.getRefId();

		//remote exec cmd req body
		EeInstDef.msg_start_shell_req_body.Builder startShellReqBuilder = EeInstDef.msg_start_shell_req_body.newBuilder();
		startShellReqBuilder.setInstId(req.getInstId());
		startShellReqBuilder.setRefId(refId);
		startShellReqBuilder.setExecId(execId);

		for (String cmd : req.getCmds())
		{
			startShellReqBuilder.addCmds(cmd);
		}

		EeInstDef.msg_inst_operator_body.Builder body = EeInstDef.msg_inst_operator_body.newBuilder();
		body.setStartShellReqBody(startShellReqBuilder).setOperatorType(OperatorType.START_SHELL_REQ).setInstType("docker");

		//net message
		EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setInstOperatorBody(body).build();
		int ret =  netMessageServiceFacade.submitMessage(tblContainerInstInfo.getNodeId(), netMessage);
		if (0 != ret)
		{
			LOGGER.error("start shell error: {} and close session: {}", ret, execId);
		}

		return ret;
	}

	public static void stopShell(String instId, String execId)
	{
		try
		{
			//get ref id, node id from db
			TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(instId);
			if (null == tblContainerInstInfo)
			{
				LOGGER.error("stop shell get instance id error: {}", instId);
				return;
			}
			String refId = tblContainerInstInfo.getRefId();
			String nodeId = tblContainerInstInfo.getNodeId();

			//header
			EeCommonDef.msg_header.Builder header = netMessageServiceFacade.makeReqMsgHeader(MessageName.INST_OPERATOR);

			//remote exec cmd req body
			EeInstDef.msg_stop_shell_req_body.Builder stopShellReqBuilder = EeInstDef.msg_stop_shell_req_body.newBuilder();
			stopShellReqBuilder.setExecId(execId);
			stopShellReqBuilder.setRefId(refId);

			EeInstDef.msg_inst_operator_body.Builder body = EeInstDef.msg_inst_operator_body.newBuilder();
			body.setStopShellReqBody(stopShellReqBuilder).setOperatorType(OperatorType.STOP_SHELL_REQ).setInstType("docker");

			//net message
			EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setInstOperatorBody(body).build();
			int ret =  netMessageServiceFacade.submitMessage(nodeId, netMessage);
			if (0 != ret)
			{
				LOGGER.error("stop shell error: {} token: {}", ret, execId);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("stop shell error: {}", e.getMessage());
		}
	}

	public static void sendInputShell(String execId, String instId, String cmd)
	{
		try
		{
			WsServerEndPoint  wsr = GlobalWsInfo.WS_SESSIONS.get(execId);
			if (null == wsr)
			{
				LOGGER.error("edge output shell get web socket server error, token: {}", execId);
				return;
			}

			wsr.setUpdateTime(System.currentTimeMillis());

			//get ref id, node id from db
			TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(instId);
			if (null == tblContainerInstInfo)
			{
				LOGGER.error("send input shell get instance id error: {}", instId);
				return;
			}
			String refId = tblContainerInstInfo.getRefId();
			String nodeId = tblContainerInstInfo.getNodeId();

			//header
			EeCommonDef.msg_header.Builder header = netMessageServiceFacade.makeReqMsgHeader(MessageName.INST_OPERATOR);

			//remote exec cmd req body
			EeInstDef.msg_input_shell_req_body.Builder inputShellReqBuilder = EeInstDef.msg_input_shell_req_body.newBuilder();
			inputShellReqBuilder.setExecId(execId);
			inputShellReqBuilder.setRefId(refId);

//			cmd += "\n";
			inputShellReqBuilder.setCmd(cmd);

			EeInstDef.msg_inst_operator_body.Builder body = EeInstDef.msg_inst_operator_body.newBuilder();
			body.setInputShellReqBody(inputShellReqBuilder).setOperatorType(OperatorType.INPUT_SHELL_REQ).setInstType("docker");

			//net message
			EeNetMessageApi.ee_net_message netMessage= EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setInstOperatorBody(body).build();

			int ret =  netMessageServiceFacade.submitMessage(nodeId, netMessage);
			if (0 != ret)
			{
				LOGGER.error("input shell error: {}", ret);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("stop shell error: ", e);
		}
	}
}
