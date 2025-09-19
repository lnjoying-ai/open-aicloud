package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpNfs;
import com.lnjoying.justice.cmp.db.model.TblCmpNfsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpNfsKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_NFS_SERVER)
public interface TblCmpNfsMapper {
    long countByExample(TblCmpNfsExample example);

    int deleteByExample(TblCmpNfsExample example);

    int deleteByPrimaryKey(TblCmpNfsKey key);

    int insert(TblCmpNfs record);

    int insertSelective(TblCmpNfs record);

    List<TblCmpNfs> selectByExample(TblCmpNfsExample example);

    TblCmpNfs selectByPrimaryKey(TblCmpNfsKey key);

    int updateByExampleSelective(@Param("record") TblCmpNfs record, @Param("example") TblCmpNfsExample example);

    int updateByExample(@Param("record") TblCmpNfs record, @Param("example") TblCmpNfsExample example);

    int updateByPrimaryKeySelective(TblCmpNfs record);

    int updateByPrimaryKey(TblCmpNfs record);

    Set<String> getNfsIds(@Param("cloudId")String cloudId);
}