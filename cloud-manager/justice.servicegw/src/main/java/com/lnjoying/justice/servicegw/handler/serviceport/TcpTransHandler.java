package com.lnjoying.justice.servicegw.handler.serviceport;

import com.lnjoying.justice.schema.entity.cluster.ClusterEntity;
import com.lnjoying.justice.servicegw.common.TimeConst;
import com.lnjoying.justice.servicegw.config.ServerConfigBean;
import com.lnjoying.justice.servicegw.handler.codec.HttpRequestEncoderT;
import com.lnjoying.justice.servicegw.handler.k8sClient.K8sClientRespHandler;
import com.lnjoying.justice.servicegw.model.ClusterProxyMsg;
import com.lnjoying.justice.servicegw.model.DirectMsgEntity;
import com.lnjoying.justice.servicegw.secret.SslContextFactory;
import com.lnjoying.justice.servicegw.service.TunnelService;
import com.lnjoying.justice.servicegw.util.SpdyUtil;
import com.micro.core.nework.unix.Inet6AddressUtil;
import com.micro.core.nework.unix.UnixSocketConnector;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.unix.DomainSocketChannel;
import io.netty.channel.unix.Socket;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.spdy.SpdyVersion;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;

import javax.net.ssl.SSLEngine;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class TcpTransHandler extends SimpleChannelInboundHandler<ClusterProxyMsg>
{
    /**
     * 10M
     */
    private int maxContentLength = 10 * 1024 * 1024;

    private static Logger LOGGER = LogManager.getLogger();

    Queue<ClusterProxyMsg> msgPool = new LinkedBlockingDeque<>();

    Channel k8sClientChannel = null;
    Boolean createConnect = false;
    Boolean ready = false;

    TunnelService tunnelService = null;

    ServerConfigBean serverConfigBean;

    ClusterEntity clusterEntity;

    List<Object> msgDirectSndPool  = new ArrayList<>();

    private boolean enalbeUpgrade;
    private SpdyVersion spdyVersion;
    private static UnixSocketConnector connector = null;
    private static String localAddress = null;
    private boolean usetunnel = false;
    private HttpRequestEncoderT requestEncoderT;
    private Map<String, Map<String, List<String>>> proxyFwdBean;

    public TcpTransHandler()
    {
        tunnelService = BeanUtils.getBean("tunnelService");
        serverConfigBean = BeanUtils.getBean("serverConfigBean");
        proxyFwdBean = BeanUtils.getBean("proxyFwdBean");
        requestEncoderT = new HttpRequestEncoderT();
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }
    
    boolean useTunel(ClusterProxyMsg proxyMsg)
    {
        if  (usetunnel)
        {
            return true;
        }
        
        if (StringUtils.isNotEmpty(proxyMsg.getClusterEntity().getClusterCaCrtPem()))
        {
            usetunnel = true;
            return true;
        }
        return false;
    }
    
    void addMsgToPool(ClusterProxyMsg proxyMsg)
    {
        Object msg = proxyMsg.getReqObj();
        
        if (msg instanceof HttpContent)
        {
            ((HttpContent) msg).retain();
        }
        msgPool.add(proxyMsg);
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClusterProxyMsg msg)
    {
        LOGGER.info("channel: {}, recv src req", ctx.channel().hashCode());

        if (clusterEntity == null)
        {
            clusterEntity = msg.getClusterEntity();
        }
        
        if (useTunel(msg))
        {
            if (k8sClientChannel != null && createConnect == true)
            {
                ClusterEntity newClusterEntity = msg.getClusterEntity();
                if (! newClusterEntity.getClusterId().equals(clusterEntity.getClusterId()))
                {
                    clusterEntity = newClusterEntity;
                    replaceSslHandler();
                }
            }
    
            if (k8sClientChannel == null && createConnect == false)
            {
                addMsgToPool(msg);
                clusterEntity = msg.getClusterEntity();
                createChannel(ctx.channel(), msg.getSrcHost(), msg.getClusterEntity());
            }
            else if (k8sClientChannel == null)
            {
                addMsgToPool(msg);
            }
            else if (ready == false)
            {
                addMsgToPool(msg);
            }
            else
            {
                fwdMessage(msg);
            }
        }
        else
        {
            if (clusterEntity != null && clusterEntity.isNetworkTest())
            {
                tunnelService.setChannel2Entity(ctx.channel().id().toString(), clusterEntity);
            }
            directFwd(ctx, msg);
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        LOGGER.info("TransHandler catch exception error, {}", cause.getMessage());

        if (clusterEntity != null && clusterEntity.isNetworkTest())
        {
            tunnelService.delChannel2Entity(ctx.channel().id().toString());
        }
    
        if (connector != null)
        {
            if (k8sClientChannel != null)
            {
                k8sClientChannel.close();
                k8sClientChannel = null;
            }
            connector.releaseSock(localAddress);
        }
    
        msgPool.clear();
        msgDirectSndPool.clear();
    
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
    
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        LOGGER.info("TransHandler inactive");

        if (clusterEntity != null && clusterEntity.isNetworkTest())
        {
            tunnelService.delChannel2Entity(ctx.channel().id().toString());
        }
        if (connector != null)
        {
            if (k8sClientChannel != null)
            {
                k8sClientChannel.close();
                k8sClientChannel = null;
            }
            connector.releaseSock(localAddress);
        }
        
        if (! usetunnel)
        {
            tunnelService.onDirectFwdCloseMessageReq(ctx.channel().toString(), clusterEntity.getWorkderId());
        }
        msgPool.clear();
        msgDirectSndPool.clear();
        ctx.close();
        ctx.fireChannelInactive();
    }
    
    void createChannel(Channel channel, String srcHost,  ClusterEntity clusterEntity)
    {
        LOGGER.info("create channel for dispatch msg. src {}", channel.hashCode());
        localAddress = String.format("/tmp/lnjoying-%s.sock", channel.hashCode());
        try
        {
            if (connector == null)
            {
                connector  = new UnixSocketConnector(domainSocketChannel ->
                {
                    ChannelPipeline pipeline = domainSocketChannel.pipeline();
                    pipeline.addLast("readTimeout" ,new ReadTimeoutHandler(TimeConst.TCP_TIMEOUT, TimeUnit.SECONDS));
                    pipeline.addLast("writeTimeout" ,new WriteTimeoutHandler(TimeConst.TCP_TIMEOUT, TimeUnit.SECONDS));
                    pipeline.addLast("httpEncoder", new HttpRequestEncoder());
                    pipeline.addLast("httpDecoder", new HttpResponseDecoder());
                });
            }
            
            ChannelFuture future =connector.connect(serverConfigBean.getServiceGatewaySock(), localAddress, clientChannel ->
            {
                String address = Inet6AddressUtil.numericToTextFormat(((Socket)((DomainSocketChannel)clientChannel).fd()).localAddress().getAddress().getAddress());
                tunnelService.setAddress2Entity(address, clusterEntity);
                ChannelPipeline pipeline = clientChannel.pipeline();
                if (StringUtils.isNotEmpty(clusterEntity.getClusterCaCrtPem()))
                {
                    SSLEngine engine =  SslContextFactory.cliEngine(clientChannel.alloc(), clusterEntity);
                    if (engine != null)
                    {
                        pipeline.addBefore("readTimeout", "ssl", new SslHandler(engine));
                    }
                }
                
                pipeline.addLast("k8shandler", new K8sClientRespHandler(channel, srcHost));
                
                LOGGER.info("connect success.channel id {}", clientChannel.hashCode());
                k8sClientChannel = clientChannel;
                fwdMsgPool();
                ready = true;
            });
            
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.info("create dispatch msg channel error. {}", e.getMessage());
            return;
        }
        createConnect = true;
    }
    
    void  fwdMsgPool()
    {
        if (! msgPool.isEmpty())
        {
            LOGGER.info("msg pool size {}", msgPool.size());
            ClusterProxyMsg object = msgPool.poll();
            while (object != null)
            {
                fwdMessage(object);
                object = msgPool.poll();
            }
        }
    }
    
    void directFwd(ChannelHandlerContext ctx, ClusterProxyMsg proxyMsg)
    {
        try
        {
            Object msg = proxyMsg.getReqObj();
            DirectMsgEntity msgEntity = new DirectMsgEntity();
            msgEntity.setDataId(ctx.channel().toString());

            ByteBuf msgBuf = Unpooled.wrappedBuffer((byte[]) msg);
            msgEntity.setByteBuf(msgBuf);

            msgEntity.setAddress(String.format("%s:%d", clusterEntity.getClusterIp(), clusterEntity.getClusterPort()));
            msgEntity.setSrcCtx(ctx);
            msgEntity.setSrcHost(proxyMsg.getSrcHost());
            msgEntity.setWorkerId(clusterEntity.getWorkderId());

            tunnelService.onDirectFwdMessageReq(msgEntity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    void fwdMessage(ClusterProxyMsg proxyMsg)
    {
        Object msg = proxyMsg.getReqObj();
        if (null == k8sClientChannel)
        {
            return;
        }
        else
        {
            if (! k8sClientChannel.isActive())
            {
                k8sClientChannel = null;
                createConnect = false;
                ready = false;
                return;
            }
        }
        
        if (msg instanceof HttpRequest)
        {
            HttpRequest req = (HttpRequest) msg;
            LOGGER.info("write first packet. channel {}, msg {}" , k8sClientChannel.hashCode(), req);
            k8sClientChannel.write(req);
            String connection = req.headers().get(HttpHeaderNames.CONNECTION);
            if (StringUtils.isNotBlank(connection))
            {
                if (connection.toUpperCase().contains("UPGRADE"))
                {
                    String dst = req.headers().get(HttpHeaderNames.UPGRADE);
                    dst = dst.toUpperCase();
                    if (! dst.isEmpty())
                    {
                        enalbeUpgrade = true;
                    }
                    if (dst.contains("SPDY"))
                    {
                        spdyVersion = SpdyUtil.getSpdyVersion(dst);
                    }
            
                }
            }
        }
        else if (msg instanceof HttpContent)
        {
            int refCnt  = ((HttpContent) msg).refCnt();
            LOGGER.debug("package ref cnt {}", refCnt);
            
            if (refCnt > 0)
            {
                ((HttpContent) msg).retain();
            }
            
            if (msg instanceof LastHttpContent)
            {
                LOGGER.info("write last packet. channel {}, msg{} ", k8sClientChannel.hashCode(), msg);
                k8sClientChannel.writeAndFlush(msg);
                if (enalbeUpgrade)
                {
                    setTransEncodePipeLine(k8sClientChannel.pipeline());
                }
            }
            else
            {
                LOGGER.info("write middle packet. channel {}, msg {}", k8sClientChannel.hashCode(), msg);
                k8sClientChannel.write(msg);
            }
        }
        else
        {
            LOGGER.info("unknown");
            k8sClientChannel.writeAndFlush(msg);
        }
    }
    
    void setTransEncodePipeLine(ChannelPipeline pipeline)
    {
        pipeline.replace("httpEncoder","byteEncoder", new ByteArrayEncoder());
    }
    
    void replaceSslHandler()
    {
        tunnelService.setAddress2Entity(k8sClientChannel.localAddress().toString(), clusterEntity);
        SSLEngine engine =  SslContextFactory.cliEngine(k8sClientChannel.alloc(), clusterEntity);
        k8sClientChannel.pipeline().replace("ssl", "ssl",  new SslHandler(engine));
    }
}
