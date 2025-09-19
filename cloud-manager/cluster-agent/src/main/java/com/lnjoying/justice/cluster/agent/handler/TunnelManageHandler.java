package com.lnjoying.justice.cluster.agent.handler;

import com.lnjoying.justice.cluster.agent.common.AgentGlobalInfo;
import com.lnjoying.justice.cluster.agent.common.SpringUtil;
import com.lnjoying.justice.cluster.agent.model.NodeConfig;
import com.lnjoying.justice.cluster.agent.netutil.NetMsgBuild;
import com.lnjoying.justice.schema.msg.EeCommonDef;
import com.lnjoying.justice.schema.msg.EeNetMessageApi;
import com.lnjoying.justice.schema.msg.EeNetMessageApi.ee_net_message;
import com.lnjoying.justice.schema.msg.EeTunnelDef;
import com.lnjoying.justice.schema.msg.MessageName;
import com.micro.core.nework.entity.NetEntity;
import com.micro.core.nework.tcp.TcpConnector;
import io.netty.channel.*;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

import static com.lnjoying.justice.cluster.agent.common.TunnelOperName.*;

public class TunnelManageHandler extends SimpleChannelInboundHandler<ee_net_message>
{
    private static Logger LOGGER = LogManager.getLogger();
    
    NodeConfig nodeConfig;
    TcpConnector connector = null;
    
