package com.lnjoying.justice.cluster.agent.handler;

import com.lnjoying.justice.cluster.agent.common.AgentGlobalInfo;
import com.lnjoying.justice.cluster.agent.common.SpringUtil;
import com.lnjoying.justice.cluster.agent.model.NodeConfig;
import com.lnjoying.justice.cluster.agent.netutil.NetMsgBuild;
import com.lnjoying.justice.schema.msg.EeCommonDef;
import com.lnjoying.justice.schema.msg.EeFwdDef;
import com.lnjoying.justice.schema.msg.EeNetMessageApi;
import com.lnjoying.justice.schema.msg.EeNetMessageApi.ee_net_message;
import com.lnjoying.justice.schema.msg.MessageName;
import com.micro.core.common.Utils;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.lnjoying.justice.cluster.agent.common.TunnelOperName.FWD_CLOSE_REQ;
import static com.lnjoying.justice.cluster.agent.common.TunnelOperName.FWD_DATA_REQ;

public class DirectFwdManageHandler extends SimpleChannelInboundHandler<ee_net_message>
{
    private static Logger LOGGER = LogManager.getLogger();
    
    NodeConfig nodeConfig;
    TcpConnector connector = null;
    
    Map<String, DirectFwdRspHandler> directFwdRspHandlerMap = new HashMap<>();
    public DirectFwdManageHandler()
    {
        nodeConfig = SpringUtil.getBean("nodeConfig");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ee_net_message netMessage) throws Exception
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("recv from {} msg: {} channel state:{}", ctx.channel().remoteAddress(), netMessage.toString(), AgentGlobalInfo.channnelState);
        }
        
        if (netMessage.getHead().getMsgName().equals(MessageName.FWD_OPERATOR))
        {
            EeFwdDef.msg_fwd_operator_body operatorBody = netMessage.getFwdOperatorBody();
            switch (operatorBody.getOperatorType())
            {
                case FWD_DATA_REQ:
                    processFwdData(ctx, operatorBody, netMessage.getHead(), netMessage.getRoute());
                    break;
                case FWD_CLOSE_REQ:
                    processFwdClose(operatorBody);
                    break;
            }
        }
        
        ctx.fireChannelRead(netMessage);
    }
    
    Channel getChannel(String dataId)
    {
        Channel channel = AgentGlobalInfo.channelMap.getChannel(dataId);
    
        if (channel != null)
        {
            return channel;
        }
        
        return null;
    }
    
    private void processFwdClose(EeFwdDef.msg_fwd_operator_body operatorBody)
    {
        try
        {
            EeFwdDef.msg_fwd_data_req_body reqBody = operatorBody.getFwdDataReqBody();
            String dataId = reqBody.getDataId();
            LOGGER.info("process fwd close for {}", dataId);
            Channel channel =  getChannel(dataId);
            if (channel == null)
            {
                return;
            }
    
            AgentGlobalInfo.channelIdMapTunnelId.remove(channel.hashCode());
            AgentGlobalInfo.channelMap.remove(dataId);
            if (channel.isActive())
            {
                LOGGER.info("channel is active ,prepare to close it for {}", dataId);
                channel.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void processFwdData(ChannelHandlerContext ctx, EeFwdDef.msg_fwd_operator_body operatorBody, EeCommonDef.msg_header header, EeCommonDef.msg_route route)
    {
        LOGGER.info("process fwd data");
        String dataId = "";
        try
        {
            EeFwdDef.msg_fwd_data_req_body reqBody = operatorBody.getFwdDataReqBody();
            dataId = reqBody.getDataId();
            String address = operatorBody.getFwdDataReqBody().getAddress();
            dataId = operatorBody.getFwdDataReqBody().getDataId();
            Channel channel = getChannel(dataId);
            
            if (channel == null)
            {
                channel = createChannel(ctx.channel(), reqBody, header, route);
                if (channel == null)
                {
                    EeNetMessageApi.ee_net_message fwDataRsp = NetMsgBuild.buildDirectFwdRsp(5001, null, dataId, route, header);
                    ctx.writeAndFlush(fwDataRsp);
                    return;
                }
            }
            else
            {
                DirectFwdRspHandler handler = directFwdRspHandlerMap.get(reqBody.getDataId());
                if (handler != null)
                {
                    handler.addSrcInfo(reqBody.getDataId(), header, route);
                }
                LOGGER.info("direct write msg :{}", Utils.bytesToString(reqBody.getData().toByteArray()));
                channel.writeAndFlush(reqBody.getData().toByteArray());
            }
        }
        catch (Exception e)
        {
            EeNetMessageApi.ee_net_message fwDataRsp = NetMsgBuild.buildDirectFwdRsp(5002, null, dataId, route, header);
            ctx.writeAndFlush(fwDataRsp);
    
            e.printStackTrace();
            LOGGER.error("hello.");
        }
    }
    
    Channel createChannel(Channel channel, EeFwdDef.msg_fwd_data_req_body reqBody,  EeCommonDef.msg_header header,  EeCommonDef.msg_route route)
    {
        String dataId = reqBody.getDataId();
        Channel ch = AgentGlobalInfo.channelMap.getChannel(dataId);
    
        DirectFwdRspHandler handler = directFwdRspHandlerMap.get(reqBody.getDataId());
        if (ch != null && handler != null)
        {
            handler.addSrcInfo(reqBody.getDataId(), header, route);
    
            ch.writeAndFlush(reqBody.getData().toByteArray());
            
            return ch;
        }
        
        LOGGER.info("create channel for dispatch msg. tunnel {} src {}", dataId, channel.hashCode());
        NetEntity entity = new NetEntity();
        entity.setChannelType(NetEntity.ChannelType.CLIENT);
        
        String address = reqBody.getAddress();
    
        String [] addressArray = StringUtils.split(address, ":");
        if (addressArray.length < 2)
        {
            return null;
        }
        
        entity.setHost(addressArray[0]);
        entity.setPort(Integer.parseInt(addressArray[1]));
        
        try
        {
            if (connector == null)
            {
                connector  = new TcpConnector(socketChannel ->
                {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast("TunReadTimeHandler", new ReadTimeoutHandler(120, TimeUnit.SECONDS));
                    pipeline.addLast("TunWriteTimeHandler", new WriteTimeoutHandler(120, TimeUnit.SECONDS));
                    pipeline.addLast("bytedecoder", new ByteArrayDecoder());
                    pipeline.addLast("byteEncoder", new ByteArrayEncoder());
                });
            }
           
            
            ChannelFuture future = connector.connect(entity, clientChannel ->
            {
                ChannelPipeline pipeline = clientChannel.pipeline();
                DirectFwdRspHandler  directFwdRspHandler = new DirectFwdRspHandler(channel, route);
                pipeline.addLast(directFwdRspHandler);
                LOGGER.info("create tunnel success, channel hash {}, address {}  for dataId {}", clientChannel.hashCode(), address, dataId);
                directFwdRspHandler.addSrcInfo(reqBody.getDataId(), header, route);
                LOGGER.info("direct write msg :{}", Utils.bytesToString(reqBody.getData().toByteArray()));
                clientChannel.writeAndFlush(reqBody.getData().toByteArray());
                directFwdRspHandlerMap.put(reqBody.getDataId(), directFwdRspHandler);
            });
            
            
            
            Channel fwdCh = future.await().channel();
            AgentGlobalInfo.channelMap.put(dataId, fwdCh);
            AgentGlobalInfo.channelIdMapTunnelId.put(fwdCh.hashCode(), dataId);
    
            return fwdCh;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.info("create channel {} channel error. {}", dataId, e.getMessage());
            return null;
        }
    }
    

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        LOGGER.info("direct channel inactive. release all tunnel");
       

        try
        {
            if (! AgentGlobalInfo.channelIdMapTunnelId.isEmpty())
            {
                AgentGlobalInfo.channelIdMapTunnelId.forEach((chId,dataId) ->{
                    Channel ch = AgentGlobalInfo.channelMap.getChannel(dataId);
                    if (ch != null)
                    {
                        ch.close();
                    }
                    AgentGlobalInfo.channelMap.remove(dataId);
                });
                
                AgentGlobalInfo.channelIdMapTunnelId.clear();
        
            }
            if (connector != null)
            {
                connector.close();
                ctx.close();
            }
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
            
            if (ctx.channel().isActive())
            {
                LOGGER.info("channel is active, ignore it");
                return;
            }
            
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
            
            if (connector != null)
            {
                connector.close();
            }
            ctx.close();
        }
        catch (Exception  e)
        {
            e.printStackTrace();
            LOGGER.info("catch tunnel exception {}", e.getMessage());
        }
    }
}
