package com.lnjoying.justice.cluster.agent.handler;

import com.lnjoying.justice.cluster.agent.common.AgentGlobalInfo;
import com.lnjoying.justice.cluster.agent.netutil.NetMsgBuild;
import com.lnjoying.justice.schema.msg.EeCommonDef;
import com.lnjoying.justice.schema.msg.EeNetMessageApi;
import com.micro.core.common.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/3/25 14:34
 */

public class TunnelRspHandler extends SimpleChannelInboundHandler<Object>
{
    private static Logger LOGGER = LogManager.getLogger();
    
    private Channel srcChannel;
    private String tunnelId;
    private int msgSeq = 0;
    private EeCommonDef.msg_route srcRoute;

    public TunnelRspHandler(Channel channel, String tunnelId, EeCommonDef.msg_route srcRoute)
    {
        super();
        this.srcChannel = channel;
        this.tunnelId = tunnelId;
        this.srcRoute = srcRoute;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
    
        byte [] pack = (byte[])msg;
        LOGGER.info("recv ch {} tunnel: {}", ctx.channel().hashCode(), Utils.byteToHexString(pack));
        EeNetMessageApi.ee_net_message fwDataRsp = NetMsgBuild.buildTunnelFwdRsp(0, pack, tunnelId, msgSeq, 1, srcRoute);
        srcChannel.writeAndFlush(fwDataRsp);
        LOGGER.info("k8s rsp :{}", fwDataRsp);
        msgSeq += 1;
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        try
        {
            cause.printStackTrace();
            LOGGER.info("K8sRspHandler catch handler error, channel {}, cause {}", ctx.channel().hashCode(), cause.getMessage());
            ctx.close();
            Integer channlHash = ctx.channel().hashCode();
            LOGGER.info("K8sRspHandler handler error, channel hash {}", channlHash);
            String tunnelId = AgentGlobalInfo.channelIdMapTunnelId.get(channlHash);
            if (StringUtils.isNotBlank(tunnelId))
            {
                LOGGER.info("catch k8s rsp exception");
                AgentGlobalInfo.channelIdMapTunnelId.remove(channlHash);
                AgentGlobalInfo.channelMap.remove(tunnelId);
                EeNetMessageApi.ee_net_message fwDataRsp = NetMsgBuild.buildTunnelFwdRsp(0, null, tunnelId, msgSeq, 0, srcRoute);
                srcChannel.writeAndFlush(fwDataRsp);
        
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("halder k8s rsp caught error {}", e.getMessage());
        }

        ctx.fireExceptionCaught(cause);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        LOGGER.info("K8sRspHandler handler inactive, channel {}", ctx.channel().hashCode());
        try
        {
            Integer channlHash = ctx.channel().hashCode();
            String tunnelId = AgentGlobalInfo.channelIdMapTunnelId.get(channlHash);
            ctx.close();
    
            if (StringUtils.isNotBlank(tunnelId))
            {
                LOGGER.info("tunnel id {} is not empty, release it", tunnelId);
                AgentGlobalInfo.channelIdMapTunnelId.remove(channlHash);
                AgentGlobalInfo.channelMap.remove(tunnelId);
                EeNetMessageApi.ee_net_message fwDataRsp = NetMsgBuild.buildTunnelFwdRsp(0, null, tunnelId, msgSeq, 0, srcRoute);
                srcChannel.writeAndFlush(fwDataRsp);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("handler channel inactive for k8s rsp handler error.msg:{}", e.getMessage());
        }
        ctx.fireChannelInactive();
    }
   
}
