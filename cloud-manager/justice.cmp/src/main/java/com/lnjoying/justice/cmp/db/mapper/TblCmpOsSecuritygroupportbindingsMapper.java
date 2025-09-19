package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygroupportbindings;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygroupportbindingsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygroupportbindingsKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsSecuritygroupportbindingsMapper {
    long countByExample(TblCmpOsSecuritygroupportbindingsExample example);

    int deleteByExample(TblCmpOsSecuritygroupportbindingsExample example);

    int deleteByPrimaryKey(TblCmpOsSecuritygroupportbindingsKey key);

    int insert(TblCmpOsSecuritygroupportbindings record);

    int insertSelective(TblCmpOsSecuritygroupportbindings record);

    List<TblCmpOsSecuritygroupportbindings> selectByExample(TblCmpOsSecuritygroupportbindingsExample example);

    TblCmpOsSecuritygroupportbindings selectByPrimaryKey(TblCmpOsSecuritygroupportbindingsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsSecuritygroupportbindings record, @Param("example") TblCmpOsSecuritygroupportbindingsExample example);

    int updateByExample(@Param("record") TblCmpOsSecuritygroupportbindings record, @Param("example") TblCmpOsSecuritygroupportbindingsExample example);

    int updateByPrimaryKeySelective(TblCmpOsSecuritygroupportbindings record);

    int updateByPrimaryKey(TblCmpOsSecuritygroupportbindings record);
}