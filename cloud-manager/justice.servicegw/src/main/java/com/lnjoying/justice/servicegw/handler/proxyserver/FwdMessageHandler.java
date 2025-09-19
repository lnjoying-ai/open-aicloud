package com.lnjoying.justice.servicegw.handler.proxyserver;

import com.lnjoying.justice.servicegw.common.TunnelState;
import com.lnjoying.justice.servicegw.model.TunnelEntity;
import com.lnjoying.justice.servicegw.service.ServicePortService;
import com.lnjoying.justice.servicegw.service.TunnelService;
import com.lnjoying.justice.schema.entity.cluster.ClusterEntity;
import com.micro.core.common.Utils;
import com.micro.core.nework.unix.Inet6AddressUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.channel.unix.DomainSocketChannel;
import io.netty.channel.unix.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;

import java.net.SocketAddress;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 2/16/22 7:54 PM
 */
public class FwdMessageHandler extends SimpleChannelInboundHandler<Object>
{
    private static Logger LOGGER = LogManager.getLogger();
    TunnelEntity tunnelEntity = null;
    
    TunnelService tunnelService;

    ServicePortService servicePortService;

    String remoteAddress = null;
    
    public FwdMessageHandler()
    {
        tunnelService = BeanUtils.getBean("tunnelService");
        servicePortService = BeanUtils.getBean("servicePortService");
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
    
        LOGGER.info("catch FwdMessageHandler response exception error. {}", cause.getMessage());
        tunnelService.delAddress2Entity(remoteAddress);
        ctx.close();
        if (tunnelEntity == null)
        {
            LOGGER.error("no tunnel for {}", ctx.channel().hashCode());
            ctx.fireExceptionCaught(cause);
            return;
        }
    
        if (tunnelEntity.getTunnelState().getCode() == TunnelState.CLOSED.getCode())
        {
            LOGGER.info("tunnel {} has been closed", tunnelEntity.getTunnelId());
            return;
        }
    
        LOGGER.info("channel {} inactive, close tunnel {}", ctx.channel().hashCode(), tunnelEntity.getTunnelId());
        tunnelService.onCloseTunnelReq(tunnelEntity);
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        LOGGER.info("fwd channel active. id {}", ctx.channel().hashCode());
        if (tunnelEntity == null)
        {
            Socket socket = (Socket)((DomainSocketChannel)ctx.channel()).fd();
            remoteAddress = Utils.bytesToString(socket.remoteAddress().getAddress().getAddress());

            if (remoteAddress.contains("u"))
            {
                remoteAddress =  remoteAddress.replace("u","");
            }

            if (remoteAddress.contains(".so"))
            {
                remoteAddress =  remoteAddress.substring(0, remoteAddress.indexOf(".so"));
            }
            LOGGER.info("remote channel {}, local channel {}",  remoteAddress, ctx.channel().hashCode());
            ClusterEntity clusterEntity = tunnelService.getClusterEntity(remoteAddress);
    
            if (clusterEntity == null)
            {
                LOGGER.info("ohh this entity in not exist", ctx.channel().hashCode());
                return;
            }
            tunnelEntity = new TunnelEntity();
            tunnelEntity.setTunnelId(String.format("%s_%s", servicePortService.getMicroserviceInstance().getEndpoints().get(0).replace("rest://", ""), Utils.assignUUId()));
        
            tunnelEntity.setAuthChannel(ctx.channel());
            tunnelEntity.setSvrChannelId(ctx.channel().hashCode());
            tunnelEntity.setClusterEntity(clusterEntity);
            tunnelEntity.setCreatTime(System.currentTimeMillis());
            tunnelService.setTunnelEntityMap(tunnelEntity);
            tunnelEntity.setTunnelState(TunnelState.CREATING);
    
            tunnelService.onCreateTunnelReq(tunnelEntity);
            LOGGER.info("create tunnel {} for channel {}", tunnelEntity.getTunnelId(), ctx.channel().hashCode());
        }
        else
        {
            LOGGER.info("tunnel entity is not null {}", tunnelEntity.toString());
        }
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
    {
        if (tunnelEntity == null)
        {
            LOGGER.info("ohh this tunnelEntity in not exist {}", ctx.channel().hashCode());
            return;
        }
        
        tunnelEntity.addMsg(msg);
        if (tunnelEntity.getTunnelState().getCode() == TunnelState.ACTIVE.getCode())
        {
            LOGGER.info("on read: tunnel {} is ready,fwd", tunnelEntity.getTunnelId());
            tunnelService.onFwdTunnelMessageReq(tunnelEntity);
        }
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        LOGGER.info("FwdMessageHandler inactive");
        tunnelService.delAddress2Entity(remoteAddress);
        ctx.close();
        if (tunnelEntity == null)
        {
            LOGGER.error("no tunnel for {}, maybe has been quit", ctx.channel().hashCode());
            return;
        }
        
        if (tunnelEntity.getTunnelState().getCode() == TunnelState.CLOSED.getCode())
        {
            LOGGER.info("tunnel {} has been closed", tunnelEntity.getTunnelId());
            return;
        }
        LOGGER.info("channel {} inactive, close tunnel {}", ctx.channel().hashCode(), tunnelEntity.getTunnelId());
        tunnelService.onCloseTunnelReq(tunnelEntity);
    }
}
