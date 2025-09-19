package com.lnjoying.justice.schema.service.cis;

import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.entity.scheduler.BindResourcesResult;
import com.lnjoying.justice.schema.entity.scheduler.SchedulerResult;
import com.lnjoying.justice.schema.entity.servicemanager.TargetServiceInfo;
import com.lnjoying.justice.schema.entity.stat.GetStatusMetricsRsp;
import com.lnjoying.justice.schema.msg.EdgeMessage;
import com.lnjoying.justice.schema.entity.scheduler.DstNode;
import io.swagger.annotations.ApiParam;

import java.util.List;

public interface ContainerInstService
{
	CombRetPacket deliver(@ApiParam(name = "message") EdgeMessage edgeMessage);

	void evacuateEdge(@ApiParam(name = "nodeId") String nodeId);
	
	void setContainer(@ApiParam(name = "instId") String instId,
					  @ApiParam(name = "instName") String instName,
					  @ApiParam(name = "imageName") String imageName,
					  @ApiParam(name = "cmd") String cmd,
					  @ApiParam(name = "refId") String refId,
					  @ApiParam(name = "status") int status,
					  @ApiParam(name = "userId") String userId,
					  @ApiParam(name = "bpId") String bpId,
					  @ApiParam(name = "dstNode") DstNode dstNode);

	void setContainerStatus(@ApiParam(name = "instId") String instId,
					  @ApiParam(name = "status") int status);

	BindResourcesResult bindResources(@ApiParam(name = "schedulerResult") SchedulerResult schedulerResult);

	boolean isOwnerOfInst(@ApiParam(name = "instId") String instId,
						  @ApiParam(name = "bpId") String bpId,
						  @ApiParam(name = "userId") String userId);

	int getContainerNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId);

	List<TargetServiceInfo> getContainer(@ApiParam(name = "specId")String specId, @ApiParam(name = "instId")String instId);

	String getSpecName(@ApiParam(name = "specId")String specId);

	String getInstName(@ApiParam(name = "instId")String instId);

	GetStatusMetricsRsp getContainerInstanceStatusMetrics(@ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId);
}
