package com.lnjoying.justice.ecrm.facade;

import com.lnjoying.justice.ecrm.service.CombRpcService;
import com.lnjoying.justice.schema.msg.EdgeMessage;
import com.lnjoying.justice.schema.msg.EeCommonDef;
import com.lnjoying.justice.schema.msg.EeNetMessageApi;
import com.micro.core.common.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NetMessageServiceFacade
{
	@Autowired
    CombRpcService combRpcService;

	public void submitMessage(String nodeId, EeNetMessageApi.ee_net_message ee_net_message)
	{
		EdgeMessage edgeMessage = new EdgeMessage();
		edgeMessage.setNodeId(nodeId);
		edgeMessage.setNetMessage(ee_net_message.toByteArray());
		combRpcService.getBusinessMsgSubmitService().submitMessage(edgeMessage);
	}

	public EeCommonDef.msg_header.Builder makeReqMsgHeader(String name)
	{
		return EeCommonDef.msg_header.newBuilder().setMsgName(name)
				.setNonce(Utils.assignUUId())
				.setSessionId(Utils.assignUUId());
	}

	public EeCommonDef.msg_header.Builder makeRspMsgHeader(String name, EeCommonDef.msg_header reqHeader)
	{
		return EeCommonDef.msg_header.newBuilder().setMsgName(name)
				.setNonce(Utils.assignUUId())
				.setSessionId(reqHeader.getSessionId());
	}
}
