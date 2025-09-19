package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumeTypes;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumeTypesExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumeTypesKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_VOLUME_TYPE)
public interface TblCmpOsVolumeTypesMapper {
    long countByExample(TblCmpOsVolumeTypesExample example);

    int deleteByExample(TblCmpOsVolumeTypesExample example);

    int deleteByPrimaryKey(TblCmpOsVolumeTypesKey key);

    int insert(TblCmpOsVolumeTypes record);

    int insertSelective(TblCmpOsVolumeTypes record);

    List<TblCmpOsVolumeTypes> selectByExample(TblCmpOsVolumeTypesExample example);

    TblCmpOsVolumeTypes selectByPrimaryKey(TblCmpOsVolumeTypesKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsVolumeTypes record, @Param("example") TblCmpOsVolumeTypesExample example);

    int updateByExample(@Param("record") TblCmpOsVolumeTypes record, @Param("example") TblCmpOsVolumeTypesExample example);

    int updateByPrimaryKeySelective(TblCmpOsVolumeTypes record);

    int updateByPrimaryKey(TblCmpOsVolumeTypes record);

    Set<String> getVolumeTypeIds(@Param("cloudId")String cloudId);
}