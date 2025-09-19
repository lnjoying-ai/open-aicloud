package com.micro.core.nework.tcp.method;

import io.netty.channel.Channel;

public interface ITcpProcess
{
    //close channel
    public void close(Channel channel) throws Exception;

    //close all channel
    public void close()throws Exception;

    //fetch a channel from channel pool
    Channel fetch();

    //get connection Num
    int getConnectionNum();

    public Channel[] getallChannel();
    public void addChannel(Channel ch);
    public void removeChannel(Channel ch);
}