    public TunnelManageHandler()
    {
        nodeConfig = SpringUtil.getBean("nodeConfig");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ee_net_message netMessage) throws Exception
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.info("recv from {} msg: {} channel state:{}", ctx.channel().remoteAddress(), netMessage.toString(), AgentGlobalInfo.channnelState);
        }
        
        if (netMessage.getHead().getMsgName().equals(MessageName.TUNNEL_OPERATOR))
        {
            EeTunnelDef.msg_tunnel_operator_body operatorBody = netMessage.getTunnelOperatorBody();
            switch (operatorBody.getOperatorType())
            {
                case CREATE_TUNNEL_REQ:
                    processCreateTunnel(ctx, operatorBody, netMessage.getHead(), netMessage.getRoute());
                    break;
                case FWD_TUNNEL_DATA_REQ:
                    processFwdReqTunnel(ctx, operatorBody, netMessage.getRoute());
                    break;
                case CLOSE_TUNNEL_REQ:
                    processCloseTunnel(ctx,  operatorBody, netMessage.getHead(), netMessage.getRoute());
                    break;
                case FWD_DATA_REQ:
                    break;
            }
        }
        
        ctx.fireChannelRead(netMessage);
    }
    

    private void processCreateTunnel(ChannelHandlerContext ctx, EeTunnelDef.msg_tunnel_operator_body operatorBody, EeCommonDef.msg_header header, EeCommonDef.msg_route route)
    {
        try
        {
            
            EeTunnelDef.msg_create_tunnel_req_body reqBody = operatorBody.getCreateTunnelReqBody();
            if (AgentGlobalInfo.channelMap.getChannel(reqBody.getTunnelId()) != null)
            {
                ee_net_message rspMsg = NetMsgBuild.buildCreateTunnelRsp(0, reqBody.getTunnelId(), header, route);
                
                ctx.writeAndFlush(rspMsg);
            }
            createChannel(ctx.channel(), reqBody.getAddress(), reqBody.getTunnelId(), header, route);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("hello.");
        }
    }
    
    void createChannel(Channel channel, String address, String tunnId, EeCommonDef.msg_header header, EeCommonDef.msg_route route)
    {
        Channel ch = AgentGlobalInfo.channelMap.getChannel(tunnId);
        
        if (ch != null)
        {
            LOGGER.info("tunnel {} already exist, channel hash {}", tunnId, ch.hashCode());
            EeNetMessageApi.ee_net_message  rsp = NetMsgBuild.buildCreateTunnelRsp(0, tunnId, header, route);
            channel.writeAndFlush(rsp);
            return;
        }
        
        LOGGER.info("create channel for dispatch msg. tunnel {} src {}", tunnId, channel.hashCode());
        NetEntity entity = new NetEntity();
        entity.setChannelType(NetEntity.ChannelType.CLIENT);
        
        if (StringUtils.isNotBlank(nodeConfig.getK8sServiceIP()))
        {
            entity.setHost(nodeConfig.getK8sServiceIP());
            entity.setPort(nodeConfig.getK8sServicePort());
        }
        else
        {
            String [] addressArray = StringUtils.split(address, ":");
            if (addressArray.length < 2)
            {
                return;
            }
            entity.setHost(addressArray[0]);
            entity.setPort(Integer.parseInt(addressArray[1]));
        }
        
        try
        {
            if (connector == null)
            {
                connector  = new TcpConnector(socketChannel ->
                {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast("TunReadTimeHandler", new ReadTimeoutHandler(600, TimeUnit.SECONDS));
                    pipeline.addLast("TunWriteTimeHandler", new WriteTimeoutHandler(600, TimeUnit.SECONDS));
                    pipeline.addLast("bytedecoder", new ByteArrayDecoder());
                    pipeline.addLast("byteEncoder", new ByteArrayEncoder());
                });
            }
           
            
            ChannelFuture future = connector.connect(entity, clientChannel ->
            {
                ChannelPipeline pipeline = clientChannel.pipeline();
                pipeline.addLast(new TunnelRspHandler(channel, tunnId, route));
                LOGGER.info("create tunnel success, channel hash {}, tunnelid {}", clientChannel.hashCode(), tunnId);
            });
            
            Channel k8sch = future.channel();
            AgentGlobalInfo.channelMap.put(tunnId, k8sch);
            AgentGlobalInfo.channelIdMapTunnelId.put(k8sch.hashCode(), tunnId);
            EeNetMessageApi.ee_net_message  rsp = NetMsgBuild.buildCreateTunnelRsp(0, tunnId, header, route);
            LOGGER.info("rsp success create {}", rsp);
            channel.writeAndFlush(rsp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.info("create tunnel {} channel error. {}", tunnId, e.getMessage());
            EeNetMessageApi.ee_net_message  rsp = NetMsgBuild.buildCreateTunnelRsp(5001, tunnId, header, route);
            LOGGER.info("rsp error create {}", rsp);
            channel.writeAndFlush(rsp);
            return;
        }
    }
    
    private void processFwdReqTunnel(ChannelHandlerContext ctx, EeTunnelDef.msg_tunnel_operator_body operatorBody, EeCommonDef.msg_route route)
    {
        EeTunnelDef.msg_fwd_tunnel_data_req_body reqBody = operatorBody.getFwdTunnelDataReqBody();
        String tunnelId = reqBody.getTunnelId();
    
        try
        {
            byte [] data = reqBody.getData().toByteArray();
            Channel channel = AgentGlobalInfo.channelMap.getChannel(tunnelId);
            if (channel != null && channel.isActive())
            {
                LOGGER.info("send tunnel {} data to channel {}", tunnelId, channel.hashCode());
                channel.writeAndFlush(data);
            }
            else
            {
                LOGGER.error("failed to send tunnel {} data. release it", tunnelId);
                EeNetMessageApi.ee_net_message fwDataRsp = null;
                AgentGlobalInfo.channelMap.remove(tunnelId);
                if (channel !=  null)
                {
                    AgentGlobalInfo.channelIdMapTunnelId.remove(channel.hashCode());
                    fwDataRsp = NetMsgBuild.buildTunnelFwdRsp(5002, null, tunnelId, 0, 0, route);
                }
                else
                {
                    fwDataRsp = NetMsgBuild.buildTunnelFwdRsp(5003, null, tunnelId, 0, 0, route);
                }
                ctx.writeAndFlush(fwDataRsp);
    
                channel = null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("fwd data error. tunnel id{}", tunnelId);
        }
    }
    
    private void processCloseTunnel(ChannelHandlerContext ctx, EeTunnelDef.msg_tunnel_operator_body operatorBody, EeCommonDef.msg_header header, EeCommonDef.msg_route route)
    {
        EeTunnelDef.msg_close_tunnel_req_body reqBody = operatorBody.getCloseTunnelReqBody();
        String tunnelId = reqBody.getTunnelId();
        Channel channel = AgentGlobalInfo.channelMap.getChannel(tunnelId);
        if (channel != null)
        {
            AgentGlobalInfo.channelIdMapTunnelId.remove(channel.hashCode());
            AgentGlobalInfo.channelMap.remove(tunnelId);
            LOGGER.info("close tunnel {}, dst channel {}", tunnelId, channel.hashCode());
            ee_net_message rsp = NetMsgBuild.buildCloseTunnelRsp(0, tunnelId, 0, header, route);
            ctx.writeAndFlush(rsp);
            try
            {
                channel.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            LOGGER.info("not find tunnel {} to close", tunnelId);
            ee_net_message rsp = NetMsgBuild.buildCloseTunnelRsp(0, tunnelId, 0, header, route);
            ctx.writeAndFlush(rsp);
        }
    }
    

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        LOGGER.info("main channel inactive. release all tunnel");
       

        try
        {
            if (! AgentGlobalInfo.channelIdMapTunnelId.isEmpty())
            {
                AgentGlobalInfo.channelIdMapTunnelId.forEach((chId,tunnelId) ->{
                    Channel ch = AgentGlobalInfo.channelMap.getChannel(tunnelId);
                    if (ch != null)
                    {
                        ch.close();
                    }
                    AgentGlobalInfo.channelMap.remove(tunnelId);
                });
                AgentGlobalInfo.channelIdMapTunnelId.clear();
        
            }
            connector.close();
            ctx.close();
        }
        catch (Exception e)
        {
            LOGGER.error("channel error, msg:{}" + e.getMessage());
        }
        finally
        {
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
    
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        try
        {
            LOGGER.info("main channel exception caught. release all tunnel. cause:{}", cause.getMessage());
            if (! AgentGlobalInfo.channelIdMapTunnelId.isEmpty())
            {
                
                AgentGlobalInfo.channelIdMapTunnelId.forEach((chId,tunnelId) ->{
                    LOGGER.info("begin to release ch {}  tunnel {}", chId, tunnelId);
                    Channel ch = AgentGlobalInfo.channelMap.getChannel(tunnelId);
                    if (ch != null)
                    {
                        ch.close();
                    }
                    AgentGlobalInfo.channelMap.remove(tunnelId);
                });
                AgentGlobalInfo.channelIdMapTunnelId.clear();
        
            }
            connector.close();
            ctx.close();
        }
        catch (Exception  e)
        {
            e.printStackTrace();
            LOGGER.info("catch tunnel exception {}", e.getMessage());
        }
    }
}
