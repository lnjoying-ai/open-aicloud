package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallRules;
import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallRulesExample;
import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallRulesKey;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

public interface TblCmpEsFirewallRulesMapper {
    long countByExample(TblCmpEsFirewallRulesExample example);

    int deleteByExample(TblCmpEsFirewallRulesExample example);

    int deleteByPrimaryKey(TblCmpEsFirewallRulesKey key);

    int insert(TblCmpEsFirewallRules record);

    int insertSelective(TblCmpEsFirewallRules record);

    List<TblCmpEsFirewallRules> selectByExample(TblCmpEsFirewallRulesExample example);

    TblCmpEsFirewallRules selectByPrimaryKey(TblCmpEsFirewallRulesKey key);

    int updateByExampleSelective(@Param("record") TblCmpEsFirewallRules record, @Param("example") TblCmpEsFirewallRulesExample example);

    int updateByExample(@Param("record") TblCmpEsFirewallRules record, @Param("example") TblCmpEsFirewallRulesExample example);

    int updateByPrimaryKeySelective(TblCmpEsFirewallRules record);

    int updateByPrimaryKey(TblCmpEsFirewallRules record);

    Set<String> getFirewallRuleIds(@Param("cloudId")String cloudId);
}