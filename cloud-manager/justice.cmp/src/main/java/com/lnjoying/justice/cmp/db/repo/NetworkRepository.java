package com.lnjoying.justice.cmp.db.repo;

import com.lnjoying.justice.cmp.db.mapper.*;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.SecurityGroupRspVo;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class NetworkRepository
{
	@Autowired
	private TblCmpVpcMapper tblCmpVpcMapper;

	@Autowired
	private TblCmpSubnetMapper tblCmpSubnetMapper;

	@Autowired
	private TblCmpEipMapper tblCmpEipMapper;

	@Autowired
	private TblCmpEipPoolMapper tblCmpEipPoolMapper;

	@Autowired
	private TblCmpEipPoolVpcRefMapper tblCmpEipPoolVpcRefMapper;

	@Autowired
	private TblCmpEipMapMapper tblCmpEipMapMapper;

	@Autowired
	private TblCmpPortMapMapper tblCmpPortMapMapper;

	@Autowired
	private TblCmpPortMapper tblCmpPortMapper;

	@Autowired
	private TblCmpSecurityGroupMapper tblCmpSecurityGroupMapper;

	@Autowired
	private TblCmpSecurityGroupRuleMapper tblSecurityGroupRuleMapper;

	@Autowired
	private TblCmpIpPoolMapper tblIpPoolMapper;

	@Autowired
	private TblCmpSgVmInstanceMapper tblSgVmInstanceMapper;

	public List<TblCmpVpc> getVpcs(TblCmpVpcExample example)
	{
		return tblCmpVpcMapper.selectByExample(example);
	}

	public TblCmpVpc getVpcById(String cloudId, String vpcId)
	{
		return tblCmpVpcMapper.selectByPrimaryKey(new TblCmpVpcKey(cloudId, vpcId));
	}

	public long countVpcByExample(TblCmpVpcExample example)
	{
		return tblCmpVpcMapper.countByExample(example);
	}

	public int createVpc(TblCmpVpc vpc) { return tblCmpVpcMapper.insertSelective(vpc); }

	public int updateVpc(TblCmpVpc vpc) {return tblCmpVpcMapper.updateByPrimaryKeySelective(vpc);}

	public Set<String> getVpcIds(String cloudId)
	{
		return tblCmpVpcMapper.getVpcIds(cloudId);
	}

	public int updateVpcByEeIdSelective(TblCmpVpc vpc) {return tblCmpVpcMapper.updateByEeIdSelective(vpc);}

	public TblCmpVpc getVpcByEeId(String cloudId, String vpcId)
	{
		return tblCmpVpcMapper.selectByEeId(cloudId, vpcId);
	}

	public List<TblCmpSubnet> getSubnets(TblCmpSubnetExample example)
	{
		return tblCmpSubnetMapper.selectByExample(example);
	}

	public TblCmpSubnet getSubnetById(String cloudId, String subnetId)
	{
		return tblCmpSubnetMapper.selectByPrimaryKey(new TblCmpSubnetKey(cloudId, subnetId));
	}

	public long countSubnetByExample(TblCmpSubnetExample example)
	{
		return tblCmpSubnetMapper.countByExample(example);
	}

	public int createSubnet(TblCmpSubnet rsSubnet){return tblCmpSubnetMapper.insertSelective(rsSubnet);}

	public int updateSubnet(TblCmpSubnet rsSubnet) { return tblCmpSubnetMapper.updateByPrimaryKeySelective(rsSubnet); }

	public Set<String> getSubnetIds(String cloudId)
	{
		return tblCmpSubnetMapper.getSubnetIds(cloudId);
	}

	public int updateSubnetByEeIdSelective(TblCmpSubnet subnet) {return tblCmpSubnetMapper.updateByEeIdSelective(subnet);}

	public TblCmpSubnet getSubnetByEeId(String cloudId, String subnetId)
	{
		return tblCmpSubnetMapper.selectByEeId(cloudId, subnetId);
	}

	public List<TblCmpEip> getEips(TblCmpEipExample example){
		return tblCmpEipMapper.selectByExample(example);
	}

	public TblCmpEip getEipById(String cloudId, String eipId)
	{
		return tblCmpEipMapper.selectByPrimaryKey(new TblCmpEipKey(cloudId, eipId));
	}

	public long countEipByExample(TblCmpEipExample example){
		return tblCmpEipMapper.countByExample(example);
	}

	public int createEip(TblCmpEip rsEip){return tblCmpEipMapper.insertSelective(rsEip);}

	public int updateEipByPrimaryKeySelective(TblCmpEip rsEip) { return tblCmpEipMapper.updateByPrimaryKeySelective(rsEip); }

	public List<Integer> getEipProtocolPorts(String cloudId, String userId, String eipId, short protocol)
	{
		return tblCmpEipMapper.selectEipIdProtocolPort(cloudId, userId, eipId, protocol);
	}

	public Set<String> getEipIds(String cloudId)
	{
		return tblCmpEipMapper.getEipIds(cloudId);
	}

	public int updateEipByPrimaryKey(TblCmpEip rsEip) { return tblCmpEipMapper.updateByPrimaryKey(rsEip); }

	public List<TblCmpEipMap> getEipMaps(TblCmpEipMapExample example){
		return tblCmpEipMapMapper.selectByExample(example);
	}

	public TblCmpEipMap getEipMapById(String cloudId, String eipMapId)
	{
		return tblCmpEipMapMapper.selectByPrimaryKey(new TblCmpEipMapKey(cloudId, eipMapId));
	}

	public long countEipMapByExample(TblCmpEipMapExample example){
		return tblCmpEipMapMapper.countByExample(example);
	}

	public int createEipMap(TblCmpEipMap rsEipMap){return tblCmpEipMapMapper.insertSelective(rsEipMap);}

	public int updateEipMap(TblCmpEipMap rsEipMap) { return tblCmpEipMapMapper.updateByPrimaryKeySelective(rsEipMap); }

	public Set<String> getEipMapIds(String cloudId)
	{
		return tblCmpEipMapMapper.getEipMapIds(cloudId);
	}

	public List<TblCmpPortMap> getPortMaps(TblCmpPortMapExample example){
		return tblCmpPortMapMapper.selectByExample(example);
	}

	public long countPortMapByExample(TblCmpPortMapExample example){
		return tblCmpPortMapMapper.countByExample(example);
	}

	public int createPortMap(TblCmpPortMap rsPortMap){return tblCmpPortMapMapper.insertSelective(rsPortMap);}

	public int updatePortMap(TblCmpPortMap rsPortMap) { return tblCmpPortMapMapper.updateByPrimaryKeySelective(rsPortMap); }

	public int updatePortMaps(TblCmpPortMapExample example, short status){
		TblCmpPortMap portMap = new TblCmpPortMap();
		portMap.setUpdateTime(new Date(System.currentTimeMillis()));
		portMap.setStatus(status);

		return tblCmpPortMapMapper.updateByExampleSelective(portMap,example);
	}

	public Set<String> getPortMapIds(String cloudId, String eipMapId)
	{
		return tblCmpPortMapMapper.getPortMapIds(cloudId, eipMapId);
	}

	public List<TblCmpPort> getPorts(TblCmpPortExample example)
	{
		return tblCmpPortMapper.selectByExample(example);
	}

	public TblCmpPort getPortById(String cloudId, String portId)
	{
		return tblCmpPortMapper.selectByPrimaryKey(new TblCmpPortKey(cloudId, portId));
	}

	public long countPortByExample(TblCmpPortExample example)
	{
		return tblCmpPortMapper.countByExample(example);
	}

	public int createPort(TblCmpPort rsPort) { return tblCmpPortMapper.insertSelective(rsPort); }

	public int updatePort(TblCmpPort rsPort) {return tblCmpPortMapper.updateByPrimaryKey(rsPort);}

	public int updatePortSelective(TblCmpPort rsPort) {return tblCmpPortMapper.updateByPrimaryKeySelective(rsPort);}

	public int updatePortByExample(TblCmpPort tblCmpRsPort, TblCmpPortExample example) {
		return tblCmpPortMapper.updateByExampleSelective(tblCmpRsPort, example);
	}

	public TblCmpPort getPortByVmInstanceId(String cloudId, String vmInstanceId)
	{
		TblCmpPortExample example = new TblCmpPortExample();
		TblCmpPortExample.Criteria criteria = example.createCriteria();
		criteria.andCloudIdEqualTo(cloudId);
		criteria.andEeStatusNotEqualTo(REMOVED);
		criteria.andInstanceIdEqualTo(vmInstanceId);
		List<TblCmpPort> tblCmpPorts = getPorts(example);
		return CollectionUtils.isEmpty(tblCmpPorts) ? null : tblCmpPorts.get(0);
	}

	public List<TblCmpSecurityGroup> getSecurityGroups(TblCmpSecurityGroupExample example)
	{
		return tblCmpSecurityGroupMapper.selectByExample(example);
	}

	public TblCmpSecurityGroup getSecurityGroupById(String cloudId, String securityGroupId)
	{
		return tblCmpSecurityGroupMapper.selectByPrimaryKey(new TblCmpSecurityGroupKey(cloudId, securityGroupId));
	}

	public long countSecurityGroupByExample(TblCmpSecurityGroupExample example)
	{
		return tblCmpSecurityGroupMapper.countByExample(example);
	}

	public int createSecurityGroup(TblCmpSecurityGroup tblCmpSecurityGroup) { return tblCmpSecurityGroupMapper.insertSelective(tblCmpSecurityGroup); }

	public int updateSecurityGroup(TblCmpSecurityGroup tblCmpSecurityGroup) {return tblCmpSecurityGroupMapper.updateByPrimaryKeySelective(tblCmpSecurityGroup);}

	public List<SecurityGroupRspVo> getSecurityGroups(String cloudId, String userId, String name, String sgId, int phaseStatus, boolean phaseStatusIsEqual, Integer pageSize, Integer startRow)
	{
		return tblCmpSecurityGroupMapper.selectByUserId(cloudId, userId, name, sgId, phaseStatus, phaseStatusIsEqual, pageSize, startRow);
	}

	public long countSecurityGroupBySearch(String cloudId, String userId, String name, String sgId, int phaseStatus, boolean phaseStatusIsEqual)
	{
		return tblCmpSecurityGroupMapper.countByUserId(cloudId, userId,name,sgId,phaseStatus, phaseStatusIsEqual);
	}

	public Set<String> getSgIds(String cloudId)
	{
		return tblCmpSecurityGroupMapper.getSgIds(cloudId);
	}

	public int updateSecurityGroupByEeIdSelective(TblCmpSecurityGroup tblCmpSecurityGroup) {return tblCmpSecurityGroupMapper.updateByEeIdSelective(tblCmpSecurityGroup);}

	public TblCmpSecurityGroup getSecurityGroupByEeId(String cloudId, String securityGroupId)
	{
		return tblCmpSecurityGroupMapper.selectByEeId(cloudId, securityGroupId);
	}

	public List<TblCmpSecurityGroupRule> getSecurityGroupRules(TblCmpSecurityGroupRuleExample example)
	{
		return tblSecurityGroupRuleMapper.selectByExample(example);
	}

	public TblCmpSecurityGroupRule getSecurityGroupRuleById(String cloudId, String securityGroupRuleId)
	{
		return tblSecurityGroupRuleMapper.selectByPrimaryKey(new TblCmpSecurityGroupRuleKey(cloudId, securityGroupRuleId));
	}

	public long countSecurityGroupRuleByExample(TblCmpSecurityGroupRuleExample example)
	{
		return tblSecurityGroupRuleMapper.countByExample(example);
	}

	public int createSecurityGroupRule(TblCmpSecurityGroupRule tblCmpSecurityGroupRule)
	{
		return tblSecurityGroupRuleMapper.insertSelective(tblCmpSecurityGroupRule);
	}

	public int updateSecurityGroupRuleSelective(TblCmpSecurityGroupRule tblCmpSecurityGroupRule)
	{
		return tblSecurityGroupRuleMapper.updateByPrimaryKeySelective(tblCmpSecurityGroupRule);
	}

	public int updateSecurityGroupRule(TblCmpSecurityGroupRule tblCmpSecurityGroupRule)
	{
		return tblSecurityGroupRuleMapper.updateByPrimaryKey(tblCmpSecurityGroupRule);
	}

	public int updateSecurityGroupRules(TblCmpSecurityGroupRule tblCmpSecurityGroupRule, TblCmpSecurityGroupRuleExample tblCmpSecurityGroupRuleExample){
		return tblSecurityGroupRuleMapper.updateByExampleSelective(tblCmpSecurityGroupRule, tblCmpSecurityGroupRuleExample);
	}

	public List<TblCmpIpPool> getIpPools(TblCmpIpPoolExample example)
	{
		return tblIpPoolMapper.selectByExample(example);
	}

	public long countIpPoolByExample(TblCmpIpPoolExample example)
	{
		return tblIpPoolMapper.countByExample(example);
	}

	public int createIpPool(TblCmpIpPool tblCmpIpPool) { return tblIpPoolMapper.insertSelective(tblCmpIpPool); }

	public int updateIpPool(TblCmpIpPool tblCmpIpPool) {return tblIpPoolMapper.updateByPrimaryKeySelective(tblCmpIpPool);}

	public int createSgVmInstance(TblCmpSgVmInstance tblCmpSgVmInstance)
	{
		return tblSgVmInstanceMapper.insertSelective(tblCmpSgVmInstance);
	}
	public long countSgVmInstancesByExample(TblCmpSgVmInstanceExample example)
	{
		return tblSgVmInstanceMapper.countByExample(example);
	}
	public List<TblCmpSgVmInstance> getSgVmInstances(TblCmpSgVmInstanceExample example)
	{
		return tblSgVmInstanceMapper.selectByExample(example);
	}
	public TblCmpSgVmInstance getSgVmInstancesById(String cloudId, String sgVmId)
	{
		return tblSgVmInstanceMapper.selectByPrimaryKey(new TblCmpSgVmInstanceKey(cloudId, sgVmId));
	}
	public int updateSgVmInstanceSelective(TblCmpSgVmInstance tblCmpSgVmInstance)
	{
		return tblSgVmInstanceMapper.updateByPrimaryKeySelective(tblCmpSgVmInstance);
	}
	public int updateSgVmInstance(TblCmpSgVmInstance tblCmpSgVmInstance)
	{
		return tblSgVmInstanceMapper.updateByPrimaryKey(tblCmpSgVmInstance);
	}
	public int updateSgVmInstanceByExample(TblCmpSgVmInstance tblCmpSgVmInstance, TblCmpSgVmInstanceExample example)
	{
		return tblSgVmInstanceMapper.updateByExampleSelective(tblCmpSgVmInstance,example);
	}
	public Set<String> getSgVmInstanceIds(String cloudId, String vmInstanceId)
	{
		return tblSgVmInstanceMapper.getSgVmInstanceIds(cloudId, vmInstanceId);
	}
	public Set<String> getSgVmInstanceIdsBySgIds(String cloudId, String vmInstanceId)
	{
		return tblSgVmInstanceMapper.getSgVmInstanceIdsBySgIds(cloudId, vmInstanceId);
	}
	public int deleteSgVmInstanceByPrimaryKey(TblCmpSgVmInstanceKey key)
	{
		return tblSgVmInstanceMapper.deleteByPrimaryKey(key);
	}

	public List<TblCmpEipPool> getEipPools(TblCmpEipPoolExample example){
		return tblCmpEipPoolMapper.selectByExample(example);
	}

	public TblCmpEipPool getEipPoolById(String cloudId, String eipPoolId)
	{
		return tblCmpEipPoolMapper.selectByPrimaryKey(new TblCmpEipPoolKey(cloudId, eipPoolId));
	}

	public long countEipPoolByExample(TblCmpEipPoolExample example){
		return tblCmpEipPoolMapper.countByExample(example);
	}

	public int createEipPool(TblCmpEipPool tblCmpEipPool){return tblCmpEipPoolMapper.insertSelective(tblCmpEipPool);}

	public int updateEipPool(TblCmpEipPool tblCmpEipPool) { return tblCmpEipPoolMapper.updateByPrimaryKeySelective(tblCmpEipPool); }

	public Set<String> getPoolIds(String cloudId)
	{
		return tblCmpEipPoolMapper.getPoolIds(cloudId);
	}

	public List<TblCmpEipPoolVpcRef> getEipPoolAndVpcRefs(TblCmpEipPoolVpcRefExample example){
		return tblCmpEipPoolVpcRefMapper.selectByExample(example);
	}

	public TblCmpEipPoolVpcRef getEipPoolAndVpcRefById(String cloudId, String eipPoolVpcId)
	{
		return tblCmpEipPoolVpcRefMapper.selectByPrimaryKey(new TblCmpEipPoolVpcRefKey(cloudId, eipPoolVpcId));
	}

	public long countEipPoolAndVpcRefByExample(TblCmpEipPoolVpcRefExample example)
	{
		return tblCmpEipPoolVpcRefMapper.countByExample(example);
	}

	public int createEipPoolAndVpcRef(TblCmpEipPoolVpcRef tblCmpEipPoolVpcRef){return tblCmpEipPoolVpcRefMapper.insertSelective(tblCmpEipPoolVpcRef);}

	public int updateEipPoolAndVpcRef(TblCmpEipPoolVpcRef tblCmpEipPoolVpcRef) { return tblCmpEipPoolVpcRefMapper.updateByPrimaryKeySelective(tblCmpEipPoolVpcRef); }

	public int updateEipPoolAndVpcRefByExample(TblCmpEipPoolVpcRef tblCmpEipPoolVpcRef, TblCmpEipPoolVpcRefExample example)
	{
		return tblCmpEipPoolVpcRefMapper.updateByExampleSelective(tblCmpEipPoolVpcRef, example);
	}
}
