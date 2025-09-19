package com.micro.core.nework.unix;

import com.micro.core.nework.unix.method.UnixAddPipeHandler;
import com.micro.core.nework.tcp.AbstractITcpProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollDomainSocketChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerDomainSocketChannel;
import io.netty.channel.unix.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class UnixSocketAcceptor extends AbstractITcpProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    private ServerBootstrap bootstrap;
    private Channel acceptorChannel = null;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public UnixSocketAcceptor(UnixAddPipeHandler unixAddPipeHandler)
    {
        bootstrap = new ServerBootstrap();
        bossGroup = new EpollEventLoopGroup();
        workerGroup = new EpollEventLoopGroup();

        bootstrap.group(bossGroup, workerGroup)
                .channel(EpollServerDomainSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_RCVBUF, 100 * 1024)
                .option(ChannelOption.SO_SNDBUF, 100 * 1024)
                .option(EpollChannelOption.SO_REUSEPORT, true)
                .childHandler(initPipeLine(unixAddPipeHandler));
    }

    public ChannelFuture bind(String localAddress) throws Exception
    {
        File file = new File(localAddress);
        if (file.exists()) {
            file.delete();
        }
        ChannelFuture future = bootstrap.bind(new DomainSocketAddress(localAddress)).sync();
        future.addListener(startListener);
        acceptorChannel = future.channel();
        return future;
    }

    @Override
    public void close() throws Exception
    {
        super.close();
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        if (acceptorChannel != null)
        {
            ChannelFuture future = acceptorChannel.close();
            future.addListener(stopListener);
        }
        acceptorChannel = null;
    }

    public void loop()
    {
        try
        {
            acceptorChannel.closeFuture().sync();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                LOGGER.info("unix socket close");
                close();

            }
            catch (Exception e)
            {
                LOGGER.error("close unix socket accept error", e);
            }
        }
    }

    private ChannelInitializer<EpollDomainSocketChannel> initPipeLine(UnixAddPipeHandler unixAddPipeHandler)
    {
        return new ChannelInitializer<EpollDomainSocketChannel>()
        {

            @Override
            protected void initChannel(EpollDomainSocketChannel epollDomainSocketChannel) throws Exception
            {
                String address = Inet6AddressUtil.numericToTextFormat(((Socket) epollDomainSocketChannel.fd()).remoteAddress().getAddress().getAddress());
                LOGGER.info("recv conn from {}", address);
                ChannelPipeline pipeline = epollDomainSocketChannel.pipeline();
                pipeline.addLast("channelcollector", new ChannelCollector());
                unixAddPipeHandler.addHandler(epollDomainSocketChannel);
            }

        };
    }


    private ChannelFutureListener startListener = new ChannelFutureListener() {

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess())
            {
                LOGGER.info("SERVER_START_SUCCESSFULLY");
            }
            else {
                LOGGER.error("SERVER_START_FAILED");
            }
        }
    };

    private ChannelFutureListener stopListener = new ChannelFutureListener() {

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess()) {
                System.out.println("SERVER_STOP_SUCCESSFULLY");
                LOGGER.info("SERVER_STOP_SUCCESSFULLY");
            } else {
                LOGGER.error("SERVER_STOP_FAILED");
            }
        }
    };

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
}
