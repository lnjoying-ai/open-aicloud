package com.lnjoying.justice.schema.service.ecrm;

import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.CombRetPbPacket;
import com.lnjoying.justice.schema.entity.scheduler.BindResourcesResult;
import com.lnjoying.justice.schema.entity.scheduler.ClusterBindNode;
import com.lnjoying.justice.schema.entity.scheduler.SchedulerResult;
import com.lnjoying.justice.schema.entity.servicemanager.NodeInfo;
import com.lnjoying.justice.schema.msg.EdgeMessage;
import com.micro.core.common.Pair;
import io.swagger.annotations.ApiParam;

import java.util.List;

public interface EdgeResourceService
{
	int deliver(@ApiParam(name = "message") EdgeMessage edgeMessage);
	Pair<Integer, Integer> edgeReg(@ApiParam(name = "message") EdgeMessage edgeMessage);
	int gwReg(@ApiParam(name = "message")   EdgeMessage edgeMessage, @ApiParam(name = "remoteIP") String remoteIp);
	CombRetPbPacket gwLogin(@ApiParam(name = "message") EdgeMessage edgeMessage, @ApiParam(name = "remoteIP") String remoteIp);
	int gwDisConnect(@ApiParam(name = "gwNodeId") String nodeId);
	int edgeDisConnect(@ApiParam(name = "edgeNodeId") String nodeId);
	CombRetPacket edgeRemoteLogin(@ApiParam(name = "message") EdgeMessage edgeMessage);
	List<ClusterBindNode> fillClusterBindNodeField(@ApiParam(name = "clusterBindNodes") List<ClusterBindNode> clusterBindNodes);
	String getOnlineGwByRegion(@ApiParam(name = "regionId") String regionId);
	Pair<Integer, Integer> getNodeStatus(@ApiParam(name = "nodeId") String nodeId);
	BindResourcesResult bindResources(@ApiParam(name = "schedulerResult") SchedulerResult schedulerResult);
	void updateServiceAgentStatus(@ApiParam(name = "agentId") String agentId, @ApiParam(name = "status") Integer status);
	CombRetPacket getServiceAgent(@ApiParam(name = "siteId")String siteId);
	NodeInfo getNode(@ApiParam(name = "nodeId")String nodeId);
	int workerReg(@ApiParam(name = "message") EdgeMessage edgeMessage);
	int workerLogin(@ApiParam(name = "message") EdgeMessage edgeMessage);
}
