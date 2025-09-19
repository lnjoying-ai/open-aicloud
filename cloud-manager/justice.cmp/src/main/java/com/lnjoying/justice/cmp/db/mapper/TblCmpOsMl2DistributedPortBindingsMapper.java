package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsMl2DistributedPortBindings;
import com.lnjoying.justice.cmp.db.model.TblCmpOsMl2DistributedPortBindingsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsMl2DistributedPortBindingsKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsMl2DistributedPortBindingsMapper {
    long countByExample(TblCmpOsMl2DistributedPortBindingsExample example);

    int deleteByExample(TblCmpOsMl2DistributedPortBindingsExample example);

    int deleteByPrimaryKey(TblCmpOsMl2DistributedPortBindingsKey key);

    int insert(TblCmpOsMl2DistributedPortBindings record);

    int insertSelective(TblCmpOsMl2DistributedPortBindings record);

    List<TblCmpOsMl2DistributedPortBindings> selectByExample(TblCmpOsMl2DistributedPortBindingsExample example);

    TblCmpOsMl2DistributedPortBindings selectByPrimaryKey(TblCmpOsMl2DistributedPortBindingsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsMl2DistributedPortBindings record, @Param("example") TblCmpOsMl2DistributedPortBindingsExample example);

    int updateByExample(@Param("record") TblCmpOsMl2DistributedPortBindings record, @Param("example") TblCmpOsMl2DistributedPortBindingsExample example);

    int updateByPrimaryKeySelective(TblCmpOsMl2DistributedPortBindings record);

    int updateByPrimaryKey(TblCmpOsMl2DistributedPortBindings record);
}