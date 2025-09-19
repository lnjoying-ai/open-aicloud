package com.lnjoying.justice.servicegw.handler.k8sClient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/3/25 14:49
 */

public class CustomHttpResponseEncoder  extends HttpResponseEncoder
{
    @Override
    public void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        super.encode(ctx, msg, out);
    }
}
