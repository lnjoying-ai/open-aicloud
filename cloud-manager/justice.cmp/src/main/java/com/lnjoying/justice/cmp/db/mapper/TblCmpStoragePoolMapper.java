package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpStoragePool;
import com.lnjoying.justice.cmp.db.model.TblCmpStoragePoolExample;
import com.lnjoying.justice.cmp.db.model.TblCmpStoragePoolKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_STORAGE_POOL)
public interface TblCmpStoragePoolMapper {
    long countByExample(TblCmpStoragePoolExample example);

    int deleteByExample(TblCmpStoragePoolExample example);

    int deleteByPrimaryKey(TblCmpStoragePoolKey key);

    int insert(TblCmpStoragePool record);

    int insertSelective(TblCmpStoragePool record);

    List<TblCmpStoragePool> selectByExample(TblCmpStoragePoolExample example);

    TblCmpStoragePool selectByPrimaryKey(TblCmpStoragePoolKey key);

    int updateByExampleSelective(@Param("record") TblCmpStoragePool record, @Param("example") TblCmpStoragePoolExample example);

    int updateByExample(@Param("record") TblCmpStoragePool record, @Param("example") TblCmpStoragePoolExample example);

    int updateByPrimaryKeySelective(TblCmpStoragePool record);

    int updateByPrimaryKey(TblCmpStoragePool record);

    Set<String> getStoragePoolIds(@Param("cloudId")String cloudId);
}