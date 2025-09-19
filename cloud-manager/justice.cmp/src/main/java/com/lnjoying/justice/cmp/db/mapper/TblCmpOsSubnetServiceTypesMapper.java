package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsSubnetServiceTypes;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSubnetServiceTypesExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSubnetServiceTypesKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsSubnetServiceTypesMapper {
    long countByExample(TblCmpOsSubnetServiceTypesExample example);

    int deleteByExample(TblCmpOsSubnetServiceTypesExample example);

    int deleteByPrimaryKey(TblCmpOsSubnetServiceTypesKey key);

    int insert(TblCmpOsSubnetServiceTypes record);

    int insertSelective(TblCmpOsSubnetServiceTypes record);

    List<TblCmpOsSubnetServiceTypes> selectByExample(TblCmpOsSubnetServiceTypesExample example);

    TblCmpOsSubnetServiceTypes selectByPrimaryKey(TblCmpOsSubnetServiceTypesKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsSubnetServiceTypes record, @Param("example") TblCmpOsSubnetServiceTypesExample example);

    int updateByExample(@Param("record") TblCmpOsSubnetServiceTypes record, @Param("example") TblCmpOsSubnetServiceTypesExample example);

    int updateByPrimaryKeySelective(TblCmpOsSubnetServiceTypes record);

    int updateByPrimaryKey(TblCmpOsSubnetServiceTypes record);
}