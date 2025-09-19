package com.micro.core.nework.unix;

import com.micro.core.nework.unix.method.UnixAddPipeHandler;
import com.micro.core.nework.unix.method.UnixConnectListener;
import com.micro.core.nework.tcp.AbstractITcpProcessor;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollDomainSocketChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.channel.unix.DomainSocketChannel;
import io.netty.channel.unix.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class UnixSocketConnector extends AbstractITcpProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    private Bootstrap bootstrap;
    private  EventLoopGroup workerGroup;


    public UnixSocketConnector(UnixAddPipeHandler unixAddPipeHandler)
    {
        bootstrap = new Bootstrap();
        workerGroup = new EpollEventLoopGroup();
        bootstrap.group(workerGroup)
                .channel(EpollDomainSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_RCVBUF, 100 * 1024)
                .option(ChannelOption.SO_SNDBUF, 100 * 1024)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(initPipeLine(unixAddPipeHandler));
    }

    public ChannelFuture connect(String remoteAddress, String localAddress, UnixConnectListener listener) throws Exception
    {
        ChannelFuture future = bootstrap.connect(new DomainSocketAddress(remoteAddress), new DomainSocketAddress(localAddress));

        future.addListener(new ChannelFutureListener()
        {
            public void operationComplete(ChannelFuture f) throws Exception
            {
                if (f.isSuccess())
                {
                    Socket domainSocketAddress = (Socket)((DomainSocketChannel)f.channel()).fd();
                    LOGGER.info("connect success. Remote: {} Local: {}",
                            Inet6AddressUtil.numericToTextFormat(domainSocketAddress.remoteAddress().getAddress().getAddress()),
                            Inet6AddressUtil.numericToTextFormat(domainSocketAddress.localAddress().getAddress().getAddress()));
                    listener.addlistener(f.channel());
                }
            }
        });

        try
        {
            future.sync();
        }
        catch (InterruptedException e)
        {

            e.printStackTrace();
        }
        return future;
    }

    private ChannelFutureListener channelStopListener = new ChannelFutureListener()
    {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception
        {
            LOGGER.info("CHANNEL_CLOSED");
        }
    };

    private ChannelInitializer<DomainSocketChannel> initPipeLine(UnixAddPipeHandler unixAddPipeHandler)
    {

        return new ChannelInitializer<DomainSocketChannel>()
        {

            @Override
            protected void initChannel(DomainSocketChannel domainSocketChannel) throws Exception
            {
                ChannelPipeline pipeline = domainSocketChannel.pipeline();
                pipeline.addLast("channelcollector", new ChannelCollector());
                unixAddPipeHandler.addHandler(domainSocketChannel);
            }
        };
    }

    private class ChannelCollector extends ChannelDuplexHandler
    {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception
        {
            // 把连接加入数组
            addChannel(ctx.channel());
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception
        {
            removeChannel(ctx.channel());
            try
            {
                ctx.fireChannelInactive();
            }
            catch (Exception e)
            {
                LOGGER.error("error");
            }

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        {
            removeChannel(ctx.channel());
            try
            {
                ctx.fireExceptionCaught(cause);
            }
            catch (Exception e)
            {
                LOGGER.error("catch exception");
            }
        }
    }

    public void releaseSock(String localAddress)
    {
        File file = new File(localAddress);
        if (file.exists()) {
            file.delete();
        }
    }
}
