package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsInstanceFaults;
import com.lnjoying.justice.cmp.db.model.TblCmpOsInstanceFaultsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsInstanceFaultsKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsInstanceFaultsMapper {
    long countByExample(TblCmpOsInstanceFaultsExample example);

    int deleteByExample(TblCmpOsInstanceFaultsExample example);

    int deleteByPrimaryKey(TblCmpOsInstanceFaultsKey key);

    int insert(TblCmpOsInstanceFaults record);

    int insertSelective(TblCmpOsInstanceFaults record);

    List<TblCmpOsInstanceFaults> selectByExample(TblCmpOsInstanceFaultsExample example);

    TblCmpOsInstanceFaults selectByPrimaryKey(TblCmpOsInstanceFaultsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsInstanceFaults record, @Param("example") TblCmpOsInstanceFaultsExample example);

    int updateByExample(@Param("record") TblCmpOsInstanceFaults record, @Param("example") TblCmpOsInstanceFaultsExample example);

    int updateByPrimaryKeySelective(TblCmpOsInstanceFaults record);

    int updateByPrimaryKey(TblCmpOsInstanceFaults record);
}