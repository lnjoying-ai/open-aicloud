package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpPciDevice;
import com.lnjoying.justice.cmp.db.model.TblCmpPciDeviceExample;
import com.lnjoying.justice.cmp.db.model.TblCmpPciDeviceKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.PciDeviceDetailInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.PciDeviceInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.RpcGpuFlavorInfo;
import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.schema.entity.cmp.RpcGpuResourceMetric;
import com.lnjoying.justice.schema.entity.cmp.RpcGpuVmInstance;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_PCI_DEVICE)
public interface TblCmpPciDeviceMapper {
    long countByExample(TblCmpPciDeviceExample example);

    int deleteByExample(TblCmpPciDeviceExample example);

    int deleteByPrimaryKey(TblCmpPciDeviceKey key);

    int insert(TblCmpPciDevice record);

    int insertSelective(TblCmpPciDevice record);

    List<TblCmpPciDevice> selectByExample(TblCmpPciDeviceExample example);

    TblCmpPciDevice selectByPrimaryKey(TblCmpPciDeviceKey key);

    int updateByExampleSelective(@Param("record") TblCmpPciDevice record, @Param("example") TblCmpPciDeviceExample example);

    int updateByExample(@Param("record") TblCmpPciDevice record, @Param("example") TblCmpPciDeviceExample example);

    int updateByPrimaryKeySelective(TblCmpPciDevice record);

    int updateByPrimaryKey(TblCmpPciDevice record);

    List<PciDeviceInfo> selectPCIDevices(TblCmpPciDeviceExample example);

    long countPCIDevices(TblCmpPciDeviceExample example);

    List<PciDeviceDetailInfo> selectPciDevices(TblCmpPciDeviceExample example);

    Set<String> getPciDeviceIds(@Param("cloudId")String cloudId);

    List<RpcGpuFlavorInfo> selectGpuFlavorInfo(@Param("cloudId")String cloudId);

    Long selectPciDeviceCount(@Param("cloudId")String cloudId);

    List<String> getGpuNames(@Param("cloudIds")List<String> cloudIds, @Param("providerId")String providerId);

    List<RpcGpuResourceMetric> selectGpuResourceMetrics(@Param("cloudIds")List<String> cloudIds, @Param("gpuName")String gpuName, @Param("providerId")String providerId);

    List<RpcGpuVmInstance> getVmInstanceInfoByCloudIdAndGpu(@Param("cloudId")String cloudId, @Param("gpuName")String gpuName);
}