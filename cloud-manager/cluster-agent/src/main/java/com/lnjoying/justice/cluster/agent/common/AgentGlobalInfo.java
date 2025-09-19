package com.lnjoying.justice.cluster.agent.common;

import com.lnjoying.justice.schema.common.ChannelState;
import com.micro.core.common.ChannelMap;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 5/19/22 9:47 PM
 */
public class AgentGlobalInfo
{
    public final static ChannelMap channelMap = new ChannelMap();
    public static Channel gConnectChanel;
    public static ChannelState channnelState = ChannelState.DISCONNECTED;
    public final static Map<Integer, String> channelIdMapTunnelId = new ConcurrentHashMap<>();
}
