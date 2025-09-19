package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpShare;
import com.lnjoying.justice.cmp.db.model.TblCmpShareExample;
import com.lnjoying.justice.cmp.db.model.TblCmpShareKey;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

public interface TblCmpShareMapper {
    long countByExample(TblCmpShareExample example);

    int deleteByExample(TblCmpShareExample example);

    int deleteByPrimaryKey(TblCmpShareKey key);

    int insert(TblCmpShare record);

    int insertSelective(TblCmpShare record);

    List<TblCmpShare> selectByExample(TblCmpShareExample example);

    TblCmpShare selectByPrimaryKey(TblCmpShareKey key);

    int updateByExampleSelective(@Param("record") TblCmpShare record, @Param("example") TblCmpShareExample example);

    int updateByExample(@Param("record") TblCmpShare record, @Param("example") TblCmpShareExample example);

    int updateByPrimaryKeySelective(TblCmpShare record);

    int updateByPrimaryKey(TblCmpShare record);

    Set<String> getShareIds(@Param("cloudId")String cloudId);
}