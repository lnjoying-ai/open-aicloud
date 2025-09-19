package com.lnjoying.justice.cluster.manager.db.mapper;

import com.lnjoying.justice.cluster.manager.db.model.TblClusterNodeInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterNodeInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblClusterNodeInfoMapper {
    long countByExample(TblClusterNodeInfoExample example);

    int deleteByExample(TblClusterNodeInfoExample example);

    int insert(TblClusterNodeInfo record);

    int insertSelective(TblClusterNodeInfo record);

    List<TblClusterNodeInfo> selectByExample(TblClusterNodeInfoExample example);

    int updateByExampleSelective(@Param("record") TblClusterNodeInfo record, @Param("example") TblClusterNodeInfoExample example);

    int updateByExample(@Param("record") TblClusterNodeInfo record, @Param("example") TblClusterNodeInfoExample example);
}