package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsMl2PortBindings;
import com.lnjoying.justice.cmp.db.model.TblCmpOsMl2PortBindingsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsMl2PortBindingsKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsMl2PortBindingsMapper {
    long countByExample(TblCmpOsMl2PortBindingsExample example);

    int deleteByExample(TblCmpOsMl2PortBindingsExample example);

    int deleteByPrimaryKey(TblCmpOsMl2PortBindingsKey key);

    int insert(TblCmpOsMl2PortBindings record);

    int insertSelective(TblCmpOsMl2PortBindings record);

    List<TblCmpOsMl2PortBindings> selectByExample(TblCmpOsMl2PortBindingsExample example);

    TblCmpOsMl2PortBindings selectByPrimaryKey(TblCmpOsMl2PortBindingsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsMl2PortBindings record, @Param("example") TblCmpOsMl2PortBindingsExample example);

    int updateByExample(@Param("record") TblCmpOsMl2PortBindings record, @Param("example") TblCmpOsMl2PortBindingsExample example);

    int updateByPrimaryKeySelective(TblCmpOsMl2PortBindings record);

    int updateByPrimaryKey(TblCmpOsMl2PortBindings record);
}