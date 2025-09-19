package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallBindings;
import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallBindingsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallBindingsKey;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

public interface TblCmpEsFirewallBindingsMapper {
    long countByExample(TblCmpEsFirewallBindingsExample example);

    int deleteByExample(TblCmpEsFirewallBindingsExample example);

    int deleteByPrimaryKey(TblCmpEsFirewallBindingsKey key);

    int insert(TblCmpEsFirewallBindings record);

    int insertSelective(TblCmpEsFirewallBindings record);

    List<TblCmpEsFirewallBindings> selectByExample(TblCmpEsFirewallBindingsExample example);

    TblCmpEsFirewallBindings selectByPrimaryKey(TblCmpEsFirewallBindingsKey key);

    int updateByExampleSelective(@Param("record") TblCmpEsFirewallBindings record, @Param("example") TblCmpEsFirewallBindingsExample example);

    int updateByExample(@Param("record") TblCmpEsFirewallBindings record, @Param("example") TblCmpEsFirewallBindingsExample example);

    int updateByPrimaryKeySelective(TblCmpEsFirewallBindings record);

    int updateByPrimaryKey(TblCmpEsFirewallBindings record);

    Set<String> getFirewallBindingIds(@Param("cloudId")String cloudId, @Param("firewallId")String firewallId);
}