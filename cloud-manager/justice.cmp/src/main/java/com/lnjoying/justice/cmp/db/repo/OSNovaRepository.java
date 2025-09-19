package com.lnjoying.justice.cmp.db.repo;

import com.lnjoying.justice.cmp.db.mapper.*;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.schema.entity.stat.GetStatusMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.StatusMetrics;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class OSNovaRepository
{
	@Autowired
	private TblCmpOsFlavorsMapper tblCmpOsFlavorsMapper;

	@Autowired
	private TblCmpOsFlavorExtraSpecsMapper tblCmpOsFlavorExtraSpecsMapper;

	@Autowired
	private TblCmpOsKeyPairsMapper tblCmpOsKeyPairsMapper;

	@Autowired
	private TblCmpOsInstancesMapper tblCmpOsInstancesMapper;

	@Autowired
	private TblCmpOsInstanceExtraMapper  tblCmpOsInstanceExtraMapper;

	@Autowired
	private TblCmpOsInstanceFaultsMapper tblCmpOsInstanceFaultsMapper;

	@Autowired
	private TblCmpOsInstanceMetadataMapper tblCmpOsInstanceMetadataMapper;

	@Autowired
	private TblCmpOsBlockDeviceMappingMapper tblCmpOsBlockDeviceMappingMapper;

	public TblCmpOsFlavors getFlavorById(String cloudId, String flavorid)
	{
		return tblCmpOsFlavorsMapper.selectByPrimaryKey(new TblCmpOsFlavorsKey(cloudId, flavorid));
	}

	public int insertFlavor(TblCmpOsFlavors tblCmpOsFlavors)
	{
		return tblCmpOsFlavorsMapper.insert(tblCmpOsFlavors);
	}

	public List<TblCmpOsFlavors> getFlavors(TblCmpOsFlavorsExample example)
	{
		return tblCmpOsFlavorsMapper.selectByExample(example);
	}

	public long countFlavorsByExample(TblCmpOsFlavorsExample example)
	{
		return tblCmpOsFlavorsMapper.countByExample(example);
	}

	public int updateFlavor(TblCmpOsFlavors tblCmpOsFlavors)
	{
		return tblCmpOsFlavorsMapper.updateByPrimaryKeySelective(tblCmpOsFlavors);
	}

	public int deleteFlavor(String cloudId, String flavorId)
	{
		TblCmpOsFlavors tblCmpOsFlavor = new TblCmpOsFlavors();
		tblCmpOsFlavor.setCloudId(cloudId);
		tblCmpOsFlavor.setFlavorid(flavorId);
		tblCmpOsFlavor.setEeStatus(REMOVED);
		return updateFlavor(tblCmpOsFlavor);
	}

	public Set<String> getFlavorIds(String cloudId)
	{
		return tblCmpOsFlavorsMapper.getFlavorIds(cloudId);
	}

	public TblCmpOsFlavorExtraSpecs getFlavorExtraSpecById(String cloudId, String flavorId, String key)
	{
		return tblCmpOsFlavorExtraSpecsMapper.selectByPrimaryKey(new TblCmpOsFlavorExtraSpecsKey(cloudId, flavorId, key));
	}

	public int insertFlavorExtraSpec(TblCmpOsFlavorExtraSpecs tblCmpOsFlavorExtraSpecs)
	{
		return tblCmpOsFlavorExtraSpecsMapper.insert(tblCmpOsFlavorExtraSpecs);
	}

	public List<TblCmpOsFlavorExtraSpecs> getFlavorExtraSpecs(TblCmpOsFlavorExtraSpecsExample example)
	{
		return tblCmpOsFlavorExtraSpecsMapper.selectByExample(example);
	}

	public long countFlavorExtraSpecsByExample(TblCmpOsFlavorExtraSpecsExample example)
	{
		return tblCmpOsFlavorExtraSpecsMapper.countByExample(example);
	}

	public int updateFlavorExtraSpec(TblCmpOsFlavorExtraSpecs tblCmpOsFlavorExtraSpecs)
	{
		return tblCmpOsFlavorExtraSpecsMapper.updateByPrimaryKeySelective(tblCmpOsFlavorExtraSpecs);
	}

	public Set<String> getFlavorExtraSpecKeys(String cloudId, String flavorId)
	{
		return tblCmpOsFlavorExtraSpecsMapper.getFlavorExtraSpecKeys(cloudId, flavorId);
	}

	public TblCmpOsKeyPairs getKeyPairById(String cloudId, Integer id)
	{
		return tblCmpOsKeyPairsMapper.selectByPrimaryKey(new TblCmpOsKeyPairsKey(cloudId, id));
	}

	public TblCmpOsKeyPairs getKeyPairByName(String cloudId, String keypairName)
	{
		return tblCmpOsKeyPairsMapper.selectByUniqueKey(new TblCmpOsKeyPairsUniqueKey(cloudId, keypairName));
	}

	public int insertKeyPair(TblCmpOsKeyPairs tblCmpOsKeyPairs)
	{
		return tblCmpOsKeyPairsMapper.insert(tblCmpOsKeyPairs);
	}

	public List<TblCmpOsKeyPairs> getKeyPairs(TblCmpOsKeyPairsExample example)
	{
		return tblCmpOsKeyPairsMapper.selectByExample(example);
	}

	public long countKeyPairsByExample(TblCmpOsKeyPairsExample example)
	{
		return tblCmpOsKeyPairsMapper.countByExample(example);
	}

	public int updateKeyPair(TblCmpOsKeyPairs tblCmpOsKeyPairs)
	{
		int res = tblCmpOsKeyPairsMapper.updateByPrimaryKeySelective(tblCmpOsKeyPairs);
		if (res == 0)
		{
			res = tblCmpOsKeyPairsMapper.updateByUniqueKeySelective(tblCmpOsKeyPairs);
		}
		return res;
	}

	public Set<String> getKeyPairNames(String cloudId)
	{
		return tblCmpOsKeyPairsMapper.getKeyPairNames(cloudId);
	}

	public int deleteKeyPair(String cloudId, String keypairName)
	{
		TblCmpOsKeyPairs tblCmpOsKeyPair = new TblCmpOsKeyPairs();
		tblCmpOsKeyPair.setCloudId(cloudId);
		tblCmpOsKeyPair.setName(keypairName);
		tblCmpOsKeyPair.setEeStatus(REMOVED);
		return updateKeyPair(tblCmpOsKeyPair);
	}

	public TblCmpOsInstances getInstanceById(String cloudId, String uuid)
	{
		return tblCmpOsInstancesMapper.selectByPrimaryKey(new TblCmpOsInstancesKey(cloudId, uuid));
	}

	public int insertInstance(TblCmpOsInstances tblCmpOsInstances)
	{
		return tblCmpOsInstancesMapper.insert(tblCmpOsInstances);
	}

	public List<TblCmpOsInstances> getInstances(TblCmpOsInstancesExample example)
	{
		return tblCmpOsInstancesMapper.selectByExample(example);
	}

	public long countInstancesByExample(TblCmpOsInstancesExample example)
	{
		return tblCmpOsInstancesMapper.countByExample(example);
	}

	public int updateInstance(TblCmpOsInstances tblCmpOsInstances)
	{
		return tblCmpOsInstancesMapper.updateByPrimaryKeySelective(tblCmpOsInstances);
	}

	public int deleteInstance(String cloudId, String serverId)
	{
		TblCmpOsInstances tblCmpOsInstance = new TblCmpOsInstances();
		tblCmpOsInstance.setCloudId(cloudId);
		tblCmpOsInstance.setUuid(serverId);
		tblCmpOsInstance.setEeStatus(REMOVED);

		return updateInstance(tblCmpOsInstance);
	}

	public Set<String> getInstanceIds(String cloudId)
	{
		return tblCmpOsInstancesMapper.getInstanceIds(cloudId);
	}

	public List<StatusMetrics> getVmInstanceStatusMetrics(String cloudId, String userId, String bpId)
	{
		List<StatusMetrics> statusMetricsList = tblCmpOsInstancesMapper.getVmInstanceStatusMetrics(cloudId, userId, bpId);
		return statusMetricsList;
	}

	public TblCmpOsInstanceExtra getInstanceExtraById(String cloudId, String instanceUuid)
	{
		return tblCmpOsInstanceExtraMapper.selectByPrimaryKey(new TblCmpOsInstanceExtraKey(cloudId, instanceUuid));
	}

	public int insertInstanceExtra(TblCmpOsInstanceExtra tblCmpOsInstanceExtra)
	{
		return tblCmpOsInstanceExtraMapper.insert(tblCmpOsInstanceExtra);
	}

	public List<TblCmpOsInstanceExtra> getInstanceExtras(TblCmpOsInstanceExtraExample example)
	{
		return tblCmpOsInstanceExtraMapper.selectByExample(example);
	}

	public long countInstanceExtrasByExample(TblCmpOsInstanceExtraExample example)
	{
		return tblCmpOsInstanceExtraMapper.countByExample(example);
	}

	public int updateInstanceExtra(TblCmpOsInstanceExtra tblCmpOsInstanceExtra)
	{
		return tblCmpOsInstanceExtraMapper.updateByPrimaryKeySelective(tblCmpOsInstanceExtra);
	}

	public TblCmpOsInstanceFaults getInstanceFaultById(String cloudId, String instanceUuid)
	{
		return tblCmpOsInstanceFaultsMapper.selectByPrimaryKey(new TblCmpOsInstanceFaultsKey(cloudId, instanceUuid));
	}

	public int insertInstanceFault(TblCmpOsInstanceFaults tblCmpOsInstanceFaults)
	{
		return tblCmpOsInstanceFaultsMapper.insert(tblCmpOsInstanceFaults);
	}

	public List<TblCmpOsInstanceFaults> getInstanceFaults(TblCmpOsInstanceFaultsExample example)
	{
		return tblCmpOsInstanceFaultsMapper.selectByExample(example);
	}

	public long countInstanceFaultsByExample(TblCmpOsInstanceFaultsExample example)
	{
		return tblCmpOsInstanceFaultsMapper.countByExample(example);
	}

	public int updateInstanceFault(TblCmpOsInstanceFaults tblCmpOsInstanceFaults)
	{
		return tblCmpOsInstanceFaultsMapper.updateByPrimaryKeySelective(tblCmpOsInstanceFaults);
	}

	public int insertInstanceMetadata(TblCmpOsInstanceMetadata tblCmpOsInstanceMetadata)
	{
		return tblCmpOsInstanceMetadataMapper.insert(tblCmpOsInstanceMetadata);
	}

	public List<TblCmpOsInstanceMetadata> getInstanceMetadatas(TblCmpOsInstanceMetadataExample example)
	{
		return tblCmpOsInstanceMetadataMapper.selectByExample(example);
	}

	public long countInstanceMetadatasByExample(TblCmpOsInstanceMetadataExample example)
	{
		return tblCmpOsInstanceMetadataMapper.countByExample(example);
	}

	public int updateInstanceMetadata(TblCmpOsInstanceMetadata tblCmpOsInstanceMetadata)
	{
		return tblCmpOsInstanceMetadataMapper.updateByPrimaryKeySelective(tblCmpOsInstanceMetadata);
	}

	public TblCmpOsBlockDeviceMapping getBlockDeviceMappingById(String cloudId, String uuid)
	{
		return tblCmpOsBlockDeviceMappingMapper.selectByPrimaryKey(new TblCmpOsBlockDeviceMappingKey(cloudId, uuid));
	}

	public int insertBlockDeviceMapping(TblCmpOsBlockDeviceMapping tblCmpOsBlockDeviceMapping)
	{
		return tblCmpOsBlockDeviceMappingMapper.insert(tblCmpOsBlockDeviceMapping);
	}

	public List<TblCmpOsBlockDeviceMapping> getBlockDeviceMappings(TblCmpOsBlockDeviceMappingExample example)
	{
		return tblCmpOsBlockDeviceMappingMapper.selectByExample(example);
	}

	public long countBlockDeviceMappingsByExample(TblCmpOsBlockDeviceMappingExample example)
	{
		return tblCmpOsBlockDeviceMappingMapper.countByExample(example);
	}

	public int updateBlockDeviceMapping(TblCmpOsBlockDeviceMapping tblCmpOsBlockDeviceMapping)
	{
		return tblCmpOsBlockDeviceMappingMapper.updateByPrimaryKeySelective(tblCmpOsBlockDeviceMapping);
	}

	public TblCmpOsBlockDeviceMapping getBlockDeviceMappingByVolumeId(String cloudId, String volumeId)
	{
		return tblCmpOsBlockDeviceMappingMapper.selectByVolumeId(cloudId, volumeId);
	}

	public int updateByVolumeIdSelective(TblCmpOsBlockDeviceMapping tblCmpOsBlockDeviceMapping)
	{
		return tblCmpOsBlockDeviceMappingMapper.updateByVolumeIdSelective(tblCmpOsBlockDeviceMapping);
	}
}
