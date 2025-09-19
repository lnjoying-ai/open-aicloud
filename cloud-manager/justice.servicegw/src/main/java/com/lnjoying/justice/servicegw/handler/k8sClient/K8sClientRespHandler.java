package com.lnjoying.justice.servicegw.handler.k8sClient;

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

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/3/25 14:34
 */
public class K8sClientRespHandler extends SimpleChannelInboundHandler<Object>
{
    private static Logger LOGGER = LogManager.getLogger();
    
    private Channel authProxyChannel;

    private String clusterServerHost;
    
    private boolean enalbeUpgrade ;
    
    
    public K8sClientRespHandler(Channel channel, String clusterServerHost)
    {
        super();
        this.authProxyChannel = channel;
        this.clusterServerHost = clusterServerHost;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        LOGGER.info("src channel {} k8s rsp: {}", ctx.channel().hashCode(), msg);
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
            authProxyChannel.writeAndFlush(msg);
        }
        else if (msg instanceof HttpContent)
        {
            ((HttpContent) msg).retain();
            if (msg instanceof LastHttpContent)
            {
                LOGGER.info("write last rsp msg, {}", msg);
                authProxyChannel.writeAndFlush(msg);
                if (enalbeUpgrade)
                {
                    setAuthPipeLine(authProxyChannel.pipeline());
                    ChannelPipeline pipeline = ctx.pipeline();
                    setTransPipeLine(pipeline);
                }
            }
            else
            {
                LOGGER.info("write middle rsp msg, {}", msg);
                authProxyChannel.writeAndFlush(msg);
            }
        }
        else
        {
            authProxyChannel.writeAndFlush(msg);
        }
    }
    
    void setTransPipeLine(ChannelPipeline pipeline)
    {
        pipeline.replace("httpDecoder", "byteDecoder", new ByteArrayDecoder());
    }
    
    void setAuthPipeLine(ChannelPipeline pipeline)
    {
        pipeline.replace("httpEncoder","byteEncoder", new ByteArrayEncoder());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        LOGGER.info("k8s client resp handler inactive");
        ctx.close();
        if (authProxyChannel != null)
        {
            authProxyChannel.close();
            authProxyChannel = null;
        }
        ctx.fireChannelInactive();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        LOGGER.info("catch K8sClientRespHandler exception, {}", cause);
        ctx.close();
        if (authProxyChannel != null)
        {
            authProxyChannel.close();
            authProxyChannel = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}
