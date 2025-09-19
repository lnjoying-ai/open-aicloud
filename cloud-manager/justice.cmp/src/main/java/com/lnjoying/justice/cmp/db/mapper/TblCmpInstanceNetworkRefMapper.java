package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpInstanceNetworkRef;
import com.lnjoying.justice.cmp.db.model.TblCmpInstanceNetworkRefExample;
import com.lnjoying.justice.cmp.db.model.TblCmpInstanceNetworkRefKey;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

public interface TblCmpInstanceNetworkRefMapper {
    long countByExample(TblCmpInstanceNetworkRefExample example);

    int deleteByExample(TblCmpInstanceNetworkRefExample example);

    int deleteByPrimaryKey(TblCmpInstanceNetworkRefKey key);

    int insert(TblCmpInstanceNetworkRef record);

    int insertSelective(TblCmpInstanceNetworkRef record);

    List<TblCmpInstanceNetworkRef> selectByExample(TblCmpInstanceNetworkRefExample example);

    TblCmpInstanceNetworkRef selectByPrimaryKey(TblCmpInstanceNetworkRefKey key);

    int updateByExampleSelective(@Param("record") TblCmpInstanceNetworkRef record, @Param("example") TblCmpInstanceNetworkRefExample example);

    int updateByExample(@Param("record") TblCmpInstanceNetworkRef record, @Param("example") TblCmpInstanceNetworkRefExample example);

    int updateByPrimaryKeySelective(TblCmpInstanceNetworkRef record);

    int updateByPrimaryKey(TblCmpInstanceNetworkRef record);

    Set<String> getInstanceNetworkRefIds(@Param("cloudId")String cloudId, @Param("instanceId")String instanceId);
}