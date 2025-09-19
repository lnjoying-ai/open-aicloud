package com.lnjoying.justice.scheduler.db.mapper;

import com.lnjoying.justice.scheduler.db.model.TblEdgeComputeInfo;
import com.lnjoying.justice.scheduler.db.model.TblEdgeComputeInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblEdgeComputeInfoMapper {
    long countByExample(TblEdgeComputeInfoExample example);

    int deleteByExample(TblEdgeComputeInfoExample example);

    int deleteByPrimaryKey(String nodeId);

    int insert(TblEdgeComputeInfo record);

    int insertSelective(TblEdgeComputeInfo record);

    List<TblEdgeComputeInfo> selectByExample(TblEdgeComputeInfoExample example);

    TblEdgeComputeInfo selectByPrimaryKey(String nodeId);

    int updateByExampleSelective(@Param("record") TblEdgeComputeInfo record, @Param("example") TblEdgeComputeInfoExample example);

    int updateByExample(@Param("record") TblEdgeComputeInfo record, @Param("example") TblEdgeComputeInfoExample example);

    int updateByPrimaryKeySelective(TblEdgeComputeInfo record);

    int updateByPrimaryKey(TblEdgeComputeInfo record);
}