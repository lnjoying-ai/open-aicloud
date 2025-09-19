package com.micro.core.common;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ChannelMap
{
    Map<String, ChannelList> channelMap = new HashMap<>();


    public  void put(String nodeId, Channel channel)
    {
        try
        {
            ChannelList channels = channelMap.get(nodeId);
            if (channels == null)
            {
                channels = new ChannelList();
                channelMap.put(nodeId, channels);
            }
            channels.add(channel);
        }
        finally
        {
        }
    }

    public void remove(String nodeId)
    {
        channelMap.remove(nodeId);
    }

    public void remove(String nodeId, Channel channel)
    {
        ChannelList channels = channelMap.get(nodeId);
        if (channels == null)
        {
            return;
        }

        if (channels.getCount() > 0)
        {
            channels.remove(channel);
        }

        if (channels.getCount() < 1 || channels == null)
        {
            remove(nodeId);
        }
    }

    public Channel getChannel(String nodeId)
    {
        ChannelList channels = channelMap.get(nodeId);
         if (null == channels || channels.getCount() < 1)
         {
             return  null;
         }
         return channels.fetch();
    }

    public  int getChannelCount(String nodeId)
    {
        try
        {
            ChannelList channels = channelMap.get(nodeId);
            int count = 0;

            if (null != channels)
            {
                count = channels.getCount();
            }
            return count;
        }
        finally
        {
        }
    }
}
