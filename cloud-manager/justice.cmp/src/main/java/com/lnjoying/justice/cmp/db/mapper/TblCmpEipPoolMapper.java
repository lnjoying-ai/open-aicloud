package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpEipPool;
import com.lnjoying.justice.cmp.db.model.TblCmpEipPoolExample;
import com.lnjoying.justice.cmp.db.model.TblCmpEipPoolKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_EIP_POOL)
public interface TblCmpEipPoolMapper {
    long countByExample(TblCmpEipPoolExample example);

    int deleteByExample(TblCmpEipPoolExample example);

    int deleteByPrimaryKey(TblCmpEipPoolKey key);

    int insert(TblCmpEipPool record);

    int insertSelective(TblCmpEipPool record);

    List<TblCmpEipPool> selectByExample(TblCmpEipPoolExample example);

    TblCmpEipPool selectByPrimaryKey(TblCmpEipPoolKey key);

    int updateByExampleSelective(@Param("record") TblCmpEipPool record, @Param("example") TblCmpEipPoolExample example);

    int updateByExample(@Param("record") TblCmpEipPool record, @Param("example") TblCmpEipPoolExample example);

    int updateByPrimaryKeySelective(TblCmpEipPool record);

    int updateByPrimaryKey(TblCmpEipPool record);

    Set<String> getPoolIds(@Param("cloudId")String cloudId);
}