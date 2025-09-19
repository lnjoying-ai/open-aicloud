package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpVmStats;
import com.lnjoying.justice.cmp.db.model.TblCmpVmStatsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpVmStatsKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpVmStatsMapper {
    long countByExample(TblCmpVmStatsExample example);

    int deleteByExample(TblCmpVmStatsExample example);

    int deleteByPrimaryKey(TblCmpVmStatsKey key);

    int insert(TblCmpVmStats record);

    int insertSelective(TblCmpVmStats record);

    List<TblCmpVmStats> selectByExample(TblCmpVmStatsExample example);

    TblCmpVmStats selectByPrimaryKey(TblCmpVmStatsKey key);

    int updateByExampleSelective(@Param("record") TblCmpVmStats record, @Param("example") TblCmpVmStatsExample example);

    int updateByExample(@Param("record") TblCmpVmStats record, @Param("example") TblCmpVmStatsExample example);

    int updateByPrimaryKeySelective(TblCmpVmStats record);

    int updateByPrimaryKey(TblCmpVmStats record);
}