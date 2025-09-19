package com.lnjoying.justice.cmp.db.repo;

import com.lnjoying.justice.cmp.db.mapper.*;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.domain.info.baremetal.BaremetalDeviceInfo;
import com.micro.core.common.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class BaremetalComputeRepository
{

	@Autowired
	private TblCmpBaremetalDeviceMapper tblCmpBaremetalDeviceMapper;

	@Autowired
	private TblCmpBaremetalInstanceMapper tblCmpBaremetalInstanceMapper;

	@Autowired
	private TblCmpBaremetalDeviceSpecMapper tblCmpBaremetalDeviceSpecMapper;

	@Autowired
	private TblCmpNicSpecMapper tblCmpNicSpecMapper;

	@Autowired
	private TblCmpNicInfoMapper tblCmpNicInfoMapper;

	@Autowired
	private TblCmpPubkeyMapper tblCmpPubkeyMapper;

	public int insertBaremetalDevice(TblCmpBaremetalDevice tblCmpBaremetalDevice)
	{
		return tblCmpBaremetalDeviceMapper.insert(tblCmpBaremetalDevice);
	}

	public List<TblCmpBaremetalDevice> getBaremetalDevices(TblCmpBaremetalDeviceExample example)
	{
		return tblCmpBaremetalDeviceMapper.selectByExample(example);
	}

	public List<BaremetalDeviceInfo> getBaremetalDeviceInfos(String cloudId, Integer baremetalDevicePhaseStatus, Integer nicSpecPhaseStatus, String userId,
															 Integer cpuNum, Long memTotal, String orderByClause, Integer pageSize, Integer startRow)
	{
		return tblCmpBaremetalDeviceMapper.selectByPhasesUserId(cloudId, baremetalDevicePhaseStatus, nicSpecPhaseStatus, userId, cpuNum, memTotal, orderByClause, pageSize, startRow);
	}

	public long countBaremetalPhasesAndUserId(String cloudId, Integer baremetalDevicePhaseStatus, Integer nicSpecPhaseStatus,
											  String userId, Integer cpuNum, Long memTotal)
	{
		return tblCmpBaremetalDeviceMapper.countByPhasesUserId(cloudId, baremetalDevicePhaseStatus, nicSpecPhaseStatus, userId, cpuNum, memTotal);
	}

	public TblCmpBaremetalDevice getBaremetalDeviceById(String cloudId, String deviceId)
	{
		return tblCmpBaremetalDeviceMapper.selectByPrimaryKey(new TblCmpBaremetalDeviceKey(cloudId, deviceId));
	}

	public long countBaremetalDeviceByExample(TblCmpBaremetalDeviceExample example)
	{
		return tblCmpBaremetalDeviceMapper.countByExample(example);
	}

	public int updateBaremetalDevice(TblCmpBaremetalDevice tblCmpBaremetalDevice)
	{
		return tblCmpBaremetalDeviceMapper.updateByPrimaryKeySelective(tblCmpBaremetalDevice);
	}

	public int updateBaremetalDeviceEeStatus(String cloudId, String deviceId, int eeStatus)
	{
		TblCmpBaremetalDevice tblCmpBaremetalDevice = new TblCmpBaremetalDevice();
		tblCmpBaremetalDevice.setCloudId(cloudId);
		tblCmpBaremetalDevice.setDeviceId(deviceId);
		tblCmpBaremetalDevice.setEeStatus(eeStatus);
		tblCmpBaremetalDevice.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
		return tblCmpBaremetalDeviceMapper.updateByPrimaryKeySelective(tblCmpBaremetalDevice);
	}

	public int updateBaremetalDevicePhaseStatusMacAndDeviceSpecId(String deviceId, int phaseStatus, String mac, String deviceSpecId)
	{
		TblCmpBaremetalDevice tblCmpBaremetalDevice = new TblCmpBaremetalDevice();
		tblCmpBaremetalDevice.setDeviceId(deviceId);
		tblCmpBaremetalDevice.setPhaseStatus(phaseStatus);
		tblCmpBaremetalDevice.setIpmiMac(mac);
		tblCmpBaremetalDevice.setDeviceSpecId(deviceSpecId);
		tblCmpBaremetalDevice.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
		return tblCmpBaremetalDeviceMapper.updateByPrimaryKeySelective(tblCmpBaremetalDevice);
	}

	public int updateBaremetalDevicePhaseStatusAndDeviceIdFromAgent(String deviceId, int phaseStatus, String deviceIdFromAgent)
	{
		TblCmpBaremetalDevice tblCmpBaremetalDevice = new TblCmpBaremetalDevice();
		tblCmpBaremetalDevice.setDeviceId(deviceId);
		tblCmpBaremetalDevice.setPhaseStatus(phaseStatus);
		tblCmpBaremetalDevice.setDeviceIdFromAgent(deviceIdFromAgent);
		tblCmpBaremetalDevice.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
		return tblCmpBaremetalDeviceMapper.updateByPrimaryKeySelective(tblCmpBaremetalDevice);
	}

	public int updateBaremetalDeviceUserId(String deviceId, String userId)
	{
		TblCmpBaremetalDevice tblCmpBaremetalDevice = new TblCmpBaremetalDevice();
		tblCmpBaremetalDevice.setDeviceId(deviceId);
		tblCmpBaremetalDevice.setUserId(userId);
		tblCmpBaremetalDevice.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
		return tblCmpBaremetalDeviceMapper.updateByPrimaryKeySelective(tblCmpBaremetalDevice);
	}

	public Set<String> getBaremetalDeviceIds(String cloudId)
	{
		return tblCmpBaremetalDeviceMapper.getBaremetalDeviceIds(cloudId);
	}

	public List<Integer> getCpuNum(String cloudId)
	{
		return tblCmpBaremetalDeviceSpecMapper.selectCpuNum(cloudId);
	}

	public List<Long> getMemTotal(String cloudId)
	{
		return tblCmpBaremetalDeviceSpecMapper.selectMemTotal(cloudId);
	}

	public int insertBaremetalInstance(TblCmpBaremetalInstance tblCmpBaremetalInstance)
	{
		return tblCmpBaremetalInstanceMapper.insert(tblCmpBaremetalInstance);
	}

	public List<TblCmpBaremetalInstance> getBaremetalInstances(TblCmpBaremetalInstanceExample example)
	{
		return tblCmpBaremetalInstanceMapper.selectByExample(example);
	}

	public TblCmpBaremetalInstance getBaremetalInstanceById(String cloudId, String instanceId)
	{
		return tblCmpBaremetalInstanceMapper.selectByPrimaryKey(new TblCmpBaremetalInstanceKey(cloudId, instanceId));
	}

	public long countBaremetalInstanceByExample(TblCmpBaremetalInstanceExample example)
	{
		return tblCmpBaremetalInstanceMapper.countByExample(example);
	}

	public int updateBaremetalInstanceByPrimaryKeySelective(TblCmpBaremetalInstance tblCmpBaremetalInstance)
	{
		return tblCmpBaremetalInstanceMapper.updateByPrimaryKeySelective(tblCmpBaremetalInstance);
	}

	public int updateBaremetalInstancePhaseStatus(String instanceId, int phaseStatus)
	{
		TblCmpBaremetalInstance tblCmpBaremetalInstance = new TblCmpBaremetalInstance();
		tblCmpBaremetalInstance.setInstanceId(instanceId);
		tblCmpBaremetalInstance.setPhaseStatus(phaseStatus);
		return tblCmpBaremetalInstanceMapper.updateByPrimaryKeySelective(tblCmpBaremetalInstance);
	}

	public int updateBaremetalInstancePhaseStatusAndShareId(String instanceId, int phaseStatus, String shareId)
	{
		TblCmpBaremetalInstance tblCmpBaremetalInstance = new TblCmpBaremetalInstance();
		tblCmpBaremetalInstance.setInstanceId(instanceId);
		tblCmpBaremetalInstance.setPhaseStatus(phaseStatus);
		tblCmpBaremetalInstance.setShareIdFromAgent(shareId);
		return tblCmpBaremetalInstanceMapper.updateByPrimaryKeySelective(tblCmpBaremetalInstance);
	}

	public int updateBaremetalInstancePhaseStatusAndIscsi(String instanceId, int phaseStatus, String iscsiTarget, String iscsiInitiator, String iscsiIpport)
	{
		TblCmpBaremetalInstance tblCmpBaremetalInstance = new TblCmpBaremetalInstance();
		tblCmpBaremetalInstance.setInstanceId(instanceId);
		tblCmpBaremetalInstance.setPhaseStatus(phaseStatus);
		tblCmpBaremetalInstance.setIscsiTarget(iscsiTarget);
		tblCmpBaremetalInstance.setIscsiInitiator(iscsiInitiator);
		tblCmpBaremetalInstance.setIscsiIpport(iscsiIpport);
		return tblCmpBaremetalInstanceMapper.updateByPrimaryKeySelective(tblCmpBaremetalInstance);
	}

	public int updateBaremetalInstancePhaseStatusAndNicId(String instanceId, int phaseStatus, String nicId)
	{
		TblCmpBaremetalInstance tblCmpBaremetalInstance = new TblCmpBaremetalInstance();
		tblCmpBaremetalInstance.setInstanceId(instanceId);
		tblCmpBaremetalInstance.setPhaseStatus(phaseStatus);
		tblCmpBaremetalInstance.setNicIdFromAgent(nicId);
		return tblCmpBaremetalInstanceMapper.updateByPrimaryKeySelective(tblCmpBaremetalInstance);
	}

	public int updateBaremetalInstancePhaseStatusAndPortId(String instanceId, int phaseStatus, String portId)
	{
		TblCmpBaremetalInstance tblCmpBaremetalInstance = new TblCmpBaremetalInstance();
		tblCmpBaremetalInstance.setInstanceId(instanceId);
		tblCmpBaremetalInstance.setPhaseStatus(phaseStatus);
		tblCmpBaremetalInstance.setPortIdFromAgent(portId);
		return tblCmpBaremetalInstanceMapper.updateByPrimaryKeySelective(tblCmpBaremetalInstance);
	}

	public int updateBaremetalInstance(TblCmpBaremetalInstance tblCmpBaremetalInstance)
	{
		return tblCmpBaremetalInstanceMapper.updateByPrimaryKeySelective(tblCmpBaremetalInstance);
	}

	public long countInstanceByPhasesUserId(String cloudId, String userId, Integer baremetalInstancePhaseStatus)
	{
		return tblCmpBaremetalInstanceMapper.countByPhasesUserId(cloudId, userId, baremetalInstancePhaseStatus);
	}

	public Set<String> getBaremetalInstanceIds(String cloudId)
	{
		return tblCmpBaremetalInstanceMapper.getBaremetalInstanceIds(cloudId);
	}

	public List<TblCmpNicInfo> getNicsByDeviceId(String cloudId, String deviceId)
	{
		TblCmpNicInfoExample example = new TblCmpNicInfoExample();
		TblCmpNicInfoExample.Criteria criteria = example.createCriteria();
		criteria.andDeviceIdEqualTo(deviceId);
		criteria.andCloudIdEqualTo(cloudId);

		return tblCmpNicInfoMapper.selectByExample(example);
	}

	public TblCmpPubkey getPubkeyById(String cloudId, String pubkeyId)
	{
		return tblCmpPubkeyMapper.selectByPrimaryKey(new TblCmpPubkeyKey(cloudId, pubkeyId));
	}

	public int insertPubkey(TblCmpPubkey tblPubkey)
	{
		return tblCmpPubkeyMapper.insert(tblPubkey);
	}

	public List<TblCmpPubkey> getPubkeys(TblCmpPubkeyExample example)
	{
		return tblCmpPubkeyMapper.selectByExample(example);
	}

	public long countPubkeyByExample(TblCmpPubkeyExample example)
	{
		return tblCmpPubkeyMapper.countByExample(example);
	}

	public int updatePubkey(TblCmpPubkey pubkey)
	{
		return tblCmpPubkeyMapper.updateByPrimaryKeySelective(pubkey);
	}

	public Set<String> getPubkeyIds(String cloudId)
	{
		return tblCmpPubkeyMapper.getPubkeyIds(cloudId);
	}

	public TblCmpBaremetalDeviceSpec getBaremetalDeviceSpecById(String cloudId, String baremetalDeviceSpecId)
	{
		return tblCmpBaremetalDeviceSpecMapper.selectByPrimaryKey(new TblCmpBaremetalDeviceSpecKey(cloudId, baremetalDeviceSpecId));
	}

	public int insertBaremetalDeviceSpec(TblCmpBaremetalDeviceSpec tblCmpBaremetalDeviceSpec)
	{
		return tblCmpBaremetalDeviceSpecMapper.insert(tblCmpBaremetalDeviceSpec);
	}

	public List<TblCmpBaremetalDeviceSpec> getBaremetalDeviceSpecs(TblCmpBaremetalDeviceSpecExample example)
	{
		return tblCmpBaremetalDeviceSpecMapper.selectByExample(example);
	}

	public long countBaremetalDeviceSpecByExample(TblCmpBaremetalDeviceSpecExample example)
	{
		return tblCmpBaremetalDeviceSpecMapper.countByExample(example);
	}

	public int updateBaremetalDeviceSpec(TblCmpBaremetalDeviceSpec tblCmpBaremetalDeviceSpec)
	{
		return tblCmpBaremetalDeviceSpecMapper.updateByPrimaryKeySelective(tblCmpBaremetalDeviceSpec);
	}

	public TblCmpNicSpec getNicSpecById(String cloudId, String nicId)
	{
		return tblCmpNicSpecMapper.selectByPrimaryKey(new TblCmpNicSpecKey(cloudId, nicId));
	}

	public int insertNicSpec(TblCmpNicSpec tblCmpNicSpec)
	{
		return tblCmpNicSpecMapper.insert(tblCmpNicSpec);
	}

	public List<TblCmpNicSpec> getNicSpecs(TblCmpNicSpecExample example)
	{
		return tblCmpNicSpecMapper.selectByExample(example);
	}

	public long countNicSpecByExample(TblCmpNicSpecExample example)
	{
		return tblCmpNicSpecMapper.countByExample(example);
	}

	public int updateNicSpec(TblCmpNicSpec tblCmpNicSpec)
	{
		return tblCmpNicSpecMapper.updateByPrimaryKeySelective(tblCmpNicSpec);
	}

	public TblCmpNicInfo getNicInfoById(String cloudId, String nicId)
	{
		return tblCmpNicInfoMapper.selectByPrimaryKey(new TblCmpNicInfoKey(cloudId, nicId));
	}

	public int insertNicInfo(TblCmpNicInfo tblCmpNicInfo)
	{
		return tblCmpNicInfoMapper.insert(tblCmpNicInfo);
	}

	public List<TblCmpNicInfo> getNicInfos(TblCmpNicInfoExample example)
	{
		return tblCmpNicInfoMapper.selectByExample(example);
	}

	public long countNicInfoByExample(TblCmpNicInfoExample example)
	{
		return tblCmpNicInfoMapper.countByExample(example);
	}

	public int updateNicInfo(TblCmpNicInfo tblCmpNicInfo)
	{
		return tblCmpNicInfoMapper.updateByPrimaryKeySelective(tblCmpNicInfo);
	}
}
