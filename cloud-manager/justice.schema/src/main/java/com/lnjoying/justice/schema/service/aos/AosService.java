package com.lnjoying.justice.schema.service.aos;

import com.lnjoying.justice.schema.entity.scheduler.BindResourcesResult;
import com.lnjoying.justice.schema.entity.scheduler.SchedulerResult;
import com.lnjoying.justice.schema.entity.servicemanager.TargetServiceInfo;
import com.lnjoying.justice.schema.msg.EdgeMessage;
import com.micro.core.common.Pair;
import io.swagger.annotations.ApiParam;

import java.util.List;

public interface AosService
{
	int deliver(@ApiParam(name = "message") EdgeMessage edgeMessage);
	void evacuateEdge(@ApiParam(name = "nodeId") String nodeId);

	BindResourcesResult bindResources(@ApiParam(name = "schedulerResult") SchedulerResult schedulerResult);

	boolean isOwner(@ApiParam(name = "stackId") String stackId,
					@ApiParam(name = "bpId") String bpId,
					@ApiParam(name = "userId") String userId);

	Pair<TtyStackDeployStatus, String> addNodeStack(@ApiParam(name = "nodeId") String nodeId,
													@ApiParam(name = "type") String type,
													@ApiParam(name = "bpId") String bpId,
													@ApiParam(name = "userId") String userId);

	Pair<TtyStackDeployStatus, String> getNodeStack(@ApiParam(name = "stackId") String stackId);

	void deleteNodeStack(@ApiParam(name = "stackId") String stackId);

	enum TtyStackDeployStatus
	{
		/**
		 * newly created
		 */
		NEW,

		/**
		 * deploying
		 */
		DEPLOYING,

		/**
		 * deployed
		 */
		DEPLOYED,

		/**
		 * error
		 */
		ERROR;
	}

	int getLiteAppNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId);

	int getHelmAppNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId);

	List<TargetServiceInfo> getStack(@ApiParam(name = "specId")String specId, @ApiParam(name = "stackId")String stackId);

	String getSpecName(@ApiParam(name = "specId")String specId);

	String getStackName(@ApiParam(name = "stackId")String stackId);
}
