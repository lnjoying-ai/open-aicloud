package com.micro.core.nework.tcp;

import com.micro.core.nework.entity.NetEntity;
import com.micro.core.nework.tcp.method.ITcpAddPipeHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultiPortTcpAcceptor extends AbstractITcpProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Map<Integer, Channel> acceptorChannelMap = new ConcurrentHashMap<>();

    public MultiPortTcpAcceptor(ITcpAddPipeHandler tcpAddPipeHandler)
    {
        bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        bootstrap.group(bossGroup, workerGroup)
                 .channel(NioServerSocketChannel.class)
                 .option(ChannelOption.SO_BACKLOG, 100)
                 .option(ChannelOption.TCP_NODELAY, true)
                 .option(ChannelOption.SO_REUSEADDR, true)
                 .option(ChannelOption.SO_RCVBUF, 100 * 1024)
                 .option(ChannelOption.SO_SNDBUF, 100 * 1024)
                 .option(EpollChannelOption.SO_REUSEPORT, true)
                 .childHandler(initPipeLine(tcpAddPipeHandler));
    }

    public ChannelFuture bind(NetEntity entity) throws Exception
    {
        ChannelFuture future = null;

        if (entity.getHost() == null)
        {
            future = bootstrap.bind(entity.getPort()).sync();
        }
        else
        {
            future = bootstrap.bind(entity.getHost(), entity.getPort()).sync();
        }

        future.addListener(startListener);
        ChannelFuture finalFt = future;
        future.channel().closeFuture().addListener((ChannelFutureListener) channelFuture -> finalFt.channel().close());
        acceptorChannelMap.put(future.hashCode(), future.channel());
        return future;
    }

    @Override
    public void close() throws Exception
    {
        super.close();
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();

        if (! acceptorChannelMap.isEmpty())
        {
            for (Map.Entry<Integer, Channel> entry : acceptorChannelMap.entrySet())
            {
                ChannelFuture future = entry.getValue().close();
                future.addListener(stopListener);
            }
            acceptorChannelMap.clear();
        }
    }

    public void removeChannel(Integer chHashCode)
    {
        Channel ch = acceptorChannelMap.get(chHashCode);
        if (ch == null)
        {
            return;
        }
        ChannelFuture future = ch.close();
        future.addListener(stopListener);

        acceptorChannelMap.remove(chHashCode);
    }

    private ChannelInitializer<SocketChannel> initPipeLine(ITcpAddPipeHandler tcpAddPipeHandler)
    {
        return new ChannelInitializer<SocketChannel>()
        {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception
            {
                LOGGER.info("recv conn from {}", socketChannel.remoteAddress().toString());
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast("channelcollector", new ChannelCollector());
                tcpAddPipeHandler.addHandler(socketChannel);
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
