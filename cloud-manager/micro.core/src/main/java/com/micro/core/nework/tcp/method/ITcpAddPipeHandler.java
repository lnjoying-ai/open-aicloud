package com.micro.core.nework.tcp.method;

import io.netty.channel.socket.SocketChannel;

public interface ITcpAddPipeHandler
{
    public void addHandler(SocketChannel socketChannel);
}
