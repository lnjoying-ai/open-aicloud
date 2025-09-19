package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsFloatingips;
import com.lnjoying.justice.cmp.db.model.TblCmpOsFloatingipsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsFloatingipsKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_FLOATINGIP)
public interface TblCmpOsFloatingipsMapper {
    long countByExample(TblCmpOsFloatingipsExample example);

    int deleteByExample(TblCmpOsFloatingipsExample example);

    int deleteByPrimaryKey(TblCmpOsFloatingipsKey key);

    int insert(TblCmpOsFloatingips record);

    int insertSelective(TblCmpOsFloatingips record);

    List<TblCmpOsFloatingips> selectByExample(TblCmpOsFloatingipsExample example);

    TblCmpOsFloatingips selectByPrimaryKey(TblCmpOsFloatingipsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsFloatingips record, @Param("example") TblCmpOsFloatingipsExample example);

    int updateByExample(@Param("record") TblCmpOsFloatingips record, @Param("example") TblCmpOsFloatingipsExample example);

    int updateByPrimaryKeySelective(TblCmpOsFloatingips record);

    int updateByPrimaryKey(TblCmpOsFloatingips record);

    int updateByPrimaryKeyEeSelective(TblCmpOsFloatingips record);

    Set<String> getFloatingipIds(@Param("cloudId")String cloudId);
}