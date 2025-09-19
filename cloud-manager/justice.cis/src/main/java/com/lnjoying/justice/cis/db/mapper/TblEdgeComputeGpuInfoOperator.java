package com.lnjoying.justice.cis.db.mapper;

import com.lnjoying.justice.cis.db.model.TblEdgeComputeGpuInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TblEdgeComputeGpuInfoOperator
{
    @Select("select * from tbl_edge_compute_gpu_info where status=#{status} and ref_id=#{refId}")
    @Results(id = "TblEdgeComputeGpuInfo", value = {
            @Result(column = "gpu_id", property = "gpuId"),
            @Result(column = "gpu_type", property = "gpuType"),
            @Result(column = "gpu_model", property = "gpuModel"),
            @Result(column = "driver_version", property = "driverVersion"),
            @Result(column = "cuda_version", property = "cudaVersion"),
            @Result(column = "cudnn_version", property = "cudnnVersion"),
            @Result(column = "node_id", property = "nodeId"),
            @Result(column = "index", property = "index"),
            @Result(column = "status", property = "status"),
            @Result(column = "ref_id", property = "refId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<TblEdgeComputeGpuInfo> selectBindGpuByRefId(@Param("refId") String refId, @Param("status") Integer status);

    @ResultMap("TblEdgeComputeGpuInfo")
    @Select("select * from tbl_edge_compute_gpu_info where status=#{status} and node_id=#{nodeId}")
    List<TblEdgeComputeGpuInfo> selectGpuByNodeIdAndStatus(@Param("nodeId") String nodeId, @Param("status") Integer status);

    @ResultMap("TblEdgeComputeGpuInfo")
    @Select("select * from tbl_edge_compute_gpu_info where node_id=#{nodeId}")
    List<TblEdgeComputeGpuInfo> selectGpuByNodeId(@Param("nodeId") String nodeId);

    @Update({ "update tbl_edge_compute_gpu_info set ref_id = #{refId},status = #{status},update_time = now() where gpu_id = #{gpuId}" })
    int updateRefIdAndStatus(@Param("gpuId") String gpuId, @Param("refId") String refId, @Param("status") Integer status);

    @Update({ "update tbl_edge_compute_gpu_info set ref_id = null,status = #{status},update_time = now() where ref_id = #{refId}" })
    int removeGpuBind(@Param("refId") String refId, @Param("status") Integer status);
}
