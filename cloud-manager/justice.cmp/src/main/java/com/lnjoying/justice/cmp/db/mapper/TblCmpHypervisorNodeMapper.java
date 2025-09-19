package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpHypervisorNode;
import com.lnjoying.justice.cmp.db.model.TblCmpHypervisorNodeExample;
import com.lnjoying.justice.cmp.db.model.TblCmpHypervisorNodeKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.AvailableGPURsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.HypervisorNodeAllocationInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.HypervisorNodeInfo;
import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_HYPERVISOR_NODE)
public interface TblCmpHypervisorNodeMapper {
    long countByExample(TblCmpHypervisorNodeExample example);

    int deleteByExample(TblCmpHypervisorNodeExample example);

    int deleteByPrimaryKey(TblCmpHypervisorNodeKey key);

    int insert(TblCmpHypervisorNode record);

    int insertSelective(TblCmpHypervisorNode record);

    List<TblCmpHypervisorNode> selectByExample(TblCmpHypervisorNodeExample example);

    TblCmpHypervisorNode selectByPrimaryKey(TblCmpHypervisorNodeKey key);

    int updateByExampleSelective(@Param("record") TblCmpHypervisorNode record, @Param("example") TblCmpHypervisorNodeExample example);

    int updateByExample(@Param("record") TblCmpHypervisorNode record, @Param("example") TblCmpHypervisorNodeExample example);

    int updateByPrimaryKeySelective(TblCmpHypervisorNode record);

    int updateByPrimaryKey(TblCmpHypervisorNode record);

    Set<String> getNodeIds(@Param("cloudId")String cloudId);

    List<HypervisorNodeInfo> selectCustomNodeInfo(TblCmpHypervisorNodeExample example);

    List<HypervisorNodeInfo> selectGpuNodeInfo(TblCmpHypervisorNodeExample example);

    List<HypervisorNodeAllocationInfo> selectNodeAllocationInfo(TblCmpHypervisorNodeExample example);

    List<AvailableGPURsp> selectAvailableGPURspByNameAndCount(@Param("cloudId")String cloudId, @Param("gpuName")String gpuName, @Param("gpuCount")Integer gpuCount, @Param("index")Integer index, @Param("size")Integer size, @Param("ibCount")Integer ibCount);

    long selectTotalAvailableGPURspByNameAndCount(@Param("cloudId")String cloudId, @Param("gpuName")String gpuName, @Param("gpuCount")Integer gpuCount);

    List<AvailableGPURsp> selectAvailableGPURsp(TblCmpHypervisorNodeExample example);

    int getIbTotalByCloudIdAndGpu(@Param("cloudId")String cloudId, @Param("gpuName")String gpuName);

    int getAvailableIbCountByCloudIdAndGpu(@Param("cloudId")String cloudId, @Param("gpuName")String gpuName);
}