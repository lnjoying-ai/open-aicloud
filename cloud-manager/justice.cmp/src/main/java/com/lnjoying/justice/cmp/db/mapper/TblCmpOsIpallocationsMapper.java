package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsIpallocations;
import com.lnjoying.justice.cmp.db.model.TblCmpOsIpallocationsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsIpallocationsKey;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_IPALLOCATION)
public interface TblCmpOsIpallocationsMapper {
    long countByExample(TblCmpOsIpallocationsExample example);

    int deleteByExample(TblCmpOsIpallocationsExample example);

    int deleteByPrimaryKey(TblCmpOsIpallocationsKey key);

    int insert(TblCmpOsIpallocations record);

    int insertSelective(TblCmpOsIpallocations record);

    List<TblCmpOsIpallocations> selectByExample(TblCmpOsIpallocationsExample example);

    TblCmpOsIpallocations selectByPrimaryKey(TblCmpOsIpallocationsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsIpallocations record, @Param("example") TblCmpOsIpallocationsExample example);

    int updateByExample(@Param("record") TblCmpOsIpallocations record, @Param("example") TblCmpOsIpallocationsExample example);

    int updateByPrimaryKeySelective(TblCmpOsIpallocations record);

    int updateByPrimaryKey(TblCmpOsIpallocations record);
}