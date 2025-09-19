package com.lnjoying.justice.cluster.manager.service.cluster;

import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.CoreServiceMetrics;
import com.lnjoying.justice.cluster.manager.domain.dto.req.cluster.RepairClusterReqDto;
import com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster.*;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.UserBasicInfo;
import com.lnjoying.justice.cluster.manager.domain.search.ClusterNodeSearchCritical;
import com.lnjoying.justice.cluster.manager.domain.search.ClusterSearchCritical;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterInfo;
import com.lnjoying.justice.cluster.manager.domain.dto.req.cluster.AddClusterInfoReqDto;
import com.lnjoying.justice.cluster.manager.domain.dto.req.cluster.UpdateClusterInfoReqDto;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.micro.core.common.Pair;

import java.util.List;

/**
 * @Description: 
 * @Author: Regulus
 * @Date: 1/6/22 9:05 PM
 */
public interface ClusterService
{
    String createCluster(AddClusterInfoReqDto clusterInfoReq, String userId, String bpId) throws WebSystemException;
    int removeCluster(String clusterId, UserBasicInfo userBasicInfo, boolean force) throws WebSystemException;
    int updateCluster(String clusterId, UpdateClusterInfoReqDto clusterInfoReq, UserBasicInfo userBasicInfo) throws WebSystemException;
    ClusterInfoRspDto getClusterInfo(String clusterId, UserBasicInfo userBasicInfo) throws WebSystemException;
    ClusterListInfoRspDto getClusterList(ClusterSearchCritical critical);
    
    int assignDev(TblClusterInfo tblClusterInfo);
    
    int releaseDev(ClusterInnerInfo clusterInnerInfo);
    int updateCluster(ClusterInnerInfo clusterInnerInfo);
    int prepareBuildCluster(ClusterInnerInfo clusterInnerInfo);
    
    
    ClusterNodesRspDto getClusterNodesInfo(ClusterNodeSearchCritical critical);

    String getKubeConfig(String clusterId, Pair<String, String> operUserInfo);
    void updateClsServiceState(String clusterId, String action, UserBasicInfo userBasicInfo);

    List<ClusterBasicInfoRspDto> getClusterBasicInfoList(ClusterSearchCritical critical);

    ClusterTerminalRspDto getClusterTerminal(String clusterId, UserBasicInfo userBasicInfo);

    ClusterNodeRspDto getClusterNodeInfo(String nodeId);

    List<CoreServiceMetrics> getClusterCoreServiceMetrics(String clusterId);

    ClusterImportCmdRspDto getImportClusterCmd(String clusterId);

    void repairCluster(String clusterId, RepairClusterReqDto repairClusterReqDto);
}
