package com.lnjoying.justice.api.handler;

import com.micro.core.common.ChannelEvent;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

public class EventHandler extends ChannelDuplexHandler
{
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
        if (evt == ChannelEvent.CONNECTED)
        {

        }

        if (evt == ChannelEvent.LOGIN_SUCCESS)
        {

        }
    }
}
