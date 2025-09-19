package com.micro.core.nework.tcp;

import com.micro.core.nework.entity.NetEntity;
import com.micro.core.nework.tcp.method.ITcpAddPipeHandler;
import com.micro.core.nework.tcp.method.ITcpConnectListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TcpConnector extends AbstractITcpProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    private Bootstrap bootstrap;
    private  EventLoopGroup workerGroup;


    public TcpConnector(ITcpAddPipeHandler tcpAddPipeHandler)
    {
        bootstrap = new Bootstrap();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        bootstrap.group(workerGroup)
                 .channel(NioSocketChannel.class)
                 .option(ChannelOption.TCP_NODELAY, true)
                 .option(ChannelOption.SO_REUSEADDR, true)
                 .option(ChannelOption.SO_RCVBUF, 100 * 1024)
                 .option(ChannelOption.SO_SNDBUF, 100 * 1024)
                 .option(ChannelOption.SO_KEEPALIVE, true)
                 .handler(initPipeLine(tcpAddPipeHandler));
    }


    public ChannelFuture connect(NetEntity entity, ITcpConnectListener listener) throws Exception
    {
        ChannelFuture future = bootstrap.connect(entity.getHost(), entity.getPort());

        future.addListener(new ChannelFutureListener()
        {
            public void operationComplete(ChannelFuture f) throws Exception
            {
                if (f.isSuccess())
                {
                    LOGGER.info("connect success. Remote: {} Local: {}",
                            f.channel().remoteAddress().toString(), f.channel().localAddress().toString());
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

    private ChannelInitializer<SocketChannel> initPipeLine(ITcpAddPipeHandler tcpAddPipeHandler)
    {

        return new ChannelInitializer<SocketChannel>()
        {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception
            {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast("channelcollector", new TcpConnector.ChannelCollector());
                tcpAddPipeHandler.addHandler(socketChannel);
                addChannel(socketChannel);
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
}
