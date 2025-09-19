package com.lnjoying.justice.cmp.db.repo;

import com.lnjoying.justice.cmp.db.mapper.*;
import com.lnjoying.justice.cmp.db.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class ESNeutronRepository
{
	@Autowired
	private TblCmpEsFirewallsMapper tblCmpEsFirewallsMapper;

	@Autowired
	private TblCmpEsFirewallPoliciesMapper tblCmpEsFirewallPoliciesMapper;

	@Autowired
	private TblCmpEsFirewallRulesMapper tblCmpEsFirewallRulesMapper;

	@Autowired
	private TblCmpEsFirewallBindingsMapper tblCmpEsFirewallBindingsMapper;

	public TblCmpEsFirewalls getFirewallById(String cloudId, String firewallId)
	{
		return tblCmpEsFirewallsMapper.selectByPrimaryKey(new TblCmpEsFirewallsKey(cloudId, firewallId));
	}

	public int insertFirewall(TblCmpEsFirewalls tblCmpEsFirewalls)
	{
		return tblCmpEsFirewallsMapper.insert(tblCmpEsFirewalls);
	}

	public List<TblCmpEsFirewalls> getFirewalls(TblCmpEsFirewallsExample example)
	{
		return tblCmpEsFirewallsMapper.selectByExample(example);
	}

	public long countFirewallsByExample(TblCmpEsFirewallsExample example)
	{
		return tblCmpEsFirewallsMapper.countByExample(example);
	}

	public int updateFirewall(TblCmpEsFirewalls tblCmpEsFirewalls)
	{
		return tblCmpEsFirewallsMapper.updateByPrimaryKeySelective(tblCmpEsFirewalls);
	}

	public int deleteFirewall(String cloudId, String firewallId)
	{
		TblCmpEsFirewalls tblCmpEsFirewall = new TblCmpEsFirewalls();
		tblCmpEsFirewall.setCloudId(cloudId);
		tblCmpEsFirewall.setId(firewallId);
		tblCmpEsFirewall.setEeStatus(REMOVED);

		return updateFirewall(tblCmpEsFirewall);
	}

	public Set<String> getFirewallIds(String cloudId)
	{
		return tblCmpEsFirewallsMapper.getFirewallIds(cloudId);
	}

	public TblCmpEsFirewallPolicies getFirewallPolicyById(String cloudId, String firewallPolicyId)
	{
		return tblCmpEsFirewallPoliciesMapper.selectByPrimaryKey(new TblCmpEsFirewallPoliciesKey(cloudId, firewallPolicyId));
	}

	public int insertFirewallPolicy(TblCmpEsFirewallPolicies tblCmpEsFirewallPolicies)
	{
		return tblCmpEsFirewallPoliciesMapper.insert(tblCmpEsFirewallPolicies);
	}

	public List<TblCmpEsFirewallPolicies> getFirewallPolicies(TblCmpEsFirewallPoliciesExample example)
	{
		return tblCmpEsFirewallPoliciesMapper.selectByExample(example);
	}

	public long countFirewallPoliciesByExample(TblCmpEsFirewallPoliciesExample example)
	{
		return tblCmpEsFirewallPoliciesMapper.countByExample(example);
	}

	public int updateFirewallPolicy(TblCmpEsFirewallPolicies tblCmpEsFirewallPolicies)
	{
		return tblCmpEsFirewallPoliciesMapper.updateByPrimaryKeySelective(tblCmpEsFirewallPolicies);
	}

	public int deleteFirewallPolicy(String cloudId, String firewallPolicyId)
	{
		TblCmpEsFirewallPolicies tblCmpEsFirewallPolicy = new TblCmpEsFirewallPolicies();
		tblCmpEsFirewallPolicy.setCloudId(cloudId);
		tblCmpEsFirewallPolicy.setId(firewallPolicyId);
		tblCmpEsFirewallPolicy.setEeStatus(REMOVED);

		return updateFirewallPolicy(tblCmpEsFirewallPolicy);
	}

	public Set<String> getFirewallPolicyIds(String cloudId)
	{
		return tblCmpEsFirewallPoliciesMapper.getFirewallPolicyIds(cloudId);
	}

	public TblCmpEsFirewallRules getFirewallRuleById(String cloudId, String firewallRuleId)
	{
		return tblCmpEsFirewallRulesMapper.selectByPrimaryKey(new TblCmpEsFirewallRulesKey(cloudId, firewallRuleId));
	}

	public int insertFirewallRule(TblCmpEsFirewallRules tblCmpEsFirewallRules)
	{
		return tblCmpEsFirewallRulesMapper.insert(tblCmpEsFirewallRules);
	}

	public List<TblCmpEsFirewallRules> getFirewallRules(TblCmpEsFirewallRulesExample example)
	{
		return tblCmpEsFirewallRulesMapper.selectByExample(example);
	}

	public long countFirewallRulesByExample(TblCmpEsFirewallRulesExample example)
	{
		return tblCmpEsFirewallRulesMapper.countByExample(example);
	}

	public int updateFirewallRuleSelective(TblCmpEsFirewallRules tblCmpEsFirewallRules)
	{
		return tblCmpEsFirewallRulesMapper.updateByPrimaryKeySelective(tblCmpEsFirewallRules);
	}

	public int updateFirewallRule(TblCmpEsFirewallRules tblCmpEsFirewallRules)
	{
		return tblCmpEsFirewallRulesMapper.updateByPrimaryKey(tblCmpEsFirewallRules);
	}

	public int deleteFirewallRule(String cloudId, String firewallRuleId)
	{
		TblCmpEsFirewallRules tblCmpEsFirewallRule = new TblCmpEsFirewallRules();
		tblCmpEsFirewallRule.setCloudId(cloudId);
		tblCmpEsFirewallRule.setId(firewallRuleId);
		tblCmpEsFirewallRule.setEeStatus(REMOVED);

		return updateFirewallRuleSelective(tblCmpEsFirewallRule);
	}

	public Set<String> getFirewallRuleIds(String cloudId)
	{
		return tblCmpEsFirewallRulesMapper.getFirewallRuleIds(cloudId);
	}

	public TblCmpEsFirewallBindings getFirewallBindingById(String cloudId, String firewallId, String routerId)
	{
		return tblCmpEsFirewallBindingsMapper.selectByPrimaryKey(new TblCmpEsFirewallBindingsKey(cloudId, firewallId, routerId));
	}

	public int insertFirewallBinding(TblCmpEsFirewallBindings tblCmpEsFirewallBindings)
	{
		return tblCmpEsFirewallBindingsMapper.insert(tblCmpEsFirewallBindings);
	}

	public List<TblCmpEsFirewallBindings> getFirewallBindings(TblCmpEsFirewallBindingsExample example)
	{
		return tblCmpEsFirewallBindingsMapper.selectByExample(example);
	}

	public long countFirewallBindingsByExample(TblCmpEsFirewallBindingsExample example)
	{
		return tblCmpEsFirewallBindingsMapper.countByExample(example);
	}

	public int updateFirewallBinding(TblCmpEsFirewallBindings tblCmpEsFirewallBindings)
	{
		return tblCmpEsFirewallBindingsMapper.updateByPrimaryKeySelective(tblCmpEsFirewallBindings);
	}

	public int deleteFirewallBinding(String cloudId, String firewallId, String routerId)
	{
		TblCmpEsFirewallBindings tblCmpEsFirewallBinding = new TblCmpEsFirewallBindings();
		tblCmpEsFirewallBinding.setCloudId(cloudId);
		tblCmpEsFirewallBinding.setFirewallId(firewallId);
		tblCmpEsFirewallBinding.setRouterId(routerId);
		tblCmpEsFirewallBinding.setEeStatus(REMOVED);

		return updateFirewallBinding(tblCmpEsFirewallBinding);
	}

	public Set<String> getFirewallBindingIds(String cloudId, String firewallId)
	{
		return tblCmpEsFirewallBindingsMapper.getFirewallBindingIds(cloudId, firewallId);
	}
}
