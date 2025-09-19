package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsKeyPairs;
import com.lnjoying.justice.cmp.db.model.TblCmpOsKeyPairsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsKeyPairsKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.cmp.db.model.TblCmpOsKeyPairsUniqueKey;
import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_KEYPAIR)
public interface TblCmpOsKeyPairsMapper {
    long countByExample(TblCmpOsKeyPairsExample example);

    int deleteByExample(TblCmpOsKeyPairsExample example);

    int deleteByPrimaryKey(TblCmpOsKeyPairsKey key);

    int insert(TblCmpOsKeyPairs record);

    int insertSelective(TblCmpOsKeyPairs record);

    List<TblCmpOsKeyPairs> selectByExample(TblCmpOsKeyPairsExample example);

    TblCmpOsKeyPairs selectByPrimaryKey(TblCmpOsKeyPairsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsKeyPairs record, @Param("example") TblCmpOsKeyPairsExample example);

    int updateByExample(@Param("record") TblCmpOsKeyPairs record, @Param("example") TblCmpOsKeyPairsExample example);

    int updateByPrimaryKeySelective(TblCmpOsKeyPairs record);

    int updateByPrimaryKey(TblCmpOsKeyPairs record);

    TblCmpOsKeyPairs selectByUniqueKey(TblCmpOsKeyPairsUniqueKey key);

    int updateByUniqueKeySelective(TblCmpOsKeyPairs record);

    Set<String> getKeyPairNames(@Param("cloudId")String cloudId);
}