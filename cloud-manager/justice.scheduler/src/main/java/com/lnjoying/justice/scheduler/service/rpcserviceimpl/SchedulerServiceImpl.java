package com.lnjoying.justice.scheduler.service.rpcserviceimpl;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.lnjoying.justice.scheduler.common.constant.SchedulerMsgType;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.scheduler.process.service.SchedulerProcessStrategy;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.scheduler.scheduler.framework.noderesources.ResourcesUtil;
import com.lnjoying.justice.schema.entity.scheduler.AssignEdge2InstReq;
import com.lnjoying.justice.schema.entity.scheduler.AssignEdge2StackReq;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.lnjoying.justice.schema.msg.scheduler.AssignEdge2ClusterReq;
import com.lnjoying.justice.schema.service.scheduler.SchedulerService;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;

@RpcSchema(schemaId = "schedulerService")
public class SchedulerServiceImpl implements SchedulerService
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    SchedulerProcessStrategy schedulerProcessStrategy;

    @Autowired
    ResourcesUtil resourcesUtil;

    private static final int size = 1000000; //100w

    private static BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), size, 0.0001);

    @Override
    public Integer allocEdge2InstResources(@ApiParam(name = "assignEdge") AssignEdge2InstReq req)
    {
        LOGGER.info("req target node for inst: {}", req);
        MessagePack messagePack = new MessagePack();
        messagePack.setMsgType(SchedulerMsgType.INST);
        messagePack.setMessageObj(req);
        schedulerProcessStrategy.sendMessage(messagePack);
        return SchedulerState.WAITING_SCHEDULING.getCode();
    }

    @Override
    public Integer allocEdge2StackResources(@ApiParam(name = "assignEdge") AssignEdge2StackReq req)
    {
        LOGGER.info("req target node for stack: {}", req);
        MessagePack messagePack = new MessagePack();
        messagePack.setMsgType(SchedulerMsgType.STACK);
        messagePack.setMessageObj(req);
        schedulerProcessStrategy.sendMessage(messagePack);
        return SchedulerState.WAITING_SCHEDULING.getCode();
    }

    @Override
    public Boolean releaseBindResources(@ApiParam(name = "nodeId") String nodeId, @ApiParam(name = "refId") String refId)
    {
        LOGGER.info("release bind resources. node: {}, ref: {}", nodeId, refId);
        resourcesUtil.releaseResources(nodeId, refId);
        resourcesUtil.updateEdgeMonopolyStatusToRemove(nodeId, refId);
        return true;
    }
    
    @Override
    public CombRetPacket allockEdge2ClusterResources(@ApiParam(name = "assignEdge") AssignEdge2ClusterReq req)
    {
        LOGGER.info("req target node for cluster: {}", req);

        MessagePack messagePack = new MessagePack();
        messagePack.setMsgType(SchedulerMsgType.CLUSTER);
        messagePack.setMessageObj(req);
        schedulerProcessStrategy.sendMessage(messagePack);

        return new CombRetPacket();
    }
    
    @Override
    public String test()
    {
        return "tttt";
    }
}
