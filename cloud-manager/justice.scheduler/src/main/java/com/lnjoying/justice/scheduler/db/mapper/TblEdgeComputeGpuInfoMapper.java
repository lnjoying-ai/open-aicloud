package com.lnjoying.justice.scheduler.db.mapper;

import com.lnjoying.justice.scheduler.db.model.TblEdgeComputeGpuInfo;
import com.lnjoying.justice.scheduler.db.model.TblEdgeComputeGpuInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblEdgeComputeGpuInfoMapper {
    long countByExample(TblEdgeComputeGpuInfoExample example);

    int deleteByExample(TblEdgeComputeGpuInfoExample example);

    int deleteByPrimaryKey(String gpuId);

    int insert(TblEdgeComputeGpuInfo record);

    int insertSelective(TblEdgeComputeGpuInfo record);

    List<TblEdgeComputeGpuInfo> selectByExample(TblEdgeComputeGpuInfoExample example);

    TblEdgeComputeGpuInfo selectByPrimaryKey(String gpuId);

    int updateByExampleSelective(@Param("record") TblEdgeComputeGpuInfo record, @Param("example") TblEdgeComputeGpuInfoExample example);

    int updateByExample(@Param("record") TblEdgeComputeGpuInfo record, @Param("example") TblEdgeComputeGpuInfoExample example);

    int updateByPrimaryKeySelective(TblEdgeComputeGpuInfo record);

    int updateByPrimaryKey(TblEdgeComputeGpuInfo record);
}