package com.lnjoying.justice.servicegw.handler.spdy;

import com.lnjoying.justice.servicegw.handler.authproxy.AuthHandler;
import com.lnjoying.justice.servicegw.handler.authproxy.ConvertHandler;
import com.lnjoying.justice.servicegw.service.rpc.CombRpcService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.spdy.*;
import io.netty.handler.ssl.SslHandler;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.SSLEngine;
import java.util.List;

/**Deprecate
 * @Description:
 * @Author: Regulus
 * @Date: 5/27/22 5:29 PM
 */
public class JoySpdyOrHttpChooser extends ByteToMessageDecoder
{
    
    private final int maxSpdyContentLength;
    private final int maxHttpContentLength;
    
    CombRpcService combRpcService;
    String clusterServerHost;
    
    public JoySpdyOrHttpChooser(int maxSpdyContentLength, int maxHttpContentLength, String clvrHost, CombRpcService combRpcService)
    {
        this.maxSpdyContentLength = maxSpdyContentLength;
        this.maxHttpContentLength = maxHttpContentLength;
        this.clusterServerHost = clvrHost;
        this.combRpcService = combRpcService;
    }
    
    protected JoySpdyOrHttpChooser.SelectedProtocol getProtocol(SSLEngine engine)
    {
        String[] protocol = StringUtils.split(engine.getSession().getProtocol(), ':');
        if (protocol.length < 2)
        {
            return JoySpdyOrHttpChooser.SelectedProtocol.HTTP_1_1;
        }
        else
        {
            JoySpdyOrHttpChooser.SelectedProtocol selectedProtocol = JoySpdyOrHttpChooser.SelectedProtocol.protocol(protocol[1]);
            return selectedProtocol;
        }
    }
    
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        if (this.initPipeline(ctx)) {
            ctx.pipeline().remove(this);
        }
        
    }
    
    private boolean initPipeline(ChannelHandlerContext ctx)
    {
        SslHandler handler = ctx.pipeline().get(SslHandler.class);
        if (handler == null)
        {
            throw new IllegalStateException("SslHandler is needed for SPDY");
        }
        else
        {
            JoySpdyOrHttpChooser.SelectedProtocol protocol = this.getProtocol(handler.engine());
            switch(protocol)
            {
                case UNKNOWN:
                    return false;
                case SPDY_3_1:
                    this.addSpdyHandlers(ctx, SpdyVersion.SPDY_3_1);
                    break;
                case HTTP_1_0:
                case HTTP_1_1:
                    this.addHttpHandlers(ctx);
                    break;
                default:
                    throw new IllegalStateException("Unknown SelectedProtocol");
            }
            
            return true;
        }
    }
    
    protected void addSpdyHandlers(ChannelHandlerContext ctx, SpdyVersion version) {
        ChannelPipeline pipeline = ctx.pipeline();
        pipeline.addLast("spdyFrameCodec", new SpdyFrameCodec(version));
        pipeline.addLast("spdySessionHandler", new SpdySessionHandler(version, true));
        pipeline.addLast("spdyHttpEncoder", new SpdyHttpEncoder(version));
        pipeline.addLast("spdyHttpDecoder", new SpdyHttpDecoder(version, this.maxSpdyContentLength));
        pipeline.addLast("spdyStreamIdHandler", new SpdyHttpResponseStreamIdHandler());
        pipeline.addLast("httpRequestHandler", this.createHttpRequestHandlerForSpdy());
    }
    
    protected void addHttpHandlers(ChannelHandlerContext ctx)
    {
        ChannelPipeline pipeline = ctx.pipeline();
        pipeline.addLast("httpRequestDecoder", new HttpRequestDecoder());
        pipeline.addLast("httpResponseEncoder", new HttpResponseEncoder());
        pipeline.addLast("authHandler", new AuthHandler(combRpcService));
        pipeline.addLast("convertHandler", new ConvertHandler());
    }
    
    protected  ChannelInboundHandler createHttpRequestHandlerForHttp()
    {
        return new SpdyRequestHandler();  //8
    }
    
    protected ChannelInboundHandler createHttpRequestHandlerForSpdy()
    {
        return this.createHttpRequestHandlerForHttp();
    }
    
    public static enum SelectedProtocol
    {
        SPDY_3_1("spdy/3.1"),
        HTTP_1_1("http/1.1"),
        HTTP_1_0("http/1.0"),
        UNKNOWN("Unknown");
        
        private final String name;
        
        private SelectedProtocol(String defaultName)
        {
            this.name = defaultName;
        }
        
        public String protocolName() {
            return this.name;
        }
        
        public static SelectedProtocol protocol(String name)
        {
            JoySpdyOrHttpChooser.SelectedProtocol[] arr$ = values();
            int len$ = arr$.length;
            
            for(int i$ = 0; i$ < len$; ++i$)
            {
                JoySpdyOrHttpChooser.SelectedProtocol protocol = arr$[i$];
                if (protocol.protocolName().equals(name))
                {
                    return protocol;
                }
            }
            
            return UNKNOWN;
        }
    }
}
