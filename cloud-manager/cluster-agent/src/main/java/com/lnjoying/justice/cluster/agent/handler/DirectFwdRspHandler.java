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

public class DirectFwdRspHandler extends SimpleChannelInboundHandler<Object>
{
    private static Logger LOGGER = LogManager.getLogger();
    
    private Channel srcChannel;
    private int msgSeq = 0;
    class SrcCache
    {
        public String dataId;
        public EeCommonDef.msg_header header;
        public EeCommonDef.msg_route route;
    
    }
    
    private SrcCache srcCache  = new SrcCache();;

    public DirectFwdRspHandler(Channel channel, EeCommonDef.msg_route srcRoute)
    {
        super();
        this.srcChannel = channel;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
    
        byte [] pack = (byte[])msg;
        LOGGER.debug("recv ch {} fwd rsp: {}", ctx.channel().hashCode(), Utils.byteToHexString(pack));
        EeNetMessageApi.ee_net_message fwDataRsp = NetMsgBuild.buildDirectFwdRsp(0, pack, srcCache.dataId, srcCache.route, srcCache.header);
        srcChannel.writeAndFlush(fwDataRsp);
        LOGGER.info("k8s rsp :{}", fwDataRsp);
        msgSeq += 1;
    }
    
    public void addSrcInfo(String dataId, EeCommonDef.msg_header header, EeCommonDef.msg_route route)
    {
        srcCache.dataId = dataId;
        srcCache.header = header;
        srcCache.route = route;
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
            String dataId = AgentGlobalInfo.channelIdMapTunnelId.get(channlHash);
            if (StringUtils.isNotBlank(dataId))
            {
                LOGGER.info("catch k8s rsp exception");
                AgentGlobalInfo.channelIdMapTunnelId.remove(channlHash);
                AgentGlobalInfo.channelMap.remove(dataId);
                EeNetMessageApi.ee_net_message fwDataRsp = NetMsgBuild.buildDirectFwdRsp(10000, null, srcCache.dataId, srcCache.route, srcCache.header);
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
            String dataId = AgentGlobalInfo.channelIdMapTunnelId.get(channlHash);
            ctx.close();
    
            if (StringUtils.isNotBlank(dataId))
            {
                LOGGER.info("tunnel id {} is not empty, release it", dataId);
                AgentGlobalInfo.channelIdMapTunnelId.remove(channlHash);
                AgentGlobalInfo.channelMap.remove(dataId);
                if (srcChannel != null && srcChannel.isActive())
                {
                    EeNetMessageApi.ee_net_message fwDataRsp = NetMsgBuild.buildDirectFwdRsp(10001, null, srcCache.dataId, srcCache.route, srcCache.header);
                    srcChannel.writeAndFlush(fwDataRsp);
                }
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
