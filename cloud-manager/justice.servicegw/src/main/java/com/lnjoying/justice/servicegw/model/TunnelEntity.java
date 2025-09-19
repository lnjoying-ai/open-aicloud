package com.lnjoying.justice.servicegw.model;

import com.lnjoying.justice.servicegw.common.TunnelState;
import com.lnjoying.justice.schema.entity.cluster.ClusterEntity;
import io.netty.channel.Channel;
import lombok.Data;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 2/26/22 12:41 PM
 */
@Data
public class TunnelEntity
{
    private String tunnelId;
    private int    svrChannelId;
    private TunnelState tunnelState;
    private ClusterEntity clusterEntity;
    private Channel authChannel;
    private long creatTime;
    private long closeTime;
    private Queue<Object> msgPool = new LinkedBlockingDeque<>();
    public void addMsg(Object obj)
    {
        msgPool.add(obj);
    }
    
    public synchronized Object getMsg()
    {
        return msgPool.poll();
    }
    
    public void clearMsg()
    {
        msgPool.clear();
    }
}



