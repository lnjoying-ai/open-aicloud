package com.lnjoying.justice.cis.webserver;

import com.lnjoying.justice.cis.controller.dto.request.LogContainerInstReq;
import com.lnjoying.justice.cis.controller.dto.request.RemoteExecCmdReq;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class WsServer extends WebSocketServer
{
	private static Logger LOGGER = LogManager.getLogger();


	public WsServer(int port)
	{
		super(new InetSocketAddress(port));
	}

	public WsServer(InetSocketAddress address)
	{
		super(address);
	}

	String getToken(WebSocket conn)
	{
		String uri = conn.getResourceDescriptor();
		if (StringUtils.isEmpty(uri))
		{
			LOGGER.error("web socket uri empty ");
			WsCommonService.closeSession(conn,"close websocket now: token error");
			return "";
		}

		return uri.split("/")[3];
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake)
	{
		String uri = conn.getResourceDescriptor();
		if (StringUtils.isEmpty(uri))
		{
			LOGGER.error("web socket uri empty ");
			WsCommonService.closeSession(conn,"close websocket now: token error");
			return;
		}

		String [] uriArray = uri.split("/");
		if (uriArray == null || uriArray.length < 4)
		{
			LOGGER.error("web socket uri error ");
			WsCommonService.closeSession(conn,"close websocket now: token error");
			return;
		}
		
		String action = uriArray[2];
		String token = uriArray[3];
		// ws连接的时候触发的代码，onOpen中我们不做任何操作
		LOGGER.info("web socket server new token: {}", token);

		//check token validation error
		if (!GlobalWsInfo.PREALLOCATED_WS_TOKENS.containsKey(token))
		{
			LOGGER.error("web socket token error not exists: {}", token);
			WsCommonService.closeSession(conn,"close websocket now: token not exist");
			return;
		}

		//repeated token error
		if (GlobalWsInfo.WS_SESSIONS.containsKey(token))
		{
			LOGGER.error("web socket token repeated: {}", token);
			WsCommonService.closeSession(conn,"close websocket now: token error");
			return;
		}

		if (action.equals("exec"))
		{
			LOGGER.info("open remote shell websocket");
			RemoteExecCmdReq req = (RemoteExecCmdReq)GlobalWsInfo.PREALLOCATED_WS_TOKENS.get(token);


			//send start shell
			if (ContainerShellService.sendStartShellReq(token, req) != 0)
			{
				WsCommonService.closeSession(conn,"close websocket now: start shell error");
				return;
			}

			WsServerEndPoint wsServerEndPoint = new WsServerEndPoint(req.getInstId(), token, action, conn);
			GlobalWsInfo.WS_SESSIONS.put(token, wsServerEndPoint);
		}
		else if(action.equals("logs"))
		{
			LOGGER.info("open log inst websocket");
			LogContainerInstReq req = (LogContainerInstReq)GlobalWsInfo.PREALLOCATED_WS_TOKENS.get(token);

			if(req.isFlow())
			{
				//log follow
				if (ContainerLogService.logFollowInstReq(token, req) != 0)
				{
					WsCommonService.closeSession(conn,"close websocket now: log follow inst error");
					return;
				}
			}
			else
			{
				//log inst
				if (ContainerLogService.logInstReq(token, req) != 0)
				{
					WsCommonService.closeSession(conn,"close websocket now: log inst error");
					return;
				}
			}

			WsServerEndPoint wsServerEndPoint = new WsServerEndPoint(req.getInstId(), token, action, conn);
			GlobalWsInfo.WS_SESSIONS.put(token, wsServerEndPoint);
		}


		//remove from pre-allocated tokens
		GlobalWsInfo.PREALLOCATED_WS_TOKENS.remove(token);

	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote)
	{
		String token = getToken(conn);
		LOGGER.info("token: {}", token);

		//remove tokes


		WsServerEndPoint wsServerEndPoint = GlobalWsInfo.WS_SESSIONS.get(token);
		if (wsServerEndPoint != null)
		{
			wsServerEndPoint.close();
		}

		//stop edge
		GlobalWsInfo.WS_SESSIONS.remove(token);
		GlobalWsInfo.PREALLOCATED_WS_TOKENS.remove(token);
		conn.send(reason);
	}

	@Override
	public void onMessage(WebSocket conn, String message)
	{
		String token = getToken(conn);

		LOGGER.info("web socket server token: {} message: {}", token, message);

		if (StringUtils.isEmpty(message))
		{
			LOGGER.info("web socket server received empty message: {}", token);
			return;
		}

		WsServerEndPoint wsServerEndPoint = GlobalWsInfo.WS_SESSIONS.get(token);
		//next to do
		if (wsServerEndPoint == null)
		{
			LOGGER.error("web socket server on message not find wsServerEntity for: {}", token);
			return;
		}
		wsServerEndPoint.onMessage(message);
	}

	@Override
	public void onError(WebSocket conn, Exception ex)
	{
		String token = getToken(conn);
		if (StringUtils.isEmpty(token))
		{
			return;
		}

		WsServerEndPoint wsServerEndPoint = GlobalWsInfo.WS_SESSIONS.get(token);
		if (wsServerEndPoint != null)
		{
			wsServerEndPoint.close();
		}

		GlobalWsInfo.WS_SESSIONS.remove(token);
		GlobalWsInfo.PREALLOCATED_WS_TOKENS.remove(token);
		ex.printStackTrace();
		LOGGER.info("webSocket exception {}", ex.getMessage());
	}

	@Override
	public void onStart()
	{
		System.out.println("start success");
	}
}