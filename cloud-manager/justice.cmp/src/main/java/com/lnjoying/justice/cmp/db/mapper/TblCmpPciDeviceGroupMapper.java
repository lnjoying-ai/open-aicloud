package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpPciDeviceGroup;
import com.lnjoying.justice.cmp.db.model.TblCmpPciDeviceGroupExample;
import com.lnjoying.justice.cmp.db.model.TblCmpPciDeviceGroupKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_PCI_DEVICE_GROUP)
public interface TblCmpPciDeviceGroupMapper {
    long countByExample(TblCmpPciDeviceGroupExample example);

    int deleteByExample(TblCmpPciDeviceGroupExample example);

    int deleteByPrimaryKey(TblCmpPciDeviceGroupKey key);

    int insert(TblCmpPciDeviceGroup record);

    int insertSelective(TblCmpPciDeviceGroup record);

    List<TblCmpPciDeviceGroup> selectByExample(TblCmpPciDeviceGroupExample example);

    TblCmpPciDeviceGroup selectByPrimaryKey(TblCmpPciDeviceGroupKey key);

    int updateByExampleSelective(@Param("record") TblCmpPciDeviceGroup record, @Param("example") TblCmpPciDeviceGroupExample example);

    int updateByExample(@Param("record") TblCmpPciDeviceGroup record, @Param("example") TblCmpPciDeviceGroupExample example);

    int updateByPrimaryKeySelective(TblCmpPciDeviceGroup record);

    int updateByPrimaryKey(TblCmpPciDeviceGroup record);

    Set<String> getPciDeviceGroupIds(@Param("cloudId")String cloudId);
}