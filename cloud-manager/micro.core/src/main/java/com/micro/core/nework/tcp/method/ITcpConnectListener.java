package com.micro.core.nework.tcp.method;

import io.netty.channel.Channel;

public interface ITcpConnectListener
{
    //to do sth after connect to peer
    public void addlistener(Channel channel);
}
