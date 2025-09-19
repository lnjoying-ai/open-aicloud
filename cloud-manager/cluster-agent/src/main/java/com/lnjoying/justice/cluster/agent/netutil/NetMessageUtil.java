package com.lnjoying.justice.cluster.agent.netutil;

import com.lnjoying.justice.schema.msg.EeCommonDef;
import com.micro.core.common.Utils;
import org.springframework.stereotype.Service;

@Service
public class NetMessageUtil
{
	public static EeCommonDef.msg_header.Builder makeReqMsgHeader(String name)
	{
		return EeCommonDef.msg_header.newBuilder().setMsgName(name)
				.setNonce(Utils.assignUUId())
				.setSessionId(Utils.assignUUId());
	}

	public static EeCommonDef.msg_header.Builder makeRspMsgHeader(String name, EeCommonDef.msg_header reqHeader)
	{
		return EeCommonDef.msg_header.newBuilder().setMsgName(name)
				.setNonce(Utils.assignUUId())
				.setSessionId(reqHeader.getSessionId());
	}
}
