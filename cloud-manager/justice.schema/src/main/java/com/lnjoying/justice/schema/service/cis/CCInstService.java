package com.lnjoying.justice.schema.service.cis;

import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.entity.dev.Registry;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import io.swagger.annotations.ApiParam;

/**
 * @Author: Regulus
 * @Date: 12/29/21 11:44 AM
 * @Description:
 */
public interface CCInstService
{
    CombRetPacket createContainer(@ApiParam(name = "targetNode")TargetNode targetNode, @ApiParam(name = "instParams")String instParams, @ApiParam(name = "instName")String instName, @ApiParam(name = "registry")Registry registry, @ApiParam(name = "autoRun")boolean autoRun, @ApiParam(name = "orchType")String orchType);
    CombRetPacket logContainer(@ApiParam(name = "targetNode")TargetNode targetNode, @ApiParam(name = "instName")String instName);
    CombRetPacket listContainer(@ApiParam(name = "targetNode")TargetNode targetNode, @ApiParam(name = "instName")String instName);
    CombRetPacket getLogContainer(@ApiParam(name = "targetNode")TargetNode targetNode, @ApiParam(name = "instName")String instName);
    CombRetPacket removeContainer(@ApiParam(name = "targetNode")TargetNode targetNode, @ApiParam(name = "instName") String instName);
    CombRetPacket cleanJkeContainer(@ApiParam(name = "targetNode")TargetNode targetNode, @ApiParam(name = "orchType")String orchType);
    CombRetPacket getContainerStatus(@ApiParam(name = "nodeId")String nodeId, @ApiParam(name = "instName")String instName);
    CombRetPacket actionContainer(@ApiParam(name = "targetNode")TargetNode targetNode, @ApiParam(name = "instName") String instName, @ApiParam(name = "action") String action);
    CombRetPacket createContainerWithInstId(@ApiParam(name = "instId")String instId, @ApiParam(name = "targetNode")TargetNode targetNode, @ApiParam(name = "instParams")String instParams, @ApiParam(name = "instName")String instName, @ApiParam(name = "registry")Registry registry, @ApiParam(name = "autoRun")boolean autoRun, @ApiParam(name = "orchType")String orchType);
    CombRetPacket removeContainerByInstId(@ApiParam(name = "instId")String instId);
    boolean checkOperUserByContainerRefId(@ApiParam(name = "containerRefId")String containerRefId, @ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId);
    boolean checkOperUserByContainerName(@ApiParam(name = "containerName")String containerName, @ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId);

}
