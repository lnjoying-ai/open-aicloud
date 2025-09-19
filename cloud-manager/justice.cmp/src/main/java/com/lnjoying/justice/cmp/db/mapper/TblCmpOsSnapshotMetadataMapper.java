package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsSnapshotMetadata;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSnapshotMetadataExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSnapshotMetadataKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsSnapshotMetadataMapper {
    long countByExample(TblCmpOsSnapshotMetadataExample example);

    int deleteByExample(TblCmpOsSnapshotMetadataExample example);

    int deleteByPrimaryKey(TblCmpOsSnapshotMetadataKey key);

    int insert(TblCmpOsSnapshotMetadata record);

    int insertSelective(TblCmpOsSnapshotMetadata record);

    List<TblCmpOsSnapshotMetadata> selectByExample(TblCmpOsSnapshotMetadataExample example);

    TblCmpOsSnapshotMetadata selectByPrimaryKey(TblCmpOsSnapshotMetadataKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsSnapshotMetadata record, @Param("example") TblCmpOsSnapshotMetadataExample example);

    int updateByExample(@Param("record") TblCmpOsSnapshotMetadata record, @Param("example") TblCmpOsSnapshotMetadataExample example);

    int updateByPrimaryKeySelective(TblCmpOsSnapshotMetadata record);

    int updateByPrimaryKey(TblCmpOsSnapshotMetadata record);
}