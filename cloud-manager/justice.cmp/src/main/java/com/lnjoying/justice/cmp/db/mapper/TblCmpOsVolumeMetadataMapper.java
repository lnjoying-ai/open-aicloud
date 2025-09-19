package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumeMetadata;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumeMetadataExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumeMetadataKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsVolumeMetadataMapper {
    long countByExample(TblCmpOsVolumeMetadataExample example);

    int deleteByExample(TblCmpOsVolumeMetadataExample example);

    int deleteByPrimaryKey(TblCmpOsVolumeMetadataKey key);

    int insert(TblCmpOsVolumeMetadata record);

    int insertSelective(TblCmpOsVolumeMetadata record);

    List<TblCmpOsVolumeMetadata> selectByExample(TblCmpOsVolumeMetadataExample example);

    TblCmpOsVolumeMetadata selectByPrimaryKey(TblCmpOsVolumeMetadataKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsVolumeMetadata record, @Param("example") TblCmpOsVolumeMetadataExample example);

    int updateByExample(@Param("record") TblCmpOsVolumeMetadata record, @Param("example") TblCmpOsVolumeMetadataExample example);

    int updateByPrimaryKeySelective(TblCmpOsVolumeMetadata record);

    int updateByPrimaryKey(TblCmpOsVolumeMetadata record);
}