package com.lnjoying.justice.ecrm.db.mapper;

import com.lnjoying.justice.ecrm.db.model.TblEdgePortInfo;
import com.lnjoying.justice.ecrm.db.model.TblEdgePortInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblEdgePortInfoMapper {
    long countByExample(TblEdgePortInfoExample example);

    int deleteByExample(TblEdgePortInfoExample example);

    int deleteByPrimaryKey(String nodeId);

    int insert(TblEdgePortInfo record);

    int insertSelective(TblEdgePortInfo record);

    List<TblEdgePortInfo> selectByExample(TblEdgePortInfoExample example);

    TblEdgePortInfo selectByPrimaryKey(String nodeId);

    int updateByExampleSelective(@Param("record") TblEdgePortInfo record, @Param("example") TblEdgePortInfoExample example);

    int updateByExample(@Param("record") TblEdgePortInfo record, @Param("example") TblEdgePortInfoExample example);

    int updateByPrimaryKeySelective(TblEdgePortInfo record);

    int updateByPrimaryKey(TblEdgePortInfo record);
}