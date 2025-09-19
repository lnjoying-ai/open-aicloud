package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsPortforwardings;
import com.lnjoying.justice.cmp.db.model.TblCmpOsPortforwardingsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsPortforwardingsKey;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

public interface TblCmpOsPortforwardingsMapper {
    long countByExample(TblCmpOsPortforwardingsExample example);

    int deleteByExample(TblCmpOsPortforwardingsExample example);

    int deleteByPrimaryKey(TblCmpOsPortforwardingsKey key);

    int insert(TblCmpOsPortforwardings record);

    int insertSelective(TblCmpOsPortforwardings record);

    List<TblCmpOsPortforwardings> selectByExample(TblCmpOsPortforwardingsExample example);

    TblCmpOsPortforwardings selectByPrimaryKey(TblCmpOsPortforwardingsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsPortforwardings record, @Param("example") TblCmpOsPortforwardingsExample example);

    int updateByExample(@Param("record") TblCmpOsPortforwardings record, @Param("example") TblCmpOsPortforwardingsExample example);

    int updateByPrimaryKeySelective(TblCmpOsPortforwardings record);

    int updateByPrimaryKey(TblCmpOsPortforwardings record);

    Set<String> getPortforwardingIds(@Param("cloudId")String cloudId);
}