package com.lnjoying.justice.cluster.agent.net;

import com.lnjoying.justice.cluster.agent.common.AgentGlobalInfo;
import com.lnjoying.justice.cluster.agent.handler.DirectFwdManageHandler;
import com.lnjoying.justice.cluster.agent.handler.SessionManageHandler;
import com.lnjoying.justice.cluster.agent.handler.TunnelManageHandler;
import com.lnjoying.justice.cluster.agent.model.NodeConfig;
import com.lnjoying.justice.cluster.agent.netutil.NetMsgBuild;
import com.lnjoying.justice.schema.msg.EeNetMessageApi;
import com.micro.core.nework.entity.NetEntity;
import com.micro.core.nework.tcp.TcpConnector;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 2/15/22 5:44 PM
 */
@Service
public class AgentSvrConnector
{
    private static Logger LOGGER = LogManager.getLogger();
    
    public AgentSvrConnector()
    {
        LOGGER.info("agent init");
    }
    
    TcpConnector connector = null;

    public void connect(NodeConfig nodeConfig, List<NetEntity> gwCandidates)
    {
        String ip = nodeConfig.getPeer_ip();
        if (StringUtils.isBlank(ip))
        {
            LOGGER.error("peer ip is blank");
            return;
        }
        
        Integer port = nodeConfig.getPeer_port();
        int regFlag = nodeConfig.getRegFlag();
        LOGGER.info("start agent connector");
        NetEntity entity = new NetEntity();
        entity.setChannelType(NetEntity.ChannelType.CLIENT);
        entity.setHost(ip);
        entity.setPort(port);
    
        try
        {
            if (connector == null)
            {
                connector  = new TcpConnector(socketChannel ->
                {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new ReadTimeoutHandler(600,  TimeUnit.SECONDS));
                    pipeline.addLast(new WriteTimeoutHandler(600, TimeUnit.SECONDS));
                    pipeline.addLast("serverLog", new LoggingHandler(LogLevel.INFO));
                    pipeline.addLast("FrameDecoder", new LengthFieldBasedFrameDecoder(10 * 10240000 , 0, 4, 0, 4, true));
                    pipeline.addLast("FrameEncoder", new LengthFieldPrepender(4));
                    pipeline.addLast("decoder",      new ProtobufDecoder(EeNetMessageApi.ee_net_message.getDefaultInstance()));
                    pipeline.addLast("encoder",      new ProtobufEncoder());
                    pipeline.addLast("sessionLogin", new SessionManageHandler());
                    pipeline.addLast("tunnel", new TunnelManageHandler());
                    pipeline.addLast("directFwd", new DirectFwdManageHandler());
                });
            }
            
            boolean ret  = connect(entity, regFlag, nodeConfig);
            
            if (! ret || ! AgentGlobalInfo.gConnectChanel.isActive())
            {
                if (gwCandidates == null)
                {
                    return;
                }
                
                for (NetEntity e : gwCandidates)
                {
                    if (connect(e, regFlag, nodeConfig) && AgentGlobalInfo.gConnectChanel.isActive())
                    {
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("create connector msg channel error. {}", e.getMessage());
            return;
        }
    }
    
    boolean connect(NetEntity entity, int regFlag, NodeConfig nodeConfig)
    {
        ChannelFuture future = null;
        try
        {
            future = connector.connect(entity, clientChannel ->
            {
                LOGGER.info("connect success.channel id {}", clientChannel.hashCode());
            
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        
        AgentGlobalInfo.gConnectChanel = future.channel();
        if (regFlag != 1)
        {
            EeNetMessageApi.ee_net_message workerRegMsg =  NetMsgBuild.buildWorkerRegMessage(nodeConfig);
            LOGGER.info("send msg: {} ", workerRegMsg);
            AgentGlobalInfo.gConnectChanel.writeAndFlush(workerRegMsg);
        }
        else
        {
            LOGGER.info("node config {}", nodeConfig);
            EeNetMessageApi.ee_net_message workerLoginMsg =  NetMsgBuild.buildWorkerLoginMessage(nodeConfig);
            LOGGER.info("send msg: {} ", workerLoginMsg);
            AgentGlobalInfo.gConnectChanel.writeAndFlush(workerLoginMsg);
        }
        return true;
    }
}
