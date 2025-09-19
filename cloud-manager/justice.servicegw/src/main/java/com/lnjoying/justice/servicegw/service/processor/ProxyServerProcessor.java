package com.lnjoying.justice.servicegw.service.processor;

import com.lnjoying.justice.servicegw.common.TimeConst;
import com.lnjoying.justice.servicegw.config.ServerConfigBean;
import com.lnjoying.justice.servicegw.handler.proxyserver.FwdMessageHandler;
import com.micro.core.nework.unix.Inet6AddressUtil;
import com.micro.core.nework.unix.UnixSocketAcceptor;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.unix.Socket;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ProxyServerProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    @Autowired
    ServerConfigBean serverConfigBean;

    private UnixSocketAcceptor acceptor;

    @Override
    public void start()
    {
        LOGGER.info("proxy server processor started");
    }
    
    @Override
    public void stop()
    {
        LOGGER.info("proxy server processor stopped");
    }
    
    @Override
    public void run()
    {
        LOGGER.info("start server");
        if (acceptor == null)
        {
            acceptor = new UnixSocketAcceptor(domainSocketChannel ->
            {
                //////////////////////////////
                String address = Inet6AddressUtil.numericToTextFormat(((Socket) domainSocketChannel.fd()).remoteAddress().getAddress().getAddress());
                LOGGER.info("proxy server recv conn from "+ address);
                ChannelPipeline pipeline = domainSocketChannel.pipeline();
                pipeline.addLast(new ReadTimeoutHandler(TimeConst.TCP_TIMEOUT, TimeUnit.SECONDS));
                pipeline.addLast(new WriteTimeoutHandler(TimeConst.TCP_TIMEOUT, TimeUnit.SECONDS));
                pipeline.addLast("decoder",      new ByteArrayDecoder());
                pipeline.addLast("encoder",      new ByteArrayEncoder());
                pipeline.addLast("fwd", new FwdMessageHandler());
            });
        }
    
        try
        {
            acceptor.bind(serverConfigBean.getServiceGatewaySock());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("ProxyServerProcessor.run error {}", e.getMessage());
        }
        LOGGER.info("start proxy server success.");
        acceptor.loop();
    }
}
