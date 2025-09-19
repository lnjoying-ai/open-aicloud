package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpResourceStats;
import com.lnjoying.justice.cmp.db.model.TblCmpResourceStatsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpResourceStatsKey;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

public interface TblCmpResourceStatsMapper {
    long countByExample(TblCmpResourceStatsExample example);

    int deleteByExample(TblCmpResourceStatsExample example);

    int deleteByPrimaryKey(TblCmpResourceStatsKey key);

    int insert(TblCmpResourceStats record);

    int insertSelective(TblCmpResourceStats record);

    List<TblCmpResourceStats> selectByExample(TblCmpResourceStatsExample example);

    TblCmpResourceStats selectByPrimaryKey(TblCmpResourceStatsKey key);

    int updateByExampleSelective(@Param("record") TblCmpResourceStats record, @Param("example") TblCmpResourceStatsExample example);

    int updateByExample(@Param("record") TblCmpResourceStats record, @Param("example") TblCmpResourceStatsExample example);

    int updateByPrimaryKeySelective(TblCmpResourceStats record);

    int updateByPrimaryKey(TblCmpResourceStats record);

    Set<String> getStatsIds(@Param("cloudId")String cloudId);
}