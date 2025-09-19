package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsDvrHostMacs;
import com.lnjoying.justice.cmp.db.model.TblCmpOsDvrHostMacsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsDvrHostMacsKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsDvrHostMacsMapper {
    long countByExample(TblCmpOsDvrHostMacsExample example);

    int deleteByExample(TblCmpOsDvrHostMacsExample example);

    int deleteByPrimaryKey(TblCmpOsDvrHostMacsKey key);

    int insert(TblCmpOsDvrHostMacs record);

    int insertSelective(TblCmpOsDvrHostMacs record);

    List<TblCmpOsDvrHostMacs> selectByExample(TblCmpOsDvrHostMacsExample example);

    TblCmpOsDvrHostMacs selectByPrimaryKey(TblCmpOsDvrHostMacsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsDvrHostMacs record, @Param("example") TblCmpOsDvrHostMacsExample example);

    int updateByExample(@Param("record") TblCmpOsDvrHostMacs record, @Param("example") TblCmpOsDvrHostMacsExample example);

    int updateByPrimaryKeySelective(TblCmpOsDvrHostMacs record);

    int updateByPrimaryKey(TblCmpOsDvrHostMacs record);
}