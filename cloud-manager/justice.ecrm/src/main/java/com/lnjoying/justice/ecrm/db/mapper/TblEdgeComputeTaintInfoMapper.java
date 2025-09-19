package com.lnjoying.justice.ecrm.db.mapper;

import com.lnjoying.justice.ecrm.db.model.TblEdgeComputeTaintInfo;
import com.lnjoying.justice.ecrm.db.model.TblEdgeComputeTaintInfoExample;
import com.lnjoying.justice.ecrm.db.model.TblEdgeComputeTaintInfoKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblEdgeComputeTaintInfoMapper {
    long countByExample(TblEdgeComputeTaintInfoExample example);

    int deleteByExample(TblEdgeComputeTaintInfoExample example);

    int deleteByPrimaryKey(TblEdgeComputeTaintInfoKey key);

    int insert(TblEdgeComputeTaintInfo record);

    int insertSelective(TblEdgeComputeTaintInfo record);

    List<TblEdgeComputeTaintInfo> selectByExample(TblEdgeComputeTaintInfoExample example);

    TblEdgeComputeTaintInfo selectByPrimaryKey(TblEdgeComputeTaintInfoKey key);

    int updateByExampleSelective(@Param("record") TblEdgeComputeTaintInfo record, @Param("example") TblEdgeComputeTaintInfoExample example);

    int updateByExample(@Param("record") TblEdgeComputeTaintInfo record, @Param("example") TblEdgeComputeTaintInfoExample example);

    int updateByPrimaryKeySelective(TblEdgeComputeTaintInfo record);

    int updateByPrimaryKey(TblEdgeComputeTaintInfo record);
}