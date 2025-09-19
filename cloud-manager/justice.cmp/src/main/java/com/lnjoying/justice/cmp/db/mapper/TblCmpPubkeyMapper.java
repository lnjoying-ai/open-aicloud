package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpPubkey;
import com.lnjoying.justice.cmp.db.model.TblCmpPubkeyExample;
import com.lnjoying.justice.cmp.db.model.TblCmpPubkeyKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_PUBKEY)
public interface TblCmpPubkeyMapper {
    long countByExample(TblCmpPubkeyExample example);

    int deleteByExample(TblCmpPubkeyExample example);

    int deleteByPrimaryKey(TblCmpPubkeyKey key);

    int insert(TblCmpPubkey record);

    int insertSelective(TblCmpPubkey record);

    List<TblCmpPubkey> selectByExample(TblCmpPubkeyExample example);

    TblCmpPubkey selectByPrimaryKey(TblCmpPubkeyKey key);

    int updateByExampleSelective(@Param("record") TblCmpPubkey record, @Param("example") TblCmpPubkeyExample example);

    int updateByExample(@Param("record") TblCmpPubkey record, @Param("example") TblCmpPubkeyExample example);

    int updateByPrimaryKeySelective(TblCmpPubkey record);

    int updateByPrimaryKey(TblCmpPubkey record);

    Set<String> getPubkeyIds(@Param("cloudId")String cloudId);
}