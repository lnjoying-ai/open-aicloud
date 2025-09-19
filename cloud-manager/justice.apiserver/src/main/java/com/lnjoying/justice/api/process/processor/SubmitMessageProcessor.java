package com.lnjoying.justice.api.process.processor;

import com.lnjoying.justice.api.constant.ApiMsgType;
import com.lnjoying.justice.api.constant.RedisCache;
import com.lnjoying.justice.api.rpcserviceimpl.CombRpcService;
import com.lnjoying.justice.api.service.NodeService;
import com.lnjoying.justice.schema.constant.OnlineStatus;
import com.lnjoying.justice.schema.entity.edgeif.EdgeDevIfState;
import com.lnjoying.justice.schema.entity.edgeif.EdgeDevIfStateInfos;
import com.lnjoying.justice.schema.entity.edgeif.EdgeWorkerIfState;
import com.lnjoying.justice.schema.entity.edgeif.EdgeWorkerIfStateInfos;
import com.lnjoying.justice.schema.entity.sys.NodeConfig;
import com.lnjoying.justice.schema.msg.*;
import com.micro.core.common.GlobalInfo;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

@Component
public class SubmitMessageProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    
    NodeConfig nodeConfig  = null;
    
    @Autowired
    CombRpcService combRpcService;
    
    @Autowired
    NodeService nodeService;
    
    @Override
    public void start()
    {
        LOGGER.info("submit message process started");
    }

    @Override
    public void stop()
    {
        LOGGER.info("submit message process stopped");
    }

    @Override
    public void run()
    {
        BlockingQueue<Object> queue = getBlockQueue();
        while (true)
        {
            try
            {
                MessagePack pack = (MessagePack) queue.take();
                switch(pack.getMsgType())
                {
                    case ApiMsgType.SUBMIT_MSG:
                        processSubmitMessage((EdgeMessage) pack.getMessageObj());
                        break;
                    case ApiMsgType.SUBMIT_WORKER_MSG:
                        processSubmitWorkerMessage((WorkerMessage) pack.getMessageObj());
                        break;
                    default:
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exceptionThrown(Exception e)
    {
        return;
    }

    void processSubmitMessage(EdgeMessage edgeMessage)
    {
        try
        {
            if (nodeConfig == null)
            {
                nodeConfig = nodeService.getNodeConfig();
            }
    
    
            if (nodeConfig == null)
            {
                LOGGER.error("node config is null");
                return;
            }
            
            LOGGER.info("process submit message ");

            Channel channel = GlobalInfo.channelMap.getChannel(edgeMessage.getNodeId());
            String dstNodeId = edgeMessage.getNodeId();
            String regionId = "";
            if (channel == null)
            {
                EdgeDevIfStateInfos devIfStates = (EdgeDevIfStateInfos) RedisUtil.oget(RedisCache.EDGE_IF, edgeMessage.getNodeId());
                if (devIfStates == null || devIfStates.getEdgeDevIfStateMap() == null || devIfStates.getEdgeDevIfStateMap().isEmpty())
                {
                    LOGGER.error("dev if state is not connected");
                    return;
                }

                regionId = devIfStates.getRegionId();

                for (Map.Entry<String, EdgeDevIfState> entry : devIfStates.getEdgeDevIfStateMap().entrySet())
                {
                    if (entry.getValue().getStatus() != OnlineStatus.ONLINE)
                    {
                        continue;
                    }

                    channel = getActiveChanel(entry.getKey());
                    if (channel == null)
                    {
                        LOGGER.error("node: {} have no active channel.",edgeMessage.getNodeId());
                        continue;
                    }
                    else
                    {
                        dstNodeId = entry.getKey();
                        break;
                    }
                }
            }

            if (channel == null)
            {
                LOGGER.info("channel is null. {}", edgeMessage.getNodeId());
                return;
            }

            LOGGER.debug("send msg to {}", dstNodeId);

            EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.parseFrom(edgeMessage.getNetMessage());
            EeCommonDef.msg_route route = netMessage.getRoute();
            route =  route.toBuilder().setDNodeId(edgeMessage.getNodeId()).setDRegionId(regionId)
                    .setORegionId(nodeConfig.getRegion_id()).setONodeId(nodeConfig.getNode_id()).build();
            EeCommonDef.msg_header.Builder header  = netMessage.getHead().toBuilder();
            header = header.setMagic(nodeConfig.getMagic_num());
            if (StringUtils.isEmpty(header.getNonce()))
            {
                header = header.setNonce(Utils.assignUUId());
            }

            netMessage = netMessage.toBuilder().setHead(header).setRoute(route).build();

            while (! sendMessage(channel, netMessage))
            {
                channel = freeOldAndGetNewChannel(dstNodeId, channel);
                if (channel == null)
                {
                    return;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("send exception. {}", e.getMessage());
        }
    }

    Channel freeOldAndGetNewChannel(String matchKey, Channel oldChannel)
    {
        if (oldChannel == null)
        {
            return null;
        }
        oldChannel.close();
        GlobalInfo.channelMap.remove(matchKey, oldChannel);
        Channel channel = GlobalInfo.channelMap.getChannel(matchKey);
        return channel;
    }

    Channel getActiveChanel(String nodeId)
    {
        Channel channel = GlobalInfo.channelMap.getChannel(nodeId);
        while (channel != null && ! channel.isActive())
        {
            channel = freeOldAndGetNewChannel(nodeId, channel);
        }

        return channel;
    }

    Boolean sendMessage(Channel channel, EeNetMessageApi.ee_net_message netMessage)
    {
        try
        {
            LOGGER.debug("submit message: {}", netMessage);
            channel.writeAndFlush(netMessage);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.info("send exception." + e);
            return false;
        }
    }

    void processSubmitWorkerMessage(WorkerMessage workerMessage)
    {
        try
        {
            LOGGER.info("process submit message ");

            Channel channel = GlobalInfo.channelMap.getChannel(workerMessage.getWorkerId());
            String dstNodeId = workerMessage.getWorkerId();
            String regionId = "";
            if (channel == null)
            {
                EdgeWorkerIfStateInfos workerIfStates = (EdgeWorkerIfStateInfos) RedisUtil.oget(RedisCache.CLS_WORKER_IF, workerMessage.getWorkerId());
                if (workerIfStates == null)
                {
                    workerIfStates = (EdgeWorkerIfStateInfos) RedisUtil.oget(RedisCache.CMP_WORKER_IF, workerMessage.getWorkerId());
                }
                if (workerIfStates == null)
                {
                    workerIfStates = (EdgeWorkerIfStateInfos) RedisUtil.oget(RedisCache.ECRM_WORKER_IF, workerMessage.getWorkerId());
                }
                if (workerIfStates == null || workerIfStates.getEdgeWorkerIfStateMap() == null || workerIfStates.getEdgeWorkerIfStateMap().isEmpty())
                {
                    LOGGER.error("dev if state is not connected");
                    return;
                }

                regionId = workerIfStates.getRegionId();

                for (Map.Entry<String, EdgeWorkerIfState> entry : workerIfStates.getEdgeWorkerIfStateMap().entrySet())
                {
                    if (entry.getValue().getStatus() != OnlineStatus.ONLINE)
                    {
                        continue;
                    }

                    channel = getActiveChanel(entry.getKey());
                    if (channel == null)
                    {
                        LOGGER.error("worker: {} have no active channel.",workerMessage.getWorkerId());
                        continue;
                    }
                    else
                    {
                        dstNodeId = entry.getKey();
                        break;
                    }
                }
            }

            if (channel == null)
            {
                LOGGER.info("channel is null. {}", workerMessage.getWorkerId());
                return;
            }

            LOGGER.debug("send msg to {}", dstNodeId);

            EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.parseFrom(workerMessage.getNetMessage());
            EeCommonDef.msg_route route = netMessage.getRoute();
            route =  route.toBuilder().setDWorkerId(workerMessage.getWorkerId()).setDRegionId(regionId)
                    .setORegionId(nodeConfig.getRegion_id()).setONodeId(nodeConfig.getNode_id()).build();
            EeCommonDef.msg_header.Builder header  = netMessage.getHead().toBuilder();
            header = header.setMagic(nodeConfig.getMagic_num());
            if (StringUtils.isEmpty(header.getNonce()))
            {
                header = header.setNonce(Utils.assignUUId());
            }

            netMessage = netMessage.toBuilder().setHead(header).setRoute(route).build();

            while (! sendMessage(channel, netMessage))
            {
                channel = freeOldAndGetNewChannel(dstNodeId, channel);
                if (channel == null)
                {
                    return;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("send exception. {}", e.getMessage());
        }
    }
}
