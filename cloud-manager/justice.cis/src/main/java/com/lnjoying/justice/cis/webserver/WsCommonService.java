package com.lnjoying.justice.cis.webserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Service;

@Service
public class WsCommonService
{
	private static Logger LOGGER = LogManager.getLogger();

	public static void sendMessageByToken(String token, String message)
	{
		try
		{
			WsServerEndPoint server = GlobalWsInfo.WS_SESSIONS.get(token);
			if (null == server)
			{
				LOGGER.error("send message not find token error: {}", token);
				return;
			}

			server.sendText(message);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void removeWsTokens(String token) { GlobalWsInfo.PREALLOCATED_WS_TOKENS.remove(token); }

	public static void removeSession(String token)
	{
		GlobalWsInfo.WS_SESSIONS.remove(token);
	}

	public static void closeSession(WebSocket session, String reason)
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
}
