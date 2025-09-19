package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsMl2PortBindingLevels;
import com.lnjoying.justice.cmp.db.model.TblCmpOsMl2PortBindingLevelsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsMl2PortBindingLevelsKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsMl2PortBindingLevelsMapper {
    long countByExample(TblCmpOsMl2PortBindingLevelsExample example);

    int deleteByExample(TblCmpOsMl2PortBindingLevelsExample example);

    int deleteByPrimaryKey(TblCmpOsMl2PortBindingLevelsKey key);

    int insert(TblCmpOsMl2PortBindingLevels record);

    int insertSelective(TblCmpOsMl2PortBindingLevels record);

    List<TblCmpOsMl2PortBindingLevels> selectByExample(TblCmpOsMl2PortBindingLevelsExample example);

    TblCmpOsMl2PortBindingLevels selectByPrimaryKey(TblCmpOsMl2PortBindingLevelsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsMl2PortBindingLevels record, @Param("example") TblCmpOsMl2PortBindingLevelsExample example);

    int updateByExample(@Param("record") TblCmpOsMl2PortBindingLevels record, @Param("example") TblCmpOsMl2PortBindingLevelsExample example);

    int updateByPrimaryKeySelective(TblCmpOsMl2PortBindingLevels record);

    int updateByPrimaryKey(TblCmpOsMl2PortBindingLevels record);
}