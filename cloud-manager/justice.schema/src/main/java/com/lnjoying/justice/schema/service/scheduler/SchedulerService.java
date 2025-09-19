package com.lnjoying.justice.schema.service.scheduler;

import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.entity.scheduler.AssignEdge2InstReq;
import com.lnjoying.justice.schema.entity.scheduler.AssignEdge2StackReq;
import com.lnjoying.justice.schema.msg.scheduler.AssignEdge2ClusterReq;
import io.swagger.annotations.ApiParam;

public interface SchedulerService
{
    Integer allocEdge2InstResources(@ApiParam(name = "assignEdge") AssignEdge2InstReq req);
    Integer allocEdge2StackResources(@ApiParam(name = "assignEdge") AssignEdge2StackReq req);
    Boolean releaseBindResources(@ApiParam(name = "nodeId") String nodeId, @ApiParam(name = "refId") String refId);
    CombRetPacket allockEdge2ClusterResources(@ApiParam(name = "assignEdge") AssignEdge2ClusterReq req);
    String test();
}
