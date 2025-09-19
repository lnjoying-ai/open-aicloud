package com.lnjoying.justice.servicegw.handler.authproxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CloudDirectHandler extends SimpleChannelInboundHandler<Object>
{
    private static Logger LOGGER = LogManager.getLogger();

    private Channel srcChannel;

    private String clusterServerHost;

    private boolean enalbeUpgrade;

    public CloudDirectHandler(Channel channel, String clusterServerHost)
    {
        super();
        this.srcChannel = channel;
        this.clusterServerHost = clusterServerHost;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        LOGGER.info("recv channel {} rsp: {}", ctx.channel().hashCode(), msg);
        if (msg instanceof HttpResponse)
        {
            HttpResponse rsp = (HttpResponse) msg;
            if (StringUtils.isNotEmpty(clusterServerHost))
            {
                rsp.headers().set(HttpHeaderNames.HOST, clusterServerHost);
            }
            LOGGER.info("write and flush first rsp msg, {}", msg);
            if (rsp.status() == HttpResponseStatus.SWITCHING_PROTOCOLS)
            {

                String dst = rsp.headers().get(HttpHeaderNames.UPGRADE);
                dst = dst.toUpperCase();
                if (! dst.isEmpty())
                {
                    enalbeUpgrade = true;
                }
                if (dst.contains("SPDY"))
                {
                    LOGGER.info("upgrade to  spdy: {}", dst);
                }
            }
            srcChannel.writeAndFlush(msg);
        }
        else if (msg instanceof HttpContent)
        {
            ((HttpContent) msg).retain();
            if (msg instanceof LastHttpContent)
            {
                LOGGER.info("write last rsp msg, {}", msg);
                srcChannel.writeAndFlush(msg);
                if (enalbeUpgrade)
                {
                    setAuthPipeLine(srcChannel.pipeline());
                    ChannelPipeline pipeline = ctx.pipeline();
                    setTransPipeLine(pipeline);
                }
            }
            else
            {
                LOGGER.info("write middle rsp msg, {}", msg);
                srcChannel.writeAndFlush(msg);
            }
        }
        else
        {
            srcChannel.writeAndFlush(msg);
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        LOGGER.info("CloudDirect handler exception, channel {}", ctx.channel().hashCode());
        ctx.fireExceptionCaught(cause);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx)
    {
        LOGGER.info("CloudDirect handler inactive, channel {}", ctx.channel().hashCode());
        ctx.fireChannelInactive();
    }

    void setTransPipeLine(ChannelPipeline pipeline)
    {
        pipeline.replace("httpDecoder", "byteDecoder", new ByteArrayDecoder());
    }

    void setAuthPipeLine(ChannelPipeline pipeline)
    {
        pipeline.replace("httpEncoder","byteEncoder", new ByteArrayEncoder());
    }
}
