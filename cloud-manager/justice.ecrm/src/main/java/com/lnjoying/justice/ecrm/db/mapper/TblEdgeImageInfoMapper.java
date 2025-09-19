package com.lnjoying.justice.ecrm.db.mapper;

import com.lnjoying.justice.ecrm.db.model.TblEdgeImageInfo;
import com.lnjoying.justice.ecrm.db.model.TblEdgeImageInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblEdgeImageInfoMapper {
    long countByExample(TblEdgeImageInfoExample example);

    int deleteByExample(TblEdgeImageInfoExample example);

    int deleteByPrimaryKey(String nodeId);

    int insert(TblEdgeImageInfo record);

    int insertSelective(TblEdgeImageInfo record);

    List<TblEdgeImageInfo> selectByExample(TblEdgeImageInfoExample example);

    TblEdgeImageInfo selectByPrimaryKey(String nodeId);

    int updateByExampleSelective(@Param("record") TblEdgeImageInfo record, @Param("example") TblEdgeImageInfoExample example);

    int updateByExample(@Param("record") TblEdgeImageInfo record, @Param("example") TblEdgeImageInfoExample example);

    int updateByPrimaryKeySelective(TblEdgeImageInfo record);

    int updateByPrimaryKey(TblEdgeImageInfo record);
}