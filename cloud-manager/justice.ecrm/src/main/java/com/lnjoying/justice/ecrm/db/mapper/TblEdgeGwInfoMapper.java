package com.lnjoying.justice.ecrm.db.mapper;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.ecrm.db.model.TblEdgeGwInfo;
import com.lnjoying.justice.ecrm.db.model.TblEdgeGwInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

@MapperModel(value="gw")
public interface TblEdgeGwInfoMapper {
    long countByExample(TblEdgeGwInfoExample example);

    int deleteByExample(TblEdgeGwInfoExample example);

    int deleteByPrimaryKey(String nodeId);

    int insert(TblEdgeGwInfo record);

    int insertSelective(TblEdgeGwInfo record);

    List<TblEdgeGwInfo> selectByExample(TblEdgeGwInfoExample example);

    TblEdgeGwInfo selectByPrimaryKey(String nodeId);

    int updateByExampleSelective(@Param("record") TblEdgeGwInfo record, @Param("example") TblEdgeGwInfoExample example);

    int updateByExample(@Param("record") TblEdgeGwInfo record, @Param("example") TblEdgeGwInfoExample example);

    int updateByPrimaryKeySelective(TblEdgeGwInfo record);

    int updateByPrimaryKey(TblEdgeGwInfo record);
}