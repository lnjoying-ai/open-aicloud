package com.lnjoying.justice.cluster.agent.net;

import com.lnjoying.justice.cluster.agent.handler.ProbeHandler;
import com.micro.core.nework.entity.NetEntity;
import com.micro.core.nework.tcp.TcpAcceptor;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 2/15/22 5:44 PM
 */
@Service("agentSvrAcceptor")
public class AgentSvrAcceptor
{
    private static Logger LOGGER = LogManager.getLogger();
    
    public void start()
    {
        LOGGER.info("start acceptor server");

        TcpAcceptor acceptor = new TcpAcceptor(socketChannel ->
        {
            LOGGER.info("recv conn from "+socketChannel.remoteAddress().toString());
            ChannelPipeline pipeline = socketChannel.pipeline();
            pipeline.addLast(new ReadTimeoutHandler(60,TimeUnit.SECONDS));
            pipeline.addLast(new WriteTimeoutHandler(60, TimeUnit.SECONDS));
            pipeline.addLast("decoder", new HttpRequestDecoder());
            pipeline.addLast("encoder", new HttpResponseEncoder());
            pipeline.addLast("probe", new ProbeHandler());
        });
        try
        {
            acceptor.bind(new NetEntity("0.0.0.0", 8080, NetEntity.ChannelType.SERVER));
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
        LOGGER.info("start acceptor success.");
        acceptor.loop();
    }
    
}
