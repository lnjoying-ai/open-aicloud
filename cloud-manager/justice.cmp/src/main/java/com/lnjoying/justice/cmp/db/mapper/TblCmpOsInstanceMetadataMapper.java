package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsInstanceMetadata;
import com.lnjoying.justice.cmp.db.model.TblCmpOsInstanceMetadataExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsInstanceMetadataKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsInstanceMetadataMapper {
    long countByExample(TblCmpOsInstanceMetadataExample example);

    int deleteByExample(TblCmpOsInstanceMetadataExample example);

    int deleteByPrimaryKey(TblCmpOsInstanceMetadataKey key);

    int insert(TblCmpOsInstanceMetadata record);

    int insertSelective(TblCmpOsInstanceMetadata record);

    List<TblCmpOsInstanceMetadata> selectByExample(TblCmpOsInstanceMetadataExample example);

    TblCmpOsInstanceMetadata selectByPrimaryKey(TblCmpOsInstanceMetadataKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsInstanceMetadata record, @Param("example") TblCmpOsInstanceMetadataExample example);

    int updateByExample(@Param("record") TblCmpOsInstanceMetadata record, @Param("example") TblCmpOsInstanceMetadataExample example);

    int updateByPrimaryKeySelective(TblCmpOsInstanceMetadata record);

    int updateByPrimaryKey(TblCmpOsInstanceMetadata record);
}