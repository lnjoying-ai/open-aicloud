package com.lnjoying.justice.ecrm.db.mapper;

import com.lnjoying.justice.ecrm.db.model.TblEdgeComputeLabelInfo;
import com.lnjoying.justice.ecrm.db.model.TblEdgeComputeLabelInfoExample;
import com.lnjoying.justice.ecrm.db.model.TblEdgeComputeLabelInfoKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblEdgeComputeLabelInfoMapper {
    long countByExample(TblEdgeComputeLabelInfoExample example);

    int deleteByExample(TblEdgeComputeLabelInfoExample example);

    int deleteByPrimaryKey(TblEdgeComputeLabelInfoKey key);

    int insert(TblEdgeComputeLabelInfo record);

    int insertSelective(TblEdgeComputeLabelInfo record);

    List<TblEdgeComputeLabelInfo> selectByExample(TblEdgeComputeLabelInfoExample example);

    TblEdgeComputeLabelInfo selectByPrimaryKey(TblEdgeComputeLabelInfoKey key);

    int updateByExampleSelective(@Param("record") TblEdgeComputeLabelInfo record, @Param("example") TblEdgeComputeLabelInfoExample example);

    int updateByExample(@Param("record") TblEdgeComputeLabelInfo record, @Param("example") TblEdgeComputeLabelInfoExample example);

    int updateByPrimaryKeySelective(TblEdgeComputeLabelInfo record);

    int updateByPrimaryKey(TblEdgeComputeLabelInfo record);
}