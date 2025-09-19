package com.lnjoying.justice.servicegw.service;

import com.lnjoying.justice.servicegw.service.rpc.CombRpcService;
import com.lnjoying.justice.schema.msg.*;
import com.micro.core.common.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NetMessageServiceFacade
{
	private static Logger LOGGER = LogManager.getLogger();
	@Autowired
	private CombRpcService combRpcService;

	@Autowired
	private ServicePortService servicePortService;

	public int submitMessage(String workerId, EeNetMessageApi.ee_net_message ee_net_message)
	{
		WorkerMessage workerMessage = new WorkerMessage();
		workerMessage.setWorkerId(workerId);
		workerMessage.setNetMessage(ee_net_message.toByteArray());
		return combRpcService.getBusinessMsgSubmitService().submitWorkerMessage(workerMessage);
	}

	public EeCommonDef.msg_header.Builder makeReqMsgHeader(String name)
	{
		return EeCommonDef.msg_header.newBuilder().setMsgName(name)
				.setNonce(Utils.assignUUId())
				.setSessionId(String.format("%s_%s", servicePortService.getMicroserviceInstance().getEndpoints().get(0).replace("rest://", ""), Utils.assignUUId()));
	}

	public EeCommonDef.msg_header.Builder makeRspMsgHeader(String name, EeCommonDef.msg_header reqHeader)
	{
		return EeCommonDef.msg_header.newBuilder().setMsgName(name)
				.setNonce(Utils.assignUUId())
				.setSessionId(reqHeader.getSessionId());
	}
	
	
	public EeTunnelDef.msg_tunnel_operator_body.Builder makeTunnelBody(String operName)
	{
		return EeTunnelDef.msg_tunnel_operator_body.newBuilder().setOperatorType(operName);
	}
	
	public EeFwdDef.msg_fwd_operator_body.Builder makeFwdBody(String operName)
	{
		return EeFwdDef.msg_fwd_operator_body.newBuilder().setOperatorType(operName);
	}
}
