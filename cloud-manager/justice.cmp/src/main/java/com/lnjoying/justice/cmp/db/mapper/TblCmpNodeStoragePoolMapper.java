package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpNodeStoragePool;
import com.lnjoying.justice.cmp.db.model.TblCmpNodeStoragePoolExample;
import com.lnjoying.justice.cmp.db.model.TblCmpNodeStoragePoolKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpNodeStoragePoolMapper {
    long countByExample(TblCmpNodeStoragePoolExample example);

    int deleteByExample(TblCmpNodeStoragePoolExample example);

    int deleteByPrimaryKey(TblCmpNodeStoragePoolKey key);

    int insert(TblCmpNodeStoragePool record);

    int insertSelective(TblCmpNodeStoragePool record);

    List<TblCmpNodeStoragePool> selectByExample(TblCmpNodeStoragePoolExample example);

    TblCmpNodeStoragePool selectByPrimaryKey(TblCmpNodeStoragePoolKey key);

    int updateByExampleSelective(@Param("record") TblCmpNodeStoragePool record, @Param("example") TblCmpNodeStoragePoolExample example);

    int updateByExample(@Param("record") TblCmpNodeStoragePool record, @Param("example") TblCmpNodeStoragePoolExample example);

    int updateByPrimaryKeySelective(TblCmpNodeStoragePool record);

    int updateByPrimaryKey(TblCmpNodeStoragePool record);
}