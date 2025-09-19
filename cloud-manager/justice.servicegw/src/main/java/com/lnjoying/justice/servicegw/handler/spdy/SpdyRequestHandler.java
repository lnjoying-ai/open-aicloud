package com.lnjoying.justice.servicegw.handler.spdy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**@Deprecate
 * @Description:
 * @Author: Regulus
 * @Date: 5/27/22 6:10 PM
 */
public class SpdyRequestHandler extends SimpleChannelInboundHandler<Object>
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception
    {
        LOGGER.info("this is spdy");
    }
}
