package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsPorts;
import com.lnjoying.justice.cmp.db.model.TblCmpOsPortsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsPortsKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_PORT)
public interface TblCmpOsPortsMapper {
    long countByExample(TblCmpOsPortsExample example);

    int deleteByExample(TblCmpOsPortsExample example);

    int deleteByPrimaryKey(TblCmpOsPortsKey key);

    int insert(TblCmpOsPorts record);

    int insertSelective(TblCmpOsPorts record);

    List<TblCmpOsPorts> selectByExample(TblCmpOsPortsExample example);

    TblCmpOsPorts selectByPrimaryKey(TblCmpOsPortsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsPorts record, @Param("example") TblCmpOsPortsExample example);

    int updateByExample(@Param("record") TblCmpOsPorts record, @Param("example") TblCmpOsPortsExample example);

    int updateByPrimaryKeySelective(TblCmpOsPorts record);

    int updateByPrimaryKey(TblCmpOsPorts record);

    Set<String> getPortIds(@Param("cloudId")String cloudId, @Param("deviceId")String deviceId);

    List<TblCmpOsPorts> selectByExampleWithShared(@Param("example")TblCmpOsPortsExample example, @Param("cloudId")String cloudId, @Param("projectId")String projectId, @Param("userId")String userId);
}