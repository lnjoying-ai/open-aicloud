package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumes;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumesExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumesKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_VOLUME)
public interface TblCmpOsVolumesMapper {
    long countByExample(TblCmpOsVolumesExample example);

    int deleteByExample(TblCmpOsVolumesExample example);

    int deleteByPrimaryKey(TblCmpOsVolumesKey key);

    int insert(TblCmpOsVolumes record);

    int insertSelective(TblCmpOsVolumes record);

    List<TblCmpOsVolumes> selectByExample(TblCmpOsVolumesExample example);

    TblCmpOsVolumes selectByPrimaryKey(TblCmpOsVolumesKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsVolumes record, @Param("example") TblCmpOsVolumesExample example);

    int updateByExample(@Param("record") TblCmpOsVolumes record, @Param("example") TblCmpOsVolumesExample example);

    int updateByPrimaryKeySelective(TblCmpOsVolumes record);

    int updateByPrimaryKey(TblCmpOsVolumes record);

    Set<String> getVolumeIds(@Param("cloudId")String cloudId);
}