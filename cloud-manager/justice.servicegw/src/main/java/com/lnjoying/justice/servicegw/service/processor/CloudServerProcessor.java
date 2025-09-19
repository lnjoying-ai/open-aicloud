package com.lnjoying.justice.servicegw.service.processor;

import com.lnjoying.justice.servicegw.common.TimeConst;
import com.lnjoying.justice.servicegw.config.ServerConfigBean;
import com.lnjoying.justice.servicegw.handler.cloudserver.DispathHandler;
import com.lnjoying.justice.servicegw.service.rpc.CombRpcService;
import com.micro.core.nework.entity.NetEntity;
import com.micro.core.nework.tcp.TcpAcceptor;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CloudServerProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    @Autowired
    ServerConfigBean serverConfigBean;
    
    private  TcpAcceptor acceptor;

    @Autowired
    private CombRpcService combRpcService;
    
    @Override
    public void start()
    {
        LOGGER.info("cloud server processor started");
    }
    
    @Override
    public void stop()
    {
        LOGGER.info("cloud server processor stopped");
    }
    
    @Override
    public void run()
    {
        LOGGER.info("start server");
        int innerPort =serverConfigBean.getCloudsvrPort();

        if (acceptor == null)
        {
            acceptor = new TcpAcceptor(socketChannel ->
            {
                //////////////////////////////
                LOGGER.info("cloud server recv conn from "+socketChannel.remoteAddress().toString());
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new ReadTimeoutHandler(TimeConst.TCP_TIMEOUT, TimeUnit.SECONDS));
                pipeline.addLast(new WriteTimeoutHandler(TimeConst.TCP_TIMEOUT, TimeUnit.SECONDS));
                pipeline.addLast("httpDecoder", new HttpRequestDecoder());
                pipeline.addLast("aggregator", new HttpObjectAggregator(5*1024*1024)); // 上传限制5M
                pipeline.addLast("httpEncoder", new HttpResponseEncoder());
                pipeline.addLast("dispatch", new DispathHandler(combRpcService));

            });
        }

        try
        {
            acceptor.bind(new NetEntity("0.0.0.0", innerPort, NetEntity.ChannelType.SERVER));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("CloudServerProcessor.run error {}", e.getMessage());
        }
        LOGGER.info("start cloud server success.");
        acceptor.loop();
    }
}
