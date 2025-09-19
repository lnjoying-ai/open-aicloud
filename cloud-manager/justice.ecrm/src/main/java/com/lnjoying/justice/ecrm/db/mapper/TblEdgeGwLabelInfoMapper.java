package com.lnjoying.justice.ecrm.db.mapper;

import com.lnjoying.justice.ecrm.db.model.TblEdgeGwLabelInfo;
import com.lnjoying.justice.ecrm.db.model.TblEdgeGwLabelInfoExample;
import com.lnjoying.justice.ecrm.db.model.TblEdgeGwLabelInfoKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblEdgeGwLabelInfoMapper {
    long countByExample(TblEdgeGwLabelInfoExample example);

    int deleteByExample(TblEdgeGwLabelInfoExample example);

    int deleteByPrimaryKey(TblEdgeGwLabelInfoKey key);

    int insert(TblEdgeGwLabelInfo record);

    int insertSelective(TblEdgeGwLabelInfo record);

    List<TblEdgeGwLabelInfo> selectByExample(TblEdgeGwLabelInfoExample example);

    TblEdgeGwLabelInfo selectByPrimaryKey(TblEdgeGwLabelInfoKey key);

    int updateByExampleSelective(@Param("record") TblEdgeGwLabelInfo record, @Param("example") TblEdgeGwLabelInfoExample example);

    int updateByExample(@Param("record") TblEdgeGwLabelInfo record, @Param("example") TblEdgeGwLabelInfoExample example);

    int updateByPrimaryKeySelective(TblEdgeGwLabelInfo record);

    int updateByPrimaryKey(TblEdgeGwLabelInfo record);
}