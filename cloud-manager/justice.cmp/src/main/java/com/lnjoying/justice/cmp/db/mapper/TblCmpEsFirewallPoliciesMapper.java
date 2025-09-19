package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallPolicies;
import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallPoliciesExample;
import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallPoliciesKey;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

public interface TblCmpEsFirewallPoliciesMapper {
    long countByExample(TblCmpEsFirewallPoliciesExample example);

    int deleteByExample(TblCmpEsFirewallPoliciesExample example);

    int deleteByPrimaryKey(TblCmpEsFirewallPoliciesKey key);

    int insert(TblCmpEsFirewallPolicies record);

    int insertSelective(TblCmpEsFirewallPolicies record);

    List<TblCmpEsFirewallPolicies> selectByExample(TblCmpEsFirewallPoliciesExample example);

    TblCmpEsFirewallPolicies selectByPrimaryKey(TblCmpEsFirewallPoliciesKey key);

    int updateByExampleSelective(@Param("record") TblCmpEsFirewallPolicies record, @Param("example") TblCmpEsFirewallPoliciesExample example);

    int updateByExample(@Param("record") TblCmpEsFirewallPolicies record, @Param("example") TblCmpEsFirewallPoliciesExample example);

    int updateByPrimaryKeySelective(TblCmpEsFirewallPolicies record);

    int updateByPrimaryKey(TblCmpEsFirewallPolicies record);

    Set<String> getFirewallPolicyIds(@Param("cloudId")String cloudId);
}