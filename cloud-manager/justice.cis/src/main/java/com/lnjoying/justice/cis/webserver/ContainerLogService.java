package com.lnjoying.justice.cis.webserver;

import com.lnjoying.justice.cis.common.constant.OperatorType;
import com.lnjoying.justice.cis.controller.dto.request.LogContainerInstReq;
import com.lnjoying.justice.cis.db.model.TblContainerInstInfo;
import com.lnjoying.justice.cis.db.repo.CisRepository;
import com.lnjoying.justice.cis.facade.NetMessageServiceFacade;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.msg.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContainerLogService
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

	public static void addWsTokens(String token, LogContainerInstReq req)
	{
		GlobalWsInfo.PREALLOCATED_WS_TOKENS.put(token, req);
	}

	public static int logInstReq(String logId, LogContainerInstReq req)
	{

		//get node id from db
		TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(req.getInstId());
		if (tblContainerInstInfo == null)
		{
			return ErrorCode.INST_DROPPED.getCode();
		}
		String nodeId = tblContainerInstInfo.getNodeId();
		String refId = tblContainerInstInfo.getRefId();
		
		int ret =  netMessageServiceFacade.logInstReq(logId, req, nodeId, refId);
		if (0 != ret)
		{
			LOGGER.error("log inst error: {} and close session: {}", ret, logId);
		}

		return ret;
	}

	public static int logFollowInstReq(String logId, LogContainerInstReq req)
	{

		//get node id from db
		TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(req.getInstId());
		String nodeId = tblContainerInstInfo.getNodeId();
		String refId = tblContainerInstInfo.getRefId();

		int ret =  netMessageServiceFacade.logFollowInstReq(logId, req, nodeId, refId);
		if (0 != ret)
		{
			LOGGER.error("log follow inst error: {} and close session: {}", ret, logId);
		}

		return ret;
	}

	public static void onLogInstRsp(String token, String logContent)
	{
		LOGGER.debug("recv log inst rsp {}", logContent);

		//look up web session
		WsServerEndPoint wsr = GlobalWsInfo.WS_SESSIONS.get(token);
		if (null == wsr)
		{
			LOGGER.error("log inst rsp get web socket server error, token: {}", token);
			return;
		}

		wsr.sendText(logContent);

	}

	public static void onOutputLogReq(EeInstDef.msg_output_log_req_body body)
	{
		LOGGER.debug("recv log out put req {}", body);
		String token = body.getLogId();
		Integer state = body.getState();
		Integer receivedSeq = body.getOutSeq();
		String logContent = body.getLogContent();

		//look up web session
		WsServerEndPoint wsr = GlobalWsInfo.WS_SESSIONS.get(token);
		if (null == wsr)
		{
			LOGGER.error("log output get web socket server error, token: {}", token);
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

		synchronized (wsr.logOutputCaches)
		{
			if (wsr.logOutputCaches.isEmpty() )
			{
				if ((wsr.latestSendSeq.get() + 1) == receivedSeq)
				{
					wsr.sendText(logContent);
					wsr.latestSendSeq.incrementAndGet();
				}
				else
				{
					wsr.logOutputCaches.add(body);
				}
			}
			//not empty
			else
			{
				if ((wsr.latestSendSeq.get() + 1) == receivedSeq)
				{
					wsr.sendText(logContent);
					wsr.latestSendSeq.incrementAndGet();

					//judge if send left
					while (!wsr.logOutputCaches.isEmpty() && ((wsr.latestSendSeq.get() + 1) == wsr.logOutputCaches.get(0).getOutSeq()))
					{
						EeInstDef.msg_output_log_req_body head = wsr.logOutputCaches.remove(0);
						wsr.sendText(head.getLogContent());
						wsr.latestSendSeq.incrementAndGet();
					}
				}
				else
				{
					wsr.addLogOutputReq(body);

					//too many accumulated and clear output caches
					if (wsr.logOutputCaches.size() > GlobalWsInfo.MAX_OUTPUT_CACHES_SIZE)
					{
						while (!wsr.logOutputCaches.isEmpty())
						{
							EeInstDef.msg_output_log_req_body head = wsr.logOutputCaches.remove(0);
							wsr.sendText(head.getLogContent());
							wsr.latestSendSeq.set(head.getOutSeq());
						}
					}
				}
			}
		}
	}

	public static void stopLog(String instId, String logId)
	{
		try
		{
			//get ref id, node id from db
			TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(instId);
			if (null == tblContainerInstInfo)
			{
				LOGGER.error("stop log get instance id error: {}", instId);
				return;
			}
			String refId = tblContainerInstInfo.getRefId();
			String nodeId = tblContainerInstInfo.getNodeId();

			//header
			EeCommonDef.msg_header.Builder header = netMessageServiceFacade.makeReqMsgHeader(MessageName.INST_OPERATOR);

			//stop log follow req body
			EeInstDef.msg_stop_log_follow_req_body.Builder stopLogFollowReqBuilder = EeInstDef.msg_stop_log_follow_req_body.newBuilder();
			stopLogFollowReqBuilder.setLogId(logId);
			stopLogFollowReqBuilder.setRefId(refId);

			EeInstDef.msg_inst_operator_body.Builder body = EeInstDef.msg_inst_operator_body.newBuilder();
			body.setStopLogFollowReqBody(stopLogFollowReqBuilder).setOperatorType(OperatorType.STOP_LOG_FOLLOW_REQ);

			//net message
			EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setInstOperatorBody(body).build();

			int ret =  netMessageServiceFacade.submitMessage(nodeId, netMessage);
			if (0 != ret)
			{
				LOGGER.error("stop log follow error: {} token: {}", ret, logId);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("stop log follow error: {}", e.getMessage());
		}
	}
}
