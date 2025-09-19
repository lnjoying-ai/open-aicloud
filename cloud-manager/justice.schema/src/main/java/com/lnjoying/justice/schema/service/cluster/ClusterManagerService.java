package com.lnjoying.justice.schema.service.cluster;

import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.entity.cluster.ClusterAuthParams;
import com.lnjoying.justice.schema.entity.cluster.ClusterSecretInfo;
import com.lnjoying.justice.schema.entity.scheduler.BindResourcesResult;
import com.lnjoying.justice.schema.entity.scheduler.SchedulerClusterResult;
import com.lnjoying.justice.schema.entity.stat.GetClusterNumMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.GetStatusMetricsRsp;
import com.lnjoying.justice.schema.msg.EdgeMessage;
import com.micro.core.common.Pair;
import io.swagger.annotations.ApiParam;

public interface ClusterManagerService
{
    int deliver(@ApiParam(name = "message") EdgeMessage edgeMessage);
    BindResourcesResult bindResources(@ApiParam(name = "schedulerResult") SchedulerClusterResult schedulerResult);
    ClusterSecretInfo getClusterServerCert();
    CombRetPacket authCluster(@ApiParam(name = "clusterAuthParams") ClusterAuthParams clusterAuthParams); //status:auth result, Object:set ClusterEntity
    int workerReg(@ApiParam(name = "message") EdgeMessage edgeMessage);
    int workerLogin(@ApiParam(name = "message") EdgeMessage edgeMessage);
    CombRetPacket authClusterUser(@ApiParam(name = "clusterAuthParams")ClusterAuthParams clusterAuthParams);
    int getClusterNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId);

    String getKubeConfig(@ApiParam(name = "clusterId")String clusterId, @ApiParam(name = "operUserInfo")Pair<String, String> operUserInfo);
    GetStatusMetricsRsp getClusterStatusMetrics(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId);
    GetClusterNumMetricsRsp getClusterNumMetrics (@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId);
    CombRetPacket getClusterAgent(@ApiParam(name = "clusterId")String clusterId);
    String getClusterName(@ApiParam(name = "clusterId")String clusterId);
}
