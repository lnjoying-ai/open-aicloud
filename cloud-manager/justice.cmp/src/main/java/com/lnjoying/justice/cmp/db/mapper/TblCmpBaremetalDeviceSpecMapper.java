package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalDeviceSpec;
import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalDeviceSpecExample;
import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalDeviceSpecKey;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_BAREMETAL_DEVICE_SPEC)
public interface TblCmpBaremetalDeviceSpecMapper {
    long countByExample(TblCmpBaremetalDeviceSpecExample example);

    int deleteByExample(TblCmpBaremetalDeviceSpecExample example);

    int deleteByPrimaryKey(TblCmpBaremetalDeviceSpecKey key);

    int insert(TblCmpBaremetalDeviceSpec record);

    int insertSelective(TblCmpBaremetalDeviceSpec record);

    List<TblCmpBaremetalDeviceSpec> selectByExample(TblCmpBaremetalDeviceSpecExample example);

    TblCmpBaremetalDeviceSpec selectByPrimaryKey(TblCmpBaremetalDeviceSpecKey key);

    int updateByExampleSelective(@Param("record") TblCmpBaremetalDeviceSpec record, @Param("example") TblCmpBaremetalDeviceSpecExample example);

    int updateByExample(@Param("record") TblCmpBaremetalDeviceSpec record, @Param("example") TblCmpBaremetalDeviceSpecExample example);

    int updateByPrimaryKeySelective(TblCmpBaremetalDeviceSpec record);

    int updateByPrimaryKey(TblCmpBaremetalDeviceSpec record);

    List<Integer> selectCpuNum(@Param("cloudId")String cloudId);

    List<Long> selectMemTotal(@Param("cloudId")String cloudId);
}