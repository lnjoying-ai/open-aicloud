package com.lnjoying.justice.aos.db.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface TblEdgeComputeInfoOperator
{
    @Update({ "update tbl_edge_compute_gpu_info set ref_id = #{refId},status = #{status},update_time = now() where node_id = #{nodeId}" })
    int updateGpuRefIdAndStatusByNode(@Param("nodeId") String nodeId, @Param("refId") String refId, @Param("status") Integer status);

    @Update({ "update tbl_edge_compute_gpu_info set ref_id = null,status = #{status},update_time = now() where ref_id = #{refId}" })
    int removeGpuBind(@Param("refId") String refId, @Param("status") Integer status);

    @Update({ "update tbl_edge_compute_info set user_id = #{userId},update_time = now() where node_id = #{nodeId}" })
    int updateEdgeComputeUser(@Param("nodeId") String nodeId, @Param("userId") String userId);
}
