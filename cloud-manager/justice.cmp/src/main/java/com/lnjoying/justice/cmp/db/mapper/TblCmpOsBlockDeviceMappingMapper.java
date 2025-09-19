package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsBlockDeviceMapping;
import com.lnjoying.justice.cmp.db.model.TblCmpOsBlockDeviceMappingExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsBlockDeviceMappingKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsBlockDeviceMappingMapper {
    long countByExample(TblCmpOsBlockDeviceMappingExample example);

    int deleteByExample(TblCmpOsBlockDeviceMappingExample example);

    int deleteByPrimaryKey(TblCmpOsBlockDeviceMappingKey key);

    int insert(TblCmpOsBlockDeviceMapping record);

    int insertSelective(TblCmpOsBlockDeviceMapping record);

    List<TblCmpOsBlockDeviceMapping> selectByExample(TblCmpOsBlockDeviceMappingExample example);

    TblCmpOsBlockDeviceMapping selectByPrimaryKey(TblCmpOsBlockDeviceMappingKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsBlockDeviceMapping record, @Param("example") TblCmpOsBlockDeviceMappingExample example);

    int updateByExample(@Param("record") TblCmpOsBlockDeviceMapping record, @Param("example") TblCmpOsBlockDeviceMappingExample example);

    int updateByPrimaryKeySelective(TblCmpOsBlockDeviceMapping record);

    int updateByPrimaryKey(TblCmpOsBlockDeviceMapping record);

    TblCmpOsBlockDeviceMapping selectByVolumeId(@Param("cloudId") String cloudId, @Param("volumeId") String volumeId);

    int updateByVolumeIdSelective(TblCmpOsBlockDeviceMapping record);
}