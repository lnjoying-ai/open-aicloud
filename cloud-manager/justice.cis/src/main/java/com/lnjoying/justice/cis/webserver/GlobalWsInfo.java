package com.lnjoying.justice.cis.webserver;

import java.util.concurrent.ConcurrentHashMap;

public class GlobalWsInfo
{

	public static ConcurrentHashMap<String, WsServerEndPoint> WS_SESSIONS = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<String, Object> PREALLOCATED_WS_TOKENS = new ConcurrentHashMap<>();
	public static final Integer MAX_OUTPUT_CACHES_SIZE = 4;
}
