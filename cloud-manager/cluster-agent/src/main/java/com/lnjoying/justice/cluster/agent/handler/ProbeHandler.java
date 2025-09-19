package com.lnjoying.justice.cluster.agent.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;


/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/3/25 14:34
 */

public class ProbeHandler extends SimpleChannelInboundHandler<HttpRequest>
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest httpRequest) throws Exception
    {
        String uri = httpRequest.uri();
        if (uri.equals("/healthz"))
        {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(200));
            String resp = "ok";
            ByteBuf buffer = Unpooled.copiedBuffer(new StringBuilder(resp), CharsetUtil.UTF_8);
            try
            {
                response.content().writeBytes(buffer);
                response.headers().set("Content-Type", "text/plain;charset=utf-8");
                response.headers().setInt("Content-Length", response.content().readableBytes());
                ctx.writeAndFlush(response);
            }
            finally
            {
                buffer.release();
            }
        }
        else
        {
            DefaultHttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
            ChannelFuture writeFuture = ctx.writeAndFlush(rsp);
            writeFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        ctx.close();
    }
    
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        ctx.close();
    }
}
