package com.lnjoying.justice.aos.db.mapper;

import com.lnjoying.justice.aos.domain.model.StackDeployInfo;
import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @Description: Related to deployment, it integrates spec and stack info queries
 * @Author: Merak
 * @Date: 2022/1/10 16:23
 */

@MapperModel(value="container_deploy")
public interface TblStackDeployInfoMapper
{

    /**
     * query by condition
     * @param status status
     * @param statusExcludeCollection exclude certain states
     * @param name  name
     * @param regionId region id
     * @param siteId site id
     * @param nodeId node id
     * @param userId user id
     * @param pageSize  page size
     * @param startRow startRow
     * @return list
     */
    List<StackDeployInfo> selectAll(@Param("status") Integer status, @Param("statusExcludeCollection") Collection<Integer> statusExcludeCollection,
                                    @Param("progressingStageCollection") Collection<Integer> progressingStageCollection,  @Param("availableStageCollection") Collection<Integer> availableStageCollection,
                                    @Param("readyStageCollection") Collection<Integer> readyStageCollection,  @Param("failedStageCollection") Collection<Integer> failedStageCollection,
                                    @Param("name") String name,
                                    @Param("regionId") String regionId, @Param("siteId") String siteId, @Param("nodeId") String nodeId,
                                    @Param("bpId") String bpId, @Param("userId") String userId, @Param("startRow") int startRow, @Param("pageSize") int pageSize, @Param("stackType") String stackType);

    /**
     * count by condition
     * @param status status
     * @param statusExcludeCollection exclude certain states
     * @param regionId region id
     * @param siteId site id
     * @param nodeId node id
     * @param userId user id
     * @return list
     */
    int countAll(@Param("status") Integer status, @Param("statusExcludeCollection") Collection<Integer> statusExcludeCollection,
                 @Param("progressingStageCollection") Collection<Integer> progressingStageCollection,  @Param("availableStageCollection") Collection<Integer> availableStageCollection,
                 @Param("readyStageCollection") Collection<Integer> readyStageCollection,  @Param("failedStageCollection") Collection<Integer> failedStageCollection,
                 @Param("name") String name,
                 @Param("regionId") String regionId, @Param("siteId") String siteId, @Param("nodeId") String nodeId,
                 @Param("bpId") String bpId, @Param("userId") String userId, @Param("stackType") String stackType);


    /**
     * query by spec id
     * @param specId
     * @return
     */
    StackDeployInfo selectBySpecId(@Param("specId") String specId, @Param("statusExcludeCollection") Collection<Integer> statusExcludeCollection,
                                   @Param("progressingStageCollection") Collection<Integer> progressingStageCollection,  @Param("availableStageCollection") Collection<Integer> availableStageCollection,
                                   @Param("readyStageCollection") Collection<Integer> readyStageCollection,  @Param("failedStageCollection") Collection<Integer> failedStageCollection);
}
