package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpNicSpec;
import com.lnjoying.justice.cmp.db.model.TblCmpNicSpecExample;
import com.lnjoying.justice.cmp.db.model.TblCmpNicSpecKey;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_NIC_SPEC)
public interface TblCmpNicSpecMapper {
    long countByExample(TblCmpNicSpecExample example);

    int deleteByExample(TblCmpNicSpecExample example);

    int deleteByPrimaryKey(TblCmpNicSpecKey key);

    int insert(TblCmpNicSpec record);

    int insertSelective(TblCmpNicSpec record);

    List<TblCmpNicSpec> selectByExample(TblCmpNicSpecExample example);

    TblCmpNicSpec selectByPrimaryKey(TblCmpNicSpecKey key);

    int updateByExampleSelective(@Param("record") TblCmpNicSpec record, @Param("example") TblCmpNicSpecExample example);

    int updateByExample(@Param("record") TblCmpNicSpec record, @Param("example") TblCmpNicSpecExample example);

    int updateByPrimaryKeySelective(TblCmpNicSpec record);

    int updateByPrimaryKey(TblCmpNicSpec record);
}