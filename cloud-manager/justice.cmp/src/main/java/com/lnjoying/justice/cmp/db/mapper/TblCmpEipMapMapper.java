package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpEipMap;
import com.lnjoying.justice.cmp.db.model.TblCmpEipMapExample;
import com.lnjoying.justice.cmp.db.model.TblCmpEipMapKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_EIP_MAP)
public interface TblCmpEipMapMapper {
    long countByExample(TblCmpEipMapExample example);

    int deleteByExample(TblCmpEipMapExample example);

    int deleteByPrimaryKey(TblCmpEipMapKey key);

    int insert(TblCmpEipMap record);

    int insertSelective(TblCmpEipMap record);

    List<TblCmpEipMap> selectByExample(TblCmpEipMapExample example);

    TblCmpEipMap selectByPrimaryKey(TblCmpEipMapKey key);

    int updateByExampleSelective(@Param("record") TblCmpEipMap record, @Param("example") TblCmpEipMapExample example);

    int updateByExample(@Param("record") TblCmpEipMap record, @Param("example") TblCmpEipMapExample example);

    int updateByPrimaryKeySelective(TblCmpEipMap record);

    int updateByPrimaryKey(TblCmpEipMap record);

    Set<String> getEipMapIds(@Param("cloudId")String cloudId);
}