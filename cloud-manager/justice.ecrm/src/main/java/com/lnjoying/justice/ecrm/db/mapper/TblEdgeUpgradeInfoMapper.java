package com.lnjoying.justice.ecrm.db.mapper;

import com.lnjoying.justice.ecrm.db.model.TblEdgeUpgradeInfo;
import com.lnjoying.justice.ecrm.db.model.TblEdgeUpgradeInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblEdgeUpgradeInfoMapper {
    long countByExample(TblEdgeUpgradeInfoExample example);

    int deleteByExample(TblEdgeUpgradeInfoExample example);

    int deleteByPrimaryKey(String nodeId);

    int insert(TblEdgeUpgradeInfo record);

    int insertSelective(TblEdgeUpgradeInfo record);

    List<TblEdgeUpgradeInfo> selectByExample(TblEdgeUpgradeInfoExample example);

    TblEdgeUpgradeInfo selectByPrimaryKey(String nodeId);

    int updateByExampleSelective(@Param("record") TblEdgeUpgradeInfo record, @Param("example") TblEdgeUpgradeInfoExample example);

    int updateByExample(@Param("record") TblEdgeUpgradeInfo record, @Param("example") TblEdgeUpgradeInfoExample example);

    int updateByPrimaryKeySelective(TblEdgeUpgradeInfo record);

    int updateByPrimaryKey(TblEdgeUpgradeInfo record);
}