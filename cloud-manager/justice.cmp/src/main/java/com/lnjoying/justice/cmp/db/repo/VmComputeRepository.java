package com.lnjoying.justice.cmp.db.repo;

import com.lnjoying.justice.cmp.common.constant.ImageOsType;
import com.lnjoying.justice.cmp.db.mapper.*;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.PciDeviceDetailInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.*;
import com.lnjoying.justice.schema.entity.cmp.RpcGpuResourceMetric;
import com.lnjoying.justice.schema.entity.cmp.RpcGpuVmInstance;
import com.lnjoying.justice.schema.entity.stat.StatusMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class VmComputeRepository
{
	@Autowired
	private TblCmpVmInstanceMapper tblVmInstanceMapper;

	@Autowired
	private TblCmpVmSnapMapper tblVmSnapMapper;

	@Autowired
	private TblCmpHypervisorNodeMapper tblHypervisorNodeMapper;

	@Autowired
	private TblCmpInstanceNetworkRefMapper tblInstanceNetworkRefMapper;

	@Autowired
	private TblCmpDiskInfoMapper tblDiskInfoMapper;

	@Autowired
	private TblCmpResourceStatsMapper tblResourceStatsMapper;

	@Autowired
	private TblCmpPciDeviceGroupMapper tblCmpPciDeviceGroupMapper;

	@Autowired
	private TblCmpPciDeviceMapper tblCmpPciDeviceMapper;

	@Autowired
	private TblCmpDiskSpecMapper tblCmpDiskSpecMapper;

	@Autowired
	private TblCmpInstanceGroupMapper tblCmpInstanceGroupMapper;

	@Autowired
	private TblCmpNfsMapper tblCmpNfsMapper;

	public TblCmpVmInstance getVmInstanceById(String cloudId, String vmInstanceId)
	{
		return tblVmInstanceMapper.selectByPrimaryKey(new TblCmpVmInstanceKey(cloudId, vmInstanceId));
	}

	public int insertVmInstance(TblCmpVmInstance tblVmInstance)
	{
		return tblVmInstanceMapper.insert(tblVmInstance);
	}

	public List<TblCmpVmInstance> getVmInstances(TblCmpVmInstanceExample example)
	{
		return tblVmInstanceMapper.selectByExample(example);
	}

	public long countVmInstancesByExample(TblCmpVmInstanceExample example)
	{
		return tblVmInstanceMapper.countByExample(example);
	}

	public int updateVmInstanceByPrimaryKeySelective(TblCmpVmInstance tblVmInstance)
	{
		return tblVmInstanceMapper.updateByPrimaryKeySelective(tblVmInstance);
	}

	public int updateVmInstanceByPrimaryKey(TblCmpVmInstance tblVmInstance)
	{
		return tblVmInstanceMapper.updateByPrimaryKey(tblVmInstance);
	}

	public int updateVmInstanceByExample(TblCmpVmInstance tblVmInstance, TblCmpVmInstanceExample tblVmInstanceExample)
	{
		return tblVmInstanceMapper.updateByExampleSelective(tblVmInstance, tblVmInstanceExample);
	}

	public Set<String> getVmInstanceIds(String cloudId)
	{
		return tblVmInstanceMapper.getVmInstanceIds(cloudId);
	}

	public int updateVmInstanceByEeIdSelective(TblCmpVmInstance tblVmInstance)
	{
		return tblVmInstanceMapper.updateVmInstanceByEeIdSelective(tblVmInstance);
	}

	public TblCmpVmInstance getVmInstanceByEeId(String cloudId, String eeId)
	{
		return tblVmInstanceMapper.selectByEeId(cloudId, eeId);
	}

	public int insertNetworkInfoAndInstanceId(TblCmpInstanceNetworkRef tblInstanceNetworkRef)
	{
		return tblInstanceNetworkRefMapper.insert(tblInstanceNetworkRef);
	}

	public List<TblCmpInstanceNetworkRef> getNetworkInfoAndInstanceIds(TblCmpInstanceNetworkRefExample example)
	{
		return tblInstanceNetworkRefMapper.selectByExample(example);
	}

	public long countNetworkInfoAndInstanceIdByExample(TblCmpInstanceNetworkRefExample example)
	{
		return tblInstanceNetworkRefMapper.countByExample(example);
	}

	public int updateNetworkInfoAndInstanceId(TblCmpInstanceNetworkRef tblInstanceNetworkRef)
	{
		return tblInstanceNetworkRefMapper.updateByPrimaryKeySelective(tblInstanceNetworkRef);
	}

	public int updateNetworkInfoAndInstanceIdByExample(TblCmpInstanceNetworkRef tblInstanceNetworkRef,TblCmpInstanceNetworkRefExample example)
	{
		return tblInstanceNetworkRefMapper.updateByExampleSelective(tblInstanceNetworkRef,example);
	}

	public Set<String> getInstanceNetworkRefIds(String cloudId, String vmInstanceId)
	{
		return tblInstanceNetworkRefMapper.getInstanceNetworkRefIds(cloudId, vmInstanceId);
	}

	public int deleteInstanceNetworkByPrimaryKey(TblCmpInstanceNetworkRefKey key)
	{
		return tblInstanceNetworkRefMapper.deleteByPrimaryKey(key);
	}

	public TblCmpVmSnap getVmSnapById(String cloudId, String vmSnapId)
	{
		return tblVmSnapMapper.selectByPrimaryKey(new TblCmpVmSnapKey(cloudId, vmSnapId));
	}

	public int insertVmSnap(TblCmpVmSnap tblCmpVmSnap)
	{
		return tblVmSnapMapper.insert(tblCmpVmSnap);
	}

	public List<TblCmpVmSnap> getVmSnaps(TblCmpVmSnapExample example)
	{
		return tblVmSnapMapper.selectByExample(example);
	}

	public long countVmSnapsByExample(TblCmpVmSnapExample example)
	{
		return tblVmSnapMapper.countByExample(example);
	}

	public int updateVmSnap(TblCmpVmSnap tblVmSnap)
	{
		return tblVmSnapMapper.updateByPrimaryKeySelective(tblVmSnap);
	}

	public int updateVmSnapByExample(TblCmpVmSnap tblVmSnap, TblCmpVmSnapExample tblVmSnapExample)
	{
		return tblVmSnapMapper.updateByExampleSelective(tblVmSnap, tblVmSnapExample);
	}

	public Set<String> getSnapIds(String cloudId)
	{
		return tblVmSnapMapper.getSnapIds(cloudId);
	}

	public TblCmpHypervisorNode getHypervisorNodeById(String cloudId, String nodeId)
	{
		return tblHypervisorNodeMapper.selectByPrimaryKey(new TblCmpHypervisorNodeKey(cloudId, nodeId));
	}

	public int insertHypervisorNode(TblCmpHypervisorNode tblHypervisorNode)
	{
		return tblHypervisorNodeMapper.insert(tblHypervisorNode);
	}

	public List<TblCmpHypervisorNode> getHypervisorNodes(TblCmpHypervisorNodeExample example)
	{
		return tblHypervisorNodeMapper.selectByExample(example);
	}

	public long countHypervisorNodeByExample(TblCmpHypervisorNodeExample example)
	{
		return tblHypervisorNodeMapper.countByExample(example);
	}

	public int updateHypervisorNode(TblCmpHypervisorNode tblHypervisorNode)
	{
		return tblHypervisorNodeMapper.updateByPrimaryKeySelective(tblHypervisorNode);
	}

	public Set<String> getNodeIds(String cloudId)
	{
		return tblHypervisorNodeMapper.getNodeIds(cloudId);
	}

	public List<HypervisorNodeInfo> selectCustomNodeInfo(TblCmpHypervisorNodeExample example)
	{
		return tblHypervisorNodeMapper.selectCustomNodeInfo(example);
	}

	public List<HypervisorNodeInfo> selectGpuNodeInfo(TblCmpHypervisorNodeExample example)
	{
		return tblHypervisorNodeMapper.selectGpuNodeInfo(example);
	}

	public List<HypervisorNodeAllocationInfo> selectNodeAllocationInfo(TblCmpHypervisorNodeExample example)
	{
		return tblHypervisorNodeMapper.selectNodeAllocationInfo(example);
	}

	public Integer getIbCount(String flavorGpuName, Integer flavorGpuCount, Boolean flavorNeedIb)
	{
		int ibCount = 0;
		if (needPartition(flavorGpuName))
		{
			switch (flavorGpuCount)
			{
				case 2:
					ibCount = 1;
					break;
				case 4:
					ibCount = 2;
					break;
				case 8:
					ibCount = 4;
					break;
				case 1:
					if (flavorNeedIb) ibCount=1;
					break;
			}
		}
		return ibCount;
	}

	public boolean needPartition(String gpuName)
	{
		return gpuName.contains("H100") || gpuName.contains("H800") || gpuName.contains("A100") || gpuName.contains("A800");
	}

	public List<AvailableGPURsp> selectAvailableGPURspByNameAndCount(String cloudId, String gpuName, Integer gpuCount, Integer index, Integer size, Integer ibCount)
	{
		return tblHypervisorNodeMapper.selectAvailableGPURspByNameAndCount(cloudId, gpuName, gpuCount, index, size, ibCount);
	}

	public long selectTotalAvailableGPURspByNameAndCount(String cloudId, String gpuName, Integer gpuCount)
	{
		return tblHypervisorNodeMapper.selectTotalAvailableGPURspByNameAndCount(cloudId, gpuName, gpuCount);
	}

	public List<AvailableGPURsp> selectAvailableGPURsp(TblCmpHypervisorNodeExample example)
	{
		return tblHypervisorNodeMapper.selectAvailableGPURsp(example);
	}

	public Integer getIbTotalByCloudIdAndGpu(String cloudId, String gpuName)
	{
		return tblHypervisorNodeMapper.getIbTotalByCloudIdAndGpu(cloudId, gpuName);
	}

	public Integer getAvailableIbCountByCloudIdAndGpu(String cloudId, String gpuName)
	{
		return tblHypervisorNodeMapper.getAvailableIbCountByCloudIdAndGpu(cloudId, gpuName);
	}

	public int insertDiskInfo(TblCmpDiskInfo tblDiskInfo)
	{
		return tblDiskInfoMapper.insert(tblDiskInfo);
	}

	public List<TblCmpDiskInfo> getDiskInfos(TblCmpDiskInfoExample example)
	{
		return tblDiskInfoMapper.selectByExample(example);
	}

	public long countDiskInfoByExample(TblCmpDiskInfoExample example)
	{
		return tblDiskInfoMapper.countByExample(example);
	}

	public int updateDiskInfo(TblCmpDiskInfo tblDiskInfo)
	{
		return tblDiskInfoMapper.updateByPrimaryKeySelective(tblDiskInfo);
	}

	public int updateDiskInfoByExample(TblCmpDiskInfo tblDiskInfo, TblCmpDiskInfoExample tblDiskInfoExample)
	{
		return tblDiskInfoMapper.updateByExampleSelective(tblDiskInfo, tblDiskInfoExample);
	}

	public int insertResourceStats(TblCmpResourceStats tblResourceStats)
	{
		return tblResourceStatsMapper.insert(tblResourceStats);
	}

	public List<TblCmpResourceStats> getResourceStats(TblCmpResourceStatsExample example)
	{
		return tblResourceStatsMapper.selectByExample(example);
	}

	public long countResourceStatsByExample(TblCmpResourceStatsExample example)
	{
		return tblResourceStatsMapper.countByExample(example);
	}

	public int updateResourceStats(TblCmpResourceStats tblResourceStats)
	{
		return tblResourceStatsMapper.updateByPrimaryKeySelective(tblResourceStats);
	}

	public int updateResourceStatsByExample(TblCmpResourceStats tblDiskInfo, TblCmpResourceStatsExample tblDiskInfoExample)
	{
		return tblResourceStatsMapper.updateByExampleSelective(tblDiskInfo, tblDiskInfoExample);
	}

	public Set<String> getStatsIds(String cloudId)
	{
		return tblResourceStatsMapper.getStatsIds(cloudId);
	}

	public long sumRootDiskSizeByUserId(String cloudId, String userId)
	{
		return tblVmInstanceMapper.sumRootDiskSizeByUserId(cloudId, userId);
	}

	public long sumDataDiskSizeByUserId(String cloudId, String userId)
	{
		return tblVmInstanceMapper.sumDataDiskSizeByUserId(cloudId, userId);
	}

	public TblCmpPciDeviceGroup getPciDeviceGroupById(String cloudId, String volumeSnapId)
	{
		return tblCmpPciDeviceGroupMapper.selectByPrimaryKey(new TblCmpPciDeviceGroupKey(cloudId, volumeSnapId));
	}

	public int insertPciDeviceGroup(TblCmpPciDeviceGroup tblCmpPciDeviceGroup)
	{
		return tblCmpPciDeviceGroupMapper.insert(tblCmpPciDeviceGroup);
	}

	public List<TblCmpPciDeviceGroup> getPciDeviceGroups(TblCmpPciDeviceGroupExample example)
	{
		return tblCmpPciDeviceGroupMapper.selectByExample(example);
	}

	public long countPciDeviceGroupsByExample(TblCmpPciDeviceGroupExample example)
	{
		return tblCmpPciDeviceGroupMapper.countByExample(example);
	}

	public int updatePciDeviceGroup(TblCmpPciDeviceGroup tblCmpPciDeviceGroup)
	{
		return tblCmpPciDeviceGroupMapper.updateByPrimaryKeySelective(tblCmpPciDeviceGroup);
	}

	public int updatePciDeviceGroupForce(TblCmpPciDeviceGroup tblCmpPciDeviceGroup)
	{
		return tblCmpPciDeviceGroupMapper.updateByPrimaryKey(tblCmpPciDeviceGroup);
	}

    public Set<String> getPciDeviceGroupIds(String cloudId)
    {
        return tblCmpPciDeviceGroupMapper.getPciDeviceGroupIds(cloudId);
    }

	public TblCmpPciDevice getPciDeviceById(String cloudId, String volumeSnapId)
	{
		return tblCmpPciDeviceMapper.selectByPrimaryKey(new TblCmpPciDeviceKey(cloudId, volumeSnapId));
	}

	public int insertPciDevice(TblCmpPciDevice tblCmpPciDevice)
	{
		return tblCmpPciDeviceMapper.insert(tblCmpPciDevice);
	}

	public List<TblCmpPciDevice> getPciDevices(TblCmpPciDeviceExample example)
	{
		return tblCmpPciDeviceMapper.selectByExample(example);
	}

	public long countPciDevicesByExample(TblCmpPciDeviceExample example)
	{
		return tblCmpPciDeviceMapper.countByExample(example);
	}

	public int updatePciDevice(TblCmpPciDevice tblCmpPciDevice)
	{
		return tblCmpPciDeviceMapper.updateByPrimaryKeySelective(tblCmpPciDevice);
	}

	public int updatePciDeviceForce(TblCmpPciDevice tblCmpPciDevice)
	{
		return tblCmpPciDeviceMapper.updateByPrimaryKey(tblCmpPciDevice);
	}

    public Set<String> getPciDeviceIds(String cloudId)
    {
        return tblCmpPciDeviceMapper.getPciDeviceIds(cloudId);
    }

	public List<PciDeviceInfo> selectPCIDevices(TblCmpPciDeviceExample example)
	{
		return tblCmpPciDeviceMapper.selectPCIDevices(example);
	}

	public long countPCIDevices(TblCmpPciDeviceExample example)
	{
		return tblCmpPciDeviceMapper.countPCIDevices(example);
	}

	public List<PciDeviceDetailInfo> selectPciDevices(TblCmpPciDeviceExample example)
	{
		return tblCmpPciDeviceMapper.selectPciDevices(example);
	}

	public List<RpcGpuFlavorInfo> selectGpuFlavorInfo(String cloudId)
	{
		return tblCmpPciDeviceMapper.selectGpuFlavorInfo(cloudId);
	}

	public Long selectPciDeviceCount(String cloudId)
	{
		Long num = tblCmpPciDeviceMapper.selectPciDeviceCount(cloudId);
		return num == null ? 0 : num;
	}

	public List<String> getGpuNames(List<String> cloudIds, String providerId)
	{
		return tblCmpPciDeviceMapper.getGpuNames(cloudIds, providerId);
	}

	public List<RpcGpuResourceMetric> getGpuResourceMetrics(List<String> cloudIds, String gpuName, String providerId)
	{
		return tblCmpPciDeviceMapper.selectGpuResourceMetrics(cloudIds, gpuName, providerId);
	}

	public List<RpcGpuVmInstance> getVmInstanceInfoByCloudIdAndGpu(String cloudId, String gpuName)
	{
		return tblCmpPciDeviceMapper.getVmInstanceInfoByCloudIdAndGpu(cloudId, gpuName);
	}

	public int insertDiskSpec(TblCmpDiskSpec tblCmpDiskSpec)
	{
		return tblCmpDiskSpecMapper.insert(tblCmpDiskSpec);
	}

	public List<TblCmpDiskSpec> getDiskSpecs(TblCmpDiskSpecExample example)
	{
		return tblCmpDiskSpecMapper.selectByExample(example);
	}

	public long countDiskSpecByExample(TblCmpDiskSpecExample example)
	{
		return tblCmpDiskSpecMapper.countByExample(example);
	}

	public TblCmpInstanceGroup getInstanceGroupById(String cloudId, String instanceGroupId)
	{
		return tblCmpInstanceGroupMapper.selectByPrimaryKey(new TblCmpInstanceGroupKey(cloudId, instanceGroupId));
	}

	public int insertInstanceGroup(TblCmpInstanceGroup tblCmpInstanceGroup)
	{
		return tblCmpInstanceGroupMapper.insert(tblCmpInstanceGroup);
	}

	public List<TblCmpInstanceGroup> getInstanceGroups(TblCmpInstanceGroupExample example)
	{
		return tblCmpInstanceGroupMapper.selectByExample(example);
	}

	public long countInstanceGroupsByExample(TblCmpInstanceGroupExample example)
	{
		return tblCmpInstanceGroupMapper.countByExample(example);
	}

	public int updateInstanceGroup(TblCmpInstanceGroup tblCmpInstanceGroup)
	{
		return tblCmpInstanceGroupMapper.updateByPrimaryKeySelective(tblCmpInstanceGroup);
	}

	public int updateInstanceGroupForce(TblCmpInstanceGroup tblCmpInstanceGroup)
	{
		return tblCmpInstanceGroupMapper.updateByPrimaryKey(tblCmpInstanceGroup);
	}

	public Set<String> getInstanceGroupIds(String cloudId)
	{
		return tblCmpInstanceGroupMapper.getInstanceGroupIds(cloudId);
	}

	public Integer getImageOsTypeByVolumeId(String cloudId, String volumeId)
	{
		TblCmpVmInstanceExample	example = new TblCmpVmInstanceExample();
		TblCmpVmInstanceExample.Criteria criteria = example.createCriteria();
		criteria.andCloudIdEqualTo(cloudId);
		criteria.andEeStatusNotEqualTo(REMOVED);
		criteria.andVolumeIdEqualTo(volumeId);
		example.setOrderByClause("create_time desc");
		List<TblCmpVmInstance> tblCmpVmInstances = tblVmInstanceMapper.selectByExample(example);

		if (tblCmpVmInstances == null || tblCmpVmInstances.isEmpty())
		{
			return null;
		}
		else
		{
			return "linux".equals(tblCmpVmInstances.get(0).getOsType())? ImageOsType.LINUX : ImageOsType.WINDOWS;
		}
	}

	public List<StatusMetrics> getVmInstanceStatusMetrics(String cloudId, String userId, String bpId)
	{
		List<StatusMetrics> statusMetricsList = tblVmInstanceMapper.getVmInstanceStatusMetrics(cloudId, userId, bpId);
		return statusMetricsList;
	}

	public TblCmpNfs getNfsById(String cloudId, String nfsId)
	{
		return tblCmpNfsMapper.selectByPrimaryKey(new TblCmpNfsKey(cloudId, nfsId));
	}

	public int insertNfs(TblCmpNfs tblCmpNfs)
	{
		return tblCmpNfsMapper.insert(tblCmpNfs);
	}

	public List<TblCmpNfs> getNfsServers(TblCmpNfsExample example)
	{
		return tblCmpNfsMapper.selectByExample(example);
	}

	public long countNfsServersByExample(TblCmpNfsExample example)
	{
		return tblCmpNfsMapper.countByExample(example);
	}

	public int updateNfs(TblCmpNfs tblCmpNfs)
	{
		return tblCmpNfsMapper.updateByPrimaryKeySelective(tblCmpNfs);
	}

	public int updateNfsByExample(TblCmpNfs tblCmpNfs, TblCmpNfsExample tblCmpNfsExample)
	{
		return tblCmpNfsMapper.updateByExampleSelective(tblCmpNfs, tblCmpNfsExample);
	}

	public Set<String> getNfsIds(String cloudId)
	{
		return tblCmpNfsMapper.getNfsIds(cloudId);
	}
}
