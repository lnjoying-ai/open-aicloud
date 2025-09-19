package com.lnjoying.justice.cis.webserver;

import com.lnjoying.justice.cis.db.repo.CisRepository;
import com.lnjoying.justice.schema.msg.EeCommonDef;
import com.lnjoying.justice.schema.msg.EeInstDef;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import org.java_websocket.WebSocket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class WsServerEndPoint
{
	private static Logger LOGGER = LogManager.getLogger();
	private String token;
	private String action;

	private String instId;

	private WebSocket session;

	private long updateTime;

	public AtomicInteger latestSendSeq = new AtomicInteger(-1);

	public List<EeInstDef.msg_output_shell_req_body> outputCaches = new LinkedList<>();
	public List<EeInstDef.msg_output_log_req_body> logOutputCaches = new LinkedList<>();

	private CisRepository cisRepository;

	public WsServerEndPoint(String instId, String token, String action, WebSocket session)
	{
		this.instId = instId;
		this.token = token;
		this.action = action;
		this.session = session;
		this.updateTime = System.currentTimeMillis();
		cisRepository = BeanUtils.getBean("cisRepository");
	}

	public void close()
	{
		if(action.equals("exec"))
		{
			ContainerShellService.stopShell(instId, token);
		}
		else if(action.equals("logs"))
		{
			ContainerLogService.stopLog(instId, token);
		}
	}

	public void onMessage(String message)
	{
		try
		{
			//send to edge
			ContainerShellService.sendInputShell(token, instId, message);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("web socket server on message error: {}", e.getMessage());
		}
	}


	public void sendText(String message)
	{
		try
		{
			//send to browser
			session.send(message);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("send text to web error: {} {}", message, e.getMessage());
		}
	}

	public void addOutputShellReq(EeInstDef.msg_output_shell_req_body body)
	{
		Integer outSeq = body.getOutSeq();
		int index = 0, size = outputCaches.size();
		EeInstDef.msg_output_shell_req_body cur = outputCaches.get(index);

		//insert by order
		while ((cur.getOutSeq() < outSeq) && (index++ < size)) {}

		if (index >= size)
		{
			outputCaches.add(body);
		}
		else
		{
			outputCaches.add(index, body);
		}
	}

	public void addLogOutputReq(EeInstDef.msg_output_log_req_body body)
	{
		Integer outSeq = body.getOutSeq();
		int index = 0, size = logOutputCaches.size();
		EeInstDef.msg_output_log_req_body cur = logOutputCaches.get(index);

		//insert by order
		while ((cur.getOutSeq() < outSeq) && (index++ < size)) {}

		if (index >= size)
		{
			logOutputCaches.add(body);
		}
		else
		{
			logOutputCaches.add(index, body);
		}
	}

	private void closeSession(String reason)
	{
		try
		{
			session.send(reason);
			session.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void release()
	{
		WsCommonService.removeSession(token);
		WsCommonService.removeWsTokens(token);
		closeSession("close websocket now");
		return;
	}
}
