package com.micro.core.nework.unix.method;

import io.netty.channel.Channel;

public interface UnixConnectListener
{
    //to do sth after connect to peer
    public void addlistener(Channel channel);
}
